package application.math;

import application.exceptions.IncorrectSplineDataException;

public class SplineInterpolator
{
    private double[] knots; //X
    private double[] values; //Y
    private double[] m; //TANGENTI

    public SplineInterpolator(double[] x, double[] y) throws IncorrectSplineDataException
    {
        if (x == null || y == null || x.length != y.length || x.length < 2)
        {
            throw new IncorrectSplineDataException("There must be at least two points. The arrays must be of equal length.");
        }

        final int n = x.length;
        double[] d = new double[n - 1]; // could optimize this out
        double[] m = new double[n];

        // Compute slopes of secant lines between successive points.
        for (int i = 0; i < n - 1; i++)
        {
            double h = x[i + 1] - x[i];
            if (h <= 0f)
            {
                throw new IncorrectSplineDataException("The knots must all have strictly increasing values.");
            }
            d[i] = (y[i + 1] - y[i]) / h;
        }

        // Initialize the tangents as the average of the secants.
        m[0] = d[0];
        for (int i = 1; i < n - 1; i++)
        {
            m[i] = (d[i - 1] + d[i]) * 0.5f;
        }
        m[n - 1] = d[n - 2];

        // Update the tangents to preserve monotonicity.
        for (int i = 0; i < n - 1; i++)
        {
            if (d[i] == 0f)
            { // successive Y values are equal
                m[i] = 0f;
                m[i + 1] = 0f;
            } else
            {
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];
                double h = (float) Math.hypot(a, b);
                if (h > 9f)
                {
                    double t = 3f / h;
                    m[i] = t * a * d[i];
                    m[i + 1] = t * b * d[i];
                }
            }
        }
        knots = x;
        values = y;
        this.m = m;
    }

    /**
     * Interpolates the value of Y = f(X) for given X. Clamps X to the domain of the spline.
     *
     * @param x The X value.
     * @return The interpolated Y = f(X) value.
     */
    public int interpolate(double x)
    {
        // Handle the boundary cases.
        final int n = knots.length;
        if (x <= knots[0])
        {
            return (int)values[0];
        }
        if (x >= knots[n-1])
        {
            return (int)values[n-1];
        }

        // Find the index 'i' of the last point with smaller X.
        // We know this will be within the spline due to the boundary tests.
        int i = 0;
        while (x >= knots[i+1])
        {
            i += 1;
            if (x == knots[i])
            {
                return (int)values[i];
            }
        }

        // Perform cubic Hermite spline interpolation.
        double h = knots[i+1] - knots[i];
        double t = (x - knots[i]) / h;
        double interpolatedValue = (values[i] * (1 + 2 * t) + h * m[i] * t) * (1 - t) * (1 - t) + (values[i+1] * (3 - 2 * t) + h * m[i + 1] * (t - 1)) * t * t;
        if(interpolatedValue>255)
            return 255;
        else if(interpolatedValue<0)
            return 0;
        else
            return (int)interpolatedValue;
    }

    // For debugging.
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        final int n = knots.length;
        str.append("[");
        for (int i = 0; i < n; i++)
        {
            if (i != 0)
            {
                str.append(", ");
            }
            str.append("(").append(knots[i]);
            str.append(", ").append(values[i]);
            str.append(": ").append(m[i]).append(")");
        }
        str.append("]");
        return str.toString();
    }
}
