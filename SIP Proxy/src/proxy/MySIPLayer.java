
package proxy;

import gov.nist.javax.sip.address.SipUri;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.swing.JPanel;


public class MySIPLayer extends BaseSIPLayer
{
	private Database	database;

	public MySIPLayer(Database database,
			String ip,
			int port,
			String realm,
			JPanel panel) {

		super(ip, port, realm, panel);
		this.database = database;
	}

	protected void log(String sprava) {
		database.log(sprava);
	}

	private String getUserID(Request request) {
		ToHeader toHlavicka = (ToHeader) request.getHeader(ToHeader.NAME);
		if(toHlavicka != null) {			
			URI cistaURI, uri;
			cistaURI = uri = toHlavicka.getAddress().getURI();
			if(uri == null) return null;
			if(uri.isSipURI()) {
				SipUri sipURI = (SipUri) uri.clone();
				sipURI.clearUriParms();
				cistaURI = sipURI;
			};

			if (cistaURI.isSipURI()) {
				SipUri SIPcistaURI = (SipUri) cistaURI;
				SIPcistaURI.clearPassword();
				SIPcistaURI.removePort();
				SIPcistaURI.clearQheaders();
				return SIPcistaURI.toString();
			}			
		}

		return null;
	}

	protected boolean checkAuthorization(Request request,
			AuthorizationHeader auth) {
		String idUzivatela = getUserID(request);
		Member uzivatel = database.getUser(idUzivatela);
		if (uzivatel == null) return false; // nenajdeny
		String username_2 = auth.getUsername();

		// Skontroluj rovnake uzivatelske mena
		if (!uzivatel.name.equals(username_2)) return false;
		return doAuthenticate(request, auth, uzivatel);
	}

	protected boolean doAuthenticate(Request request, AuthorizationHeader auth,
			Member uzivatel) {
		byte data[];
		URI uri = auth.getURI();
		if (uri == null) return false;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			String A1 = uzivatel.name + ":" + auth.getRealm() + ":"
					+ uzivatel.password;
			String sifraA1 = spravHex( messageDigest.digest(A1.getBytes()) );

			String A2 = request.getMethod().toUpperCase() + ":"
					+ uri.toString();
			String sifraA2 = spravHex( messageDigest.digest(A2.getBytes()) );

			String KD = sifraA1 + ":" + auth.getNonce();
			if (auth.getCNonce() != null) KD += ":" + auth.getCNonce();
			KD += ":" + sifraA2;

			String mojaSifra = spravHex( messageDigest.digest(KD.getBytes()) );
			return mojaSifra.compareTo(auth.getResponse()) == 0;
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void UkonciHovori() {
		System.out.println("ukoncujem hovori");
	}
}
