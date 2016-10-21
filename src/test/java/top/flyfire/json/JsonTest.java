package top.flyfire.json;

import org.junit.Test;
import top.flyfire.json.component.defaults.parse.ParseJavaObjectCpt;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/6/21.
 */
public class JsonTest {

    private String jsonData = "   {a:   123       ,  \" b\" :    \"123\"\r\n,  c:  \"2015-12-12 12:12:12\"  ,   d    :  [1,2,3  , [ 4,5]]  ,e:456}";

    @Test
    public void testFormat() throws Exception {
        String json = Json.format(jsonData);
        System.out.println(json);
    }

    @Test
    public void testParse() throws Exception {
        ParseJavaObjectCpt parseJavaObjectCpt = new ParseJavaObjectCpt();
        Json.exec(jsonData,parseJavaObjectCpt);
        Object object = parseJavaObjectCpt.result();
        System.out.println(object);
    }

}