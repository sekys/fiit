#include <windows.h>
#include <gl\gl.h>
#include <gl\glu.h>
#include <stdio.h>
#include <math.h>
#include <time.h>

typedef struct
{
    unsigned char r;
    unsigned char g;
    unsigned char b;
} RBG;

typedef struct
{
    RBG* data;
    int w, h;
} Image;

GLuint texture[10];
GLUquadricObj	*q;
bool AnimaciaMode = true;

Image MyLoadImage(int w, int h, const char * patch)
{
    FILE *subor;
    Image ima;
    ima.w = w;
    ima.h = h;
    ima.data = (RBG*) malloc(sizeof(RBG) * w * h);
    subor = fopen(patch, "rb");
    if (subor==NULL)
    {
        perror("Subor nenajdeny.");
    }
    fread(ima.data, sizeof(RBG), w * h, subor);
    fclose(subor);
    return ima;
}

void buildTextures()
{
    Image image[10];
    image[0] = MyLoadImage(900, 600, "Data\\automatyonlinehry.RGB");
    image[1] = MyLoadImage(256, 256, "Data\\envroll.RGB");
    image[2] = MyLoadImage(512, 256, "Data\\ball0.RGB");
    image[3] = MyLoadImage(512, 256, "Data\\ball1.RGB");
    image[4] = MyLoadImage(512, 256, "Data\\ball2.RGB");

    glEnable(GL_TEXTURE_2D);
    glGenTextures(5, &texture[0]);
    int i;
    for (i=0; i < 5; i++)
    {
        glBindTexture(GL_TEXTURE_2D, texture[i]);
        glTexImage2D(GL_TEXTURE_2D, 0, 3, image[i].w, image[i].h, 0, GL_RGB, GL_UNSIGNED_BYTE, image[i].data);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    }

    glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
}

void DrawObject(int indexOfTexture)
{
    glColor3f(1.0f, 1.0f, 1.0f);
    glBindTexture(GL_TEXTURE_2D, texture[indexOfTexture]);
    gluSphere(q, 0.35f, 32, 32);

    glBindTexture(GL_TEXTURE_2D, texture[1]);
    glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE);
    glEnable(GL_TEXTURE_GEN_S);
    glEnable(GL_TEXTURE_GEN_T);
    gluSphere(q, 0.35f, 32, 32);
    glDisable(GL_TEXTURE_GEN_S);
    glDisable(GL_TEXTURE_GEN_T);
    glDisable(GL_BLEND);

}

void buildLight() {
    GLfloat jasnost[] = {0.7f, 0.7f, 0.7f, 1.0f};
    GLfloat rozptyl[] = {1.0f, 1.0f, 1.0f, 1.0f};
    GLfloat pozicia[] = {4.0f, 4.0f, 6.0f, 1.0f};
    glLightfv(GL_LIGHT0, GL_AMBIENT, jasnost);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, rozptyl);
    glLightfv(GL_LIGHT0, GL_POSITION, pozicia);
    glEnable(GL_LIGHT0);
    glEnable(GL_LIGHTING);
}

void buildRoller();

void buildScene()
{
    glClearColor(0.2f, 0.5f, 1.0f, 1.0f);
    glClearDepth(1.0f);
    glClearStencil(0);

    buildTextures();
    buildRoller();
    buildLight();

    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

    q = gluNewQuadric();
    gluQuadricNormals(q, GL_SMOOTH);
    gluQuadricTexture(q, GL_TRUE);
    glMatrixMode(GL_MODELVIEW);
}

void DrawFloor()
{
    glBindTexture(GL_TEXTURE_2D, texture[0]);
    glBegin(GL_QUADS);
    glNormal3f(0.0, 1.0, 0.0);
    glTexCoord2f(0.0f, 1.0f);
    glVertex3f(-2.0, 0.0, 2.0);
    glTexCoord2f(0.0f, 0.0f);
    glVertex3f(-2.0, 0.0,-2.0);
    glTexCoord2f(1.0f, 0.0f);
    glVertex3f( 2.0, 0.0,-2.0);
    glTexCoord2f(1.0f, 1.0f);
    glVertex3f( 2.0, 0.0, 2.0);
    glEnd();
}

