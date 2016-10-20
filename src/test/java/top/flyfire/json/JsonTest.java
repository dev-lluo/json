package top.flyfire.json;

import org.junit.Test;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/6/21.
 */
public class JsonTest {

    @Test
    public void testFormat() throws Exception {
        String json = Json.format("   {a:   123       ,  \" b\" :    \"123\"\r\n,  c:  \"2015-12-12 12:12:12\"  ,   d    :  [1,2,3  , [ 4,5]]  ,e:456}");
        System.out.println(json);
    }

}