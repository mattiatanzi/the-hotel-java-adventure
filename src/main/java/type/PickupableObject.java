/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type;

import type.interfaces.Pickupable;

/**
 *
 * @author matti
 */
public class PickupableObject extends AdvObject implements Pickupable {
    
    public PickupableObject(int id, String name, String description) {
        super(id, name, description);
    }
}