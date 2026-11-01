/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta l'inventario del giocatore.
 * Contiene tutti gli oggetti trasportati dal personaggio.
 * @author matti
 */
public class Inventory {
    private final List<AdvObject> list = new ArrayList<>();
    
    public List<AdvObject> getList() {
        return list; 
    }
    
    public void add(AdvObject o) {
        list.add(o);
    }
    
    public void remove(AdvObject o) {
        list.remove(o);
    }
    
    public boolean contains(AdvObject o) {
        return list.contains(o);
    }
    
    public boolean containsId(int id) {
        for(AdvObject obj : list) {
            if(obj.getId() == id) 
                return true;
        }
        return false;
    }
}

