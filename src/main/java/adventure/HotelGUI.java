/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package adventure;

import db.EmailDTO;
import impl.AbstractGame;
import impl.HotelGame;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import observer.GameObserver;
import type.AdvObject;
import type.Direction;
import type.GameConstants;
import type.interfaces.Combinable;
import type.interfaces.Openable;
import type.interfaces.Pickupable;
import type.interfaces.Unlockable;

/**
 * Vista principale del gioco. Si aggiorna
 * dinamicamente al variare dello stato.
 * @author matti
 */
public class HotelGUI extends JFrame implements GameObserver {
    
    /** Riferimento al gioco */
    private final AbstractGame game;
    
    /** Bottone schermata iniziale. */
    private final JButton btnInizia;
    
    /** Bottone schermata finale. */
    private JButton btnFine;
    
    /** Indice fase dell'outro. */
    private int indiceOutro = 0;
    
    /** Array per memorizzare le fasi dell'outro. */
    private String[] frasiOutro;
    
    /** Array per memorizzare le fasi dell'introduzione. */
    private String[] frasiIntro;
    
    /** Indice fase dell'intro. */
    private int indiceIntro = 0;
    
    /**
     * Crea una nuova finestra di gioco.
     * @param game Istanza del gioco
     */
    public HotelGUI(AbstractGame game) {
        this.game = game;
        initComponents();
        btnNord.setForeground(Color.WHITE);
        btnSud.setForeground(Color.WHITE);
        btnEst.setForeground(Color.WHITE);
        btnOvest.setForeground(Color.WHITE);
        
        // Schermata avvio
        jPanel3.setVisible(false);
        jPanel2.setVisible(false);
        
        btnInizia = new JButton("Avanti");
        btnInizia.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnInizia.setBackground(new Color(51, 51, 51));
        btnInizia.setForeground(Color.WHITE);
        
        btnInizia.addActionListener(e -> avanzaIntro());
        jPanel1.add(btnInizia);
        
        // Configurazione
        setupListeners();        
        setTitle("The Hotel - Adventure");
        setLocationRelativeTo(null);
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    /**
     * Mostra la schermata iniziale
     */
    public void mostraIntro() {
        String intro = game.getIntro();
        
        // Separa le frasi dell'intro
        frasiIntro = intro.split("\\.\\.\\.");
        indiceIntro = 0;
        textAreaOutput.setText("");
        
        // Stampa la prima fasa
        avanzaIntro();
    }
     /**
      * Gestisce l'avanzamento dell'intro
      */
    public void avanzaIntro() {
        // Verifica se ci sono altre frasi
        if (frasiIntro != null && indiceIntro < frasiIntro.length) {
            // Fase attuale pulita
            String frammento = frasiIntro[indiceIntro].trim();
            
            if (!frammento.isEmpty()) {
                textAreaOutput.append(frammento + "...\n");
            }
            
            indiceIntro++;
            
            if (indiceIntro >= frasiIntro.length) {
                btnInizia.setText("Inizia l'avventura");
            }
        } else {
            avviaAvventura();
        }
    }
    
    /**
     * Rivela l'interfaccia di gioco e innesca il resto dell'avvio
     */
    public void avviaAvventura() {
        jPanel1.remove(btnInizia);
        jPanel3.setVisible(true);
        jPanel2.setVisible(true);
    
        game.addObserver(this);
        update(game);
        
        this.revalidate();
        this.repaint();
        jPanel1.revalidate();
        jPanel1.repaint();
    }
    
    /**
     * Associa le azioni ai pulsanti.
     */
    private void setupListeners() {
        btnNord.addActionListener((ActionEvent e) -> eseguiMovimento(Direction.NORD));
        btnSud.addActionListener((ActionEvent e) -> eseguiMovimento(Direction.SUD));
        btnOvest.addActionListener((ActionEvent e) -> eseguiMovimento(Direction.OVEST));
        btnEst.addActionListener((ActionEvent e) -> eseguiMovimento(Direction.EST));
    }
    
    /**
     * Gestisce la logica di click per il movimento.
     * @param dir La direzione scelta dall'utente.
     */
    private void eseguiMovimento(Direction dir) {
        game.muoviGiocatore(dir);
    }
    
    /**
     * Metodo per l'aggiornamento dello stato.
     * @param game Istanza aggiornata del gioco.
     */
    @Override
    public void update(AbstractGame game) {
        // Aggiornamento finestra narrativa
        SwingUtilities.invokeLater( () -> {
            textAreaOutput.setText("");
            textAreaOutput.append("--- " + game.getCurrentRoom().getName().toUpperCase() + " ---\n");

            // Descrizione
            textAreaOutput.append("\n---" + game.getCurrentRoom().getDescrizioneDinamica() + "\n");

            // Gestione pulsanti in base alle direzioni disponibili
            btnNord.setForeground(game.getMapGraph().getAdiacente(game.getCurrentRoom(), Direction.NORD) != null ? Color.WHITE : Color.GRAY);
            btnSud.setForeground(game.getMapGraph().getAdiacente(game.getCurrentRoom(), Direction.SUD) != null ? Color.WHITE : Color.GRAY);
            btnOvest.setForeground(game.getMapGraph().getAdiacente(game.getCurrentRoom(), Direction.OVEST) != null ? Color.WHITE : Color.GRAY);
            btnEst.setForeground(game.getMapGraph().getAdiacente(game.getCurrentRoom(), Direction.EST) != null ? Color.WHITE : Color.GRAY);

            
            
            // Avvisi thread
            String avviso = ((HotelGame)game).getAvvisoGeneratore();
            if (avviso != null && !avviso.isEmpty()) {
                textAreaOutput.append("\nATTENZIONE: " + avviso);
            }
            
            // Esito ultima azione del giocatore
            String ultimoEsito = ((HotelGame)game).getUltimoEsito();
            if (ultimoEsito != null && !ultimoEsito.isEmpty()) {
                textAreaOutput.append("\n> " + ultimoEsito);
            }
            
            // Aggiornamento
            aggiornaPannelliOggetti(game);
            });
  
        
    }
    
    /**
     * Crea dinamicamente bottoni.
     * Svuota e ripopola il pannello dei bottoni ad ogni cambio stato.
     * @param game L'istanza aggiornata del gioco.
     */
    private void aggiornaPannelliOggetti(AbstractGame game) {
        // Pulizia bottoni vecchi
        panelStanza.removeAll();
        panelInventario.removeAll();
        
        // Configurazione stile bottoni
        Font stileAvventura = new Font("Times New Roman", Font.BOLD, 12);
        Color coloreSfondo = new Color(51, 51, 51);
        Color coloreTesto = Color.WHITE;
        
        // Generazione dinamica bottoni per oggetti in stanza
        for (AdvObject obj : game.getCurrentRoom().getObjects()) {
            
            if (obj.isVisible()) {
                JButton btnObj = new JButton(obj.getName());

                // Applicazione stile ai bottoni
                btnObj.setFont(stileAvventura);
                btnObj.setBackground(coloreSfondo);
                btnObj.setForeground(coloreTesto);

                btnObj.addActionListener(e -> {
                    
                    // Logica per il generatore
                    if (obj.getId() == GameConstants.GENERATORE &&
                            ((HotelGame)game).isFusibileInserito() &&
                            !((HotelGame)game).isCorrenteRipristinata()) {
                        
                        int scelta = JOptionPane.showConfirmDialog(
                            this,
                            "Il generatore è spento e contiene un fusibile, vuoi riavviarlo?",
                            "Generatore",
                            JOptionPane.YES_NO_OPTION
                        );

                        ((HotelGame)game).gestisciRiavvioGeneratore(scelta == JOptionPane.YES_OPTION);
                        return;
                    }
                    
                    // Logica per il db del pc
                    if (obj.getId() == GameConstants.PC && ((HotelGame) game).isPcSbloccato()) {
                        HotelGame hotel = (HotelGame) game;
                        
                        Object[] opzioniMenu = {"Leggere Email", "Sblocco remoto stanze"};
                        int scelta = JOptionPane.showOptionDialog(
                                this,
                                "Scegli l'operazione da eseguire:",
                                "Terminale Reception",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                opzioniMenu,
                                opzioniMenu[0]
                        );
                        
                        if (scelta == 0) {
                           
                            List<EmailDTO> emails = hotel.getEmailDisponibili();
                            EmailDTO selezionata = (EmailDTO) JOptionPane.showInputDialog(
                                this,
                                "Seleziona una mail da leggere:", 
                                "Client Email Reception", 
                                JOptionPane.PLAIN_MESSAGE, 
                                null, 
                                emails.toArray(),
                                null
                            );

                            if (selezionata != null) {
                                hotel.segnaMail(selezionata);
                            }
                            
                        } else if (scelta == 1) {
                            
                            // Controllo sblocco stanza
                            if (hotel.isStanza301Sbloccata()) {
                                hotel.gestisciSbloccoRemoto(null);
                                return;
                            }
                            
                            String inputStanza = JOptionPane.showInputDialog(this, "Inserire il numero della stanza da sbloccare:");
                            if (inputStanza != null && !inputStanza.trim().isEmpty()) {
                                Thread threadSblocco = new Thread(() -> {
                                    Socket socket = null;
                                    PrintWriter out = null;
                                    BufferedReader in = null;

                                    try {
                                        socket = new Socket("127.0.0.1", 8888);
                                        out = new PrintWriter(socket.getOutputStream(), true);
                                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                        out.println(inputStanza);
                                        String risposta = in.readLine();

                                        SwingUtilities.invokeLater(() -> hotel.gestisciSbloccoRemoto(risposta));

                                    } catch (IOException ex) {
                                        SwingUtilities.invokeLater(hotel::notificaErroreConnessione);
                                    } finally {
                                        if (out != null) {
                                            out.close();
                                        }
                                        try {
                                            if (in != null) {
                                                in.close();
                                            }
                                        } catch (IOException ex) {}
                                        try {
                                            if (socket != null) {
                                                socket.close();
                                            }
                                        } catch (IOException ex) {}
                                    }
                                });

                                threadSblocco.start();
                            }                            
                            return;
                        }
                        
                        return;
                    }

                    if (obj instanceof Pickupable) {
                        game.raccogliOggetto(obj);
                    } else if (obj instanceof Unlockable && (!(obj instanceof Openable) || !((Openable)obj).isOpen())) {
                        String password = JOptionPane.showInputDialog(this, "Inserire la combinazione/password:");
                        if (password == null) {
                            return;
                        }
                        ((HotelGame)game).sbloccaOggetto(obj, password);
                    } else if (obj instanceof Openable && !((Openable) obj).isOpen()) {
                        game.apriOggetto(obj);
                    } else if (obj instanceof Combinable) {

                        AdvObject selezionato = (AdvObject) JOptionPane.showInputDialog(this,
                            "Scegli un oggetto dell'inventario",
                            "Usa oggetto",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            game.getInventory().getList().toArray(),
                            null);

                        if (selezionato == null) {
                            return;
                        }

                        game.usaOggettoSuTarget(selezionato, obj);
                    } else {
                        ((HotelGame)game).esaminaOggetto(obj);
                    }
                });
                
                 panelStanza.add(btnObj);
            }
        }
        
        // Gestione inventario
        for (AdvObject obj : game.getInventory().getList()) {
            JButton btnInv = new JButton(obj.getName());
            
            // Applicazione stile ai bottoni
            btnInv.setFont(stileAvventura);
            btnInv.setBackground(coloreSfondo);
            btnInv.setForeground(coloreTesto);
            
            btnInv.addActionListener(e -> ((HotelGame)game).esaminaOggetto(obj));
            panelInventario.add(btnInv);
        }
        
        // Aggiornamento
        panelStanza.revalidate();
        panelStanza.repaint();
        panelInventario.revalidate();
        panelInventario.repaint();
    }
    
    /**
     * Configura l'interfaccia per la schermata finale.
     */
    public void mostraOutro() {
        jPanel3.setVisible(false);
        jPanel2.setVisible(false);
        
        // Scolleghiamo dagli aggiornamenti per la schermata finale
        game.removeObserver(this);
        
        String outro = ((HotelGame)game).getOutro();
        frasiOutro = outro.split("\\.\\.\\.");
        indiceOutro = 0;
        textAreaOutput.setText("");
        
        btnFine = new JButton("Avanti");
        btnFine.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnFine.setBackground(new Color(51, 51, 51));
        btnFine.setForeground(Color.WHITE);
        
        btnFine.addActionListener(e -> avanzaOutro());
        jPanel1.add(btnFine);
        
        jPanel1.revalidate();
        jPanel1.repaint();
        
        avanzaOutro();
    }
    
    /**
     * Gestisce l'avanzamento del testo dell'outro e chiude il gioco.
     */
    public void avanzaOutro() {
        if (frasiOutro != null && indiceOutro < frasiOutro.length) {
            String pezzo = frasiOutro[indiceOutro].trim();
            if (!pezzo.isEmpty()) {
                textAreaOutput.append(pezzo + "...\n");
            }
            
            indiceOutro++;
            if (indiceOutro >= frasiOutro.length) {
                btnFine.setText("Chiudi Gioco");
            }
        } else {
            // Quando il testo termina chiude l'applicazione
            System.exit(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaOutput = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnNord = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnOvest = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnEst = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnSud = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelStanza = new javax.swing.JPanel();
        panelInventario = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnRapporto = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        textAreaOutput.setEditable(false);
        textAreaOutput.setBackground(new java.awt.Color(0, 0, 0));
        textAreaOutput.setColumns(20);
        textAreaOutput.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        textAreaOutput.setForeground(new java.awt.Color(255, 255, 255));
        textAreaOutput.setRows(5);
        jScrollPane1.setViewportView(textAreaOutput);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 150));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new java.awt.GridLayout(3, 3));

        jLabel5.setPreferredSize(new java.awt.Dimension(25, 25));
        jPanel3.add(jLabel5);

        btnNord.setBackground(new java.awt.Color(51, 51, 51));
        btnNord.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnNord.setText("N");
        btnNord.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnNord.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(btnNord);

        jLabel4.setPreferredSize(new java.awt.Dimension(25, 25));
        jPanel3.add(jLabel4);

        btnOvest.setBackground(new java.awt.Color(51, 51, 51));
        btnOvest.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnOvest.setText("O");
        btnOvest.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnOvest.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(btnOvest);

        jLabel3.setPreferredSize(new java.awt.Dimension(25, 25));
        jPanel3.add(jLabel3);

        btnEst.setBackground(new java.awt.Color(51, 51, 51));
        btnEst.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnEst.setText("E");
        btnEst.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnEst.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(btnEst);

        jLabel7.setPreferredSize(new java.awt.Dimension(25, 25));
        jPanel3.add(jLabel7);

        btnSud.setBackground(new java.awt.Color(51, 51, 51));
        btnSud.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnSud.setText("S");
        btnSud.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnSud.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(btnSud);

        jLabel6.setPreferredSize(new java.awt.Dimension(25, 25));
        jPanel3.add(jLabel6);

        jPanel1.add(jPanel3);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new java.awt.GridLayout(3, 1));

