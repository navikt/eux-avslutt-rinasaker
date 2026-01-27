### EUX Avslutt Rinasaker

Denne applikasjonen tilbyr tjenester i `AvsluttRinasakerApi` som avslutter og håndterer status
på rinasaker. Den er bygget med Kotlin og Spring Boot, og bruker Maven som byggesystem.
Applikasjonen integrerer med Kafka for å håndtere dokument- og sakshendelser.

## Prosessflyt Diagram

```mermaid
flowchart TB
    subgraph Kafka["Kafka Events"]
        CaseEvent["eux-rina-case-events-v1"]
        DocEvent["eux-rina-document-events-v1"]
    end

    subgraph DataPopulering["Data Populering (Kafka Listener)"]
        CaseEvent --> LeggTilRinasak["leggTilRinasak()"]
        DocEvent --> LeggTilDokument["leggTilDokument()"]
        LeggTilRinasak --> NY_SAK[("NY_SAK")]
        LeggTilDokument --> SettVirksom["settVirksom()"]
        SettVirksom -->|"Hvis UVIRKSOM"| NY_SAK
    end

    subgraph P1["Prosess 1: sett-uvirksom"]
        NY_SAK -->|"SettUvirksomService\netter X dager uten aktivitet"| UVIRKSOM[("UVIRKSOM")]
    end

    subgraph P2["Prosess 2: til-avslutning"]
        UVIRKSOM -->|"TilAvslutningService"| TilAvslutningEval{"Evaluering av\navslutningskriterier"}
        
        TilAvslutningEval -->|"sisteSedForAvslutning\nsedExistsForAvslutning\nmottattSedExists\nsentSedExists"| AvsluttMed["avsluttMed(scope)"]
        TilAvslutningEval -->|"Ingen scope definert\n(motpart)"| AVSLUTTES_AV_MOTPART[("AVSLUTTES_AV_MOTPART")]
        TilAvslutningEval -->|"opprettOppgave = true\n(ingen kriterier møtt)"| OPPRETT_OPPGAVE[("OPPRETT_OPPGAVE")]
        
        AvsluttMed -->|"AVSLUTT_LOKALT"| TIL_AVSLUTNING_LOKALT[("TIL_AVSLUTNING_LOKALT")]
        AvsluttMed -->|"AVSLUTT_GLOBALT"| TIL_AVSLUTNING_GLOBALT[("TIL_AVSLUTNING_GLOBALT")]
    end

    subgraph P3["Prosess 3: avslutt"]
        TIL_AVSLUTNING_LOKALT -->|"AvsluttService\n→ EuxRinaTerminatorApi"| AvsluttLokaltResult{"Resultat"}
        TIL_AVSLUTNING_GLOBALT -->|"AvsluttService\n→ EuxRinaTerminatorApi"| AvsluttGlobaltResult{"Resultat"}
        
        AvsluttLokaltResult -->|"Suksess"| AVSLUTTET_LOKALT[("AVSLUTTET_LOKALT")]
        AvsluttLokaltResult -->|"Conflict"| HANDLING_MANGLER[("HANDLING_MANGLER")]
        AvsluttLokaltResult -->|"Feil"| HANDLING_FEILET[("HANDLING_FEILET")]
        
        AvsluttGlobaltResult -->|"Suksess"| AVSLUTTET_GLOBALT[("AVSLUTTET_GLOBALT")]
        AvsluttGlobaltResult -->|"Conflict"| HANDLING_MANGLER
        AvsluttGlobaltResult -->|"Feil"| HANDLING_FEILET
    end

    subgraph P4["Prosess 4: lag-oppgave"]
        OPPRETT_OPPGAVE -.->|"Ikke implementert"| OPPGAVE_OPPRETTET[("OPPGAVE_OPPRETTET")]
    end

    subgraph P5["Prosess 5: til-arkivering"]
        AVSLUTTET_LOKALT -->|"TilArkiveringService\netter X dager"| TIL_ARKIVERING[("TIL_ARKIVERING")]
        AVSLUTTET_GLOBALT -->|"TilArkiveringService\netter X dager"| TIL_ARKIVERING
    end

    subgraph P6["Prosess 6: arkiver"]
        TIL_ARKIVERING -->|"ArkiverService\n→ EuxRinaTerminatorApi"| ArkiverResult{"Resultat"}
        ArkiverResult -->|"Suksess"| ARKIVERT[("ARKIVERT")]
        ArkiverResult -->|"Conflict"| HANDLING_MANGLER
        ArkiverResult -->|"Feil"| HANDLING_FEILET
    end

    subgraph P7["Prosess 7: slett-dokumentutkast"]
        SLETT_DOKUMENTUTKAST[("SLETT_DOKUMENTUTKAST")] -->|"SlettDokumentutkastService\n→ EuxRinaTerminatorApi"| SlettResult{"Resultat"}
        SlettResult -->|"Suksess"| UVIRKSOM
        SlettResult -->|"Conflict"| HANDLING_MANGLER
        SlettResult -->|"Feil"| HANDLING_FEILET
    end

    subgraph ExternalAPI["Ekstern API"]
        EuxRinaTerminatorApi["eux-rina-terminator-api"]
    end

    style Kafka fill:#e1f5fe
    style DataPopulering fill:#f3e5f5
    style P1 fill:#fff3e0
    style P2 fill:#e8f5e9
    style P3 fill:#fce4ec
    style P4 fill:#f5f5f5
    style P5 fill:#e0f2f1
    style P6 fill:#fff8e1
    style P7 fill:#fbe9e7
    style ExternalAPI fill:#e3f2fd
```

