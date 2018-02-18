package com.frederiksen.formidable.rendering;

import com.frederiksen.formidable.rendering.uniforms.IntegerUniform;
import com.frederiksen.formidable.utils.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class PixelizedRenderer {
    private static final String VERTEX_SHADER_SOURCE = "shaders/fbo_render.vs";
    private static final String FRAGMENT_SHADER_SOURCE = "shaders/fbo_render.fs";

    private int screenWidth;
    private int screenHeight;
    private int renderWidth;
    private int renderHeight;

    private Renderer renderer;
    private ShaderProgram shader;
    private Framebuffer framebuffer;
    private Shape rectangle;

    private IntegerUniform textureSlotUniform;

    public interface Renderer {
        void render();
    }

    public PixelizedRenderer(Renderer renderer, int width, int height, int pixelSize) {
        this.renderer = renderer;
        this.screenWidth = width;
        this.screenHeight = height;

        renderWidth = width / pixelSize;
        renderHeight = height / pixelSize;

        shader = new ShaderProgram();
        framebuffer = new Framebuffer(renderWidth, renderHeight);
        rectangle = new Shape(new float[] {
                -1, -1,  -1, 1,  1, 1,  1, -1
        }, new float[] {
                0, 0,  0, 1,  1, 1,  1, 0
        }, new int[] {
                3, 1, 0, 2, 1, 3
        });

        textureSlotUniform = new IntegerUniform();
    }

    public void init() throws Exception {
        shader.createProgram();
        shader.createVertexShader(Utils.readFile(VERTEX_SHADER_SOURCE));
        shader.createFragmentShader(Utils.readFile(FRAGMENT_SHADER_SOURCE));
        shader.bindAttribute("position", 0);
        shader.bindAttribute("texCoord", 1);
        shader.linkProgram();

        textureSlotUniform.register(shader, "texture");
        textureSlotUniform.upload(0);

        framebuffer.create();
        rectangle.upload();
    }

    public void render() {
        // pipe rendering through our framebuffer
        framebuffer.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // lower res
        glViewport(0, 0, renderWidth, renderHeight);
        // render scene normally
        renderer.render();
        // reture res to normal
        glViewport(0, 0, screenWidth, screenHeight);
        framebuffer.unbind();
        // draw fbo to screen
        shader.bind();
        // bind fbo texture
        glActiveTexture(GL_TEXTURE0);
        framebuffer.getTexture().bind();
        // render rectangle of screen
        rectangle.render();
        // reset
        framebuffer.getTexture().unbind();
        shader.unbind();
    }

    public void dispose() {
        shader.dispose();
        framebuffer.dispose();
        rectangle.dispose();
    }
}
