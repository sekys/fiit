
/**
 * Pomocna trieda pre rychle debugovanie.
 * Meria cas a spotrebovanu pamet medzi 2 bodmy.
 * 
 * @author Seky
 * 
 */
public class Debug
{
	protected long		mem;
	protected double	time;

	public Debug() {
		mem = Runtime.getRuntime().freeMemory();
		time = System.currentTimeMillis();
	}

	public void End(String name) {
		time = (System.currentTimeMillis() - time) / 1000.0;
		mem = (mem - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
		System.out.println(name + " za " + time + "sec " + mem + "mb");
	}
}

