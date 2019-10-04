#version 140

in vec2 pass_texCoord;
in vec3 surfaceNormal;
in vec3 toLightDirection;
in vec3 colour;
in vec3 toCameraDirection;

out vec4 out_Colour;

uniform sampler2D texSampler;

uniform vec3 lightColour;
uniform float lightIntensity;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightDirection = normalize(toLightDirection);

    float brightness = max(dot(unitNormal, unitLightDirection), 0.0) * lightIntensity;
    vec3 diffuse = brightness * lightColour;

    vec3 toCameraDirectionUnit = normalize(toCameraDirection);
    vec3 lightDirection = -unitLightDirection;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = max(dot(reflectedLightDirection, toCameraDirectionUnit), 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;



    out_Colour = vec4(diffuse, 1.0) * vec4(colour, 1.0) + vec4(finalSpecular, 1.0);
//    out_Colour = vec4(diffuse, 1.0) * texture(texSampler, pass_texCoord) + vec4(finalSpecular, 1.0);
//    out_Colour = vec4(colour, 1.0);
}