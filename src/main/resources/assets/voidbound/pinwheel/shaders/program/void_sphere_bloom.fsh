uniform sampler2D Sampler0;

uniform float bloom;
uniform vec4 ColorModulator;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor * ColorModulator;
    fragColor = color * bloom;
}
