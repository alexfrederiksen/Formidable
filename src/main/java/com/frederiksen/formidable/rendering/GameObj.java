package com.frederiksen.formidable.rendering;

import com.frederiksen.formidable.game.SceneShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObj {
    private final Vector3f position;
    private final Vector3f rotation;
    private float scale;

    private Mesh mesh;
    private boolean dead;

    private static final Matrix4f transform = new Matrix4f();

    public GameObj() {
        position = new Vector3f();
        rotation = new Vector3f();
        scale = 1f;
        dead = false;
    }

    public GameObj(Mesh mesh, float x, float y, float z) {
        this();
        this.mesh = mesh;
        position.set(x, y, z);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    private static final Matrix4f temp = new Matrix4f();

    public void prepare(SceneShader shader, Matrix4f view) {
        view.mul(getTransform(), temp);
        shader.modelViewMatrix.upload(temp);
    }

    public Matrix4f getTransform() {
        transform.identity()
                .translate(position)
                .rotateX((float) Math.toRadians(-rotation.x))
                .rotateY((float) Math.toRadians(-rotation.y))
                .rotateZ((float) Math.toRadians(-rotation.z))
                .scale(scale);
        return transform;
    }
}
