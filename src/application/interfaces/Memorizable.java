package application.interfaces;

/**
 * Memorizable interface implemented by the explorer for exporting capabilities.
 * @author Antonio Spoleto Junior
 */
public interface Memorizable
{
    /**
     * Export canvas snapshot to a file.
     * @param width
     * @param height
     */
    void saveSnapshotToFile(int width, int height);

    /**
     * Export context state to a file.
     */
    void saveContextToFile();
}
