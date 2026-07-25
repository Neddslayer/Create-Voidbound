#version 440 core

#include veil:light
#include veil:fog

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;
layout(location = 2) in vec2 UV0;
layout(location = 3) in ivec2 UV2;
layout(location = 4) in vec3 Normal;

uniform sampler2D Sampler2;

out vec2 texCoord;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 NormalMat;
uniform vec3 ChunkOffset;
uniform int FogShape;

out float vertexDistance;
out vec3 View;
out vec3 outNormal;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    View = normalize(ModelViewMat * vec4(Position, 1.0)).xyz;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
    vertexColor = Color;
    texCoord0 = UV0;
    outNormal = (NormalMat * vec4(Normal, 1.0)).xyz;
}