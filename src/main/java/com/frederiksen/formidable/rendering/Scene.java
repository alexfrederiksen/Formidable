package com.frederiksen.formidable.rendering;

import com.frederiksen.formidable.cameras.Camera;
import com.frederiksen.formidable.game.SceneShader;
import com.frederiksen.formidable.lighting.Light;
import com.frederiksen.formidable.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

public class Scene {
    private static final String VERTEX_SHADER_CODE = "shaders/main.vs";
    private static final String FRAGMENT_SHADER_CODE = "shaders/main.fs";

    private Vector3f ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);

    private Light[]                  lights;
    private Map<Mesh, List<GameObj>> meshObjMap;
    private SceneShader              shader;
    private Camera                   camera;
    private PixelizedRenderer        pixelizedRenderer;

    public Scene(Camera camera, int width, int height) {
        this.camera = camera;

        shader = new SceneShader();
        meshObjMap = new HashMap<>();

        Random random = new Random();

        lights = new Light[SceneShader.MAX_LIGHTS];
        for (int i = 0; i < lights.length; i++) {
            lights[i] = new Light(
                    new Vector3f(random.nextFloat() * 20f, random.nextFloat() * 1f, random.nextFloat() * 20f),
                    new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()),
                    1f,
                    new Light.Attentuation(0f, 0.5f, 1f)
            );
        }

        pixelizedRenderer = new PixelizedRenderer(() -> draw(), width, height, 5);
    }

    public void init() {
        try {
            Utils.log("Creating shader program...");
            shader.createProgram();
            // create shaders
            Utils.log("Creating shaders...");
            shader.createVertexShader(Utils.readFile(VERTEX_SHADER_CODE));
            shader.createFragmentShader(Utils.readFile(FRAGMENT_SHADER_CODE));
            // bind attributes
            Utils.log("Binding attributes...");
            shader.bindAttribute("position", 0);
            shader.bindAttribute("texCoord", 1);
            shader.bindAttribute("normal", 2);
            // link program
            Utils.log("Linking program");
            shader.linkProgram();
            //shader.bind();
            // register uniforms
            Utils.log("Registering uniforms...");
            shader.registerUniforms();
            //shader.unbind();
            pixelizedRenderer.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        shader.bind();
        // setup some constant globals
        shader.specularPower.upload(100f);
        shader.globalAmbient.upload(ambientLight);
        shader.unbind();
    }

    public void registerMesh(Mesh mesh) {
        meshObjMap.put(mesh, new ArrayList<>());
    }

    public void registerObj(GameObj obj) {
        Mesh mesh = obj.getMesh();
        if (!meshObjMap.containsKey(mesh)) {
            System.err.println("Mesh is not registered.");
        } else {
            meshObjMap.get(mesh).add(obj);
        }
    }

    public void update() {
        lights[0].getPosition().set(camera.getPosition());
    }

    public void render() {
        pixelizedRenderer.render();
    }

    public void draw() {
        Matrix4f projection = camera.getProjection();
        Matrix4f view = camera.getView();
        // bind shader program
        shader.bind();
        // pre-render setup
        shader.projectionMatrix.upload(projection);
        // update lights with view matrix
        for (Light light : lights) {
            light.prepare(view);
        }
        shader.lights.upload(lights);
        // render objects by mesh
        for (Mesh mesh : meshObjMap.keySet()) {
            List<GameObj> objs = meshObjMap.get(mesh);
            if (objs.isEmpty()) continue;
            // renders the batch of objects
            mesh.renderBegin(shader);
            for (int i = objs.size() - 1; i >= 0; i--) {
                GameObj obj = objs.get(i);
                boolean meshChange = obj.getMesh() != mesh;
                // remove dead and changed objects
                if (obj.isDead() || meshChange) {
                    objs.remove(obj);
                    if (meshChange) registerObj(obj);
                    continue;
                }
                // setup uniforms before rendering
                obj.prepare(shader, view);
                // render the object
                mesh.render();
            }
            // end rendering
            mesh.renderEnd();
            // unbind shader program
            shader.unbind();

        }
    }

    public void dispose() {
        for (Mesh mesh : meshObjMap.keySet()) {
            mesh.dispose();
        }
        shader.dispose();
        pixelizedRenderer.dispose();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
