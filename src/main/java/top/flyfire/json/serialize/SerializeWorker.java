package top.flyfire.json.serialize;


import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.*;
import top.flyfire.json.event.JsonWorkListener;
import top.flyfire.json.event.JsonEventPool;
import top.flyfire.json.serialize.component.StructNode;
import top.flyfire.json.serialize.component.defaults.JsonArrayStruct;
import top.flyfire.json.serialize.component.defaults.JsonObjectStruct;
import top.flyfire.json.serialize.component.defaults.JsonValue;

/**
 * Created by devll on 2016/11/9.
 */
public class SerializeWorker implements JsonMaster,JsonWorker {

    private JsonValue value;

    private JsonWorkListener markBuilder;

    private JsonRoute route;

    private JsonEventPool markPool;

    private Object mark;

    private boolean breakOff;

    private JsonWorker JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public SerializeWorker(Object object, JsonWorkListener markBuilder){
        this.markBuilder = markBuilder;
        this.route = new JsonRoute();
        this.markPool = new JsonEventPool(this.route);
        value = JsonValue.buildValue(object,null);
        JSONVALUEDPARSER = new JsonValuedWorker();
        JSONKYSTRUCTPARSER = new JsonObjectWorker();
        JSONIDXSTRUCTPARSER = new JsonArrayWorker();
    }

    private JsonValue getJsonValued(){
        return (JsonValue) value;
    }

    private StructNode getStructSwap(){
        return (StructNode) value;
    }

    @Override
    public void parse() {
        peek().parse();
    }

    @Override
    public JsonWorker peek() {
        if(value instanceof JsonObjectStruct){
            return JSONKYSTRUCTPARSER;
        }else if(value instanceof JsonArrayStruct){
            return JSONIDXSTRUCTPARSER;
        }else if(value instanceof JsonValue){
            return JSONVALUEDPARSER;
        }else {
            throw new RuntimeException();
        }
    }


    private void flush(){
        JsonValue temp = value;
        value = temp.getParent();
        temp.destroy();
    }

    public class JsonValuedWorker implements JsonWorker {

        @Override
        public void parse() {
            mark = getJsonValued().getCached();
            boolean isNull = mark == null;
            boolean hasWrapper = false;
            if(!isNull){
                hasWrapper = !ReflectUtils.isJdkPrimitiveType(mark.getClass());
            }
            markValue(isNull,false, hasWrapper);
            route.pop();
        }
    }

    public class JsonObjectWorker implements JsonWorker,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                boolean isBreaker = false;
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushObjectKey((String) entry.indexing());
                    isBreaker = markIndex(entry.indexing(),true);
                    value = entry.value();
                    SerializeWorker.this.parse();
                    reset(isBreaker);
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            markOpen(true);
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            flush();
            boolean b = getStructSwap().hasNext();
            markNext(b);
            return b;
        }

        @Override
        public void validateAndEnd() {
            markClose(true);
            route.pop();
        }
    }


    public class JsonArrayWorker implements JsonWorker,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                boolean isBreak = false;
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushArrayIndex((int) entry.indexing());
                    isBreak = markIndex(entry.indexing(),false);
                    value = entry.value();
                    SerializeWorker.this.parse();
                    reset(isBreak);
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            markOpen(false);
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            flush();
            boolean b = getStructSwap().hasNext();
            markNext(b);
            return b;
        }

        @Override
        public void validateAndEnd() {
            markClose(false);
            route.pop();
        }
    }

    private void markValue(boolean isNull,boolean isUndefined, boolean hasWrapper){
        if(breakOff) return;
        markBuilder.markValue(markPool.borrowValue(mark,isNull,isUndefined,hasWrapper));
    }

    private void markOpen(boolean forObject){
        if(breakOff) return;
        markBuilder.markOpen(markPool.borrowStruct(forObject));
    }

    private void markClose(boolean forObject){
        if(breakOff) return;
        markBuilder.markClose(markPool.borrowStruct(forObject));
    }

    private boolean markIndex(Object index,boolean forObject){
        if(breakOff) return false;
        return breakOff = markBuilder.markIndex(markPool.borrowIndex(index,forObject));
    }

    private void markNext(boolean hasNext){
        if(breakOff) return;
        markBuilder.markNext(markPool.borrowNext(hasNext));
    }

    private void reset(boolean isBreaker){
        if(isBreaker){
            breakOff = false;
        }
    }

}
