package it.unimib.sd2024;

/**
 * Classe che rappresenta un ordine.
 */
public class Ordine {

    private int idOrdine;
    private int idUtente;
    private int idDominio;
    private String dataOrdine;
    private String oggetto;
    private double importoPagato;

    /**
     * Costruttore predefinito.
     */
    public Ordine() {
    }

    // Getters e Setters

    /**
     * Restituisce l'ID dell'ordine.
     *
     * @return l'ID dell'ordine.
     */
    public int getIdOrdine() {
        return idOrdine;
    }

    /**
     * Imposta l'ID dell'ordine.
     *
     * @param idOrdine l'ID dell'ordine.
     */
    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return l'ID dell'utente.
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param idUtente l'ID dell'utente.
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce l'ID del dominio.
     *
     * @return l'ID del dominio.
     */
    public int getIdDominio() {
        return idDominio;
    }

    /**
     * Imposta l'ID del dominio.
     *
     * @param idDominio l'ID del dominio.
     */
    public void setIdDominio(int idDominio) {
        this.idDominio = idDominio;
    }

    /**
     * Restituisce la data dell'ordine.
     *
     * @return la data dell'ordine.
     */
    public String getDataOrdine() {
        return dataOrdine;
    }

    /**
     * Imposta la data dell'ordine.
     *
     * @param dataOrdine la data dell'ordine.
     */
    public void setDataOrdine(String dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    /**
     * Restituisce l'oggetto dell'ordine.
     *
     * @return l'oggetto dell'ordine.
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * Imposta l'oggetto dell'ordine.
     *
     * @param oggetto l'oggetto dell'ordine.
     */
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    /**
     * Restituisce l'importo pagato per l'ordine.
     *
     * @return l'importo pagato per l'ordine.
     */
    public double getImportoPagato() {
        return importoPagato;
    }

    /**
     * Imposta l'importo pagato per l'ordine.
     *
     * @param importoPagato l'importo pagato per l'ordine.
     */
    public void setImportoPagato(double importoPagato) {
        this.importoPagato = importoPagato;
    }
}