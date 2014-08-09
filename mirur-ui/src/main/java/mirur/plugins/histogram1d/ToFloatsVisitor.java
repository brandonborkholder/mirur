package mirur.plugins.histogram1d;

import mirur.core.Array1dVisitor;

public class ToFloatsVisitor implements Array1dVisitor {
    private float[] floats;

    public float[] getFloats() {
        return floats;
    }

    @Override
    public void visit(double[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = (float) v[i];
        }
    }

    @Override
    public void visit(long[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i];
        }
    }

    @Override
    public void visit(float[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i];
        }
    }

    @Override
    public void visit(int[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i];
        }
    }

    @Override
    public void visit(short[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i];
        }
    }

    @Override
    public void visit(char[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i];
        }
    }

    @Override
    public void visit(byte[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i];
        }
    }

    @Override
    public void visit(boolean[] v) {
        floats = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            floats[i] = v[i] ? 1.0f : 0.0f;
        }
    }
}
