package mirur.core;

import static java.lang.Math.max;

public class IsValidArrayVisitor implements Array1dVisitor, Array2dVisitor {
    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }

    private void invalid() {
        valid = false;
    }

    @Override
    public void visit(double[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (double[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(long[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (long[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(float[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (float[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(int[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (int[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(short[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (short[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(char[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (char[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(byte[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (byte[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(boolean[][] v) {
        if (v.length == 0) {
            invalid();
            return;
        }

        int maxLen2 = 0;
        for (boolean[] e : v) {
            if (e == null) {
                invalid();
                return;
            } else {
                maxLen2 = max(maxLen2, e.length);
            }
        }

        if (maxLen2 == 0) {
            invalid();
        }
    }

    @Override
    public void visit(double[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(long[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(float[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(int[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(short[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(char[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(byte[] v) {
        if (v.length == 0) {
            invalid();
        }
    }

    @Override
    public void visit(boolean[] v) {
        if (v.length == 0) {
            invalid();
        }
    }
}
