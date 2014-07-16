package simple;

import java.util.Collection;

@SuppressWarnings("unused")
class Foo {
	private final double[] array;
    private final Collection<? extends Number> col;

    Foo(double[] v, Collection<? extends Number> c){
        array = v;
        col = c;
    }
}