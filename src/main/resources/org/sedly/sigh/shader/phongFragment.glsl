#version 140

const int MAX_POINT_LIGHTS = 4;
const int MAX_SPOT_LIGHTS = 4;

in vec2 texCoord0;
in vec3 surfaceNormal;
in vec4 color0;

in vec3 wPosition;

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

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    BaseLight baseLight;
    Attenuation attenuation;
    vec3 position;
    float range;
};

struct SpotLight {
    PointLight pointLight;
    vec3 direction;
    float cutoff;
};

uniform vec4 baseColor;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform vec3 eyePos;

uniform DirectionalLight directionalLight;
uniform SpecularReflection specularReflection;

uniform PointLight[MAX_POINT_LIGHTS] pointLights;
uniform SpotLight[MAX_SPOT_LIGHTS] spotLights;

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

vec4 calcPointLight(PointLight pointLight, vec3 normal) {

    vec3 lightDirection = wPosition - pointLight.position;
    float distanceToPoint = length(lightDirection);

    if (distanceToPoint > pointLight.range) {
        return vec4(0,0,0,0);
    }

    lightDirection = normalize(lightDirection);

    vec4 color = calcLight(pointLight.baseLight, lightDirection, normal);

    float attenuation = pointLight.attenuation.constant
            + pointLight.attenuation.linear * distanceToPoint
            + pointLight.attenuation.exponent * distanceToPoint * distanceToPoint
            + 0.0001;

    return color / attenuation;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 normal) {

    vec3 lightDirection = normalize(wPosition - spotLight.pointLight.position);
    float spotFactor = dot(lightDirection, spotLight.direction);

    vec4 color = vec4(0,0,0,0);

    if (spotFactor > spotLight.cutoff) {
        color = calcPointLight(spotLight.pointLight, normal)
                * (1.0 - (1.0 - spotFactor) / (1.0 - spotLight.cutoff));
    }

    return color;
}

void main(void) {

    vec4 totalLight = vec4(ambientLight, 1);
    vec4 color = color0;
    vec4 textureColor = texture(sampler, texCoord0);

    if (textureColor != vec4(0,0,0,0)) {
        color *= textureColor;
    }

    vec3 normal = normalize(surfaceNormal);

    totalLight += calcDirectionalLight(directionalLight, normal);

    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
        if (pointLights[i].baseLight.intensity > 0) {
            totalLight += calcPointLight(pointLights[i], normal);
        }
    }

    for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
        if (spotLights[i].pointLight.baseLight.intensity > 0) {
            totalLight += calcSpotLight(spotLights[i], normal);
        }
    }

    gl_FragColor = color * totalLight;
}