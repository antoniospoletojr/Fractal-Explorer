package application.structural;

import application.math.Complex;
import application.processing.ColorPalette;
import application.strategies.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FractalFactory
{
    public static FractalStrategy makeFractal(String family, ColorPalette palette)
    {
        switch (family)
        {
            case "mandelbrot":
                return new MandelbrotStrategy(palette);
            case "juliaFixed":
                return new JuliaStrategy(palette);
            case "multibrot":
                return multibrotHandler(palette);
            case "julia":
                return juliaHandler(palette);
            case "burningShip":
                return new BurningShipStrategy(palette);
            case "newton":
                return new NewtonStrategy(palette);
            default:
                return null;
        }
    }

    static private FractalStrategy juliaHandler(ColorPalette palette)
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Julia value picker");
        dialog.setHeaderText("Choose a C value for building Julia fractal");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
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
            return new JuliaStrategy(palette,new Complex(x,y));
        return null;
    }

    static private FractalStrategy multibrotHandler(ColorPalette palette)
    {
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
