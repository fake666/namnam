package namnam.importer;

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
public abstract class NamNamImporter {

    public abstract Mensa load(InputStream xmlStream) throws NamNamImportException;

    public Mensa loadFromURL(URL theURL) throws NamNamImportException {
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
        return load(ins);
    }

    public Mensa loadFromFile(File xmlFile) throws NamNamImportException {
        Mensa ret = null;
        try {
            ret = load((InputStream)new FileInputStream(xmlFile));
        } catch (FileNotFoundException fnfe) {
            throw new NamNamImportException("File not found while loading namnam menues from file",fnfe);
        }
        return ret;
    }


}
