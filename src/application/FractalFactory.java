package application;

public class FractalFactory
{
    static FractalStrategy makeFractal(String family, ColorPalette palette)
    {
        switch (family)
        {
            case "Mandelbrot":
                return new MandelbrotStrategy(palette);
            case "Julia":
                return new JuliaStrategy(palette);
            case "BurningShip":
                return new BurningShipStrategy(palette);
            default:
            {
                System.out.println("Stringa invalida");
                return null;
            }
        }
    }
}
