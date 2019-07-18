package sample;

public class Complex
{
    private double real;
    private double imag;

    Complex()
    {
        this.real = 0;
        this.imag = 0;
    }

    Complex(double real, double imag)
    {
        this.real = real;
        this.imag = imag;
    }

    public double abs()
    {
        return Math.hypot(real, imag);
    }

    @Override
    public String toString()
    {
        return (real + "" + imag + "i");
    }

}
