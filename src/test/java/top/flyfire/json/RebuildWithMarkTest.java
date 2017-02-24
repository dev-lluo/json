package top.flyfire.json;

import org.junit.Test;
import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.deserialize.Deserializer;
import top.flyfire.json.deserialize.component.defaults.highparse.HighParseJavaObjectBuilder;
import top.flyfire.json.deserialize.component.defaults.parse.ParseJavaObjectBuilder;

/**
 * Created by shyy_work on 2017/2/24.
 */
public class RebuildWithMarkTest {

    private String jsonData = "   {abcd:   123       ,  \"b\" :    \"123\"\r\n,  c:  \"2015-12-12 12:12:12\"  ,   d    :  [1,2,3  , [ 4,5]]  ,e:456,f:[  ],g:{ },h:{a:123}}";

    @Test
    public void testSimpleParse(){
        ParseJavaObjectBuilder parseJavaObjectBuilder = new ParseJavaObjectBuilder();
        new Deserializer(jsonData,parseJavaObjectBuilder).parse();
        System.out.print(parseJavaObjectBuilder.get());
    }

    @Test
    public void testHighParse() throws Exception {
        HighParseJavaObjectBuilder highParseJavaObjectBuilder = new HighParseJavaObjectBuilder(ReflectUtils.getMetaInfo(TestBean.class));
        new Deserializer(jsonData,highParseJavaObjectBuilder).parse();
        System.out.println(highParseJavaObjectBuilder.get());

    }

}
