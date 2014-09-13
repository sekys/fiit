#define WIN32_LEAN_AND_MEAN
#include <windows.h>

#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <math.h>
#include <iostream>
#include <time.h>
#include <fstream>

#include <GL/glew.h>
#include <GL/freeglut.h>
#include <GL/freeglut_ext.h>

#include "font.h"
#include "roller.h"
#include "draw.h"

int staryCas = 0;
float CameraPosition[3];
float CameraUhol = 0.f;

void display()
{
    int aktualnyCas, deltaCas;
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    glMatrixMode(GL_MODELVIEW);
    aktualnyCas = glutGet(GLUT_ELAPSED_TIME);
    deltaCas = aktualnyCas - staryCas;
    staryCas = aktualnyCas;
    rollingStatus(deltaCas);
    scena(deltaCas);
    glFlush();
    glutSwapBuffers();
}

void update()
{
    glutPostRedisplay();
}

void keyboard(unsigned char key, int x, int y)
{
    switch(key)
    {
    case 32: {
        roll = true;
        break;
    }
    case 'e': {
        AnimaciaMode = !AnimaciaMode;
        break;
    }
    case 'o': {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60, 1, .1, 100);
        const float polomer = 8.f;
        CameraUhol += 10.f;
        CameraPosition[0] = sin( uhol2radian(CameraUhol) ) * polomer;
        CameraPosition[2] = cos( uhol2radian(CameraUhol) ) * polomer;
        gluLookAt(CameraPosition[0], CameraPosition[1], CameraPosition[2], 0.0f, -1.0f, 0.0f, 0, 1, 0);
        glMatrixMode(GL_MODELVIEW);
        break;
    }
    case 'u': {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60, 1, .1, 100);
        CameraPosition[0] = 0.0f;
        CameraPosition[1] = 8.0f;
        CameraPosition[2] = 2.0f;
        gluLookAt(CameraPosition[0], CameraPosition[1], CameraPosition[2], 0.0f, -1.0f, 0.0f, 0, 1, 0);
        glMatrixMode(GL_MODELVIEW);
        break;
    }
    case 'i': {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60, 1, .1, 100);
        gluLookAt(10.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0, 1, 0);
        glMatrixMode(GL_MODELVIEW);
        break;
    }
    case 'q': {
        exit(0);
        break;
    }
    }
    glutPostRedisplay();
}

int main(int argc, char ** argv)
{
    AllocConsole() ;
    freopen( "CON", "w", stdout );
    glutInit(&argc, argv);
    glutInitWindowSize(512, 512);
    glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
    glutCreateWindow("ROLL THE ROLL");

    glewInit();
    glMatrixMode(GL_PROJECTION);
    gluPerspective(60, 1, .1, 100);
    CameraPosition[0] = 0.0f;
    CameraPosition[1] = 8.0f;
    CameraPosition[2] = 2.0f;
    gluLookAt(CameraPosition[0], CameraPosition[1], CameraPosition[2], 0.0f, -1.0f, 0.0f, 0, 1, 0);
    //gluLookAt(10.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0, 1, 0);

    buildScene();
    buildFont();

    glutDisplayFunc(display);
    glutIdleFunc(update);
    glutKeyboardFunc(keyboard);
    glutMainLoop();
    return EXIT_SUCCESS;
}
