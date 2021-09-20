#version 330

in vec2 texCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec4 color;
    vec3 position; // in view coordinates
    float intensity;
    Attenuation att;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
//uniform vec3 camera_pos;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColors(Material mat, vec2 textureCoord)
{
    if (mat.hasTexture == 1)
    {
        vec4 textureColor = texture(texture_sampler, textureCoord);
        ambientC = textureColor * mat.ambient;
        diffuseC = textureColor * mat.diffuse;
        speculrC = mat.specular;
    }
    else
    {
        ambientC = mat.ambient;
        diffuseC = mat.diffuse;
        speculrC = mat.specular;
    }
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec4 diffuseColor = vec4(0.0, 0.0, 0.0, 0.0);
    vec4 specularColor = vec4(0.0, 0.0, 0.0, 0.0);

    // Diffuse
    vec3 light_direction = light.position - position;
    vec3 to_light_source = normalize(light_direction);
    float diffuseFactor = max(dot(normal, to_light_source), 0.0);
    diffuseColor = diffuseC * light.color * light.intensity * diffuseFactor;

    // Specular
    vec3 camera_direction = normalize(-position);
    vec3 from_light_source = -to_light_source;
    vec3 reflected_light = normalize(reflect(from_light_source, normal));
    float specularFactor = max(dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = speculrC * specularFactor * material.reflectance * light.color;

    // Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;

    return (diffuseColor + specularColor) / attenuationInv;
}

void main()
{
    setupColors(material, texCoord);

    vec4 diffuseSpecularComp = calcPointLight(pointLight, mvVertexPos, mvVertexNormal);

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}