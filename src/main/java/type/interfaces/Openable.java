/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package type.interfaces;

import impl.AbstractGame;

/**
 * Questa interfaccia segnala che un
 * oggetto può essere aperto
 * @author matti
 */
public interface Openable {
    boolean isOpen();
    
    void setOpen(boolean open);
    
    /**
     * Gestisce la logica di apertura.
     * @param game L'istanza di gioco.
     * @return L'esito dell'apertura.
     */
    String open(AbstractGame game);
}
