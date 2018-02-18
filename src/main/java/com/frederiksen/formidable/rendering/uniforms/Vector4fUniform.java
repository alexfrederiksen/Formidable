package com.frederiksen.formidable.rendering.uniforms;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.glUniform4f;

public class Vector4fUniform extends Uniform<Vector4f> {
    @Override
    public void upload(Vector4f value) {
        glUniform4f(id, value.x, value.y, value.z, value.w);
    }
}
