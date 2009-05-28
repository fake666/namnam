package namnam.twitterer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import namnam.model.Mensaessen;
import namnam.model.Tagesmenue;
import namnam.parser.NamNamParser;
import winterwell.jtwitter.Twitter;

/**
 * base class for twittering daily menues
 * @author fake
 */
public abstract class NamNamTwitterer {

    protected static NamNamParser myParser;
    protected static SimpleDateFormat df;

    protected static boolean doTwitter = false;

    protected Twitter myTwitter;

    public static final Logger logger = Logger.getLogger(NamNamTwitterer.class.getName());

    public NamNamTwitterer() {
        df = new SimpleDateFormat("E dd.MM.", Locale.GERMAN);
    }

    public static void setDoTwitter(boolean yesno) {
        doTwitter = yesno;
    }
    public static boolean isDoTwitter() {
        return doTwitter;
    }

    public void sendMenue(Date theDate) {

        try {
            Map<Date,Tagesmenue> result = myParser.getCurrentMenues();
            if(result == null || result.isEmpty()) {
                logger.log(Level.SEVERE,"NO menues found for any day!");
                if(doTwitter)
                    myTwitter.updateStatus("@fake666 Gar kein Happa gefunden! Irgendwas stimmt nicht! Hilfe!");
            }

            Tagesmenue t = result.get(theDate);
            if(t == null) {
                logger.log(Level.SEVERE,"Konnte leider kein happa finden!");
                if(doTwitter)
                    myTwitter.updateStatus("Konnte leider kein Happa für "+df.format(theDate)+" finden :(");
            }
            Iterator<Mensaessen> mIt = t.getMensaessen().iterator();
            while(mIt.hasNext()) {
                Mensaessen me = mIt.next();
                String date = df.format(theDate) + ": ";
                String end = " (" + me.getStudentenPreis() + " €/" + me.getPreis() + " €)" ;
                if(me.isBeef()) {
                    end = " (mit Rindfl.)" + end;
                } else if(me.isMoslem()) {
                    end = " (k. Schweinefl.)" + end;
                } else if(me.isVegetarian()) {
                    end = " (vegetarisch)" + end;
                }
                String status = date + me.getBeschreibung() + end;

                if(status.length() > 140) {
                    int toomuch = 140 - status.length();

                    status = date + me.getBeschreibung().substring(0,toomuch-3) + "..."+ end;
                }

                if(doTwitter)
                    myTwitter.updateStatus(status);

                System.out.println(status);
            }
        } catch (Exception ex) {
            System.err.println(df.format(theDate) + ": Heute gibt's wohl nix!");
            ex.printStackTrace();

            if(doTwitter)
                myTwitter.updateStatus(df.format(theDate) + ": Heute gibt's wohl nix!");
        }


    }
}
