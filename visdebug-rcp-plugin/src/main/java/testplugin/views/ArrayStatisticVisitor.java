package testplugin.views;

public abstract class ArrayStatisticVisitor extends SimpleArrayVisitor {
    private final String name;

    protected boolean isValid;

    public ArrayStatisticVisitor(String name) {
        this.name = name;
        isValid = false;
    }

    public String getName() {
        return name;
    }

    public String getStatistic() {
        if (isValid) {
            return getValue();
        } else {
            return "n/a";
        }
    }

    @Override
    public void visit(int i, double v) {
        if (!Double.isNaN(v)) {
            visit(v);
            isValid = true;
        }
    }

    protected abstract void visit(double v);

    protected abstract String getValue();

    public static class Sum extends ArrayStatisticVisitor {
        double sum = 0;

        public Sum() {
            super("sum");
        }

        @Override
        protected void visit(double v) {
            sum += v;
        }

        @Override
        protected String getValue() {
            return String.valueOf(sum);
        }
    }

    public static class Mean extends ArrayStatisticVisitor {
        double sum = 0;
        int count = 0;

        public Mean() {
            super("mean");
        }

        @Override
        protected void visit(double v) {
            sum += v;
            count++;
        }

        @Override
        protected String getValue() {
            return String.valueOf(sum / count);
        }
    }

    public static class Max extends ArrayStatisticVisitor {
        double max = Double.NEGATIVE_INFINITY;

        public Max() {
            super("max");
        }

        @Override
        protected void visit(double v) {
            if (max < v) {
                max = v;
            }
        }

        @Override
        protected String getValue() {
            return String.valueOf(max);
        }
    }

    public static class Min extends ArrayStatisticVisitor {
        double min = Double.POSITIVE_INFINITY;

        public Min() {
            super("min");
        }

        @Override
        protected void visit(double v) {
            if (v < min) {
                min = v;
            }
        }

        @Override
        protected String getValue() {
            return String.valueOf(min);
        }
    }

    public static class CountNaN extends ArrayStatisticVisitor {
        int count;

        public CountNaN() {
            super("count of NaN");
        }

        @Override
        public void visit(int i, double v) {
            visit(v);
            isValid = true;
        }

        @Override
        protected void visit(double v) {
            if (Double.isNaN(v)) {
                count++;
            }
        }

        @Override
        protected String getValue() {
            return String.valueOf(count);
        }
    }

    public static class CountPosInf extends ArrayStatisticVisitor {
        int count;

        public CountPosInf() {
            super("count of +Inf");
        }

        @Override
        public void visit(int i, double v) {
            visit(v);
            isValid = true;
        }

        @Override
        protected void visit(double v) {
            if (v == Double.POSITIVE_INFINITY) {
                count++;
            }
        }

        @Override
        protected String getValue() {
            return String.valueOf(count);
        }
    }

    public static class CountNegInf extends ArrayStatisticVisitor {
        int count;

        public CountNegInf() {
            super("count of -Inf");
        }

        @Override
        public void visit(int i, double v) {
            visit(v);
            isValid = true;
        }

        @Override
        protected void visit(double v) {
            if (v == Double.NEGATIVE_INFINITY) {
                count++;
            }
        }

        @Override
        protected String getValue() {
            return String.valueOf(count);
        }
    }
}
