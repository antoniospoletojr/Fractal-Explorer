package sample;

public class Complex
{
    public double real;
    public double imag;

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

    public void add(double real, double imag)
    {
        this.real += real;
        this.imag += imag;
    }

    public void add(Complex z)
    {
        this.real += z.real();
        this.imag += z.imag();
    }

    public void subtract(double real, double imag)
    {
        this.real -= real;
        this.imag -= imag;
    }

    public void subtract(Complex z)
    {
        this.real -= z.real();
        this.imag -= z.imag();
    }

    public void square()
    {
        double temp = 2*real*imag;
        this.real = real*real - imag*imag;
        this.imag = temp;
    }

    public double abs()
    {
        return Math.hypot(real, imag);
    }

    public double real()
    {
        return real;
    }

    public double imag()
    {
        return imag;
    }

    public void setReal(double real)
    {
        this.real = real;
    }

    public void setImag(double imag)
    {
        this.imag = imag;
    }

    @Override
    public String toString()
    {
        return (real + "  " + imag + "i");
    }

}
