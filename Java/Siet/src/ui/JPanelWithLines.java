package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import siet.Prepojenie;

/**
 * JPanelWithLines je specialna trieda, ktora sa stara o vykreslovanie ciar
 * medzi tlacitkam. Vykresluju sa zaroven cervene bodky na znak komunikacie
 * zariadeni.
 * 
 */
public class JPanelWithLines extends JPanel {
	private ArrayList<Prepojenie> prepojenia;

	public JPanelWithLines(ArrayList<Prepojenie> zoznam) {
		super();
		prepojenia = zoznam;
		prepareTimer();
	}

	/**
	 * Priprav casovac, ktory kazdych 100 ms spusti prekreslenie obrazovky a
	 * posunie komunikaciu trocha dalej.
	 */
	protected void prepareTimer() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Posun komunikaciu ourcity cas dalej
				for (Prepojenie prep : prepojenia) {
					prep.posunKomunikaciuDalej(0.05f);
				}

				// Prekresli ciary
				repaint();
			}
		};

		new Timer(100, taskPerformer).start();
	}

	/**
	 * Pretazena metoda, ktora sa stara o vykreslovanie ciar.
	 */
	protected void paintComponent(Graphics g) {		
		super.paintComponent(g); // Vykresli komponenty, ktore su sucastou panela

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Vykresli ciary medzi zariadeniami
		for (Prepojenie prep : prepojenia) {
			g2d.setPaint(Color.BLACK);
			Point a = prep.getZdroj().getStred();
			Point b = prep.getCiel().getStred();
			g2d.drawLine(a.x, a.y, b.x, b.y);

			// Ak je aniamcia zapnuta tak sa objavit cervena bodka
			if (prep.isAnimacia()) {
				// Vykonaj interpolaciu
				Point p = prep.getPoziciaPaketu();
				g2d.setPaint(Color.RED);
				g2d.drawRect(p.x, p.y, 2, 2);
			}
		}
	}
}