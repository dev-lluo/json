package top.flyfire.json;

import org.junit.Test;
import top.flyfire.common.reflect.RawType;
import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.deserialize.component.defaults.highparse.HighParseJavaObjectCpt;
import top.flyfire.json.deserialize.component.defaults.parse.ParseJavaObjectCpt;
import top.flyfire.json.serialize.Serializer;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/6/21.
 */
public class JsonTest {

    private String jsonData = "   {a:   123       ,  \"b\" :    \"123\"\r\n,  c:  \"2015-12-12 12:12:12\"  ,   d    :  [1,2,3  , [ 4,5]]  ,e:456}";

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
        Serializer serializer = new Serializer(object);
        serializer.parse();
    }

    @Test
    public void testHighParse() throws Exception {
        HighParseJavaObjectCpt highParseJavaObjectCpt = new HighParseJavaObjectCpt(ReflectUtils.unWrap(TestBean.class));
        Json.exec(jsonData,highParseJavaObjectCpt);
        Object object = highParseJavaObjectCpt.result();
        System.out.println(object);

    }

    @Test
    public void testHighParse2() throws Exception {
        Object object = Json.parse("{a:123,b:456,c:{a:name,c:'string'}}",new RawType<TestPBean<TestPBean>>(){}.getType());
        System.out.println(object);
    }

    @Test
    public void testHighParse3() throws Exception {
        Object object = Json.parse("{a:123,b:456,c:123}",new RawType<TestPBean<Integer>>(){}.getType());
        System.out.println(object);
    }


}