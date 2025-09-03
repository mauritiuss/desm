# DEVELOPMENT FLOW

## 1° STEP
### REST server e MQTT
- Progettazione dell'Administration server (risorse e metodi) ✅
- Analisi dei problemi di sincronizzazione ✅
- Test del Server REST con strumenti dedicati ✅
- Sviluppo dell'Administration client ✅
- Progettazione del Renewable Energy Provider ✅
- Implementazione del protocollo MQTT per pubblicare le richieste del Renewable Energy Provider ✅

## 2° STEP
### Sviluppo della rete delle Thermal Power Plants
- Architettura e progettazione di protocolli della rete di impianti peer-to-peer ✅
  - Creazione della struttura per il ring algortihm. ✅
- Inserimento di una Thermal Power Plant nella rete peer-to-peer (ThermalPowerPlant --> AdministrationServer) ✅
  - Se non esiste un'altra ThermalPowerPlant con quell'ID:
      - AdministrationServer manda a ThermalPowerPlant l'elenco delle altre ThermalPowerPlant già presenti nella rete. ✅
      - Altrimenti AdministrationServer rifiuta la richiesta da parte della ThermalPowerPlant ✅
- Comunicazione tra ThermalPowerPlant tramite GRPc ✅
- Algoritmo di elezione distribuito e decentralizzato ✅

## 3° STEP
### Raccolta dei dati del sensore
- Implementazione della raccolta dei dati dei sensori
- Calcolo del lato del server statistico

## 4° STEP
### Uscita controllata di una Thermal Power Plant
