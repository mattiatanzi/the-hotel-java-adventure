/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package type.interfaces;

import type.AdvObject;

/**
 * Interfaccia funzionale custom per creare oggetti di gioco.
 * @author matti
 */
@FunctionalInterface
public interface AdvObjectMaker {
    AdvObject create(int id, String name, String description);
}
