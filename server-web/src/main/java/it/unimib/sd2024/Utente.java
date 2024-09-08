package it.unimib.sd2024;

/**
 * Classe che rappresenta un utente.
 */
public class Utente {

    private int id;
    private String nome;
    private String cognome;
    private String email;

    /**
     * Costruttore predefinito.
     */
    public Utente() {
    }

    // Getters e Setters

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return l'ID dell'utente.
     */
    public int getid() {
        return id;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param id l'ID dell'utente.
     */
    public void setid(int id) {
        this.id = id;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome il nome dell'utente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome il cognome dell'utente.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return l'email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email l'email dell'utente.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}