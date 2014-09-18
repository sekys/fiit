
package Aplikacia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public abstract class Okno extends JFrame
{
	protected JButton	alarmA	= new JButton("Alarm s postriekovacom on/off");
	protected JButton	alarmB	= new JButton("Alarm co vola hasicov on/off");
	protected JButton	vypis	= new JButton("Vypis historiu");
	protected JPanel	panel	= new JPanel();
	protected JTextArea	text	= new JTextArea(25, 20);

	public Okno() {
		super("Alarm");
		setTitle("Alarm");

		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(alarmA);
		panel.add(alarmB);
		panel.add(vypis);
		add(panel);

		text.setEditable(false);
		text.setSize(400, 400);
		JScrollPane scroll = new JScrollPane(text);
		add(scroll, BorderLayout.NORTH);
		setSize(600, 600);
		setVisible(true);

		presmerujVystupDoOkna();
		start();
		alarmA.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				alarmAClick();
			}
		});
		alarmB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				alarmBClick();
			}
		});
		vypis.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				vypis();
			}
		});
	}

	protected abstract void start();
	protected abstract void alarmAClick();
	protected abstract void alarmBClick();
	protected abstract void vypis();

	private void updateTextArea(final String t) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() {
				text.append(t);
			}
		});
	}

	private void presmerujVystupDoOkna() {
		OutputStream out = new OutputStream()
		{
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
	}
}
