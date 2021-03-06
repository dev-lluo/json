package top.flyfire.json;

import org.junit.Test;
import top.flyfire.json.deserialize.DeserializeWorker;
import top.flyfire.json.deserialize.component.defaults.parse.ParseJavaObjectBuilder;
import top.flyfire.json.serialize.SerializeWorker;
import top.flyfire.json.serialize.component.defaults.tojson.ParseJsonBuilder;

/**
 * Created by shyy_work on 2017/2/24.
 */
public class RebuildWithMarkTest {

    private String jsonData = "   {abcd:   123       ,  \"b\" :    \"123\"\r\n,  c:  \"2015-12-12 12:12:12\"  ,   d    :  [1,2,3  , [ 4,5]]  ,e:456,f:[  ],g:{ },h:{a:123}}";

    @Test
    public void testSimpleParse(){
        ParseJavaObjectBuilder parseJavaObjectBuilder = new ParseJavaObjectBuilder();
        new DeserializeWorker(jsonData,parseJavaObjectBuilder).work();
        Object object = parseJavaObjectBuilder.get();
        System.out.println(object);
        ParseJsonBuilder jsonBuilder2 = new ParseJsonBuilder();
        new SerializeWorker(object,jsonBuilder2).work();
        System.out.println(jsonBuilder2.get());
    }

    @Test
    public void testHighParse() throws Exception {
        Object object = Json.deserialize(jsonData,TestBean.class);
        System.out.println(Json.serialize(object));
    }

}
