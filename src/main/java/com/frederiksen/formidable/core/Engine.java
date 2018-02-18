package com.frederiksen.formidable.core;

import com.frederiksen.formidable.utils.Timer;
import com.frederiksen.formidable.utils.Utils;

import static org.lwjgl.opengl.GL11.*;

public class Engine {
    private static final double FPS_TARGET = 60f;
    private static final boolean LOG_FPS = true;

    private Timer fpsLogTimer;
    private Game game;
    private Window window;
    private InputManager inputManager;

    private double framerate;
    private boolean running;

    public Engine(Game game, String title, int width, int height,
                  boolean vSync) {
        this.game = game;
        this.window = new Window(title, width, height, vSync);

        inputManager = new InputManager(window);
        running = true;

        fpsLogTimer = new Timer();
        fpsLogTimer.setTrigger(2.0);
        fpsLogTimer.setAutoReset(true);
        fpsLogTimer.setTriggerEvent(() -> {
            Utils.log("FPS: " + framerate);
        });
    }

    public void start() {
        try {
            init();
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // make sure dispose is called no matter what
            dispose();
        }
    }

    public void init() {
        // initialize window (must be done first!)
        window.init();
        // set the input manager
        window.setInputManager(inputManager);
        // initialize game
        game.init(this);
    }

    public void loop() {
        Timer loopTimer = new Timer();
        loopTimer.reset();

        double targetDeltaTime = 1f / FPS_TARGET;
        double accumulator = 0f;
        while(running && !window.shouldClose()) {
            double deltaTime = loopTimer.getElapsed();
            loopTimer.reset();
            // update the engine's current fps
            framerate = 1f / deltaTime;
            // add to accumulator
            accumulator += deltaTime;
            // perform updates that fit in accumulator
            while (accumulator >= targetDeltaTime) {
                update((float) targetDeltaTime);
                accumulator -= targetDeltaTime;
            }
            // do rendering
            render();
            // if v-sync is not enabled, use our custom sync
            if (!window.isvSync()) {
                sync(loopTimer, targetDeltaTime);
            }
        }
    }

    /**
     * Waits the remaining time for a loop to cap the
     * framerate (v-sync would take care of this)
     * @param loopTimer timer used for computing elapse
     * @param target target delta time for a loop (1 / framerate)
     */
    public void sync(Timer loopTimer, double target) {
        // wait in 1ms intervals for the remaining time
        while (loopTimer.getElapsed() < target) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(float deltaTime) {
        if (LOG_FPS) fpsLogTimer.update();
        inputManager.update();
        game.update(deltaTime);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render() {
        clear();
        // resize viewport
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
            game.resize(window.getWidth(), window.getHeight());
        }
        // render game graphics
        game.render();
        // buffer swapping
        window.render();
    }

    public void dispose() {
        game.dispose();
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public Window getWindow() {
        return window;
    }

    public static class Builder {
        private Game game;
        private String title = "Game";
        private int width = 800;
        private int height = 640;
        private boolean vSync = false;

        public Builder(Game game) {
            this.game = game;
        }

        public Engine build() {
            return new Engine(game, title, width, height,
                    vSync);
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setvSync(boolean vSync) {
            this.vSync = vSync;
            return this;
        }

    }
}
