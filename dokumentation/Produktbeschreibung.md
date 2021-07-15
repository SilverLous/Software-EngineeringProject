SE1 Team 7 Parkhaus Produktbeschreibung
======
Inhaltsverzeichnis
======


[[_TOC_]]


# Produktumfang

Klassische Funktionalität des Beispiels 

### Standortbasiertes Sytem

In diesem Projekt ist es möglich in 1.605 ausgewählten Deutschen Standorten (zufällig) zu Parken.
 - Hierbei wird bei jedem neuen Kunden (Browser Session) ein neuer Standort eröffnet.
 - Kunden können dynamisch zu bereits existierenden Parkhäusern wechseln.
 - Für das Wiederfinden des Parkhauses als letztes Element die Koordinaten des Parkhauses als Karte verlinkt.

### Einfache Statistiken und Überwachung

Für alle Parkhäuser gibt es eine einheitliche Managerseite, in der man Statistiken über alle Parkhäuser einsehen kann.
  - Live aktualisierende Tages- und Wocheneinnahmen
  - Einnahmen per Auto-Typ
  - Einnahmen per Bundesland
  - Live Webcam in unterstützte Parkhäuser




# Verwendete Technologien

### Backend
 - Kotlin 1.5
 - Jakarta
 - RxJava
 - EclipseLink + HSQLDB
 - Weld 4.0.0.Final


### Frontend
 - JS Websockets 
 - bootstrap 5
 - Plotly
 - Axios
 - ccm.parkhaus

### Deployment
 - Gitlab Pipelines
   - Maven develop Pipeline
   - Docker Productive Pipeline  
 - Jenkins Pipelines
   - SonarQube Analysen
 - Docker-Swarm als Container Umgebung
     - Traefik als reverse proxy
     - Tomcat 10


# Lines of Code

Alle Kotlin, Java und JSP Dateien seit 2021-04-30 (herausgerechnet starter repo code)
stand: 15.07.2021 15:45

Total commits: 367 <br>
Total ctimes: 1970 <br>
Total files: 91 <br>
Total loc: 3415 <br>

| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach |  1801 |    139 |     37  | 41.07/37.26  |
| Lukas Gerlach  |  1666 |    102 |     22  | 37.99/27.34  |
| Alexander      |   905 |    124 |     22  | 20.63/33.24  |
| Eileen Hanz    |    13 |      8 |      3 | 0.30/2.14   |


