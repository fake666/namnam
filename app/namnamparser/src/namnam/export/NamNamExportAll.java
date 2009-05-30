package namnam.export;

import namnam.export.json.NamNamJSONExporter;
import namnam.export.jxml.NamNamJXMLExporter;
import namnam.model.Mensa;
import namnam.parser.NamNamParser;
import namnam.parser.erlangennuernberg.NamNamParserEI;
import namnam.parser.erlangennuernberg.NamNamParserIN;

/**
 * helper class exporting all mensae known to all supported formats.
 * @author fake
 */
public class NamNamExportAll {

    public static void main(String[] args) {

        NamNamParser inParser = new NamNamParserIN();
        NamNamParser eiParser = new NamNamParserEI();

        NamNamExporter nnjex = new NamNamJXMLExporter();
        NamNamExporter nnjsonex = new NamNamJSONExporter();

        try {
            Mensa in = inParser.getCurrentMenues();
            nnjex.export(in);
            nnjsonex.export(in);
        } catch (Exception ex) {
            System.err.println("Error fetching ingolstadt menues!");
            ex.printStackTrace(System.err);
        }

        try {
            Mensa ei = eiParser.getCurrentMenues();
            nnjex.export(ei);
            nnjsonex.export(ei);
        } catch (Exception ex) {
            System.err.println("Error fetching eichstaett menues!");
            ex.printStackTrace(System.err);
        }



    }
}
