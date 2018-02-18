package com.frederiksen.formidable.rendering;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Texture {
    private int textureId;
    private int width;
    private int height;

    private boolean fromFile;
    private ByteBuffer dataBuffer;

    public Texture(String filename) {
        fromFile = true;
        try {
            PNGDecoder decoder = new PNGDecoder(
                    new FileInputStream(new File(filename)));
            width = decoder.getWidth();
            height = decoder.getHeight();
            // create buffer
            dataBuffer = ByteBuffer.allocateDirect(4 * width * height);
            // load texture into buffer
            decoder.decode(dataBuffer, width * 4, PNGDecoder.Format.RGBA);
            // reset position to 0
            dataBuffer.flip();
        } catch (IOException e) {
            System.err.println(String.format("Failed to load texture [%s].", filename));
            if (dataBuffer != null) {
                memFree(dataBuffer);
            }
            dataBuffer = null;
        }
    }

    public Texture(int width, int height) {
        fromFile = false;
        this.width = width;
        this.height = height;
    }

    public void upload() {
        if (isUploaded()) {
            // already uploaded
            return;
        }
        if (fromFile && dataBuffer == null) {
            System.err.println("Texture was not properly loaded.");
            return;
        }
        // create texture
        textureId = glGenTextures();
        // bind to it
        bind();
        // set unpacking method (by 1 byte)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        // setup nearest neightbor
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // upload data to graphics card
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, fromFile ? dataBuffer : null);
        // free data if used
        if (fromFile) memFree(dataBuffer);
        // unbind it
        unbind();
    }

    public boolean isUploaded() {
        return textureId != 0;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void dispose() {
        glDeleteTextures(textureId);
    }
}
