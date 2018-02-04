# Demo Implementierungsphase

## Vorbedingungen

###### Server
- Datenbank läuft
- Postfix läuft
- `./sensors/` Verzeichnis existiert

###### Sensoren
- Raspberry Pi mit Sensoren läuft, Skripte laufen
- Pi hat Verbindung zum Server

###### Client
- Client-App ist auf 2 Smartphones installiert
- Smartphones haben Verbindung zum Server


## Ablauf

Zu Beginn darauf hinweisen, dass **Timestamps von Sensoren** implementiert wurden!

### Server

Konfiguration des Servers erfolgt über SSH, damit die Benutzererstellung gezeigt werden kann.

###### Allgemeines Setup

1. Server starten
2. `help` Befehl zeigen

###### Benutzerverwaltung

3. 2 neue Benutzer anlegen (John und Jane Doe?)
4. `listusers` Befehl zeigen -> neue Benutzer werden angezeigt

###### Sensorenverwaltung

5. `listsensors` -> noch keine Sensoren konfiguriert
6. Server auschalten -> `stop`
7. Konfigurationsdateien in `./sensors/` verschieben (Konfigdateien zeigen?)
8. Server starten
9. `listsensors` -> neue Sensoren werden angezeigt


### Client

###### Login

1. mit einem der zuvor erstellten Benutzer einloggen
2. `Neues Muster` auswählen

###### Muster-Editor

Wir erstellen zunächst ein ganz simples Muster:
Button gedrückt -> Socket-Action

3. Sensor-Node hinzufügen -> in Liste werden die zuvor konfigurierten Sensoren angezeigt
4. Socket-Action hinzufügen
5. Menü der Action öffnen, Nachricht einstellen
6. Nodes verbinden
7. Muster abspeichern

###### Deployment (1)

8. In Musterliste das Muster deployen -> Symbol ändert sich, Serverausgabe zeigen?
9. Button drücken -> Muster wird erkannt (über Skript demonstrieren)  
   *falls Sensor nicht funktioniert, Simulation nutzen (muss noch vorbereitet werden)*
10. Muster undeployen
11. Button drücken -> Muster wird nicht mehr erkannt

###### Muster bearbeiten

Wir bauen das Muster so um, dass das Muster erkannt wird,
wenn der Knopf innerhalb von 10 Sekunden mind. 5 mal gedrückt wurde.

12. Auf zweitem Smartphone mit anderem Benutzer einloggen
13. Muster-Liste anzeigen -> zuvor erstelltes Muster wird angezeigt
14. Muster im Editor öffnen
15. Aggregation-Node hinzufügen
16. Sensor mit Aggregation verbinden
17. Aggregation bearbeiten -> richtiges Attribut und `Summe` auswählen, Zeitfenster `10 sek.` festlegen
18. Filter-Node hinzufügen, ConstantInputNode mit Wert `5` hinzufügen
19. Muster fertig zusammenbauen
20. Muster speichern

###### Deployment (2)

21. Bearbeitetes Muster deployen
22. Button 5x innerhalb 10 sek. drücken -> Muster wird erkannt (über Skript zeigen)
23. Button zu selten drücken -> Muster nicht mehr erkannt

###### Muster umbenennen

24. `Muster umbenennen` auswählen, neuen Namen festlegen
25. Auf anderem Smartphone: Neuer Name wird angezeigt (nach Aktualisieren)

###### Export zu Siddhi-QL
26. `Muster exportieren` auswählen -> Share-Intent öffnet sich
27. z.B. E-Mail zum Teilen auswählen, Code per E-Mail verschicken

###### Muster duplizieren

28. `Muster duplizieren` auswählen, neuen Namen festlegen
29. Muster bearbeiten, z.B. Zeitfenster ändern
30. Zeigen, dass ursprüngliches Muster unverändert ist

###### Muster löschen

31. Für das duplizierte Muster `Muster löschen` auswählen
32. Zeigen, dass das Muster auf beiden Smartphones nicht mehr angezeigt wird.

###### Komplexeres Muster

Idee: **Temperaturüberwachung**  
E-Mail schicken, wenn Temperatur 30 Sekunden lang über 25 °C ist und der Knopf nicht gedrückt wurde.

### Server

1. `listpatterns` zeigen: alle zuvor erstellten (und noch nicht gelöschten) Muster werden angezeigt
2. `deleteuser`: einen Benutzer löschen
3. Demonstrieren, dass sich der gelöschte Benutzer nicht mehr einloggen kann
