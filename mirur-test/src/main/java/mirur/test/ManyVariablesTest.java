package mirur.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import mirur.core.Array1DImpl;
import mirur.core.VariableObject;
import mirur.plugins.MirurView;
import mirur.plugins.bar1d.BarView;
import mirur.plugins.heatmap2d.HeatmapView;
import mirur.plugins.line1d.LineView;

public class ManyVariablesTest extends Harness {
    private Queue<Runnable> q;

    public ManyVariablesTest() {
        q = join(createViews(), createArrays());

        q.add(new Runnable() {
            public void run() {
                System.exit(0);
            }
        });

        nextPushed();
    }

    private Queue<Runnable> join(List<MirurView> views, List<Object> arrays) {
        Queue<Runnable> q = new ArrayDeque<Runnable>();

        for (final MirurView view : views) {
            for (Object array : arrays) {
                final VariableObject obj = new Array1DImpl("tmp", array);
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

    private List<Object> createArrays() {
        List<Object> arrays = new ArrayList<Object>();

        arrays.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });

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
