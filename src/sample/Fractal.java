package sample;

class Fractal
{
    private double real_max;
    private double real_min;
    private double imag_max;
    private double imag_min;
    private FractalStrategy strategy;

    Fractal(double real_max, double real_min, double imag_max, double imag_min)
    {
        this.real_max = real_max;
        this.real_min = real_min;
        this.imag_max = imag_max;
        this.imag_min = imag_min;
    }
    public void setStrategy(FractalStrategy strategy)
    {
        this.strategy = strategy;
    }
}

interface FractalStrategy
{
    public void execute();
}

class MandelbrotStrategy implements FractalStrategy
{
    @Override
    public void execute()
    {

    }
}

