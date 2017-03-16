package top.flyfire.json;

import org.junit.Test;

import java.util.Date;

/**
 * Created by devll on 2017/3/16.
 */
public class StringTest {

   public static class Getter<T>{
       private T t;

       public T getT() {
           return t;
       }

       public Getter(T t) {
           this.t = t;
       }
   }

   private Getter<Integer> g1 = new Getter<>(1);

   private Getter<char[]> g2 = new Getter<>(new char[]{'1','2'});

    @Test
    public void test(){
        StringBuilder builder = new StringBuilder();
        builder.append(g2.getT());
        Object[] objects = new Object[]{1,2.3,"123",new char[]{'a'},new Date()};
        for(Object object : objects){
            builder.append(object);
        }
        System.out.println(builder);
    }


}
