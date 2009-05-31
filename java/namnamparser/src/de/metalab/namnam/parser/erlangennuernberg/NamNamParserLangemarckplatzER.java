package de.metalab.namnam.parser.erlangennuernberg;


/**
 * parser specific for the langemarckplatz URL
 *
 * @author fake
 */
public class NamNamParserLangemarckplatzER extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-Erlangen-Langemarckplatz";

    public NamNamParserLangemarckplatzER() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-er-langemarck.shtml";
    }

    public String getMensaName() {
        return NamNamParserLangemarckplatzER.name;
    }
}
