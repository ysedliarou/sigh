#version 140

in vec3 position;
in vec2 texCoord;
in vec3 normal;

out vec3 surfaceNormal;
out vec2 texCoord0;

out vec3 toCameraDirection;
out vec3 wPosition;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {

    vec4 worldPosition = transformationMatrix * vec4(position, 1);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    texCoord0 = texCoord;
    surfaceNormal = (transformationMatrix * vec4(normal, 1)).xyz;

    wPosition = worldPosition.xyz;
    toCameraDirection = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
}