/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package type.interfaces;

import impl.AbstractGame;
import type.AdvObject;

/**
 * Questa interfaccia indica se un oggetto
 * è combinabile.
 * @author matti
 */
public interface Combinable {
    /**
     * Gestisce la logica di combinazione tra oggetti.
     * @param game L'istanza di gioco.
     * @param item L'oggetto da combinare selezionato
     * @return L'esito dell'operazione.
     */
    String combine(AbstractGame game, AdvObject item);
}
