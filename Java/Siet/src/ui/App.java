package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JTextField;

import java.awt.Color;

import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.Dimension;

/**
 * Tato trieda predstavuj GUI. Subor je zvacsa vygenerovany za pomoci ineho
 * programu. Program sa vola WindowBuilder.
 * 
 */
public class App extends InteraktivnyEditor {

	private JFrame frame;

	protected JFrame getFrame() {
		return frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		super();
		initialize();

		// Inicializujeme nase casti kodu
		installTypes();
		installDefault();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Editor");
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		m_okno = new JPanelWithLines(this.prepojenia);
		m_okno.setBackground(Color.WHITE);
		frame.getContentPane().add(m_okno, BorderLayout.CENTER);
		m_okno.setLayout(null);
		m_okno.setBounds(0, 0, 300, 300);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("56px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("110px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("16px"), }, new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel = new JLabel("Komponent");
		panel_1.add(lblNewLabel, "4, 2, left, center");

		JLabel lblNewLabel_1 = new JLabel("Nazov");
		panel_1.add(lblNewLabel_1, "2, 4, right, center");

		m_nazov = new JTextField();
		panel_1.add(m_nazov, "4, 4, left, top");
		m_nazov.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Typ");
		panel_1.add(lblNewLabel_2, "2, 6, right, default");

		m_typ = new JComboBox();
		m_typ.setMinimumSize(new Dimension(80, 20));
		panel_1.add(m_typ, "4, 6, fill, default");

		JLabel lblNewLabel_3 = new JLabel("Rozhrani");
		panel_1.add(lblNewLabel_3, "2, 8, right, default");

		m_rozhrani = new JTextField();
		panel_1.add(m_rozhrani, "4, 8, fill, default");
		m_rozhrani.setColumns(10);
		m_rozhrani.setEditable(false);

		JButton m_nastav = new JButton("Nastav");
		m_nastav.addActionListener(getNastavAckiu());
		panel_1.add(m_nastav, "4, 10");

		JButton m_pridaj = new JButton("Pridaj");
		m_pridaj.addActionListener(getPridajAkciu());
		panel_1.add(m_pridaj, "4, 12");

		JLabel lblNewLabel_4 = new JLabel("Komunikacia");
		panel_1.add(lblNewLabel_4, "4, 20");

		JLabel lblNewLabel_5 = new JLabel("Zdroj");
		panel_1.add(lblNewLabel_5, "2, 22, right, default");

		m_zdroj = new JComboBox();
		panel_1.add(m_zdroj, "4, 22, fill, default");

		JLabel lblNewLabel_6 = new JLabel("Ciel");
		panel_1.add(lblNewLabel_6, "2, 24, right, default");

		m_ciel = new JComboBox();
		panel_1.add(m_ciel, "4, 24, fill, default");

		JButton m_spoj = new JButton("Spoj");
		m_spoj.addActionListener(getSpojAkciu());
		panel_1.add(m_spoj, "4, 26");

		JButton m_komunikuj = new JButton("Komunikuj");
		m_komunikuj.addActionListener(getKomunikujAkciu());
		panel_1.add(m_komunikuj, "4, 28");

	}
}
