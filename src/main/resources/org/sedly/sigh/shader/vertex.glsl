#version 140

in vec3 position;
in vec2 texCoord;
in vec3 normal;

out vec3 colour;
out vec2 pass_texCoord;

out vec3 surfaceNormal;
out vec3 toLightDirection;

out vec3 toCameraDirection;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightDirection;

float p(float a, float b) {
    return cos(clamp((a-b) / (a + b), 0.1, 0.9)) * 0.5;
}

void main(void) {

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_texCoord = texCoord;
    colour = vec3(p(position.x, position.y), p(position.y, position.z), p(position.z, position.x));

    surfaceNormal = (transformationMatrix * vec4(normal, 1.0)).xyz;;
    toLightDirection = -(lightDirection - worldPosition.xyz);
    toCameraDirection = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}