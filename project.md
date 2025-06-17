# 1 Project Description
Questo progetto consiste nella progettazione e nell'implementazione di DESM (Distributed Energy Supply Management), una simulazione semplificata di una rete di approvvigionamento energetico supervisionata da un Renewable Energy ProviderRenewable Energy Provider rinnovabile. La figura 1 mostra l'architettura complessiva di DESM.

![Architettura di DESM](./assets/img/Overall%20architecture%20of%20DESM.png)
*Figura 1: Architettura generale di DESM*

Il Renewable Energy Provider rinnovabile genera energia da fonti rinnovabili come l'energia eolica, idroelettrica e solare. Sfortunatamente, l'energia prodotta non è sempre sufficiente a soddisfare la domanda totale. Per affrontare questa carenza, il Renewable Energy Provider rinnovabile si affida a una rete di centrali termiche per fornire l'energia aggiuntiva richiesta. Quando viene ricevuta una nuova richiesta di produzione di energia, le centrali termiche della rete competono per soddisfarla. L'assegnazione della richiesta si basa sul prezzo offerto più basso tra gli impianti concorrenti. La rete di centrali termiche è gestita da un Administration serverAdministration server, che gestisce la registrazione di nuovi impianti che si uniscono al sistema.
Inoltre, ogni Thermal Power Plant è dotata di sensori che monitorano le emissioni di inquinanti durante la produzione di energia; questi dati ambientali vengono trasmessi periodicamente al Administration server, che può essere interrogato dal Administration client per recuperare statistiche. L'obiettivo di questo progetto è implementare il Renewable Energy Provider rinnovabile, una rete peer-to-peer di centrali termoelettriche, il Administration server e il Administration client.

# 2 Operazione generale
Questa sezione descrive il funzionamento generale del sistema. Le sezioni successive forniranno una spiegazione dettagliata per ogni componente specifico.

## 2.1 Pubblicazione della richiesta di energia
A intervalli regolari di 10 secondi, il Renewable Energy Provider rinnovabile pubblica una richiesta per una certa quantità di energia, misurata in chilowattora (kWh). Questa richiesta viene trasmessa a tutte le centrali termiche all'interno della rete.

## 2.2 Fase di offerta
Dopo aver ricevuto la richiesta di energia, ogni Thermal Power Plant avvia un processo di elezione per determinare quale impianto soddisferà la richiesta. Durante questa fase, ogni impianto termico genera un'offerta che rappresenta il costo per soddisfare la richiesta. L'impianto che offre il costo più basso è selezionato.

## 2.3 Adempimento della richiesta 
Una volta che una Thermal Power Plant vince l'offerta, procede a soddisfare la richiesta di energia. L'impianto utilizza una quantità di tempo pari a 1 millisecondo moltiplicato per il numero di kWh nella richiesta per completare la produzione di energia. Durante questo periodo, l'impianto non può accettare nuove richieste di produzione di energia.

## 2.4 Nuova Thermal Power Plant 
Il sistema consente l'aggiunta dinamica di nuove centrali termiche alla rete in qualsiasi momento. Pertanto, tutti gli algoritmi e i processi devono tenere conto di questa possibilità. Ad esempio, una nuova Thermal Power Plant può unirsi alla rete durante un'elezione in corso e deve essere in grado di partecipare alla fase di offerta.

## 2.5 Sensori di inquinamento
Una volta che una Thermal Power Plant si unisce alla rete, inizia ad acquisire dati dal suo sensore di inquinamento, che monitora le emissioni durante la produzione di energia. Queste letture vengono trasmesse periodicamente al Administration server. Il Administration server può essere interrogato da un Administration client in modo che un'eventuale autorità competente possa monitorare i livelli di inquinamento e mitigare l'impatto ambientale.

# 3 Applicazioni da implementare
Per questo progetto, è necessario sviluppare le seguenti applicazioni: 
- **Renewable Energy Provider**: il processo che simula il Renewable Energy Provider rinnovabile.
- **Thermal Power Plant**: il processo che rappresenta le centrali termiche nella rete DESM. 
- **Administration server**: un server REST che aggiunge/rimuove dinamicamente le centrali elettriche a DESM e consente al Administration client di vedere gli impianti attualmente attivi nella rete e di calcolare le statistiche sui livelli di inquinamento. 
- **Administration client**: un client che consente di interrogare il Administration server per ottenere informazioni sulle centrali termiche attualmente attive nella rete e sulle loro emissioni. 

Si prega di notare che ogni Thermal Power Plant è un processo autonomo, e quindi non deve essere implementato come un thread. Di seguito, forniamo maggiori dettagli sulle applicazioni che devono essere sviluppate.

# 4 Renewable energy provider 
Il Renewable Energy Provider rinnovabile genera una richiesta di energia ogni 10 secondi, specificando la quantità di energia richiesta in chilowattora (kWh) insieme a un timestamp. La quantità di energia è un numero generato casualmente tra 5000 e 15000. Questa richiesta viene quindi pubblicata su un argomento MQTT, a cui sono sottoscritti tutti gli impianti termici della rete. La richiesta rimane in tema fino a quando non viene selezionato un impianto per fornire l'energia richiesta.

