
package org.bytewerk.namnam.gui;

import java.net.URL;

import javax.swing.JOptionPane;

import org.bytewerk.namnam.importer.NamNamImporter;
import org.bytewerk.namnam.importer.jxml.NamNamJXMLImporter;
import org.bytewerk.namnam.model.Mensa;

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

			if(ex.getCause() instanceof java.net.UnknownHostException)
			{
				JOptionPane.showMessageDialog(null, 
						"Konnte keine Verindung zum Server\n'"+
						ex.getCause().getMessage() + "'\nim Internet aufbauen!", 
						"Fehler beim Verbinden",
						JOptionPane.ERROR_MESSAGE);	
			} 
			else 
			{
				JOptionPane.showMessageDialog(null,
						ex.getMessage(),
						"Fehler!",
						JOptionPane.ERROR_MESSAGE);
				
			}
			return;
		}
		new Gui(m);
	}


}
