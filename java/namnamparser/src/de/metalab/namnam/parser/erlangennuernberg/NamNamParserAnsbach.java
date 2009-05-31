package de.metalab.namnam.parser.erlangennuernberg;


/**
 * parser specific for the ansbach URL
 *
 * @author fake
 */
public class NamNamParserAnsbach extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-Ansbach";

    public NamNamParserAnsbach() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-ansbach.shtml";
    }

    public String getMensaName() {
        return NamNamParserAnsbach.name;
    }
}
