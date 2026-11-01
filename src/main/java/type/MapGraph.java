/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package type;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta una mappa di gioco generica basata su un grafo orientato.
 * @author matti
 * @param <T> Il tipo di nodo del grafo
 */
public class MapGraph<T> {
    // Struttura principale, ad ogni Room associa una mappa Direction -> Room
    private final Map<T, Map<Direction, T>> mapG;

    public MapGraph() {
        this.mapG = new HashMap<>();
    }
    
    /** 
     * Aggiunge una Nodo al grafo.
     * @param nodo Il nodo da aggiungere
     */
    public void addNodo(T nodo) {
        mapG.putIfAbsent(nodo, new HashMap<>());
    }
    
    /**
     * Crea un collegamento orientato tra due nodi.
     * 
     * @param source Il nodo di partenza.
     * @param direction La direzione (es: "nord").
     * @param destination Il nodo di arrivo.
     */
    public void addEdge(T source, Direction direction, T destination) {
        if (!mapG.containsKey(source)) {
            addNodo(source);        
        }
        if (!mapG.containsKey(destination)) {
            addNodo(destination);
        }
        // Aggiunta dell'arco
        mapG.get(source).put(direction, destination);
    }
    
    /**
     * Restituisce il nodo adiacente dato un nodo di partenza e una direzione.
     * @param currentNodo Il nodo in cui si trova il giocatore.
     * @param direction La direzione in cui si vuole andare.
     * @return Il nodo di destinazione oppure null se non si può.
     * 
     */
    public T getAdiacente(T currentNodo, Direction direction) {
        Map<Direction, T> edges = mapG.get(currentNodo);
        if (edges != null) {
            return edges.get(direction);
        }
        return null;
    }
}