package testplugin.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.widgets.Menu;

import testplugin.FisheyeAction;
import testplugin.views.DataPainter;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.axis.tagged.Tag;
import com.metsci.glimpse.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class DataPainterImpl implements DataPainter {
    private final GlimpseLayout layout;
    private final List<ResetAction> resets;

    public DataPainterImpl(GlimpseLayout layout) {
        this.layout = layout;
        resets = new ArrayList<>();
    }

    public void addAxis(Axis1D axis) {
        if (axis instanceof TaggedAxis1D) {
            resets.add(new ResetTagsAction((TaggedAxis1D) axis));
        } else {
            resets.add(new ResetAction1d(axis));
        }
    }

    public void addAxis(Axis2D axis) {
        resets.add(new ResetAction2d(axis));
    }

    @Override
    public GlimpseLayout getLayout() {
        return layout;
    }

    @Override
    public Menu populateMenu(Menu parent) {
        ContributionItem item = new ActionContributionItem(new FisheyeAction());
        item.fill(parent, -1);
        return parent;
    }

    @Override
    public void resetAxes() {
        for (ResetAction a : resets) {
            a.reset();
        }

        for (ResetAction a : resets) {
            a.validate();
        }
    }

    @Override
    public void uninstall(GlimpseCanvas canvas) {
        canvas.removeLayout(layout);
    }

    private interface ResetAction {
        void reset();

        void validate();
    }

    private static class ResetAction1d implements ResetAction {
        final Axis1D axis;
        final double min;
        final double max;

        ResetAction1d(Axis1D axis) {
            this.axis = axis;
            this.min = axis.getMin();
            this.max = axis.getMax();
        }

        @Override
        public void reset() {
            axis.setMin(min);
            axis.setMax(max);
        }

        @Override
        public void validate() {
            axis.validate();
        }
    }

    private static class ResetTagsAction extends ResetAction1d {
        final Map<String, Double> tags;

        public ResetTagsAction(TaggedAxis1D axis) {
            super(axis);
            tags = new TreeMap<>();
            for (Tag t : axis.getSortedTags()) {
                tags.put(t.getName(), t.getValue());
            }
        }

        @Override
        public void reset() {
            super.reset();

            TaggedAxis1D axis = (TaggedAxis1D) this.axis;
            for (Entry<String, Double> e : tags.entrySet()) {
                axis.getTag(e.getKey()).setValue(e.getValue());
            }
        }
    }

    private static class ResetAction2d implements ResetAction {
        final Axis2D axis;
        final ResetAction1d x;
        final ResetAction1d y;

        ResetAction2d(Axis2D axis) {
            this.axis = axis;
            x = new ResetAction1d(axis.getAxisX());
            y = new ResetAction1d(axis.getAxisY());
        }

        @Override
        public void reset() {
            x.reset();
            y.reset();
        }

        @Override
        public void validate() {
            axis.validate();
        }
    }
}
