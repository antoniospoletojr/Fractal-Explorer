package sample;
import javafx.scene.canvas.Canvas;

abstract class FractalStrategy implements Memento
{
    protected double realMax;
    protected double realMin;
    protected double imagMax;
    protected double imagMin;


    abstract public void render(Canvas canvas);

    public FractalMemento createMemento()
    {
        return new FractalMemento();
    }

    public void restore(FractalMemento memento)
    {
        memento.restore();
    }

    public void manipulate(double x, double y, double scale)
    {
        double centerX = realMin + (realMax - realMin) * x / 600;
        double centerY = imagMax - (imagMax - imagMin) * y / 500;
        double temp_realMin = 0;
        double temp_realMax = 0;
        double temp_imagMin = 0;
        double temp_imagMax = 0;

        temp_realMin = centerX - Math.abs(realMax - realMin) / (2 * scale);
        temp_realMax = centerX + Math.abs(realMax - realMin) / (2 * scale);
        temp_imagMin = centerY - Math.abs(imagMax - imagMin) / (2 * scale);
        temp_imagMax = centerY + Math.abs(imagMax - imagMin) / (2 * scale);

        realMax = temp_realMax;
        realMin = temp_realMin;
        imagMax = temp_imagMax;
        imagMin = temp_imagMin;
    }

    class FractalMemento implements Memento
    {
        private double savedRealMax;
        private double savedRealMin;
        private double savedImagMax;
        private double savedImagMin;

        public FractalMemento()
        {
            savedRealMax = realMax;
            savedRealMin = realMin;
            savedImagMax = imagMax;
            savedImagMin = imagMin;
        }

        public void restore()
        {
            realMax = savedRealMax;
            realMin = savedRealMin;
            imagMax = savedImagMax;
            imagMin = savedImagMin;
        }
    }
}
