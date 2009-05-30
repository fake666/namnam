package namnam.importer.jxml;

import java.beans.XMLDecoder;
import java.io.InputStream;
import namnam.importer.NamNamImportException;
import namnam.importer.NamNamImporter;
import namnam.model.Mensa;

/**
 * load a mensa object saved as java xml serialisation
 * @author fake
 */
public class NamNamJXMLImporter extends NamNamImporter {

    public Mensa load(InputStream xmlStream) throws NamNamImportException {
        Mensa ret = null;
        try {
            XMLDecoder xdec = new XMLDecoder(xmlStream);
            ret = (Mensa)xdec.readObject();
            xdec.close();
        } catch (Exception ex) {
           throw new NamNamImportException("Error while importing namnam jxml stream",ex);
        }
       return ret;
    }

}
