package application.creational;

import application.math.Complex;
import application.processing.ColorPalette;
import application.strategies.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple factory implementing the building process of the fractal families
 * @author Antonio Spoleto Junior
 */

public class FractalFactory
{
    /**
     * Gets a palette and a string representing the family, and returns the correct strategy object.
     * @param family
     * @param palette
     * @return
     */
    public static FractalStrategy makeFractal(String family, ColorPalette palette)
    {
        switch (family)
        {
            case "mandelbrot":
                return new MandelbrotStrategy(palette);
            case "juliaFixed":
                return juliaHandler(palette);
            case "multibrot":
                return multibrotHandler(palette);
            case "julia":
                return new JuliaStrategy(palette);
            case "burningShip":
                return new BurningShipStrategy(palette);
            case "newton":
                return new NewtonStrategy(palette);
            default:
                return null;
        }
    }

    /**
     * Handle the initialization of the Julia fractal strategy.
     * @param palette
     * @return
     */
    private static FractalStrategy juliaHandler(ColorPalette palette)
    {
        //Construct user dialog for setting the C parameter of the strategy
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Julia value picker");
        dialog.setHeaderText("Choose a C value for building Julia fractal");
        ButtonType defaultButton = new ButtonType("Default");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL,defaultButton);
        VBox box = new VBox();
        Slider xSlider = new Slider();
        xSlider.setMin(-2);
        xSlider.setMax(2);
        xSlider.setValue(0);
        xSlider.setShowTickLabels(true);
        xSlider.setShowTickMarks(true);
        xSlider.setMajorTickUnit(0.2);
        xSlider.setBlockIncrement(0.2d);
        Slider ySlider = new Slider();
        ySlider.setMin(-2);
        ySlider.setMax(2);
        ySlider.setValue(0);
        ySlider.setShowTickLabels(true);
        ySlider.setShowTickMarks(true);
        ySlider.setMajorTickUnit(0.2);
        ySlider.setBlockIncrement(0.2d);
        box.getChildren().add(0, xSlider);
        box.getChildren().add(1, ySlider);
        dialog.getDialogPane().setContent(box);
        Optional<ButtonType> result = dialog.showAndWait();
        double x = xSlider.getValue();
        double y = ySlider.getValue();
        if(result.get()==ButtonType.OK)
            return new FixedJuliaStrategy(palette,new Complex(x,y));
        if(result.get()==defaultButton)
            return new FixedJuliaStrategy(palette,new Complex( -0.7269, 0.1889));
        return null;
    }

    /**
     * Handle the initialization of the Multibrot fractal strategy.
     * @param palette
     * @return
     */
    private static FractalStrategy multibrotHandler(ColorPalette palette)
    {
        //Construct user dialog for setting the exponent parameter of the strategy.
        List<String> choices = new ArrayList<>();
        for (int i = 3; i <= 10; i++)
            choices.add(Integer.toString(i));
        Dialog<String> dialog = new ChoiceDialog<>("3", choices);
        dialog.setTitle("Multibrot picker");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose the exponent of your multibrot:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
            return new MultibrotStrategy(palette, Integer.parseInt(result.get()));
        return null;
    }
}
