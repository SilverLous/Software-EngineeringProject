## Gesamt-Retrospektive:

Thomas:

###Was lief gut?

Die Teamarbeit im Allgemeinen lief nach der Hälfte der Zeit gut, da dann auch jeder wusste, wovon der andere spricht und 
das Projekt immer mehr Form annahm.

###Was hat sich bewährt?

Issues Schreiben und Sprint Meetings halten, um jeden auf dem Laufenden zu halten.

###Was waren Erfolgsrezepte?

- Dezentrale Datenbank, um Seiteneffekte zu verhindern
- Dependency Injection für eine simple aber mächtige Logik Verwaltung

###Was lief schlecht?

Nicht zeitlich eingehaltene bzw. überhaupt angefangene TODOS geduldet, um alle mitzunehmen,
hat dem Projekt insbesondere am Anfang mehr geschadet als geholfen.

Wir haben versucht, Servlet Tests zu erstellen, aber weder Arquillian noch Mockito haben zu einem verwertbaren Ergebnis geführt,
weil die Dependency Injection in den Tests nicht funktionierte.

###Was würde man anders machen, wenn man noch einmal neu anfangen könnte?

Spring verwenden, der durchschnittliche Stack Overflow Artikel war 5 bis 10 Jahre alt und fast nie mit Jakarta kompatibel.
Zudem gab es für die Datenbank bei Spring auf den ersten Blick Build-in Funktionen, um Repositorys zu erstellen, was das ganze Projekt einfacher gemacht hätte.
Oder zurück auf javax gehen, damit man sich nicht unnötig Aufwand macht.

TomcatEE oder Glassfish nutzen, durch die nachträgliche Erweiterung der CDI haben wir uns insbesondere in Tests und
in manch einer dubiosen Fehlermeldung unnötig viel Stress eingehandelt.

###Also was hat man daraus gelernt und was würde man im nächsten Projekt besser machen?

Aufpassen, dass die Zentrale Dependency Klasse nicht zu groß wird, Einfalt Sektionen machen, um Logik in einer Klasse zu sortieren.

