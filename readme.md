### EUX Avslutt Rinasaker

Denne applikasjonen tilbyr tjenester i `AvsluttRinasakerApi` som avslutter og håndterer status 
på rinasaker. Den er bygget med Kotlin og Spring Boot, og bruker Maven som byggesystem. 
Applikasjonen integrerer med Kafka for å håndtere dokument- og sakshendelser.

## Prosesser i APIet

### Sett Uvirksom
Markerer rinasaker som uvirksomme ved å oppdatere statusen til saken. Denne prosessen 
bruker `SettUvirksomService` for å utføre operasjonen.

### Til Avslutning
Setter rinasaker til avslutning ved å oppdatere statusen til saken. Denne prosessen 
bruker `TilAvslutningService` for å utføre operasjonen.

### Avslutt
Avslutter rinasaker ved å oppdatere statusen til saken og eventuelt arkivere den. Denne 
prosessen bruker `AvsluttService` for å utføre operasjonen.

### Lag Oppgave
Lager oppgave for manuell avslutning av rinasaker. Denne prosessen er foreløpig ikke 
implementert, men vil lage en oppgave som krever manuell intervensjon.

### Til Arkivering
Setter rinasaker til arkivering ved å oppdatere statusen til saken. Denne prosessen 
bruker `TilArkiveringService` for å utføre operasjonen.

### Arkiver
Arkiverer rinasaker ved å oppdatere statusen til saken og flytte den til arkivet. 
Denne prosessen bruker `ArkiverService` for å utføre operasjonen.

## Brukte teknologier
* Kotlin
* Spring Boot
* Maven
* Kafka

#### Avhengigheter

* JDK 21

## API Dokumentasjon

APIet er dokumentert med Swagger og tilbyr følgende endepunkt for å starte prosesser:

```POST /api/v1/prosesser/{prosess}/execute```

### Parametere

* `prosess` - Navnet på prosessen som skal startes:
    * `sett-uvirksom` - Markerer rinasaker som uvirksomme
    * `til-avslutning` - Sett rinasaker til avslutning
    * `avslutt` - Avslutter rinasaker
    * `lag-oppgave` - Lager oppgave for manuell avslutning av rinasaker
    * `til-arkivering` - Setter rinasaker til arkivering
    * `arkiver` - Arkiverer rinasaker
