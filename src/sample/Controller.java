package sample;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private Canvas canvas;

    private static double canvasWidth;
    private static double canvasHeight;
    private static double real_max = 1;
    private static double real_min = -2;
    private static double imag_max = 1.5;
    private static double imag_min = -1.5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        renderSet(canvas.getGraphicsContext2D());
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                double clickedX = event.getSceneX();
                double clickedY = event.getSceneY();
                double scaleFactor = 1.5;
                double centerX = real_min + (real_max - real_min) * clickedX / canvasWidth;
                double centerY = imag_max - (imag_max - imag_min) * clickedY / canvasHeight;
                double temp_real_min=0;
                double temp_real_max=0;
                double temp_imag_min=0;
                double temp_imag_max=0;
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    temp_real_min = centerX - Math.abs(real_max - real_min) / (2 * scaleFactor);
                    temp_real_max = centerX + Math.abs(real_max - real_min) / (2 * scaleFactor);
                    temp_imag_min = centerY - Math.abs(imag_max - imag_min) / (2 * scaleFactor);
                    temp_imag_max = centerY + Math.abs(imag_max - imag_min) / (2 * scaleFactor);
                }
                if (event.getButton() == MouseButton.SECONDARY)
                {
                    temp_real_min = centerX - Math.abs(real_max - real_min) / (2 / scaleFactor);
                    temp_real_max = centerX + Math.abs(real_max - real_min) / (2 / scaleFactor);
                    temp_imag_min = centerY - Math.abs(imag_max - imag_min) / (2 / scaleFactor);
                    temp_imag_max = centerY + Math.abs(imag_max - imag_min) / (2 / scaleFactor);
                }
                real_max = temp_real_max;
                real_min = temp_real_min;
                imag_max = temp_imag_max;
                imag_min = temp_imag_min;
                renderSet(canvas.getGraphicsContext2D());
            }
        });
    }

    private void renderSet(GraphicsContext ctx)
    {
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        double deltaX = (real_max - real_min) / canvasWidth;
        double deltaY = (imag_max - imag_min) / canvasHeight;
        int convergenceSteps = 50;
        double reC = real_min;
        double imC = imag_max;
        for (double x = 0; x < canvasWidth; x++)
        {
            reC += deltaX;
            imC = imag_max;
            for (double y = 0; y < canvasHeight; y++)
            {
                imC -= deltaY;
                double convergenceValue = checkConvergence(reC, imC, convergenceSteps);
                double convergenceRatio = convergenceValue / convergenceSteps;
                if (convergenceValue != convergenceSteps)
                {
                    ctx.setFill(Color.hsb(convergenceRatio*360, 1F,1F));
                } else
                {
                    ctx.setFill(Color.BLACK); // Convergence Color
                }
                ctx.fillRect(x, y, 1, 1);
            }
        }
    }

    private int checkConvergence(double reC, double imC, int convergenceSteps)
    {
        double real_z = 0;
        double imag_z = 0;
        for (int i = 0; i < convergenceSteps; i++)
        {
            double real_z_temp = real_z * real_z - (imag_z * imag_z);//CALCOLO PARTE REALE
            double imag_z_temp = 2 * (real_z * imag_z);//CALCOLO PARTE IMMAGINARIA
            real_z = real_z_temp + reC;
            imag_z = imag_z_temp + imC;
            //SE IL MODULO DEL NUMERO E' MAGGIORE DI DUE IL NOSTRO C NON E' NEL SET
            // √(a² + b²) <= 2.0
            // a² + b² <= 4.0
            if (real_z * real_z + imag_z * imag_z >= 4.0)
            {
                return i;
            }
        }
        return convergenceSteps;
    }


}
