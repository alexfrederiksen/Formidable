package com.frederiksen.formidable.core;

public interface Game {
    public void init(Engine engine);
    public void update(float deltaTime);
    public void render();
    public void resize(int width, int height);
    public void dispose();
}
