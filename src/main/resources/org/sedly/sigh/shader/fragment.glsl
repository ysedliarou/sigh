#version 140

in vec2 pass_texCoords;
in vec3 surfaceNormal;
in vec3 toLightDirection;
in vec3 colour;
in vec3 toCameraDirection;

out vec4 out_Colour;

uniform sampler2D texSampler;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightDirection = normalize(toLightDirection);

    float nDot1 = dot(unitNormal, unitLightDirection);
    float brightness = max(nDot1, 0.0);
    vec3 diffuse = brightness * lightColour;

    vec3 toCameraDirectionUnit = normalize(toCameraDirection);
    vec3 lightDirection = -unitLightDirection;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = max(dot(reflectedLightDirection, toCameraDirectionUnit), 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;



//    out_Colour = vec4(diffuse, 1.0) * vec4(colour, 0.0) + vec4(finalSpecular, 1.0);
    out_Colour = vec4(diffuse, 1.0) * texture(texSampler, pass_texCoords) + vec4(finalSpecular, 1.0);
//    out_Colour = vec4(colour, 0.0);
}