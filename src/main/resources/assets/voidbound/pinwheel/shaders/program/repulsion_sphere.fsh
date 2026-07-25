#version 440 core

#include veil:fog

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 View;
in vec3 outNormal;

out vec4 fragColor;

float fresnel(float amount, vec3 normal, vec3 view)
{
    return pow((1.0 - clamp(dot(normalize(normal), normalize(view)), 0.0, 1.0 )), amount);
}

void main() {
    vec2 screen_uv = gl_FragCoord.xy / ScreenSize;

    float base_fresnel = fresnel(3, outNormal, -View);

    float horizon_fresnel = clamp(pow(base_fresnel + 0.75, 5), 0.0, 1.0);
    float alpha = vertexColor.a * horizon_fresnel;
    vec4 vertexColorAlpha = vec4(vertexColor.rgb, alpha);

    vec4 color = vec4(.8, 0, .9, 1.0) * vertexColorAlpha * ColorModulator;

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}