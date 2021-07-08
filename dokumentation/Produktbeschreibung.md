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

### Deploment
 - Gitlab Pipelines
 - Jenkins Pipelines
 - SonarQube Analysen
 - Traefik als reverse proxy
 - Docker-Swarm als Container Umgebung
 - Tomcat 10


# Lines of Code

