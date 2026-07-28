#version 440 core

#include veil:light
#include veil:fog

#veil:buffer veil:camera VeilCamera

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;
layout(location = 2) in vec2 UV0;
layout(location = 3) in ivec2 UV2;
layout(location = 4) in ivec3 Normal;

uniform sampler2D Sampler2;

out vec2 texCoord;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform int FogShape;
uniform float VeilRenderTime;

out float vertexDistance;
out vec3 vertexPos;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    vertexPos = ((VeilCamera.IViewMat * ModelViewMat) * vec4(Position, 1.0)).xyz + VeilCamera.CameraPosition - VeilCamera.CameraBobOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
    vertexColor = Color;
    texCoord0 = UV0;
}