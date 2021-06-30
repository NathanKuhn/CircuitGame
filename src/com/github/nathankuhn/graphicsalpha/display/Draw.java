package com.github.nathankuhn.graphicsalpha.display;

import com.github.nathankuhn.graphicsalpha.utils.Color;

import static org.lwjgl.opengl.GL11.*;

public class Draw {

    public static void Rectangle(Color color, float x, float y, float height, float width) {

        glBegin(GL_QUADS);

        glColor3f(color.r, color.g, color.b);

        glVertex3f(x, y, 0.0f);
        glVertex3f(x + width, y, 0.0f);
        glVertex3f(x + width, y + height, 0.0f);
        glVertex3f(x, y + height, 0.0f);

        glEnd();

    }

    public static void Line(Color color, float x1, float y1, float x2, float y2) {

        glBegin(GL_LINES);

        glColor3f(color.r, color.g, color.b);

        glVertex3f(x1, y1, 0.0f);
        glVertex3f(x2, y2, 0.0f);

        glEnd();

    }

}
