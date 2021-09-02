#version 330

in vec3 outColor;
in vec2 texCoord;

out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = texture(texture_sampler, texCoord) * (outColor.x + outColor.y + outColor.z) / 3;
}