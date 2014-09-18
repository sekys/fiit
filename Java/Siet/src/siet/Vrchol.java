package siet;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JButton;

/**
 * Vrchol v editore. Tento vrchol predstavuje tlacitko a zaroven zariadenie v
 * editore.
 * 
 */
public class Vrchol extends JButton {
	private SietoveZariadenie zariadenie;

	public Vrchol(SietoveZariadenie zariadenie) {
		super();
		this.zariadenie = zariadenie;
	}

	@Override
	public String getText() {
		if (zariadenie == null) {
			return "?";
		}
		return zariadenie.toString();
	}

	@Override
	public void setText(String arg0) {
		// Ignorujeme
	}

	public SietoveZariadenie getZariadenie() {
		return zariadenie;
	}

	public void setZariadenie(SietoveZariadenie zariadenie) {
		this.zariadenie = zariadenie;
	}

	@Override
	public String toString() {
		return zariadenie.toString();
	}

	/**
	 * Pomocna metoda pre urcenie stredu z obdlznika.
	 * 
	 * @param r
	 * @return
	 */
	public Point getStred() {
		Rectangle r = getBounds();
		return new Point(r.x + r.width / 2, r.y + r.height / 2);
	}
};