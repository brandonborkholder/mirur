/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
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

import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.ints.IntComparator;

public class SortHelperVisitor implements Array1dVisitor {
    public int[] indexes;
    public Object sorted;

    private void initIndexes(int n) {
        indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
    }

    @Override
    public void visit(double[] orig) {
        final double[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Double.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                double t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(long[] orig) {
        final long[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Long.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                long t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(float[] orig) {
        final float[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Float.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                float t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(int[] orig) {
        final int[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Double.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                int t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(short[] orig) {
        final short[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Short.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                short t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(char[] orig) {
        final char[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Character.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                char t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(byte[] orig) {
        final byte[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Byte.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                byte t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        Arrays.quickSort(0, v.length, c, s);
        sorted = v;
    }

    @Override
    public void visit(boolean[] orig) {
        final boolean[] v = orig.clone();
        IntComparator c = new SimpleComp() {
            @Override
            public int compare(int k1, int k2) {
                return Boolean.compare(v[k1], v[k2]);
            }
        };
        Swapper s = new Swapper() {
            @Override
            public void swap(int a, int b) {
                boolean t = v[a];
                v[a] = v[b];
                v[b] = t;

                int u = indexes[a];
                indexes[a] = indexes[b];
                indexes[b] = u;
            }
        };
        initIndexes(v.length);
        sorted = v;
        Arrays.quickSort(0, v.length, c, s);
    }

    private abstract class SimpleComp implements IntComparator {
        @Override
        public int compare(Integer o1, Integer o2) {
            return compare(o1.intValue(), o2.intValue());
        }
    }

}