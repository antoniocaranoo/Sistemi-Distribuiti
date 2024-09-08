package it.unimib.sd2024;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

/**
 * Classe principale che avvia il server del database.
 * Questo server ascolta le connessioni dai client su una porta specificata.
 */
public class Main {
    /**
     * Porta di ascolto del server.
     */
    public static final int PORT = 3030;
    private static final int THREAD_POOL_SIZE = 15;
    private static final Database db = Database.getInstance();

    /**
     * Avvia il server del database e ascolta le connessioni in arrivo dai client.
     *
     * @throws IOException se si verifica un errore di I/O durante l'apertura del
     *                     serverSocket.
     */
    public static void startServer() throws IOException {
        // Crea un pool di thread per gestire le connessioni in modo concorrente
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Crea un ServerSocket che ascolta sulla porta specificata
        var server = new ServerSocket(PORT);
        System.out.println("Database listening at localhost:" + PORT);

        try {
            // Loop infinito per accettare e gestire le connessioni dei client
            while (true) {
                Socket clientSocket = server.accept(); // Accetta la connessione dal client
                System.out.println("New client connected");
                // Invia la gestione della connessione a un thread nel pool
                executorService.submit(new Handler(clientSocket, db));
            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e);
        } finally {
            // Chiude il pool di thread quando il server termina
            executorService.shutdown();
            server.close();
        }
    }

    /**
     * Metodo principale che avvia il server del database.
     *
     * @param args argomenti passati da riga di comando (non utilizzati in questo
     *             caso).
     *
     * @throws IOException se si verifica un errore di I/O durante l'avvio del
     *                     server.
     */
    public static void main(String[] args) throws IOException {
        startServer();
    }
}