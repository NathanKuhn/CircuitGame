#version 330

in vec2 texCoord;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    float shade = 1.0;
    if(abs(outNormal) == vec3(1.0, 0.0, 0.0))
    {
        shade = 0.6;
    } else if(abs(outNormal) == vec3(0.0, 0.0, 1.0))
    {
        shade = 0.8;
    }

    vec4 color = texture(texture_sampler, texCoord);
    if (color.w < 0.1) {
        discard;
    }

    fragColor = color * shade;
    fragColor.w = color.w;
}