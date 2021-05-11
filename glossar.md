Geschäftslogik:
- Depencency Injection: Eine Möglichkeit Logik von UI via Services zu trennen. Hier verwendete Framework ist Weld
- Global Service: Eine in der Applikation einmalig existierende Klasse, egal wo man sie aufruft
- Session Service: Eine Klasse die pro Browser existiert und bei jedem neuen Kunden neu erstellt wird

Fachlogik:
- Kunde: Ein PKW welches im Parkhaus einen Platz findet und eine gewisse Zeit steht.
- Einfahren: Das Betreten und Parkplatz finden eines Kunden.
- Ausfahren: Das Aufgeben des Parkplatzes, Bezahlen und Verlassen des Parkhauses eines Kunden.
- Einfahrzeit: Der Zeitstempel an dem der Kunde einfährt.
- Ausfahrtzeit: Der Zeitstempel an dem der Kunde ausfährt.
- Kosten: Der Geldbetrag, der zum Ausfahren aus dem Parkhaus bezahlt werden muss, welcher sich aus der Differenz von 
  Ausfahrtzeit und Einfahrzeit und der Art des PKWs des Kunden.