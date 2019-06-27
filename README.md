# VRP-Client
Trasporto pubblico nella città di Roma: un approccio metaeuristico all'ottimizzazione delle rotte degli autobus

Istruzioni per importare il progetto client nell'ambiente di sviluppo:

- Creare un nuovo progetto Java denominato "VRP-Client"
- Scaricare il file .zip di questa Github repository ed importarlo nel progetto appena creato
- Assicurarsi che la cartella "src" sia riconosciuta "source folder" (da usare per i file sorgente)
- Assicurarsi che "client" e "gui" (all'interno di "src") siano importati come package e che sia presente il file module-info.java
- Assicurarsi che la cartella "data" sia presente nel class path del progetto (configurabile tramite il build path)
- Scaricare la libreria JavaFX 12.0.1 SDK specifica per il sistema operativo in uso al seguente URL: https://gluonhq.com/products/javafx/ 
- Estrarla ed aggiungere i file .jar (path: /lib) in una nuova libreria utente "JavaFX12" (senza modificare i percorsi delle sottocartelle sul disco fisso e/o spostare gli altri file di sistema)
- Includere tale libreria "JavaFX12" nel module path del progetto (per sicurezza effettuare un cleaning del progetto al termine delle precedenti operazioni)
- Configurare opportunamente il Build Path del progetto scegliendo una versione JDK 11+ per assicurarsi che il programma funzioni
- Eseguire la classe "Main", all'interno del package "gui" (percorso: gui.Main)

Istruzioni per eseguire il runnable .jar:

- Scaricare il file .jar eseguibile specifico per la piattaforma in uso:
- Linux: https://drive.google.com/open?id=13FWwnMTafFvW_bUWo6XmiOoT5dzD07UN
- Mac: https://drive.google.com/open?id=1TLNRorvu9CiymW9KWKgT5bDY4Xl4o1r4
- Windows: https://drive.google.com/open?id=1oXoYaBK1iAI9QHJ9E5t69OeSOkYMIcrQ
- Estrarre la cartella "bin" dalla radice del file .jar appena scaricato e posizionarne tutti i file .so/.dylib/.dll all'interno della cartella "bin" relativa invece al percorso della JVM in uso (JDK 11+)
- Assicurarsi che il file .jar sia eseguito dall'applicazione javaw (all'interno della cartella "bin" relativa alla JVM in uso)
- Eseguire il programma VRP-Client_[SO-Name].jar mediante doppio click del mouse (oppure da linea di comando utilizzando la seguente sintassi: java -jar nomeFile.jar)

Note: per testare una linea bisogna scrivere semplicemente il numero corrispondente nel campo di testo e cliccare sul bottone desiderato; sono supportate le seguenti linee: 23, 30, 60, 64, 160, 218, 649, 671, 714, 716; i relativi file di testo sono memorizzati nella cartella "data/buses/" del file VRP-Client_[SO-Name].jar

![racoMaps1](https://user-images.githubusercontent.com/51203516/60122532-aec37380-9785-11e9-83d0-aeec4fc784d1.png)
![racoMaps2](https://user-images.githubusercontent.com/51203516/60122546-b420be00-9785-11e9-9d13-e20d83dba4e9.png)
![racoMaps3](https://user-images.githubusercontent.com/51203516/60122553-bb47cc00-9785-11e9-84c1-fdaeb0290565.png)
![racoMaps4](https://user-images.githubusercontent.com/51203516/60122560-c13dad00-9785-11e9-9f21-65b27fd9a5a8.png)
