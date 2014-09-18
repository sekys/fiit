
package moje;

public class Hrabanie
{
	public static PovodnaMapa	povodna;
	public Zahrada				moja;
	public Zen					zen;
	private int					aktualnePohrabal;

	public Hrabanie(Zen zen) {
		moja = povodna.clone();
		this.zen = zen;
		zen.pocet_krokov = 0;
	}

	public boolean hrabaj(int indexOkraja) {
		// Taahj zena po policku
		aktualnePohrabal = 0;
		if (!tahaj(indexOkraja)) {
			zen.pohrabanych += aktualnePohrabal;
			return false;
		}
		zen.pohrabanych += aktualnePohrabal;

		// Vypis aktualny progress hrabania
		/*System.out.println("\n");
		moja.vypis();
		System.out.println("\n");*/

		// Zakreslil som vsetko ? skonci - vitaz
		if (povodna.prazdnePolicka == zen.pohrabanych) {
			return false;
		}
		return true;
	}

	private boolean tahaj(int indexOkraja) {
		// Ktorym smerom, to urcime podla indexu ...
		int stara_pozicia, pozicia;
		pozicia = povodna.i2pos.get(indexOkraja);
		if (!moja.jeOkraj(pozicia)) System.out.println("toto nieje okraj !!!");

		if (moja.jeObsadene(pozicia)) {
			System.out.println("dany index uz bol spracovany");
			return true;
		}

		zen.pocet_krokov++;
		zakresli(pozicia);

		// Zisti ktorym smerom sa ma pohybovat a tym ho smeruj
		Zahrada.Pohyb pohyb = povodna.getPohyb(indexOkraja);
		while (true) {
			stara_pozicia = pozicia;
			pozicia = moja.pohyb(pohyb, pozicia);

			// Zen narazil na prekazku
			if (moja.jeObsadene(pozicia)) {
				pohyb = najdiAlternativnyPohyb(stara_pozicia);
				if (pohyb == null) {
					// Zen mozno je na okraji obrazovky a moze odstupit mimo obrazovky
					if(moja.jeOkraj(stara_pozicia) && aktualnePohrabal > 1) {
						break;
					}
					return false;
				}
				zen.pocet_krokov++;
				pozicia = stara_pozicia;
				continue;
			}

			zakresli(pozicia);
			if(moja.jeOkraj(pozicia, pohyb)) break;
		}
		return true;
	}

	private void zakresli(int pozicia) {
		moja.zahrada[pozicia] = zen.pocet_krokov; // zapiseme tam cislo
		aktualnePohrabal++;
	}

	// somZaseknutyKtorymSmeromMozemIst
	public Zahrada.Pohyb najdiAlternativnyPohyb(int pozicia) {
		int x;

		x = moja.pohyb(Zahrada.Pohyb.VLAVO, pozicia);
		if (!moja.jeZaciatokRiadka(pozicia) && !moja.jeObsadene(x)) return Zahrada.Pohyb.VLAVO;
		x = moja.pohyb(Zahrada.Pohyb.VPRAVO, pozicia);
		if (!moja.jeKoniecRiadka(pozicia) && !moja.jeObsadene(x)) return Zahrada.Pohyb.VPRAVO;
		x = moja.pohyb(Zahrada.Pohyb.HORE, pozicia);
		if (!moja.jeZaciatokStlpca(pozicia) && !moja.jeObsadene(x)) return Zahrada.Pohyb.HORE;
		x = moja.pohyb(Zahrada.Pohyb.DOLE, pozicia);
		if (!moja.jeKoniecStlpca(pozicia) && !moja.jeObsadene(x)) return Zahrada.Pohyb.DOLE;

		// nemam sa kam otocit - je koniec hry
		return null;
	}

	public static void main(String[] args) {
		PovodnaMapa mapa = new PovodnaMapa();
		mapa.Nacitaj();
		mapa.vypis();

		Hrabanie.povodna = mapa;
		Hrabanie h = new Hrabanie(new Zen());

		// * *
		// * P
		// h.moja.zahrada[118] = 5;
		// h.moja.zahrada[107] = 5;
		// h.hrabaj(23);

	}
}
