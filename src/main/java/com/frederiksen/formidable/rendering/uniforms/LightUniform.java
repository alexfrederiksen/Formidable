package com.frederiksen.formidable.rendering.uniforms;

import com.frederiksen.formidable.lighting.Light;
import com.frederiksen.formidable.rendering.ShaderProgram;

public class LightUniform extends Uniform<Light> {
    private Vector3fUniform position = new Vector3fUniform();
    private Vector3fUniform color = new Vector3fUniform();
    private FloatUniform intensity = new FloatUniform();
    private FloatUniform attConstant = new FloatUniform();
    private FloatUniform attLinear = new FloatUniform();
    private FloatUniform attExponent = new FloatUniform();

    @Override
    public void register(ShaderProgram program, String name) throws Exception {
        position.register(program, name + ".position");
        color.register(program, name + ".color");
        intensity.register(program, name + ".intensity");
        attConstant.register(program, name + ".att.constant");
        attLinear.register(program, name + ".att.linear");
        attExponent.register(program, name + ".att.exponent");
    }

    @Override
    public void upload(Light value) {
        position.upload(value.getViewPosition());
        color.upload(value.getColor());
        intensity.upload(value.getIntensity());
        attConstant.upload(value.getAttentuation().getConstant());
        attLinear.upload(value.getAttentuation().getLinear());
        attExponent.upload(value.getAttentuation().getExponent());
    }
}
