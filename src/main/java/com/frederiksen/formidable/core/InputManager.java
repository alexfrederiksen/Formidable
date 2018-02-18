package com.frederiksen.formidable.core;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputManager {
    private static final double MAX_MOUSE_DELTA = 100.0;

    private Window window;

    private Vector2d mouseDelta;
    private Vector2d mouseLastPos;
    private Vector2d mousePos;

    private boolean inWindow;

    public interface OnKeyHandler {
        void handle(int key, int action);
    }

    public List<OnKeyHandler> OnKeyHandlers;

    public InputManager(Window window) {
        this.window = window;

        mouseDelta = new Vector2d();
        mouseLastPos = new Vector2d();
        mousePos = new Vector2d();

        OnKeyHandlers = new ArrayList<>();
    }

    public void onKeyUpdate(int key, int action) {
        for (OnKeyHandler handler : OnKeyHandlers) {
            handler.handle(key, action);
        }
    }

    public void onMousePosUpdate(double x, double y) {
        mousePos.set(x, y);
    }

    public void onMouseEnter(boolean enter) {
        inWindow = enter;
    }

    public void onMouseButtonUpdate(int button, int action) {

    }

    public void update() {
        mouseDelta.set(
                mousePos.x - mouseLastPos.x,
                mousePos.y - mouseLastPos.y);
        mouseLastPos.set(mousePos);
    }

    public void addOnKeyHandler(OnKeyHandler handler) {
        OnKeyHandlers.add(handler);
    }

    public void removeOnKeyHandler(OnKeyHandler handler) {
        OnKeyHandlers.remove(handler);
    }

    public boolean isKeyPressed(int keyCode) {
        return getKeyState(keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased(int keyCode) {
        return getKeyState(keyCode) == GLFW_RELEASE;
    }

    public int getKeyState(int keyCode) {
        return GLFW.glfwGetKey(window.getWindowId(), keyCode);
    }

    public Vector2d getMouseDelta() {
        return mouseDelta;
    }

    public Vector2d getMousePos() {
        return mousePos;
    }

    public boolean isInWindow() {
        return inWindow;
    }
}
