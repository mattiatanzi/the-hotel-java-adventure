/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package observer;

/**
 * Interfaccia che rappresenta il soggetto osservabile.
 * Mantiene nel contesto di gioco una collezione di {@link GameObserver} e si occupa
 * di avvisarli quando si verifica un mutamento di stato nel gioco.
 * @author matti
 */
public interface GameObservable {
    
    /**
     * Registra un nuovo observer alla lista.
     * @param observer L'oggetto che desidera iscriversi.
     */
    void addObserver(GameObserver observer);
    
    /**
     * Rimuove un observer registrato dalla lista.
     * @param observer L'oggetto che desidera disicriversi.
     */
    void removeObserver(GameObserver observer);
    
    /**
     * Notifica tutti gli observer registrati.
     */
    void notifyObservers();
}
