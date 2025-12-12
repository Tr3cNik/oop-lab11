# oop-lab11 - Teoria

## Workflow dei DVSC

Nell'utilizzo di un DVSC non c'è un effettivo standard per la gestione del workflow quando usato in gruppo.
Si sceglie la tipologia del workflow in base al team, la semplicità e le esigenze:

* Bisogna fare differenza tra progetti piccoli e grandi
* Bisogna fare differenza tra team sparso e team di fiducia



In progetti piccoli con team sparso si utilizza un singolo branch con multiple fork e pull request.

In progetti piccoli con team coeso si utilizza un singolo branch con singola repo dove tutti hanno diritto di push.

In progetti grandi con team sparso si utilizzano più branch con multiple fork e pull request.

In progetti grandi con team coeso si utilizzano più branch con singola repo dove tutti hanno diritto di push.



Per lavorare con fork e pull request si fa come segue (utile da sapere per progetti open source):

* Un singolo maintainer crea la repo, ed è l'unico con diritto di scrittura
* Gli altri membri hanno una fork a testa dove lavorano
* I membri fanno pull dal repo centrale e push sulla propria fork
* Quando una feature è completa nella propria fork, si apre una pull request
* Il maintainer revisiona il codice finché non è soddisfatto e accetta la pull request facendo il merge del codice nella repo centrale



Per lavorare su più branch solitamente si utilizza un "git-flow", cioè un modello convenzionale per la nomenclatura e gestione dei vari branch di una repo.



In un modello semplificato, si mantiene un branch principale sempre compilabile e funzionante e ad esso vengono affiancati dei "feature branch" diversi per sviluppare nuove funzionalità.
Quest'ultimi si dovrebbero sincronizzare con il branch principale frequentemente per evitare dei conflict.



## Monitoraggio e Profiling con VisualVM

Per analizzare le performance di un'applicazione si possono approcciare 2 metodi:

* top-down: dall'alto livello a scendere
* bottom-up: dal basso livello a risalire



Dall'analisi delle performance bisogna distinguere il monitoraggio dal profiling:

* Il monitoraggio osserva e raccoglie i dati non invasivi di un'applicazione in esecuzione
* Il profiling osserva e raccoglie i dati anche invasivi di un'applicazione in esecuzione



Il profiling può raccogliere il tempo d'esecuzione dei metodi (method profiling) e l'utilizzo di memoria da parte delle applicazioni (memory profiling).



Questi 2 aspetti fanno parte del performance testing (effettuabile automaticamente con dei benchmark), opportuno da eseguire specialmente su applicazioni complesse che presentano anche meccanismi di concorrenza.

Tra gli strumenti che permettono tale testing delle applicazioni eseguite sulla JVM:

* `JConsole`
* `JVisualVM`



JVisualVM è un profiler per applicazioni Java che consente di misurare e analizzare le performance.
Si richiama con il comando `jvisualvm` o `visualvm` e fornisce vari plugin per analisi specifiche.

JVisualVM ha le seguenti schede di monitoraggio:

* Applications
* Monitor
* Threads
* Sampler
* Profiler
