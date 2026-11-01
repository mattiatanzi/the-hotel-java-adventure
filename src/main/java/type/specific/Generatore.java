/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type.specific;

import impl.AbstractGame;
import impl.HotelGame;
import type.AdvObject;
import type.GameConstants;
import type.interfaces.Combinable;

/**
 * Oggetto interattivo, serve a ripristinare l'elettricità combinato 
 * con il corretto strumento
 * @author matti
 */
public class Generatore extends AdvObject implements Combinable {
    
    public Generatore(int id, String name, String description) {
        super(id, name, description);
    }
    
    @Override
    public String combine(AbstractGame game, AdvObject item) {
        // Verifica che stiamo usando il Fusibile
        if (item.getId() == GameConstants.FUSIBILE) {
            HotelGame hotel = (HotelGame) game;
            hotel.getInventory().remove(item);
            
            // Thread del countdown
            hotel.avviaGeneratoreBackup();
            return "Inserisci il Fusibile nel vano del Generatore. La corrente è stata ripristinata ma non durerà molto.";
        }
        return "Non riesci a capire come usare " + item.getName() + " sul Generatore.";
    }
}
