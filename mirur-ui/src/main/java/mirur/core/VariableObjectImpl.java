package mirur.core;

public class VariableObjectImpl implements VariableObject {
    private final String name;
    private final Object object;

    public VariableObjectImpl(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSignature() {
        return object.getClass().getSimpleName();
    }

    @Override
    public Object getData() {
        return object;
    }

    @Override
    public String toString() {
        return object.getClass().getName();
    }
}
