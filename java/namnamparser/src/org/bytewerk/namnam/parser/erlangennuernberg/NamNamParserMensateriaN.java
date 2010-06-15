package org.bytewerk.namnam.parser.erlangennuernberg;


/**
 * parser specific for the mensateria nuernberg URL
 *
 * @author fake
 */
public class NamNamParserMensateriaN extends NamNamParserErlangenNuernbergBase {

    public static final String name = "Studiwerk-Erlangen-Nuernberg-Mensa-Mensateria-N";

    public NamNamParserMensateriaN() {
        super();
        this.theURL = "http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-n-mensateria.shtml";
    }

    public String getMensaName() {
        return NamNamParserMensateriaN.name;
    }
}
