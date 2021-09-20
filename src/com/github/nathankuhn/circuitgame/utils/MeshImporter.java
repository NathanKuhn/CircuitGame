package com.github.nathankuhn.circuitgame.utils;

import com.github.nathankuhn.circuitgame.engine.Mesh;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MeshImporter {

    public static final String MODEL_PATH = "res/";

    public static Mesh LoadFromOBJ(String name) {

        try {

            File file = new File(MODEL_PATH + name);

            Scanner fileReader = new Scanner(file);

            String line;
            List<String> vertLines = new ArrayList<>();
            List<String> normLines = new ArrayList<>();
            List<String> uvLines = new ArrayList<>();
            List<String> faceLines = new ArrayList<>();

            while (fileReader.hasNextLine()) {

                line = fileReader.nextLine();

                if (line.startsWith("v "))
                    vertLines.add(line);

                else if (line.startsWith("vn"))
                    normLines.add(line);

                else if (line.startsWith("vt"))
                    uvLines.add(line);

                else if (line.startsWith("f"))
                    faceLines.add(line);
            }

            int verts = vertLines.size();
            int norms = normLines.size();
            int uvNum = uvLines.size();
            int faces = faceLines.size();

            float[] positions = new float[verts * 3];
            float[] normals = new float[norms * 3];
            float[] uvs = new float[uvNum * 2];
            int[] positionIndices = new int[faces * 3];

            for (int vert = 0; vert < verts; vert++) {

                String vertLine = vertLines.get(vert);
                vertLine = vertLine.replace("v ", "");
                String[] v = vertLine.split(" ");

                for (int i = 0; i < 3; i++) {
                    positions[vert * 3 + i] = Float.parseFloat(v[i]);
                }
            }

            for (int uv = 0; uv < uvNum; uv++) {

                String uvLine = uvLines.get(uv);
                uvLine = uvLine.replace("vt ", "");
                String[] vt = uvLine.split(" ");

                uvs[uv * 2] = Float.parseFloat(vt[0]);
                uvs[uv * 2 + 1] = Float.parseFloat(vt[1]);

            }

            for (int norm = 0; norm < norms; norm++) {

                String normLine = normLines.get(norm);
                normLine = normLine.replace("vn ", "");
                String[] vn = normLine.split(" ");

                for (int i = 0; i < 3; i++) {
                    normals[norm * 3 + i] = Float.parseFloat(vn[i]);
                }
            }

            float[] arrangedNormals = new float[verts * 3];
            float[] arrangedUvs = new float[verts * 2];
            int normalIndex;
            int positionIndex;
            int uvIndex;

            for (int face = 0; face < faces; face++) {

                String faceLine = faceLines.get(face);
                faceLine = faceLine.replace("f ", "");
                String [] f = faceLine.split(" ");

                for (int i = 0; i < 3; i++) {
                    positionIndex = Integer.parseInt(f[i].split("/")[0]) - 1;
                    normalIndex = Integer.parseInt(f[i].split("/")[2]) - 1;
                    uvIndex = Integer.parseInt(f[i].split("/")[1]) - 1;

                    positionIndices[face * 3 + i] = positionIndex;

                    for (int j = 0; j < 3; j++) {
                        arrangedNormals[positionIndex * 3 + j] = normals[normalIndex * 3 + j];
                    }
                    for (int j = 0; j < 2; j++) {
                        arrangedUvs[positionIndex * 2 + j] = uvs[uvIndex * 2 + j];
                    }
                }

            }

            fileReader.close();
            return new Mesh(positions, arrangedNormals, arrangedUvs, positionIndices);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Mesh(new float[0], new float[0], new float[0], new int[0]);
    }

}
