package sample;
import javafx.scene.canvas.Canvas;

class FractalExplorer
{
    private FractalStrategy strategy;
    private Coordinates coords;

    FractalExplorer()
    {
        coords = new Coordinates();
    }

    public void setStrategy(FractalStrategy strategy)
    {
        this.strategy = strategy;
        strategy.init(coords);
    }

    public void render(Canvas canvas)
    {
        strategy.render(canvas,coords);
    }

    private void manipulate(double x, double y, double scale)
    {
        double centerX = coords.getRealMin() + (coords.getRealMax() - coords.getRealMin()) * x / 600;
        double centerY = coords.getImagMax() - (coords.getImagMax() - coords.getImagMin()) * y / 500;
        double tempRealMin = centerX - Math.abs(coords.getRealMax() - coords.getRealMin()) / (2 * scale);
        double tempRealMax = centerX + Math.abs(coords.getRealMax() - coords.getRealMin()) / (2 * scale);
        double tempImagMin = centerY - Math.abs(coords.getImagMax() - coords.getImagMin()) / (2 * scale);
        double tempImagMax = centerY + Math.abs(coords.getImagMax() - coords.getImagMin()) / (2 * scale);
        coords.setRealMin(tempRealMin);
        coords.setRealMax(tempRealMax);
        coords.setImagMin(tempImagMin);
        coords.setImagMax(tempImagMax);
    }

    public void zoomIn(double x, double y)
    {
        manipulate(x,y, 1.5);
    }

    public void zoomOut(double x, double y)
    {
        manipulate(x,y, 0.6666666666666666);
    }

    public void shift(double x, double y)
    {
        manipulate(x,y, 1);
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
        private Coordinates savedCoords;

        public FractalMemento()
        {
            savedCoords = coords.clone();
        }

        public void restore()
        {
            coords = savedCoords;
        }
    }
}

