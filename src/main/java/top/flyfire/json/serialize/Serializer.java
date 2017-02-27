package top.flyfire.json.serialize;


import top.flyfire.json.*;
import top.flyfire.json.mark.*;
import top.flyfire.json.serialize.component.StructNode;
import top.flyfire.json.serialize.component.defaults.JsonArrayStruct;
import top.flyfire.json.serialize.component.defaults.JsonObjectStruct;
import top.flyfire.json.serialize.component.defaults.JsonValue;

/**
 * Created by devll on 2016/11/9.
 */
public class Serializer implements Peeker ,Parser {

    private JsonValue value;

    private JsonMarkBuilder markBuilder;

    private JsonRoute route;

    private JsonMarkPool markPool;

    private Object mark;

    private boolean breakOff;

    private Parser JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public Serializer(Object object, JsonMarkBuilder markBuilder){
        this.markBuilder = markBuilder;
        this.route = new JsonRoute();
        this.markPool = new JsonMarkPool(this.route);
        value = JsonValue.buildValue(object,null);
        JSONVALUEDPARSER = new JsonValuedParser();
        JSONKYSTRUCTPARSER = new JsonObjectParser();
        JSONIDXSTRUCTPARSER = new JsonArrayParser();
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
    public Parser peek() {
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

    public class JsonValuedParser implements Parser{

        @Override
        public void parse() {
            mark = getJsonValued().getCached();
            markValue(false,false,false);
            route.pop();
        }
    }

    public class JsonObjectParser implements Parser ,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                boolean isBreaker = false;
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushObjectKey((String) entry.indexing());
                    isBreaker = markIndex(entry.indexing(),true);
                    value = entry.value();
                    Serializer.this.parse();
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


    public class JsonArrayParser implements Parser ,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                boolean isBreak = false;
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushArrayIndex((int) entry.indexing());
                    isBreak = markIndex(entry.indexing(),false);
                    value = entry.value();
                    Serializer.this.parse();
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

    private void markValue(boolean hasWrapper,boolean isNull,boolean isUndefined){
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
