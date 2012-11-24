package org.bytewerk.namnam.android.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bytewerk.namnam.model.Cafeteria;

/**
 * abstrakte basisklasse fuer alle java-basierten namnam-importer.
 * 
 * @author fake
 */
public class NamNamXMLImporter {

    /**
     * muss von implementierenden klassen ueberschrieben werden, enthaelt den
     * eigentlichen loading-code
     *
     * @param xmlStream der inputstream, den wir unten noch schoen vorbereiten
     * @return eine Mensa-instanz
     * @throws org.bytewerk.namnam.importer.NamNamImportException wenn was schiefgeht ;-)
     */
    public Cafeteria load(InputStream xmlStream) {
        final Cafeteria result;
    	try{
        	final SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
        	
        	final NamNamXMLHandler handler = new NamNamXMLHandler();        	
			sax.parse(xmlStream, handler);
			result = handler.getCafeteria();
			
        }catch(Exception e){
        	throw new RuntimeException(e.getMessage(),e);
        }
       return result;
    }

    /**
     * laedt ein mensa-objekt unter angabe einer URL
     *
     * um z.b. das Mensa-Objekt der hochschule ingolstadt mittels jxml zu laden, ruft man einfach diese
     * Methode mit dem argument der mensa auf:
     *
     * Mensa in = myImportert.loadFromURL(new URL("http://namnam.bytewerk.org/files/Studiwerk-Erlangen-Nuernberg-Mensa-IN.jxml"));
     *
     * @param theURL die URL der zu importierenden mensa-instanz
     * @return die Mensa-instanz
     * @throws org.bytewerk.namnam.importer.NamNamImportException wenn was schiefgeht (url nicht vorhanden/erreichbar/etc)
     */
    public Cafeteria loadFromURL(URL theURL) {
        URLConnection con;
        InputStream ins;
        
        try {
            con = theURL.openConnection();
            ins = con.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        return load(ins);
    }

    /**
     * laedt eine mensa-instanz von einem lokalen file, zu benutzen genu wie loadFromURL
     * @param xmlFile das file, dass geladen werden soll
     * @return die Mensa-instanz, die in dem file schlummerte
     * @throws org.bytewerk.namnam.importer.NamNamImportException wenn was schief geht (file not found, etc)
     */
    public Cafeteria loadFromFile(File xmlFile) {
        Cafeteria ret;
        try {
            ret = load((InputStream)new FileInputStream(xmlFile));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        return ret;
    }


}
