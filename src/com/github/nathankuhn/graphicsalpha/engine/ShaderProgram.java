package com.github.nathankuhn.graphicsalpha.engine;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    protected static final String DEFAULT_VERTEX_PATH = "shaders/defaultVertex.shader";
    protected static final String DEFAULT_FRAGMENT_PATH = "shaders/defaultFragment.shader";

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    public ShaderProgram() throws Exception {

        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create shader");
        }
    }

    private void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    private void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderType == 0) {
            throw new Exception("Could not create Shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    private void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validation shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void init(String vertexShaderPath, String fragmentShaderPath) throws Exception {
        createVertexShader(Utils.LoadResource(vertexShaderPath));
        createFragmentShader(Utils.LoadResource(vertexShaderPath));
        link();
    }

    public void init() throws Exception {
        createVertexShader(Utils.LoadResource(DEFAULT_VERTEX_PATH));
        createFragmentShader(Utils.LoadResource(DEFAULT_FRAGMENT_PATH));
        link();
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

}
