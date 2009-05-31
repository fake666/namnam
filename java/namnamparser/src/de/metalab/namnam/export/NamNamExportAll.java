package de.metalab.namnam.export;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.metalab.namnam.export.ical.NamNamICALExporter;
import de.metalab.namnam.export.json.NamNamJSONExporter;
import de.metalab.namnam.export.jxml.NamNamJXMLExporter;
import de.metalab.namnam.export.xml.NamNamXMLExporter;
import de.metalab.namnam.model.Mensa;
import de.metalab.namnam.parser.NamNamParser;
import de.metalab.namnam.parser.erlangennuernberg.*;

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

	List<NamNamParser> np = new ArrayList<NamNamParser>();
        np.add(new NamNamParserIN());
        np.add(new NamNamParserEI());
	np.add(new NamNamParserAnsbach());
	np.add(new NamNamParserLangemarckplatzER());
	np.add(new NamNamParserMensateriaN());
	np.add(new NamNamParserRegensburgerStrN());
	np.add(new NamNamParserSchuettN());
	np.add(new NamNamParserSuedMensaER());

        NamNamExporter nnjex = new NamNamJXMLExporter(baseDir);
        NamNamExporter nnjsonex = new NamNamJSONExporter(baseDir);
        NamNamExporter nnxmlex = new NamNamXMLExporter(baseDir);
        NamNamExporter nnicalex = new NamNamICALExporter(baseDir);

	Iterator<NamNamParser> npIt = np.iterator();
	while(npIt.hasNext()) {
		NamNamParser cur = npIt.next();
		try {
		    Mensa m = cur.getCurrentMenues();
		    nnjex.export(m);
		    nnjsonex.export(m);
		    nnxmlex.export(m);
		    nnicalex.export(m);
		} catch (NamNamExportException nneex) {
		    logger.log(Level.SEVERE,"Error exporting: " + cur.getMensaName(), nneex);
		} catch (Exception ex) {
		    logger.log(Level.SEVERE,"Error fetching: " + cur.getMensaName(), ex);
		}
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
