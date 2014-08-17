package simple;

import java.util.Collection;

@SuppressWarnings("unused")
class Input {
	private final double[] array;
    private final Collection<? extends Number> col;

    Input(double[] v, Collection<? extends Number> c){
        array = v;
        col = c;
    }
}