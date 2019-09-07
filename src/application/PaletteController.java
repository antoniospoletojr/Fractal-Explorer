package application;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaletteController implements Initializable, Switchable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button backButton;
    @FXML
    private ArrayList<ColorPicker> colors;
    @FXML
    private ArrayList<Canvas> canvases;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ColorPaletteBuilder paletteBuilder = ColorPaletteBuilder.getInstance();
        paletteBuilder.color1(null).color2(null).color3(null).color4(null).color5(null).colorsCount(0);
        drawPalettes();
    }

    private void drawPalettes()
    {
        int height = (int) canvases.get(0).getHeight();
        int width = (int) canvases.get(0).getWidth();
        ColorPalette palette[] = new ColorPalette[canvases.size()];
        String[] files = {"gold_sea", "aurora", "night_neons", "black_white", "sunny", "custom"};
        for (int i = 0; i < palette.length; i++)
            palette[i] = new ColorPalette(files[i]);
        PixelWriter writer[] = new PixelWriter[canvases.size()];
        for (int i = 0; i < canvases.size(); i++)
            writer[i] = canvases.get(i).getGraphicsContext2D().getPixelWriter();
        for (int x = 0; x < width; x++)                  //For each pixel
        {
            for (int y = 0; y < height; y++)
            {
                for (int k = 0; k < writer.length; k++)  //For each writer
                {
                    writer[k].setColor(x, y, palette[k].getColor((double) x / width));
                }
            }
        }
    }

    @FXML
    public void registerColor(Event event)
    {
        ColorPaletteBuilder paletteBuilder = ColorPaletteBuilder.getInstance();
        ColorPicker picker = ((ColorPicker) event.getSource());
        String color = picker.getId();
        switch (color)
        {
            case "color1":
                paletteBuilder = paletteBuilder.color1(picker.getValue());
                break;
            case "color2":
                paletteBuilder = paletteBuilder.color2(picker.getValue());
                break;
            case "color3":
                paletteBuilder = paletteBuilder.color3(picker.getValue());
                break;
            case "color4":
                paletteBuilder = paletteBuilder.color4(picker.getValue());
                break;
            case "color5":
                paletteBuilder = paletteBuilder.color5(picker.getValue());
                break;
        }
        drawCustomPalette();
    }

    void drawCustomPalette()
    {
        if (ColorPaletteBuilder.getColorsCount() > 1)
        {
            try
            {
                int height = (int) canvases.get(5).getHeight();
                int width = (int) canvases.get(5).getWidth();
                ColorPalette palette = ColorPaletteBuilder.getInstance().build();
                PixelWriter writer = canvases.get(5).getGraphicsContext2D().getPixelWriter();
                for (int x = 0; x < width; x++)                  //For each pixel
                {
                    for (int y = 0; y < height; y++)
                    {
                        writer.setColor(x, y, palette.getColor((double) x / width));
                    }
                }
            } catch (IncorrectSplineDataException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("You cannot create a palette with just one color!");
                alert.showAndWait();
            }

        }
    }

    public void changeScene(Event event)
    {
        Button button = (Button) event.getSource();
        if (button == backButton)
        {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/landing.fxml")); //carica il root della nuova scena
                Stage window = (Stage) anchorPane.getScene().getWindow();
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
                FadeTransition ft = new FadeTransition(Duration.millis(400), anchorPane);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                ft.setOnFinished(e ->
                {
                    window.setScene(newScene);
                });

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            ColorPalette palette = null;
            switch (button.getText())
            {
                case "Gold Sea":
                    palette = new ColorPalette("gold_sea");
                    break;
                case "Aurora":
                    palette = new ColorPalette("aurora");
                    break;
                case "Night Neons":
                    palette = new ColorPalette("night_neons");
                    break;
                case "Black & White":
                    palette = new ColorPalette("black_white");
                    break;
                case "Sunny":
                    palette = new ColorPalette("sunny");
                    break;
                case "Custom":
                    try
                    {
                        palette = ColorPaletteBuilder.getInstance().build();
                    } catch (IncorrectSplineDataException e)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("You need atleast 2 colors for a palette to be usable.");
                        alert.showAndWait();
                        return;
                    }
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/family.fxml"));
            FamilyController controller = new FamilyController(palette);
            loader.setController(controller);
            try
            {
                Parent root = loader.load();
                Stage window = (Stage) anchorPane.getScene().getWindow();
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
                ImageView destImage = new ImageView(root.snapshot(new SnapshotParameters(),new WritableImage(800, 600)));
                destImage.setTranslateX(800);
                anchorPane.getChildren().add(destImage);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(destImage.translateXProperty(), 0, Interpolator.EASE_BOTH);
                KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
                timeline.setOnFinished(t->{
                    anchorPane.getChildren().remove(destImage);
                    window.setScene(newScene);
                });

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
