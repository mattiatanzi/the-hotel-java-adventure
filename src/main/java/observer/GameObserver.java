/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import impl.AbstractGame;

/**
 * Interfaccia che definisce il comportamento di un Observer.
 * Ogni componente che necessita aggiornamento deve implementarla.
 * @author matti
 */
public interface GameObserver {
    
    /**
     * Aggiorna lo stato di gioco.
     * @param game 
     */
    void update(AbstractGame game);
}
