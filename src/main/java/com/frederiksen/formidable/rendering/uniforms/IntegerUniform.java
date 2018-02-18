package com.frederiksen.formidable.rendering.uniforms;

import static org.lwjgl.opengl.GL20.glUniform1i;

public class IntegerUniform extends Uniform<Integer> {
    @Override
    public void upload(Integer value) {
        glUniform1i(id, value);
    }
}
