
package proxy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.TooManyHopsException;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Message;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class BaseSIPLayer implements SipListener
{
	protected static boolean			AUTORIZATION	= true;

	protected SipStack					sipStack;
	protected SipFactory				sipFactory;
	protected HeaderFactory				headerFactory;
	protected MessageFactory			messageFactory;
	protected SipProvider				sipProvider;
	protected AddressFactory			addressFactory;

	protected String					myIP;
	protected int						myPort;
	protected String					m_realm;
	protected HashMap<String, Address>	mapTable		= new HashMap<String, Address>();

	private Random						random;

	/*
	 * Priprav sietove prvky na zachytavanie udajov.
	 */
	public BaseSIPLayer(String ip, int port, String realm, JPanel panel) {
		myIP = ip;
		myPort = port;
		m_realm = realm;
		homePanel = panel;
		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		random = new Random(System.currentTimeMillis());

		try {
			createStack();

			addressFactory = sipFactory.createAddressFactory();
			headerFactory = sipFactory.createHeaderFactory();
			messageFactory = sipFactory.createMessageFactory();

			ListeningPoint tcp = sipStack.createListeningPoint(port, "tcp");
			ListeningPoint udp = sipStack.createListeningPoint(port, "udp");
			sipProvider = sipStack.createSipProvider(tcp);
			sipProvider.addSipListener(this);
			sipProvider = sipStack.createSipProvider(udp);
			sipProvider.addSipListener(this);
		}
		catch (Throwable e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/*
	 * Vytvor zasobnik, ktory bude uchovavat prijate spravy.
	 * On potrebuje nastavit nejake parametre.
	 */
	private void createStack() throws PeerUnavailableException {
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", "Proxy");
		properties.setProperty("javax.sip.IP_ADDRESS", myIP);

		// Nazvy debuh suborov
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "log/serverlog.txt");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "log/debuglog.txt");
		//properties.setProperty("gov.nist.javax.sip.PASS_INVITE_NON_2XX_ACK_TO_LISTENER", "true");
		properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT", "off");
		sipStack = sipFactory.createSipStack(properties);
	}

	protected void fileEcho(String nazov, String sprava) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(nazov + ".txt", true));
			out.println(sprava);
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class GUITrojica extends JPanel
	{
		public JTextArea	text;
		public JButton		close;

		public GUITrojica(String name) {
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(name), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			setMinimumSize(new Dimension(500, 150));

			text = new JTextArea("", 10, 20);
			text.setLineWrap(true);
			JScrollPane scroll = new JScrollPane(text);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			this.add(scroll);

			close = new JButton("X");
			this.add(close);

			final Component me = this;
			close.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) {
					guiMap.remove(me);
					homePanel.remove(me);
					homePanel.revalidate();
					homePanel.repaint();
				}
			});
		}
	}

	private HashMap<String, GUITrojica>	guiMap	= new HashMap<String, GUITrojica>();
	private JPanel						homePanel;

	private void guilog(String key, String msg) {
		GUITrojica gui = guiMap.get(key);
		gui = guiMap.get(key);
		if (gui == null) {
			gui = new GUITrojica(key);
			homePanel.add(gui);
			guiMap.put(key, gui);
			homePanel.revalidate();
			homePanel.repaint();
		}

		gui.text.append(msg);
	}

	protected void log(Message msg, String prefix) {
		String sprava = prefix + "\n" + msg.toString();
		System.out.println(sprava);
		guilog("All", sprava);
		fileEcho("log/All", sprava);
		log(sprava);

		// Dialogy - pomocou call ID
		CallIdHeader callidheader = ((CallIdHeader) msg.getHeader(CallIdHeader.NAME));
		if (callidheader != null) {
			String callid = callidheader.getCallId();
			if (callid != null) {
				fileEcho("log/CallID_" + callid, sprava);
				guilog("CallID_" + callid, sprava);
			}
		}

		// Tranzakcie cez CSeq
		CSeqHeader cseqHeader = (CSeqHeader) msg.getHeader(CSeqHeader.NAME);
		if (cseqHeader != null) {
			long cseqNumber = cseqHeader.getSeqNumber();
			fileEcho("log/CSeq_" + cseqNumber, sprava);
			guilog("CSeq_" + cseqNumber, sprava);
		}
	}
	protected abstract void log(String sprava);

	/*
	 * Server prijal odpoved z serveru.
	 * Co s tym ? sa riesi tu
	 */
	public void processResponse(ResponseEvent evt) {
		try {
			Response response = evt.getResponse();
			log(response, "Response:");

			// Vymaz prve via
			response.removeFirst(ViaHeader.NAME);
			// Max forwards zniz
			znizMaxForwards(response);
			// User agent zmen na nas
			nastavNasUserAgent(response);
			// Odosleme - system zisti kde to ma poslat podla drueho VIA
			try {
				log(response, "Posielam:");
				sipProvider.sendResponse(response);
			}
			catch (SipException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SUBSCRIBE(Request req) {
		Response response;
		try {
			response = messageFactory.createResponse(489, req);
			log(response, "Posielam:");
			sipProvider.sendResponse(response);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (SipException e) {
			e.printStackTrace();
		}
	}

	public void processRequest(RequestEvent evt) {
		try {
			Request req = evt.getRequest();
			log(req, "Request:");
			String method = req.getMethod();

			if (method.equals("REGISTER")) {
				// Ide o register metodu ktora je trocha specialna
				REGISTER(req);
			} else if (method.equals("SUBSCRIBE")) {
				// Ide o subscribe metodu ktora je trocha specialna
				SUBSCRIBE(req);
			} else if (method.equals("ACK")) {
				// Ide o ICK metodu
				ACK(req);
			} else if (method.equals("BYE")) {
				// Ide o subscribe metodu ktora je trocha specialna
				BYE(req);
			} else if (method.equals("INVITE")) {
				// Ide o subscribe metodu ktora je trocha specialna
				INVITE(req);
			} else {
				// Vsetke ostatne metody
				ZvysneRequesty(req);
			}
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	private void UzivatelNiejeOnline(Request req) {
		try {
			Response response = messageFactory.createResponse(Response.NOT_FOUND, req);
			ToHeader to = (ToHeader) response.getHeader(ToHeader.NAME);
			to.setTag("888");
			ServerTransaction st = sipProvider.getNewServerTransaction(req);
			st.sendResponse(response);
		}
		catch (TransactionAlreadyExistsException e1) {
			e1.printStackTrace();
		}
		catch (TransactionUnavailableException e2) {
			e2.printStackTrace();
		}
		catch (ParseException e3) {
			e3.printStackTrace();
		}
		catch (SipException e4) {
			e4.printStackTrace();
		}
		catch (InvalidArgumentException e5) {
			e5.printStackTrace();
		}
	}

	private void pridajMojeVIA(Request req) {
		// S najvrchnejsieho VIA sprav hash
		ViaHeader via = (ViaHeader) req.getHeader(ViaHeader.NAME);
		int hash = via.getBranch().hashCode();

		// Pridaj nove VIA
		ViaHeader nove = null;
		try {
			String branch = "z9hg4bk" + hash;
			nove = headerFactory.createViaHeader(myIP, getPort(), getTransport(), branch);
			req.addFirst(nove);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e1) {
			e1.printStackTrace();
		}
		catch (SipException e1) {
			e1.printStackTrace();
		}
	}

	private Set<Hovor>	hovoriPrebiehajuce	= new LinkedHashSet<Hovor>();

	private void ACK(Request req) {
		/*
		 try {
			Response response = messageFactory.createResponse(Response.OK, req);
			log(response, "Posielam:");
			sipProvider.sendResponse(response);
		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		catch (SipException e) {
			e.printStackTrace();
		}
		*/
		
		// Pridaj moje VIA
		pridajMojeVIA(req);
		// Max forwards zniz
		znizMaxForwards(req);
		// User agent zmen na nas
		nastavNasUserAgent(req);
		// Pridaj record route
		pridajRecordRoute(req);
		
		// a odosleme
		try {
			log(req, "Posielam:");
			sipProvider.sendRequest(req);
		}
		catch (SipException e) {
			e.printStackTrace();
		}

		// zaregistruj hovor
		Hovor hovor = new Hovor(req);
		if (hovoriPrebiehajuce.contains(hovor)) {
			System.out.println("pozor taky hovor uz je");
		}
		hovoriPrebiehajuce.add(hovor);
	}

	private void BYE(Request req) {
		// Pridaj moje VIA
		pridajMojeVIA(req);
		// Max forwards zniz
		znizMaxForwards(req);
		// User agent zmen na nas
		nastavNasUserAgent(req);
		// Pridaj record route
		pridajRecordRoute(req);
		// a odosleme
		try {
			log(req, "Posielam:");
			sipProvider.sendRequest(req);
		}
		catch (SipException e) {
			e.printStackTrace();
		}

		// ukonci hovor
		Hovor hovor = new Hovor(req);
		if (!hovoriPrebiehajuce.contains(hovor)) {
			System.out.println("pozor taky hovor neexistuje je");
		}
		hovoriPrebiehajuce.remove(hovor);
	}

	private void INVITE(Request req) {
		if (!req.getRequestURI().isSipURI()) {
			System.out.println("neposlal sip prikaz");
			return;
		}

		// Obsahuje tabulka dane meno ? +> neobsahuje
		String meno = ((SipURI) req.getRequestURI()).getUser();
		if (!mapTable.containsKey(meno)) {
			UzivatelNiejeOnline(req);
			return;
		}

		// Posli 180 trying
		sendTrying(req);

		// Pridaj nase VIA
		Address kamPoslat = mapTable.get(meno);
		pridajMojeVIA(req);
		// Max forwards zniz
		znizMaxForwards(req);
		// User agent zmen na nas
		nastavNasUserAgent(req);
		// Pridaj record route
		pridajRecordRoute(req);
		// Zadame na aku IP to ma odoslat
		req.setRequestURI(kamPoslat.getURI());
		// a odosleme
		try {
			log(req, "Posielam:");
			sipProvider.sendRequest(req);
		}
		catch (SipException e) {
			e.printStackTrace();
		}
	}

	private void sendTrying(Request req) {
		try {
			Response response = messageFactory.createResponse(Response.TRYING, req);
			ServerTransaction st = sipProvider.getNewServerTransaction(req);
			st.sendResponse(response);
		}
		catch (TransactionAlreadyExistsException e1) {
			e1.printStackTrace();
		}
		catch (TransactionUnavailableException e2) {
			e2.printStackTrace();
		}
		catch (ParseException e3) {
			e3.printStackTrace();
		}
		catch (SipException e4) {
			e4.printStackTrace();
		}
		catch (InvalidArgumentException e5) {
			e5.printStackTrace();
		}
	}

	private void ZvysneRequesty(Request req) {
		if (!req.getRequestURI().isSipURI()) {
			System.out.println("neposlal sip prikaz");
			return;
		}

		// Obsahuje tabulka dane meno ? +> neobsahuje
		String meno = ((SipURI) req.getRequestURI()).getUser();
		if (!mapTable.containsKey(meno)) {
			UzivatelNiejeOnline(req);
			return;
		}

		// Pridaj nase VIA
		Address kamPoslat = mapTable.get(meno);
		pridajMojeVIA(req);
		// Max forwards zniz
		znizMaxForwards(req);
		// User agent zmen na nas
		nastavNasUserAgent(req);
		// Pridaj record route
		pridajRecordRoute(req);
		// Zadame na aku IP to ma odoslat
		req.setRequestURI(kamPoslat.getURI());
		// a odosleme
		try {
			log(req, "Posielam:");
			sipProvider.sendRequest(req);
		}
		catch (SipException e) {
			e.printStackTrace();
		}
	}

	protected void znizMaxForwards(Message m) {
		MaxForwardsHeader mfHeader = (MaxForwardsHeader) m.getHeader(MaxForwardsHeader.NAME);
		if (mfHeader == null) return;
		try {
			mfHeader.decrementMaxForwards();
		}
		catch (TooManyHopsException e) {
			e.printStackTrace();
		}
		m.setHeader(mfHeader);
	}

	protected void nastavNasUserAgent(Message m) {
		UserAgentHeader userAgentHeader = null;
		try {
			List<String> userAgentTokens = new LinkedList<String>();
			userAgentTokens.add("Proxy");
			userAgentTokens.add("1.0");
			userAgentTokens.add(System.getProperty("os.name"));
			userAgentHeader = headerFactory.createUserAgentHeader(userAgentTokens);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		m.setHeader(userAgentHeader);
	}

	private int getPort() {
		ListeningPoint[] lps = sipProvider.getListeningPoints();
		return lps[0].getPort();
	}

	private String getTransport() {
		ListeningPoint[] lps = sipProvider.getListeningPoints();
		return lps[0].getTransport();
	}

	private void pridajRecordRoute(Message req) {
		req.removeHeader(RouteHeader.NAME);
		try {
			SipURI uri = addressFactory.createSipURI(null, myIP);
			uri.setPort(getPort());
			uri.setLrParam();
			Address address = addressFactory.createAddress(null, uri);
			RecordRouteHeader rr = headerFactory.createRecordRouteHeader(address);
			req.addHeader(rr);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void REGISTER(Request req) {
		// Ak uzivatel sa odhlasuje
		FromHeader from = (FromHeader) req.getHeader(FromHeader.NAME);
		String cislo = ((SipURI) from.getAddress().getURI()).getUser();
		ExpiresHeader expire = (ExpiresHeader) req.getHeader(ExpiresHeader.NAME);
		if(expire.getExpires() == 0) {
			if(!mapTable.containsKey(cislo)) {
				System.out.println("Uzivatel je uz odhlaseny");
			} else {
				System.out.println("Uzivatel " + cislo + " odhlaseny");
				mapTable.remove(cislo);
			}
			return;
		}
		
		if (AUTORIZATION) {
			// Autentifikacia je zapnuta
			Autentifikacia(req);
		} else {
			// Bez autentifikaciu clienta hned autorizujeme
			Authorized(req);
		}
	}

	private void Autentifikacia(Request req) {
		AuthorizationHeader auth; // ProxyAuthorizationHeader
		auth = (AuthorizationHeader) req.getHeader(AuthorizationHeader.NAME);

		// Nemame riadok authorize
		if (auth == null) {
			UnAuthorized(req);
			return;
		}

		// Overime prihlasovacie udaje
		if (!checkAuthorization(req, auth)) {
			// Udaje nesedia
			req.removeHeader(AuthorizationHeader.NAME);
			UnAuthorized(req);
			return;
		}

		// Udaje sedia, client je autorizovany
		Authorized(req);
	}

	protected abstract boolean checkAuthorization(Request request,
			AuthorizationHeader auth);

	private void Authorized(Request req) {
		// Vytvor odpoved 200 OK
		try {
			Response response = messageFactory.createResponse(Response.OK, req);
			log(response, "Posielam:");
			sipProvider.sendResponse(response);

			// Zapis udaje do mapovacej tabulky
			FromHeader from = (FromHeader) req.getHeader(FromHeader.NAME);
			String cislo = ((SipURI) from.getAddress().getURI()).getUser();
			ContactHeader contact = (ContactHeader) req.getHeader(ContactHeader.NAME);
			Address adresa = contact.getAddress();
			mapTable.put(cislo, adresa);
			System.out.println("Uzivatel " + cislo + " prihlaseny");
		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		catch (SipException e2) {
			e2.printStackTrace();
		}
	}

	protected String generateNonce() {
		try {
			MessageDigest sifrovanie = MessageDigest.getInstance("MD5");
			String cas = (new Long(System.currentTimeMillis())).toString();
			String nahodneCislo = (new Long(random.nextLong())).toString();
			String nonce = cas + nahodneCislo;
			return spravHex(sifrovanie.digest(nonce.getBytes()));
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		};
		return null;
	}

	private char[]	HEX_STRING	= {'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f'};

	protected String spravHex(byte[] data) {
		int index = 0;
		char[] novedata = new char[data.length * 2];

		for (int pozicia = 0; pozicia < data.length; pozicia++) {
			novedata[index++] = HEX_STRING[(data[pozicia] >> 4) & 0x0F];
			novedata[index++] = HEX_STRING[data[pozicia] & 0x0f];
		}

		return new String(novedata);
	}

	private void UnAuthorized(Request req) {
		try {
			Response response = messageFactory.createResponse(Response.UNAUTHORIZED, req);
			WWWAuthenticateHeader www = headerFactory.createWWWAuthenticateHeader("Digest");
			www.setParameter("realm", m_realm);
			www.setParameter("nonce", generateNonce());
			www.setParameter("opaque", "");
			www.setParameter("stale", "FALSE");
			www.setParameter("algorithm", "MD5");
			response.setHeader(www);

			log(response, "Posielam:");
			sipProvider.sendResponse(response);
		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		catch (SipException e2) {
			e2.printStackTrace();
		}
	}

	public void processTimeout(TimeoutEvent evt) {
		System.out.println("Spravu sa nepodarilo odoslat: " + evt.getTimeout());
	}
	public void processIOException(IOExceptionEvent evt) {
		System.out.println("Spravu sa nepodarilo odoslat: " + evt.getHost());
	}
	public void processDialogTerminated(DialogTerminatedEvent evt) {
		System.out.println("DialogTerminated");
		System.out.println(evt.getDialog().toString());
	}
	public void processTransactionTerminated(TransactionTerminatedEvent evt) {
		System.out.println("TransactionTerminated");
		System.out.println(evt.toString());
	}
}
