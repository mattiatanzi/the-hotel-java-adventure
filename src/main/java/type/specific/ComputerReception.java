/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type.specific;

import impl.AbstractGame;
import impl.HotelGame;
import type.AdvObject;
import type.GameConstants;
import type.interfaces.Unlockable;

/**
 * Oggetto con enigma basato su input testuale di una password.
 * @author matti
 */
public class ComputerReception extends AdvObject implements Unlockable {
    
    public ComputerReception(int id, String name, String description) {
        super(id, name, description);
    }
    
    @Override
    public String unlock(AbstractGame game, String password) {
        HotelGame hotel = (HotelGame) game;
        
        // Verifiche sullo stato
        if (hotel.isPcSbloccato()) {
            return "Il sistema è già operativo, le mail mostrano i dettagli del ricatto.";
        }
        if (!hotel.isCorrenteRipristinata()) {
            return "Il monitor è spento e privo di segnale, impossibile procedere.";
        }
        
        // Controllo sulla password
        if (password != null && password.trim().equalsIgnoreCase(GameConstants.PASSWORD_PC)) {
            hotel.setPcSbloccato(true);
            return "Autenticazione riuscita. Hai sbloccato il portale.";
        }
        return "Chiave di accesso errata.";
    }
}
