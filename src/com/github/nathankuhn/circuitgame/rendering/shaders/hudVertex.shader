#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uv;

out vec2 texCoord;

uniform mat4 projModelMatrix;
uniform mat4 transformMatrix;

void main()
{
    vec4 pos = transformMatrix * vec4(position, 1.0);
    gl_Position = projModelMatrix * pos;
    texCoord = uv;
}