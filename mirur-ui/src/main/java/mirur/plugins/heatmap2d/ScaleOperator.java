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
package mirur.plugins.heatmap2d;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.log10;

import java.nio.FloatBuffer;

public interface ScaleOperator {
	public final ScaleOperator NORMAL = new ScaleOperator() {
		@Override
		public void operate(FloatBuffer buf) {
			// nop
		}

		@Override
		public double operate(double v) {
			return v;
		}

		@Override
		public double unoperate(double v) {
			return v;
		}
	};
	public final ScaleOperator LOG10 = new ScaleOperator() {
		@Override
		public void operate(FloatBuffer buf) {
			final int n = buf.limit();
			for (int i = 0; i < n; i++) {
				float v = buf.get(i);
				v = (float) log10(v);
				buf.put(i, v);
			}
		}

		@Override
		public double operate(double v) {
			return log10(v);
		}

		@Override
		public double unoperate(double v) {
			return exp(log(10) * v);
		}
	};
	public final ScaleOperator LOG = new ScaleOperator() {
		@Override
		public void operate(FloatBuffer buf) {
			final int n = buf.limit();
			for (int i = 0; i < n; i++) {
				float v = buf.get(i);
				v = (float) log(v);
				buf.put(i, v);
			}
		}

		@Override
		public double operate(double v) {
			return log(v);
		}

		@Override
		public double unoperate(double v) {
			return exp(v);
		}
	};
	public final ScaleOperator EXP10 = new ScaleOperator() {
		@Override
		public void operate(FloatBuffer buf) {
			final double log10 = log(10);
			final int n = buf.limit();
			for (int i = 0; i < n; i++) {
				float v = buf.get(i);
				v = (float) exp(v * log10);
				buf.put(i, v);
			}
		}

		@Override
		public double operate(double v) {
			return exp(v * log(10));
		}

		@Override
		public double unoperate(double v) {
			return log10(v);
		}
	};
	public final ScaleOperator EXP = new ScaleOperator() {
		@Override
		public void operate(FloatBuffer buf) {
			final int n = buf.limit();
			for (int i = 0; i < n; i++) {
				float v = buf.get(i);
				v = (float) exp(v);
				buf.put(i, v);
			}
		}

		@Override
		public double operate(double v) {
			return exp(v);
		}

		@Override
		public double unoperate(double v) {
			return log(v);
		}
	};

	void operate(FloatBuffer buf);

	double operate(double v);

	double unoperate(double v);
}
