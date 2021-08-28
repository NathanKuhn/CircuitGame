#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;

out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 transformMatrix;
uniform mat4 rotationMatrix;

uniform vec3 lightDirection;
uniform vec3 lightColor;
uniform float lightIntensity;
uniform vec3 ambientColor;
uniform float ambientIntensity;

void main()
{
    gl_Position = projectionMatrix * transformMatrix * vec4(position, 1.0);
    vec3 worldNormal = (rotationMatrix * vec4(normal, 1.0)).xyz;
    vec3 color = vec3(1.0, 1.0, 1.0);
    color.x *= lightColor.x / lightColor.x;
    float lightMultiplier = max(dot(worldNormal, -lightDirection), 0) * lightIntensity;
    outColor = lightMultiplier * color + ambientColor * ambientIntensity;
}