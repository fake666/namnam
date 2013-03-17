
package org.bytewerk.namnam.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.bytewerk.namnam.model.Mensa;

import com.toedter.calendar.JDateChooser;

/**
 * swing gui dialog
 * @author chiffre, invalid-id
 * TODO Spalte mit Speise soll sich automatisch anpassen
 * TODO vor und zurück Buttons
 */
public class Gui extends JFrame
	implements ActionListener
{
	
	/* 
	 * Gibt die passende Größe der Spalte "Speise" in Pixel zurück.
	 */
	private int getSize4SpeiseColumn(String cont_col1, 
								 	String cont_col2,
								 	String cont_col3) {
		
		/* Es wird die Länge der längsten Zeichenkette gesucht. 
		 * Diese wird duch Sortieren einer Liste der Längen erreicht. 
		 * Anschließend wird das letzte Element (also die größte Länge) verwendet.
		 */
	
		List<Integer> lst = new LinkedList<Integer>();
		lst.add(cont_col1.length());
		lst.add(cont_col2.length());
		lst.add(cont_col3.length());
		Collections.sort(lst);
//		for(Integer i: lst) {
//			System.out.println(i+"");
//		}
//		System.out.println("");
		
		return lst.get(lst.size()-1) * 6 + 30;
	}
	
	public Gui(Mensa m)
	{
	
		int lenSpeiseSpalte = 0;
		this.setLayout(new BorderLayout());
		this.m = m;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("namnam - immer hungrig");
		this.setResizable(false);
		
		
		c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		heute = c.getTime();
		
		outputGrid = new JTable(new NamnamTableModel(m, heute));
		outputGrid.setBackground(hellrosa);
		outputGrid.setShowGrid(false);
		outputGrid.setShowVerticalLines(true);
		lenSpeiseSpalte = this.getSize4SpeiseColumn((String) outputGrid.getModel().getValueAt(0, 0),
													(String) outputGrid.getModel().getValueAt(1, 0),
													(String) outputGrid.getModel().getValueAt(2, 0));
		outputGrid.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(lenSpeiseSpalte);
		
		outputGrid.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); 
		outputGrid.setAutoCreateRowSorter(true);
		
		outputGrid.getTableHeader().setBackground(dunkelrosa);
		
		datechooser = new JDateChooser(heute);
		datechooser.setDateFormatString("dd.MMMMM yyyy");
		datechooser.setMinSelectableDate(m.getFirstDate());
		datechooser.setMaxSelectableDate(m.getLastDate());

		/* Dies sind Buttons für nen Tag vor, zurück und für den akt. Tag.
		 * Sie ersetzen den Button "Menü laden" im sichtbaren Bereich. Aus
		 * Stilgründen wird der Button noch verwendet, siehe dafür actionPerformed().
		 * Wichtig: alle ActionCommand für gestern, heute und morgen
		 * 			müssen aufgrund der actionPerformed auf "Tag" enden
		 */
		JButton gestrTag = new JButton("zurück");
		gestrTag.setBackground(dunkelrosa);
		gestrTag.addActionListener(this);
		gestrTag.setActionCommand("gestriger Tag");

		
		JButton aktTag = new JButton("heute");
		aktTag.setBackground(dunkelrosa);
		aktTag.addActionListener(this);
		aktTag.setActionCommand("heutiger Tag");
		
		JButton naechstTag = new JButton("vor");
		naechstTag.setBackground(dunkelrosa);
		naechstTag.addActionListener(this);
		naechstTag.setActionCommand("naechster Tag");
		
		/* unsichtbar */
		load = new JButton("Men\374 Laden");
		load.addActionListener(this);
		load.setActionCommand("load");
		
		JLabel header = new JLabel("Men\374 des Tages f\374r ");
	
		JPanel oben = new JPanel();
		oben.setBackground(dunkeldunkelrosa);
		oben.add(header);
		oben.add(datechooser);
		oben.add(gestrTag);
		oben.add(aktTag);
		oben.add(naechstTag);
		
		JPanel essensanzeige = new JPanel();
		essensanzeige.setLayout(new BorderLayout());
		essensanzeige.add(outputGrid.getTableHeader(), BorderLayout.PAGE_START);
		essensanzeige.add(outputGrid);
		
		this.add(oben, BorderLayout.NORTH);
		this.add(essensanzeige, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
		/* Positioniere das Fenster in der Mitte des Bildschirms */
		Dimension scrsize = this.getToolkit().getScreenSize();
		this.setLocation(
				(scrsize.width - this.getWidth()) / 2,
				(scrsize.height - this.getHeight()) / 2);
					
	}

	public void actionPerformed(ActionEvent e)
	{
		String acmd = e.getActionCommand();
		
		/* 
		 * das ActionCommand "load" wird nur von den unteren actionCommands ausgelöst
		 */
		if(acmd.equals("load")) 
		{
			int lenSpalteSpeise = 0;
			outputGrid.setModel(new NamnamTableModel(this.m, this.datechooser.getDate()));
			lenSpalteSpeise = this.getSize4SpeiseColumn((String)outputGrid.getModel().getValueAt(0,0),
														(String)outputGrid.getModel().getValueAt(1,0),
														(String)outputGrid.getModel().getValueAt(2,0));
			outputGrid.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(lenSpalteSpeise);
		} 
		else if(acmd.endsWith("Tag")) 
		{
			/* sobald dieser Code ausgeführt wird, will der Benutzer entweder
			 * im Datum vor, zurück schalten oder das aktuelle Datum anzeigen.
			 */
			c.setTime(datechooser.getDate());
			if(acmd.startsWith("naechster")) 
			{
				/* eins dazu addieren */
				c.add(Calendar.DAY_OF_YEAR, 1);
			} 
			else if(acmd.startsWith("gestriger"))
			{
				/* eins abziehen */
				c.add(Calendar.DAY_OF_YEAR, -1);
			}
			else if(acmd.startsWith("heutiger")) 
			{
				/* aktuelles Datum */
				c = Calendar.getInstance();
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
			}
			this.datechooser.setDate(c.getTime());
			
			/* Menü der Mensa für vorher aufbereitetes Datum laden */
			load.doClick();

		}
		
	}

	private JButton load;
	private static final long serialVersionUID = 0L;
	private Calendar c;
	private JTable outputGrid;
	private Date heute;
	private JDateChooser datechooser;
	private Mensa m;
	private final Color dunkelrosa = new Color(255, 139, 240);
	private final Color hellrosa = new Color(255, 196, 247);
	private final Color dunkeldunkelrosa = new Color(255, 0 , 222);
}
