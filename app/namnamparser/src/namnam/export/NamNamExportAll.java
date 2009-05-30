package namnam.export;

import java.util.logging.Level;
import java.util.logging.Logger;
import namnam.export.ical.NamNamICALExporter;
import namnam.export.json.NamNamJSONExporter;
import namnam.export.jxml.NamNamJXMLExporter;
import namnam.export.xml.NamNamXMLExporter;
import namnam.model.Mensa;
import namnam.parser.NamNamParser;
import namnam.parser.erlangennuernberg.NamNamParserEI;
import namnam.parser.erlangennuernberg.NamNamParserIN;

/**
 * helper class exporting all mensae known to all supported formats.
 * @author fake
 */
public class NamNamExportAll {

    private static Logger logger = Logger.getLogger(NamNamExportAll.class.getName());

    public static void main(String[] args) {

        NamNamParser inParser = new NamNamParserIN();
        NamNamParser eiParser = new NamNamParserEI();

        NamNamExporter nnjex = new NamNamJXMLExporter();
        NamNamExporter nnjsonex = new NamNamJSONExporter();
        NamNamExporter nnxmlex = new NamNamXMLExporter();
        NamNamExporter nnicalex = new NamNamICALExporter();

        Mensa in = null;
        try {
            in = inParser.getCurrentMenues();
            nnjex.export(in);
            nnjsonex.export(in);
            nnxmlex.export(in);
            nnicalex.export(in);
        } catch (NamNamExportException nneex) {
            logger.log(Level.SEVERE,"Error exporting ingolstadt menues!",nneex);
        } catch (Exception ex) {
            logger.log(Level.SEVERE,"Error fetching ingolstadt menues!",ex);
        }

        Mensa ei = null;
        try {
            ei = inParser.getCurrentMenues();
            nnjex.export(ei);
            nnjsonex.export(ei);
            nnxmlex.export(ei);
            nnicalex.export(ei);
        } catch (NamNamExportException nneex) {
            logger.log(Level.SEVERE,"Error exporting eichstaett menues!",nneex);
        } catch (Exception ex) {
            logger.log(Level.SEVERE,"Error fetching eichstaett menues!",ex);
        }


    }
}
