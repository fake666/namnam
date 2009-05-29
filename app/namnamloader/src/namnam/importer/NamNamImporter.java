package namnam.importer;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import namnam.model.Mensa;

/**
 * read in nam nam xml file and deserialize it
 * @author fake
 */
public class NamNamImporter {

    public static Mensa loadMensa(URL theURL) throws NamNamImportException {
        URLConnection con = null;
        try {
            con = theURL.openConnection();
        } catch (IOException ioex) {
            throw new NamNamImportException("IO Error while opening URL connection",ioex);
        }
        InputStream ins = null;
        try {
            ins = con.getInputStream();
        } catch (IOException ioex) {
            throw new NamNamImportException("IO Error while opening http input stream",ioex);
        }
        return loadMensa(ins);
    }

    public static Mensa loadMensa(File xmlFile) throws NamNamImportException {
        Mensa ret = null;
        try {
            ret = loadMensa((InputStream)new FileInputStream(xmlFile));
        } catch (FileNotFoundException fnfe) {
            throw new NamNamImportException("File not found while loading namnam menues from file",fnfe);
        }
        return ret;
    }

    public static Mensa loadMensa(InputStream xmlStream) throws NamNamImportException {
        Mensa ret = null;
        try {
            XMLDecoder xdec = new XMLDecoder(xmlStream);
            ret = (Mensa)xdec.readObject();
            xdec.close();
        } catch (Exception ex) {
           throw new NamNamImportException("Error while importing namnam xml stream",ex);
        }
       return ret;
    }

}
