#language:de
Funktionalität: Fotoalbum
Die Gäste können Fotoalben anschauen und selbst Bilder in ein Fotoalbum hochladen.

  Grundlage:
    Angenommen Kerstin wurde freigeschaltet
    Und        Kerstin hat sich angemeldet

  @finished
  Szenario: Fotos hochladen
    Angenommen Kerstin ruft die Fotoalben auf
    Wenn       sie ein Bild hochlädt
    Dann       wird ein Fotoalbum für sie erstellt
    Und        kann sie das Foto anschauen

  @current
  Szenario: Blättern im Fotoalbum
    Angenommen Kerstin hat drei Bilder hochgeladen
    Und        Kerstin ruft ihr Fotoalbum auf
    Wenn       sie zum zweiten Bild blättert
    Dann       kann sie das zweite Foto anschauen
