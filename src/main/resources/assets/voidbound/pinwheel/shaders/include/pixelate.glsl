vec2 pixelate(vec2 p, float x) {
    return floor(p * x) / x;
}

vec3 pixelate(vec3 p, float x) {
    return floor(p * x) / x;
}