/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package impl;
import db.EmailDTO;
import db.MailDAO;
import type.Direction;
import type.Inventory;
import type.Room;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import type.AdvObject;
import type.AdvObjectContainer;
import type.GameConstants;
import type.PickupableObject;
import type.interfaces.AdvObjectMaker;
import type.interfaces.Combinable;
import type.interfaces.Openable;
import type.interfaces.Pickupable;
import type.interfaces.Unlockable;
import type.specific.CassettoUfficio;
import type.specific.ComputerReception;
import type.specific.Diario;
import type.specific.Generatore;

/**
 * Classe principale del motore di gioco, rappresenta fisicamente l'hotel.
 * Gestisce l'inizializzazione della mappa, il caricamento di file esterni
 * e mantiene lo stato corrente della partita.
 * 
 * @author matti
 */
public class HotelGame extends AbstractGame {
    
    // Varibili di stato per logiche di gioco
    private boolean correnteRipristinata = false;
    private boolean pcSbloccato = false;
    private boolean stanza301Sbloccata = false;
    private boolean fusibileInserito = false;
    private String avvisoGeneratore = "";
    private String ultimoEsito = "";
    private Thread threadGeneratore;
    
    
    /** Mappa per indicizzare la stanze tramite ID durante il caricamento. */
    private final Map<Integer, Room> stanze = new HashMap<>();
    
    /** Mappa di supporto, associa l'ID dell'oggetto alla stanza da cui proviene. */
    private final Map<Integer, String> provenienzaOggetti = new HashMap<>();
    
    /** Mappa per l'istanziazzione dinamica degli oggetti. */
    private final Map<Integer, AdvObjectMaker> objectMakerMap = new HashMap<>();
    
    /** Oggetto per la comunicazione con il db. */
    private final MailDAO mailDAO;
    
    /**
     * Inizializza il grafo della mappa e avvia il caricamento 
     * delle risorse.
     * @param inventory L'inventario iniziale del giocatore
     */
    public HotelGame(Inventory inventory) {
        super(inventory);
        this.mailDAO = new MailDAO();
        
        objectMakerMap.put(GameConstants.PC, ComputerReception::new);
        objectMakerMap.put(GameConstants.CASSETTO, CassettoUfficio::new);
        objectMakerMap.put(GameConstants.GENERATORE, Generatore::new);
        objectMakerMap.put(GameConstants.DIARIO, Diario::new);
    }
    
    /**
     * Metodo di inizializzazione del gioco.
     * Carica tutte le risorse e imposta la stanza iniziale.
     */
    @Override
    public void init() {
        mailDAO.initDB(); // Inizializza e popola db
        caricaStanze(); // Inizializza stanze
        caricaCollegamenti(); // Inizializza mappa
        caricaOggetti(); // Inizializza oggetti
        // Imposta la stanza di partenza
        this.currentRoom = stanze.get(GameConstants.HALL);
    }
    
    // Metodi di Setup
    
    public boolean isCorrenteRipristinata() {
        return correnteRipristinata;
    }
    
    public void setCorrenteRipristinata(boolean b) {
        this.correnteRipristinata = b;
    }
    
    public boolean isPcSbloccato() {
        return pcSbloccato;
    }
    
    public void setPcSbloccato(boolean b) {
        this.pcSbloccato = b;
    }
    
    public boolean isStanza301Sbloccata() {
        return stanza301Sbloccata;
    }
    
    public boolean isFusibileInserito() {
        return fusibileInserito;
    }

    public void setFusibileInserito(boolean fusibileInserito) {
        this.fusibileInserito = fusibileInserito;
    }
    
    public void setStanza301Sbloccata(boolean b) {
        this.stanza301Sbloccata = b;
    }

    public void setAvvisoGeneratore(String avvisoGeneratore) {
        this.avvisoGeneratore = avvisoGeneratore;
    }

    public List<EmailDTO> getEmailDisponibili() {
        return mailDAO.getListaEmail();
    }

    public String getAvvisoGeneratore() {
        return avvisoGeneratore;
    }

    public String getUltimoEsito() {
        return ultimoEsito;
    }

    public void setUltimoEsito(String ultimoEsito) {
        this.ultimoEsito = ultimoEsito;
    }
    
