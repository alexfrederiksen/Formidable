package com.frederiksen.formidable.game;

import com.frederiksen.formidable.lighting.Light;
import com.frederiksen.formidable.rendering.ShaderProgram;
import com.frederiksen.formidable.rendering.uniforms.*;

public class SceneShader extends ShaderProgram {
    public static final int MAX_LIGHTS = 10;

    public final Matrix4fUniform projectionMatrix = new Matrix4fUniform();
    public final Matrix4fUniform modelViewMatrix  = new Matrix4fUniform();
    public final MaterialUniform material         = new MaterialUniform();
    public final Vector3fUniform globalAmbient    = new Vector3fUniform();
    public final FloatUniform    specularPower    = new FloatUniform();

    public final ArrayUniform<Light, LightUniform> lights = new ArrayUniform<>(
            () -> new LightUniform(),
            MAX_LIGHTS);

    public void registerUniforms() throws Exception {
        projectionMatrix.register(this, "projection");
        modelViewMatrix.register(this, "modelView");
        globalAmbient.register(this, "globalAmbient");
        material.register(this, "material");
        specularPower.register(this, "specularPower");

        lights.register(this, "lights");
    }
}
