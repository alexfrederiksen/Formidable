package com.frederiksen.formidable.rendering;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private Texture texture;
    private int     framebufferId;
    private int     renderbufferId;
    private int     width;
    private int     height;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        texture = new Texture(width, height);
    }

    public void create() {
        framebufferId = glGenFramebuffers();
        bind();
        texture.upload();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                GL_TEXTURE_2D, texture.getTextureId(), 0);
        // create render buffer for depth and stencil
        renderbufferId = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderbufferId);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT,
                GL_RENDERBUFFER, renderbufferId);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        System.out.printf("Framebuffer status: %s%n", status == GL_FRAMEBUFFER_COMPLETE ? "GOOD" : "BAD");
        unbind();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void dispose() {
        glDeleteFramebuffers(framebufferId);
        glDeleteRenderbuffers(renderbufferId);
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public int getFramebufferId() {
        return framebufferId;
    }
}
