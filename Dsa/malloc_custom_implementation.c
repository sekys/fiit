#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/* MALLOC implementation
	@zadanie: 1
	@autor: Lukas Sekerak
*/

#define UINT unsigned int
#define MIN_PAMET sizeof(char) // min pamet ktora sa da alokovat

/*+ Reprezentuje kazdy jeden pametovy blok
	- prazny ci zaplneny
	- velkost bloku
	- odkazi na dalsi a predchadzajuci blok
	- velksot 16 bytov
*/
struct MemBlock { 
	char m_isfree;
	unsigned int m_size;
	//UINT m_id;

	struct MemBlock* m_next;
	struct MemBlock* m_prev;
};

/*+ Hlavna globalna premenna, ktora reprezentuje celu nasu pamet
	- Kebyze mozem dalsiu globalnu do ktorej ulozim gEnd ako koniec pamete 
		hlavicka by zaberala o m_next menej pamete a algoritmus by bol o trochu rychlejsi
		inak sa musi pouzit m_next ako odkaz.
	- Dalsia globlna gFreeList by sa mohla pouzit na udrziavanie zoznamu len volnych blokov atd..
*/
void* gMemory;
//struct MemBlock* M;
//void *gStart;
//void *gEnd;

/*+ Inicializuj pamet
	- Spravy vsetke potrebne operacie
*/
void memory_init(void *ptr, unsigned int size) {
	struct MemBlock * firstblock;
	int velkost;
	gMemory = ptr;
	
	velkost = size - sizeof(struct MemBlock); 
	if(velkost <= 0 || ptr == NULL) {
		printf("Chyba");
	}

	// Pripravy prvy klok pamete
	firstblock = (struct MemBlock*) gMemory;
	firstblock->m_isfree = 1;
	firstblock->m_prev = NULL;
	firstblock->m_next = NULL;
	firstblock->m_size = velkost;
	//firstblock->m_id = 1;

	//gStart = ptr;
	//gEnd = (char*)gMemory + size;
	//printf("%d <-> %d - %d\n", gStart, gEnd, (char*)gStart - (char*)gEnd);
	//M = gMemory;
}

/*+ ANTI FRAGMENTACIA
	- Dokaze spajat rozne male bloky pamete do vacsich
	- Hlada volne bloky na pravej aj na lavej strane
*/
void memory_antifragmentation(struct MemBlock** cblock) 
{
	struct MemBlock **cblock2;

	// - Skontroluj ci dalsi 
	while((*cblock)->m_next != NULL && (*cblock)->m_next->m_isfree == 1) {
		(*cblock)->m_size += sizeof(struct MemBlock) + (*cblock)->m_next->m_size;
		(*cblock)->m_next->m_prev = (*cblock);
		(*cblock)->m_next = (*cblock)->m_next->m_next;
	}
	
	// a ten predchadzaujici blok je volny ak ano spoj ich dokopy
	cblock2 = cblock;
	while(*cblock2 != NULL && (*cblock2)->m_prev != NULL && (*cblock2)->m_prev->m_isfree == 1) {
		(*cblock2)->m_prev->m_size += sizeof(struct MemBlock) + (*cblock2)->m_size;
		(*cblock2)->m_prev->m_next = (*cblock2)->m_next;
		if((*cblock2)->m_next != NULL) {
			(*cblock2)->m_next->m_prev = (*cblock2)->m_prev;
		}
		cblock2 = &((*cblock2)->m_prev);
	}
}

int memory_check(struct MemBlock *b)
{
	// Zaciname od zaciatku pamete
	struct MemBlock** current;
	current = (struct MemBlock**) &gMemory;

	// Prehladavaj
	while(*current != NULL)
	{
		// Dany blok musi byt volny a musi mat dostatok pamete
		if(*current == b) {
			// Wohooo :) naslo sa nieco
			return 1;
		}
		// Prejdi na dalsi block
		current = &(*current)->m_next;
	}
	return 0;
}