        panelStanza.setBackground(new java.awt.Color(0, 0, 0));
        panelStanza.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OGGETTI NELLA STANZA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        panelStanza.setForeground(new java.awt.Color(255, 255, 255));
        panelStanza.setMinimumSize(new java.awt.Dimension(100, 100));
        panelStanza.setPreferredSize(new java.awt.Dimension(200, 200));
        panelStanza.setLayout(new javax.swing.BoxLayout(panelStanza, javax.swing.BoxLayout.Y_AXIS));
        jPanel2.add(panelStanza);

        panelInventario.setBackground(new java.awt.Color(0, 0, 0));
        panelInventario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INVENTARIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        panelInventario.setForeground(new java.awt.Color(255, 255, 255));
        panelInventario.setLayout(new javax.swing.BoxLayout(panelInventario, javax.swing.BoxLayout.Y_AXIS));
        jPanel2.add(panelInventario);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        btnRapporto.setBackground(new java.awt.Color(51, 51, 51));
        btnRapporto.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnRapporto.setForeground(new java.awt.Color(255, 255, 255));
        btnRapporto.setText("Consulta Taccuino");
        btnRapporto.setPreferredSize(new java.awt.Dimension(150, 50));
        btnRapporto.addActionListener(this::btnRapportoActionPerformed);
        jPanel4.add(btnRapporto);