## Status Oversikt

```mermaid
stateDiagram-v2
    [*] --> NY_SAK: Kafka Event\n(Case/Document)
    
    NY_SAK --> UVIRKSOM: sett-uvirksom\n(etter X dager)
    UVIRKSOM --> NY_SAK: Nytt dokument mottatt
    
    UVIRKSOM --> TIL_AVSLUTNING_LOKALT: til-avslutning\n(kriterier møtt, lokal scope)
    UVIRKSOM --> TIL_AVSLUTNING_GLOBALT: til-avslutning\n(kriterier møtt, global scope)
    UVIRKSOM --> AVSLUTTES_AV_MOTPART: til-avslutning\n(ingen scope, er motpart)
    UVIRKSOM --> OPPRETT_OPPGAVE: til-avslutning\n(opprettOppgave=true)
    
    TIL_AVSLUTNING_LOKALT --> AVSLUTTET_LOKALT: avslutt\n(suksess)
    TIL_AVSLUTNING_LOKALT --> HANDLING_MANGLER: avslutt\n(conflict)
    TIL_AVSLUTNING_LOKALT --> HANDLING_FEILET: avslutt\n(feil)
    
    TIL_AVSLUTNING_GLOBALT --> AVSLUTTET_GLOBALT: avslutt\n(suksess)
    TIL_AVSLUTNING_GLOBALT --> HANDLING_MANGLER: avslutt\n(conflict)
    TIL_AVSLUTNING_GLOBALT --> HANDLING_FEILET: avslutt\n(feil)
    
    OPPRETT_OPPGAVE --> OPPGAVE_OPPRETTET: lag-oppgave\n(ikke implementert)
    
    AVSLUTTET_LOKALT --> TIL_ARKIVERING: til-arkivering\n(etter X dager)
    AVSLUTTET_GLOBALT --> TIL_ARKIVERING: til-arkivering\n(etter X dager)
    
    TIL_ARKIVERING --> ARKIVERT: arkiver\n(suksess)
    TIL_ARKIVERING --> HANDLING_MANGLER: arkiver\n(conflict)
    TIL_ARKIVERING --> HANDLING_FEILET: arkiver\n(feil)
    
    SLETT_DOKUMENTUTKAST --> UVIRKSOM: slett-dokumentutkast\n(suksess)
    SLETT_DOKUMENTUTKAST --> HANDLING_MANGLER: slett-dokumentutkast\n(conflict)
    SLETT_DOKUMENTUTKAST --> HANDLING_FEILET: slett-dokumentutkast\n(feil)
    
    ARKIVERT --> [*]
    AVSLUTTES_AV_MOTPART --> [*]
    KAN_IKKE_AVSLUTTES --> [*]
```

## Til-Avslutning Beslutningslogikk

