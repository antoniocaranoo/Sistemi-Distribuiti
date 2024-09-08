# Progetto Sistemi Distribuiti 2023-2024 - API REST

Documentazione delle API REST del progetto 2023/2024, fornite dal server web. Ogni risorsa (dominio, ordine, utente) fornita dal server ha un path dedicato.

## `/domains`

### GET

**Descrizione**: Recupera tutti i domini dal database. Questo metodo invia una richiesta al database per ottenere la lista di tutti i domini registrati e restituisce la lista in formato JSON.

**Parametri**: nessuno.

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene una lista di domini in formato JSON.

**Codici di stato restituiti**:
* 200 OK : La richiesta è stata completata con successo. Restituisce la lista dei domini in formato JSON.
* 404 Not Found: Nessun dominio trovato nel database.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/domains/{id} `

### GET

**Descrizione**: Recupera un dominio specifico in base al suo ID. Questo metodo invia una richiesta al database per ottenere le informazioni su un dominio specifico identificato dal suo ID e restituisce i dettagli del dominio in formato JSON.

**Parametri**: `id`: ID dell'utente (tipo int).

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene i dettagli del dominio richiesto in formato JSON.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce i dettagli del dominio in fromato JSON.
* 404 Not Found: Il dominio specificato non è stato trovato nel database.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/domains/{nome}/{tld}`

### GET

**Descrizione**: Recupera un dominio specifico in base al nome e al TLD (Top-Level Domain). Questo metodo invia una richiesta al database per ottenere le informazioni su un dominio specifico e restituisce i dettagli del dominio in formato JSON.

**Parametri**: `nome`: Nome del dominio (tipo String).
`tld`: TLD del dominio (tipo String).

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene i dettagli del dominio richiesto in formato JSON.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce i dettagli del dominio in formato JSON.
* 404 Not Found: Il dominio specificato non è stato trovato nel database.
* 409 Conflict: Il dominio è già in fase di registrazione.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/domains/users/{id}`

### GET

**Descrizione**: Recupera tutti i domini associati a un utente specifico. Questo metodo invia una richiesta al database per ottenere la lista di domini registrati da un utente specifico identificato dal suo ID.

**Parametri**: `id`: ID dell'utente (tipo int).

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene una lista di domini associati all'utente specificato in formato JSON.

**Codici di stato restituiti**:
* 200 OK : La richiesta è stata completata con successo. Restituisce la lista dei domini dell'utente.
* 404 Not Found: Nessun dominio trovato per l'utente specificato.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/domains `

### POST

**Descrizione**: Aggiunge un nuovo dominio al database. Questo metodo riceve i dettagli di un dominio in formato JSON, li converte in una stringa JSON, invia la stringa al database per l'inserimento, e restituisce l'ID del dominio aggiunto.

**Parametri**: `dominio`: Oggetto `Dominio` contenente i dettagli del dominio da aggiungere.

**Header**: nessuno.

**Body richiesta**: Il corpo della richiesta deve contenere un oggetto Dominio in formato JSON.

**Risposta**: In caso di successo, la risorsa creata è indicata nell'header location.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce l'ID del dominio aggiunto in formato JSON.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o un errore interno del server.

## `/domains`

### PUT

**Descrizione**: Rinnova un dominio esistente nel database. Questo metodo riceve i dettagli di un dominio in formato JSON, li converte in una stringa JSON, invia la stringa al database per il rinnovo del dominio e restituisce l'ID del dominio rinnovato.

**Parametri**: `dominio`: Oggetto `Dominio` contenente i dettagli del dominio da rinnovare.

**Header**: nessuno.

**Body richiesta**: Il corpo della richiesta deve contenere un oggetto `Dominio` in formato JSON.

**Risposta**: In caso di successo, la risposta contiene l'ID del dominio rinnovato.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce l'ID del dominio rinnovato.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o un errore interno del server.

## `/users `

### GET

**Descrizione**: Recupera tutti gli utenti dal database. Questo metodo invia una richiesta al database per ottenere la lista di tutti gli utenti registrati e restituisce la lista in formato JSON.

**Parametri**: nessuno.

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene una lista di utenti in formato JSON.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce la lista degli utenti in formato JSON.
* 404 Not Found: Nessun utente trovato nel database.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/users/{id} `

### GET

**Descrizione**: Recupera un utente specifico in base al suo ID. Questo metodo invia una richiesta al database per ottenere le informazioni su un utente specifico e restituisce i dettagli dell'utente in formato JSON.

**Parametri**: `id`: ID dell'utente (tipo int).

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene i dettagli dell'utente richiesto in formato JSON.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce i dettagli dell'utente in formato JSON.
* 404 Not Found: L'utente specificato non è stato trovato nel database.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/users`

### POST

**Descrizione**: Aggiunge un nuovo utente al database. Questo metodo riceve i dettagli di un utente in formato JSON, li converte in una stringa JSON, invia la stringa al database per l'inserimento, e restituisce una risposta che conferma l'aggiunta dell'utente.

**Parametri**: `utente`: Oggetto `Utente` contenente i dettagli dell'utente da aggiungere.

**Header**: nessuno.

**Body richiesta**: Il corpo della richiesta deve contenere un oggetto `Utente` in formato JSON.

**Risposta**: In caso di successo, la risorsa creata è indicata nell'header location.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce una conferma dell'aggiunta dell'utente.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o un errore interno del server.


## `/orders`

### GET

**Descrizione**: Recupera tutti gli ordini dal database. Questo metodo invia una richiesta al database per ottenere la lista di tutti gli ordini registrati e restituisce la lista in formato JSON.

**Parametri**: nessuno.

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene una lista di ordini in formato JSON.

**Codici di stato restituiti**: 
* 200 OK: La richiesta è stata completata con successo. Restituisce la lista degli ordini in formato JSON.
* 404 Not Found: Nessun ordine trovato nel database.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/orders/{id} `

### GET

**Descrizione**: Recupera tutti gli ordini di un utente specifico in base al suo ID. Questo metodo invia una richiesta al database per ottenere la lista degli ordini dell'utente specificato e restituisce gli ordini in formato JSON.

**Parametri**: `id`: ID dell'utente (tipo int).

**Header**: nessuno.

**Body richiesta**: nulla, body non previsto.

**Risposta**: In caso di successo, la risposta contiene una lista di ordini dell'utente specificato in formato JSON.

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce la lista degli ordini dell'utente in formato JSON.
* 404 Not Found: Nessun ordine trovato per l'utente specificato.
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o durante il parsing del JSON.

## `/orders `

### POST

**Descrizione**: Aggiunge un nuovo ordine al database. Questo metodo riceve i dettagli di un ordine in formato JSON, li converte in una stringa JSON, invia la stringa al database per l'inserimento, e restituisce l'ID del dominio associato all'ordine aggiunto.

**Parametri**: `ordine`: Oggetto `Ordine` contenente i dettagli dell'ordine da aggiungere.

**Header**: nessuno.

**Body richiesta**: Il corpo della richiesta deve contenere un oggetto `Ordine` in formato JSON.

**Risposta**: In caso di successo, la risorsa creata è indicata nell'header location .

**Codici di stato restituiti**:
* 200 OK: La richiesta è stata completata con successo. Restituisce l'ID del dominio associato all'ordine aggiunto (in formato JSON).
* 500 Internal Server Error: Si è verificato un errore durante la comunicazione con il database o un errore interno del server.
