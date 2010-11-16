package org.bytewerk.namnam.gui;

import java.util.Date;
import javax.swing.table.AbstractTableModel;
import org.bytewerk.namnam.model.Mensa;
import org.bytewerk.namnam.model.Mensaessen;

/*
 * Das NamnamTableModel ist dafür zuständig, dass die JTable im Hauptfenster
 * mit Daten gefüllt wird. Es wird daher beim Initialisieren der JTable eine neue
 * Instanz eines NamnamTables erstellt, mit der Mensa und einem Datum als Parameter.
 * Sobald der Benutzer ein neues Datum ausgewählt hat, muss dafür ein neues 
 * NamnamTableModel erstellt werden, da das aktuelle NNTM(NamnamTableModel) die
 * Daten für des vorherige Datum enthält. Beim Erstellen eines neuen NNTM wird
 * das aktuelle NNTM aufgrund des Garbage Collectors von Java aus dem Speicher
 * wegen Nichtbenutzung entfernt.
 *  
 */
public class NamnamTableModel extends AbstractTableModel {
	private String[][] tabledata;
	private String[] spalten = { "Speise", "Preis (Stud/Mitarb)", "Anm." };
	private static final long serialVersionUID = 0L;
	
	/* Sobald auf "Menü laden" geklickt wurde, wird ein neues 
	 * NamnamTableModel erstellt und damit neue Daten abgefragt.
	 */
	public NamnamTableModel(Mensa m, Date c) {
		this.tabledata = (String[][]) this.tagesMenu(m, c); 
		
	}
	
	/* Hilsmethode, die mithilfe der NamnamModel-API die Daten 
	 * aus dem Internet erfragt und in einem zweidimensionalen
	 * Array speichert.
	 */
	private Object[][] tagesMenu(Mensa m, Date d)
	{
		String[][] tbldata = new String[3][3];
		if(m.hasMenuForDate(d))
		{
			java.util.List<Mensaessen> ms = m.getMenuForDate(d).getMenues();
			for(int i=0; i < ms.size(); i++)
			{
				Mensaessen essen = ms.get(i);
				tbldata[i] = new String[3];
				tbldata[i][0] = new String(essen.getBeschreibung());
				tbldata[i][1] = new String((new StringBuilder(String.valueOf(this.getStudentenPreis(essen)))).append(" \u20AC / ").append(this.getPreis(essen)).append(" \u20AC").toString());
				
				/* Füge Anmerkung für vegetarisch, ohne Schweinefleisch bzw. mit Rindfleisch dazu */
				if(essen.isBeef()) {
					tbldata[i][2] = "mit lecker Rind!";
				} else if(essen.isMoslem()) {
					tbldata[i][2] =	"ohne Schweinefleisch"; 
				} else if(essen.isVegetarian()) {
					tbldata[i][2] = "Vegetarisch";
				} else {
					tbldata[i][2] = "";
				}
			}

		} else
		{
			/* Der Benutzer soll die Fehlermeldung in der Speisespalte angezeigt
			 * bekommen.
			 */
			tbldata[0] = new String[3];
			tbldata[0][0] = "";
			tbldata[0][1] = "Kein Menü verfügbar!";
			tbldata[0][2] = "";
			/* Da keine Menüs verfügbar sind, werden die restlichen Zellen mit
			 * Leerstrings beschrieben
			 */
			for(int i = 1; i < 3; i++) {
				for(int j = 0;j < 3;j++) {
					tbldata[i][j] = "";
				}
			}
			//new Alert();
		}
		return tbldata;
	}
	
	/*
	 * Ermittelt, wie viel ein Essen für Studenten kostet.  
	 */
	private String getStudentenPreis(Mensaessen essen)
	{
		return essen.getStudentenPreis().toString();
	}

	/*
	 * Ermittelt, wie viel ein Essen für Mitarbeiter kostet.
	 */
	private String getPreis(Mensaessen essen)
	{
		return essen.getPreis().toString();
	}
	
	/*
	 * Aufgerufen von Java, gibt die Anzahl der Spalten zurück.
	 */
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.spalten.length;
	}

	/*
	 * Aufgerufen von Java, gibt die Anzahl der Zeilen zurück.
	 */
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.tabledata.length;
	}

	/*
	 * Aufgerufen von Java, gibt den Wert einer Zelle zurück.
	 */
	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		return tabledata[row][col];
	}

	/*
	 * Aufgerufen von Java, gibt den Titel einer Spalten zurück.
	 */
	public String getColumnName(int col) {
		return spalten[col];
	}
	
	/*
	 * Aufgerufen von Java, legt fest, ob eine Zelle editierbar ist.
	 */
	public boolean isEditable(int row, int col) {
		return false;
	}
}
