package lucenecranfield;

import java.io.File;
import java.io.FileFilter;

/**
 * This class is used as a .txt file filter
 */
public class TextFileFilter implements FileFilter
{
    /**
     *
     * @param pathname
     * @return
     */
    @Override
    public boolean accept(File pathname)
    {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
}
