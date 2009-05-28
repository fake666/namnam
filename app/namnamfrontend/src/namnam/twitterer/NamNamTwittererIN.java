package namnam.twitterer;

import namnam.parser.erlangennuernberg.NamNamParserIN;
import winterwell.jtwitter.Twitter;

/**
 * twitter ingolstadt updates
 * @author fake
 */
public class NamNamTwittererIN extends NamNamTwitterer {

    public NamNamTwittererIN() {
        super();
        myTwitter = new Twitter("hsin_mensa","toppas123");
        myParser = new NamNamParserIN();
    }

}
