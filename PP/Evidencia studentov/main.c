#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const char NAZOV_SUBORU[] = "vstup.txt";

// Structure ako pomocka
// meno, priezvisko, roËnÌk, odbor, evidenËnÈ ËÌslo.
struct Student
{
	char meno[100]; // statick˙ veækosù.
	char priezvisko[100];
	int rocnik;
	int odbor;
	int evidencne;
};

struct Student** zoznamStudentov; // dynamickÈ pole smernÌkov
int studentovPameti = 0; // kolko paameti mas naalokovane
int studentov = 0; // pocet studentov v poli


void uvolniDynamickuPamet() {
	int i;
	if(zoznamStudentov == NULL) return;
	for(i=0; i < studentov; i++) {
		if(zoznamStudentov[i] != NULL) {
			free(zoznamStudentov[i]);
		}
	}
	studentovPameti = 0;
	studentov = 0;
	free(zoznamStudentov);
	zoznamStudentov = NULL;
}

struct Student* nacitajStudenta(FILE* f) {
	struct Student* student = (struct Student*) malloc(1 * sizeof(struct Student));
	printf("Zadaj meno:\n");
	fscanf(f, "%90s", &student->meno);
	printf("Zadaj priezvisko:\n");
	fscanf(f, "%90s", &student->priezvisko);
	printf("Zadaj rocnik:\n");
	fscanf(f, "%d", &student->rocnik);
	printf("Zadaj odbor:\n");
	fscanf(f, "%d", &student->odbor);
	printf("Zadaj evidencne:\n");
	fscanf(f, "%d", &student->evidencne);
	printf("Pridaj !!\n");
	return student;
}
void nacitajZoSuboru() {
	FILE * f;
	struct Student* student;
	int pomocna;

	f = fopen(NAZOV_SUBORU, "r");
	if(f == NULL) {
		printf("Subor nenajdeny !\n");
		return;
	}
	uvolniDynamickuPamet();
	if(studentovPameti == 0) {
		zoznamStudentov = (struct Student**) malloc(1 * sizeof(struct Student*));
		studentovPameti = 1;
	}

	while(1) 
	{
		if(studentovPameti == studentov) {
			zoznamStudentov =  (struct Student**) realloc(zoznamStudentov, studentovPameti * 2 * sizeof(struct Student**));
			studentovPameti = 2 *studentovPameti;
		}

		// Dana funkcia vracia -1 ak je koniec suboru alebo nastala chyba
		student = (struct Student*) malloc(1 * sizeof(struct Student));
		pomocna = fscanf(f, "%90s\n", &student->meno);
		if(pomocna == EOF) {
			free(student);
			break;
		}
		fscanf(f, "%90s\n", &student->priezvisko);
		fscanf(f, "%d\n", &student->rocnik);
		fscanf(f, "%d\n", &student->odbor);		
		fscanf(f, "%d\n", &student->evidencne);
		zoznamStudentov[studentov] = student;
		studentov++;
	}
	fclose(f);
}

void pridajStudenta() {
	if(studentovPameti == 0) {
		zoznamStudentov = (struct Student**) malloc(1 * sizeof(struct Student*));
		studentovPameti = 1;
	}

	if(studentovPameti == studentov) {
		zoznamStudentov =  (struct Student**) realloc(zoznamStudentov, studentovPameti * 2 * sizeof(struct Student**));
		studentovPameti = 2*studentovPameti;
	}

	zoznamStudentov[studentov] = nacitajStudenta(stdin);
	studentov++;
}

void poposuvajStudentov(int start) {
	int i;
	for(i = start; i<studentov; i++) {
		zoznamStudentov[i] = zoznamStudentov[i + 1];
	}
}

void odstranStudenta() {
	char meno[100];
	int i;
	struct Student* s ;

	if(zoznamStudentov == NULL) return;
	printf("Zadaj meno studenta:\n");
	scanf("%90s\n", &meno);
	for(i = 0; i < studentov; i++) {
		s = zoznamStudentov[i];
		if( strcmp(s->meno, meno) == 0 ) {
			poposuvajStudentov(i);
			zoznamStudentov[studentov-1] = NULL;
			studentov--;
			return;
		}
	}

	printf("Student nenajdeny !\n");
}

void usporiadajPodlaPriezviska() {
	int i, j;
	struct Student* temp;
	if(zoznamStudentov == NULL) return;
	for(i=0; i<studentov; i++) {
		for(j=i+1; j < studentov; j++) {
			if( strcmp( zoznamStudentov[i]->priezvisko, zoznamStudentov[j]->priezvisko) > 0) {
				temp = zoznamStudentov[i];
				zoznamStudentov[i] = zoznamStudentov[j];
				zoznamStudentov[j] = temp;
			}
		}
	}
}

void vypisStudenta(struct Student* student) {
	printf("meno: %.30s\n", student->meno);
	printf("priezvisko: %.30s\n", student->priezvisko);
	printf("rocnik: %d\n", student->rocnik);
	printf("odbor: %d\n", student->odbor);
	printf("evidencne: %d\n", student->evidencne);
	printf("\n");
}

void vyhladajPodlaEvidecneho() {
	struct Student* s;
	int evidencne;
	int i;

	if(zoznamStudentov == NULL) return;
	printf("Zadaj evidencne cislo:\n");
	scanf("%d", &evidencne);
	for(i = 0; i < studentov; i++) {
		s = zoznamStudentov[i];
		if(s->evidencne == evidencne ) {
			 vypisStudenta(s);
			return;
		}
	}

	printf("Student nenajdeny !\n");
}

void vypisnaObrazovku() {
	int i;
	if(zoznamStudentov == NULL) return;
	for(i = 0; i < studentov; i++) {
		vypisStudenta(zoznamStudentov[i]);	
	}
}

int main() {
	char C;
	zoznamStudentov = NULL;

			    printf("            MENU\n\n"
               "N. Nacitaj zoznam\n"
               "P. Pridanie noveho studenta\n"
               "O. Odstranenie studenta\n"
               "E. Vyhladanie podla evidencneho cisla\n"
               "V. Vypis studentov\n"
               "U. Usporiadanie podla priezviska\n"
               "K. Koniec\n\n");
	        printf("Vyberte si: ");

	while(1) {
		scanf("%c", &C);		
		switch(C) { // Parse
			case 'P': { pridajStudenta(); break; }
			case 'O': { odstranStudenta(); break; }
			case 'U': { usporiadajPodlaPriezviska(); break; }
			case 'E': { vyhladajPodlaEvidecneho(); break; }
			case 'V': { vypisnaObrazovku(); break; }
			case 'N': { nacitajZoSuboru(); break; }
			case 'K': { 
				uvolniDynamickuPamet();
				return 0; 
			}
			default: {

			}
		};
	}
	return 0;
}
