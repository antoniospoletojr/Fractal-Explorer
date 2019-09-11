package application.exceptions;

/**
 * Exception class used when incorrect fields type or numbers are submitted for interpolation.
 * @author Antonio Spoleto Junior
 */
public class IncorrectSplineDataException extends Exception
{
    private String subMessage;

    public IncorrectSplineDataException()
    {
        super("Incorrect input data for spline creation.");
        this.subMessage = "";
    }

    public IncorrectSplineDataException(String message)
    {
        super(message);
        this.subMessage = "";
    }

    public IncorrectSplineDataException(String message, String subMessage)
    {
        super(message);
        this.subMessage = "";
    }

    public String toString()
    {
        return super.getMessage() + (this.subMessage.equals("") ? "" : ":" + this.subMessage);
    }
}

