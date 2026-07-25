#version 440

#include veil:fog
#include voidbound:cnoise
#include voidbound:pixelate

uniform sampler2D BlockAtlas;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;
uniform float VeilRenderTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord1;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    ivec2 atlasSize = textureSize(BlockAtlas, 0);
    float cn = clamp(2 * cnoise( pixelate(vec3(texCoord0.x * 512 * (atlasSize.x / atlasSize.y), 0, texCoord0.y * 512), 2) + vec3(0, VeilRenderTime, 0) ), -1, 1);
    vec3 noise = (pow(cn, 2) - pow(cn, 3)) * vec3(.65, .6, 1);
    vec4 originalColor = texture(BlockAtlas, texCoord0) * ColorModulator;
    vec4 color = originalColor * vertexColor;
    if (originalColor.a < 0.1) {
        discard;
    }

    if (all(lessThan(abs(originalColor.rgb - vec3(0.086274, 1, 0)), vec3(0.1)))) {
        color.rgb = noise;
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
