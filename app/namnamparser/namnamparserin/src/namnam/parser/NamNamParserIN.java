package namnam.parser;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.ccil.cowan.tagsoup.Parser;

import namnam.parser.util.MutableNamespaceContext;
import org.w3c.dom.NodeList;

/**
 *
 * @author fake
 */
public class NamNamParserIN implements NamNamParser {

    private URL myURL;

    public NamNamParserIN() {
        try {
            myURL = new URL("http://www.studentenwerk.uni-erlangen.de/verpflegung/de/sp-ingolstadt.shtml");
        } catch (MalformedURLException mfe) {
            // can not happen, url is fixed!
        }
    }

    public static void main(String[] args) {
        NamNamParser foo = new NamNamParserIN();
        try {
            foo.getCurrentMenues();
        } catch (Exception ex) {
            System.err.println("Exception: " + ex);
        }
    }

    public Map<Date,Map<String,Integer[]>> getCurrentMenues() throws Exception {
        HashMap<Date,Map<String,Integer[]>> ret = new HashMap<Date,Map<String,Integer[]>>();

        Node node = getHtmlUrlNode(myURL);
        MutableNamespaceContext nc = new MutableNamespaceContext();
        nc.setNamespace("html", "http://www.w3.org/1999/xhtml");
        
        String QUERY = "//html:div[@id='content']/html:div[1]/html:table";

        // leave it all behind
        Node table = xPathQuery(node, QUERY, nc).item(0).cloneNode(true);

        QUERY = "//*[contains(html:div,'Essen')]";
        NodeList anchors = xPathQuery(table, QUERY, nc);
        for(int n = 0; n < anchors.getLength(); n++) {
            Node td = anchors.item(n);
            String idx = td.getChildNodes().item(0).getTextContent();
            Node descTd = td.getNextSibling();
            String desc = descTd.getChildNodes().item(0).getTextContent();
            Node sPriceTd = descTd.getNextSibling();
            String sPrice_string = sPriceTd.getChildNodes().item(0).getTextContent();
            Node bPriceTd = sPriceTd.getNextSibling();
            String bPrice_string = bPriceTd.getChildNodes().item(0).getTextContent();

            // that was easy. the tricky part is to find the date!
            String date = "";
            if(idx.endsWith("1")) { // Esen 1: go down
                date = td.getParentNode().getNextSibling().
                        getChildNodes().item(0).getChildNodes().item(0).getTextContent();
            } else if(idx.endsWith("2")) { // Essen 2: right here
                date = td.getPreviousSibling().
                        getChildNodes().item(0).getTextContent();
            } else if(idx.endsWith("3")) { // Essen 3: go up
                date = td.getParentNode().getPreviousSibling().
                        getChildNodes().item(0).getChildNodes().item(0).getTextContent();
            } else {
                System.err.println("Warning: unknown index skipped! " + idx);
                continue;
            }

            String parseDate = date.substring(3);
            parseDate = parseDate.substring(0,parseDate.lastIndexOf('.')+1)
                    + "20"
                    + parseDate.substring(parseDate.lastIndexOf('.')+1,parseDate.length());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date d = sdf.parse(parseDate);

            // now parse the prices
            Integer sPrice = null, bPrice = null;
            if(sPrice_string != null && !"".equals(sPrice_string.trim())) {
                int cIdx = sPrice_string.indexOf(',');
                sPrice = Integer.parseInt(
                        sPrice_string.substring(0,cIdx)
                        .concat(
                        sPrice_string.substring(cIdx+1,cIdx+3))
                );
            }
            if(bPrice_string != null && !"".equals(bPrice_string.trim())) {
                int cIdx = bPrice_string.indexOf(',');
                bPrice = Integer.parseInt(
                        bPrice_string.substring(0,cIdx)
                        .concat(
                        bPrice_string.substring(cIdx+1,cIdx+3))
                );
            }

            Map<String,Integer[]> mealMap = ret.get(d);
            if(mealMap == null) {
                mealMap = new HashMap<String,Integer[]>();
                ret.put(d,mealMap);
            }
            mealMap.put(desc,new Integer[]{sPrice,bPrice});
        }

        return ret;
    }

  /**
   * @param urlString The URL of the page to retrieve
   * @return A Node with a well formed XML doc coerced from the page.
   * @throws Exception if something goes wrong. No error handling at all
   * for brevity.
   */
  public Node getHtmlUrlNode(URL url) throws Exception {

    SAXTransformerFactory stf =
      (SAXTransformerFactory) TransformerFactory.newInstance();
    TransformerHandler th = stf.newTransformerHandler();

    // This dom result will contain the results of the transformation
    DOMResult dr = new DOMResult();
    th.setResult(dr);

    Parser parser = new Parser();
    parser.setContentHandler(th);

    URLConnection urlConn = url.openConnection();
    InputStream stream = urlConn.getInputStream();

    // This is where the magic happens to convert HTML to XML
    parser.parse(new InputSource(stream));
    
    return dr.getNode();
  }

  public NodeList xPathQuery(Node node, String query, NamespaceContext nc) throws Exception {
    XPathFactory xpf = XPathFactory.newInstance();
    XPath xpath = xpf.newXPath();
    xpath.setNamespaceContext(nc);
    return (NodeList) xpath.evaluate(query, node, XPathConstants.NODESET);
  }

  public String dumpNode(Node node, boolean omitDeclaration)
    throws Exception {
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    if (omitDeclaration) {
      xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    }
    StringWriter sw = new StringWriter();
    Result result = new StreamResult(sw);
    Source source = new DOMSource(node);
    xformer.transform(source, result);
    return sw.toString();
  }
}
