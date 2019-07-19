package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class RenderController implements Initializable
{
    @FXML
    private Canvas canvas;
    private FractalExplorer explorer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        explorer = new FractalExplorer();
        explorer.setStrategy(new MandelbrotStrategy());
        explorer.render(canvas);
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                double xCoord = event.getSceneX();
                double yCoord = event.getSceneY();
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    explorer.zoomIn(xCoord, yCoord);
                    explorer.render(canvas);
                }
                if (event.getButton() == MouseButton.SECONDARY)
                {
                    explorer.undo();
                    explorer.render(canvas);
                }
                /*
                if(event == UNDO)
                    explorer.undo();
                    explorer.render()
                */
            }

//
//            @Override
//            public void handle(MouseEvent event)
//            {
//                double clickedX = event.getSceneX();
//                double clickedY = event.getSceneY();
//                double scaleFactor = 1.5;
//                double centerX = real_min + (real_max - real_min) * clickedX / canvasWidth;
//                double centerY = imag_max - (imag_max - imag_min) * clickedY / canvasHeight;
//                double temp_real_min = 0;
//                double temp_real_max = 0;
//                double temp_imag_min = 0;
//                double temp_imag_max = 0;
//                if (event.getButton() == MouseButton.PRIMARY)
//                {
//                    temp_real_min = centerX - Math.abs(real_max - real_min) / (2 * scaleFactor);
//                    temp_real_max = centerX + Math.abs(real_max - real_min) / (2 * scaleFactor);
//                    temp_imag_min = centerY - Math.abs(imag_max - imag_min) / (2 * scaleFactor);
//                    temp_imag_max = centerY + Math.abs(imag_max - imag_min) / (2 * scaleFactor);
//                }
//                if (event.getButton() == MouseButton.SECONDARY)
//                {
//                    temp_real_min = centerX - Math.abs(real_max - real_min) / (2 / scaleFactor);
//                    temp_real_max = centerX + Math.abs(real_max - real_min) / (2 / scaleFactor);
//                    temp_imag_min = centerY - Math.abs(imag_max - imag_min) / (2 / scaleFactor);
//                    temp_imag_max = centerY + Math.abs(imag_max - imag_min) / (2 / scaleFactor);
//                }
//                real_max = temp_real_max;
//                real_min = temp_real_min;
//                imag_max = temp_imag_max;
//                imag_min = temp_imag_min;
//                fractal.render(canvas);
//            }
        });
    }
}
