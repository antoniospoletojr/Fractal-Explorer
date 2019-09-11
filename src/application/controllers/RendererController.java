package application.controllers;

import application.beans.Context;
import application.beans.Coordinates;
import application.exceptions.PrecisionLimitException;
import application.interfaces.Memento;
import application.interfaces.Memorizable;
import application.interfaces.Switchable;
import application.math.Complex;
import application.processing.ImageProcessing;
import application.strategies.FractalStrategy;
import application.strategies.JuliaStrategy;
import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The controller associated with the rendering process of the fractals.
 * This controller handles the user events and send them to its inner class, FractalExplorer,
 * in order to process them.
 * @author Antonio Spoleto Junior
 */
public class RendererController implements Initializable, Switchable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Canvas canvas;
    @FXML
    private ProgressBar indicator;
    @FXML
    private Slider slider;
    @FXML
    private Text timeText;
    @FXML
    private Text xCoordinate;
    @FXML
    private Text yCoordinate;

    private FractalStrategy fractal;
    private FractalExplorer explorer;
    private Stack<Memento> history;

    /**
     * Core class exchanging informations with the rendering controller. It implements Strategy
     * pattern and handles the different operations needed for exploring a fractal. Common interations with
     * the canvas are responsability of this class, as well as the exporting feature.
     * @author Antonio Spoleto Junior
     */
    private class FractalExplorer implements Memorizable
    {
        //Thread related
        private Service<Void> thread;
        private ExecutorService executor;
        //Fractal related
        private Context context;
        private FractalStrategy strategy;

        /**
         * Initialize the context and executor needed to run the background threads.
         */
        public FractalExplorer()
        {
            this.context = new Context();
            this.executor = Executors.newSingleThreadExecutor(r ->
            {
                Thread thread = new Thread(r);
                thread.setDaemon(true); // allows app to exit if tasks are running
                return thread;
            });
        }

        /**
         * Set the fractal strategy at runtime and stamp the actual context with
         * the init parameters of the strategy.
         * @param strategy
         */
        public void setStrategy(FractalStrategy strategy)
        {
            this.strategy = strategy;
            this.strategy.init(context);
        }

        /**
         * Render the fractal strategy with the stored context. Rendering is done by a thread pool
         * which makes sure user inputs don't get lost.
         */
        public void render()
        {
            saveContextToFile();
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            WritableImage image = new WritableImage((int) width, (int) height);
            indicator.setProgress(-1);
            thread = new Service<Void>()
            {
                @Override
                protected Task<Void> createTask()
                {
                    return new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            Long elapsedTime = strategy.render(image, context);
                            timeText.setText(elapsedTime.toString() + "ms");
                            return null;
                        }
                    };
                }
            };
            thread.setExecutor(executor);
            thread.start();
            thread.setOnSucceeded(t ->
            {
                canvas.getGraphicsContext2D().drawImage(image, 0, 0);
                indicator.setProgress(1);
            });
        }

        /**
         * Update fractal dynamically using new parameters based on mouse position
         * @param x
         * @param y
         */
        public void render(double x, double y)
        {
            double deltaX = (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) / canvas.getWidth();
            double deltaY = (context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) / canvas.getHeight();
            Complex c = new Complex(x * deltaX + context.getCoordinates().getRealMin(), -y * deltaY + context.getCoordinates().getImagMax());
            ((JuliaStrategy)strategy).setC(c);
            explorer.render();
        }

        /**
         * Save the current view to a png file with given width and height.
         * Rendering is done by a thread pool, in a similar way as render().
         * 2x Supersampling is added to reduce aliasing.
         * @param width
         * @param height
         */
        @Override
        public void saveSnapshotToFile(int width, int height)
        {
            int supersampledWidth = width * ImageProcessing.SS_FACTOR;
            int supersampledHeight = height * ImageProcessing.SS_FACTOR;
            WritableImage image = new WritableImage(supersampledWidth, supersampledHeight);
            indicator.setProgress(-1);
            thread = new Service<Void>()
            {
                @Override
                protected Task<Void> createTask()
                {
                    return new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            Long elapsedTime = strategy.render(image, context);
                            timeText.setText(elapsedTime.toString() + "ms");
                            return null;
                        }
                    };
                }
            };
            thread.setExecutor(executor);
            thread.start();
            thread.setOnSucceeded(t ->
            {
                WritableImage supersampledImage = ImageProcessing.supersampling(image);
                File file = new File("image.png");
                try
                {
                    ImageIO.write(SwingFXUtils.fromFXImage(supersampledImage, null), "png", file);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                indicator.setProgress(1);
            });
        }

        /**
         * Append context to a log file for debugging/utility purposes.
         */
        @Override
        public void saveContextToFile()
        {
            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter("log", true));
                out.write(fractal.getClass().getSimpleName() + "\n" + context.toString() + "\n\n");
                out.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Main canvas manipulation feature. It gets the coordinates of the user click and a scale factor.
         * It is invoked by the exposed methods of interactions.
         * @param x
         * @param y
         * @param scale
         */
        private void manipulate(double x, double y, double scale)
        {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            double centerX = context.getCoordinates().getRealMin() + (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) * x / width;
            double centerY = context.getCoordinates().getImagMax() - (context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) * y / height;
            double tempRealMin = centerX - Math.abs(context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) / (2 * scale);
            double tempRealMax = centerX + Math.abs(context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) / (2 * scale);
            double tempImagMin = centerY - Math.abs(context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) / (2 * scale);
            double tempImagMax = centerY + Math.abs(context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) / (2 * scale);
            context.getCoordinates().setRealMin(tempRealMin);
            context.getCoordinates().setRealMax(tempRealMax);
            context.getCoordinates().setImagMin(tempImagMin);
            context.getCoordinates().setImagMax(tempImagMax);
        }

        /**
         * Zoom into the canvas at an exact point. Throw a PrecisionLimitException if the zooming gets too deep.
         * @param x
         * @param y
         * @throws PrecisionLimitException
         */
        public void zoomIn(double x, double y) throws PrecisionLimitException
        {
            if (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin() < 0.000000000000001)
                throw new PrecisionLimitException();
            manipulate(x, y, 1.5);
        }

        /**
         * Zoom out from the canvas at an excat point.
         * @param x
         * @param y
         */
        public void zoomOut(double x, double y)
        {
            manipulate(x, y, 0.6666666666666666);
        }

        /**
         * Move the canvas without zooming.
         * @param x
         * @param y
         */
        public void shift(double x, double y)
        {
            manipulate(x, y, 1);
        }

        /**
         * Update graphical labels with current position in the canvas.
         * @param x
         * @param y
         */
        public void showCurrentLocation(double x, double y)
        {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            Double tempX = context.getCoordinates().getRealMin() + (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) * x / width;
            Double tempY = context.getCoordinates().getImagMax() - (context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) * y / height;
            DecimalFormat formatter = new DecimalFormat("+#,##0.00000000000000;-#");
            xCoordinate.setText("x: " + formatter.format(tempX));
            yCoordinate.setText("y: " + formatter.format(tempY));
        }

        public void setIterations(int value)
        {
            context.setIterations(value);
        }

        public int getIterations()
        {
            return context.getIterations();
        }

        public void toggleSmoothing()
        {
            context.toggleSmoothing();
        }

        public void toggleEqualization()
        {
            context.toggleEqualization();
        }

        //_________________________________________________

        /**
         * Save and returns actual coordinates through a Memento object.
         * @return
         */
        public Memento saveState()
        {
            return new FractalMemento();
        }

        /**
         * Restore a Memento state into the explorer.
         * @param memento
         */
        public void restoreState(Memento memento)
        {
            memento.restore();
        }

        /**
         * Nested class implementing Memento pattern.
         * @author Antonio Spoleto Junior
         */
        class FractalMemento implements Memento
        {
            private Coordinates savedCoordinates;

            /**
             * Clone actual coordinates state into a Memento.
             */
            public FractalMemento()
            {
                savedCoordinates = context.getCoordinates().clone();
            }

            /**
             * Restore coordinates state into the explorer.
             */
            public void restore()
            {
                context.setCoordinates(savedCoordinates);
            }
        }
    }

    /**
     * Single constructor which stores a strategy and initialize the history of Mementos.
     * @param fractal
     */
    public RendererController(FractalStrategy fractal)
    {
        this.fractal = fractal;
        history = new Stack<>();
    }

    /**
     * Initialize renderer scene controller.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        explorer = new FractalExplorer();
        explorer.setStrategy(fractal);
        explorer.render();

        slider.valueProperty().addListener((obs, oldval, newVal) -> slider.setValue(newVal.intValue()));
        slider.setValue(explorer.getIterations());
    }

    /**
     * Listen to mouse events.
     * @param event
     */
    @FXML
    public void mouseListener(MouseEvent event)
    {
        double xCoord = event.getX();
        double yCoord = event.getY();
        if(event.isControlDown())//Send coordinates to JuliaStrategy for live update of the canvas
        {
            if(fractal instanceof JuliaStrategy)
                explorer.render(xCoord, yCoord);
        }
        else if(event.getButton() == MouseButton.PRIMARY)//If left click, zoom in
        {
            history.push(explorer.saveState());
            try
            {
                explorer.zoomIn(xCoord, yCoord);
            } catch (PrecisionLimitException e)
            {
                if (!history.empty()) explorer.restoreState(history.pop());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
            explorer.render();
        }
        else if(event.getButton() == MouseButton.SECONDARY)//If right click, zoom out
        {
            history.push(explorer.saveState());
            explorer.zoomOut(xCoord, yCoord);
            explorer.render();
        }
    }

    /**
     * Handles the export/saving feature.
     * @param event
     */
    @FXML
    void saveListener(MouseEvent event)
    {
        List<String> choices = new ArrayList<>();
        choices.add("2000x2000");
        choices.add("1500x1500");
        choices.add("1000x1000");
        choices.add("600x600");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("1000x1000", choices);
        dialog.setTitle("Save image");
        dialog.setHeaderText("Choose a resolution");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            String[] resolution = result.get().split("x");
            int width = Integer.parseInt(resolution[0]);
            int height = Integer.parseInt(resolution[1]);
            explorer.saveSnapshotToFile(width, height);
        }
    }

    /**
     * Listen ot key events.
     * @param event
     */
    @FXML
    public void keyListener(KeyEvent event)
    {
        if (event.getCode() == KeyCode.SPACE)
        {
            if (!history.empty()) explorer.restoreState(history.pop());
            explorer.render();
        } else if (event.getCode() == KeyCode.W)
        {
            history.push(explorer.saveState());
            explorer.shift(300, 220);
            explorer.render();
        } else if (event.getCode() == KeyCode.S)
        {
            history.push(explorer.saveState());
            explorer.shift(300, 280);
            explorer.render();
        } else if (event.getCode() == KeyCode.A)
        {
            history.push(explorer.saveState());
            explorer.shift(270, 250);
            explorer.render();
        } else if (event.getCode() == KeyCode.D)
        {
            history.push(explorer.saveState());
            explorer.shift(330, 250);
            explorer.render();
        }
    }

    /**
     * Listen to cursor position.
     * @param event
     */
    @FXML
    public void positionListener(MouseEvent event)
    {
        double xCoord = event.getX();
        double yCoord = event.getY();
        explorer.showCurrentLocation(xCoord, yCoord);
    }

    /**
     * Handles the option checkboxes for changing colouring algorithm and turn on equalization
     * @param event
     */
    @FXML
    public void checkBoxListener(ActionEvent event)
    {
        CheckBox checkBox = (CheckBox) event.getSource();
        if (checkBox.getId().equals("smoothing")) explorer.toggleSmoothing();
        if (checkBox.getId().equals("equalization")) explorer.toggleEqualization();
        explorer.render();
    }

    /**
     * Listen to events on the bottom slider.
     */
    @FXML
    public void sliderListener()
    {
        explorer.setIterations((int) slider.getValue());
        explorer.render();
    }

    @Override
    @FXML
    public void changeScene(Event event)
    {
        if (event.getSource() == homeButton)
        {
            try
            {
                Node currentNode = (Node) event.getSource(); //prendi l'oggetto che ha generato l'event
                Parent root = FXMLLoader.load(getClass().getResource("../FXML/landing.fxml")); //carica il root della nuova scena
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
                Scene currentScene = anchorPane.getScene(); //prendi la sua scena
                Stage window = (Stage) currentNode.getScene().getWindow();
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
        } else if (event.getSource() == backButton)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/family.fxml"));
                FamilyController controller = new FamilyController(fractal.getPalette());
                loader.setController(controller);
                Parent root = loader.load(); //carica il root della nuova scena
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
                Stage window = (Stage) anchorPane.getScene().getWindow();
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
        }
    }
}

