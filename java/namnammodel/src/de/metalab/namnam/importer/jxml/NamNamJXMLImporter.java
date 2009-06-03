package de.metalab.namnam.importer.jxml;

import java.beans.XMLDecoder;
import java.io.InputStream;
import de.metalab.namnam.importer.NamNamImportException;
import de.metalab.namnam.importer.NamNamImporter;
import de.metalab.namnam.model.Mensa;

/**
 * diese klasse erweitert die namnamimporter klasse und implementiert
 * die moeglichkeit, einen inputstream, der als xml serialisierte java-
 * objekte enthaelt, wieder zu deserialiseren.
 *
 * @author fake
 */
public class NamNamJXMLImporter extends NamNamImporter {

    /**
     * diese methode erlaubt es einen inputStream anzugeben, der eine als java xml serialisierte
     * Mensa-instanz enthaelt.
     *
     * @param xmlStream der InputStream
     * @return das Mensa-Objekt das in diesem Stream schlummerte
     * @throws de.metalab.namnam.importer.NamNamImportException wenn ewas schiefgeht ;-)
     */
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
