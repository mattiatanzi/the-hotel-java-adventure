/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package type;

/**
 * Enumerazione per gestire le direzioni di movimento.
 * @author matti
 */
public enum Direction {
    NORD, SUD, EST, OVEST;
    
    /**
     * Converte una stringa nella rispettiva costante enumerata.
     * @param dir La stringa da convertire.
     * @return La costante Direction, oppure null
     * @throws IllegalArgumentException se la stringa non corrisponde a una direzione valida
     */
    public static Direction fromString(String dir) {
        if (dir == null) return null;
        switch(dir.trim().toLowerCase()) {
            case "nord" -> {
                return NORD;
            }
            case "sud" -> {
                return SUD;
            }
            case "est" -> {
                return EST;
            }
            case "ovest" -> {
                return OVEST;
            }
            default -> throw new IllegalArgumentException("Direzione non valida: " + dir);
        }
    }
}
