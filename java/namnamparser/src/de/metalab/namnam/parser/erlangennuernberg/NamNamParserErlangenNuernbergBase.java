package de.metalab.namnam.parser.erlangennuernberg;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import de.metalab.namnam.model.Mensa;
import de.metalab.namnam.model.Mensaessen;
import de.metalab.namnam.model.Tagesmenue;
import de.metalab.namnam.parser.NamNamParser;
import de.metalab.namnam.parser.util.MutableNamespaceContext;
import de.metalab.namnam.parser.util.XPathUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * common functionality for all menues from the erlangen/nuernberg studentenwerk
 * 
 * if you wonder why this parser is subclassed just for the urls, keep in mind that
 * the format of the html pages can change any time and may be inconsistent between
 * the differen locations.
 *
 * @author fake
 */
public abstract class NamNamParserErlangenNuernbergBase implements NamNamParser {

    private static Logger logger = Logger.getLogger(NamNamParserErlangenNuernbergBase.class.getName());

    protected String theURL; // this is what children fill in
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    protected Pattern pricePattern = Pattern.compile("[0-9]+,[0-9]+");
    protected DecimalFormat df = new DecimalFormat();
    protected DecimalFormatSymbols decf = new DecimalFormatSymbols(Locale.GERMAN);

    protected NamNamParserErlangenNuernbergBase() {
        this.df.setDecimalFormatSymbols(this.decf);
    }

    abstract public String getMensaName();

    public Mensa getCurrentMenues() throws Exception {
        Mensa mensa = new Mensa(this.getMensaName());

        Node node = XPathUtil.getHtmlUrlNode(theURL, "latin1");
        MutableNamespaceContext nc = new MutableNamespaceContext();
        nc.setNamespace("html", "http://www.w3.org/1999/xhtml");

        String QUERY = "//html:table";

        // leave it all behind
        NodeList qResult = XPathUtil.query(node, QUERY, nc);
        if(qResult.getLength() == 0) {
            throw new NullPointerException("xpath query returned no nodes! please check xpath query!");
        }
        Node table = qResult.item(0).cloneNode(true);

        QUERY = "//*[contains(html:div,'Essen')]";

        NodeList anchors = XPathUtil.query(table, QUERY, nc);
        for (int n = 0; n < anchors.getLength(); n++) {
            Node td = anchors.item(n);
            String idx = td.getChildNodes().item(0).getTextContent();
            Node descTd = td.getNextSibling();
            String desc = descTd.getChildNodes().item(0).getTextContent();
            Node sPriceTd = descTd.getNextSibling();
            String sPrice = sPriceTd.getChildNodes().item(0).getTextContent();
            Node bPriceTd = sPriceTd.getNextSibling();
            String bPrice = bPriceTd.getChildNodes().item(0).getTextContent();

            // that was easy. the tricky part is to find the date!
            String date = "";
 	    // there may be funny special chars in the essen-name. of course. so we just check for
	    // the existense of a 1, 2 or 3 in the idx string...
	    // note however: during summer break, some mensae only offer 2 meals, so the date may be left of idx 1!
           if (idx.contains("1")) { // Esen 1: go down
                date = td.getParentNode().getNextSibling().
                        getChildNodes().item(0).getChildNodes().item(0).getTextContent();
		if(getDateFromString(date) == null) {
			//try with left of us:
			date = td.getPreviousSibling().getChildNodes().item(0).getTextContent();
		}
            } else if (idx.contains("2")) { // Essen 2: right here
                date = td.getPreviousSibling().
                        getChildNodes().item(0).getTextContent();
		if(getDateFromString(date) == null) {
			//try with left of the row above us: (summer mode)
                	date = td.getParentNode().getPreviousSibling().
                       	    getChildNodes().item(0).getChildNodes().item(0).getTextContent();
		}
            } else if (idx.contains("3")) { // Essen 3: go up
                date = td.getParentNode().getPreviousSibling().
                        getChildNodes().item(0).getChildNodes().item(0).getTextContent();
            } else {
                logger.log(Level.SEVERE, this.getMensaName() + ": unknown index skipped! " + idx);
                continue;
            }

            Date d = getDateFromString(date);
            if(d == null) {
		logger.log(Level.WARNING, this.getMensaName() + ": could not parse date '" + date  + "' for index " + idx);
		continue;
	    }
            
            Tagesmenue daymeal = mensa.getMenuForDate(d);
            if (daymeal == null) {
                daymeal = new Tagesmenue(d);
                mensa.addDayMenue(daymeal);
            }

	    Integer bPriceInCents = null;
            Integer sPriceInCents = null;
	    try {
	    	bPriceInCents = getPriceInCents(bPrice);
		if(bPriceInCents == null) throw new Exception("return value was null");
	    } catch (Exception ex) {
		logger.log(Level.SEVERE, this.getMensaName() + ", " + date + ": converting bed. price '"+bPrice+"' to cents failed",ex);
	    }
	    try {
	    	sPriceInCents = getPriceInCents(sPrice);
		if(sPriceInCents == null) throw new Exception("return value was null");
	    } catch (Exception ex) {
		logger.log(Level.SEVERE, this.getMensaName() + ", " + date + ": converting student price '"+sPrice+"' to cents failed",ex);
	    }

	    if(bPriceInCents == null || sPriceInCents == null) continue;

            Mensaessen me = new Mensaessen(desc, bPriceInCents, sPriceInCents);
            daymeal.addMenu(me);
        }

        return mensa;
    }

    protected Integer getPriceInCents(String s) throws Exception {
        if (s == null || "".equals(s.trim())) return null;
	Matcher m = pricePattern.matcher(s);
	if(!m.find()) {
		logger.log(Level.WARNING,this.getMensaName() + ": Price format did not match regular expression");
		return null;
	}
	s = m.group();
        return new Double(df.parse(s.substring(0, s.indexOf(',')+2)).doubleValue() * 100).intValue();
    }

    protected Date getDateFromString(String d) throws Exception {
        if(d == null || "".equals(d.trim()) || d.length() < 8) return null;
        Date date = sdf.parse(d.substring(3));
        return date;
    }
}
