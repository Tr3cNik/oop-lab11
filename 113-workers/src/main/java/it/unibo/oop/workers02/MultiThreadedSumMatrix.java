package it.unibo.oop.workers02;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * Calculation with stream of a matrix sum.
 */
public final class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    /**
     * Builds a multithreaded matrix sum using streams.
     *
     * @param nthread
     *            no. of thread performing the sum.
     */
    public MultiThreadedSumMatrix(final int nthread) {
        this.nthread = nthread;
    }

    @Override
    public double sum(final double[][] matrix) {
        final int threadRows = matrix.length % nthread + matrix.length / nthread;
        return DoubleStream.iterate(0, start -> start + threadRows)
            .limit(nthread)
            .mapToObj(start -> new Worker(matrix, (int) start, threadRows))
            .peek(Thread::start)
            .peek(MultiThreadedSumMatrix::joinUninterruptibly)
            .mapToDouble(Worker::getResult)
            .sum();
    }

    @SuppressWarnings("PMD.AvoidPrintStackTrace")
    private static void joinUninterruptibly(final Thread target) {
        var joined = false;
        while (!joined) {
            try {
                target.join();
                joined = true;
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private double res;

        /**
         * Build a new worker.
         *
         * @param list
         *            the matrix to sum
         * @param startpos
         *            the initial position for this worker
         * @param nelem
         *            the no. of elems to sum up for this worker
         */
        Worker(final double[][] list, final int startpos, final int nelem) {
            super();
            this.matrix = list; // NOPMD
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public synchronized void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            this.res = Stream.of(this.matrix)
                .skip(startpos)
                .limit(nelem)
                .flatMapToDouble(DoubleStream::of)
                .sum();
        }

        /**
         * Returns the result of summing up the integers within the matrix.
         *
         * @return the sum of every element in the matrix
         */
        public synchronized double getResult() {
            return this.res;
        }

    }
}
