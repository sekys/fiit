package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

//Na server sa pripajaju clienti, definuj pre nich triedu
public class ServerClient implements Runnable {
	private Socket client;
	private PrintWriter out = null;
	private Thread t;

	// Kazdemu clientovy priradime ID
	private int id = -1;
	protected static ArrayList<ServerClient> clients = new ArrayList<ServerClient>(); // Chceme mat aj zoznam clientov

	ServerClient(Socket client) {
		// Priradime potrebne hodnoty, vypiseme debug, zapneme nit
		this.client = client;
		id = clients.size();
		System.out.println(this+ "Client connected.");
		clients.add(this);
		t = new Thread(this);
		t.start();
	}

	public void run() {
		String line;
		BufferedReader in = null;
		try {
			// Potrebujeme sa spojit so serverom
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			int index = clients.indexOf(this);
			Disconnect(index);
		}

		while (true) {
			try {
				// Client nam poslal nejaku spravu
				line = in.readLine();
				Prijmam(line);
			} catch (IOException e) {
				// Exception - client sa odpojil
				int index = clients.indexOf(this);
				if (index != -1) {
					Disconnect(index);
				}
				break;
			}
		}
	}
	
	// Event - client sa odpojil
	private void Disconnect(int index) { 
		System.out.println(this+"Client disconnect.");
		clients.remove(index);
	}

	// Event - Client nam poslal spravu
	private void Prijmam(String txt) { 
		//Command("NAME", txt);
		PosliSpravuOstatnym(txt);
		System.out.println(this+"Prijmam: "+txt);
		Server.getInstance().AddText(txt);
	}

	// Posli data naspat ostatnym clientom
	private void PosliSpravuOstatnym(String line) {
		int i;
		for (i = 0; i < clients.size(); i++) {
			if (clients.get(i) != this) { // Okrem daneho clienta
				clients.get(i).Msg(line);
			}
		}
	}
	
	// Len male pomocky :
	public void Msg(String txt) {
		out.println(txt);
	}
	public void Command(String name, String value) {
		out.println("%C%"+name+"."+value);
	}
	public String toString() {
		return "("+id+")";
	}
}