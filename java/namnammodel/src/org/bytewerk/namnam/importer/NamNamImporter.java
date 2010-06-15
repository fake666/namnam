package org.bytewerk.namnam.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.bytewerk.namnam.model.Mensa;

/**
 * abstrakte basisklasse fuer alle java-basierten namnam-importer.
 * 
 * @author fake
 */
public abstract class NamNamImporter {

    /**
     * muss von implementierenden klassen ueberschrieben werden, enthaelt den
     * eigentlichen loading-code
     *
     * @param xmlStream der inputstream, den wir unten noch schoen vorbereiten
     * @return eine Mensa-instanz
     * @throws de.metalab.namnam.importer.NamNamImportException wenn was schiefgeht ;-)
     */
    public abstract Mensa load(InputStream xmlStream) throws NamNamImportException;

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
     * @throws de.metalab.namnam.importer.NamNamImportException wenn was schiefgeht (url nicht vorhanden/erreichbar/etc)
     */
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

    /**
     * laedt eine mensa-instanz von einem lokalen file, zu benutzen genu wie loadFromURL
     * @param xmlFile das file, dass geladen werden soll
     * @return die Mensa-instanz, die in dem file schlummerte
     * @throws de.metalab.namnam.importer.NamNamImportException wenn was schief geht (file not found, etc)
     */
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
