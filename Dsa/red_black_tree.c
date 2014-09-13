#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define ALLOC (List*) malloc(sizeof(List))
#define IS_RED(x) (x != NULL && x->color == 'R') // Ak je NULL ide o List a ten je cierny z definicie
#define LEFT 0
#define RIGHT 1
#define BLACK 'B'
#define RED 'R'

/* Zaneprazdneny minister implementation
	@zadanie: 2
	@autor: Lukas Sekerak
*/

/*+ Hlavna struktura
	- Predstavuje list stromu
	- Obsahuje udaje o stretnuti OD-DO a ID stretnutia
	- Odkazi na deti
*/
struct _List {
	// Cutom data
	int a;
	int b;
	int id;
	char color;

	// Odkazi
	struct _List *child[2];
	struct _List *parent;
};
typedef struct _List List;

// Hlavny koren
List* gGlobal;
int gResult;
List* gTempInsert;

List* getsurodenec(List* x) {
	List* dad = x->parent;
	if( x == dad->left) {
		return dad->right;
	} else {
		return dad->left;
	}
}



/*+ Insert
	- Rekurzivne prehladavaj strom
	- Ak najdes miesto kde ho ma vlozit, tak ho vloz
*/
void insertfix(List* parent, int dir) {
	if(parent[dir]->color == RED) {
		parent[dir]->color = BLACK;
		parent[!dir]->color = BLACK;

		// FIX ked je to koren nema uz parenta
		if(parent->parent == NULL) {
			parent->color = BLACK;
		} else if ( parent->parent == RED) {
			insertfix(item->parent->parent);
		}
	}
}
void insert_create(List* parent, int dir)
{
	// Vloz subor
	parent[dir] = gTempInsert;

	// Je strom nevyvazeny ?
	if(!IS_RED(parent)) return;
		
	// 2 typy chyb
	List* surodenec = parent[!dir];
	if(IS_RED(surodenec)) { 
		// A
		insertfix(parent, !dir);
	} else {
		// X je opacnym smerom od Y ako Y od Z
		int smer = (parent->child[LEFT] == parent); // je na lavej strane ?
		if(smer == dir) {
			// B - 1
		} else {
			// B - 2
		}
	}
}
void insert_find_r(List* parent, int dir)
{
	if (parent[dir] == NULL) {
		insert_create(parent, dir);
		return;
	} 
	int strana = (gTempInsert->id > parent[dir]->id >id);
	insert2(parent, strana);
}
void insert(int a, int b, int id) 
{  
	gTempInsert = ALLOC;
	gTempInsert->child[RIGHT] = NULL;
	gTempInsert->child[LEFT] = NULL;
	gTempInsert->a = a;
	gTempInsert->b = b;
	gTempInsert->id = id;
	gTempInsert->color = RED;
	gTempInsert->parent = NULL;

	// Ak ide o koren - ten je trocha specialny
	if(gGlobal == NULL) {
		gGlobal = gTempInsert;
		gGlobal->color = BLACK;
	} else {
		// Inak je to klasicka struktura
		int strana = (gTempInsert->id > gGlobal->id > id);
		insert2(gGlobal, strana);
	}
}


/*+ Delete
	- Rekurzivne prehladavaj strom
	- Ak najdes dany prvok, vymaze ho
	- Zabezspecuje aj vymazanie korena stromu
	- Posielame si parenta, rodica
*/
void vymaz2(int x, List* item, List* parent) {
	if (item == NULL) return;
	
	// Nasle sme prvok ktory mame vymazat
	if (x == item->id) {
		// Ide o koren
		if(parent == NULL) {
			free(gGlobal);
			gGlobal = NULL;

		} else {
		// Ide o normalny list

		}
		return;
	}

	// Pokracuj dalej
	if ( x > item->id) {
		vymaz2(x, item->right, item);
	} else {
		vymaz2(x, item->left, item);
	}
}
void vymaz(int x) {
	vymaz2(x, gGlobal, NULL);
}


