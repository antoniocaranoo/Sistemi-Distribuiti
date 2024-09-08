package it.unimib.sd2024;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connettore {

    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3030;

    /**
     * Legge la risposta dal socket.
     *
     * @param br BufferedReader per leggere dal socket.
     * @return Stringa contenente la risposta dal socket.
     * @throws IOException se c'è un errore durante la lettura dal socket.
     */
    public static String readFromSocket(BufferedReader br) throws IOException {
        StringBuilder build = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null && !line.equals("END")) {
            build.append(line);
        }
        return build.toString();
    }

    /**
     * Invia un messaggio al database e ritorna la risposta.
     *
     * @param message Messaggio da inviare.
     * @return Risposta del database.
     * @throws IOException se c'è un errore durante la comunicazione con il
     *                     database.
     */
    public static String sendToDB(String message) throws IOException {
        try (Socket socket = new Socket(DB_HOST, DB_PORT);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.writeBytes(message);
            System.out.println("SERVER'S SENDING: " + message.trim());
            String answer = readFromSocket(buff);
            System.out.println("SERVER'S RECEIVING: " + answer);
            return answer;
        } catch (IOException e) {
            throw new IOException("Error accessing the DB: " + e.getMessage(), e);
        }
    }

}
