package application.structural;

import application.exceptions.IncorrectSplineDataException;
import application.processing.ColorPalette;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class ColorPaletteBuilder
{
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    private Color color5;
    static int colorsCount = 0;

    private static class SingletonHolder
    {
        public static final ColorPaletteBuilder instance = new ColorPaletteBuilder();
    }

    private ColorPaletteBuilder(){}

    public static ColorPaletteBuilder getInstance()
    {
        return SingletonHolder.instance;
    }

    public ColorPaletteBuilder colorsCount(int colorsCount)
    {
        this.colorsCount = colorsCount;
        return this;
    }

    public ColorPaletteBuilder color1(Color color)
    {
        if(color1==null)
            colorsCount++;
        this.color1 = color;
        return this;
    }

    public ColorPaletteBuilder color2(Color color)
    {
        if(color2==null)
            colorsCount++;
        this.color2 = color;
        return this;
    }

    public ColorPaletteBuilder color3(Color color)
    {
        if(color3==null)
            colorsCount++;
        this.color3 = color;
        return this;
    }

    public ColorPaletteBuilder color4(Color color)
    {
        if(color4==null)
            colorsCount++;
        this.color4 = color;
        return this;
    }

    public ColorPaletteBuilder color5(Color color)
    {
        if(color5==null)
            colorsCount++;
        this.color5 = color;
        return this;
    }

    public static int getColorsCount()
    {
        return colorsCount;
    }

    public ColorPalette build() throws IncorrectSplineDataException
    {
        ArrayList<Double> redList = new ArrayList<>();
        ArrayList<Double> greenList = new ArrayList<>();
        ArrayList<Double> blueList = new ArrayList<>();
        Color[] colors = {color1,color2, color3, color4, color5};
        for(int i=0; i<colors.length; i++)
        {
            if(colors[i]!=null)
            {
                redList.add(colors[i].getRed()*255);
                greenList.add(colors[i].getGreen()*255);
                blueList.add(colors[i].getBlue()*255);
            }
        }
        double[] red = new double[colorsCount];
        double[] green = new double[colorsCount];
        double[] blue = new double[colorsCount];

        for (int i = 0; i < colorsCount; i++)
        {
            red[i] = redList.get(i);
            green[i] = greenList.get(i);
            blue[i] = blueList.get(i);
        }
        return new ColorPalette(red, green, blue);
    }

    @Override
    public String toString()
    {
        return "ColorPaletteBuilder{" + "color1=" + color1 + ", color2=" + color2 + ", color3=" + color3 + ", color4=" + color4 + ", color5=" + color5 + '}';
    }
}