/*+ Findinterval
	- Rekurzivne prehladava strom
	- Hlada hodnoty ktore vyhovuju podmienke
*/
void findinterval2(int a, int b, List* item)
{
	if (item == NULL) return;
	if(
		(item->a >= a && item->a <= b) 
		||
		(item->b >= a && item->b <= b) 
		||
		(item->a <= a && item->b >= b) 
	) {
		gResult = 1;
		printf("%d ", item->id);
	}

	findinterval2(a, b, item->left);
	findinterval2(a, b, item->right);
}
void findinterval(int a, int b){
	gResult = 0;
	findinterval2(a, b, gGlobal);
	if(gResult == 0) {
		printf("-");
	}
	printf("\n");
}



/*+ Findpoint
	- Rekurzivne prehladava strom
	- Hlada hodnoty ktore vyhovuju podmienke
*/
void findpoint2(int x, List* item)
{
	if (item == NULL) return;
	if(x >= item->a && x <= item->b) {
		gResult = 1;
		printf("%d ", item->id);
	}

	findpoint2(x, item->left);
	findpoint2(x, item->right);
}
void findpoint(int x) {
	gResult = 0;
	findpoint2(x, gGlobal);
	if(gResult == 0) {
		printf("-");
	}
	printf("\n");
}

/*+ Lava a Prava rotacia
	- Fixuje nevyvazenost stromu
	- Je potrebna pri insert a vymaz operaciach
*/
void left_rotation()
{

}


int rbt_test(List* root )
{
	int lh, rh;
	if ( root == NULL ) {
		return 1;
	} else {
	List *ln = root->link[0];
	List *rn = root->link[1];

     /* Consecutive red links */
     if ( IS_RED ( root ) ) {
       if ( IS_RED ( ln ) || IS_RED ( rn ) ) {
         printf ( "Red violation" );
         return 0;
       }
     }
 
     lh = rbt_test ( ln );
     rh = rbt_test ( rn );
 
     /* Invalid binary search tree */
     if ( ( ln != NULL && ln->id >= root->id)
       || ( rn != NULL && rn->id <= root->id ) ) {
       printf ( "Binary tree violation" );
       return 0;
     }
 
     /* Black height mismatch */
     if ( lh != 0 && rh != 0 && lh != rh ) {
       printf ( "Black violation" );
       return 0;
     }
 
     /* Only count black links */
     if ( lh != 0 && rh != 0 )
       return IS_RED ( root ) ? lh : lh + 1;
     else
		return 0;
	}
}

/*+ Parser
	- Nacitava prikazi a deli ich
	- Ziskane cisla posiela do konkretnych funkcii
*/
void parser()
{
	char prikaz[100];
	int p[3];
	FILE *f;
	//f = stdin;
	f = fopen("subor.txt", "r");

	while(1) {
		fscanf(f, "%s", prikaz);
		if( prikaz == NULL || feof(f) || prikaz[0] == 'Q') {
			// Fix bug ked sa posledny prikaz spusti 2x
			return;
		}

		if(prikaz[0] == 'I') {							// INSERT
			fscanf(f, "%d", &p[0]);
			fscanf(f, "%d", &p[1]);
			fscanf(f, "%d", &p[2]);
			insert(p[0], p[1], p[2]);

		} else if(prikaz[0] == 'F' && prikaz[4] == 'I') { // FINDINTERVAL
			fscanf(f, "%d", &p[0]);
			fscanf(f, "%d", &p[1]);
			fscanf(f, "%d", &p[2]);
			findinterval(p[0], p[1]);

		} else if(prikaz[0] == 'F' && prikaz[4] == 'P') { // FINDPOINT
			fscanf(f, "%d", &p[0]);
			findpoint(p[0]);

		} else if(prikaz[0] == 'D') {					// DELETE
			fscanf(f, "%d", &p[0]);
			vymaz(p[0]);

		} else {
			printf("Error");
		}
	}
}

/*+ Hlavny program
	- Priprav koren
	- SPusti parser
*/
int main()
{
	gGlobal = 0;	
	parser();
	return 0;
}