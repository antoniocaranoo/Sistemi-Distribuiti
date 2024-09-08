package it.unimib.sd2024;

/**
 * Classe che rappresenta un dominio.
 */
public class Dominio {

    private int id;
    private String nome;
    private String tld;
    private String dataRegistrazione;
    private String dataScadenza;
    private String stato;
    private Integer proprietario;

    /**
     * Costruttore predefinito.
     */
    public Dominio() {
    }

    // Getters and Setters

    /**
     * Restituisce l'ID del dominio.
     *
     * @return l'ID del dominio.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID del dominio.
     *
     * @param id l'ID del dominio.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce il nome del dominio.
     *
     * @return il nome del dominio.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del dominio.
     *
     * @param nome il nome del dominio.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il TLD (Top Level Domain) del dominio.
     *
     * @return il TLD del dominio.
     */
    public String gettld() {
        return tld;
    }

    /**
     * Imposta il TLD (Top Level Domain) del dominio.
     *
     * @param tld il TLD del dominio.
     */
    public void settld(String tld) {
        this.tld = tld;
    }

    /**
     * Restituisce la data di registrazione del dominio.
     *
     * @return la data di registrazione del dominio.
     */
    public String getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * Imposta la data di registrazione del dominio.
     *
     * @param dataRegistrazione la data di registrazione del dominio.
     */
    public void setDataRegistrazione(String dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * Restituisce la data di scadenza del dominio.
     *
     * @return la data di scadenza del dominio.
     */
    public String getDataScadenza() {
        return dataScadenza;
    }

    /**
     * Imposta la data di scadenza del dominio.
     *
     * @param dataScadenza la data di scadenza del dominio.
     */
    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * Restituisce lo stato del dominio.
     *
     * @return lo stato del dominio.
     */
    public String getStato() {
        return stato;
    }

    /**
     * Imposta lo stato del dominio.
     *
     * @param stato lo stato del dominio.
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Restituisce l'ID del proprietario del dominio.
     *
     * @return l'ID del proprietario del dominio.
     */
    public Integer getProprietario() {
        return proprietario;
    }

    /**
     * Imposta l'ID del proprietario del dominio.
     *
     * @param proprietario l'ID del proprietario del dominio.
     */
    public void setProprietario(Integer proprietario) {
        this.proprietario = proprietario;
    }
}