/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type.specific;

import type.PickupableObject;

/**
 * Classe relativa al diario del direttore.
 * @author matti
 */
public class Diario extends PickupableObject {
    
    private boolean letto = false;

    public Diario(int id, String name, String description) {
        super(id, name, description);
    }
    
    /**
     * Imposta lo stato di lettura del diario
     * @param letto true se il diario è stato letto, false altrimenti
     */
    public void setLetto(boolean letto) {
        this.letto = letto;
    }
    
    public boolean isLetto() {
        return letto;
    }
}
