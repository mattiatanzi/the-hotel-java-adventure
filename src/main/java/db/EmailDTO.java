/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

/**
 * Gestisce il database per le email del computer.
 * @author matti
 */
public class EmailDTO {
    private final int id;
    private final String mittente;
    private final String oggetto;

    public EmailDTO(int id, String mittente, String oggetto) {
        this.id = id;
        this.mittente = mittente;
        this.oggetto = oggetto;
    }

    public int getId() {
        return id;
    }

    public String getMittente() {
        return mittente;
    }

    public String getOggetto() {
        return oggetto;
    }

    @Override
    public String toString() {
        return id + " -- " + oggetto;
    }   
}