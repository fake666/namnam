package namnam.export;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import namnam.model.Mensa;
import namnam.parser.NamNamParser;
import namnam.parser.erlangennuernberg.NamNamParserEI;
import namnam.parser.erlangennuernberg.NamNamParserIN;

/**
 *
 * @author fake
 */
public class NamNamExporter {

    public static void main(String[] args) {

        NamNamParser inParser = new NamNamParserIN();
        NamNamParser eiParser = new NamNamParserEI();

        try {
            Mensa in = inParser.getCurrentMenues();

            FileOutputStream fos = new FileOutputStream(in.getName()+".xml");
            XMLEncoder xenc = new XMLEncoder(fos);
            xenc.writeObject(in);
            xenc.close();
            fos.close();

        } catch (Exception ex) {
            System.err.println("Error fetching ingolstadt menues!");
            ex.printStackTrace(System.err);
        }

        try {
            Mensa ei = eiParser.getCurrentMenues();

            FileOutputStream fos = new FileOutputStream(ei.getName()+".xml");
            XMLEncoder xenc = new XMLEncoder(fos);
            xenc.writeObject(ei);
            xenc.close();
            fos.close();

        } catch (Exception ex) {
            System.err.println("Error fetching eichstaett menues!");
            ex.printStackTrace(System.err);
        }



    }

}
