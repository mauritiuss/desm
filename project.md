# 1 Project Description
Questo progetto consiste nella progettazione e nell'implementazione di DESM (Distributed Energy Supply Management), una simulazione semplificata di una rete di approvvigionamento energetico supervisionata da un Renewable Energy ProviderRenewable Energy Provider rinnovabile. La figura 1 mostra l'architettura complessiva di DESM.

![Architettura di DESM](./assets/img/Overall%20architecture%20of%20DESM.png)
*Figura 1: Architettura generale di DESM*

Il Renewable Energy Provider rinnovabile genera energia da fonti rinnovabili come l'energia eolica, idroelettrica e solare. Sfortunatamente, l'energia prodotta non è sempre sufficiente a soddisfare la domanda totale. Per affrontare questa carenza, il Renewable Energy Provider rinnovabile si affida a una rete di centrali termiche per fornire l'energia aggiuntiva richiesta. Quando viene ricevuta una nuova richiesta di produzione di energia, le centrali termiche della rete competono per soddisfarla. L'assegnazione della richiesta si basa sul prezzo offerto più basso tra gli impianti concorrenti. La rete di centrali termiche è gestita da un Administration serverAdministration server, che gestisce la registrazione di nuovi impianti che si uniscono al sistema.
Inoltre, ogni centrale termoelettrica è dotata di sensori che monitorano le emissioni di inquinanti durante la produzione di energia; questi dati ambientali vengono trasmessi periodicamente al Administration server, che può essere interrogato dal Administration client per recuperare statistiche. L'obiettivo di questo progetto è implementare il Renewable Energy Provider rinnovabile, una rete peer-to-peer di centrali termoelettriche, il Administration server e il Administration client.

# 2 Operazione generale
Questa sezione descrive il funzionamento generale del sistema. Le sezioni successive forniranno una spiegazione dettagliata per ogni componente specifico.

## 2.1 Pubblicazione della richiesta di energia
A intervalli regolari di 10 secondi, il Renewable Energy Provider rinnovabile pubblica una richiesta per una certa quantità di energia, misurata in chilowattora (kWh). Questa richiesta viene trasmessa a tutte le centrali termiche all'interno della rete.

## 2.2 Fase di offerta
Dopo aver ricevuto la richiesta di energia, ogni Thermal Power Plant avvia un processo di elezione per determinare quale impianto soddisferà la richiesta. Durante questa fase, ogni impianto termico genera un'offerta che rappresenta il costo per soddisfare la richiesta. L'impianto che offre il costo più basso è selezionato.

## 2.3 Adempimento della richiesta 
Una volta che una Thermal Power Plant vince l'offerta, procede a soddisfare la richiesta di energia. L'impianto utilizza una quantità di tempo pari a 1 millisecondo moltiplicato per il numero di kWh nella richiesta per completare la produzione di energia. Durante questo periodo, l'impianto non può accettare nuove richieste di produzione di energia.

## 2.4 Nuova centrale elettrica 
Il sistema consente l'aggiunta dinamica di nuove centrali termiche alla rete in qualsiasi momento. Pertanto, tutti gli algoritmi e i processi devono tenere conto di questa possibilità. Ad esempio, una nuova Thermal Power Plant può unirsi alla rete durante un'elezione in corso e deve essere in grado di partecipare alla fase di offerta.

## 2.5 Sensori di inquinamento
Una volta che una centrale termoelettrica si unisce alla rete, inizia ad acquisire dati dal suo sensore di inquinamento, che monitora le emissioni durante la produzione di energia. Queste letture vengono trasmesse periodicamente al Administration server. Il Administration server può essere interrogato da un Administration client in modo che un'eventuale autorità competente possa monitorare i livelli di inquinamento e mitigare l'impatto ambientale.

# 3 Applicazioni da implementare
Per questo progetto, è necessario sviluppare le seguenti applicazioni: 
- **Renewable Energy Provider**: il processo che simula il Renewable Energy Provider rinnovabile.
- **Thermal Power Plant**: il processo che rappresenta le centrali termiche nella rete DESM. 
- **Administration server**: un server REST che aggiunge/rimuove dinamicamente le centrali elettriche a DESM e consente al Administration client di vedere gli impianti attualmente attivi nella rete e di calcolare le statistiche sui livelli di inquinamento. 
- **Administration client**: un client che consente di interrogare il Administration server per ottenere informazioni sulle centrali termiche attualmente attive nella rete e sulle loro emissioni. 

Si prega di notare che ogni centrale elettrica è un processo autonomo, e quindi non deve essere implementato come un thread. Di seguito, forniamo maggiori dettagli sulle applicazioni che devono essere sviluppate.

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

Nota che per il bene di questo progetto, l'indirizzo di tutti i componenti sarà sempre localhost, poiché tutto è in esecuzione localmente sulla tua macchina. Una volta avviato, il processo dell'impianto deve registrarsi con il sistema tramite il Administration server. Se il suo inserimento ha esito positivo (cioè, non ci sono altre piante con lo stesso ID), l'impianto riceve dal Administration server l'elenco delle altre piante già presenti nella rete (cioè indirizzo e numero di porta di ogni impianto). Una volta che la centrale elettrica riceve queste informazioni, inizia ad acquisire dati dal sensore di inquinamento. Se ci sono già altre piante nella rete, la nuova pianta si presenta alle altre piante. Infine, l'impianto sottoscrive l'argomento MQTT in cui il Renewable Energy Provider rinnovabile pubblicherà le richieste di produzione di energia.