# 5 Thermal Power Plant
Ogni Thermal Power Plant esegue un processo che è responsabile di: 
- Coordinamento con gli altri impianti utilizzando gRPC per decidere quale impianto soddisferà la richiesta di energia. 
- Invio delle informazioni del server sui valori rilevati dai sensori di inquinamento tramite MQTT.

## 5.1 Inizializzazione 
Una Thermal Power Plant viene inizializzata specificando: 
- ID.
- Indirizzo di ascolto e porta per la comunicazione con le altre piante.
- Indirizzo e porta del Administration server. 

Nota che per il bene di questo progetto, l'indirizzo di tutti i componenti sarà sempre localhost, poiché tutto è in esecuzione localmente sulla tua macchina. Una volta avviato, il processo dell'impianto deve registrarsi con il sistema tramite il Administration server. Se il suo inserimento ha esito positivo (cioè, non ci sono altre piante con lo stesso ID), l'impianto riceve dal Administration server l'elenco delle altre piante già presenti nella rete (cioè indirizzo e numero di porta di ogni impianto). Una volta che la Thermal Power Plant riceve queste informazioni, inizia ad acquisire dati dal sensore di inquinamento. Se ci sono già altre piante nella rete, la nuova pianta si presenta alle altre piante. Infine, l'impianto sottoscrive l'argomento MQTT in cui il Renewable Energy Provider rinnovabile pubblicherà le richieste di produzione di energia.

## 5.2 Sincronizzazione distribuita

### 5.2.1 Gestione delle richieste di energia
Le centrali elettriche devono utilizzare un algoritmo distribuito e decentralizzato per decidere chi si farà carico di ogni richiesta di energia. In particolare, ogni richiesta sarà gestita dall'impianto che soddisfa i seguenti criteri: 
- La Thermal Power Plant non deve già gestire un'altra richiesta. 
- La Thermal Power Plant è quella che offre il prezzo più basso. 
- Nel caso in cui due o più piante offrano lo stesso prezzo, vince la pianta con l'ID più alto.

### 5.2.2 Generazione di prezzi
Ogni volta che una Thermal Power Plant deve partecipare a un'elezione, utilizza un generatore casuale per simulare il calcolo del prezzo in $/kWh nell'intervallo [0,1, 0,9]

### 5.2.3 Algoritmo elettorale
Poiché le centrali termiche non conoscono in anticipo il prezzo offerto dagli altri impianti, è necessario implementare un algoritmo ad anello (Chang e Roberts).

## 5.3 Sensore di inquinamento
Le centrali elettriche sono dotate di un sensore di inquinamento che rileva la quantità di CO2 emessa durante il processo di produzione. È misurato in grammi (g). Durante il funzionamento del sistema, le centrali elettriche raccolgono informazioni sulle emissioni da inviare al Administration server. Ogni sensore di inquinamento produce periodicamente misurazioni delle emissioni di CO2 dell'impianto. Ogni singola misurazione è caratterizzata da:
- Valore CO2 (g).
- Timestamp della misurazione, espressa in millisecondi.

La generazione di tali misurazioni è prodotta da un simulatore. Al fine di semplificare l'implementazione del progetto, è possibile scaricare il codice del simulatore direttamente dalla pagina del corso su MyAriel, nella sezione "Progetto". Ogni simulatore assegna il numero di secondi dopo mezzanotte come timestamp associato a una misurazione. Il codice del simulatore deve essere aggiunto come pacchetto al progetto e non deve essere modificato. Durante la fase di inizializzazione, ogni impianto avvia il filo del simulatore che genererà le misurazioni per il sensore di inquinamento. Ogni simulatore è un filo costituito da un ciclo infinito che genera periodicamente (con una frequenza predefinita) le misurazioni simulate. Tali misurazioni vengono aggiunte a una corretta struttura dati.

Forniamo solo l'interfaccia (Buffer) di questa struttura dati che espone due metodi: 
- void add(Measurement m).
- Elenca <Measurement> readAllAndClean().

Pertanto, è necessario creare una classe che implementi questa interfaccia. Si noti che ogni Thermal Power Plant è dotata di un singolo sensore. Il thread di simulazione utilizza il metodo addMeasurement per riempire la struttura dei dati. Invece, il metodo readAllAndClean, deve essere utilizzato per ottenere le misurazioni memorizzate nella struttura dei dati. Alla fine di un'operazione di lettura, readAllAndClean fa spazio a nuove misurazioni nel buffer. In particolare, è necessario elaborare i dati dei sensori attraverso la tecnica della finestra scorrevole che è stata introdotta nelle lezioni di teoria. Devi considerare un buffer di 8 misurazioni, con un fattore di sovrapposizione del 50%. Quando la dimensione del buffer è uguale a 8 misurazioni, è necessario calcolare la media di queste 8 misurazioni. L'impianto invierà quindi queste medie al Administration server.

