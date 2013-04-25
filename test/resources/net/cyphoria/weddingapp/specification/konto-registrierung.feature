#language:de
Funktionalität: Account Registrierung
Kerstin möchte sich ein Benutzerkonto anlegen,
um Zugriff auf alle geschützten Bereiche zu erhalten.

  Szenario: Erfolgreiche Registrierung
  Angenommen Kerstin hat die Einladungskarte erhalten
  Und        ruft die Registrierungsseite auf
  Wenn       sie sich mit ihren Daten für ein neues Benutzerkonto registriert
  Dann       wird eine Bestätigungsseite mit einer persönlichen Begrüßung angezeigt
  Und        wird ein Konto für Sie angelegt
  Und        erhalten die Administratoren eine Benachrichtigung

  @current
  Szenario: Kein Login ohne Freischaltung
  Angenommen Kerstin hat sich registriert
  Wenn       Kerstin sich anmelden möchte
  Dann       erhält Kerstin eine Fehlermeldung

  Szenario: Benutzerfreischaltung
  Angenommen Stefan hat sich angemeldet
  Wenn       er Kerstin frei schaltet
  Dann       erhält Kerstin eine E-Mail mit einem automatisch generierten Passwort
  Und        kann sich Kerstin mit diesem Passwort anmelden

  Szenario: Benutzerreischaltung nur für Administratoren
  Angenommen Kerstin hat sich angemeldet
  Wenn       Kerstin die Gästeliste aufruft
  Dann       wird Kerstin der Zugang verwehrt
