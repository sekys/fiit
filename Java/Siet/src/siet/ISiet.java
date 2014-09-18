package siet;

import java.util.List;

/**
 * Interface sluzi na prepojenie zariadenia s editorom. Pretoze, editor obsahuje
 * informacie o susedov (ako siet) kazdeho zariadenia. A zariadenie chce poznat
 * svojich pripojenych susedov. Taketo riesenie je najlepsie cez interface.
 * 
 * 
 */
public interface ISiet {
	public List<SietoveZariadenie> getZoznamPripojenychZariadeni(
			SietoveZariadenie v);

	public void posli(SietoveZariadenie a, SietoveZariadenie b, Paket p);
}
