#version 140

in vec3 position;
in vec2 texCoord;
in vec3 normal;

out vec3 surfaceNormal;
out vec2 texCoord0;
out vec4 color0;

out vec3 wPosition;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

float p(float a, float b) {
    return cos(clamp((a-b) / (a + b), 0.1, 0.9)) * 0.5;
}

void main(void) {

    vec4 worldPosition = transformationMatrix * vec4(position, 1);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    texCoord0 = texCoord;
    surfaceNormal = (transformationMatrix * vec4(normal, 0)).xyz;

    wPosition = worldPosition.xyz;
    color0 = vec4(p(position.x, position.y), p(position.y, position.z), p(position.z, position.x), 1);

}