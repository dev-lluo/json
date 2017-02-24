package top.flyfire.json;

import org.junit.Test;

/**
 * Created by devll on 2017/2/22.
 */
public class JsonRouteTest {

    @Test
    public void testRoute(){
        JsonRoute route = new JsonRoute();
        System.out.println(route.get());
        route.pushObjectKey("items");
        System.out.println(route.get());
        route.pushArrayIndex(0);
        System.out.println(route.get());
        route.pop();
        System.out.println(route.get());
        route.pushArrayIndex(1);
        System.out.println(route.get());
    }

}
