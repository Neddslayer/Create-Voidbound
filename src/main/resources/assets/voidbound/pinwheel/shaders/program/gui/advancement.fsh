#include voidbound:cnoise

uniform float VeilRenderTime;
uniform vec4 ColorModulator;
uniform vec2 ScreenSize;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec2 uv = (gl_FragCoord.xy / ScreenSize.xy) * 16;
    uv.x *= (ScreenSize.x / ScreenSize.y);

    float random = rand(floor((uv + rand(vec2(VeilRenderTime)) * 50) * 16) / 16);

    vec3 col = vec3(1, 0, 1);
    float cn = clamp(2 * cnoise( (floor((vec3(uv.x, 0, uv.y)) * 8) / 16) + vec3(0, VeilRenderTime, 0) ), -1, 1);
    vec3 noise = (pow(cn, 2) - pow(cn, 3)) * vec3(0.8, 0, 1) * random;

    col += noise;

    col = clamp(noise, vec3(0), vec3(1));

    vec4 color = vec4(col, 1.0) * ColorModulator;

    if (color.a == 0.0) {
        discard;
    }
    fragColor = color * ColorModulator;
}
