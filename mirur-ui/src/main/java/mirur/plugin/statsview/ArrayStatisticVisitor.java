package mirur.plugin.statsview;

import mirur.core.ArrayVisitor;
import mirur.core.SimpleArrayVisitor;

public interface ArrayStatisticVisitor extends ArrayVisitor {
    String getName();

    String getStatistic();

    abstract class AbstractStatisticVisitor extends SimpleArrayVisitor implements ArrayStatisticVisitor {
        private final String name;

        protected boolean isValid;

        public AbstractStatisticVisitor(String name) {
            this.name = name;
            isValid = false;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
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
    }

    public static class Sum extends AbstractStatisticVisitor {
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

    public static class Mean extends AbstractStatisticVisitor {
        org.apache.commons.math3.stat.descriptive.moment.Mean mean = new org.apache.commons.math3.stat.descriptive.moment.Mean();

        public Mean() {
            super("mean");
        }

        @Override
        protected void visit(double v) {
            mean.increment(v);
        }

        @Override
        protected String getValue() {
            return String.valueOf(mean.getResult());
        }
    }

    public static class Variance extends AbstractStatisticVisitor {
        org.apache.commons.math3.stat.descriptive.moment.Variance var = new org.apache.commons.math3.stat.descriptive.moment.Variance();

        public Variance() {
            super("variance");
        }

        @Override
        protected void visit(double v) {
            var.increment(v);
        }

        @Override
        protected String getValue() {
            return String.valueOf(var.getResult());
        }
    }

    public static class Max extends AbstractStatisticVisitor {
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

    public static class Min extends AbstractStatisticVisitor {
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

    public static class CountNaN extends AbstractStatisticVisitor {
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

    public static class CountPosInf extends AbstractStatisticVisitor {
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

    public static class CountNegInf extends AbstractStatisticVisitor {
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

    public static class CountPositive extends AbstractStatisticVisitor {
        int count;

        public CountPositive() {
            super("count of positive");
        }

        @Override
        protected String getValue() {
            return Integer.toString(count);
        }

        @Override
        protected void visit(double v) {
            if (v > 0) {
                count++;
            }
        }
    }

    public static class CountNegative extends AbstractStatisticVisitor {
        int count;

        public CountNegative() {
            super("count of negative");
        }

        @Override
        protected String getValue() {
            return Integer.toString(count);
        }

        @Override
        protected void visit(double v) {
            if (v < 0) {
                count++;
            }
        }
    }

    public static class CountZero extends AbstractStatisticVisitor {
        int count;

        public CountZero() {
            super("count of zeros");
        }

        @Override
        protected String getValue() {
            return Integer.toString(count);
        }

        @Override
        protected void visit(double v) {
            if (v == 0) {
                count++;
            }
        }
    }

    public static class CountTrue extends SimpleArrayVisitor implements ArrayStatisticVisitor {
        int count;

        @Override
        public void visit(int i, boolean v) {
            count += v ? 1 : 0;
        }

        @Override
        public void visit(int i, double v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return "count of true";
        }

        @Override
        public String getStatistic() {
            return String.valueOf(count);
        }
    }

    public static class CountFalse extends SimpleArrayVisitor implements ArrayStatisticVisitor {
        int count;

        @Override
        public void visit(int i, boolean v) {
            count += v ? 0 : 1;
        }

        @Override
        public void visit(int i, double v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return "count of false";
        }

        @Override
        public String getStatistic() {
            return String.valueOf(count);
        }
    }

    public static class SumLong implements ArrayStatisticVisitor {
        long sum;

        @Override
        public String getName() {
            return "count of false";
        }

        @Override
        public String getStatistic() {
            return String.valueOf(sum);
        }

        @Override
        public void visit(int i, long v) {
            sum += v;
        }

        @Override
        public void visit(int i, boolean v) {
            visit(i, v ? 1 : 0);
        }

        @Override
        public void visit(int i, double v) {
            visit(i, (long) v);
        }

        @Override
        public void visit(int i, float v) {
            visit(i, (long) v);
        }

        @Override
        public void visit(int i, int v) {
            visit(i, (long) v);
        }

        @Override
        public void visit(int i, short v) {
            visit(i, (long) v);
        }

        @Override
        public void visit(int i, char v) {
            visit(i, (long) v);
        }

        @Override
        public void visit(int i, byte v) {
            visit(i, (long) v);
        }
    }
}
