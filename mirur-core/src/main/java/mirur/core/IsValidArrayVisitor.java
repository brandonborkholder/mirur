/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © 2023 Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
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
