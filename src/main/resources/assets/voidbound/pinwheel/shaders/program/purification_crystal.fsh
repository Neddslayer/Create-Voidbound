#version 150

#include veil:fog

uniform sampler2D BlockAtlas;
uniform sampler2D ScreenBuffer;

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

//Number of turbulence waves
#define TURB_NUM 4.0
//Turbulence wave amplitude
#define TURB_AMP 0.2
//Turbulence wave speed
#define TURB_SPEED 2.5
//Turbulence frequency
#define TURB_FREQ 16.0
//Turbulence frequency multiplier
#define TURB_EXP 2.4

//Apply turbulence to coordinates
vec2 turbulence(vec2 p, float freq, float amp, float speed)
{
    //Turbulence rotation matrix
    mat2 rot = mat2(0.6, -0.8, 0.8, 0.6);

    //Loop through turbulence octaves
    for(float i=0.0; i < TURB_NUM; i++)
    {
        //Scroll along the rotated y coordinate
        float phase = freq * (p * rot).y + speed * VeilRenderTime + i;
        //Add a perpendicular sine wave offset
        p += amp * rot[0] * sin(phase) / freq;

        //Rotate for the next octave
        rot *= mat2(0.6, -0.8, 0.8, 0.6);
        //Scale down for the next octave
        freq *= TURB_EXP;
    }

    return p;
}

mat4 contrastMatrix( float _contrast ){
    float t = ( 1.0 - _contrast ) / 2.0;
    return mat4(
    vec4(_contrast, 0, 0, 0),
    vec4(0, _contrast, 0, 0),
    vec4(0, 0, _contrast, 0),
    vec4(t, t, t, 1));
}

vec4 blur5(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
    vec4 color = vec4(0.0);
    vec2 off1 = vec2(1.3333333333333333) * direction;
    color += texture2D(image, uv) * 0.29411764705882354;
    color += texture2D(image, uv + (off1 / resolution)) * 0.35294117647058826;
    color += texture2D(image, uv - (off1 / resolution)) * 0.35294117647058826;
    return color;
}

void main() {
    vec2 screen_uv = gl_FragCoord.xy / ScreenSize;
    vec4 screenColor = blur5(ScreenBuffer, turbulence(screen_uv, 16, 0.2, 2.5), ScreenSize, vec2(10, 0));
    float g = dot(screenColor.rgb + 0.2, vec3(0.299, 0.587, 0.114));
    screenColor = contrastMatrix(4) * vec4(g, g, g, 1.0);

    vec4 originalColor = texture(BlockAtlas, texCoord0) * ColorModulator;
    vec4 color = mix(originalColor, screenColor * (originalColor + 0.5), 0.2 + sin(VeilRenderTime * 2) * 0.1);
    if (originalColor.a < 0.1) {
        discard;
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
