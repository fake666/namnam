package namnam.export.jxml;

import java.beans.XMLEncoder;
import java.io.OutputStream;
import namnam.export.NamNamExportException;
import namnam.export.NamNamExporter;

/**
 * module to export a mensa object to java serialised xml
 * @author fake
 */
public class NamNamJXMLExporter extends NamNamExporter {

    protected void doExport(OutputStream os) throws NamNamExportException{
        XMLEncoder xenc = new XMLEncoder(os);
        xenc.writeObject(mensa);
        xenc.close();
    }

    @Override
    public String getFileName() {
        return mensa.getName()+".jxml";
    }

}
