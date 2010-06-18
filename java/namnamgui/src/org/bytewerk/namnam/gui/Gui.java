
package org.bytewerk.namnam.gui;

import com.toedter.calendar.JDateChooser;
import org.bytewerk.namnam.model.Mensa;
import org.bytewerk.namnam.model.Mensaessen;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * swing gui dialog
 * @author chiffre
 */
public class Gui extends JFrame
	implements ActionListener
{

	private void tagesMenu(Mensa m, Date d)
	{
		if(m.hasMenuForDate(d))
		{
			outputGrid.setVisible(false);
			outputGrid.removeAll();
			java.util.List ms = m.getMenuForDate(d).getMenues();
			for(Iterator iterator = ms.iterator(); iterator.hasNext();)
			{
				Mensaessen essen = (Mensaessen)iterator.next();
				outputGrid.add(new JLabel(essen.getBeschreibung()));
				outputGrid.add(new JLabel((new StringBuilder(String.valueOf(getStudentenPreis(essen)))).append(" \u20AC / ").append(getPreis(essen)).append(" \u20AC").toString()));
				if(essen.isBeef())
					outputGrid.add(new JLabel("mit lecker Rind!"));
				if(essen.isMoslem())
					outputGrid.add(new JLabel("Moslem"));
				if(essen.isVegetarian())
					outputGrid.add(new JLabel("Vegetarisch"));
				if(!essen.isBeef() && !essen.isMoslem() && !essen.isVegetarian())
					outputGrid.add(new JLabel(""));
			}

			outputGrid.setVisible(true);
		} else
		{
			new Alert();
		}
	}

	private String getStudentenPreis(Mensaessen essen)
	{
		return essen.getStudentenPreis().toString();
	}

	private String getPreis(Mensaessen essen)
	{
		return essen.getPreis().toString();
	}

	public Gui(Mensa m)
	{
		c = Calendar.getInstance();
		outputGrid = new JPanel();
		heute = new Date(c.get(1) - 1900, c.get(2), c.get(5), 0, 0, 0);
		this.m = m;
		setDefaultCloseOperation(3);
		setTitle("namnam - immer hungrig");
		outputGrid.setLayout(new GridLayout(0, 3, 5, 5));
		datechooser = new JDateChooser(heute);
		datechooser.setDateFormatString("dd. MMMMM yyyy");
		datechooser.setMinSelectableDate(m.getFirstDate());
		datechooser.setMaxSelectableDate(m.getLastDate());
		JButton load = new JButton("Men\374 Laden");
		load.addActionListener(this);
		load.setActionCommand("load");
		JPanel oben = new JPanel();
		JLabel header = new JLabel("Men\374 des Tages f\374r ");
		tagesMenu(m, heute);
		oben.add(header);
		oben.add(datechooser);
		oben.add(load);
		add("North", oben);
		add("South", outputGrid);
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("load"))
			tagesMenu(m, datechooser.getDate());
	}

	private static final long serialVersionUID = 0L;
	private Calendar c;
	private JPanel outputGrid;
	private Date heute;
	private JDateChooser datechooser;
	private Mensa m;
}
