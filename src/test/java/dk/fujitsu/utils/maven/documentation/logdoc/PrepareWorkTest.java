package dk.fujitsu.utils.maven.documentation.logdoc;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: claus
 * Date: 03-09-11
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class PrepareWorkTest extends LogDoc {
    private PrepareWork subject = new PrepareWork();

    public PrepareWorkTest() {
        super(new File("./test.log"));
    }

    @Test
    public void testMaxWidths() {

        subject.onFileBegin("12");
        subject.doWork("12", "12", "12");
        Assert.assertArrayEquals(new int[]{2, 2, 2, 2}, subject.getColumnWidths());

        subject.doWork("123", "12", "12");
        Assert.assertArrayEquals(new int[] {2, 3, 2, 2}, subject.getColumnWidths());

        subject.doWork("12", "123", "12");
        Assert.assertArrayEquals(new int[] {2, 3, 3, 2}, subject.getColumnWidths());

        subject.doWork("12", "12", "123");
        Assert.assertArrayEquals(new int[] {2, 3, 3, 3}, subject.getColumnWidths());

        subject.doWork("12", "12", "12");
        Assert.assertArrayEquals(new int[]{2, 3, 3, 3}, subject.getColumnWidths());

        subject.onFileBegin("123");
        Assert.assertArrayEquals(new int[] {3, 3, 3, 3}, subject.getColumnWidths());
    }
}
