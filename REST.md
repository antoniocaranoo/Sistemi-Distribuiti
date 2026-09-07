# Project - REST API

Documentation for the REST APIs provided by the Web server. Each resource (`domain`, `order`, `user`) exposed by the server has a dedicated path.

## `/domains`

### GET

**Description**: Retrieves all domains from the database. This method sends a request to the database to obtain the list of all registered domains and returns the list in JSON format.

**Parameters**: none.

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains a list of domains in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the list of domains in JSON format.
- `404 Not Found`: No domains were found in the database.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/domains/{id}`

### GET

**Description**: Retrieves a specific domain by its ID. This method sends a request to the database to obtain information about the domain identified by the specified ID and returns its details in JSON format.

**Parameters**: `id`: domain ID (`int`).

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains the details of the requested domain in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the domain details in JSON format.
- `404 Not Found`: The specified domain was not found in the database.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/domains/{name}/{tld}`

### GET

**Description**: Retrieves a specific domain by its name and TLD (Top-Level Domain). This method sends a request to the database to obtain information about the specified domain and returns its details in JSON format.

**Parameters**:
- `name`: domain name (`String`).
- `tld`: domain TLD (`String`).

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains the details of the requested domain in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the domain details in JSON format.
- `404 Not Found`: The specified domain was not found in the database.
- `409 Conflict`: The domain is already being registered.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/domains/users/{id}`

### GET

**Description**: Retrieves all domains associated with a specific user. This method sends a request to the database to obtain the list of domains registered by the user identified by the specified ID.

**Parameters**: `id`: user ID (`int`).

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains a list of domains associated with the specified user in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the list of domains associated with the user.
- `404 Not Found`: No domains were found for the specified user.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/domains`

### POST

**Description**: Adds a new domain to the database. This method receives the domain details in JSON format, converts them into a JSON string, sends the string to the database for insertion, and returns the ID of the added domain.

**Parameters**: `dominio`: `Dominio` object containing the details of the domain to be added.

**Headers**: none.

**Request Body**: The request body must contain a `Dominio` object in JSON format.

**Response**: If successful, the created resource is specified in the `Location` header.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the ID of the added domain in JSON format.
- `500 Internal Server Error`: An error occurred while communicating with the database or an internal server error occurred.

---

## `/domains`

### PUT

**Description**: Renews an existing domain in the database. This method receives the domain details in JSON format, converts them into a JSON string, sends the string to the database to renew the domain, and returns the ID of the renewed domain.

**Parameters**: `dominio`: `Dominio` object containing the details of the domain to be renewed.

**Headers**: none.

**Request Body**: The request body must contain a `Dominio` object in JSON format.

**Response**: If successful, the response contains the ID of the renewed domain.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the ID of the renewed domain.
- `500 Internal Server Error`: An error occurred while communicating with the database or an internal server error occurred.

---

# Users

## `/users`

### GET

**Description**: Retrieves all users from the database. This method sends a request to the database to obtain the list of all registered users and returns the list in JSON format.

**Parameters**: none.

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains a list of users in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the list of users in JSON format.
- `404 Not Found`: No users were found in the database.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/users/{id}`

### GET

**Description**: Retrieves a specific user by their ID. This method sends a request to the database to obtain information about the specified user and returns the user's details in JSON format.

**Parameters**: `id`: user ID (`int`).

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains the details of the requested user in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the user details in JSON format.
- `404 Not Found`: The specified user was not found in the database.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/users`

### POST

**Description**: Adds a new user to the database. This method receives the user details in JSON format, converts them into a JSON string, sends the string to the database for insertion, and returns a response confirming that the user was added.

**Parameters**: `utente`: `Utente` object containing the details of the user to be added.

**Headers**: none.

**Request Body**: The request body must contain an `Utente` object in JSON format.

**Response**: If successful, the created resource is specified in the `Location` header.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns confirmation that the user was added.
- `500 Internal Server Error`: An error occurred while communicating with the database or an internal server error occurred.

---

# Orders

## `/orders`

### GET

**Description**: Retrieves all orders from the database. This method sends a request to the database to obtain the list of all registered orders and returns the list in JSON format.

**Parameters**: none.

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains a list of orders in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the list of orders in JSON format.
- `404 Not Found`: No orders were found in the database.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/orders/{id}`

### GET

**Description**: Retrieves all orders associated with a specific user based on their ID. This method sends a request to the database to obtain the list of orders associated with the specified user and returns them in JSON format.

**Parameters**: `id`: user ID (`int`).

**Headers**: none.

**Request Body**: none.

**Response**: If successful, the response contains a list of orders associated with the specified user in JSON format.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the list of orders associated with the user in JSON format.
- `404 Not Found`: No orders were found for the specified user.
- `500 Internal Server Error`: An error occurred while communicating with the database or parsing the JSON.

---

## `/orders`

### POST

**Description**: Adds a new order to the database. This method receives the order details in JSON format, converts them into a JSON string, sends the string to the database for insertion, and returns the ID of the domain associated with the added order.

**Parameters**: `ordine`: `Ordine` object containing the details of the order to be added.

**Headers**: none.

**Request Body**: The request body must contain an `Ordine` object in JSON format.

**Response**: If successful, the created resource is specified in the `Location` header.

**Returned Status Codes**:
- `200 OK`: The request was completed successfully. Returns the ID of the domain associated with the added order in JSON format.
- `500 Internal Server Error`: An error occurred while communicating with the database or an internal server error occurred.
