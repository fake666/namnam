package org.bytewerk.namnam.model;

/**
 * diese klasse repraesentiert ein generisches essen mit einem preis
 * und dient eigentlich momentan nur als basisklasse fuer die
 * Mensaessen-klasse.
 *
 * @see org.bytewerk.namnam.model.Mensaessen
 * @author fake
 * @author testicle
 * @author nati
 */
public class Essen {

    private Preis preis;
    private String beschreibung;

    private boolean vegetarian = false;
    private boolean moslem = false;
    private boolean beef = false;

    private final static char moslemToken = 'X';
    private final static char veggieToken = 'V';
    private final static char beefToken   = 'R';


    public Essen() {
    }

    /**
     * initialisert ein essen-objekt mit beschreibung und preis in cent
     * bis zu 2stellige zahlen am ende der beschreibung werden abgeschnitten,
     * ein V markiert das essen als vegetarisch, ein X als ohne schweinefleisch
     * und ein R als mit Rind.
     * 
     * @param beschreibung die beschreibung des essens, aus der die vegetarisch/k. schweinefl./rind daten extrahiert werden
     * @param wert der preis des essens in cent
     */
    public Essen(String beschreibung, Integer wert) {
        this.preis = new Preis(wert);
        this.beschreibung = beschreibung.replaceAll("\n", "").replaceAll("\t", "");

        // parse the description
        parseDescription();
    }

    /**
     * gibt den reinen text der beschreibung des essens zurueck. hier sind die token
     * am ende (V,X,R) sowie eventuelle bis zu 2stellige zahlen am ende bereits
     * entfernt!
     *
     * @return den reinen beschreibungstext des essens.
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * setzt die beschreibung des essens auf den angegebenen text. versucht, wie
     * im konstruktor beschrieben zu parsen.
     *
     * @param beschreibung
     */
    public void setBeschreibung(String beschreibung) {
        vegetarian = beef = moslem = false;
        this.beschreibung = beschreibung.replaceAll("\n", "").replaceAll("\t", "");
        parseDescription();
    }

    /**
     * gibt an, ob dieses essen als "enthaelt rind" markiert wurde
     * @return true, wenn dieses essen rind enthaelt, false andernfalls
     */
    public boolean isBeef() {
        return beef;
    }
    /**
     * legt fest ob dieses essen rind enthaelt
     * @param beef true, wenn das essen rind enthaelt, false andernfalls
     */
    public void setBeef(boolean beef) {
        this.beef = beef;
    }

    /**
     * gibt an, ob dieses essen als "enthaelt kein schweinefleisch" markiert wurde
     * die beziechung "moslem" ist schlecht gewaehlt, das seh ich ein. im verhaeltnis
     * zu isBeef koennte man meinen das essen enthalte moslems. das ist hoffentlich
     * niemals der fall! ;-)
     *
     * @return true, wenn dieses essen kein schweinefleisch enthaelt, false andernfalls
     */
    public boolean isMoslem() {
        return moslem;
    }
    /**
     * legt fest ob dieses essen kein schweinefleisch etnhaelt.
     * sollte das essen tatsaechlich moslems enthalten, wenden sie sich bitte ans
     * gesundheitsamt ;-))
     * @param moslem true, wenn das essen kein schweinefleisch enthaelt, false andernfalls
     */
    public void setMoslem(boolean moslem) {
        this.moslem = moslem;
    }

    /**
     * gibt an, ob dieses essen als "vegetarisch" markiert wurde
     * @return true, wenn dieses essen vegetarisch ist, false andernfalls
     */
    public boolean isVegetarian() {
        return vegetarian;
    }
    /**
     * legt fest ob dieses essen vegetarisch ist.
     * @param vegetarian true, wenn das essen vegetarisch ist, false andernfalls
     */
    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    /**
     * gibt das mit diesem essen assoziierte "Preis"-objekt zurueck, das ist der nicht-reduzierte
     * Preis, den "jedermann" zahlen muss, der kein student ist.
     * @return den normalen, nicht reduzierten preis des essens
     */
    public Preis getPreis() {
        return preis;
    }
    /**
     * legt den normalen, nicht reduzierten preis fuer dieses essen fest.
     * @param preis der zu setzende, normale, nicht reduzierte preis
     */
    public void setPreis(Preis preis) {
        this.preis = preis;
    }

    
    private void parseDescription() {
        // cut off chars from the back of the string. if it's numbers, keep on
        // stripping; if it's one of the tokens above prepended by a space, set
        // the sate accordingly.
        String[] tokens = beschreibung.split(" ");
        int n = tokens.length-1;
        for(; n>=0; n--) {
            String token = tokens[n].trim();
            if(token.equals("")) continue; // skip empty
            if(token.length() > 2) break; // past the last token!

            for(int c=0; c < token.length();c++) {
                if(token.charAt(c) == veggieToken) {
                    vegetarian = true;
                    break;
                } else if(token.charAt(c) == moslemToken) {
                    moslem = true;
                    break;
                } else if(token.charAt(c) == beefToken) {
                    beef = true;
                    break;
                }
            }
        }
        // now n contains the last token that is interesting.
        this.beschreibung = "";
        for(int m = 0; m <= n; m++) {
            if(!tokens[m].trim().equals("")) //skip only-spaces-tokens
                this.beschreibung += tokens[m] + " ";
        }
        this.beschreibung = this.beschreibung.trim();
    }
    

}
