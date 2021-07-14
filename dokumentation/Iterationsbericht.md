1. Projekt in Kotlin umschreiben
   
![Services](/dokumentation/Kotlin_Issue.png?raw=true)

Wir haben und als Team nach Rücksprache mit dem Dozenten dafür entschieden, das Parkhaus statt in Java in Kotlin zu schreiben. 
Auf diese Weise konnten wir eine aufstrebende Java-ähnliche Sprache erlernen und direkt in der Praxis anwenden. 



2. Services (Dependency Injection) hinzufügen

![Services](/dokumentation/Services_Issues.png?raw=true)

Wir haben uns dazu entschlossen, unsere Struktur auf Basis von Services und Servlets aufzubauen.
Auf diese Weise konnten wir die Aufgaben eines Requests klar aufteilen: Die Servlets nehmen die Requests an und leiten sie
an die entsprechende Funktion in dem Service weiter. Die Services verarbeiten die Daten und liefern sie an das Servlet zurück, 
das wiederum die Antwort an den Client oder die Webkomponente schickt. So werden die Integrationslogik und die Fachlogik
sinnvoll getrennt.


3. MVP
   
![MVP](/dokumentation/MVP_Milestone.png?raw=true)

Unser nächstes Ziel war es, das von und festgelegte MVP fertigzustellen. Nun, da unsere Grundstruktur stand, 
konnten wir uns voll auf die User Stories konzentrieren, die mit diesem Thema verbunden waren und ein Parkhaus mit den 
geforderten Funktionen implementieren.


4. Datenbank vollständig Implementieren

![Datenbank](/dokumentation/Milestone_JPA.png?raw=true)

Während unserer Arbeit fiel uns auf, dass bei unserer Struktur bei jeder Erstellung eines neuen Parkhauses die Daten des
Alten verloren gingen. Das war nicht von uns gewollt, und das Ziel, die Daten über die Lebensdauer des Servlets hinaus 
zu speichern, wurde der Agenda hinzugefügt. An dieser Stelle konnten wir auf umfangreiches Vorwissen von Thomas zurückgreifen, 
der eine HSQL-Datenbank für das Projekt anlegte. So wurden wir von Servlet-Kontext unabhängig und konnten auch über Server-Neustarts hinweg
die Daten speichern.

