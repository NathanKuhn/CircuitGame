#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uv;

out vec3 outColor;
out vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 transformMatrix;
uniform mat4 rotationMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightDirection;
uniform vec3 lightColor;
uniform float lightIntensity;
uniform vec3 ambientColor;
uniform float ambientIntensity;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformMatrix * vec4(position, 1.0);
    vec3 worldNormal = (rotationMatrix * vec4(normal, 1.0)).xyz;
    float lightMultiplier = max(dot(worldNormal, -lightDirection), 0) * lightIntensity;
    outColor = lightMultiplier * lightColor + ambientColor * ambientIntensity;
    texCoord = uv;
}