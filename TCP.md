## Protocollo di comunicazione tra database e server Web

Il protocollo di comunicazione tra database e server Web deve essere costruito considerando le richieste del sistema. Si suggerisce di implementare un insieme limitato di comandi e di trasmettere dati in forma testuale.

### Comandi Implementati

I seguenti comandi TCP sono utilizzati per la comunicazione tra il server Web e il database:

#### DominiResource

1. **Recupera tutti i domini dal database**
   - **Comando**: `GET/ID/all/DOMAIN\n`
   - **Metodo**: `getAllDomains()`

2. **Recupera tutti i domini associati a un utente specifico**
   - **Comando**: `GET/user/{id}/DOMAIN\n`
   - **Metodo**: `getUsersDomains(@PathParam("id") int id)`

3. **Recupera un dominio specifico in base al nome e al TLD**
   - **Comando**: `GET/NOME/TLD/{nome}/{tld}/DOMAIN\n`
   - **Metodo**: `getDomainByNomeTLD(@PathParam("nome") String nome, @PathParam("tld") String tld)`

4. **Recupera un dominio specifico in base al suo ID**
   - **Comando**: `GET/ID/{id}/DOMAIN\n`
   - **Metodo**: `getDomainById(@PathParam("id") int id)`

5. **Aggiunge un nuovo dominio al database**
   - **Comando**: `POST/{jsonString}/DOMAIN\n`
   - **Metodo**: `addDomain(Dominio dominio)`

6. **Rinnova un dominio esistente**
   - **Comando**: `PUT/domain/{jsonString}/DOMAIN\n`
   - **Metodo**: `rinnovaDominio(Dominio dominio)`

#### OrdineResource

1. **Recupera tutti gli ordini**
   - **Comando**: `GET/ID/all/ORDERS\n`
   - **Metodo**: `getOrders()`

2. **Recupera gli ordini di un utente specifico**
   - **Comando**: `GET/id/{id}/ORDERS\n`
   - **Metodo**: `getOrderUser(@PathParam("id") int id)`

3. **Aggiunge un nuovo ordine**
   - **Comando**: `POST/{jsonString}/ORDERS\n`
   - **Metodo**: `addOrders(Ordine ordine)`

#### UtentiResource

1. **Recupera tutti gli utenti**
   - **Comando**: `GET/ID/all/USER\n`
   - **Metodo**: `getAllUsers()`

2. **Recupera un utente specifico in base al suo ID**
   - **Comando**: `GET/ID/{id}/USER\n`
   - **Metodo**: `getUserById(@PathParam("id") int id)`

3. **Aggiunge un nuovo utente**
   - **Comando**: `POST/{jsonString}/USER\n`
   - **Metodo**: `addUser(Utente utente)`

### Descrizione del Protocollo

Il protocollo utilizza comandi testuali semplici inviati tramite TCP. Ogni comando è strutturato in un formato specifico che include il metodo HTTP simulato, i parametri necessari e l'oggetto target. Le risposte dal database sono anch'esse in formato testuale e terminano con la stringa "END" per indicare la fine della trasmissione dei dati.

### Implementazione del Protocollo

L'implementazione del protocollo è realizzata tramite le seguenti classi Java che stabiliscono una connessione TCP con il database, inviano comandi e ricevono risposte:

- `DominiResource`
- `OrdineResource`
- `UtentiResource`

Le comunicazioni sono gestite principalmente attraverso le classi `Socket`, `DataOutputStream` e `BufferedReader` per inviare e ricevere messaggi.

### Esempio di Comunicazione

Esempio di invio di un comando al database:

```java
try (Socket socket = new Socket(DB_HOST, DB_PORT);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

    out.writeBytes("GET/ID/all/DOMAIN\n");
    System.out.println("SERVER'S SENDING: GET/ID/all/DOMAIN\n");
    String response = readFromSocket(buff);
}
