# The Hotel

"The Hotel" è un'applicazione Java (sviluppata come progetto per il corso di Metodi Avanzati di Programmazione) che combina una robusta logica di gioco testuale/avventura a oggetti con un'interfaccia grafica (GUI) in **Java Swing**, persistenza dei dati tramite database **H2** (JDBC) e comunicazione di rete multithreaded tramite **Socket**.

## Caratteristiche Principali
- **Architettura OOP:** Design pattern orientati agli oggetti, uso di Generics e strutture dati personalizzate (`MapGraph`).
- **Interfaccia Grafica (Swing) & CLI:** Possibilità di interagire sia tramite interfaccia grafica (`HotelGUI`) sia da terminale.
- **Persistenza:** Integrazione con database relazionale H2 tramite JDBC per il salvataggio dei dati.
- **Networking:** Supporto per socket multithreaded per la gestione della comunicazione.

## Requisiti di Sistema
- **Java Development Kit (JDK):** Versione 25 o compatibili.
- **Apache Maven:** Per la gestione delle dipendenze e la compilazione del progetto.

## Compilazione ed Esecuzione
Assicurati di trovarti nella cartella principale del progetto (dove si trova il file `pom.xml`) e apri il terminale.

### 1. Compilazione del progetto con Maven
Per compilare il codice sorgente e pacchettizzare l'applicazione, esegui:
```bash
mvn clean compile