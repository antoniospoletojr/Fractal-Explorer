package sample;
import javafx.scene.canvas.Canvas;
import java.util.Stack;

class FractalExplorer
{
    private FractalStrategy strategy;
    private Stack<FractalStrategy.FractalMemento> history;

    FractalExplorer()
    {
        history = new Stack();
    }

    public void setStrategy(FractalStrategy strategy)
    {
        this.strategy = strategy;
    }

    public void render(Canvas canvas)
    {
        strategy.render(canvas);
        history.push(strategy.createMemento());
    }

    public void undo()
    {
        strategy.restore(history.peek());
    }

    public void zoomIn(double x, double y)
    {
        strategy.manipulate(x, y, 1.5);
    }

    public void zoomOut(double x, double y)
    {

    }
}

