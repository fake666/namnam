package namnam.twitterer;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author fake
 */
public class Main {


    /**
     * @param args the command line arguments
     */

    public static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String args[]) {

        // set the calendar to today
        Calendar today = Calendar.getInstance();
        //today.add(Calendar.DAY_OF_MONTH,12);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if(today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            logger.log(Level.WARNING,"Wochenende! Ich hab' frei!");
            return;
        }

        logger.log(Level.INFO, "twittering ingolstadt");
        NamNamTwittererIN in = new NamNamTwittererIN();
        in.sendMenue(today.getTime());
        logger.log(Level.INFO, "twittering eichstaett");
        NamNamTwittererEI ei = new NamNamTwittererEI();
        ei.sendMenue(today.getTime());
    }

}
