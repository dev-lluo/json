package top.flyfire.json;

import org.junit.Test;
import top.flyfire.json.deserialize.Deserializer;
import top.flyfire.json.deserialize.FormatTrigger;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/6/21.
 */
public class JsonTest {

    @Test
    public void testDeserialize() throws Exception {
        new Deserializer("{a:123,b:\"123\",c:\"2015-12-12 12:12:12\",d:[1,2,3]}",new FormatTrigger()).deserialize();
    }

}