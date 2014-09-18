package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import siet.PC;
import siet.SietoveZariadenie;
import siet.Switch;
import siet.Vrchol;

/**
 * Ide o editor, ktory je rozsireny o akcie s pouzivatelom.
 * 
 */
public abstract class InteraktivnyEditor extends Editor {

	protected JTextField m_nazov;
	protected JComboBox m_typ;
	protected JTextField m_rozhrani;
	protected JPanel m_okno;
	protected JComboBox m_zdroj;
	protected JComboBox m_ciel;

	/**
	 * Ked pouzivatel klikol na vrchol, chceme aby udaje vo formulari sa
	 * prisposobili udajov na vrchole.
	 */
	@Override
	protected void klikolNaVrchol(Vrchol zvoleny) {
		m_nazov.setText(zvoleny.getZariadenie().toString());
		m_rozhrani.setText(zvoleny.getZariadenie().getPocetPripojeni()
				.toString());
		m_typ.setSelectedItem(zvoleny.getZariadenie().getClass());
	}

	// Pouzivatel klikol na tlacitko komunikuj
	protected ActionListener getKomunikujAkciu() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vrchol a = getZdroj();
				Vrchol b = getCiel();
				if (a == b) {
					JOptionPane.showMessageDialog(getFrame(),
							"Nemozno prepojit tieto komponenty.");
					return;
				}
				if (a == null || b == null) {
					return;
				}
				if (!komunikuj(a, b)) {
					JOptionPane
							.showMessageDialog(getFrame(),
									"Aplikacia nepodporuje poslanie paketu priamo cez celu siet.");
				}
			}
		};
	}

	// Pouzivatel klikol na tlacitko spoj
	protected ActionListener getSpojAkciu() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vrchol a = getZdroj();
				Vrchol b = getCiel();
				if (a == b) {
					JOptionPane.showMessageDialog(getFrame(),
							"Nemozno prepojit tieto komponenty.");
					return;
				}
				if (a == null || b == null) {
					return;
				}
				if (!prepoj(a, b)) {
					JOptionPane
							.showMessageDialog(getFrame(),
									"Prepojenie uz existuje alebo dosiahlo maximalny pocet pripojeni!");
				}
			}
		};
	}

	// Pouzivatel klikol na tlacitko pridaj
	protected ActionListener getPridajAkciu() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer id = getZvoleneID();
				SietoveZariadenie typ = getZvolenyTyp();
				//Integer rozhrani = getPocetRozhrani();
				if (id == null || typ == null) // || rozhrani == null
					return;
				install(id, typ, 10, 10);
			}
		};
	}

	// Pouzivatel klikol na tlacitko nastav
	protected ActionListener getNastavAckiu() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vrchol vrchol = getZvoleny();
				if (vrchol == null) {
					return;
				}
				Integer id = getZvoleneID();
				SietoveZariadenie typ = getZvolenyTyp();
				//Integer rozhrani = getPocetRozhrani();
				if (id == null || typ == null ) { //|| rozhrani == null
					return;
				}
				if (!vrchol.getZariadenie().getClass().equals(typ.getClass())) {
					// TODO: zrus existujuce prepojenia
					vrchol.setZariadenie(typ);
				}
				vrchol.getZariadenie().setUnikatnaAdresa(id);
			}
		};
	}

	// Pouzivatel si moze zvolit typy, ake ?
	protected void installTypes() {
		// Pouzite pokrocile veci z typom triedy
		m_typ.addItem(PC.class);
		m_typ.addItem(Switch.class);
	}

	private Vrchol getZdroj() {
		Vrchol vrchol;
		vrchol = (Vrchol) m_zdroj.getSelectedItem();
		return vrchol;
	}

	private Vrchol getCiel() {
		Vrchol vrchol;
		vrchol = (Vrchol) m_ciel.getSelectedItem();
		return vrchol;
	}

	/**
	 * Pretazena metoda ! Zaroven sa zavola super metoda. Nainstalovane vrcholy
	 * pridaj do zoznamu vrchol, do combo boxov
	 */
	public Vrchol install(Integer id, SietoveZariadenie zariadenie, int x, int y) {
		Vrchol v = super.install(id, zariadenie, x, y);
		this.m_zdroj.addItem(v);
		this.m_ciel.addItem(v);
		m_okno.add(v);
		return v;
	}

	private SietoveZariadenie getZvolenyTyp() {
		// Najdeme co pouzivatel vybral ... vybral nejaku triedu
		// Pouzity template v jace
		Class<? extends SietoveZariadenie> trieda;
		trieda = (Class<? extends SietoveZariadenie>) m_typ.getSelectedItem();
		if (trieda == null) {
			return null;
		}
		try {
			// Kontrola vynimky
			// S triedy spravime instanciu a posleme ju pouzivatelovi
			return trieda.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Integer getZvoleneID() {
		// Pouzivatel si zvolil nazov alebo ID pre zariadenie
		String nazov = m_nazov.getText();
		if (nazov.isEmpty()) {
			JOptionPane.showMessageDialog(getFrame(),
					"Do nazvu komponentu napis cislo.");
			return null;
		}
		Integer cislo;
		try {
			// Mi chceme aby si zvolil iba cislo
			cislo = Integer.parseInt(nazov);
		} catch (NumberFormatException e) {
			// Kontrola ci pouzivatel zadal cislo ... cislo sa vyparsue z textu
			// Ak je zle vyhodi vynimku
			JOptionPane.showMessageDialog(getFrame(),
					"Do nazvu komponentu napis cislo.");
			return null;
		}

		// Teraz je potrebne este overit ci take ID cislo existuje
		boolean existuje = existujeZariadenie(cislo);
		if (existuje) {
			JOptionPane.showMessageDialog(getFrame(),
					"Dane cislo uz existuje, zvolte prosim ine!");
			return null;
		}
		return cislo;
	}

	/*
	private Integer getPocetRozhrani() {
		// Pouzivatel si zvolil nazov alebo ID pre zariadenie
		String nazov = m_rozhrani.getText();
		if (nazov.isEmpty()) {
			JOptionPane.showMessageDialog(getFrame(),
					"Do pocet rozhrani napis cislo.");
			return null;
		}
		Integer cislo;
		try {
			// Mi chceme aby si zvolil iba cislo
			cislo = Integer.parseInt(nazov);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(getFrame(),
					"Do pocet rozhrani napis cislo.");
			return null;
		}

		return cislo;
	}
	*/

	protected abstract JFrame getFrame();
}
