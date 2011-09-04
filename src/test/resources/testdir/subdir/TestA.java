package testdir.subdir;

import org.apache.log4j.Logger;

public class TestA {
    private static final Logger LOGGER = Logger.getLogger(TestA.class);

    public void doSomeWork() {

        //@LOGDOC logs entries when ever doSomeWork is called
        LOGGER.info("doSomeWork is called");
    }
}
