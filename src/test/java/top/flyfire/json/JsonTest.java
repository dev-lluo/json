package top.flyfire.json;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/6/21.
 */
public class JsonTest {

    @Test
    public void testDeserialize() throws Exception {
        Map map = (Map) Json.deserialize("{a:123,b:\"123\",c:\"2015-12-12 12:12:12\"}");
        System.out.println(map);
    }
}