package com.frederiksen.formidable.rendering;

import com.frederiksen.formidable.AABox;
import com.frederiksen.formidable.game.SceneShader;
import com.frederiksen.formidable.lighting.Material;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    protected float[] vertices;
    protected float[] texCoords;
    protected float[] normals;
    protected int[] indices;

    private int vaoId;
    private List<Integer> vboIds;

    private Material material;

    public Mesh() {
        vboIds = new ArrayList<>();
    }

    public Mesh(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
        this.vertices = vertices;
        this.texCoords = texCoords;
        this.normals = normals;
        this.indices = indices;

        vboIds = new ArrayList<>();
    }

    public void upload() {
        // create vao
        vaoId = glGenVertexArrays();
        // bind to it
        glBindVertexArray(vaoId);
        // create vertex vbo
        createVbo(0, 3, vertices);
        // create texture coord vbo
        createVbo(1, 2, texCoords);
        // create normal vbo
        createVbo(2, 3, normals);
        // create index vbo
        createIndexVbo(indices);
        // unbind any vbo
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // unbind vao
        glBindVertexArray(0);
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


    /**
     * Renders mesh with a single object
     * NOTE: should be called between renderBegin() and renderEnd()
     */
    public void render() {
        // draw triangles
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }

    public void renderBegin(SceneShader shader) {
        // upload materials
        shader.material.upload(material);
        // enable texture
        if (material.hasTexture()) {
            // activate texture
            glActiveTexture(GL_TEXTURE0);
            // bind texture
            material.getTexture().bind();
        }
        // enable attributes
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    public void renderEnd() {
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        if (material.hasTexture()) {
            material.getTexture().unbind();
        }
    }

    public void dispose() {
        glDisableVertexAttribArray(0);
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public AABox computeAABox(AABox box) {
        float minX = vertices[0];
        float minY = vertices[1];
        float minZ = vertices[2];
        float maxX = vertices[0];
        float maxY = vertices[1];
        float maxZ = vertices[2];
        // compute min and max for each axis
        for (int i = 1; i < vertices.length / 3; i++) {
            minX = Math.min(minX, vertices[i * 3]);
            maxX = Math.max(maxX, vertices[i * 3]);
            minY = Math.min(minY, vertices[i * 3 + 1]);
            maxY = Math.max(maxY, vertices[i * 3 + 1]);
            minZ = Math.min(minZ, vertices[i * 3 + 2]);
            maxZ = Math.max(maxZ, vertices[i * 3 + 2]);
        }
        // create box from min max ranges
        box.set(minX, maxX, minY, maxY, minZ, maxZ);
        return box;
    }
}
