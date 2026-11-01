/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestisce un server in esecuzione su thread separato.
 * Serve ad elaborare le richieste di sblocco remoto per le stanze dell'hotel.
 * @author matti
 */
public class RoomServer implements Runnable {
    
    private final int port;
    private boolean running = true;
    private ServerSocket serverSocket;

    public RoomServer(int port) {
        this.port = port;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            
            while (running) {
                Socket clientSocket = null;
                BufferedReader in = null;
                PrintWriter out = null;
                
                try {
                    clientSocket = serverSocket.accept();
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String inputLine = in.readLine();
                    if (inputLine != null) {
                        if (inputLine.trim().equals("301")) {
                            out.println("Sblocco remoto andato a buon fine, la stanza 301 è ora accessibile");
                        } else {
                            out.println("Stanza attualmente occupata o non disponibile");
                        }
                    }
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Il server è stato chiuso. Arresto in corso...");
                    } else {
                        System.out.println("Errore di comunicazione col client: " + e.getMessage());
                    }
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        try {
                            in.close(); 
                        } catch (IOException e) {}
                    }
                    if (clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {}
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Impossibile avviare il server sulla porta " + port);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
    
    public void fermaServer() {
        this.running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura del server: " + e.getMessage());
        }
    }
}