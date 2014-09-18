package siet;

/**
 * Vlastna vynimka - vnutorna trieda. Nastane vtedy ked zariadenie ma plno
 * pripojeni.
 * 
 */
public class MaxPocetPripojeni extends Exception {
	public MaxPocetPripojeni() {
		super("MaxPocetPripojeni");
	}
}
