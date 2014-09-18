package ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import siet.PC;
import siet.Paket;
import siet.Prepojenie;
import siet.SietoveZariadenie;
import siet.Switch;
import siet.Vrchol;

/**
 * Trieda sa zaobera editorom, je tu vsetka funkcionalita, ktora z editorom
 * suvisi.
 */
public class Editor extends Siet {
	private Vrchol zvoleny;
	private KeyListener m_keylistener;
	private ActionListener m_actionlistener;

	public Editor() {
		zvoleny = null;
		createListeners();
	}

	/**
	 * Nainstaluj vrchol respektive zariadenie do systemu
	 * 
	 * @param id
	 * @param zariadenie
	 * @param x
	 * @param y
	 * @return
	 */
	public Vrchol install(Integer id, SietoveZariadenie zariadenie, int x, int y) {
		// Nastav poziciu v okne a pridaj tlacitko
		Vrchol vrchol = super.install(id, zariadenie, x, y);
		Dimension size = vrchol.getPreferredSize();
		vrchol.setBounds(x, y, size.width, size.height);
		vrchol.addActionListener(m_actionlistener);
		vrchol.addKeyListener(m_keylistener);
		vrcholy.add(vrchol);
		return vrchol;
	}

	/**
	 * Metoda sa spusti ked pouzivatel klikol na vrchol
	 * 
	 * @param zvoleny
	 */
	protected void klikolNaVrchol(Vrchol zvoleny) {
	}

	/**
	 * Nainstaluj uvodne zariadenia do editora
	 */
	protected void installDefault() {
		// Nainstaluj zariadenia
		Vrchol a, b, c, d;
		a = install(1, new PC(), 20, 20);
		b = install(2, new Switch(), 80, 100);
		c = install(3, new PC(), 120, 180);
		d = install(4, new PC(), 40, 160);

		// Nainstaluj prepojenia
		prepoj(a, b);
		prepoj(c, b);
		prepoj(d, b);
	}

	/**
	 * Ake bolo zvolene zaraidenie , na ktore sa kliklo ?
	 * 
	 * @return
	 */
	public Vrchol getZvoleny() {
		return zvoleny;
	}

	/**
	 * Pouzivatel klikol na spustenie komunikacie, je potrebne identifikovat
	 * prepojenie a poslat nejaky paket.
	 * 
	 * @param a
	 * @param b
	 */
	protected boolean komunikuj(Vrchol a, Vrchol b) {
		Prepojenie prep = new Prepojenie(a, b);

		for (Prepojenie existujucePrepojenie : prepojenia) {
			if (existujucePrepojenie.equals(prep)) {
				// Vyber nahodny ciel
				Random rand = new Random();
				int index = rand.nextInt(vrcholy.size());
				SietoveZariadenie ciel = vrcholy.get(index).getZariadenie();

				// Nasiel som take prepojenie posli paket
				Paket paket = new Paket();
				paket.setZdroj(existujucePrepojenie.getZdroj().getZariadenie());
				paket.setCiel(ciel);
				paket.setPrenasanaInformacia("ahoj");
				existujucePrepojenie.posliPaket(paket);
				return true;
			}
		}

		// Nenasiel som take prepojenie ?
		return false;
	}

	/**
	 * Pouzivatel stlacil klavesnicu, zrejme pohol zariadenim vo vrchole !
	 * 
	 * @param key
	 */
	protected void stlacilKlavesnicu(int key) {
		int x = 0;
		int y = 0;
		switch (key) {
		case KeyEvent.VK_RIGHT: {
			x += 5;
			break;
		}
		case KeyEvent.VK_LEFT: {
			x -= 5;
			break;
		}
		case KeyEvent.VK_UP: {
			y -= 5;
			break;
		}
		case KeyEvent.VK_DOWN: {
			y += 5;
			break;
		}
		default: {
			break;
		}
		}
		Rectangle rect = zvoleny.getBounds();
		rect.x += x;
		rect.y += y;
		zvoleny.setBounds(rect);
	}

	/**
	 * Zachytavaju sa udalosti, ked pouzivtel pouzival klavesu. Alebo ked klikol
	 * na tlacitko (vrchol) v editore.
	 */
	protected void createListeners() {
		// Listener na pohyb - interface je pretazeny anonymne
		m_keylistener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (zvoleny == null) {
						return;
					}
					int key = e.getKeyCode();
					stlacilKlavesnicu(key);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		};

		// Listener na zakliknutie vrchola
		m_actionlistener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zvoleny = (Vrchol) e.getSource();
				klikolNaVrchol(zvoleny);
			}
		};
	}
}
