package testdir.subdir;

import org.apache.log4j.Logger;

public class TestB {
    private static final Logger LOGGER = Logger.getLogger(TestB.class);

    public void doSomeWork() {
        
        //@LOGDOC logs entries when ever doSomeWork is called
        LOGGER.info("doSomeWork is called");
    }
}
