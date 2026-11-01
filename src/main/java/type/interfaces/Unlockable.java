/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package type.interfaces;

import impl.AbstractGame;

/**
 *
 * @author matti
 */
public interface Unlockable {
    /**
     * Gestisce la logica di apertura.
     * @param game L'istanza del gioco.
     * @param password La password inserita dall'utente.
     * @return L'esito dell'operazione di apertura.
     */
    String unlock(AbstractGame game, String password);
}