    /**
     * Avvia il thread del generatore di backup.
     * Gestisce il timer e le notifiche a schermo.
     */
    public void avviaGeneratoreBackup() {
        
        if (threadGeneratore != null && threadGeneratore.isAlive()) {
            threadGeneratore.interrupt();
        }
        setCorrenteRipristinata(true);
        setFusibileInserito(true);
        setAvvisoGeneratore("Capacità residua energia di backup al 100%");
        
        threadGeneratore = new Thread( () -> {
            try {
                int[] percentuali = {75, 50, 25};
                
                Thread.sleep(30000);
                
                for (int p : percentuali) {
                    final int currentP = p;
                    
                    SwingUtilities.invokeLater( () -> {
                        setAvvisoGeneratore("Capacità residua energia di backup al " + currentP + "%");
                        notifyObservers();
                    });
                    
                    Thread.sleep(30000);
                }
                
                SwingUtilities.invokeLater( () -> {
                    setCorrenteRipristinata(false);
                    setPcSbloccato(false);
                    setAvvisoGeneratore("Capacità di backup terminata, il generatore si è spento e dev'essere riavviato.");
                    notifyObservers();
                });
                
                Thread.sleep(6000);
                SwingUtilities.invokeLater( () -> {
                    setAvvisoGeneratore("");
                    notifyObservers();
                });
                
            } catch (InterruptedException ex) {
                System.out.println("Countdown del generatore precedente annullato");
            }
        });
        
        threadGeneratore.start();
    }
    
    /**
     * Gestisce l'avvio del generatore tramite flag.
     * @param conferma Flag di coferma
     * @return Esito testuale dell'operazione.
     */
    public String gestisciRiavvioGeneratore(boolean conferma) {
        String esito;
        if (conferma) {
            avviaGeneratoreBackup();
            esito = "Hai riavviato il generatore";
        } else {
            esito = "Lasci il generatore spento";
        }
        setUltimoEsito(esito);
        notifyObservers();
        return esito;
    }
    
    /**
     * Consente di leggere una mail, effettua un controllo riguardo la logica di gioco
     * della lettura di tutte le mail incriminate.
     * @param idEmail L'id della mail da leggere.
     * @return Il corpo della mail.
     */
    public String leggiEmail(int idEmail) {
        String corpo = mailDAO.leggiMail(idEmail);
        StringBuilder result = new StringBuilder(corpo);
        
        if (mailDAO.tutteProveLette() && !inventory.containsId(GameConstants.PROVE_RICATTO)) {
            ottieniProveRicatto();
            result.append("\n> [DEDUZIONE]: Il direttore veniva ricattato, le mail lo confermano!");
        }
        
        return result.toString();
    }
    
    /**
     * Legge una mail, formatta il testo e notifica la view.
     * @param selezionata Email selezionata.
     */
    public void segnaMail(EmailDTO selezionata) {
        String testoEmail = leggiEmail(selezionata.getId());
        String visualizzazioneMail = "Da: " + selezionata.getMittente() + " -- Oggetto: " + selezionata.getOggetto() + "\n" + testoEmail + "\n";
        setUltimoEsito(visualizzazioneMail);
        notifyObservers();
    }
    
