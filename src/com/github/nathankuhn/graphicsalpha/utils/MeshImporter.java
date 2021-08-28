package com.github.nathankuhn.graphicsalpha.utils;

import com.github.nathankuhn.graphicsalpha.engine.Mesh;

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
            List<String> faceLines = new ArrayList<>();

            while (fileReader.hasNextLine()) {

                line = fileReader.nextLine();

                if (line.startsWith("v "))
                    vertLines.add(line);

                else if (line.startsWith("vn"))
                    normLines.add(line);

                else if (line.startsWith("f"))
                    faceLines.add(line);
            }

            int verts = vertLines.size();
            int norms = normLines.size();
            int faces = faceLines.size();

            float[] positions = new float[verts * 3];
            float[] normals = new float[norms * 3];
            int[] positionIndices = new int[faces * 3];

            String vertLine;

            for (int vert = 0; vert < verts; vert++) {

                vertLine = vertLines.get(vert);
                vertLine = vertLine.replace("v ", "");
                String[] v = vertLine.split(" ");

                for (int i = 0; i < 3; i++) {
                    positions[vert * 3 + i] = Float.parseFloat(v[i]);

                }
            }

            String normLine;

            for (int norm = 0; norm < norms; norm++) {

                normLine = normLines.get(norm);
                normLine = normLine.replace("vn ", "");
                String[] vn = normLine.split(" ");

                for (int i = 0; i < 3; i++) {
                    normals[norm * 3 + i] = Float.parseFloat(vn[i]);
                }
            }

            String faceLine;
            float[] arrangedNormals = new float[verts * 3];
            int normalIndice;
            int positionIndice;

            for (int face = 0; face < faces; face++) {

                faceLine = faceLines.get(face);
                faceLine = faceLine.replace("f ", "");
                String [] f = faceLine.split(" ");

                for (int i = 0; i < 3; i++) {
                    positionIndice = Integer.parseInt(f[i].split("/")[0]) - 1;
                    normalIndice = Integer.parseInt(f[i].split("/")[2]) - 1;

                    positionIndices[face * 3 + i] = positionIndice;

                    for (int j = 0; j < 3; j++) {
                        arrangedNormals[positionIndice * 3 + j] = normals[normalIndice * 3 + j];
                    }
                }

            }

            fileReader.close();
            return new Mesh(positions, arrangedNormals, new float[verts * 2], positionIndices);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Mesh(new float[0], new float[0], new float[0], new int[0]);
    }

}
