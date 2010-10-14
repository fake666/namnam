
package org.bytewerk.namnam.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

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
 */
public class Gui extends JFrame
	implements ActionListener
{

	public Gui(Mensa m)
	{
	
		this.setLayout(new BorderLayout());
		this.m = m;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("namnam - immer hungrig");
		
		
		c = Calendar.getInstance();
		heute = new Date(c.get(1) - 1900, c.get(2), c.get(5), 0, 0, 0);
		
		outputGrid = new JTable(new NamnamTableModel(m, heute));
		outputGrid.setBackground(hellrosa);
		outputGrid.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		outputGrid.setAutoCreateRowSorter(true);
		outputGrid.getTableHeader().setBackground(dunkelrosa);
		
		datechooser = new JDateChooser(heute);
		datechooser.setDateFormatString("dd.MMMMM yyyy");
		datechooser.setMinSelectableDate(m.getFirstDate());
		datechooser.setMaxSelectableDate(m.getLastDate());
		
		JButton load = new JButton("Men\374 Laden");
		load.setBackground(dunkelrosa);
		load.addActionListener(this);
		load.setActionCommand("load");
		
		JLabel header = new JLabel("Men\374 des Tages f\374r ");
	
		JPanel oben = new JPanel();
		oben.setBackground(dunkeldunkelrosa);
		oben.add(header);
		oben.add(datechooser);
		oben.add(load);
		
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
		if(e.getActionCommand().equals("load")) 
		{
			outputGrid.setModel(new NamnamTableModel(this.m, this.datechooser.getDate()));
		}
		
	}

	private static final long serialVersionUID = 0L;
	private Calendar c;
	private JTable outputGrid;
	private Date heute;
	private JDateChooser datechooser;
	private Mensa m;
	private JPanel essensanzeige;
	private final Color dunkelrosa = new Color(255, 139, 240);
	private final Color hellrosa = new Color(255, 196, 247);
	private final Color dunkeldunkelrosa = new Color(255, 0 , 222);
}
