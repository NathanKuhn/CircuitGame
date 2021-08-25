#version 330

in vec3 outNormal;

out vec4 fragColor;

void main()
{
    fragColor = vec4(outNormal, 1.0) + vec4(0.4, 0.4, 0.1, 0.0);
}