#language:de
Funktionalität: Account Registrierung
Kerstin möchte sich ein Benutzerkonto anlegen,
um Zugriff auf alle geschützten Bereiche zu erhalten.

  @finished
  Szenario: Erfolgreiche Registrierung
  Angenommen Kerstin hat die Einladungskarte erhalten
  Und        ruft die Registrierungsseite auf
  Wenn       sie sich mit ihren Daten für ein neues Benutzerkonto registriert
  Dann       wird eine Bestätigungsseite mit einer persönlichen Begrüßung angezeigt
  Und        wird ein Konto für Sie angelegt
  Und        erhalten die Administratoren eine Benachrichtigung

  @finished
  Szenario: Kein Login ohne Freischaltung
  Angenommen Kerstin hat sich registriert
  Wenn       Kerstin versucht sich anzumelden
  Dann       wird Kerstin die Anmeldung verweigert

  @finished
  Szenario: Benutzerfreischaltung
  Angenommen Kerstin hat sich registriert
  Und        Stefan hat sich angemeldet
  Wenn       er Kerstin freischaltet
  Dann       erhält Kerstin eine E-Mail mit einem automatisch generierten Passwort
  Und        kann sich Kerstin mit dem neuen Passwort anmelden

  @finished
  Szenario: Benutzerfreischaltung nur für Administratoren
  Angenommen Kerstin wurde freigeschaltet
  Und        Kerstin hat sich angemeldet
  Wenn       Kerstin die Gästeliste aufruft
  Dann       wird Kerstin der Zugang verwehrt
