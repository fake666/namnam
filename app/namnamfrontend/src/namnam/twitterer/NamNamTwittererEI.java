package namnam.twitterer;

import namnam.parser.erlangennuernberg.NamNamParserEI;
import winterwell.jtwitter.Twitter;

/**
 * twitter ingolstadt updates
 * @author fake
 */
public class NamNamTwittererEI extends NamNamTwitterer {

    public NamNamTwittererEI() {
        super();
        myTwitter = new Twitter("kuei_mensa","toppas123");
        myParser = new NamNamParserEI();
    }

}
