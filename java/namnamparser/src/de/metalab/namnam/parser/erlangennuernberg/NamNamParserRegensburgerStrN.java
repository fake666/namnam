package de.metalab.namnam.parser.erlangennuernberg;


/**
 * parser specific for the regensburger str nuernberg URL
 *
 * @author fake
 */
public class NamNamParserRegensburgerStrN extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-Regensburgerstr.-N";

    public NamNamParserRegensburgerStrN() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-n-regens.shtml";
    }

    public String getMensaName() {
        return NamNamParserRegensburgerStrN.name;
    }
}
