package application.math;

public class Complex
{
    private double real;
    private double imag;

    public Complex()
    {
        this.real = 0;
        this.imag = 0;
    }

    public Complex(double real, double imag)
    {
        this.real = real;
        this.imag = imag;
    }

    public Complex(Complex z)
    {
        this.real = z.real();
        this.imag = z.imag();
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

    public void multiply(double scalar)
    {
        this.real *= scalar;
        this.imag *= scalar;
    }

    public void multiply(double real, double imag)
    {
        this.real = this.real*real - this.imag*imag;
        this.imag = this.real*imag + this.imag()*real;
    }

    public void multiply(Complex z)
    {
        double temp = this.real*z.imag() + this.imag*z.real();;
        this.real = this.real*z.real() - this.imag*z.imag();
        this.imag = temp;
    }

    public void divide(Complex z)
    {
        double a = this.real;
        double b = this.imag;
        double c = z.real();
        double d = z.imag();
        this.real = (a*c+b*d)/(c*c+d*d);
        this.imag = (b*c-a*d)/(c*c+d*d);
    }

    public void conj()
    {
        this.imag = -this.imag;
    }

    public void pow(int exp)
    {
        Complex multiplier = new Complex(this.real,this.imag);
        for(int i=1; i<exp; i++)
        {
            this.multiply(multiplier);
        }
    }

    public void square()
    {
        double temp = 2*real*imag;
        this.real = real*real - imag*imag;
        this.imag = temp;
    }

    public double modulus()
    {
        return Math.sqrt((this.real*this.real)+(this.imag*this.imag));
    }

    public double squaredModulus()
    {
        return ((this.real*this.real)+(this.imag*this.imag));
    }

    public double absoluteDifference(Complex z)
    {
        double temp_real,temp_imag;
        temp_real = this.real - z.real();
        temp_imag = this.imag - z.imag();
        return Math.sqrt((temp_real*temp_real)+(temp_imag*temp_imag));
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
