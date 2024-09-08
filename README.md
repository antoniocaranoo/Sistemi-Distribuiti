# Progetto Sistemi Distribuiti 2023-2024

il sito RicorsivaMente Domains permette agli utenti di acquistare e rinnovare i domini internet disponibili

## Componenti del gruppo

* Davide Baiardi 894430 <d.baiardi1@campus.unimib.it>
* Antonio Carano 902447 <a.carano1@campus.unimib.it>
* Camilla Cantaluppi 894557 <c.cantaluppi6@campus.unimib.it>

## Compilazione ed esecuzione

Sia il server Web sia il database sono applicazioni Java gestire con Maven. All'interno delle rispettive cartelle si può trovare il file `pom.xml` in cui è presenta la configurazione di Maven per il progetto. Si presuppone l'utilizzo della macchina virtuale di laboratorio, per cui nel `pom.xml` è specificato l'uso di Java 21.

Il server Web e il database sono dei progetti Java che utilizano Maven per gestire le dipendenze, la compilazione e l'esecuzione.

### Client Web

Per avviare il client Web è necessario utilizzare l'estensione "Live Preview" su Visual Studio Code, come mostrato durante il laboratorio. Tale estensione espone un server locale con i file contenuti nella cartella `client-web`.

L'alternativa consigliata è quella di accedere a 'localhost:8080' dal browser Google Chrome, verrà aperta la pagina index.html

**Attenzione**: è necessario configurare CORS in Google Chrome come mostrato nel laboratorio.

il client-web consiste in due file html:
* index: gestisce l'accesso e la registrazione degli utenti e mostra i domini già registrati (sono collegati i relativi file css per lo stile e i metodi JavaScript)
* dashboard: gestisce l'acquisto, il rinnovo e mostra i domini e gli ordini effettuati dall' utente con cui si è fatto l'accesso (sono collegati i relativi file css per lo stile e i metodi JavaScript)


### Server Web

Il server Web utilizza Jetty e Jersey. Si può avviare eseguendo `mvn jetty:run` all'interno della cartella `server-web`. Espone le API REST all'indirizzo `localhost` alla porta `8080`.

il server-web è formato dai seguent file:
* Utente: rappresenta un modello di dati per un utente con attributi di base come ID, nome, cognome e email
* Ordine: rappresenta un modello di dati per gestire le informazioni relative a un ordine
* Dominio: rappresenta un modello di dati per un dominio internet, includendo informazioni come l'ID, nome, TLD (Top-Level Domain), date di registrazione e scadenza, stato, e il proprietario del dominio.
* DominiResource: gestisce le operazioni CRUD per la risorsa "domains" attraverso un'interfaccia RESTful usando JAX-RS
* OrdiniResource: gestisce le operazioni CRUD per la risorsa "orders" attraverso un'interfaccia RESTful usando JAX-RS
* UtentiResource: gestisce le operazioni CRUD per la risorsa "users" attraverso un'interfaccia RESTful usando JAX-RS
* JsonException: implementa la gestione delle eccezioni per la deserializzazione JSON in un'API RESTful utilizzando JAX-RS (Java API for RESTful Web Services)
* JsonParsingException: è un gestore di eccezioni personalizzato per un'API RESTful utilizzando JAX-RS. Essa mappa le eccezioni di tipo ProcessingException, specificatamente per problemi di deserializzazione JSON, a risposte HTTP con uno stato 400 (Bad Request). Questo permette di fornire risposte più informative ai client quando si verificano errori di elaborazione JSON.


### Database

Il database è una semplice applicazione Java. Si possono utilizzare i seguenti comandi Maven:

* `mvn clean`: per ripulire la cartella dai file temporanei,
* `mvn compile`: per compilare l'applicazione,
* `mvn exec:java`: per avviare l'applicazione (presuppone che la classe principale sia `Main.java`). Si pone in ascolto all'indirizzo `localhost` alla porta `3030`.

il database è formato da tre file json che contengono rispettivamente tutte le istanze dei:
* utenti
* domini
* ordini

e da classi che gestiscono la logica del database:
* Main: è la classe principale che avvia un server di database e gestisce le connessioni dei client
* Handler: gestisce le connessioni dei client a un server database e implementa l'interfaccia Runnable, consentendo la gestione delle richieste dei client in thread separati. Ogni istanza della classe è responsabile di una singola connessione del client
* Database: offre una gestione centralizzata dei dati per utenti, domini e ordini, con metodi per leggere, aggiungere e aggiornare i dati nel formato JSON. Utilizza il pattern Singleton per garantire una singola istanza condivisa, sincronizzando l'accesso ai metodi di modifica per evitare problemi di concorrenza
