package com.frederiksen.formidable.cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FPSCamera extends Camera {
    private float pitch;
    private float yaw;

    private static final Vector3f xAxis = new Vector3f();
    private static final Vector3f yAxis = new Vector3f();
    private static final Vector3f zAxis = new Vector3f();

    private Vector3f right;
    private Vector3f up;
    private Vector3f forward;

    public FPSCamera() {
        right = new Vector3f();
        up = new Vector3f();
        forward = new Vector3f();
    }

    public void set(float pitch, float yaw) {
        this.pitch = (float) Math.toRadians(pitch);
        this.yaw = (float) Math.toRadians(yaw);
    }

    public Vector3f getRight() {
        return right;
    }

    public Vector3f getUp() {
        return up;
    }

    public Vector3f getForward() {
        return forward;
    }

    @Override
    public Matrix4f getView() {
        // compute trig expressions
        float cosPitch = (float) Math.cos(pitch);
        float sinPitch = (float) Math.sin(pitch);
        float cosYaw = (float) Math.cos(yaw);
        float sinYaw = (float) Math.sin(yaw);
        // compute the camera axes
        xAxis.set(cosYaw, 0f, -sinYaw);
        yAxis.set(sinYaw * sinPitch, cosPitch, cosYaw * sinPitch);
        zAxis.set(sinYaw * cosPitch, -sinPitch, cosPitch * cosYaw);
        // set directional vectors
        forward.set(zAxis).mul(-1);
        right.set(xAxis);
        up.set(yAxis);

        setOrientation(xAxis, yAxis, zAxis);
        return view;
    }

    private static final Vector3f temp = new Vector3f();

    public void moveForward(float dst) {
        position.add(forward.x * dst, 0f,
                forward.z * dst);
    }

    public void moveBackward(float dst) {
        position.add(forward.x * -dst, 0f,
                forward.z * -dst);
    }

    public void moveRight(float dst) {
        position.add(right.x * dst, 0f,
                right.z * dst);
    }

    public void moveLeft(float dst) {
        position.add(right.x * -dst, 0f,
                right.z * -dst);
    }
}
