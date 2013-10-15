#language:de
Funktionalität: Fotoalbum
Die Gäste können Fotoalben anschauen und selbst Bilder in ein Fotoalbum hochladen.

  Grundlage:
    Angenommen Kerstin wurde freigeschaltet
    Und        Kerstin hat sich angemeldet

  @current
  Szenario: Fotos hochladen
    Angenommen Kerstin ruft die Fotoalben auf
    Wenn       sie ein Bild hochlädt
    Dann       wird ein Fotoalbum für sie erstellt
    Und        kann sie das Foto anschauen
