package it.unimib.sd2024;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Quando viene lanciata un'eccezione in Jersey, sia da parte dello sviluppatore (nei metodi
 * definiti), oppure da Jersey stesso, si possono definire dei mappatori personalizzati da
 * eccezione Java a e risposte HTTP.
 * 
 * JsonParsingException viene chiamata quando c'è un errore di deserializzazione JSON,
 * semplicemente invece di restituire lo stato 500 restituisce 400.
 * 
 * L'annotazione "@Provider" serve a far registrare la mappatura in automatico in JAX-RS.
 */
@Provider
public class JsonParsingException implements ExceptionMapper<ProcessingException> {
  public Response toResponse(ProcessingException ex) {
    return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).type("text/plain").build();
  }
}
