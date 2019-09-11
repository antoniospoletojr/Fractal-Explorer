package application.processing;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageProcessing
{
    public static final int SS_FACTOR = 2;

    public static WritableImage supersampling(WritableImage inputImage)
    {
        WritableImage outputImage = new WritableImage((int)inputImage.getWidth()/SS_FACTOR,(int)inputImage.getHeight()/SS_FACTOR);
        PixelWriter writer = outputImage.getPixelWriter();
        PixelReader reader = inputImage.getPixelReader();
        for (int x = 0, i=0; x < inputImage.getWidth()-SS_FACTOR; x=x+SS_FACTOR, i++)
        {
            for (int y = 0, j=0; y < inputImage.getHeight()-SS_FACTOR; y=y+SS_FACTOR, j++)
            {

                writer.setColor(i,j,meanFilter(x,y,reader));
            }
        }
        return outputImage;
    }

    private static Color meanFilter(int x, int y, PixelReader reader)
    {
        double R=0,G=0,B=0;
        for (int k = 0; k < SS_FACTOR; k++)
        {
            for (int l = 0; l < SS_FACTOR; l++)
            {
                R += reader.getColor(x+k,y+k+l).getRed();
                B += reader.getColor(x+k,y+k+l).getBlue();
                G += reader.getColor(x+k,y+k+l).getGreen();
            }
        }
        return Color.color(R/(2*SS_FACTOR),G/(2*SS_FACTOR),B/(2*SS_FACTOR));
    }

    //private static Color medianFilter();
}
