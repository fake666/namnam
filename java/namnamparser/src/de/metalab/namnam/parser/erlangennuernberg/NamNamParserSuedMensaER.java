package de.metalab.namnam.parser.erlangennuernberg;


/**
 * parser specific for the sued mensa in erlangen URL
 *
 * @author fake
 */
public class NamNamParserSuedMensaER extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-Sued-Erlangen";

    public NamNamParserSuedMensaER() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-er-sued.shtml";
    }

    public String getMensaName() {
        return NamNamParserSuedMensaER.name;
    }
}
