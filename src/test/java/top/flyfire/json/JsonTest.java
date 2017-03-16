package top.flyfire.json;

import org.junit.Test;
import top.flyfire.common.reflect.RawType;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/6/21.
 */
public class JsonTest {

    private String jsonData = "   {abcd:   123       ,  \"b\" :    \"123\"\r\n,  c:  \"2015-12-12 12:12:12\"  ,   d    :  [1,2,3  , [ 4,5]]  ,e:456,f:[  ],g:{ },h:{a:123}}";

    @Test
    public void testFormat() throws Exception {
        String json = Json.format(jsonData);
        System.out.println(json);
    }


    @Test
    public void testHighParse() throws Exception {
        Object object = Json.deserialize(jsonData,TestBean.class);
        System.out.println(object);

    }

    @Test
    public void testHighParse2() throws Exception {
        Object object = Json.deserialize("{a:123,b:456,c:{a:name,c:'string'}}",new RawType<TestPBean<TestPBean>>(){}.getType());
        System.out.println(object);
    }

    @Test
    public void testHighParse3() throws Exception {
        Object object = Json.deserialize("{a:123,b:456,c:123}",new RawType<TestPBean<Integer>>(){}.getType());
        System.out.println(object);
    }

    @Test
    public void testUpdateKernel(){
        String json = "[1,\"123\"]";
        Object object = Json.deserialize(json);
        System.out.print(object);
    }

}