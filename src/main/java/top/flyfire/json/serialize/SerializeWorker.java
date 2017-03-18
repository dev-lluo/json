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

    private JsonWorkListener workListener;

    private JsonRoute route;

    private JsonEventPool eventPool;

    private Object mark;

    private boolean breakOff;

    private JsonWorker JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public SerializeWorker(Object object, JsonWorkListener markBuilder){
        this.workListener = markBuilder;
        this.route = new JsonRoute();
        this.eventPool = new JsonEventPool(this.route);
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
    public void work() {
        call().work();
    }

    @Override
    public JsonWorker call() {
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
        public void work() {
            mark = getJsonValued().getCached();
            boolean isNull = mark == null;
            boolean hasWrapper = false;
            if(!isNull){
                hasWrapper = !ReflectUtils.isJdkPrimitiveType(mark.getClass());
            }
            onValue(isNull,false, hasWrapper);
            route.pop();
        }
    }

    public class JsonObjectWorker implements JsonWorker,Enumeration {

        @Override
        public void work() {
            if(validateAndStart()){
                boolean isBreaker = false;
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushObjectKey((String) entry.indexing());
                    isBreaker = onIndex(entry.indexing(),true);
                    value = entry.value();
                    SerializeWorker.this.work();
                    reset(isBreaker);
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            onOpen(true);
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            flush();
            boolean b = getStructSwap().hasNext();
            onNext(b);
            return b;
        }

        @Override
        public void validateAndEnd() {
            onClose(true);
            route.pop();
        }
    }


    public class JsonArrayWorker implements JsonWorker,Enumeration {

        @Override
        public void work() {
            if(validateAndStart()){
                boolean isBreak = false;
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushArrayIndex((int) entry.indexing());
                    isBreak = onIndex(entry.indexing(),false);
                    value = entry.value();
                    SerializeWorker.this.work();
                    reset(isBreak);
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            onOpen(false);
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            flush();
            boolean b = getStructSwap().hasNext();
            onNext(b);
            return b;
        }

        @Override
        public void validateAndEnd() {
            onClose(false);
            route.pop();
        }
    }

    private void onValue(boolean isNull, boolean isUndefined, boolean hasWrapper){
        if(breakOff) return;
        workListener.onValue(eventPool.borrowValue(mark,isNull,isUndefined,hasWrapper));
    }

    private void onOpen(boolean forObject){
        if(breakOff) return;
        workListener.onOpen(eventPool.borrowStruct(forObject));
    }

    private void onClose(boolean forObject){
        if(breakOff) return;
        workListener.onClose(eventPool.borrowStruct(forObject));
    }

    private boolean onIndex(Object index, boolean forObject){
        if(breakOff) return false;
        return breakOff = workListener.onIndex(eventPool.borrowIndex(index,forObject));
    }

    private void onNext(boolean hasNext){
        if(breakOff) return;
        workListener.onNext(eventPool.borrowNext(hasNext));
    }

    private void reset(boolean isBreaker){
        if(isBreaker){
            breakOff = false;
        }
    }

}
