package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.Matrix4;
import com.github.nathankuhn.graphicsalpha.utils.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    protected static final String DEFAULT_VERTEX_PATH = "shaders/defaultVertex.shader";
    protected static final String DEFAULT_FRAGMENT_PATH = "shaders/defaultFragment.shader";

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        uniforms = new HashMap<>();
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
            System.out.println("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
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
        programId = glCreateProgram();
        if (programId == 0) {
            System.out.println("Could not create shader");
            throw new Exception("Could not create shader");
        }

        createVertexShader(ShaderResource.LoadResource(vertexShaderPath));
        createFragmentShader(ShaderResource.LoadResource(fragmentShaderPath));
        link();
    }

    public void init() throws Exception {
        init(DEFAULT_VERTEX_PATH, DEFAULT_FRAGMENT_PATH);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public void createUniform(String uniformName) throws Exception {

        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);

    }
    public void setUniform(String uniformName, Matrix4 value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            fb.put(value.getArray());
            fb.flip();
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }
    public void setUniform(String uniformName, Vector3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(3);
            fb.put(value.getArray());
            fb.flip();
            glUniform3fv(uniforms.get(uniformName), fb);
        }
    }
    public void setUniform(String uniformName, float value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(1);
            fb.put(value);
            fb.flip();
            glUniform1fv(uniforms.get(uniformName), fb);
        }
    }
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

}
