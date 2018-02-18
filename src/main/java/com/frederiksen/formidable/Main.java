package com.frederiksen.formidable;

import com.frederiksen.formidable.cameras.FPSCamera;
import com.frederiksen.formidable.controllers.FPSCameraController;
import com.frederiksen.formidable.core.Engine;
import com.frederiksen.formidable.core.Game;
import com.frederiksen.formidable.core.InputManager;
import com.frederiksen.formidable.lighting.Material;
import com.frederiksen.formidable.loaders.OBJLoader;
import com.frederiksen.formidable.rendering.*;
import com.frederiksen.formidable.rendering.uniforms.ArrayUniform;
import com.frederiksen.formidable.rendering.uniforms.IntegerUniform;
import com.frederiksen.formidable.utils.Utils;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class Main {
    public static void main(String[] args) {

        int width = 800;
        int height = 640;
        if (args.length >= 3) {
            width = Integer.parseInt(args[1]);
            height = Integer.parseInt(args[2]);
        }

        System.out.printf("Creating window %d x %d...%n", width, height);

        Engine engine = new Engine.Builder(new TestGame(width, height))
                .setTitle("My Test Game")
                .setWidth(width)
                .setHeight(height)
                .setvSync(false)
                .build();
        // reset that engine
        engine.start();
    }

    public static class TestGame implements Game {
        private Scene scene;
        private FPSCamera camera;
        private FPSCameraController controller;
        private GameObj obj;
        private Engine engine;
        private Material material;

        public TestGame(int width, int height) {
            camera = new FPSCamera();
            controller = new FPSCameraController(camera, 3f);
            camera.setPerspective(60, 800, 640, 0.1f, 100f);
            //camera.setRotation(0f, 180f, 0f);
            scene = new Scene(camera, width, height);
        }

        @Override
        public void init(Engine engine) {
            this.engine = engine;

            engine.getInputManager().addOnKeyHandler((key, action) -> {
               if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                   engine.getWindow().setShouldClose(true);
               }
               if (key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS) {
                   engine.getWindow().toggleCursorEnable();
               }
            });

            scene.init();

            Mesh mesh = OBJLoader.importModel("models/cube.obj");
            Texture blockTex = new Texture("textures/block.png");
            blockTex.upload();
            //mesh.setMaterial(new Material(blockTex, 0, 1f));

            mesh.setMaterial(new Material(
                    new Vector4f(1f, 1f, 1f, 1f),
                    new Vector4f(1f, 1f, 1f, 1f),
                    new Vector4f(1f, 1f, 1f, 1f),
                    1.0f));

            mesh.upload();
            scene.registerMesh(mesh);

            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    scene.registerObj(
                            new GameObj(mesh, x * 4, 0, y * 4)
                    );
                }
            }
        }

        public void createObjs(Mesh mesh) {

        }

        @Override
        public void update(float deltaTime) {
            //obj.getRotation().y = (obj.getRotation().y + deltaTime * 30f) % 360f;
            //obj.getRotation().z = (obj.getRotation().y + deltaTime * 30f) % 360f;
            controller.update(deltaTime, engine.getInputManager());
            scene.update();
        }

        @Override
        public void render() {
            scene.render();
        }

        @Override
        public void resize(int width, int height) {
            camera.setScreenSize(width, height);
        }

        @Override
        public void dispose() {
            scene.dispose();
        }
    }
}
