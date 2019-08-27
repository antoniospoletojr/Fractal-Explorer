package application;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;

class FractalExplorer
{

    private Service<Void> thread;
    private FractalStrategy strategy;
    private Canvas canvas;
    private Coordinates coordinates;
    private ProgressIndicator indicator;
    private Text timeText;

    FractalExplorer(ProgressIndicator indicator, Canvas canvas, Text timeText)
    {
        this.timeText = timeText;
        this.indicator = indicator;
        this.canvas = canvas;
        coordinates = new Coordinates();
    }

    public void setStrategy(FractalStrategy strategy)
    {
        this.strategy = strategy;
        this.strategy.init(coordinates);
        this.strategy.setIterations(30);
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
        thread.start();
        thread.setOnSucceeded(t ->
        {
            synchronized (canvas)
            {
                canvas.getGraphicsContext2D().drawImage(offScreen, 0, 0);
            }
            indicator.setProgress(1);
        });
    }

    public void setIterations(int iterations)
    {
        this.strategy.setIterations(iterations);
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

