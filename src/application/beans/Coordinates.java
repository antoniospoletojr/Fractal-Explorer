package application.beans;

/**
 * Bean class implementing the coordinates of the complex plane, used to render the fractals.
 * It implements Cloneable for the Memento pattern.
 * @author Antonio Spoleto Junior
 */
public class Coordinates implements Cloneable
{
    private double realMax;
    private double realMin;
    private double imagMax;
    private double imagMin;

    public Coordinates(){}

    /**
     * Construct the plane with its 4 edges.
     * @param realMax
     * @param realMin
     * @param imagMax
     * @param imagMin
     */
    public Coordinates(double realMax, double realMin, double imagMax, double imagMin)
    {
        this.realMax = realMax;
        this.realMin = realMin;
        this.imagMax = imagMax;
        this.imagMin = imagMin;
    }

    public double getRealMax()
    {
        return realMax;
    }

    public void setRealMax(double realMax)
    {
        this.realMax = realMax;
    }

    public double getRealMin()
    {
        return realMin;
    }

    public void setRealMin(double realMin)
    {
        this.realMin = realMin;
    }

    public double getImagMax()
    {
        return imagMax;
    }

    public void setImagMax(double imagMax)
    {
        this.imagMax = imagMax;
    }

    public double getImagMin()
    {
        return imagMin;
    }

    public void setImagMin(double imagMin)
    {
        this.imagMin = imagMin;
    }

    @Override
    public Coordinates clone() {
        try {
            return (Coordinates) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString()
    {
        return "realMax=" + realMax + ", realMin=" + realMin + ", imagMax=" + imagMax + ", imagMin=" + imagMin;
    }
}
