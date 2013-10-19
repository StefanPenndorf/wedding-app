#language:de
@finished
Funktionalität: Fotoalbum
Die Gäste können Fotoalben anschauen und selbst Bilder in ein Fotoalbum hochladen.

  Grundlage:
    Angenommen Kerstin wurde freigeschaltet
    Und        Kerstin hat sich angemeldet

  Szenario: Fotos hochladen
    Angenommen Kerstin ruft die Fotoalben auf
    Wenn       sie ein Bild hochlädt
    Dann       wird ein Fotoalbum für sie erstellt
    Und        kann sie das erste Foto anschauen

  Szenario: Vorblättern im Fotoalbum
    Angenommen Kerstin hat drei Bilder hochgeladen
    Und        Kerstin ruft ihr Fotoalbum auf
    Wenn       sie ein Bild weiter blättert
    Dann       kann sie das zweite Foto anschauen

  Szenario: Zurückblättern im Fotoalbum
    Angenommen Kerstin hat drei Bilder hochgeladen
    Und        Kerstin ruft ihr Fotoalbum auf
    Wenn       sie ein Bild weiter blättert
    Und        sie ein Bild zurück blättert
    Dann       kann sie das erste Foto anschauen