        jPanel2.add(jPanel4);

        getContentPane().add(jPanel2, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Gestisce l'azione di click sul pulsante del rapporto d'indagine.
     * Valuta le condizioni di vittoria (che consistono nel possedere il fermacarte e aver letto tutte 
     * le mail contenenti un ricatto). In caso positivo si passa alla schermata finale.
     * @param evt L'evento generato dall'azione di premere il bottone.
     */
    private void btnRapportoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRapportoActionPerformed
        HotelGame hotel = (HotelGame) game;
        
        // Verifica sul possedimento dei due oggetti per la vittoria
        if (hotel.getInventory().containsId(GameConstants.FERMACARTE) && hotel.getInventory().containsId(GameConstants.PROVE_RICATTO)) {
            mostraOutro();
        } else {
            String rapporto = hotel.generaRapportoIndagine();
            textAreaOutput.append(rapporto + "\n");
        }
    }//GEN-LAST:event_btnRapportoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEst;
    private javax.swing.JButton btnNord;
    private javax.swing.JButton btnOvest;
    private javax.swing.JToggleButton btnRapporto;
    private javax.swing.JButton btnSud;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelInventario;
    private javax.swing.JPanel panelStanza;
    private javax.swing.JTextArea textAreaOutput;
    // End of variables declaration//GEN-END:variables
}
