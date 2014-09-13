#include <stdio.h>
#include <stdlib.h>
#include <math.h> 

struct CPLX { // komplexne cislo z = a + b * i
	double re; // realna cast
	double im; // imagirnarna cast
};

struct CPLX add(struct CPLX a, struct CPLX b) {
	struct CPLX nova;
	nova.re = a.re + b.re;
	nova.im = a.im + b.im;
	return nova;
}

struct CPLX sub(struct CPLX a, struct CPLX b) {
	struct CPLX nova;
	nova.re = a.re - b.re;
	nova.im = a.im - b.im;
	return nova;
}

struct CPLX mult(struct CPLX a, struct CPLX b) {
	struct CPLX nova;
	nova.re = a.re * b.re - a.im * b.im;
	nova.im = a.re * b.im + a.im * b.re;
	return nova;
}

struct CPLX divis(struct CPLX a, struct CPLX b) {
	struct CPLX nova;
	nova.re = (a.re * b.re + a.im * b.im) / (b.re * b.re + b.im * b.im);
	nova.im = (a.im * b.re - a.re * b.im) / (b.re * b.re + b.im * b.im);
	return nova;
}

// Podla zadania toto ti ma vypocitat absolutnu hodnotu 
//  z = a + b * i
// ABS(z) = odmocnina (a*a + b*b)
double complex_n_modul(struct CPLX z) {
	return sqrt(z.re * z.re + z.im * z.im);
};

struct CPLX vypocitaj(char znamienko, struct CPLX a, struct CPLX b) {
	switch(znamienko) {
	case '*': return mult(a, b);
	case '/': return divis(a, b);
	case '+': return add(a, b);
	case '-': return sub(a, b);
	default: {
			 }
	}
}

int main() {
	// Nazvy suborov s ktorymi pracujeme
	const char* vstupny_subor = "vstup_3_komplex_cisla_operatory.txt";
	const char* vystupny_subor = "vystup_3_komplex_cisla_operatory.txt";

	FILE* fop;
	FILE* vyst;

	struct CPLX z1, z2, z3, vysledneKomplexneCislo, medziVysledok;
	char operator1, operator2;	
	int koniecSuboru;
	double absolutnaHodnota;

	// Otvorime subory
	fop = fopen(vstupny_subor, "r");
	if(fop == NULL) {
		printf("Subor sa nepodarilo otvorit\n");
		return 1;
	}
	vyst = fopen(vystupny_subor, "w");
	if(vyst == NULL) {
		printf("Subor sa nepodarilo otvorit\n");
		return 1;
	}

	// Teraz prechadzaj subor po riadkov
	while(1) {
		// Nacitaj cisla
		koniecSuboru = fscanf(fop, "%7lf + i %7lf", &z1.re, &z1.im);

		// Proces opakuj kym nie je koniec suboru
		if(koniecSuboru == EOF) break;

		fscanf(fop, "\t%c\t", &operator1);
		// %7lf znamena ze cislo ma maximalnu dlzku 7, teda -4.1234 precita
		fscanf(fop, "%7lf + i %7lf", &z2.re, &z2.im); 
		fscanf(fop, "\t%c\t", &operator2);
		fscanf(fop, "%7lf + i %7lf\n", &z3.re, &z3.im);

		// Teraz zisti prioritu operacii a podla toho to vykonaj
		if(operator1 == '*') { // dalej operator2 moze byt hocico
			medziVysledok = mult(z1, z2);
			vysledneKomplexneCislo = vypocitaj(operator2, medziVysledok, z3);
		} else if(operator2 == '*') { // dalej operator1 moze byt hocico, toto ma vacsiu prioritu
			medziVysledok = mult(z2, z3);
			vysledneKomplexneCislo = vypocitaj(operator1, z1, medziVysledok);
		} else if(operator1 == '/') { // operator 2 uz je len + alebo - takze to je ejdno
			medziVysledok = divis(z1, z2);
			vysledneKomplexneCislo = vypocitaj(operator2, medziVysledok, z3);
		} else if(operator2 == '/') { // operator 1 je uz len + alebo - takze to je ejdno
			medziVysledok = divis(z2, z3);
			vysledneKomplexneCislo = vypocitaj(operator1, z1, medziVysledok);
		} else {
			// obidva operatory su bud + alebo - takze to je jedno v akom poradi sa spustia
			medziVysledok = vypocitaj(operator1, z1, z2);
			vysledneKomplexneCislo = vypocitaj(operator2, medziVysledok, z3);
		}

		// Zapis vysledky do suboru
		// %.6lf znamena ze vypise cislo a maximalne 6 cisel za desatinnou ciarkou
		fprintf(vyst, "(%.6lf + i %.6lf)", z1.re, z1.im);
		fprintf(vyst, " %c ", operator1);
		fprintf(vyst, "(%.6lf + i %.6lf)", z2.re, z2.im);
		fprintf(vyst, " %c ", operator2);
		fprintf(vyst, "(%.6lf + i %.6lf)", z3.re, z3.im);
		fprintf(vyst, " = %.6lf + i %.6lf\n", vysledneKomplexneCislo.re, vysledneKomplexneCislo.im);

		absolutnaHodnota = complex_n_modul(vysledneKomplexneCislo);
		fprintf(vyst, "\t\t\t\t modul(absolutna hodnota) vysledku\t %lf\n", absolutnaHodnota);
	}

	// Zatvor subory
	fclose(fop);
	fclose(vyst);
	return 0;
}