package org.bytewerk.namnam.parser.erlangennuernberg;


/**
 * parser specific for the schuett nuernberg URL
 *
 * @author fake
 */
public class NamNamParserSchuettN extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-Schuett-N";

    public NamNamParserSchuettN() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-n-schuett.shtml";
    }

    public String getMensaName() {
        return NamNamParserSchuettN.name;
    }
}
