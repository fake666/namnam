package org.bytewerk.namnam.parser.erlangennuernberg;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.bytewerk.namnam.model.Mensa;
import org.bytewerk.namnam.model.Mensaessen;
import org.bytewerk.namnam.model.Tagesmenue;
import org.bytewerk.namnam.parser.NamNamParser;
import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * common functionality for all menues from the erlangen/nuernberg studentenwerk
 * <p/>
 * if you wonder why this parser is subclassed just for the urls, keep in mind
 * that the format of the html pages can change any time and may be inconsistent
 * between the different locations.
 *
 * @author fake
 * @author Jan Knieling
 */
public abstract class NamNamParserErlangenNuernbergBase implements NamNamParser {

    private static Logger logger = Logger
            .getLogger(NamNamParserErlangenNuernbergBase.class.getName());

    protected String theURL; // this is what children fill in
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    protected String priceRegex = "[\\s]*\\d+[,.]\\d\\d[\\s]*€[\\s]*";
    protected DecimalFormat df = new DecimalFormat();
    protected DecimalFormatSymbols decf = new DecimalFormatSymbols(
            Locale.GERMAN);

    protected NamNamParserErlangenNuernbergBase() {
        this.df.setDecimalFormatSymbols(this.decf);
    }

    abstract public String getMensaName();

    public Mensa getCurrentMenues() throws Exception {
        Mensa mensa = new Mensa(this.getMensaName());

        XPathFactory xpathFac = XPathFactory.newInstance();
        XPath xpath = xpathFac.newXPath();
        URL url = new URL(theURL);
        InputStream input = url.openStream();

        XMLReader reader = new Parser();
        reader.setFeature(Parser.namespacesFeature, false);
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();

        DOMResult domResult = new DOMResult();
        transformer.transform(new SAXSource(reader, new InputSource(input)),
                domResult);

        // There is only one table on the pages so we just check for the tr's
        String QUERY = "//tr";
        Node rootNode = domResult.getNode();
        NodeList qResult = (NodeList) xpath.evaluate(QUERY, rootNode,
                XPathConstants.NODESET);

        // leave it all behind
        if (qResult.getLength() == 0) {
            throw new NullPointerException(
                    "xpath query returned no nodes! please check xpath query!");
        }

        Date lastDate = null;
        for (int n = 0; n < qResult.getLength(); n++) {

            // The structure of the mensa pages contains one table with all
            // meals. This table contains a lot of tr's where every tr with a
            // meal contains 5 td's where each one contains a div with the
            // real information. So the first check is if the td has got 5 children and if
            // those also have at least one child.
            final NodeList currentTd = qResult.item(n).getChildNodes();
            if (currentTd.getLength() != 5
                    || !currentTd.item(1).hasChildNodes()) {
                lastDate = null;
                continue;
            }

            // Get prices if it's no day off
            final Node descTd = currentTd.item(1);
            final String desc = descTd.getFirstChild().getTextContent();
            if (desc.toLowerCase().contains("feiertag")) {
                lastDate = null;
                continue;
            }

            final Node sPriceTd = descTd.getNextSibling();
            final Node bPriceTd = sPriceTd.getNextSibling();
            final String sPrice = sPriceTd.getFirstChild().getTextContent();
            final String bPrice = bPriceTd.getFirstChild().getTextContent();
            if (!sPrice.matches(priceRegex) || !bPrice.matches(priceRegex)) {
                lastDate = null;
                continue;
            }

            /*
                * Get the date. Every
                * day has two or three meals and depending on the number of meals
                * there are two ways to get the date: If there are two meals, the
                * date is in the td before the desc and if there are three meals,
                * the date will be in the td of the desc of the second meal. For
                * all the other meals up to the next td without content we set the
                * calcuted date. The algorithm restarts if a break between two days
                * is recognized (look at all those continues ;) ). That's why we
                * set the lastDate to null in those cases
                */
            final Date date;
            if (lastDate != null) {
                date = lastDate;
            } else {
                final Node mayDateTd = currentTd.item(0);
                if (mayDateTd.getFirstChild() == null) {
                    continue;
                }
                String dateContent = mayDateTd.getFirstChild().getTextContent();

                if (dateContent.isEmpty()) {
                    dateContent = qResult.item(n + 1).getFirstChild()
                            .getFirstChild().getTextContent();
                }
                // In one of the mensa pages they had daily
                // meals and a string in the td where actually the date should have been (and where we didn't expect the string), so
                // verify that it's indeed a date e.g. Mo 01.01.12
                if (!dateContent.matches("\\w\\w\\s\\d\\d\\.\\d\\d\\.\\d\\d")) {
                    continue;
                }
                date = getDateFromString(dateContent);
                lastDate = date;
            }

            // At least create the menu and start again
            Tagesmenue daymeal = mensa.getMenuForDate(date);
            if (daymeal == null) {
                daymeal = new Tagesmenue(date);
                mensa.addDayMenue(daymeal);
            }

            Integer bPriceInCents = null;
            Integer sPriceInCents = null;
            try {
                bPriceInCents = getPriceInCents(bPrice);
                if (bPriceInCents == null)
                    throw new Exception("return value was null");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, this.getMensaName() + ", " + date
                        + ": converting bed. price '" + bPrice
                        + "' to cents failed", ex);
            }
            try {
                sPriceInCents = getPriceInCents(sPrice);
                if (sPriceInCents == null)
                    throw new Exception("return value was null");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, this.getMensaName() + ", " + date
                        + ": converting student price '" + sPrice
                        + "' to cents failed", ex);
            }

            if (bPriceInCents == null || sPriceInCents == null)
                continue;

            Mensaessen me = new Mensaessen(desc, bPriceInCents, sPriceInCents);
            daymeal.addMenu(me);
        }

        return mensa;
    }

    protected Integer getPriceInCents(String s) throws Exception {
        if (s == null || "".equals(s.trim())){
            return null;
        }
        Pattern pricePattern = Pattern.compile(priceRegex);
        Matcher m = pricePattern.matcher(s);
        if (!m.find()) {
            logger.log(Level.WARNING, this.getMensaName()
                    + ": Price format did not match regular expression");
            return null;
        }
        String tmp = m.group();
        return new Double(df.parse(tmp.substring(0, tmp.indexOf(',') + 2))
                .doubleValue() * 100).intValue();
    }

    protected Date getDateFromString(String d) throws Exception {
        if (d == null || "".equals(d.trim()) || d.length() < 8)
            return null;
        Date date = sdf.parse(d.substring(3));
        return date;
    }
}
