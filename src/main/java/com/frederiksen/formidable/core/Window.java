package com.frederiksen.formidable.core;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private String title;
    private int width;
    private int height;

    private long windowId;
    private boolean resized;
    private boolean vSync;

    private boolean cursorEnable = true;

    private InputManager inputManager;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;

        resized = false;
    }

    public void init() {
        // setup error callback
        GLFWErrorCallback.createPrint(System.err).set();
        // initialize GLFW
        boolean success = glfwInit();
        if (!success) {
            throw new IllegalStateException("Failed to initialize GLFW.");
        }
        // setup hints
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        // create window
        windowId = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowId == NULL) {
            throw new RuntimeException("Failed to create GLFW window.");
        }
        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowId, (window, width, height) -> {
            this.width = width;
            this.height = height;
            resized = true;
        });
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowId,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
        // Make the OpenGL context current
        glfwMakeContextCurrent(windowId);
        if (vSync) {
            // Enable v-sync
            glfwSwapInterval(1);
        }
        // Make the window visible
        glfwShowWindow(windowId);
        GL.createCapabilities();
        // Set the clear color
        setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // enable depth test (avoids stupid)
        glEnable(GL_DEPTH_TEST);
        // enable face culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public void setCursorEnable(boolean enable) {
        cursorEnable = enable;
        if (enable) {
            glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        } else {
            glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
    }

    public void toggleCursorEnable() {
        setCursorEnable(!cursorEnable);
    }

    public boolean isCursorEnable() {
        return cursorEnable;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowId);
    }

    public void setShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(windowId, shouldClose);
    }

    public void render() {
        glfwSwapBuffers(windowId);
        glfwPollEvents();
    }

    /**
     * Set the input manager for this window
     * NOTE: invoke AFTER init()
     * @param manager
     */
    public void setInputManager(InputManager manager) {
        inputManager = manager;
        glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
            manager.onKeyUpdate(key, action);
        });
        glfwSetCursorPosCallback(windowId, (window, x, y) -> {
            manager.onMousePosUpdate(x, y);
        });
        glfwSetCursorEnterCallback(windowId, (window, enter) -> {
            manager.onMouseEnter(enter);
        });
        glfwSetMouseButtonCallback(windowId, (window, button, action, mode) -> {
            manager.onMouseButtonUpdate(button, action);
        });
    }

    public long getWindowId() {
        return windowId;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
