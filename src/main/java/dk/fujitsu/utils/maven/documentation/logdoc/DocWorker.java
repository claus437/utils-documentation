package dk.fujitsu.utils.maven.documentation.logdoc;

/**
 * Created by IntelliJ IDEA.
 * User: claus
 * Date: 03-09-11
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public interface DocWorker {
    void onFileBegin(String className);
    void doWork(String level, String comment, String statement);
}
