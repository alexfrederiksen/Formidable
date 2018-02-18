package com.frederiksen.formidable.rendering;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Shape {
    private List<Integer> vboIds;

    private int vaoId;

    private float[] vertices;
    private float[] texCoords;
    private int[] indices;

    public Shape(float[] vertices, float[] texCoords, int[] indices) {
        this.vertices = vertices;
        this.texCoords = texCoords;
        this.indices = indices;

        vboIds = new ArrayList<>();
    }

    public void upload() {
        vaoId = glGenVertexArrays();
        bind();
        // create vertex buffer
        createVbo(0, 2, vertices);
        // create tex coord buffer
        createVbo(1, 2, texCoords);
        // create indices buffer
        createIndexVbo(indices);
        // unbind buffer
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        unbind();
    }

    public int createVbo(int location, int componentSize, float[] data) {
        FloatBuffer buffer = null;
        int vboId = 0;
        try {
            vboId = glGenBuffers();
            vboIds.add(vboId);
            buffer = MemoryUtil.memAllocFloat(data.length);
            buffer.put(data).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
            glVertexAttribPointer(location, componentSize, GL_FLOAT, false, 0, 0);
        } finally {
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
        return vboId;
    }

    public int createIndexVbo(int[] data) {
        IntBuffer buffer = null;
        int vboId = 0;
        try {
            vboId = glGenBuffers();
            vboIds.add(vboId);
            buffer = MemoryUtil.memAllocInt(data.length);
            buffer.put(data).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        } finally {
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
        return vboId;
    }

    public void render() {
        bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        unbind();
    }

    public void bind() {
        glBindVertexArray(vaoId);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void dispose() {
        for (Integer i : vboIds) {
            glDeleteBuffers(i);
        }
        glDeleteVertexArrays(vaoId);
    }
}
