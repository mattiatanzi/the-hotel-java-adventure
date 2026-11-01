/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package adventure;

import impl.HotelGame;
import java.awt.EventQueue;
import rete.RoomServer;
import type.Inventory;

/**
 * Classe principale di avvio.
 * * @author matti
 */
public class Main {

    /**
     * Metodo di avvio del simulatore The Hotel.
     * @param args Argomenti passati da riga di comando (ignorati).
     */
    public static void main(String[] args) {
        // Avvio del server di rete su un thread separato
        RoomServer server = new RoomServer(8888);
        Thread serverThread = new Thread(server);
        serverThread.start();
        
        // Inizializzazione dipendenze
        Inventory inv = new Inventory(); 
        HotelGame game = new HotelGame(inv);
        
        // Caricamento in memoria dei file
        game.init();
        
        // Lancio GUI
        EventQueue.invokeLater(() -> {
            HotelGUI view = new HotelGUI(game);
            
            view.mostraIntro();
            view.setVisible(true);
        });
    }
}