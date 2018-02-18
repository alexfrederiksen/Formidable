#version 120

attribute vec2 position;
attribute vec2 texCoord;

varying vec2 fragTexCoord;

void main() {
    gl_Position = vec4(position, 0.0, 1.0);
    fragTexCoord = texCoord;
}