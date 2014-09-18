
package proxy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Window extends JFrame
{
	JButton		ukonciHovori	= new JButton("Ukonci hovori");

	JTextField	meno			= new JTextField(10);
	JTextField	celemeno		= new JTextField(10);
	JTextField	heslo			= new JTextField(10);
	JTextField	domena			= new JTextField(10);
	JButton		odoslat			= new JButton("Pridat");
	JPanel		mainPane;

	public static void main(String[] args) {
		new Window();
	}

	public Window() {
		super();

		mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		setTitle("Proxy");
		
		mainPane.setMaximumSize( new Dimension(690, 690));
        JScrollPane scroll = new JScrollPane(mainPane);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);			
		scroll.setMaximumSize( new Dimension(690, 690));
		
		// Nacitaj udaje z konfiguracneho suboru
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("config.txt"));
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		String port = properties.getProperty("proxy.port");
		String realm = properties.getProperty("proxy.realm");
		String ip = properties.getProperty("database.ip");
		String dbname = properties.getProperty("database.dbname");
		String name = properties.getProperty("database.name");
		String pass = properties.getProperty("database.pass");

		String mojaIP = null;
		try {
			mojaIP = InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		final Database databaza = new Database(ip, dbname, name, pass);
		final MySIPLayer sip = new MySIPLayer(databaza, mojaIP, Integer.parseInt(port), realm, mainPane);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});

		ukonciHovori.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				sip.UkonciHovori();
			}
		});
		odoslat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				databaza.addUser(meno.getText(), celemeno.getText(), heslo.getText(), domena.getText());
			}
		});

		meno.setMaximumSize(new Dimension(200, 40));
		celemeno.setMaximumSize(new Dimension(200, 40));
		heslo.setMaximumSize(new Dimension(200, 40));
		domena.setMaximumSize(new Dimension(200, 40));
		//mainPane.add(ukonciHovori);
		mainPane.add(meno);
		mainPane.add(celemeno);
		mainPane.add(heslo);
		mainPane.add(domena);
		mainPane.add(odoslat);
		add(scroll);
		
		setSize(700, 700);
		setVisible(true);
	}
}
