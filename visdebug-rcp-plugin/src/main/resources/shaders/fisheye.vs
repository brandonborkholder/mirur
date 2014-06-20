uniform float pos;
uniform float radius;

void main() {
    float x = gl_Vertex.x;
    float shift = 0.0;
    float shiftPct;

    float delta = x - pos;
    if (abs(delta) < radius) {
        shiftPct = -0.5 * abs(delta) / radius + 0.5;
        shift = shiftPct * (radius - abs(delta)) * sign(delta);
    }

    gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + vec4(shift, 0, 0, 0));
    gl_FrontColor = gl_Color;
}