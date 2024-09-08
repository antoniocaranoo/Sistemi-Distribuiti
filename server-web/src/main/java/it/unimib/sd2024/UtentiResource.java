package it.unimib.sd2024;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Rappresenta la risorsa "users" in "http://localhost:8080/users".
 */
@Path("users")
public class UtentiResource {

    /**
     * Implementazione di GET "/users".
     *
     * @return una risposta contenente tutti gli utenti in formato JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {

        ArrayList<Utente> use = new ArrayList<>();
        String request = "";

        try {
            request = Connettore.sendToDB("GET/ID/all/USER\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request.contains("ERROR_CLASS_NOT_PRESENT_IN_DB")) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Errore interno\"}")
                    .build();
        }

        if (request.isEmpty() || request == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Utenti non trovati\"}")
                    .build();
        }

        try {
            Jsonb jsonb = JsonbBuilder.create();
            use = jsonb.fromJson(request, new ArrayList<Utente>() {
            }.getClass().getGenericSuperclass());
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(use, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Implementazione di GET "/users/{id}".
     *
     * @param id l'ID dell'utente da recuperare.
     * @return una risposta contenente l'utente specificato in formato JSON.
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {

        Utente use = null;
        String request = "";

        try {
            request = Connettore.sendToDB("GET/ID/" + id + "/USER\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request.contains("ERROR_CLASS_NOT_PRESENT_IN_DB")) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Errore interno\"}")
                    .build();
        }

        if (request == null || request.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Utente non trovato\"}")
                    .build();
        }

        try {
            Jsonb jsonb = JsonbBuilder.create();
            use = jsonb.fromJson(request, Utente.class);
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(use, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Implementazione di POST "/users".
     *
     * @param utente l'utente da aggiungere.
     * @return una risposta che conferma l'aggiunta dell'utente.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Utente utente) {

        Jsonb jsonb = JsonbBuilder.create();
        String jsonString = jsonb.toJson(utente);
        String request = "";

        try {
            request = Connettore.sendToDB("POST/" + jsonString + "/USER\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Utente non inserito correttamente\"}")
                    .build();

        try {
            var uri = new URI("/contacts/" + request);
            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return Response.serverError().build();
        }

    }
}