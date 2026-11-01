/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per la gestione delle operazioni sul db.
 * @author matti
 */
public class MailDAO {
    private final String url = "jdbc:h2:./hoteldb;DB_CLOSE_DELAY=-1";
    
    /**
     * Inizializza il database all'avvio.
     * Crea la tabella principale e la popola.
     */
    public void initDB() {
        String createTable = "CREATE TABLE IF NOT EXISTS MESSAGGI_EMAIL (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "mittente VARCHAR(50), " +
                "oggetto VARCHAR(50), " + 
                "corpo TEXT, "  +
                "is_ricatto BOOLEAN, " + 
                "letto BOOLEAN )";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            
            stmt.execute(createTable);
            
            // Popolamento del database
            rs = stmt.executeQuery("SELECT COUNT (*) FROM MESSAGGI_EMAIL");
            rs.next();
            if (rs.getInt(1) == 0) {
                inserisciMail(conn);
            } else {
                stmt.execute("UPDATE MESSAGGI_EMAIL SET letto = false");
            }
        } catch (Exception e) {
                System.err.println("Errore con l'inizializzazione del database " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {}
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {}
            try {
                if (conn != null) {
                    conn.close();
                } 
            } catch (SQLException e) {}
        }  
    }
    
    /**
     * Popola la tabella del db inserendo le mail.
     * @param conn La connessione del db.
     * @throws Exception Errori durante l'esecuzione delle query.
     */
    private void inserisciMail(Connection conn) throws Exception {
        String ins = "INSERT INTO MESSAGGI_EMAIL (mittente, oggetto, corpo, is_ricatto, letto) VALUES (?, ?, ?, ?, false)";
        PreparedStatement pstmt =  null;

        try {
            pstmt = conn.prepareStatement(ins);
            Object[][] mails = {
                {"Enel Energia", "Bolletta scaduta", "Gentile cliente, la invitiamo a regolarizzare i suoi pagamenti.", false},
                {"Sconosciuto", "R: Debiti di gioco", "Hai tempo fino a domani. Se non paghi, dirò tutto alla polizia.", true},
                {"Booking", "Nuove prenotazioni", "Hai 3 nuove prenotazioni per il weekend.", false},
                {"Sconosciuto", "R: Rinvio pagamento", "Non mi interessano le tue scuse. O i soldi o la tua reputazione.", true},
                {"Reception", "Turni settimana", "In allegato i turni aggiornati della reception.", false},
                {"Spam", "Hai vinto un iPhone!", "Clicca qui per ritirare il tuo premio.", false},
                {"Fornitore", "Consegna lenzuola", "Le confermiamo che la consegna avverrà in data odierna.", false},
                {"Sconosciuto", "Ultimo avvertimento", "So cosa hai fatto nel 2015. Questa è l'ultima mail, preparati.", true},
                {"Manutenzione", "Guasto ascensore", "L'ascensore principale è fuori uso, tecnici in arrivo.", false},
                {"Amministrazione", "Fatture mensili", "Si prega di inviare le fatture entro fine mese.", false}
            };
              
            for (Object[] mail : mails) {
                pstmt.setString(1, (String) mail[0]);
                pstmt.setString(2, (String) mail[1]);
                pstmt.setString(3, (String) mail[2]);
                pstmt.setBoolean(4, (Boolean) mail[3]);
                pstmt.executeUpdate();
            } 
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento delle mail: " + e.getMessage());
            throw e;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {}
            }
        }
    }
    
    /**
     * Recupera la lista della email presenti nel db.
     * @return Lista di {@link EmailDTO}.
     */
    public List<EmailDTO> getListaEmail() {
        List<EmailDTO> lista = new ArrayList<>();
        String all = "SELECT id, mittente, oggetto FROM MESSAGGI_EMAIL";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
       try {
           conn = DriverManager.getConnection(url);
           stmt = conn.createStatement();
           rs = stmt.executeQuery(all);
           
           while (rs.next()) {
               lista.add(new EmailDTO(rs.getInt("id"), rs.getString("mittente"), rs.getString("oggetto")));
           }
           
       } catch (SQLException e) {
           System.err.println("Errore durante il recupero delle email: " + e.getMessage());
       } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {}
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {}
            try {
                if (conn != null) {
                    conn.close();
                }
           } catch (SQLException e) {}
       }
       
       return lista;
    }
    
    /**
     * Legge una mail e segna la lettura.
     * @param id Identificatore mail da leggere.
     * @return Testo completo della mail.
     */
    public String leggiMail(int id) {
        String mail = "";
        
        Connection conn = null;
        PreparedStatement update = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        
        
        try {
            conn = DriverManager.getConnection(url);
            
            // Segna mail letta
            String updateQ = "UPDATE MESSAGGI_EMAIL SET letto = true WHERE id = ?";
            update = conn.prepareStatement(updateQ);
            update.setInt(1, id);
            update.executeUpdate();
            update.close();
            
            // Recupero testo
            String selectQ = "SELECT corpo FROM MESSAGGI_EMAIL WHERE id = ?";
            select = conn.prepareStatement(selectQ);
            select.setInt(1, id);
            rs = select.executeQuery();
            
            if (rs.next()) {
                mail = rs.getString("corpo");
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura della mail: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {}
            try {
                if (select != null) {
                    select.close();
                }
            } catch (SQLException e) {}
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {}       
        }
        
        return mail;
    }
    
    /**
     * Verifica se tutte le mail contenenti un ricatto sono state lette.
     * @return true se tutte le mail con ricatto sono state lette, false altrimenti.
     */
    public boolean tutteProveLette() {
        boolean esito = false;
        String select = "SELECT COUNT (*) FROM MESSAGGI_EMAIL WHERE is_ricatto = true AND letto = false";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(select);
            
            if (rs.next()) {
                esito = (rs.getInt(1) == 0); // Non esistono mail non lette
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante il controllo delle prove " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {}
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {}
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {}
        }
        
        return esito;
    }
    
}
