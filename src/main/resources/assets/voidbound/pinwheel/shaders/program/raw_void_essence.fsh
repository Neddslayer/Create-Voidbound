#version 440 core

#include veil:fog
#include voidbound:cnoise
#include voidbound:zdiff

#veil:buffer veil:camera VeilCameraMatrices

uniform sampler2D PostEntityDepthBuffer;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;
uniform float VeilRenderTime;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 vertexPos;

out vec4 fragColor;

void main() {
    vec2 uv = gl_FragCoord.xy / ScreenSize.xy;

    float clipDepth = texture(PostEntityDepthBuffer, uv).r;

    float diff = z_diff(clipDepth, gl_FragCoord.z, VeilCameraMatrices.NearPlane, VeilCameraMatrices.FarPlane);

    float random = rand(floor((vertexPos.xyz + rand(vec3(VeilRenderTime)) * 50) * 16) / 16);

    vec3 col = vec3(1, 0, 1) * pow(clamp(1-diff + sin(VeilRenderTime) * 0.1, 0, 1), 4.0);
    float cn = clamp(2 * cnoise( (floor((vec3(vertexPos.x, vertexPos.y * 0.5, vertexPos.z)) * 8) / 16) + vec3(0, VeilRenderTime, 0) ), -1, 1);
    vec3 noise = (pow(cn, 2) - pow(cn, 3)) * vec3(0.8, 0, 1) * random;

    col += diff > 0.5 ? noise : mix(vec3(1, 0.7, 1), noise, diff / 0.5);

    col = clamp(col, vec3(0), vec3(1));

    vec4 color = vec4(col, 1.0) * vertexColor * ColorModulator;

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}