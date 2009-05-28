package namnam.parser.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * xpath utility methods for all the parsers
 *
 * uses code fragments and the general approach for tag soup using xpath from
 * http://blog.oroup.com/2006/11/05/the-joys-of-screenscraping/
 * thanks to oliver roupe!
 *
 * i just switched to commons/httpclient to be able to change the content encoding
 * and handle weird servers more effectivley.
 * 
 * @author fake
 */
public class XPathUtil {

    /**
     * @param urlString The URL of the page to retrieve
     * @return A Node with a well formed XML doc coerced from the page.
     * @throws Exception if something goes wrong. No error handling at all
     * for brevity.
     */
    public static Node getHtmlUrlNode(String url, String encoding) throws Exception {

        SAXTransformerFactory stf =
                (SAXTransformerFactory) TransformerFactory.newInstance();
        TransformerHandler th = stf.newTransformerHandler();

        // This dom result will contain the results of the transformation
        DOMResult dr = new DOMResult();
        th.setResult(dr);

        Parser parser = new Parser();
        parser.setContentHandler(th);

        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);

        int statusCode = client.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
            return null;
        }
        
        //InputStream foo = method.getResponseBodyAsStream();
        // This is where the magic happens to convert HTML to XML
        InputSource src = new InputSource(method.getResponseBodyAsStream());
        src.setEncoding(encoding);
        parser.parse(src);

        method.releaseConnection();

        return dr.getNode();
    }

    /**
     * @param node An XML DOM Tree for query
     * @param query An XPATH query to run against the DOM Tree
     * @param nc The namespaceContext that maps prefixes to XML namespace
     * @return A list of nodes that result from running the query against
     * the node.
     * @throws Exception If anything goes wrong. No error handling for brevity
     */
    public static NodeList query(Node node, String query, NamespaceContext nc) throws Exception {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        xpath.setNamespaceContext(nc);
        return (NodeList) xpath.evaluate(query, node, XPathConstants.NODESET);
    }

    /**
     * a debug method
     * @param node A node to be dumped to a string
     * @param omitDeclaration A boolean whether to omit the XML declaration
     * @return A string representation of the node.
     * @throws Exception If anything goes wrong. Error handling omitted.
     */
    public static String dumpNode(Node node, boolean omitDeclaration)
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
