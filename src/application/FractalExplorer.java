package application;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.WritableImage;

class FractalExplorer
{

    private Service<Void> thread;
    private FractalStrategy strategy;
    private Coordinates coordinates;
    private ProgressIndicator indicator;


    FractalExplorer(ProgressIndicator indicator)
    {
        this.indicator = indicator;
        coordinates = new Coordinates();
    }

    public void setStrategy(FractalStrategy strategy)
    {
        this.strategy = strategy;
        this.strategy.init(coordinates);
    }

    public void render(Canvas canvas)
    {
        WritableImage offScreen = new WritableImage(600, 500);
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
                        strategy.render(offScreen.getPixelWriter(),coordinates);
                        return null;
                    }
                };
            }
        };
        thread.start();
        thread.setOnSucceeded(t -> {
            canvas.getGraphicsContext2D().drawImage(offScreen, 0, 0);
            indicator.setProgress(1);
        });
    }

    private void manipulate(double x, double y, double scale)
    {
        double centerX = coordinates.getRealMin() + (coordinates.getRealMax() - coordinates.getRealMin()) * x / 600;
        double centerY = coordinates.getImagMax() - (coordinates.getImagMax() - coordinates.getImagMin()) * y / 500;
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
        private Coordinates savedcoordinates;

        public FractalMemento()
        {
            savedcoordinates = coordinates.clone();
        }

        public void restore()
        {
            coordinates = savedcoordinates;
        }
    }
}

