package com.github.nathankuhn.graphicsalpha.display;

import com.github.nathankuhn.graphicsalpha.utils.Color;
import com.github.nathankuhn.graphicsalpha.utils.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class Draw {

    public static void Rectangle(Color color, Vector2 position, Vector2 dimensions) {

        glBegin(GL_QUADS);

        glColor3f(color.r, color.g, color.b);

        glVertex3f(position.x, position.y, 0.0f);
        glVertex3f(position.x + dimensions.x, position.y, 0.0f);
        glVertex3f(position.x + dimensions.x, position.y + dimensions.y, 0.0f);
        glVertex3f(position.x, position.y + dimensions.y, 0.0f);

        glEnd();

    }

    public static void Line(Color color, Vector2 start, Vector2 end) {

        glBegin(GL_LINES);

        glColor3f(color.r, color.g, color.b);

        glVertex3f(start.x, start.y, 0.0f);
        glVertex3f(end.x, end.y, 0.0f);

        glEnd();

    }

}
