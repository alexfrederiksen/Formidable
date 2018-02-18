package com.frederiksen.formidable.rendering.uniforms;

import com.frederiksen.formidable.lighting.Material;
import com.frederiksen.formidable.rendering.ShaderProgram;

public class MaterialUniform extends Uniform<Material> {
    private Vector4fUniform ambient = new Vector4fUniform();
    private Vector4fUniform diffuse = new Vector4fUniform();
    private Vector4fUniform specular = new Vector4fUniform();
    private FloatUniform reflectance = new FloatUniform();
    private IntegerUniform hasTexture = new IntegerUniform();
    private IntegerUniform texture = new IntegerUniform();

    @Override
    public void register(ShaderProgram program, String name) throws Exception {
        ambient.register(program, name + ".ambient");
        diffuse.register(program, name + ".diffuse");
        specular.register(program, name + ".specular");
        reflectance.register(program, name + ".reflectance");
        hasTexture.register(program, name + ".hasTexture");
        texture.register(program, name + ".texture");
    }

    @Override
    public void upload(Material value) {
        ambient.upload(value.getAmbient());
        diffuse.upload(value.getDiffuse());
        specular.upload(value.getSpecular());
        reflectance.upload(value.getReflectance());
        hasTexture.upload(value.hasTexture() ? 1 : 0);
        texture.upload(value.getTextureSlot());
    }
}
