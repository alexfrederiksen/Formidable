package com.frederiksen.formidable.cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {
    private float fov;
    private float aspectRatio;
    private float near;
    private float far;

    protected Vector3f position;

    protected static final Matrix4f projection = new Matrix4f();
    protected static final Matrix4f view = new Matrix4f();

    public Camera() {
        position = new Vector3f();
    }

    public void setPerspective(float fov, int width, int height, float near, float far) {
        this.fov = (float) Math.toRadians(fov);
        this.aspectRatio = (float) width / height;
        this.near = near;
        this.far = far;
    }

    public void setOrientation(Vector3f xAxis, Vector3f yAxis, Vector3f zAxis) {
        /*
         * Compound view matrix
         * [ xAxis.x xAxis.y xAxis.z -xAxis.dot(pos) ]
         * [ yAxis.x yAxis.y yAxis.z -yAxis.dot(pos) ]
         * [ zAxis.x zAxis.y zAxis.z -zAxis.dot(pos) ]
         * [       0       0       0               1 ]
         */
        view.set(
                xAxis.x, yAxis.x, zAxis.x, 0,
                xAxis.y, yAxis.y, zAxis.y, 0,
                xAxis.z, yAxis.z, zAxis.z, 0,
                -xAxis.dot(position),
                -yAxis.dot(position),
                -zAxis.dot(position), 1);
    }

    public void setScreenSize(int width, int height) {
        aspectRatio = (float) width / height;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public abstract Matrix4f getView();

    public Matrix4f getProjection() {
        return projection.setPerspective(fov, aspectRatio, near, far);
    }

}
