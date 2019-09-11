package application.exceptions;
public class PrecisionLimitException extends Exception
{
    private String subMessage;

    public PrecisionLimitException()
    {
        super("Precision limit reached. Zooming won't be allowed any further!");
        this.subMessage = "";
    }

    public PrecisionLimitException(String message)
    {
        super(message);
        this.subMessage = "";
    }

    public PrecisionLimitException(String message, String subMessage)
    {
        super(message);
        this.subMessage = "";
    }

    public String toString()
    {
        return super.getMessage() + (this.subMessage.equals("") ? "" : ":" + this.subMessage);
    }
}

