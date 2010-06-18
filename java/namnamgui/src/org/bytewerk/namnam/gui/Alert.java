package org.bytewerk.namnam.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Alert used if no menue can be found for a date
 * @author chiffre
 */

public class Alert extends JFrame
	implements ActionListener
{

	public Alert()
	{
		setTitle("Fehler");
		JLabel meldung = new JLabel("Zum gegebenen Datum ist kein Men\374 verf\374gbar!");
		JButton exit = new JButton("Abbrechen");
		add("North", meldung);
		add("South", exit);
		exit.addActionListener(this);
		exit.setActionCommand("exit");
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("exit"))
			dispose();
	}

	private static final long serialVersionUID = 0L;
}
