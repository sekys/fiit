package Udalost;

/**
 * Ide o udalost pri ktorej vznikne poziar
 */
public class Poziar implements InterfaceUdalost
{

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public String pomenovanieUdalosti() {
		return "Poziar";
	}

}
