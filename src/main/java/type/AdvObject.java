/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type;

/**
 * Rappresenta un oggetto all'interno dell'hotel.
 * Questa classe gestisce gli oggetti semplici.
 * 
 * @author matti
 */
public class AdvObject {
    
    /** L'identificatore dell'oggetto. */
    private final int id;
    
    /** Il nome dell'oggetto. */
    private String name;
    
    /** La descrizione dell'oggetto. */
    private String description;
    
    /** Flag per gestire la visibilità dell'oggetto sulla mappa. */
    private boolean visible = true;
    /**
     * Costruisce un nuovo oggetto dell'hotel.
     * @param id L'identificatore dell'oggetto.
     * @param name Il nome dell'oggetto.
     * @param description La descrizione dell'oggetto.
     */
    public AdvObject(int id, String name, String description) {
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + this.id;
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
        final AdvObject other = (AdvObject) obj;
        return this.id == other.id;
    }
    
    public String toString() {
        return this.name;
    }
}
