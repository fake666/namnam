package namnam.model;

/**
 * represents food
 * @author fake
 */
public class Essen {

    private Preis preis;
    private String beschreibung;

    private boolean isVegetarian = false;
    private boolean isMoslem = false;
    private boolean isBeef = false;

    private final static char moslemToken = 'X';
    private final static char veggieToken = 'V';
    private final static char beefToken   = 'R';


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
        isVegetarian = isBeef = isMoslem = false;
        this.beschreibung = beschreibung.replaceAll("\n", "").replaceAll("\t", "");
        parseDescription();
    }

    public boolean isBeef() {
        return isBeef;
    }

    public void setIsBeef(boolean isBeef) {
        this.isBeef = isBeef;
    }

    public boolean isMoslem() {
        return isMoslem;
    }

    public void setIsMoslem(boolean isMoslem) {
        this.isMoslem = isMoslem;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
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
                    isVegetarian = true;
                    break;
                } else if(token.charAt(c) == moslemToken) {
                    isMoslem = true;
                    break;
                } else if(token.charAt(c) == beefToken) {
                    isBeef = true;
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
