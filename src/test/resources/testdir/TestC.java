package testdir.subdir;

import org.apache.log4j.Logger;

public class TestC {
    private static final Logger LOGGER = Logger.getLogger(TestC.class);

    public void doSomeWork() {
        //@LOGDOC logs entries when ever doSomeWork is called and no else
        LOGGER.info("doSomeWork is called twice" + 10 + 10);
    }
}
