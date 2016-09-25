package mirur.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import mirur.core.Array1DImpl;
import mirur.core.Array2DRectangular;
import mirur.core.VariableObject;
import mirur.plugins.MirurView;
import mirur.plugins.bar1d.BarView;
import mirur.plugins.heatmap2d.HeatmapView;
import mirur.plugins.line1d.LineView;

public class ManyVariablesTest extends Harness {
    private Queue<Runnable> q;

    public ManyVariablesTest() {
        q = join(createViews(), createArrays1d(), createArrays2d());

        q.add(new Runnable() {
            public void run() {
                System.exit(0);
            }
        });

        nextPushed();
    }

    private Queue<Runnable> join(List<MirurView> views, List<Object> arrays1d, List<Object> arrays2d) {
        Queue<Runnable> q = new ArrayDeque<Runnable>();

        for (final MirurView view : views) {
            for (Object array : arrays1d) {
                final VariableObject obj = new Array1DImpl("tmp", array);
                if (view.supportsData(obj)) {
                    q.add(new Runnable() {
                        public void run() {
                            setView(view, obj);
                        }
                    });
                }
            }
            for (Object array : arrays2d) {
                final VariableObject obj = new Array2DRectangular("tmp", array);
                if (view.supportsData(obj)) {
                    q.add(new Runnable() {
                        public void run() {
                            setView(view, obj);
                        }
                    });
                }
            }
        }

        return q;
    }

    private List<MirurView> createViews() {
        List<MirurView> views = new ArrayList<MirurView>();

        views.add(new LineView());
        views.add(new BarView());
        views.add(new HeatmapView());

        return views;
    }

    private List<Object> createArrays1d() {
        List<Object> arrays = new ArrayList<Object>();

        Random r = new Random(0);

        {
            int[] a = new int[300];
            for (int i = 0; i < a.length; i++) {
                a[i] = r.nextInt(100);
            }
            arrays.add(a);
        }
        {
            float[] a = new float[1000];
            for (int i = 0; i < a.length; i++) {
                a[i] = r.nextFloat();
            }
            arrays.add(a);
        }
        {
            double[] a = new double[1000];
            for (int i = 0; i < a.length; i++) {
                a[i] = 5 + r.nextDouble() * 1e-8;
            }
            arrays.add(a);
        }

        return arrays;
    }

    private List<Object> createArrays2d() {
        List<Object> arrays = new ArrayList<Object>();

        Random r = new Random(0);

        {
            float[][] a = new float[30][1000];
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    a[i][j] = r.nextFloat();
                }
            }
            arrays.add(a);
        }

        return arrays;
    }

    @Override
    protected void nextPushed() {
        q.poll().run();
    }

    public static void main(String[] args) {
        new ManyVariablesTest().loop();
    }
}
