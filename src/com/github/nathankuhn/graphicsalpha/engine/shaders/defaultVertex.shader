#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uv;

out vec2 texCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 projectionMatrix;
uniform mat4 transformMatrix;
uniform mat4 viewMatrix;

void main()
{
    vec4 mvPos = viewMatrix * transformMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * transformMatrix * vec4(position, 1.0);
    texCoord = uv;
    mvVertexNormal = normalize(viewMatrix * vec4(normal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}