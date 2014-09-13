// Konstanty
const int celkovypocet = 12;
const int MedzeraMedziGulami = 3;
const float DeltaStupen = 360.0f / (float) celkovypocet;
const float MiniDeltaStupen = DeltaStupen / (float) MedzeraMedziGulami;

int IndexiTextur[3][12];
float StartUhol[3];
int NahodnyPocet[3];
int PocetOtoceni[3];
int AktualnyIndex[3];

// 0 nic sa nedeje
// 1 rolujem
// 2 vyhral si, pauza
// 3 prehral, pauza
int rollTimer;
int rollStatus = 0;
bool roll = false;

float uhol2radian(float uhol)
{
    return uhol * 3.141592654f / 180.0f;
}

void vypis() {
    for(int i=0; i < 12; i++) {
        printf("%d. %d %d %d\n", i, IndexiTextur[0][i], IndexiTextur[1][i], IndexiTextur[2][i]);
    }
}

bool vypocitajVitastvo() {
    int a, b, c;
    a = AktualnyIndex[0];
    b = AktualnyIndex[1];
    c = AktualnyIndex[2];
    /*
    printf("I %d %d %d\n", IndexiTextur[0][a], IndexiTextur[1][b], IndexiTextur[2][c]);
    printf("N %d %d %d\n", NahodnyPocet[0], NahodnyPocet[1], NahodnyPocet[2]);
    printf("A %d %d %d\n", AktualnyIndex[0], AktualnyIndex[1], AktualnyIndex[2]);
    vypis();
    */
    return IndexiTextur[0][a] == IndexiTextur[1][b] && IndexiTextur[1][b] == IndexiTextur[2][c];
}

void rollingStatus(int deltaTime) {
    // Stav rolovania uprav
    if(rollStatus == 0) {
        if(roll) {
            // Zacni rolovat
            rollTimer = 0;
            rollStatus = 1;
            roll = false;

            for(int i=0; i < 3; i++) {
                NahodnyPocet[i] = (rand() % 72 + 1);
                AktualnyIndex[i] = (12 + AktualnyIndex[i] - (NahodnyPocet[i] % 12) ) % 12;//% 12;
                PocetOtoceni[i] =  0;
            }
        }
    } else if(rollStatus == 1) {
        // Ked uz rolujem, zvysuj cas
        roll = false;
        if(PocetOtoceni[0] == NahodnyPocet[0]* MedzeraMedziGulami) {
            if(PocetOtoceni[1] == NahodnyPocet[1]* MedzeraMedziGulami) {
                if(PocetOtoceni[2] == NahodnyPocet[2]* MedzeraMedziGulami) {
                    rollTimer = 0;
                    rollStatus = vypocitajVitastvo() == true ? 2 : 3;
                } else {
                    StartUhol[2] += MiniDeltaStupen;
                    PocetOtoceni[2]++;
                }
            } else {
                StartUhol[1] += MiniDeltaStupen;
                PocetOtoceni[1]++;
            }
        } else {
            StartUhol[0] += MiniDeltaStupen;
            PocetOtoceni[0]++;
        }
    } else if(rollStatus == 2 || rollStatus == 3) {
        // Cakaj maly usek a poskytni novu hru
        rollTimer += deltaTime;
        if(rollTimer > 2000) {
            // Chod naspat na zaciatok
            rollStatus = 0;
            rollTimer = 0;
            roll = false;
        }
    }
}

void DrawObject(int x);

void renderRoller()
{
    const float polomer = 2.5f;
    float uhol;

    for(int c=0; c < 3; c++)
    {
        for(int r=0; r < celkovypocet; r++)
        {
            uhol = 10.f + StartUhol[c] + r * DeltaStupen;
            float a = sin( uhol2radian(uhol) ) * polomer;
            float b = cos( uhol2radian(uhol) ) * polomer;

            glLoadIdentity();
            glTranslatef(c * 1.f - 1.0f, 0.f - a, b );
            glRotatef(120.f, 0.0f, 0.0f, 1.0f);
            DrawObject(IndexiTextur[c][r]);
        }
    }
}

void buildRoller() {
    srand ( time(NULL) );
    int a, b;
    for(a=0; a < 3; a++) {
        StartUhol[a] = 0.f;
        AktualnyIndex[a] = 9;
        for( b=0; b < 12; b++) {
            IndexiTextur[a][b] = (rand() % 3) + 2;
        }
    }
}
