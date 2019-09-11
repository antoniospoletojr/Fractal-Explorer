package application.beans;

/**
 * Bean class implementing the context used by the fractal strategies at run-time.
 * @author Antonio Spoleto Junior
 */
public class Context
{
    private Coordinates coordinates;
    private int iterations;
    private boolean smoothing;
    private boolean equalization;

    /**
     * Default initialization for a context.
     */
    public Context()
    {
        this.iterations = 0;
        this.smoothing = true;
        this.equalization = false;
        this.coordinates = new Coordinates();
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates)
    {
        this.coordinates = coordinates;
    }

    public int getIterations()
    {
        return iterations;
    }

    public void setIterations(int value)
    {
        this.iterations = value;
    }

    public boolean smoothing()
    {
        return smoothing;
    }

    public void toggleSmoothing()
    {
        smoothing = !smoothing;
    }

    public boolean equalization()
    {
        return equalization;
    }

    public void toggleEqualization()
    {
        equalization = !equalization;
    }

    @Override
    public String toString()
    {
        return "coordinates=" + coordinates + "\niterations=" + iterations + "\nsmoothing=" + smoothing + "\nequalization=" + equalization;
    }
}
