package de.metalab.namnam.parser.erlangennuernberg;


/**
 * parser specific for the ingolstadt URL
 *
 * @author fake
 */
public class NamNamParserEI extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-EI";

    public NamNamParserEI() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-eichstaett.shtml";
    }

    public String getMensaName() {
        return NamNamParserEI.name;
    }
}
