#version 150

uniform sampler2D Sampler0;
uniform float Radius;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 oneTexel;

out vec4 fragColor;

float gaussian(float x, float sigma) {
    const float pi = 3.14159265359;
    return (1.0 / sqrt(2.0 * pi * sigma * sigma)) * exp(-(x * x) / (2.0 * sigma * sigma));
}

vec4 gaussianBlur(vec2 uv) {
    vec4 color = vec4(0.0);
    float halfWidth = floor(u_Radius * 0.5);
    float sigma = halfWidth * 0.5;
    float sum = 0.0;

    for (float x = -halfWidth; x <= halfWidth; x++) {
        for (float y = -halfWidth; y <= halfWidth; y++) {
            float weight = gaussian(x, sigma) * gaussian(y, sigma);
            vec2 offset = vec2(x * oneTexel.x, y * oneTexel.y);
            vec4 texColor = texture(Sampler0, uv + offset);
            color += texColor * weight;
            sum += weight;
        }
    }
    return color / sum;
}

void main() {
    vec4 color = gaussianBlur(texCoord0) * vertexColor;
    fragColor = color * ColorModulator;
}
