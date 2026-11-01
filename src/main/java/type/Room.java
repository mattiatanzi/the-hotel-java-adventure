/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rappresenta una stanza dell'hotel.
 * Gestisce sia i testi statici dell'ambiente sia l'elenco dinamico 
 * degli oggetti in essa contenuti.
 * @author matti
 */
public class Room {
    /** L'identificativo della stanza. */
    private final int id;
    
    /** Il nome della stanza. */
    private String name;
    
    /** La descrizione statica della stanza. */
    private String description;
    
    /** La descrizione dinamica della stanza. */
    private String lookDescription;
    
    /** Oggetti presenti fisicamente nella stanza. */
    private final List<AdvObject> objects = new ArrayList<>();

    /**
     * Costruisce una nuova Room.
     * @param id Identificatore della stanza.
     * @param name Nome della stanza.
     * @param description Descrizione statica della stanza
     */
    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description; 
    }
    
    public void setLookDescription(String lookDescription) {
        this.lookDescription = lookDescription;
    }
    
    public String getLookDescription() {
        return lookDescription;
    }

    public List<AdvObject> getObjects() {
        return objects;
    }
    
    public void addObject(AdvObject object) {
        objects.add(object);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        return this.id == other.id;
    }
    
    
    
    /**
     * Costruisce la descrizione dinamica della stanza.
     * Concatena la parte statica con la lista formattata degli
     * oggetti interattivi attualmente presenti dentro la stanza.
     * @return La stringa completa della descrizione.
     */
    public String getDescrizioneDinamica() {
        StringBuilder sb = new StringBuilder();
        
        // Descrizione statica
        sb.append(description);
        
        // Descrizione dinamica, se presente
        if (lookDescription != null && !lookDescription.isEmpty()) {
            sb.append("\n").append(lookDescription);
        }
        
        // Generazione dinamica lista oggetti
        if(!objects.isEmpty()) {
            sb.append("\nAll'interno della stanza puoi notare: ");
            
            String oggettiPresenti = objects.stream()
                    .filter(obj -> obj != null && obj.isVisible()) // Filtro di sicurezza
                    .map(AdvObject::getName) // Mappatura a nome
                    .collect(Collectors.joining(", ")); // Unione nomi
            if (!oggettiPresenti.isEmpty()) {
                sb.append(oggettiPresenti).append(".");
            }
            
        }
        
        return sb.toString();
    }
}