    /**
     * Gestisce l'esaminazione di un oggetto, caso speciale: diario.
     * @param obj 
     */
    public void esaminaOggetto(AdvObject obj) {
        
        if (obj == null) {
            setUltimoEsito("Cosa intendi esaminare?");
            notifyObservers();
            return;
        }
        
        if (obj instanceof Diario) {
            ((Diario)obj).setLetto(true);
        }
        
        setUltimoEsito("Esamini " + obj.getName() + ":\n" + obj.getDescription());
        notifyObservers();
    }
    
    
    /**
     * Legge e stampa a schermo il file di introduzione di gioco.
     * @return Il testo dll'introduzione del gioco
     */
    @Override
    public String getIntro() {
        StringBuilder introText = new StringBuilder();
        
        try {
            List<String> righe = leggiFile("/intro.txt");
            for (String riga : righe) {
                introText.append(riga).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "Errore: impossibile trovare il file intro.txt";
        } catch (IOException e) {
            return "Errore: impossibile leggere il file intro.txt";
        }
        return introText.toString();
    }
    
    /**
     * Legge e restituisce il testo del file di outro del gioco.
     * @return Il resto di outro del gioco.
     */
    public String getOutro() {
        StringBuilder outroText = new StringBuilder();
        
        try {
            List<String> righe = leggiFile("/outro.txt");
            for (String riga : righe) {
                outroText.append(riga).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "Impossibile trovare il file outro.txt";
        } catch (IOException e) {
            return "Impossibile leggere il file outro.txt";
        }
        
        return outroText.toString();
    }
    
    /**
     * Carica i dati delle stanze da un file esterno e le istanzia in memoria.
     * Le stanze vengono salvate in una mappa.
     */
    private void caricaStanze() {
        try {
            leggiFile("/stanze.csv").stream()
                .filter(riga -> riga != null && !riga.trim().isEmpty())
                .map(riga -> riga.split("\\|"))
                .filter(dati -> dati.length >= 3)
                .forEach(dati -> {
                    int id = Integer.parseInt(dati[0].trim());
                    String nome = dati[1].trim();
                    String descrizione = dati[2].trim();
                    
                    Room room = new Room(id, nome, descrizione);
                    if (dati.length >= 4) {
                        room.setLookDescription(dati[3].trim());
                    }
                    
                    stanze.put(id, room);
                    mapGraph.addNodo(room);
                });
        } catch (IOException | NumberFormatException e) {
            System.out.println("Impossibile caricare le stanze dell'hotel");
        }
    }
    
    /**
     * Legge i collegamenti tra le stanze e popola il grafo.
     */
    private void caricaCollegamenti() {
        try {
            leggiFile("/collegamenti.csv").stream()
                .filter(riga -> riga != null && !riga.trim().isEmpty())
                .map(riga -> riga.split("\\|"))
                .filter(dati -> dati.length == 3)
                .forEach(dati -> {
                    int idPartenza = Integer.parseInt(dati[0].trim());
                    String direzioneStringa = dati[1].trim();
                    int idArrivo = Integer.parseInt(dati[2].trim());
                    
                    Room stanzaPartenza = stanze.get(idPartenza);
                    Room stanzaArrivo = stanze.get(idArrivo);
                    
                    if (stanzaPartenza != null && stanzaArrivo != null) {
                        try {
                            Direction direzione = Direction.fromString(direzioneStringa);
                            mapGraph.addEdge(stanzaPartenza, direzione, stanzaArrivo);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Direzione '" + direzioneStringa + "' non riconosciuta");
                        }
                    }
                });
        } catch (IOException | NumberFormatException e) {
            System.out.println("Impossibile collegare le stanze sulla mappa.");
        }
    }
    
    /**
     * Istanzia gli oggetti li inserisce nel grafo delle stanze.
     * Carica i dati degli oggetti dal file oggetti.csv, esso ha formato:
     * id | nome | descrizione | tipo(C/O) | stanzaId | containerId | pickupable | richiedeApertura
     * N.B. richiedeApertura è true se l'oggetto è Openable e deve iniziare chiuso.
     */
    private void caricaOggetti() {
        Map<Integer, AdvObject> indiceOggetti = new HashMap<>();
        
        try {
            List<String> righe = leggiFile("/oggetti.csv");
            for (String riga : righe) {
                if (riga.trim().isEmpty()) {
                    continue;
                }
                
                String[] dati = riga.split("\\|");
                if (dati.length == 8) {
                    int id = Integer.parseInt(dati[0].trim());
                    String nome = dati[1].trim();
                    String descrizione = dati[2].trim();
                    String tipo = dati[3].trim();
                    int stanzaId = Integer.parseInt(dati[4].trim());
                    int containerId = Integer.parseInt(dati[5].trim());
                    boolean pickupable = Boolean.parseBoolean(dati[6].trim());
                    boolean richiedeApertura = Boolean.parseBoolean(dati[7].trim());
                    
                    AdvObject obj;
                    AdvObjectMaker maker = objectMakerMap.get(id);
                    
                    
                    if (maker != null) {
                        obj = maker.create(id, nome, descrizione);
                    } else if ("C".equalsIgnoreCase(tipo)) {
                        obj = new AdvObjectContainer(id, nome, descrizione);
                    } else if (pickupable) {
                        obj = new PickupableObject(id, nome, descrizione);
                    } else {
                        obj = new AdvObject(id, nome, descrizione);
                    }
                    
                    if (obj instanceof Openable) {
                        ((Openable) obj).setOpen(!richiedeApertura);
                    }
                    
                    indiceOggetti.put(id, obj);
                    if (stanzaId != -1 && stanze.get(stanzaId) != null) {
                        Room stanzaOspitante = stanze.get(stanzaId);
                        stanzaOspitante.addObject(obj);
                        provenienzaOggetti.put(id, stanzaOspitante.getName());
                        
                    } else if (containerId != -1) {
                        AdvObject contenitore = indiceOggetti.get(containerId);
                        if (contenitore instanceof AdvObjectContainer) {
                            ((AdvObjectContainer) contenitore).add(obj);
                            provenienzaOggetti.put(id, provenienzaOggetti.getOrDefault(containerId, "Sconosciuta"));       
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Errore: impossibile caricare gli oggetti.");
        }
    }
    
    /**
     * Gestisce il movimento del giocatore all'interno della mappa 
     * dell'Hotel (grafo). Interroga la struttura dati per verificare l'esistenza
     * di un'adiacenza nella direzione indicata.
     * @param direzione L'enumerativo che indica la direzione del movimento
     * @return Una string informativa sull'esito del movimento, viene mostrata all'utente.
     */
    @Override
    public String muoviGiocatore(Direction direzione) {
        // Recupero della stanza adiacente
        Room prossimaStanza = mapGraph.getAdiacente(currentRoom, direzione);
    
        if (prossimaStanza == null) {
            setUltimoEsito("Non puoi proseguire in questa direzione, scegli un'altra opzione.");
            notifyObservers();
            return getUltimoEsito();
        }
        
        // Gestione serrattura elettronica nella Stanza 301
        if (prossimaStanza.getId() == GameConstants.STANZA_301 && !stanza301Sbloccata) {
            setUltimoEsito("La porta della stanza 301 è controllata da una serratura elettronica. \n\nRimani in: " + currentRoom.getName());
            notifyObservers();
            return getUltimoEsito();
        }
        
        // Gestione buio seminterrato nel Seminterrato
        boolean mostraMessaggioTorcia = false; // Flag
        if (prossimaStanza.getId() == GameConstants.SEMINTERRATO) {
            // Controllo inventario (serve Torcia)
            boolean haTorcia = inventory.containsId(GameConstants.TORCIA);
            if (!haTorcia) {
                setUltimoEsito("Il Seminterrato è avvolto dall'oscurità a causa del blackout. \n\nRimani in: " + currentRoom.getName());
                notifyObservers();
                return getUltimoEsito();
            }
            if (!correnteRipristinata) {
                mostraMessaggioTorcia = true;
            }
        }
        
        if (mostraMessaggioTorcia) {
            setUltimoEsito("La torcia illumina il buio seminterrato. Entri in: " + prossimaStanza.getName());
        } else {
            setUltimoEsito("Ti sposti verso " + direzione.toString().toLowerCase() + " ed entri in: " + prossimaStanza.getName());
        }
        
        setCurrentRoom(prossimaStanza);
        return getUltimoEsito();
    }
    
    /**
     * Esegue la transazione dovuta alla raccolta di un ogetto. Rimuove l'oggetto dalla 
     * stanza e lo inserisce nell'inventario del giocatore, effettua le dovute verifiche.
     * @param obj L'oggetto che il giocatore vuole raccogliere
     * @return Stringa descrittiva riguardo l'esito dell'operazione
     */
    @Override
    public String raccogliOggetto(AdvObject obj) {
        if (obj == null) {
            return "Specificare un oggetto valido da raccogliere.";
        }
        
        if(obj instanceof Pickupable) {
            obj.setVisible(false);
            inventory.add(obj);
            String esito = "Hai raccolto: " + obj.getName() + ".";
            
            // Controllo sull'arma del delitto
            if (obj.getId() == GameConstants.FERMACARTE) {
                esito += "\n> [DEDUZIONE]: Questo fermacarte è sporco di sangue, è stata ritrovata l'arma del delitto";
            }
            
            setUltimoEsito(esito);
            notifyObservers();
            return esito;
        }
        return "Non puoi prendere l'oggetto " + obj.getName();
    }
    
    /**
     * In caso l'oggetto sia un contenitore, permette di rendere accessibile
     * al giocatore il suo contenuto
     * @param obj L'oggetto che il giocatore vuole aprire
     * @return Stringa descrittiva riguardo l'esito dell'operazione
     */
    @Override
    public String apriOggetto(AdvObject obj) {
        if (obj == null) {
            return "Cosa intendi aprire?";
        }
        
        if (obj instanceof Openable) {
            String result = ((Openable) obj).open(this);
            setUltimoEsito(result);
            notifyObservers();
            return result;
        }
        return "Questo oggetto non può essere aperto.";
    }
    
    /**
     * Gestisce l'interazione combinata tra un'oggetto presente nell'inventario
     * e uno presente nella stanza.
     * @param strumento L'oggetto dell'inventario.
     * @param target L'oggetto nello scenario.
     * @return Stringa di testo che descrive l'esito.
     */
    @Override
    public String usaOggettoSuTarget(AdvObject strumento, AdvObject target) {
        if (strumento == null || target == null) {
            return "Combinazione di oggetti non valida.";
        }
        
        if (!inventory.contains(strumento)) {
            return "Non possiedi l'oggetto " + strumento.getName() + " nel tuo inventario.";
        }
        
        if (target instanceof Combinable) {
            String result = ((Combinable) target).combine(this, strumento);
            setUltimoEsito(result);
            notifyObservers();
            return result;
        }
        return "Non ha alcun effetto";
    }
    
    /**
     * Esegue la verifica di autenticazione su un oggetto sbloccabile.
     * @param obj L'oggetto da sbloccare
     * @param password La stringa digitata dall'utente.
     * @return Stringa che descrive l'esito.
     */
    public String sbloccaOggetto(AdvObject obj, String password) {
        if (obj == null) {
            return "Cosa intendi sbloccare?";
        }
        
        if (obj instanceof Unlockable) {
            String result = ((Unlockable) obj).unlock(this, password);
            setUltimoEsito(result);
            notifyObservers();
            return result;
        }
        return "Non c'è nulla da sbloccare.";
    }
    
    private String elaboraSbloccoRemoto(String rispostaServer) {
        if (stanza301Sbloccata) {
            return "La stanza 301 è gia stata sbloccata.";
        }
        
        if (rispostaServer != null && rispostaServer.startsWith("Sblocco")) {
            stanza301Sbloccata = true;
            return "La richiesta ha avuto esito positivo " + rispostaServer;
        }
        
        return "La richiesta non è andata buon fine: " + rispostaServer;
    }
    
    /**
     * Gestisce lo logica di sblocco dell'accesso remoto dal pc.
     * @param rispostaServer L'esito dell'azione.
     */
    public void gestisciSbloccoRemoto(String rispostaServer) {
        String esito = elaboraSbloccoRemoto(rispostaServer);
        setUltimoEsito(esito);
        notifyObservers();
    }
    
    /**
     * Gestisce la comunicazione di errore nel connettersi al server.
     */
    public void notificaErroreConnessione() {
        setUltimoEsito("Impossibile contattare il server");
        notifyObservers();
    }
     
    /**
     * Metodo generico per la lettura di un file.
     * @param path Il path del file da leggere
     * @return Lista di stringhe con le righe del file
     * @throws IOException Il file non esiste o non viene letto
     */
    private List<String> leggiFile(String path) throws IOException {
        List<String> righe = new ArrayList<>();
        InputStream is = getClass().getResourceAsStream(path);
        
        if (is == null) {
            throw new FileNotFoundException("Path non riconosciuto: " + path);
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                righe.add(line);
            }
        } finally {
            br.close();
        }
        return righe;     
    }
    
    /**
     * Esegue un'analisi dell'inventario per generare un resoconto.
     * Raggruppa le prove raccolte in base alla stanza di provenienza.
     * @return Il testo formattato contenente il riepilogo degli indizi raccolti.
     */
    public String generaRapportoIndagine() {
        if (inventory.getList().isEmpty()) {
            return "Il tuo taccuino è vuoto. Non hai ancora raccolto alcun indizio.";
        }

        Map<String, List<String>> indiziPerStanza = inventory.getList().stream()
                .filter(o -> !(o instanceof AdvObjectContainer)) 
                .collect(Collectors.groupingBy(
                        o -> provenienzaOggetti.getOrDefault(o.getId(), "Luogo Ignoto"), 
                        Collectors.mapping(AdvObject::getName, Collectors.toList())
                ));
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nAPPUNTI DELL'INDAGINE\n");
        sb.append("\nProve in base al luogo di ritrovamento:\n");
        
        indiziPerStanza.forEach((stanza, indizi) -> {
            sb.append("- ").append(stanza).append(":\n");
            indizi.forEach(indizio -> sb.append("   - ").append(indizio).append("\n"));
        });
        
        return sb.toString();
    }
    
    /**
     * Aggiunge l'oggetto con le prove ricavate dalle mail di ricatto
     * all'inventario del giocatore.
     */
    private void ottieniProveRicatto() {
        if (!inventory.containsId(GameConstants.PROVE_RICATTO)) {
            AdvObject prove = new AdvObject(GameConstants.PROVE_RICATTO, "Prove del ricatto", "Email che confermano il movente dell'omicidio, il direttore veniva ricattato dalla vittima.");
            inventory.add(prove);
            provenienzaOggetti.put(GameConstants.PROVE_RICATTO, "Computer");
        }
        
    }
}