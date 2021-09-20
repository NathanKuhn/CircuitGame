package com.github.nathankuhn.circuitgame.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderResource {

    public static String LoadResource(String path) {

        InputStream stream = ShaderResource.class.getResourceAsStream(path);
        String content = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line = "";

        try {
            while (line != null) {
                content += line + "\n";
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
            System.out.println(e.getStackTrace());
        }
        return content;

    }

}
