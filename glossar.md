Geschäftslogik:
- Depencency Injection: Eine Möglichkeit Logik von UI via Services zu trennen. Das hier verwendete Framework ist Weld
- Global Service: Eine in der Applikation einmalig existierende Klasse, egal wo man sie aufruft
- Session Service: Eine Klasse die pro Browser existiert, und bei jedem neuen Anwender neu erstellt wird

Fachlogik:
- Auto: Ein PKW, welcher im Parkhaus einen Platz findet und eine gewisse Zeit steht.
- Einfahren: Das Passieren der Schranke und Parkplatz finden eines Autos.
- Ausfahren: Das Aufgeben des Parkplatzes, Bezahlen und Verlassen des Parkhauses eines Autos.
- Einfahrtzeit: Der Zeitstempel an dem das Auto einfährt.
- Ausfahrtzeit: Der Zeitstempel an dem das Auto ausfährt.
- Kosten: Der Geldbetrag, der zum Ausfahren aus dem Parkhaus bezahlt werden muss, welcher sich aus der Differenz von 
  Ausfahrtzeit und Einfahrzeit und der Art des Autos ergibt.