## 5.4 Invia dati al Administration server
Ogni 10 secondi, ogni Thermal Power Plant deve comunicare al Administration server l'elenco delle medie delle misurazioni di CO2 calcolate dopo l'ultima comunicazione con il server. Questo elenco di medie deve essere inviato al server associato a:
- L'ID della Thermal Power Plant
- Il timestamp in cui è stato calcolato l'elenco

Questa comunicazione avviene attraverso un argomento MQTT adeguato a cui il server è iscritto.

## 5.5 Lasciare la rete
Per semplicità, si presume che nessuna Thermal Power Plant, una volta entrata nella rete, la lasci.

# 6 Administration server
L'Administration server raccoglie gli ID delle Thermal Power Plant registrate nel sistema e riceve anche da loro i valori dei sensori di inquinamento. Queste informazioni saranno quindi interrogate dal Administration client. 
Pertanto, questo server deve fornire diverse interfacce REST per:
- Gestire l'inizializzazione delle Thermal Power Plant
- Consentire all'Administration client di eseguire le query

Inoltre, il server deve iscriversi all'argomento MQTT su cui le Thermal Power Plant pubblicano i valori dei sensori di inquinamento, li leggono e li memorizzano correttamente.

## 6.1 Interfaccia REST delle Thermal Power Plant
### 6.1.1 Inizializzazione delle Thermal Power Plant
Il server deve memorizzare le seguenti informazioni per ogni Thermal Power Plant che si unisce alla rete:
- ID
- Indirizzo (nel tuo caso sarà localhost)
- Il numero di porta su cui è disponibile per gestire la comunicazione con gli altri processi dell'impianto. 

Una Thermal Power Plant può essere aggiunta alla rete solo se non ci sono altre Thermal Power Plant con lo stesso identificatore. 
Se l'inserimento ha successo, l'Administration server restituisce alla Thermal Power Plant l'elenco degli impianti che hanno già aderito alla rete, includendo per ciascuno di essi l'ID, l'indirizzo e il numero di porta per la comunicazione.

## 6.2 Statistiche sull'inquinamento
L'Administration server deve essere in grado di ricevere le statistiche locali di CO2 dalle Thermal Power Plant, il che significa che deve sottoscrivere un argomento MQTT adeguato. 
Questi dati devono essere archiviati in strutture dati adeguate che verranno utilizzate per eseguire analisi successive. 
Durante lo sviluppo del progetto, assicurati di sincronizzare correttamente le operazioni di lettura e scrittura effettuate su queste strutture di dati. 
Infatti, l'Administration server può ricevere statistiche locali mentre l'Administration client gli richiede di eseguire alcuni calcoli su tali valori.

## 6.3 Interfaccia REST dell'Administration client
Quando richiesto dal Administration client attraverso l'interfaccia descritta nella sezione 7, l'Administration server deve essere in grado di calcolare le seguenti statistiche: 
- L'elenco delle Thermal Power Plant attualmente nella rete
- La media dei livelli di emissione di CO2 inviati da tutti gli impianti al server che si sono verificati tra il timestamp t1 e il timestamp t2.

# 7 Administration client
L'Administration client è costituito da una semplice interfaccia a riga di comando che consente l'interazione con l'interfaccia REST fornita dall'Administration server. 
Quindi, questa applicazione stampa un menu semplice per selezionare uno dei servizi offerti dall'Administration server descritto nella sezione 6.3 e per inserire possibili parametri richiesti.

# 8 Semplificazioni e restrizioni
È importante ricordare che lo scopo di questo progetto è dimostrare la capacità di progettare e costruire applicazioni distribuite e pervasive. 
Pertanto, tutti gli aspetti che non sono strettamente correlati al protocollo di comunicazione, alla concorrenza e alla gestione dei dati sensoriali sono secondari. 
Inoltre, è possibile presumere che nessun nodo si comporti in modo dannoso. Al contrario, dovresti gestire possibili errori nei dati inseriti dall'utente. 
Inoltre, il codice deve essere robusto: tutte le possibili eccezioni devono essere gestite correttamente. Sebbene le librerie Java forniscano più classi per gestire situazioni di concorrenza, per scopi educativi, è obbligatorio utilizzare solo i metodi e le classi spiegati durante il corso di laboratorio. 
Pertanto, qualsiasi struttura di dati di sincronizzazione necessaria (come serrature, semafori o buffer condivisi) dovrebbe essere implementata da zero e sarà discussa durante la presentazione del progetto.
Per quanto riguarda la comunicazione tra i processi delle Thermal Power Plant, è necessario utilizzare il framework gRPC. 
Se sono necessarie comunicazioni broadcast, queste devono essere eseguite in parallelo e non in sequenza.

# 9 MQTT Broker
Il broker MQTT su cui si basa DESM è Mosquitto e si presume che sia online al seguente indirizzo: tcp://localhost:1883. 
Il fornitore di energia rinnovabile utilizza questo broker per pubblicare la richiesta di energia. 
Il Broker viene utilizzato anche dalla Thermal Power Plant per pubblicare le misurazioni dei sensori di inquinamento.