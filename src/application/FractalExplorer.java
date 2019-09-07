package application;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class FractalExplorer
{

    //Thread related
    private Service<Void> thread;
    private ExecutorService executor;
    //Coordinates
    private Coordinates coordinates;
    //GUI
    private Canvas canvas;
    private ProgressBar indicator;
    private Text timeText;
    private Text xCoordinate;
    private Text yCoordinate;

    private FractalStrategy strategy;

    FractalExplorer(ProgressBar indicator, Canvas canvas, Text timeText, Text xCoordinate, Text yCoordinate)
    {
        this.timeText = timeText;
        this.indicator = indicator;
        this.canvas = canvas;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.coordinates = new Coordinates();
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true); // allows app to exit if tasks are running
            return thread ;
        });
    }

    public void setStrategy(FractalStrategy strategy)
    {
        this.strategy = strategy;
        this.strategy.init(coordinates);
    }

    public void render()
    {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        WritableImage offScreen = new WritableImage((int) width, (int) height);
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
                        Long elapsedTime = strategy.render(offScreen, coordinates);
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
            canvas.getGraphicsContext2D().drawImage(offScreen, 0, 0);
            indicator.setProgress(1);
        });
    }

    private void manipulate(double x, double y, double scale)
    {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double centerX = coordinates.getRealMin() + (coordinates.getRealMax() - coordinates.getRealMin()) * x / width;
        double centerY = coordinates.getImagMax() - (coordinates.getImagMax() - coordinates.getImagMin()) * y / height;
        double tempRealMin = centerX - Math.abs(coordinates.getRealMax() - coordinates.getRealMin()) / (2 * scale);
        double tempRealMax = centerX + Math.abs(coordinates.getRealMax() - coordinates.getRealMin()) / (2 * scale);
        double tempImagMin = centerY - Math.abs(coordinates.getImagMax() - coordinates.getImagMin()) / (2 * scale);
        double tempImagMax = centerY + Math.abs(coordinates.getImagMax() - coordinates.getImagMin()) / (2 * scale);
        coordinates.setRealMin(tempRealMin);
        coordinates.setRealMax(tempRealMax);
        coordinates.setImagMin(tempImagMin);
        coordinates.setImagMax(tempImagMax);
    }

    public void zoomIn(double x, double y)
    {
        manipulate(x, y, 1.5);
    }

    public void zoomOut(double x, double y)
    {
        manipulate(x, y, 0.6666666666666666);
    }

    public void shift(double x, double y)
    {
        manipulate(x, y, 1);
    }

    public void showCurrentLocation(double x, double y)
    {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        Double tempX = coordinates.getRealMin() + (coordinates.getRealMax() - coordinates.getRealMin()) * x / width;
        Double tempY = coordinates.getImagMax() - (coordinates.getImagMax() - coordinates.getImagMin()) * y / height;
        xCoordinate.setText("x: " + String.format("%.8f",tempX));
        yCoordinate.setText("y: " + String.format("%.8f",tempY));
    }

    public Memento saveState()
    {
        return new FractalMemento();
    }

    public void restoreState(Memento memento)
    {
        memento.restore();
    }

    class FractalMemento implements Memento
    {
        private Coordinates savedCoordinates;

        public FractalMemento()
        {
            savedCoordinates = coordinates.clone();
        }

        public void restore()
        {
            coordinates = savedCoordinates;
        }
    }
}

