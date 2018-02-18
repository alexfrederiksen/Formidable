package com.frederiksen.formidable.rendering.uniforms;

import com.frederiksen.formidable.rendering.ShaderProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public abstract class Uniform<T> {
    protected ShaderProgram shaderProgram;
    protected int id;
    protected String name;

    protected T value;

    /**
     * Registers the uniform variable from the shader program
     * @param program
     * @param name
     * @throws Exception
     */
    public void register(ShaderProgram program, String name) throws Exception {
        this.shaderProgram = program;
        this.name = name;

        id = glGetUniformLocation(shaderProgram.getProgramId(), name);
        if (id < 0) {
            throw new Exception("Failed to find uniform: " + name);
        }
    }

    // TODO: call this in all childeren
    public void upload(T value) {
        this.value = value;
    }
}
