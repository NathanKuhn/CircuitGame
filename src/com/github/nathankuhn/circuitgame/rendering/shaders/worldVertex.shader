#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uv;

out vec2 texCoord;
out vec3 outNormal;

uniform mat4 transformMatrix;
uniform mat4 viewMatrix;

void main()
{
    vec4 mvPos = viewMatrix * transformMatrix * vec4(position, 1.0);
    gl_Position = viewMatrix * transformMatrix * vec4(position, 1.0);
    texCoord = uv;
    outNormal = normal;
}