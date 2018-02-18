package com.frederiksen.formidable.controllers;

import com.frederiksen.formidable.core.InputManager;
import com.frederiksen.formidable.cameras.FPSCamera;
import org.lwjgl.glfw.GLFW;

import static com.frederiksen.formidable.utils.Utils.clamp;

public class FPSCameraController {
    private static final float MOUSE_SENSITIVITY = 10f;
    private static final float MIN_PITCH = -45f;
    private static final float MAX_PITCH = 45f;

    private FPSCamera camera;
    private float speed;

    private float pitch;
    private float yaw;

    public FPSCameraController(FPSCamera camera, float speed) {
        this.camera = camera;
        this.speed = speed;
    }

    public void update(float deltaTime, InputManager input) {
        if (input.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.moveLeft(speed * deltaTime);
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.moveBackward(speed * deltaTime);
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.moveRight(speed * deltaTime);
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.moveForward(speed * deltaTime);
        }

        pitch = (pitch + (float) input.getMouseDelta().y * -MOUSE_SENSITIVITY * deltaTime) % 360f;
        pitch = clamp(pitch, MIN_PITCH, MAX_PITCH);
        yaw = (yaw + (float) input.getMouseDelta().x * -MOUSE_SENSITIVITY * deltaTime) % 360f;

        camera.set(pitch, yaw);
    }
}
