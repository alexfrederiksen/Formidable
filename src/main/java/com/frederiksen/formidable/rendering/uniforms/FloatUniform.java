package com.frederiksen.formidable.rendering.uniforms;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class FloatUniform extends Uniform<Float> {
    @Override
    public void upload(Float value) {
        glUniform1f(id, value);
    }
}
