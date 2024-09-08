package it.unimib.sd2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Gestisce una connessione del client tramite socket.
 * Questa classe implementa l'interfaccia Runnable per consentire
 * l'esecuzione in un thread separato.
 */
public class Handler implements Runnable {
    private Socket client; // Il socket del client
    private Database db; // Il riferimento al database

    /**
     * Costruttore della classe Handler.
     *
     * @param client Il socket del client da gestire.
     * @param db     Il riferimento al database utilizzato per le operazioni.
     */
    public Handler(Socket client, Database db) {
        this.client = client;
        this.db = db;
    }

    /**
     * Esegue il thread per gestire la richiesta del client.
     * Legge la richiesta, esegue l'operazione richiesta sul database e invia la
     * risposta.
     */
    public void run() {
        try {
            // Stream per la lettura e scrittura attraverso il socket
            var out = new PrintWriter(client.getOutputStream(), true);
            var in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Legge la richiesta inviata dal client
            String request = in.readLine();
            System.out.println("QUERY RECEIVED: " + request);

            // Analizza la richiesta ricevuta
            String instruction[] = request.split("/");
            String answer = null;

            // Gestisce la richiesta in base al metodo HTTP (GET, POST, PUT, ecc.)
            switch (instruction[0]) {
                case "GET":
                    // Gestisce le richieste GET
                    if (instruction.length == 4) {
                        if (instruction[1].equalsIgnoreCase("ID")) {
                            // GET/ID/{ID}/DOMAIN o GET/ID/{ID}/USER o GET/ID/{ID}/ORDER
                            if (instruction[3].equalsIgnoreCase("DOMAIN")) {
                                answer = db.readDomains(instruction[2]);
                            } else if (instruction[3].equalsIgnoreCase("USER")) {
                                answer = db.readUsers(instruction[2]);
                            } else {
                                answer = db.readOrders(instruction[2]);
                            }
                        } else if (instruction[1].equalsIgnoreCase("USER")) {
                            // GET/USER/{ID}/DOMAIN
                            if (instruction[3].equalsIgnoreCase("DOMAIN")) {
                                answer = db.readDomainsByProprietario(instruction[2]);
                            }
                        }
                    } else if (instruction.length == 6) {
                        // GET/{qualcosa}/{nome}/{tld}/DOMAIN
                        if (instruction[5].equalsIgnoreCase("DOMAIN")) {
                            answer = db.readDomainsNomeTLD(instruction[3], instruction[4]);
                        }
                    }
                    break;
                case "POST":
                    // Gestisce le richieste POST
                    if (instruction[2].equalsIgnoreCase("USER")) {
                        answer = db.addUser(instruction[1]);
                    } else if (instruction[2].equalsIgnoreCase("DOMAIN")) {
                        answer = db.addDomain(instruction[1]);
                    } else {
                        answer = db.addOrder(instruction[1]);
                    }
                    break;
                case "PUT":
                    // Gestisce le richieste PUT
                    if (instruction[3].equalsIgnoreCase("DOMAIN")) {
                        answer = db.updateDomain(instruction[2]);
                    }
                    break;
                default:
                    // Gestione di altri metodi non supportati
                    break;
            }

            System.out.println("QUERY RESULT: " + answer);

            // Invia la risposta al client
            out.println(answer);

            // Chiude i flussi e il socket del client
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            // Gestisce le eccezioni IO
            System.err.println(e);
        }
    }
}