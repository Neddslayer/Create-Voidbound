#include voidbound:vnoise

uniform sampler2D Sampler0;

uniform float VeilRenderTime;
uniform vec4 ColorModulator;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * ColorModulator;

    if (color.a == 0.0) {
        discard;
    }

    if (color.r > 0.5) {
        vec3 voro = voronoi3d(vec3(texCoord0 * 100, VeilRenderTime));
        voro.b = 1;
        color = vec4(voro, 1.0);
    }

    fragColor = color * ColorModulator;
}
