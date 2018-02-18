package com.frederiksen.formidable.rendering;

import com.frederiksen.formidable.lighting.Light;
import com.frederiksen.formidable.lighting.Material;
import com.frederiksen.formidable.rendering.uniforms.Uniform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private int vertexShaderId;
    private int fragmentShaderId;
    private int programId;

    public void createProgram() throws Exception {
        // create the program
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Failed to create shader program.");
        }
    }

    public void createVertexShader(String source) throws Exception {
        vertexShaderId = createShader(source, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String source) throws Exception {
        fragmentShaderId = createShader(source, GL_FRAGMENT_SHADER);
    }

    public void bindAttribute(String name, int location) throws Exception {
        if (vertexShaderId != 0) {
            glBindAttribLocation(vertexShaderId, location, name);
        } else {
            throw new Exception("Vertex shader must be created to bind attributes.");
        }
    }

    public int createShader(String source, int type) throws Exception {
        if (programId == 0) {
            throw new Exception("Shader program must be created before individule shaders.");
        }
        // create the shader
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            throw new Exception(String.format("Failed to create %s shader.",
                    type == GL_VERTEX_SHADER ? "vertex" : "fragment"));
        }
        // attach source to shader
        glShaderSource(shaderId, source);
        // compile shader
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception(String.format("Failed to compile %s shader code: %s",
                    type == GL_VERTEX_SHADER ? "vertex" : "fragment",
                    glGetShaderInfoLog(shaderId, 1024)));
        }
        // attach shader to this program
        glAttachShader(programId, shaderId);
        return shaderId;
    }

    public void linkProgram() throws Exception {
        // link the program
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Failed to link shader code: " + glGetProgramInfoLog(programId, 1024));
        }
        // detach any shaders that were created
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }
        // validate the program (remove on production stage)
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }


    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        // unbind if in use
        unbind();
        // remove program
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public int getProgramId() {
        return programId;
    }
}