/*+ Uvolni pamet
	- Uvolnovanie pamete je extremne rychle, konstatne
	- Antyfragmetacia vsak trva dlhsie ale ta sa nemusi vykonat pri free, moze
	casovo spustat alebo pri nedostatku pamete
*/
void memory_free(void *ptr)
{
	struct MemBlock* cblock;

	// Error ?
	if(ptr == NULL) {
		printf("Invalid pointer!\n");
		exit(1);
	}

	// ptr posun na hlavicku HLAVICKA | DATA - pozor na ((char*)(ptr)) - TYP*(sucet) / ULOZISKOVYTYP
	cblock = (struct MemBlock*) ( (char*) ptr - sizeof(struct MemBlock));

	// Skontroluj ci tento blok je nas - TODO: Hladaj takyto blok v zozname
	if(cblock == NULL || cblock->m_isfree || !memory_check(cblock) ) {
		printf("Invalid pointer!\n");
		exit(1);
	}

	// Status pamete nastav na free
	cblock->m_isfree = 1; // mozno po antyfragmentacii toto nieje potrebne

	// --- ANTI FRAGMENTACIA
	memory_antifragmentation(&cblock);
}

/* Typ vyhladavania volnej pamete
	- Extremne rychly
	- Avsak nemusi byt velmi efektivny z hladiska vyuzivania pamete
*/
struct MemBlock* memory_firstfit(UINT size)
{
	// Zaciname od zaciatku pamete
	struct MemBlock** current;
	current = (struct MemBlock**) &gMemory;

	// Prehladavaj
	while(*current != NULL)
	{
		// Dany blok musi byt volny a musi mat dostatok pamete
		if((*current)->m_isfree && (*current)->m_size >= size) {
			// Wohooo :) naslo sa nieco
			return *current;
		}

		// Prejdi na dalsi block
		current = &(*current)->m_next;
	}

	// Nenaslo nic
	return NULL;
}

/* Typ vyhladavania volnej pamete
	- Extremne efektivnejsi ale pomalsi
*/
struct MemBlock* memory_bestfit(UINT size)
{
	// Zaciname od zaciatku pamete
	struct MemBlock** current;
	struct MemBlock* best;
	UINT bestsize;

	current = (struct MemBlock**) &gMemory;
	best = NULL;
	bestsize = 65535;

	// Prehladavaj
	while(*current != NULL)
	{
		// Dany blok musi byt volny a musi mat dostatok pamete
		if((*current)->m_isfree && (*current)->m_size >= size) {
			// Je to lepsie ako minuly ?
			if( (*current)->m_size < bestsize) {
				best = (*current);
				bestsize = best->m_size;
			}
		}

		// Prejdi na dalsi block
		current = &(*current)->m_next;
	}
	return best;
}

/* Ktory typ vyhladavania zvolime ?
	- Tieto 2 funkcie sa mozu striedat
	- Napriklad na zaciatku po init je najlepsi First fit
	- Neskor pri nedostatku pamete je lepsi Best fit
*/
struct MemBlock* memory_find(UINT size)
{
	// First fit ?
	//return memory_firstfit(size);

	// Best fit ?
	return memory_bestfit(size);
}

/*+ Rozdel pamet ak je to mozne
	Mame urcity blok pamete napriklad o velkosti 900bytov.
	A potrebujeme naalokovat 80bytov.
	Je hlupost pre blok prirad 900bytov, postaci priradit 80+hlavicku.
	A na zvysku sa vytvori novy blok.
*/
void memory_fragme(struct MemBlock** freeblock, UINT size)
{
	// Zisti kolko pamete sa zvysi
	int zvysok = (*freeblock)->m_size - size;

	// Ked zvysok je vacsi ako hlavicku + MIN_PAMET 
	// teda bolo by mozne vytvorit dalsi blok + nejaku pamet pre najmensi typ
	if(zvysok >= (int) (sizeof(struct MemBlock)+MIN_PAMET)) {
		
		// Rozdel pamet - Sprav novy blok
		struct MemBlock* newblock;
		newblock = (struct MemBlock *) (  (char*)(*freeblock) + size + sizeof(struct MemBlock)  );
		/*
			int i;	// TYP*(sucet) / ULOZISKOVYTYP
			i = ((char*)newblock - (char*)(*freeblock));
			i = newblock < gEnd && newblock >= gStart;
			printf("%d\n", newblock);
			//newblock->m_id = (*freeblock)->m_id + 1;
		*/
		newblock->m_isfree = 1;
		newblock->m_size = zvysok - sizeof(struct MemBlock);
		newblock->m_prev = (*freeblock);
		newblock->m_next = (*freeblock)->m_next;
		
		(*freeblock)->m_size = size;
		(*freeblock)->m_next = newblock;
		return;
	} 

	// Pamet sa neda rozdelit, ponechaj ju
}

