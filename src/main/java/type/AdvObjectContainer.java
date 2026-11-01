/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type;

import impl.AbstractGame;
import java.util.ArrayList;
import java.util.List;
import type.interfaces.Openable;

/**
 * Rappresenta un oggetto contenitore all'interno del gioco.
 * Estende la classe base {@link AdvObject} aggiungendo la capacità di
 * contenere altri oggetti.
 * @author matti
 * @see AdvObject
 */
public class AdvObjectContainer extends AdvObject implements Openable {
    
    /** Lista degli oggetti contenuti all'interno del contenitore. */
    private final List<AdvObject> list = new ArrayList<>();
    
    /** Stato corrente del contenitore. */
    private boolean open = false;
    
    /**
     * Costruisce un nuovo oggetto contenitore.
     * @param id Identificatore univoco dell'oggetto.
     * @param name Il nome dell'oggetto.
     * @param description La descrizione dell'oggetto
     */
    public AdvObjectContainer(int id, String name, String description) {
        super(id, name, description);
    }

    /**
     * Inserisce un oggetto all'interno del contenitore.
     * @param o L'oggetto da aggiungere.
     */
    public void add(AdvObject o) {
        list.add(o);
    }
    
    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String open(AbstractGame game) {
        if (isOpen()) {
            return getName() + " è già aperto.";
        }
        
        setOpen(true);
        
        if (!list.isEmpty()) {
            list.forEach(game.getCurrentRoom()::addObject);
            list.clear(); 
            return "Aprendo " + getName() + " riveli gli oggetti custoditi al suo interno.";
        }
        return "Hai aperto " + getName() + ", ma è vuoto.";
    }
}
