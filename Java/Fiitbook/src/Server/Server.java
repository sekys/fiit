package Server;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends JFrame {
	private final int PORT = 4444;
	private JTextArea textfield = new JTextArea(6, 20);
	private static Server instance = null; // Server ma mat len jednu instanciu
	private ServerSocket server = null;

	Server() {
		// Nastav hodnoty
		setSize(250, 300);
		setTitle("FiiTBooK - Server");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Pridaj dalsie prvky
		textfield.setEditable(false);
		textfield.setBackground(Color.WHITE);
		JScrollPane scroll = new JScrollPane(textfield);
		add(scroll, BorderLayout.CENTER);
		setBackground(Color.WHITE);
		
		if(instance != null) {
			instance.ShutDown();
		}
		instance = this;
		setVisible(true);
		Start();
	}

	public static Server getInstance() {
		if(instance == null) {
			new Server();
		}
		return instance;
	}

	public void AddText(String n) {
		textfield.append(n+"\n");
	}

	protected void Start() {
		/* Dost dolezita metoda, sputame server
		 * - Nemusi sa to podarit !
		 */
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Port "+PORT+" nieje volny !");
			System.exit(-1);
		}
		while (true) {
			try {
				ServerClient c = new ServerClient(server.accept());
			} catch (IOException e) {
				System.out.println("Chyba pri spracovani portu: "+PORT);
				System.exit(-1);
			}
		}
	}

	protected void finalize() {
		ShutDown();
	}

	public void ShutDown() {
		/* Server sa vypol alebo sa ma vypnut
		 * Socket musime uzatvorit !
		 */
		try {
			if (!server.isClosed())
				server.close();
			instance = null;
		} catch (IOException e) {
			System.out.println("Nemozem zatvorit socket !");
		}
	}

	public static void main(String[] args) {
		Server s = new Server();
	}
}