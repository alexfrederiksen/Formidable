package com.frederiksen.formidable.lighting;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Light {
    private Vector3f viewPosition; // used in fragment shader

    private Vector3f position;
    private Vector3f color;
    private float intensity;

    private Attentuation attentuation;

    public Light(Vector3f position, Vector3f color, float intensity, Attentuation attentuation) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
        this.attentuation = attentuation;

        viewPosition = new Vector3f();
    }

    private static final Vector4f temp = new Vector4f();

    public void prepare(Matrix4f view) {
        temp.set(position, 1f);
        temp.mul(view);
        viewPosition.set(temp.x, temp.y, temp.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getViewPosition() {
        return viewPosition;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public Attentuation getAttentuation() {
        return attentuation;
    }

    public static class Attentuation {
        private float constant;
        private float linear;
        private float exponent;

        public Attentuation(float exponent, float linear, float constant) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public float getLinear() {
            return linear;
        }

        public float getExponent() {
            return exponent;
        }
    }

}
