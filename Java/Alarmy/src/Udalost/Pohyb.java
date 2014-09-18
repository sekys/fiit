package Udalost;

/**
 * Pri tejto udalosti dojde k pohybu.
 */
public class Pohyb implements InterfaceUdalost
{
	@Override
	public int getID() {
		return 2;
	}

	@Override
	public String pomenovanieUdalosti() {
		return "Pohyb";
	}

}
