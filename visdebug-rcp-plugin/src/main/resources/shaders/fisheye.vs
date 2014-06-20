uniform float center;
uniform float radius;
uniform float factor;

void main() {
    float x = gl_Vertex.x;
    float shiftPct;

    float delta = x - center;
    if (abs(delta) < radius) {
        // hyperbolic transform
        shiftPct = abs(delta / radius);
        shiftPct *= 3.1415925 / 2.0;
        shiftPct = abs(atan(shiftPct));
        shiftPct *= radius;

        x += factor * shiftPct * sign(delta);
        x = max(center - radius, min(x, center + radius));
    }

    gl_Position = gl_ModelViewProjectionMatrix * vec4(x, gl_Vertex.yzw);
    gl_FrontColor = gl_Color;
}