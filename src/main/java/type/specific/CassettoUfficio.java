/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type.specific;

import impl.AbstractGame;
import type.AdvObjectContainer;
import type.GameConstants;
import type.interfaces.Unlockable;

/**
 * Cassetto della scrivania dell'ufficio.
 * Per l'apertura richiede la lettura precedente del diario.
 * @author matti
 */
public class CassettoUfficio extends AdvObjectContainer implements Unlockable {
    
    public CassettoUfficio(int id, String name, String description) {
        super(id, name, description);
    }
    
    /**
     * @param game L'istanza di gioco
     * @param password La password di sblocco inserita
     * @return L'esito testuale dello sblocco.
     */
    @Override
    public String unlock(AbstractGame game, String password) {
        if (isOpen()) {
            return getName() + " è già aperto.";
        }
        
        boolean haLettoDiario = game.getInventory().getList().stream()
                .filter(o -> o instanceof Diario)
                .map(o -> (Diario) o)
                .anyMatch(Diario::isLetto);
        
        if (!haLettoDiario) {
            return "Il cassetto è serrato da un lucchetto a combinazione.";
        }
        
        if (password == null || !password.trim().equals(GameConstants.COMBINAZIONE_CASSETTO)) {
            return "Combinazione errata.";
        }
        
        return open(game);
    }
}