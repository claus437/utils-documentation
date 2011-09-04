package dk.fujitsu.utils.maven.documentation;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: claus
 * Date: 03-09-11
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public interface FileHandler {
    void onRoundBegin(File file);
    void onFileBegin(File file);
    void onLineRead(String line);

    /**
     *
     * @return true to re-read the file
     */
    boolean onFileComplete();

    /**
     *
     * @return true to hook into another run.
     */
    boolean onRoundComplete();

    void destroy();
}
