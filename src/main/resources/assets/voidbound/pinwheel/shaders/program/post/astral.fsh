uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform vec2 ScreenSize;
uniform float VeilRenderTime;
uniform float TimeToDisappear;

in vec2 texCoord;

out vec4 fragColor;

#define BOXRADIUS 1

void boxBlur( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = texCoord;

    int kernel_window_size = BOXRADIUS*2+1;
    int samples = kernel_window_size*kernel_window_size;

    highp vec4 color = vec4(0, 0, 0, 1);

    highp float wsum = 0.0;
    for (int ry = -BOXRADIUS; ry <= BOXRADIUS; ++ry)
    for (int rx = -BOXRADIUS; rx <= BOXRADIUS; ++rx)
    {
        highp float w = 1.0;
        wsum += w;
        vec4 rgb = texture(DiffuseSampler0, uv+vec2(rx,ry)/ScreenSize.xy);
        color += rgb;
    }

    fragColor = color/wsum;
}

mat4 saturationMatrix( float saturation )
{
    vec3 luminance = vec3( 0.3086, 0.6094, 0.0820 );

    float oneMinusSat = 1.0 - saturation;

    vec3 red = vec3( luminance.x * oneMinusSat );
    red+= vec3( saturation, 0, 0 );

    vec3 green = vec3( luminance.y * oneMinusSat );
    green += vec3( 0, saturation, 0 );

    vec3 blue = vec3( luminance.z * oneMinusSat );
    blue += vec3( 0, 0, saturation );

    return mat4( red,     0,
            green,   0,
            blue,    0,
            0, 0, 0, 1 );
}

void boxEdge( out vec4 fragColor, in vec2 fragCoord )
{
    vec4 original = texture(DiffuseSampler0, texCoord);
    vec4 blurred = vec4(0);
    boxBlur(blurred, fragCoord);
    float gray = length(blurred.rgb);
    vec3 col = vec3(1.95, 1.9, 2.3);

    fragColor = saturationMatrix(2.0) * vec4(vec3(length(vec2(dFdx(gray), dFdy(gray)))) * col + vec3(TimeToDisappear), 1.0);
}

void main() {
    boxEdge(fragColor, texCoord);
}