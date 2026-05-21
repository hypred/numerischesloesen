# numerischesloesen

Dieses kleine Projekt berechnet eine Nullstelle der Gleichung f(x)=a*x+b und g(x)=c*d^x.

Web-Oberfläche
---------------

Kompilieren und lokalen Server starten:

```bash
javac loesung.java WebServer.java
java WebServer
```

Öffne dann im Browser: http://localhost:8080/

Alternativ: statische Website (empfohlen)
--------------------------------------

Die Web-Oberfläche ist komplett clientseitig und benötigt keinen Java-Server. Du kannst sie einfach per GitHub Pages veröffentlichen:

1. Push das Repo nach GitHub (Branch `main`).
2. Die GitHub Actions Workflow-Datei veröffentlicht automatisch den Inhalt des Verzeichnisses `web/` als GitHub Pages Seite.
3. Nach erfolgreichem Workflow findest du die Seite unter `https://<owner>.github.io/<repo>/`.

Wenn du lieber manuell veröffentlichen willst, kannst du auch das Verzeichnis `web/` in das `docs/`-Verzeichnis verschieben und GitHub Pages aus `docs/` aktivieren.
