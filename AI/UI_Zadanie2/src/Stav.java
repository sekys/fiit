import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Reprezentacia stavu.
 * 
 * @author Seky
 */
public class Stav
{
	private byte[]		data;					// data v danom stave - postupnost cisel
	private Stav		previous;				// odkaz na predchadzajuci stav
	private int			heuresticValue;
	private int			hashValue;
	private byte		medzera;

	public static int	WIDTH_FORMAT	= 3;

	/**
	 * Pouziva sa pre pociatocny a konecny stav
	 * 
	 * @param data
	 * @param medzera
	 */
	public Stav(byte[] data) {
		this.data = data;
		previous = null;
		prepareValues();

		for (byte x = 0; x < data.length; x++) {
			if (data[x] == 0) {
				medzera = x;
				return;
			}
		}
		throw new InputMismatchException();
	}

	/**
	 * Pouziva sa pre vytvaranie stavov, kde pozname predchadzajuci stav a poziciu novej medzery
	 * 
	 * @param p
	 * @param dajMedzeruIndex
	 * @param medzeraIndex
	 */
	public Stav(Stav p, int dajMedzeruIndex) {
		previous = p;

		// Vygeneruj nove data
		data = new byte[p.data.length];
		// //data = Arrays.copyOf(p.data, p.data.length);
		System.arraycopy(p.data, 0, data, 0, p.data.length);
		data[p.medzera] = data[dajMedzeruIndex];
		data[dajMedzeruIndex] = 0;
		medzera = (byte) dajMedzeruIndex;
		prepareValues();
	}

	private void prepareValues() {
		heuresticValue = -1;
		hashValue = 0; // Arrays.hashCode(data);
		for (int i = 0; i < data.length; i++) {
			hashValue += i * data[i];
		}
	}
	/**
	 * Formatovany vypis hodnot stavu.
	 */
	public String toString() {
		String out = new String(new StringBuffer(data.length * 2));
		for (int x = 0; x < data.length; x++) {
			if (x % WIDTH_FORMAT == 0) out += "\n";
			out += String.format("%d ", data[x]);
		}
		return out;
		// return data.toString();
	}

	public byte getMedzera() {
		return medzera;
	}

	private void dumpPathRec(Stav stav, int pocet) {
		if (stav == null) {
			System.out.println("Hlbka: " + Integer.toString(pocet));
			return;
		}
		dumpPathRec(stav.getPrevious(), pocet + 1);
		System.out.println(stav);
	}

	public void dumpPath() {
		dumpPathRec(this, 0);
	}

	public boolean equals(Object o) {
		// if (!(o instanceof Stav)) return false;
		Stav o2 = (Stav) o;
		if (o2.medzera != medzera) return false;
		if (o2.hashValue != hashValue) return false;
		return Arrays.equals(this.data, o2.data);
	}

	public int hashCode() {
		return hashValue;
		// return heuresticValue;
	}

	public byte[] getData() {
		return data;
	}

	public Stav getPrevious() {
		return previous;
	}

	public void setHeuresticValue(int x) {
		heuresticValue = x;
	}
	public int getHeuresticValue() {
		return heuresticValue;
	}
}