float tParameter = 0.f;
float interpolacia(float t, float A, float B)
{
   return (1.f-t) * A + B * t;
}
void Animacia(int delta) {
    float x, z;
    float polomer, uhol;
    tParameter += (float) delta / 10000.f;
    //printf("%d %.2f %.2f \n", delta, t, tParameter);
    if(tParameter > 1.f) {
        tParameter =  (float) ((int) (tParameter * 10000) % 10000) / 10000.f;
    }

    // Hlavny bod posuvaj ako trojholnik
    if(tParameter > 0.5) {
        z = interpolacia((tParameter - 0.5f) * 2.f, 4.f, -4.f);
    } else {
        z = interpolacia(tParameter * 2.f, -4.f, 4.f);
    }
    glLoadIdentity();
    x = interpolacia(tParameter, -4.f, 4.f);
    glTranslatef(x, 0.f, z);
    DrawObject(2);

    // Druhy objekt je na pevno pripojeny k prvemu a rotuj ho okolo Z suradnice
    glPushMatrix();
        glTranslatef(0.f, 0.f, -0.8f);
        x = interpolacia(tParameter, 0.f, 360.f);
        glRotatef(x, 0.0f, .0f, 1.0f);
        DrawObject(3);
    glPopMatrix();

    // Treti objek rotuj okolo hlavneho objektu
    glPushMatrix();
        polomer = 0.9f;
        uhol = interpolacia(tParameter, 0.f, 3600.f);
        x = sin( uhol2radian(uhol) ) * polomer;
        z = cos( uhol2radian(uhol) ) * polomer;
        glTranslatef(x, 0.f, z);
        DrawObject(4);
    glPopMatrix();

    // Treti objek rotuj okolo hlavneho objektu pomalsie
    glPushMatrix();
        polomer = 3.3f;
        uhol = interpolacia(tParameter, 0.f, 720.f);
        x = sin( uhol2radian(uhol) ) * polomer;
        z = cos( uhol2radian(uhol) ) * polomer;
        glTranslatef(x, 0.f, z);
        DrawObject(2);

        // Okolo druheho bieleho objektu sprav 2 objekty, mensie a rychl orotujuce
        glPushMatrix();
            polomer = 0.5f;
            uhol = interpolacia(tParameter, 0.f, 7200.f);
            x = sin( uhol2radian(uhol) ) * polomer;
            z = cos( uhol2radian(uhol) ) * polomer;
            glTranslatef(x, 0.f, z);
            glScalef(0.3f, 0.3f, 0.3f);
            DrawObject(3);
        glPopMatrix();

        glPushMatrix();
            polomer = 0.6f;
            uhol = interpolacia(tParameter, 180.f, 540.f);
            x = sin( uhol2radian(uhol) ) * polomer;
            z = cos( uhol2radian(uhol) ) * polomer;
            glTranslatef(x, 0.f, z);
            glPushMatrix();
                x = interpolacia(tParameter, 0.f, 360.f);
                glRotatef(x, 0.0f, .0f, 1.0f);
                x = interpolacia(tParameter, 0.7f, 2.f);
                glScalef(x, x, x);
                DrawObject(4);
            glPopMatrix();

                // Mini mesiac
                polomer = 0.5f;
                uhol = interpolacia(tParameter, 0.f, 720.f);
                x = sin( uhol2radian(uhol) ) * polomer;
                z = cos( uhol2radian(uhol) ) * polomer;
                glTranslatef(x, 0.f, z);
                glScalef(0.3f, 0.3f, 0.3f);
                DrawObject(4);
        glPopMatrix();

    glPopMatrix();
}

void scena(int delta)
{
    glLoadIdentity();
    glTranslatef(0.0f, 0.f, 0.0f);
    glScalef(2.0f, 2.0f, 2.0f);
    glColor4f(1.0f, 1.0f, 1.0f, 0.8f);
    glDisable(GL_LIGHTING);
    DrawFloor();
    glEnable(GL_LIGHTING);

    if(AnimaciaMode) {
        Animacia(delta);
    } else {
        renderRoller();

        glDisable(GL_LIGHTING);
        glLoadIdentity();
        glTranslatef(-1.f,3.f,-2.0f);
        glColor3f(1.0f, 0.f, 0.f);
        glRasterPos2f(0.f, 0.f);
        if(rollStatus == 0) {
            glPrint("STLAC MEDZERNIK");
        } else if(rollStatus == 1) {
            glPrint("ROLLLLLLLLLLING");
        } else if(rollStatus == 2) {
            glPrint("VYHRAL SI");
        } else if(rollStatus == 3) {
            glPrint("PREHRAL SI");
        }
        glEnable(GL_LIGHTING);
    }
}

