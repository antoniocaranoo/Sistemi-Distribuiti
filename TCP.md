## Communication Protocol Between the Database and Web Server

The communication protocol between the database and the Web server is designed according to the system requirements. It uses a limited set of commands and transmits data in textual format.

### Implemented Commands

The following TCP commands are used for communication between the Web server and the database.

#### DominiResource

1. **Retrieve all domains from the database**
   - **Command**: `GET/ID/all/DOMAIN\n`
   - **Method**: `getAllDomains()`

2. **Retrieve all domains associated with a specific user**
   - **Command**: `GET/user/{id}/DOMAIN\n`
   - **Method**: `getUsersDomains(@PathParam("id") int id)`

3. **Retrieve a specific domain by name and TLD**
   - **Command**: `GET/NOME/TLD/{nome}/{tld}/DOMAIN\n`
   - **Method**: `getDomainByNomeTLD(@PathParam("nome") String nome, @PathParam("tld") String tld)`

4. **Retrieve a specific domain by its ID**
   - **Command**: `GET/ID/{id}/DOMAIN\n`
   - **Method**: `getDomainById(@PathParam("id") int id)`

5. **Add a new domain to the database**
   - **Command**: `POST/{jsonString}/DOMAIN\n`
   - **Method**: `addDomain(Dominio dominio)`

6. **Renew an existing domain**
   - **Command**: `PUT/domain/{jsonString}/DOMAIN\n`
   - **Method**: `rinnovaDominio(Dominio dominio)`

#### OrdineResource

1. **Retrieve all orders**
   - **Command**: `GET/ID/all/ORDERS\n`
   - **Method**: `getOrders()`

2. **Retrieve the orders associated with a specific user**
   - **Command**: `GET/id/{id}/ORDERS\n`
   - **Method**: `getOrderUser(@PathParam("id") int id)`

3. **Add a new order**
   - **Command**: `POST/{jsonString}/ORDERS\n`
   - **Method**: `addOrders(Ordine ordine)`

#### UtentiResource

1. **Retrieve all users**
   - **Command**: `GET/ID/all/USER\n`
   - **Method**: `getAllUsers()`

2. **Retrieve a specific user by their ID**
   - **Command**: `GET/ID/{id}/USER\n`
   - **Method**: `getUserById(@PathParam("id") int id)`

3. **Add a new user**
   - **Command**: `POST/{jsonString}/USER\n`
   - **Method**: `addUser(Utente utente)`

### Protocol Description

The protocol uses simple textual commands sent over TCP. Each command follows a specific format containing the simulated HTTP method, the required parameters, and the target object.

Responses from the database are also transmitted in textual format and terminate with the string `END`, which indicates the end of the data transmission.

### Protocol Implementation

The protocol is implemented through the following Java classes, which establish a TCP connection with the database, send commands, and receive responses:

- `DominiResource`
- `OrdineResource`
- `UtentiResource`

Communication is mainly handled using the following Java classes:

- `Socket`
- `DataOutputStream`
- `BufferedReader`

These classes are used to establish the connection and send and receive messages between the Web server and the database.

### Communication Example

The following example shows how a command is sent from the Web server to the database:

    try (Socket socket = new Socket(DB_HOST, DB_PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        out.writeBytes("GET/ID/all/DOMAIN\n");
        System.out.println("SERVER'S SENDING: GET/ID/all/DOMAIN\n");
        String response = readFromSocket(buff);
    }

In this example, the Web server:

1. establishes a TCP connection with the database using `Socket`;
2. creates a `DataOutputStream` to send data;
3. creates a `BufferedReader` to receive the database response;
4. sends the `GET/ID/all/DOMAIN\n` command;
5. reads the response returned by the database through `readFromSocket()`.
