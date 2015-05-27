package Client;
import java.awt.Color;
import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

public class Client 
	extends JFrame 
	implements ActionListener, Runnable
{
	// Casti okna
	private JButton button = new JButton("Odosli");
	private JTextField input = new JTextField(100);
	private JTextArea chatbox = new JTextArea(25, 20);
	
	// Komunikacia, nit,..
	private PrintWriter out;
	private BufferedReader in;
	private Thread t;
	private Socket socket;
	protected String name = "Client";
	
	public Client() {	
		setSize(500, 500);
		setBackground(Color.WHITE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// Registruj nove prvky do okna
		button.addActionListener(this);
		button.setSize(100, 10);
		input.setSize(5, 5);

		chatbox.setEditable(false);
		chatbox.setBackground(Color.WHITE);
		JScrollPane scroll = new JScrollPane(chatbox);
				
		add(scroll, BorderLayout.NORTH);
		add(input, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		Start();
	}
	protected void Start(){
		try{
			// Vytvor spojenie so serverom na IP a PORTE
			socket = new Socket("localhost", 4444);
			// Odchadzajuce spravy
			out = new PrintWriter( socket.getOutputStream(), true);
			// Prichadzajuce spravy
			in = new BufferedReader( new InputStreamReader(socket.getInputStream()) );
			
			// Zaroven vytvor nit ak sa to podarilo
		    t = new Thread(this);
			t.start();
			setVisible(true);
	     } catch (UnknownHostException e) {
	    	 // Mozu nasta rozne problemy :
	    	 System.out.println("Host nenajdeny / Zla IP adresa !");
	    	 System.exit(0);
	     } catch  (IOException e) {
	    	 System.out.println("Server nenajdeny !");
	    	 System.exit(0);
	     } 
	}
	public void actionPerformed(ActionEvent event){ // Event - Klikne na odoslat 
		if(event.getSource() != button) return;
	     
	     // Spracuj udaje, posli, vynuluj
	     String txt = input.getText();
	     Send(txt);
	     input.setText(new String(""));
	} 
	protected void Alert(String x) {
		System.out.println("!!! "+x+" !!!");
	}
	// Posli spravu na server
	public void Send(String n) {
		if(n.length() > 0) { // Len ak treba
			out.println(n);
			System.out.println("Posielam: "+n);
			AddTxt(n);
		}
	}
	// Nastav novu spravu
	protected void AddTxt(String n) {
		chatbox.append(n+"\n");
	}
	public void run() {
		// Zisti ci su nove data
	    while(true) {
	    	try{
		    	 String txt = in.readLine();
		    	 System.out.println("Server posiela: " +txt);
		    	 // Aky typ spravy poslal ? 
		    	 if(txt.contains("%C%")) {
		    		 txt = txt.replaceFirst("%C%", "");
		    		 CommandFromServer(txt.split("[.]"));
		    	 } else {
		    		 MsgFromServer(txt);
		    	 }
		     } catch (IOException e){
		    	 System.out.println("Error pri prenose - Server shutdown ?");
		       	 System.exit(0);
		     }	
	    }
	}
	
	// Dalsie eventy ako pomocky
	// Pretazime si metodu, len ako pomocka
	public void setTitle() {
		super.setTitle("FiiTBooK - "+name);
	}
	protected void MsgFromServer(String txt) { // event
		System.out.println("Server posiela spravu: " +txt);
		AddTxt(txt);
	}
	protected void CommandFromServer(String[] cmds) { // event
		System.out.println("Server posiela prikaz: "+cmds);
		if(cmds[0].equalsIgnoreCase("NAME")) {
			name = cmds[1];
			setTitle();
		}
	}
	public static void main(String[] args) {
		Client c = new Client();
	}
} 