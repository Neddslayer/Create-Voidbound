float linear_rim(float depth, float near, float far)
{
    float z = 2.0 * depth - 1.0;
    return near * far / (far + depth * (near -far));
}

float z_diff(float clip, float cam, float near, float far) {
    float z_depth = linear_rim(clip, near, far); // depth texture
    float z_pos   = linear_rim(cam,  near, far); // from position
    return clamp(z_depth - z_pos, 0, 1);
}