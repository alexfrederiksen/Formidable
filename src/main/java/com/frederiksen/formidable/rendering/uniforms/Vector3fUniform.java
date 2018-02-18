package com.frederiksen.formidable.rendering.uniforms;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL20.glUniform3f;

public class Vector3fUniform extends Uniform<Vector3f> {
    @Override
    public void upload(Vector3f value) {
        glUniform3f(id, value.x, value.y, value.z);
    }
}
