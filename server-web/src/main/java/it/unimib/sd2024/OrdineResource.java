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
 * Rappresenta la risorsa "orders" in "http://localhost:8080/orders".
 */
@Path("orders")
public class OrdineResource {

    /**
     * Implementazione di GET "/orders".
     *
     * @return una risposta contenente tutti gli ordini in formato JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() {

        ArrayList<Ordine> ordine = new ArrayList<>();
        String request = "";

        try {
            request = Connettore.sendToDB("GET/ID/all/ORDERS\n");
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
                    .entity("{\"error\":\"Ordini non trovati\"}")
                    .build();
        }

        try {
            Jsonb jsonb = JsonbBuilder.create();
            ordine = jsonb.fromJson(request, new ArrayList<Ordine>() {
            }.getClass().getGenericSuperclass());
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(ordine, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Implementazione di GET "/orders/{id}".
     *
     * @param id l'ID dell'utente i cui ordini devono essere recuperati.
     * @return una risposta contenente gli ordini dell'utente specificato in formato
     *         JSON.
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderUser(@PathParam("id") int id) {

        ArrayList<Ordine> ordine = new ArrayList<>();
        String request = "";

        try {
            request = Connettore.sendToDB("GET/id/" + id + "/ORDERS\n");
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
                    .entity("{\"error\":\"Ordine non trovato\"}")
                    .build();
        }

        try {
            Jsonb jsonb = JsonbBuilder.create();
            ordine = jsonb.fromJson(request, new ArrayList<Ordine>() {
            }.getClass().getGenericSuperclass());
        } catch (JsonbException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'analisi del JSON: " + e.getMessage() + "\"}")
                    .build();
        }

        return Response.ok(ordine, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Implementazione di POST "/orders".
     *
     * @param ordine l'ordine da aggiungere.
     * @return una risposta che conferma l'aggiunta dell'ordine.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrder(Ordine ordine) {

        Jsonb jsonb = JsonbBuilder.create();
        String jsonString = jsonb.toJson(ordine);
        String request = "";

        try {
            request = Connettore.sendToDB("POST/" + jsonString + "/ORDERS\n");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore durante l'accesso al DB: " + e.getMessage() + "\"}")
                    .build();
        }

        if (request == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Errore di inserimento dati:\"}")
                    .build();
        }

        try {
            var uri = new URI("/orders/" + request);
            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return Response.serverError().build();
        }

    }

}