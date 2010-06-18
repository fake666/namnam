
package org.bytewerk.namnam.gui;

import org.bytewerk.namnam.importer.NamNamImporter;
import org.bytewerk.namnam.importer.jxml.NamNamJXMLImporter;
import org.bytewerk.namnam.model.Mensa;
import java.net.URL;

/**
 * basic namnam java gui base class
 * @author chiffre
 */
public class Main
{

	public static final String theUrl = "http://namnam.bytewerk.org/files/Studiwerk-Erlangen-Nuernberg-Mensa-IN.jxml";

	public Main()
	{
	}

	public static void main(String args[])
	{
		NamNamImporter jxmlImporter = new NamNamJXMLImporter();
		Mensa m;
		try
		{
			m = jxmlImporter.loadFromURL(new URL(theUrl));
		}
		catch(Exception ex)
		{
			System.err.println("autsch, da klappt was nicht!");
			ex.printStackTrace();
			return;
		}
		new Gui(m);
	}


}
