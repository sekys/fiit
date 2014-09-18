import java.io.InputStreamReader;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;

public class AI
{
	protected int				size_x;
	protected int				size_y;
	protected Stav				root;

	protected LinkedList<Stav>		todoStavy;
	protected HashSet<Stav>		grafSpracovany;
	protected GreedyComparator	greedy;
	protected Heuristic			heuristic;
	protected Debug				debug;
	protected int				steps;

	protected int				velkost;
	protected int				size_x_m1;
	protected int				velkost2;

	public AI(Stav root, byte w, byte h) {
		size_x = w;
		size_y = h;
		this.root = root;

		velkost = size_x * size_y;
		size_x_m1 = size_x - 1;
		velkost2 = velkost - size_x;
		Stav.WIDTH_FORMAT = w;
		
		greedy = new GreedyComparator();
		todoStavy = new LinkedList<Stav>();
		grafSpracovany = new HashSet<Stav>();
	}

	/**
	 * Metoda len nacita vstup, a spusti prehladavanie..
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(new InputStreamReader(System.in));
		try {
			byte w, h, i, typ;
			w = in.nextByte();
			h = in.nextByte();
			typ = in.nextByte();
			byte[] begin = new byte[w * h];
			byte[] target = new byte[w * h];

			for (i = 0; i < begin.length; i++) {
				begin[i] = in.nextByte();
			}
			for (i = 0; i < target.length; i++) {
				target[i] = in.nextByte();
			}

			AI main = new AI(new Stav(begin), w, h);
			Stav result;
			if (typ == 0) {
				result = main.findSolution(new PocetPolicokHeuristic(new Stav(target)));
			} else {
				result = main.findSolution(new SucetVzdialenostiHeuristic(new Stav(target), h));
			}

			// Vypis cestu od startu k cielovemu stavu
			if (result == null) {
				System.out.println("cesta nenajdena");
			} else {
				result.dumpPath();
			}
		}
		catch (NoSuchElementException ne) {
			ne.printStackTrace();
		}
	}

	/**
	 * Metoda aplikuje vsetke mozne operatory na dany stav.
	 * Vysledkom je zoznam novych moznych stavov.
	 * Tento zoznam je uz vyfiltrovany o vrcholy, ktore sme raz mali
	 * Kazdy prvok zoznamu sa ohodnoti heurestickou funckiou.
	 * 
	 * @param root
	 * @return
	 */
	protected List<Stav> applyOperators(Stav root) {
		int medzera, medzeraPredchadzajuca, novaMedzera;
		ArrayList<Stav> dalsieStavy = new ArrayList<Stav>(5);
		medzera = root.getMedzera();

		// Pomocka so starou medzerou
		if (root.getPrevious() != null) {
			medzeraPredchadzajuca = root.getPrevious().getMedzera();
		} else {
			medzeraPredchadzajuca = -1; // aby nikdy nenastalo
		}

		// Poposuvaj vsetko roznymi smermy do lava, do prava, hore, dole
		novaMedzera = medzera - 1;
		if ((medzera % size_x) > 0 && medzeraPredchadzajuca != novaMedzera) { // && medzera > 0
			addToListCheck(new Stav(root, novaMedzera), dalsieStavy);
		}

		novaMedzera = medzera + 1;
		if (medzera % size_x != size_x_m1 && medzera != novaMedzera) {
			addToListCheck(new Stav(root, novaMedzera), dalsieStavy);
		}

		novaMedzera = medzera - size_x;
		if (medzera >= size_x && medzera != novaMedzera) {
			addToListCheck(new Stav(root, novaMedzera), dalsieStavy);
		}

		novaMedzera = medzera + size_x;
		if (medzera < velkost2 && medzeraPredchadzajuca != novaMedzera) {
			addToListCheck(new Stav(root, novaMedzera), dalsieStavy);
		}

		return dalsieStavy;
	}

	private void addToListCheck(Stav check, AbstractCollection<Stav> list) {
		check.setHeuresticValue(heuristic.compareTo(check));
		if (!grafSpracovany.add(check)) return;
		list.add(check);
	}

	/**
	 * Sluzi na zoradenie prvkov, podla heurestickej hodnoty.
	 * 
	 * @author Seky
	 */
	protected class GreedyComparator implements Comparator<Stav>
	{
		@Override
		public int compare(Stav o1, Stav o2) {
			// 0 znamena ze prvok sa nevymeni - menej vymen tak je to rychlejsie
			return o1.getHeuresticValue() < o2.getHeuresticValue() ? -1 : 0;
		}
	}

	protected int metrosteps = 0;
	/**
	 * Prehladavanie stavoveho priestoru.
	 * 
	 * @param heuristic
	 * @return posledny stav
	 */
	public Stav findSolution(Heuristic heuristic) {
		todoStavy.clear();
		grafSpracovany.clear();
		this.heuristic = heuristic;
		todoStavy.add(root);
		addToListCheck(root, grafSpracovany);
		steps = 0;
		debug = new Debug();
		
		// Prehladavame postupne stavy zo zasobniku
		while (!todoStavy.isEmpty()) {
			Stav trebaOverit = todoStavy.poll();
			steps++;

			// Nasli sme cielovy vrchol ?
			if (trebaOverit.getHeuresticValue() == 0) {
				printStats();
				return trebaOverit;
			}

			// Musime hladat iny vrchol - najdi dalsie stavy, vyfiltruj ich na mozne stavy a zorad
			// ich podla ohodnotenia heurestickou funkciou
			List<Stav> dalsieMozneStavy = applyOperators(trebaOverit);
			if(!METRO || metrop()) {
				Collections.sort(dalsieMozneStavy, greedy); // najefektivnejsi sort
			} else {
				Collections.shuffle(dalsieMozneStavy);
				metrosteps++;
			}
			temperature++;
			for(Stav v : dalsieMozneStavy) {
				todoStavy.addFirst(v);
			}
		}
		printStats();
		return null;
	}

	protected int temperature = 0;
	protected boolean METRO = true;
	
	protected boolean metrop() {
		 return ((int) (Math.random() * 10000)) < temperature;
	}
	
	protected void printStats() {
		int zasobnik = todoStavy.size();
		int spracovane = grafSpracovany.size();
		debug.End("Spracovane za");
		System.out.println("Krokov: " + Integer.toString(steps));
		System.out.println("Vytvoreny stavovy priestor: "
				+ Integer.toString(spracovane + zasobnik));
		System.out.println("Pocet v rade: " + Integer.toString(zasobnik));
		System.out.println("Spracovanych: " + Integer.toString(spracovane));
		System.out.println("Metro krokov: " + Integer.toString(metrosteps));
	}
}