/*+ Funkcia naalokuje novu pamet 
	- Vracia NULL pri neuspechu
*/
void *memory_alloc(unsigned int size) 
{
	// Skus najst nejaky volny block...
	struct MemBlock* freeblock;
	if(size == 0) return NULL;
	freeblock = memory_find(size);
	
	// Nenasli sme zial ziadnu volnu pamet
	if(!freeblock) return NULL;
	
	// Pamet uz nieje volna
	freeblock->m_isfree = 0;

	// Rozdel pamet ak je to potrebne a mozne
	memory_fragme(&freeblock, size);

	// Presun pointer z hlavicky na DATA
	//return (char*)freeblock + sizeof(struct MemBlock);
	// TYP*(sucet) / ULOZISKOVYTYP
	//return ((int)freeblock + sizeof(struct MemBlock)); 
	return (freeblock+1); // to iste
}

/*
void memory_dump()
{
	// Zaciname od zaciatku pamete
	struct MemBlock** current;
	current = (struct MemBlock**) &gMemory;

	// Prehladavaj
	while(*current != NULL && (*current)->m_next != NULL) {
		printf("- %d", (*current)->m_next - (*current) );
		printf("- %d", ((*current) + (*current)->m_size) - (*current) );
		current = &(*current)->m_next;
	}
	printf("\n");
}
*/
/*
int main()
{	
	char region[50];
	char* pointers[10];

	memory_init(region, 50);
	pointers[0] = (char*) memory_alloc(5);
	if (pointers[0]) {
		memset(pointers[0], 0, 5);
	};
	pointers[1] = (char*) memory_alloc(3);
	if (pointers[1]) {
		memset(pointers[1], 0, 3);
	};
	pointers[2] = (char*) memory_alloc(6);
	if (pointers[2]) {
		memset(pointers[2], 0, 6);
	};

	if (pointers[0]) {
		memory_free(pointers[0]);
	};
	pointers[3] = (char*) memory_alloc(10);
	if (pointers[3]) {
		memset(pointers[3], 0, 10);
	};
	if (pointers[2]) {
		memory_free(pointers[2]);
	};
	pointers[4] = (char*) memory_alloc(5);
	if (pointers[4]) {
		memset(pointers[4], 0, 5);
	};

	if (pointers[1]) {
		memory_free(pointers[1]);
	};
	if (pointers[3]) {
		memory_free(pointers[3]);
	};
	
	return 0;
}
*/
/*

#define H_ISALLOC(a)    (*a & 1) // 0x01
#define H_GETSIZE(a)    (*a & ~0x1)
#define H_SETSIZE(a,b)	*a = b | 0x1;
#define H_DEALLOC(a)    *a = *a & ~0x1;

char memory[1000];
char* data[30];
char *p;
UINT i;

//	TYP*(sucet) / ULOZISKOVYTYP
	struct MemBlock *a = 0;
	struct MemBlock *b = 0;
	
	printf("%d\n", sizeof(char));
	printf("%d\n", sizeof(int));
	printf("%d\n", sizeof(unsigned int));
	printf("%d\n", sizeof(long));
	printf("%d\n", sizeof(void*));
	printf("%d\n", sizeof(struct MemBlock));
	printf("%d\n", sizeof(struct MemBlock*));
	printf("---\n");

	b = a + sizeof(char)*100; printf("%d\n", b - a);
	b = a + 100; printf("%d\n", b - a); // 100
	b = (char*) a + 100; printf("%d\n", b - a); // 25
	b = ( long *) a + 100; printf("%d\n", b - a);
	b = ( float *) a + 100; printf("%d\n", b - a);
	b = (UINT*) ( (struct MemBlock*) a + 100 ); printf("%d\n", b - a); // 16*100 / 4


*/

