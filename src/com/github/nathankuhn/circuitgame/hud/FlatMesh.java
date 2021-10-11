package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.Mesh;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;

public class FlatMesh {

    public static Mesh BuildHudMesh(float centerX, float centerY, float sizeX, float sizeY, int rows, int cols) {

        float originX = centerX - sizeX / 2;
        float originY = centerY - sizeY / 2;

        int cells = rows * cols;

        Vector3f[] verts = new Vector3f[cells * 4];
        Vector3f[] normals = new Vector3f[cells * 4];
        Vector2f[] uvs = new Vector2f[cells * 4];
        Vector3i[] faces = new Vector3i[cells * 2];

        int currentCell;

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {

                currentCell = x + y * cols;
                verts[currentCell * 4] =     new Vector3f(x       * sizeX + originX, y       * sizeY + originY, 0.0f);
                verts[currentCell * 4 + 1] = new Vector3f(x       * sizeX + originX, (y + 1) * sizeY + originY, 0.0f);
                verts[currentCell * 4 + 2] = new Vector3f((x + 1) * sizeX + originX, (y + 1) * sizeY + originY, 0.0f);
                verts[currentCell * 4 + 3] = new Vector3f((x + 1) * sizeX + originX, y       * sizeY + originY, 0.0f);

                normals[currentCell * 4] =     new Vector3f(0.0f, 1.0f, 0.0f);
                normals[currentCell * 4 + 1] = new Vector3f(0.0f, 1.0f, 0.0f);
                normals[currentCell * 4 + 2] = new Vector3f(0.0f, 1.0f, 0.0f);
                normals[currentCell * 4 + 3] = new Vector3f(0.0f, 1.0f, 0.0f);

                uvs[currentCell * 4] =     new Vector2f(0.0f, 0.0f);
                uvs[currentCell * 4 + 1] = new Vector2f(0.0f, 1.0f);
                uvs[currentCell * 4 + 2] = new Vector2f(1.0f, 1.0f);
                uvs[currentCell * 4 + 3] = new Vector2f(1.0f, 0.0f);

                faces[currentCell * 2] = new Vector3i(
                        currentCell * 4,
                        currentCell * 4 + 2,
                        currentCell * 4 + 1
                );
                faces[currentCell * 2 + 1] = new Vector3i(
                        currentCell * 4,
                        currentCell * 4 + 3,
                        currentCell * 4 + 2
                );

            }
        }

        return new Mesh(verts, normals, uvs, faces);

    }

    public static Mesh BuildHudMesh(Vector2f origin, Vector2f size, int rows, int cols) {
        return BuildHudMesh(origin.x, origin.y, size.x, size.y, rows, cols);
    }

}
