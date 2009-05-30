package de.metalab.namnam.model;

/**
 * represents food
 * @author fake
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

    public Essen(String beschreibung, Integer wert) {
        this.preis = new Preis(wert);
        this.beschreibung = beschreibung.replaceAll("\n", "").replaceAll("\t", "");

        // parse the description
        parseDescription();
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        vegetarian = beef = moslem = false;
        this.beschreibung = beschreibung.replaceAll("\n", "").replaceAll("\t", "");
        parseDescription();
    }

    public boolean isBeef() {
        return beef;
    }

    public void setBeef(boolean beef) {
        this.beef = beef;
    }

    public boolean isMoslem() {
        return moslem;
    }

    public void setMoslem(boolean moslem) {
        this.moslem = moslem;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    

    public Preis getPreis() {
        return preis;
    }

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
