# Project

The **RicorsivaMente Domains** website allows users to purchase and renew available Internet domains.

## Compilation and Execution

Both the Web server and the database are Java applications managed with Maven. Inside their respective folders, the `pom.xml` file contains the Maven configuration for each project. The use of the laboratory virtual machine is assumed; therefore, Java 21 is specified in the `pom.xml`.

The Web server and the database are Java projects that use Maven to manage dependencies, compilation, and execution.

### Web Client

To start the Web client, it is necessary to use the **Live Preview** extension in Visual Studio Code, as shown during the laboratory sessions. This extension exposes a local server containing the files located in the `client-web` folder.

The recommended alternative is to access `localhost:8080` using Google Chrome, which will open the `index.html` page.

**Warning:** CORS must be configured in Google Chrome as shown during the laboratory sessions.

The `client-web` consists of two HTML files:

- **index**: handles user login and registration and displays the domains that have already been registered. The corresponding CSS files for styling and JavaScript methods are linked to this page.
- **dashboard**: handles domain purchases and renewals and displays the domains and orders associated with the currently logged-in user. The corresponding CSS files for styling and JavaScript methods are linked to this page.

### Web Server

The Web server uses Jetty and Jersey. It can be started by running:

    mvn jetty:run

inside the `server-web` folder.

It exposes the REST APIs on `localhost` at port `8080`.

The `server-web` consists of the following files:

- **Utente**: represents the data model for a user, with basic attributes such as ID, first name, last name, and email.
- **Ordine**: represents the data model used to manage information related to an order.
- **Dominio**: represents the data model for an Internet domain, including information such as ID, name, TLD (Top-Level Domain), registration and expiration dates, status, and domain owner.
- **DominiResource**: manages CRUD operations for the `domains` resource through a RESTful interface using JAX-RS.
- **OrdiniResource**: manages CRUD operations for the `orders` resource through a RESTful interface using JAX-RS.
- **UtentiResource**: manages CRUD operations for the `users` resource through a RESTful interface using JAX-RS.
- **JsonException**: implements exception handling for JSON deserialization in a RESTful API using JAX-RS (Java API for RESTful Web Services).
- **JsonParsingException**: is a custom exception handler for a RESTful API using JAX-RS. It maps `ProcessingException` exceptions, specifically those related to JSON deserialization issues, to HTTP responses with status `400 Bad Request`. This makes it possible to provide more informative responses to clients when JSON processing errors occur.

### Database

The database is a simple Java application.

The following Maven commands can be used:

- `mvn clean`: cleans the project folder by removing temporary files.
- `mvn compile`: compiles the application.
- `mvn exec:java`: starts the application, assuming that the main class is `Main.java`.

The database application listens on `localhost` at port `3030`.

The database consists of three JSON files containing all instances of:

- users;
- domains;
- orders.

It also includes classes that manage the database logic:

- **Main**: the main class that starts the database server and manages client connections.
- **Handler**: manages client connections to the database server and implements the `Runnable` interface, allowing client requests to be handled in separate threads. Each instance of the class is responsible for a single client connection.
- **Database**: provides centralized management of users, domains, and orders, with methods for reading, adding, and updating data in JSON format. It uses the Singleton pattern to ensure that only one shared instance exists and synchronizes access to modification methods to prevent concurrency issues.
