package com.frederiksen.formidable.lighting;

import com.frederiksen.formidable.rendering.Texture;
import org.joml.Vector4f;

public class Material {
    private Vector4f ambient;
    private Vector4f diffuse;
    private Vector4f specular;

    private float reflectance;

    private Texture texture;
    private int textureSlot;

    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, float reflectance) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.reflectance = reflectance;
    }

    public Material(Texture texture, int textureSlot, float reflectance) {
        this.ambient = new Vector4f();
        this.diffuse = new Vector4f();
        this.specular = new Vector4f();

        this.texture = texture;
        this.textureSlot = textureSlot;
        this.reflectance = reflectance;
    }

    public Material(Texture texture, int textureSlot) {
        this.ambient = new Vector4f();
        this.diffuse = new Vector4f();
        this.specular = new Vector4f();

        this.texture = texture;
        this.textureSlot = textureSlot;
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector4f ambient) {
        this.ambient = ambient;
    }

    public Vector4f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector4f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector4f specular) {
        this.specular = specular;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getTextureSlot() {
        return textureSlot;
    }

    public void setTextureSlot(int textureSlot) {
        this.textureSlot = textureSlot;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
