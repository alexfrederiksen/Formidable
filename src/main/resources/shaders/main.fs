#version 120

const int MAX_LIGHTS = 10;

struct Attentuation {
    float constant;
    float linear;
    float exponent;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;

    float reflectance;

    int hasTexture;
    sampler2D texture;
};

struct Light {
    vec3 position; // in view coordinates
    vec3 color;
    float intensity;

    Attentuation att;
};

uniform Light lights[MAX_LIGHTS];
uniform Material material;
uniform vec3 globalAmbient;
uniform float specularPower;

varying vec2 fragTexCoord;
varying vec3 fragPosition;
varying vec3 fragNormal;

vec4 computePointLight(Light light, vec4 diffuse, vec4 specular) {
    // compute diffuse
    vec3 toLight = light.position - fragPosition;
    vec3 toLightNormalized = normalize(toLight);
    float diffuseFactor = max(dot(toLightNormalized, fragNormal), 0.0);

    // compute specular
    vec3 reflected = reflect(-toLightNormalized, fragNormal);
    vec3 toCamera = -fragPosition; // camera is at origin (since in camera space)
    float specularFactor = max(dot(reflected, normalize(toCamera)), 0.0);
    // apply specular power
    specularFactor = pow(specularFactor, specularPower);

    // compute light total
    vec4 totalColor = (diffuse * (diffuseFactor * light.intensity)
        + specular * (specularFactor * material.reflectance * light.intensity)) * vec4(light.color, 1.0);
    // apply attentuation (dims with distance)
    float dist = length(toLight);
    totalColor /= (light.att.constant
        + light.att.linear * dist
        + light.att.exponent * dist * dist);

    return totalColor;
}

void main() {
    vec4 final = vec4(0.0, 0.0, 0.0, 0.0);

    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    // define material colors
    if (material.hasTexture == 1) {
        ambient = texture2D(material.texture, fragTexCoord);
        diffuse = ambient;
        specular = ambient;
    } else {
        ambient = material.ambient;
        diffuse = material.diffuse;
        specular = material.specular;
    }
    // process lights
    for (int i = 0; i < MAX_LIGHTS; i++) {
        final += computePointLight(lights[i], diffuse, specular);
    }
    // add the ambient light
    final += material.ambient * vec4(globalAmbient, 1.0);
    // apply fog effect
    gl_FragColor = mix(final, vec4(0.0, 0.0, 0.0, 1.0),
        clamp(-fragPosition.z / 10.0, 0.0, 1.0));
}