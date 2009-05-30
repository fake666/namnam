package de.metalab.namnam.parser.erlangennuernberg;


/**
 * parser specific for the ingolstadt URL
 *
 * @author fake
 */
public class NamNamParserIN extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-IN";

    public NamNamParserIN() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-ingolstadt.shtml";
    }

    protected String getMensaName() {
        return NamNamParserIN.name;
    }
}
