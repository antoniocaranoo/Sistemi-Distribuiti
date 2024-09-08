package it.unimib.sd2024;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

/**
 * Classe che gestisce un database documentale creata attraverso l'utilizzo del
 * pattern Singleton
 */
public class Database {

    private static Database instance;
    private static final String USER_FILE = "utenti.json";
    private static final String DOMAIN_FILE = "domini.json";
    private static final String ORDER_FILE = "ordini.json";
    private final String userPath;
    private final String domainPath;
    private final String orderPath;

    /**
     * Costruttore privato per il singleton
     * Inizializza i percorsi dei file e controlla la loro esistenza.
     */
    private Database() {
        String currentDir = Paths.get("").toAbsolutePath().toString();
        this.userPath = currentDir + File.separator + USER_FILE;
        this.domainPath = currentDir + File.separator + DOMAIN_FILE;
        this.orderPath = currentDir + File.separator + ORDER_FILE;
        checkAndCreateFiles();
        updateDomainStatus();
    }

    /**
     * Metodo per ottenere l'istanza Singleton della classe Database.
     *
     * @return l'istanza Singleton del Database.
     */
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Metodo per controllare e creare i file se non esistono.
     */
    private void checkAndCreateFiles() {
        try {
            if (!Files.exists(Paths.get(userPath))) {
                Files.createFile(Paths.get(userPath));
                try (JsonWriter writer = Json.createWriter(new FileOutputStream(userPath))) {
                    writer.writeArray(Json.createArrayBuilder().build());
                }
            }
            if (!Files.exists(Paths.get(domainPath))) {
                Files.createFile(Paths.get(domainPath));
                try (JsonWriter writer = Json.createWriter(new FileOutputStream(domainPath))) {
                    writer.writeArray(Json.createArrayBuilder().build());
                }
            }
            if (!Files.exists(Paths.get(orderPath))) {
                Files.createFile(Paths.get(orderPath));
                try (JsonWriter writer = Json.createWriter(new FileOutputStream(orderPath))) {
                    writer.writeArray(Json.createArrayBuilder().build());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per aggiornare lo stato dei domini alla data odierna.
     */
    public void updateDomainStatus() {
        LocalDate today = LocalDate.now(); // ottiene la data corrente.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // crea un formattatore per le date che
                                                                                 // seguirà il pattern "yyyy-MM-dd"

        // viene aperto il file specificato da domainPath e viene letto il suo contenuto
        // in un array JSON
        try (FileInputStream fis = new FileInputStream(domainPath);
                JsonReader reader = Json.createReader(fis)) {
            JsonArray jsonArray = reader.readArray();
            JsonArrayBuilder updatedArrayBuilder = Json.createArrayBuilder();

            /*
             * Per ogni oggetto JSON nell'array:
             * - La data di scadenza viene letta e convertita in un oggetto LocalDate
             * - Se la data di oggi è successiva alla data di scadenza, l'oggetto viene
             * aggiornato con lo stato "scaduto" e il proprietario impostato a NULL
             * - Se la data di scadenza non è passata, l'oggetto viene lasciato inalterato
             */
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                LocalDate expirationDate = LocalDate.parse(jsonObject.getString("dataScadenza"), formatter);
                if (today.isAfter(expirationDate)) {
                    JsonObject updatedDomain = Json.createObjectBuilder(jsonObject)
                            .add("stato", "scaduto")
                            .add("proprietario", JsonValue.NULL)
                            .build();
                    updatedArrayBuilder.add(updatedDomain);
                } else {
                    updatedArrayBuilder.add(jsonObject);
                }
            }

            JsonArray updatedJsonArray = updatedArrayBuilder.build();

            // il file viene aperto in modalità scrittura e il nuovo array JSON viene
            // scritto nel file
            try (OutputStream fos = new FileOutputStream(domainPath);
                    JsonWriter jsonWriter = Json.createWriter(fos)) {
                jsonWriter.writeArray(updatedJsonArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per trovare gli utenti (o l'utente) attraverso la lettura del database
     *
     * @param id l'ID dell'utente da leggere, o "all" per leggere tutti gli utenti.
     * @return una stringa JSON contenente l'utente o gli utenti richiesti.
     */
    public String readUsers(String id) {
        String result = null;
        try (FileInputStream fis = new FileInputStream(userPath);
                JsonReader reader = Json.createReader(fis)) {
            JsonArray jsonArray = reader.readArray();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Se id è uguale a "all", il metodo scrive l'intero array JSON
            // nell'outputStream
            if (id.equalsIgnoreCase("all")) {
                try (JsonWriter writer = Json.createWriter(outputStream)) {
                    writer.writeArray(jsonArray);
                }
            } else {
                // Se id non è "all", viene interpretato come un identificatore numerico
                // dell'utente.
                // Il metodo cerca l'utente con l'id specificato all'interno dell'array JSON
                int userId = Integer.parseInt(id);
                JsonObject user = null;
                for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                    if (jsonObject.getInt("id") == userId) {
                        user = jsonObject;
                        break;
                    }
                }
                if (user != null) {
                    try (JsonWriter writer = Json.createWriter(outputStream)) {
                        writer.writeObject(user);
                    }
                }
            }

            result = outputStream.toString();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Metodo per trovare i domini (o il dominio) attraverso la lettura del
     * database.
     * 
     * @param id l'ID del dominio da leggere, o "all" per leggere tutti i domini.
     * @return una stringa JSON contenente il dominio o i domini richiesti.
     */
    public String readDomains(String id) {
        String result = null;
        try (FileInputStream fis = new FileInputStream(domainPath);
                JsonReader reader = Json.createReader(fis)) {
            JsonArray jsonArray = reader.readArray();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            if (id.equalsIgnoreCase("all")) {
                try (JsonWriter writer = Json.createWriter(outputStream)) {
                    writer.writeArray(jsonArray);
                }
            } else {
                int idDomain = Integer.parseInt(id);
                JsonObject domain = null;
                for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                    if (jsonObject.getInt("id") == idDomain) {
                        domain = jsonObject;
                        break;
                    }
                }
                if (domain != null) {
                    try (JsonWriter writer = Json.createWriter(outputStream)) {
                        writer.writeObject(domain);
                    }
                }
            }

            result = outputStream.toString();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Metodo per l'aggiunta di un utente attraverso la scrittura nel database con
     * accesso sincronizzato
     * 
     * @param userJsonString stringa rappresentante il nuovo utente da aggiungere
     * @return I dati dell'utente aggiunto come stringa JSON, o null se si verifica
     *         un errore.
     */
    public synchronized String addUser(String userJsonString) {
        JsonObject newUser;
        try (JsonReader jsonReader = Json.createReader(new StringReader(userJsonString))) {
            JsonObject tempUser = jsonReader.readObject();

            // Legge gli utenti esistenti dal file
            JsonArray usersArray;
            try (InputStream fis = new FileInputStream(userPath);
                    JsonReader reader = Json.createReader(fis)) {
                usersArray = reader.readArray();
            } catch (Exception e) {
                // In caso di errore (file non esistente o vuoto), crea un nuovo array
                usersArray = Json.createArrayBuilder().build();
            }

            // Conta gli utenti esistenti e determina il nuovo ID
            int newId = usersArray.size() + 1;

            // Costruisce manualmente il JsonObject con il nuovo ID
            newUser = Json.createObjectBuilder(tempUser)
                    .add("id", newId)
                    .build();

            // Aggiunge il nuovo utente all'array
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (JsonObject user : usersArray.getValuesAs(JsonObject.class)) {
                arrayBuilder.add(user);
            }
            arrayBuilder.add(newUser);
            JsonArray updatedUsersArray = arrayBuilder.build();

            // Scrive l'array aggiornato di nuovo nel file
            try (OutputStream fos = new FileOutputStream(userPath);
                    JsonWriter jsonWriter = Json.createWriter(fos)) {
                jsonWriter.writeArray(updatedUsersArray);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return String.valueOf(newId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo per l'aggiunta di un dominio attraverso la scrittura nel database con
     * accesso sincronizzato
     *
     * @param domainJsonString stringa rappresentante il nuovo dominio da aggiungere
     * @return I dati del dominio aggiunto come stringa JSON, o null se si verifica
     *         un errore.
     */
    public synchronized String addDomain(String domainJsonString) {

        JsonObject newDomain;
        try (JsonReader jsonReader = Json.createReader(new StringReader(domainJsonString))) {
            JsonObject tempDomain = jsonReader.readObject();

            // Legge i domini esistenti dal file
            JsonArray domainsArray;
            try (InputStream fis = new FileInputStream(domainPath);
                    JsonReader reader = Json.createReader(fis)) {
                domainsArray = reader.readArray();
            } catch (Exception e) {
                // In caso di errore (file non esistente o vuoto), crea un nuovo array
                domainsArray = Json.createArrayBuilder().build();
            }

            // Assegna un ID al nuovo dominio
            int newId = domainsArray.size() + 1;

            // Costruisce manualmente il JsonObject con i campi nell'ordine desiderato
            newDomain = Json.createObjectBuilder(tempDomain)
                    .add("id", newId)
                    .build();

            // Aggiunge il nuovo dominio all'array
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (JsonObject dom : domainsArray.getValuesAs(JsonObject.class)) {
                arrayBuilder.add(dom);
            }
            arrayBuilder.add(newDomain);
            JsonArray updatedDomainsArray = arrayBuilder.build();

            // Scrive l'array aggiornato di nuovo nel file
            try (OutputStream fos = new FileOutputStream(domainPath);
                    JsonWriter jsonWriter = Json.createWriter(fos)) {
                jsonWriter.writeArray(updatedDomainsArray);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return String.valueOf(newId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo per trovare i domini filtrati in base al proprietario attraverso la
     * lettura del database.
     *
     * @param id L'ID del proprietario per filtrare i domini.
     * @return I domini del proprietario come stringa JSON, o null se si verifica un
     *         errore.
     */
    public String readDomainsByProprietario(String id) {
        String result = null;
        try (FileInputStream fis = new FileInputStream(domainPath);
                JsonReader reader = Json.createReader(fis)) {
            JsonArray jsonArray = reader.readArray();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            /*
             * FILTRARE I DOMINI:
             * - Scorre ogni oggetto JSON nell'array (jsonArray).
             * - Controlla se il campo proprietario non è nullo.
             * - Confronta il valore del campo proprietario con l'ID del proprietario
             * fornito (idProprietario).
             * - Se corrisponde, aggiunge l'oggetto JSON al JsonArrayBuilder
             */
            int idProprietario = Integer.parseInt(id);
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                if (!jsonObject.isNull("proprietario")) {
                    Integer proprietario = jsonObject.getInt("proprietario");
                    if (proprietario == idProprietario) {
                        arrayBuilder.add(jsonObject);
                    }
                }
            }

            JsonArray resultArray = arrayBuilder.build();
            try (JsonWriter writer = Json.createWriter(outputStream)) {
                writer.writeArray(resultArray);
            }

            result = outputStream.toString();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Metodo per trovare i domini filtrati per nome e tld attraverso
     * la lettura del database.
     *
     * @param nome Il nome del dominio da cercare.
     * @param tld  Il TLD del dominio da cercare.
     * @return Il dominio corrispondente come stringa JSON, o null se non trovato o
     *         si verifica un errore.
     */
    public String readDomainsNomeTLD(String nome, String tld) {
        String result = null;
        try (FileInputStream fis = new FileInputStream(domainPath);
                JsonReader reader = Json.createReader(fis)) {

            JsonArray jsonArray = reader.readArray();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonObject domain = null;
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                if (jsonObject.getString("nome").equalsIgnoreCase(nome)
                        && jsonObject.getString("tld").equalsIgnoreCase(tld)
                        && jsonObject.getString("stato").equalsIgnoreCase("attivo")) {
                    domain = jsonObject;
                    break;
                }
            }
            if (domain != null) {
                try (JsonWriter writer = Json.createWriter(outputStream)) {
                    writer.writeObject(domain);
                }
            } else {
                return null;
            }

            result = outputStream.toString();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * !!! qui dovrebbe essere passato l'oggetto direttamente aggiornato !!!
     */

    /**
     * Metodo per aggiornare un dominio esistente; l'aggiornamento è in base al
     * rinnovo del
     * dominio attraverso la scrittura sul database.
     *
     * @param updatedDomainJson I dati del dominio aggiornato come stringa
     * @return I dati del dominio aggiornato come stringa JSON, o null se si
     *         verifica un errore.
     */
    public synchronized String updateDomain(String updatedDomainJson) {
        try (FileInputStream fis = new FileInputStream(domainPath);
                JsonReader reader = Json.createReader(fis)) {

            JsonArray jsonArray = reader.readArray();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            // Converti la stringa JSON in un oggetto JsonObject
            JsonReader jsonReader = Json.createReader(new StringReader(updatedDomainJson));
            JsonObject updatedDomainObject = jsonReader.readObject();
            int domainId = updatedDomainObject.getInt("id");

            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                if (jsonObject.getInt("id") == domainId) {
                    // Aggiungi il dominio aggiornato con l'ordine desiderato
                    arrayBuilder.add(updatedDomainObject);

                } else {
                    // Mantieni il dominio originale
                    arrayBuilder.add(jsonObject);
                }
            }

            JsonArray updatedJsonArray = arrayBuilder.build();
            try (FileOutputStream fos = new FileOutputStream(domainPath);
                    JsonWriter writer = Json.createWriter(fos)) {
                writer.writeArray(updatedJsonArray);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return updatedDomainJson;
    }

    /**
     * Mettodo per leggere gli ordini dal database filtrati per ID utente.
     *
     * @param idUtente L'ID dell'utente per filtrare gli ordini, o "all" per leggere
     *                 tutti gli ordini.
     * @return Gli ordini filtrati come stringa JSON, o null se si verifica un
     *         errore.
     */
    public String readOrders(String idUtente) {
        String result = null;
        try (FileInputStream fis = new FileInputStream(orderPath);
                JsonReader reader = Json.createReader(fis)) {
            JsonArray jsonArray = reader.readArray();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            if (idUtente.equalsIgnoreCase("all")) {
                try (JsonWriter writer = Json.createWriter(outputStream)) {
                    writer.writeArray(jsonArray);
                }
            } else {
                int userId = Integer.parseInt(idUtente);
                JsonArrayBuilder filteredOrdersBuilder = Json.createArrayBuilder();
                for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                    if (jsonObject.getInt("idUtente") == userId) {
                        filteredOrdersBuilder.add(jsonObject);
                    }
                }
                JsonArray filteredOrdersArray = filteredOrdersBuilder.build();
                try (JsonWriter writer = Json.createWriter(outputStream)) {
                    writer.writeArray(filteredOrdersArray);
                }
            }

            result = outputStream.toString();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Metodo per aggiungere un nuovo ordine attraverso
     * la scrittura nel database.
     *
     * @param orderJsonString I dati dell'ordine come stringa JSON da aggiungere.
     * @return I dati dell'ordine aggiunto come stringa JSON, o null se si verifica
     *         un errore.
     */
    public synchronized String addOrder(String orderJsonString) {
        JsonObject newOrder;
        try (JsonReader jsonReader = Json.createReader(new StringReader(orderJsonString))) {
            JsonObject tempOrder = jsonReader.readObject();

            // Legge gli ordini esistenti dal file
            JsonArray ordersArray;
            try (InputStream fis = new FileInputStream(orderPath);
                    JsonReader reader = Json.createReader(fis)) {
                ordersArray = reader.readArray();
            } catch (Exception e) {
                // In caso di errore (file non esistente o vuoto), crea un nuovo array
                ordersArray = Json.createArrayBuilder().build();
            }

            int maxId = ordersArray.size() + 1;

            // Costruisce il nuovo ordine con il nuovo ID
            newOrder = Json.createObjectBuilder(tempOrder)
                    .add("idOrdine", maxId)
                    .build();

            // Aggiunge il nuovo ordine all'array
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (JsonObject order : ordersArray.getValuesAs(JsonObject.class)) {
                arrayBuilder.add(order);
            }
            arrayBuilder.add(newOrder);
            JsonArray updatedOrdersArray = arrayBuilder.build();

            // Scrive l'array aggiornato di nuovo nel file
            try (OutputStream fos = new FileOutputStream(orderPath);
                    JsonWriter jsonWriter = Json.createWriter(fos)) {
                jsonWriter.writeArray(updatedOrdersArray);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return String.valueOf(maxId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}