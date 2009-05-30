package de.metalab.namnam.export;

import java.util.logging.Level;
import java.util.logging.Logger;
import de.metalab.namnam.export.ical.NamNamICALExporter;
import de.metalab.namnam.export.json.NamNamJSONExporter;
import de.metalab.namnam.export.jxml.NamNamJXMLExporter;
import de.metalab.namnam.export.xml.NamNamXMLExporter;
import de.metalab.namnam.model.Mensa;
import de.metalab.namnam.parser.NamNamParser;
import de.metalab.namnam.parser.erlangennuernberg.NamNamParserEI;
import de.metalab.namnam.parser.erlangennuernberg.NamNamParserIN;

/**
 * helper class exporting all mensae known to all supported formats.
 * @author fake
 */
public class NamNamExportAll {

    private static Logger logger = Logger.getLogger(NamNamExportAll.class.getName());

    public static void main(String[] args) {

        if(args.length < 1) {
            usage();
            return;
        }

        String baseDir = null;

        String arg = null;
        for(int i = 0; i < args.length; i++) {
            arg = args[i];
            if(arg.equals("--help")) {
				usage();
				return;
            } else if (arg.startsWith("--basedir=")) {
                baseDir = arg.substring(arg.indexOf('=')+1,arg.length());
            }
        }

        if(baseDir == null) {
            usage();
            return;
        }

        NamNamParser inParser = new NamNamParserIN();
        NamNamParser eiParser = new NamNamParserEI();

        NamNamExporter nnjex = new NamNamJXMLExporter(baseDir);
        NamNamExporter nnjsonex = new NamNamJSONExporter(baseDir);
        NamNamExporter nnxmlex = new NamNamXMLExporter(baseDir);
        NamNamExporter nnicalex = new NamNamICALExporter(baseDir);

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
            ei = eiParser.getCurrentMenues();
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

    public static void usage() {
        System.out.println( "NamNamParser - (C) 2009 Thomas 'fake' Jakobi");
        System.out.println( "A meta-lab.de project");
		System.out.println();
		System.out.println("Usage:");
		System.out.println();
		System.out.println("java -jar namnamparser.jar --basedir=<directory> ");
		System.out.println();
		System.out.println("Options:");
		System.out.println(" --help         This help text");
		System.out.println();
		System.out.println("Examples:");
		System.out.println(" java -jar namnamparser.jar --basedir=/tmp");
		System.out.println();
    }
}
