#version 140

in vec2 texCoord0;
in vec3 surfaceNormal;
in vec4 color0;

in vec3 wPosition;

out vec4 fragColor;

struct BaseLight {
    vec4 color;
    float intensity;
};

struct DirectionalLight {
    BaseLight baseLight;
    vec3 direction;
};

struct SpecularReflection {
    float intensity;
    float power;
};

uniform vec4 baseColor;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform vec3 eyePos;

uniform DirectionalLight directionalLight;
uniform SpecularReflection specularReflection;

vec4 calcLight(BaseLight baseLight, vec3 direction, vec3 normal) {

    vec4 diffuseColor = vec4(0,0,0,0);
    vec4 specularColor = vec4(0,0,0,0);

    float diffuseFactor = max(dot(normal, -direction), 0);

    if (diffuseFactor > 0) {
        diffuseColor = baseLight.color * baseLight.intensity * diffuseFactor;

        vec3 dirToEye = normalize(eyePos - wPosition);

        vec3 reflectionDirection = normalize(reflect(direction, normal));

        float specularFactor = max(dot(dirToEye, reflectionDirection), 0);
        specularFactor = pow(specularFactor, specularReflection.power);

        if (specularFactor > 0) {
            specularColor = baseLight.color * specularReflection.intensity * specularFactor;
        }

    }

    return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal) {
    return calcLight(directionalLight.baseLight, -directionalLight.direction, normal);
}

void main(void) {

    vec4 totalLight = vec4(ambientLight, 1);
    vec4 color = color0;
    vec4 textureColor = texture(sampler, texCoord0);

    if (textureColor != vec4(0,0,0,0)) {
//        color *= textureColor;
    }

    vec3 normal = normalize(surfaceNormal);

    totalLight += calcDirectionalLight(directionalLight, normal);

    fragColor = color * totalLight;

}