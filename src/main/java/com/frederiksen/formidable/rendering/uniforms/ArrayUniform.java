package com.frederiksen.formidable.rendering.uniforms;

import com.frederiksen.formidable.rendering.ShaderProgram;

import java.lang.reflect.Array;

public class ArrayUniform<V, U extends Uniform<V>> extends Uniform<V[]> {
    private U[] uniforms;
    private int size;
    private UniformFactory<U> factory;

    public interface UniformFactory<A> {
        public A newInstance();
    }

    public ArrayUniform(UniformFactory<U> factory, int size) {
        this.factory = factory;
        this.size = size;
        uniforms = (U[]) Array.newInstance(factory.newInstance().getClass(), size);
        // initialize instances
        for (int i = 0; i < size; i++) {
            uniforms[i] = factory.newInstance();
        }
    }

    @Override
    public void register(ShaderProgram program, String name) throws Exception {
        // create them
        for (int i = 0; i < size; i++) {
            uniforms[i].register(program, String.format("%s[%d]", name, i));
        }
    }

    @Override
    public void upload(V[] values) {
        if (values.length != size) {
            System.err.println("Arrays must be same size.");
            return;
        }
        // upload each value
        for (int i = 0; i < size; i++) {
            uniforms[i].upload(values[i]);
        }
    }

    public void set(int index, V value) {
        uniforms[index].upload(value);
    }
}