```mermaid
flowchart TB
    Start["Rinasak med status UVIRKSOM"] --> CheckRole{"Er NAV sakseier?"}
    
    CheckRole -->|"Ja (PO)"| CheckSakseierScope{"bucAvsluttScopeSakseier\ndefinert?"}
    CheckRole -->|"Nei (CP)"| CheckMotpartScope{"bucAvsluttScopeMotpart\ndefinert?"}
    
    CheckSakseierScope -->|"Ja"| EvalSakseier["Evaluer kriterier\n(scope fra sakseier)"]
    CheckSakseierScope -->|"Nei"| AvsluttesAvMotpart["AVSLUTTES_AV_MOTPART"]
    
    CheckMotpartScope -->|"Ja"| EvalMotpart["Evaluer kriterier\n(scope fra motpart)"]
    CheckMotpartScope -->|"Nei"| AvsluttesAvMotpart
    
    EvalSakseier --> Criteria
    EvalMotpart --> Criteria
    
    Criteria{"Avslutningskriterier"}
    
    Criteria -->|"1. sisteSedFraNavAvslutning\n(krevesSendtFraNav=true\nog siste SED fra NAV\nog sedType i liste)"| AvsluttMed["Avslutt med scope"]
    
    Criteria -->|"2. sisteSedForAvslutning\n(krevesSendtFraNav=false\nog sedType i liste)"| AvsluttMed
    
    Criteria -->|"3. sedExistsForAvslutning\n(SED i sedExistsForAvslutningAutomatisk)"| AvsluttMed
    
    Criteria -->|"4. mottattSedExistsForAvslutning\n(Mottatt SED i liste)"| AvsluttMed
    
    Criteria -->|"5. sentSedExistsForAvslutning\n(Sendt SED i liste)"| AvsluttMed
    
    Criteria -->|"Ingen kriterier møtt"| CheckOppgave{"opprettOppgave=true?"}
    
    CheckOppgave -->|"Ja"| OpprettOppgave["OPPRETT_OPPGAVE"]
    CheckOppgave -->|"Nei"| IngenEndring["Ingen statusendring"]
    
    AvsluttMed --> ScopeCheck{"Scope type"}
    ScopeCheck -->|"AVSLUTT_LOKALT"| TilAvslutningLokalt["TIL_AVSLUTNING_LOKALT"]
    ScopeCheck -->|"AVSLUTT_GLOBALT"| TilAvslutningGlobalt["TIL_AVSLUTNING_GLOBALT"]
    
    style Start fill:#e1f5fe
    style AvsluttesAvMotpart fill:#fff3e0
    style OpprettOppgave fill:#fce4ec
    style TilAvslutningLokalt fill:#e8f5e9
    style TilAvslutningGlobalt fill:#e8f5e9
    style IngenEndring fill:#f5f5f5
```

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

### Slett Dokumentutkast

Sletter dokumentutkast for X001 i rinasaker som har status `SLETT_DOKUMENTUTKAST`. 
Ved suksess settes saken tilbake til `UVIRKSOM` status. Denne prosessen bruker 
`SlettDokumentutkastService` for å utføre operasjonen via `eux-rina-terminator-api`.

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
    * `slett-dokumentutkast` - Sletter dokumentutkast for X001

## BUC Setup

`Buc.kt` definerer hvordan BUC-er skal håndteres i applikasjonen.

| Variabel                                           | Beskrivelse                                                                                                                             | Eksempelverdi                     |
|----------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------|
| `navn`                                             | Navnet på BUC, må matche med setup i RINA                                                                                               | `"H_BUC_01"`                      |
| `antallDagerBeforeUvirksom`                        | Antall dager før BUC blir uvirksom, uvirksom er staten før det kjøres regler for å se om det skal avsluttes                             | `90`                              |
| `antallDagerBeforeArkivering`                      | Antall dager før arkivering                                                                                                             | `180`                             |
| `sisteSedForAvslutningAutomatisk`                  | Liste over SED-er for automatisk avslutning, BUC vil avsluttes automatisk hvis saken er uvirksom og SED'en er i lista                   | `["H002"]`                        |
| `sisteSedForAvslutningAutomatiskKrevesSendtFraNav` | Kreves siste SED sendt fra NAV for automatisk avslutning, `true` hvis det kreves at siste SED er sendt fra NAV                          | `false`                           |
| `sedExistsForAvslutningAutomatisk`                 | Liste over eksisterende SED-er for automatisk avslutning, hvis SED i lista eksisterer kan det avsluttes automatisk hvis BUC er uvirksom | `["F003"]`                        |
| `mottattSedExistsForAvslutningAutomatisk`          | Liste over mottatte SED-er for automatisk avslutning, hvis SED i lista eksisterer kan det avsluttes automatisk hvis BUC er uvirksom     | `["U002", "U004"]`                |
| `sentSedExistsForAvslutningAutomatisk`             | Liste over sendte SED-er for automatisk avslutning, hvis SED i lista eksisterer kan det avsluttes automatisk hvis BUC er uvirksom       | `["H070"]`                        |
| `bucAvsluttScopeSakseier`                          | Angir om saken skal avsluttes lokalt eller globalt automatisk når vi er sakseier, hvis `null` vil ikke saken avsluttes automatisk       | `BucAvsluttScope.AVSLUTT_LOKALT`  |
| `bucAvsluttScopeMotpart`                           | Angir om saken skal avsluttes lokalt eller globalt automatisk når vi er motpart, hvis `null` vil ikke saken avsluttes automatisk        | `BucAvsluttScope.AVSLUTT_GLOBALT` |
| `opprettOppgave`                                   | Hvis ikke kriterier er møtt og saken er uvirksom kan det lages oppgave, aktivt ved `true`                                               | `true`                            |
