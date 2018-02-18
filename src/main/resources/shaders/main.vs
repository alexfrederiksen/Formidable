#version 120

attribute vec3 position;
attribute vec2 texCoord;
attribute vec3 normal;

uniform mat4 projection;
uniform mat4 modelView;

varying vec2 fragTexCoord;
varying vec3 fragPosition;
varying vec3 fragNormal;

void main() {
    vec4 mvPos = modelView * vec4(position, 1.0);
    gl_Position = projection * mvPos;
    // set fragment variables
    fragTexCoord = texCoord;
    fragPosition = mvPos.xyz;
    fragNormal = normalize(modelView * vec4(normal, 0.0)).xyz;
}