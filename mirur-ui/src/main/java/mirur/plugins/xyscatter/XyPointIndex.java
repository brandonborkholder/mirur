package mirur.plugins.xyscatter;

import java.util.Collection;

import com.metsci.glimpse.painter.shape.PointSetPainter.IdXy;
import com.metsci.glimpse.util.quadtree.QuadTreeXys;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import mirur.core.AbstractInterleavedVisitor;
import mirur.plugins.DataUnitConverter;

public class XyPointIndex extends AbstractInterleavedVisitor {
    private DataUnitConverter xUnitConverter;
    private DataUnitConverter yUnitConverter;
    private QuadTreeXys<IdXy> quadTree;

    public XyPointIndex(DataUnitConverter xUnitConverter, DataUnitConverter yUnitConverter) {
        this.xUnitConverter = xUnitConverter;
        this.yUnitConverter = yUnitConverter;
        quadTree = new QuadTreeXys<>(5);
    }

    public IntList getArrayIdx(double x, double y, double radiusX, double radiusY) {
        Collection<IdXy> result = quadTree.search((float) (x - radiusX), (float) (x + radiusX), (float) (y - radiusY), (float) (y + radiusY));
        if (result == null) {
            return IntLists.EMPTY_LIST;
        } else {
            IntList indexes = new IntArrayList(result.size());
            for (IdXy r : result) {
                indexes.add(r.id());
            }

            return indexes;
        }
    }

    @Override
    protected void visit(int i, double x, double y) {
        quadTree.add(new IdXy(i, (float) xUnitConverter.data2painter(x), (float) yUnitConverter.data2painter(y)));
    }

    @Override
    protected void visit(int i, float x, float y) {
        quadTree.add(new IdXy(i, (float) xUnitConverter.data2painter(x), (float) yUnitConverter.data2painter(y)));
    }
}
