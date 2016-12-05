package top.flyfire.json;

import org.junit.Test;

/**
 * Created by devll on 2016/12/4.
 */
public class TimeTest {

    @Test
    public void test() throws Exception {
        Thread.sleep(1000);
        test(2);
    }

    public void test(int i) throws Exception {
        if (i > 0) {
            test(i - 1);
        }
        Thread.sleep(500);
    }

}
