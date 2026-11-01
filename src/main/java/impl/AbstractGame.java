/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import java.util.ArrayList;
import java.util.List;
import observer.GameObservable;
import observer.GameObserver;
import type.Inventory;
import type.MapGraph;
import type.Room;

/**
 * Architettura base per l'engine dell'avventura testuale.
 * Gestisce stato (mappa e inventario) dell'avventura.
 * @author matti
 */
public abstract class AbstractGame implements GameObservable {
    /** Stanza corrente in cui si trova il giocatore. */
    protected Room currentRoom;
    
    /** Inventario per la raccolta degli oggetti. */
    protected final Inventory inventory;
    /** Grafo spaziale per navigare l'ambiente. */
    protected final MapGraph<Room> mapGraph;
    
    /** Observer legati per gli aggiornamenti di stato. */
    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Inizializza le strutture primarie.
     * @param inventory L'inventario iniziale
     */
    public AbstractGame(Inventory inventory) {
        this.inventory = inventory;
        this.mapGraph = new MapGraph<>();
    }

    /**
     * Metodo astratto per il setup di gioco.
     */
    public abstract void init();

    /**
     * @return La stanza attuale.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    /**
     * Cambia la stanza e notifica gli elementi legati.
     * @param currentRoom La destinazione.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
        notifyObservers();
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public MapGraph<Room> getMapGraph() {
        return mapGraph;
    }

    @Override
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this); 
        }
    }   
    
    // Operazioni astratte
    
    /**
     * Restituisce il testo introduttivo dell'avventura.
     * @return Stringa contenente l'intro narrativa.
     */
    public abstract String getIntro();
    
    /**
     * Gestisce lo spostamento del giocatore in una specifica direzione.
     * @param direzione La direzione verso cui muoversi.
     * @return L'esito testuale del movimento.
     */
    public abstract String muoviGiocatore(type.Direction direzione);
    
    /**
     * Permette al giocatore di raccogliere un oggetto presente nella stanza.
     * @param obj L'oggetto da raccogliere.
     * @return L'esito testuale della raccolta.
     */
    public abstract String raccogliOggetto(type.AdvObject obj);
    
    /**
     * Permette al giocatore di aprire un oggetto apribile.
     * @param obj L'oggetto da aprire.
     * @return L'esito testuale dell'apertura.
     */
    public abstract String apriOggetto(type.AdvObject obj);
    
    /**
     * Combina un oggetto nell'inventrario con un target nello scenario.
     * @param strumento L'oggetto posseduto dal giocatore.
     * @param target L'oggetto target nello scenario di gioco.
     * @return L'esito testuale della combinazione.
     */
    public abstract String usaOggettoSuTarget(type.AdvObject strumento, type.AdvObject target);
}