package top.flyfire.json.deserialize;

/**
 * Created by devll on 2016/10/18.
 */
public class FormatTrigger implements DeserializeTrigger {
    @Override
    public void newStruct(int type, int level) {
        if(type==0){
            System.out.println("[");
        }else{
            System.out.println("{");
        }
    }

    @Override
    public void endStruct(int type, int level) {
        for(int i = 0;i<level;i++){
            System.out.print("\t");
        }
        if(type==0){
            System.out.println("]");
        }else{
            System.out.println("}");
        }
    }

    @Override
    public void indexing(Object index,int level) {
        for(int i = 0;i<level;i++){
            System.out.print("\t");
        }
        System.out.print(index);
        System.out.print(" : ");
    }

    @Override
    public void rawValue(String value,int level) {
        System.out.println(value);
    }
}
