#include <stdarg.h>

GLuint	base;

GLvoid buildFont()
{
    HFONT	font;
    HFONT	oldfont;

    base = glGenLists(96);

    font = CreateFont(	-24,
                        0,
                        0,
                        0,
                        FW_BOLD,
                        FALSE,
                        FALSE,
                        FALSE,
                        ANSI_CHARSET,
                        OUT_TT_PRECIS,
                        CLIP_DEFAULT_PRECIS,
                        ANTIALIASED_QUALITY,
                        FF_DONTCARE|DEFAULT_PITCH,
                        "Courier New");

    HDC__* hDC = wglGetCurrentDC();
    oldfont = (HFONT)SelectObject(hDC, font);
    wglUseFontBitmaps(hDC, 32, 96, base);
    SelectObject(hDC, oldfont);
    DeleteObject(font);
}

GLvoid glPrint(const char *fmt, ...)
{
    char		text[256];
    va_list		ap;

    if (fmt == NULL)
        return;

    va_start(ap, fmt);
    vsprintf(text, fmt, ap);
    va_end(ap);

    glPushAttrib(GL_LIST_BIT);
    glListBase(base - 32);
    glCallLists(strlen(text), GL_UNSIGNED_BYTE, text);
    glPopAttrib();
}
