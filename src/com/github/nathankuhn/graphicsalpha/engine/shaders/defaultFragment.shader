#version 330

in vec3 outNormal;

out vec4 fragColor;

void main()
{
    fragColor = vec4(outNormal, 1.0);
}