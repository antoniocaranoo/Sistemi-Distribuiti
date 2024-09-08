package it.unimib.sd2024;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 * Classe che gestisce le operazioni sui domini.
 */
@Path("domains")
public class DominiResource {

    private static final Map<String, Instant> lockdomains = Collections.synchronizedMap(new HashMap<>());
    private static final long LOCK_TIMEOUT = 121; // durata del lock in secondi
    private static final Logger logger = Logger.getLogger(DominiResource.class.getName());

    /**
     * Recupera tutti i domini dal database.
     *
     * @return Risposta contenente la lista di domini.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDomains() {

        ArrayList<Dominio> dom = new ArrayList<>();
        String request = "";

        try {
            request = Connettore.sendToDB("GET/ID/all/DOMAIN\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request.contains("ERROR_CLASS_NOT_PRESENT_IN_DB"))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Errore interno\"}")
                    .build();

        if (request == null || request.isEmpty())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Domini non trovati\"}")
                    .build();

        try {
            Jsonb jsonb = JsonbBuilder.create();
            dom = jsonb.fromJson(request, new ArrayList<Dominio>() {
            }.getClass().getGenericSuperclass());
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(dom, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Recupera tutti i domini associati a un utente specifico.
     *
     * @param id ID dell'utente.
     * @return Risposta contenente la lista di domini dell'utente.
     */
    @Path("/users/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDomains(@PathParam("id") int id) {

        ArrayList<Dominio> dom = new ArrayList<>();
        String request = "";

        try {
            request = Connettore.sendToDB("GET/user/" + id + "/DOMAIN\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request.contains("ERROR_CLASS_NOT_PRESENT_IN_DB"))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Errore interno\"}")
                    .build();

        if (request == null || request.isEmpty())
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Domini non trovati per questo utente\"}")
                    .build();

        try {
            Jsonb jsonb = JsonbBuilder.create();
            dom = jsonb.fromJson(request, new ArrayList<Dominio>() {
            }.getClass().getGenericSuperclass());
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(dom, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Recupera un dominio specifico in base al nome e al TLD.
     *
     * @param nome Nome del dominio.
     * @param tld  TLD del dominio.
     * @return Risposta contenente il dominio.
     */
    @Path("/{name}/{tld}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDomainByNameTLD(@PathParam("name") String nome, @PathParam("tld") String tld) {

        Dominio dom = null;
        String request = "";

        try {
            request = Connettore.sendToDB("GET/NOME/TLD/" + nome + "/" + tld + "/DOMAIN\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error communicating with the database: " + e.getMessage())
                    .build();
        }

        if (request.contains("ERROR_CLASS_NOT_PRESENT_IN_DB"))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Errore interno\"}")
                    .build();

        if (request == null || request.isEmpty() || request.equalsIgnoreCase("null")) {
            boolean isAdded = lockdomain(nome + "." + tld);
            logger.info("Lock domain result: " + isAdded);

            if (!isAdded) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\":\"Il dominio è già in fase di registrazione.\"}")
                        .build();
            }

            return Response.ok("{}").build(); // Return an empty JSON object
        }

        try {
            Jsonb jsonb = JsonbBuilder.create();
            dom = jsonb.fromJson(request, Dominio.class);
        } catch (JsonbException e) {
            e.printStackTrace();
            unlockdomain(nome + "." + tld);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(dom, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Recupera un dominio specifico in base al suo ID.
     *
     * @param id ID del dominio.
     * @return Risposta contenente il dominio.
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDomainById(@PathParam("id") int id) {

        Dominio dom = null;
        String request = "";

        try {
            request = Connettore.sendToDB("GET/ID/" + id + "/DOMAIN\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request.contains("ERROR_CLASS_NOT_PRESENT_IN_DB"))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Errore interno\"}")
                    .build();

        if (request == null || request.isEmpty())
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Dominio non trovato\"}")
                    .build();

        try {
            Jsonb jsonb = JsonbBuilder.create();
            dom = jsonb.fromJson(request, Dominio.class);
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(dom, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Aggiunge un nuovo dominio al database.
     *
     * @param dominio Il dominio da aggiungere.
     * @return Risposta contenente l'ID del dominio aggiunto.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDomain(Dominio dominio) {

        Jsonb jsonb = JsonbBuilder.create();
        String jsonString = jsonb.toJson(dominio);
        String request = "";

        try {
            request = Connettore.sendToDB("POST/" + jsonString + "/DOMAIN\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Dominio non inserito correttamente\"}")
                    .build();

        unlockdomain(dominio.getNome() + "." + dominio.gettld());

        try {
            var uri = new URI("/domains/" + request);
            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return Response.serverError().build();
        }
    }

    /**
     * Rinnova un dominio esistente.
     *
     * @param dominio Il dominio da rinnovare.
     * @return Risposta contenente l'ID del dominio rinnovato.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response renewDomain(Dominio dominio) {

        Jsonb jsonb = JsonbBuilder.create();
        String jsonString = jsonb.toJson(dominio);
        String request = "";

        try {
            request = Connettore.sendToDB("PUT/domain/" + jsonString + "/DOMAIN\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Dominio non aggiornato correttamente\"}")
                    .build();

        return Response.ok(dominio.getId()).build();
    }

    // vedere meglio + lambda expression :)
    public static synchronized boolean lockdomain(String resource) {
        if (!lockdomains.containsKey(resource)) {
            lockdomains.put(resource, Instant.now().plusSeconds(LOCK_TIMEOUT));
            System.out.println("Locked domain: " + resource + " until " + lockdomains.get(resource));

            // Avvia un thread per sbloccare il dominio dopo 60 secondi
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                unlockdomain(resource);
            }, LOCK_TIMEOUT, TimeUnit.SECONDS);

            return true;
        }
        return false;
    }

    public static synchronized void unlockdomain(String resource) {
        lockdomains.remove(resource);
        System.out.println("Unlocked domain: " + resource);
    }
}