package top.flyfire.json.deserialize;

import top.flyfire.json.*;
import top.flyfire.json.deserialize.exception.UnexpectedEndTokenException;
import top.flyfire.json.deserialize.exception.UnexpectedTokenException;
import top.flyfire.json.event.JsonWorkListener;
import top.flyfire.json.event.JsonEventPool;

/**
 * Created by shyy_work on 2016/6/21.
 */
@SuppressWarnings("all")
public class DeserializeWorker implements JsonMaster,JsonWorker {

    private String source;

    private JsonWorkListener workListener;

    private JsonRoute route;

    private JsonEventPool eventPool;

    private  CharCached markCached;

    private String mark;

    private boolean hasWrapper;

    private boolean breakOff;

    private int cursor, cursorBound, level;

    private JsonWorker STRUCTEDPARSER;

    private JsonWorker INDEXEDPARSER;

    private JsonWorker PRIMITIVEPARSER;

    public DeserializeWorker(String source, JsonWorkListener markBuilder) {
        this.source = source;
        this.cursor = this.level = 0;
        this.cursorBound = source.length();
        this.workListener = markBuilder;
        this.route = new JsonRoute();
        this.eventPool = new JsonEventPool(this.route);
        this.markCached = new CharCached();
        STRUCTEDPARSER = new ObjectWorker();
        INDEXEDPARSER = new ArrayWorker();
        PRIMITIVEPARSER = new PrimitiveWorker();
    }

    @Override
    public void work() {
        call().work();
    }


    @Override
    public JsonWorker call() {
        char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before value
        if (JsonMarker.isArrayStart(dest)) {
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            return INDEXEDPARSER;
        } else if (JsonMarker.isObjectStart(dest)) {
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            return STRUCTEDPARSER;
        } else {
            return PRIMITIVEPARSER;
        }
    }


    private boolean tokenRead(JsonMarker.MarkGroup endGroup) {
        char token = '\0';
        int quoteWrapper,
                escape = 0; //0,1
        if(null!=mark)markCached.clear();
        readFirst:
        {
            for (;JsonMarker.isInvisibleChar(token = fetch()); roll()) ;
            if (token == JsonMark.DOUBLE_QUOTE || token == JsonMark.SINGLE_QUOTE) quoteWrapper = token;
            else if(endGroup.exists(token)) throw new UnexpectedTokenException(source,cursor);
            else quoteWrapper = 0;
        }
        read2End:
        {
            if (hasWrapper = (quoteWrapper > 0)) {
                while (roll()) {
                    token = fetch();
                    if (escape == 0) {
                        if (token == JsonMark.ESCAPE) {
                            escape = 1;
                        } else if ((token ^ quoteWrapper) == 0) {
                            quoteWrapper = 0;
                            break;
                        } else {
                            markCached.push(token);
                        }
                    } else {
                        if (token == 't') {
                            markCached.push('\t');
                        } else if (token == 'r') {
                            markCached.push('\r');
                        } else if (token == 'n') {
                            markCached.push('\n');
                        } else if (token == 'f') {
                            markCached.push('\f');
                        } else if (token == 'b') {
                            markCached.push('\b');
                        } else if (token == '/') {
                            markCached.push('/');
                        } else if (token == '\\') {
                            markCached.push('\\');
                        } else if (token == '"') {
                            markCached.push('"');
                        } else if (token == '\'') {
                            markCached.push('\'');
                        } else {
                            markCached.push(token);
                        }
                        escape = 0;
                    }
                }
                while (roll() && !endGroup.exists(token = fetch())) {
                    if (JsonMarker.isInvisibleChar(token))
                        continue;
                    throw new UnexpectedTokenException(source,cursor);
                }
                ;
            } else {
                boolean notSkip = true;
                markCached.push(token);
                while (roll() && !endGroup.exists(token = fetch())) {
                    if(notSkip) {
                        if(JsonMarker.isInvisibleChar(token)){
                            notSkip = false;
                        }else {
                            markCached.push(token);
                        }
                    }else{
                        if(JsonMarker.isInvisibleChar(token))
                            continue ;
                        throw new UnexpectedTokenException(source,cursor);
                    }
                }
            }
        }
        validate:
        {
            if (quoteWrapper != 0) {
                throw new UnexpectedEndTokenException(source);
            }
        }
        mark = markCached.toString();
        return hasWrapper;
    }

    private char fetch() {
        return source.charAt(cursor);
    }

    public char fetchIgnoreisInvisibleChar() {
        char dest;
        while (JsonMarker.isInvisibleChar(dest = source.charAt(cursor))) {
            if (!roll()) throw new UnexpectedTokenException(source,cursor);
        }
        return dest;
    }

    private boolean roll() {
        return ++cursor < cursorBound;
    }

    private class ObjectWorker implements JsonWorker, Enumeration, JsonMaster {

        private JsonWorker PRIMITIVEPARSER;

        public ObjectWorker() {
            super();
            PRIMITIVEPARSER = new ObjectPrimitiveWorker();
        }

        @Override
        public void work() {
            if (validateAndStart()) {
                boolean isBreaker = false;
                do {
                    key:
                    {
                        tokenRead(JsonMarker.ObjectK2VGroup);
                        route.pushObjectKey(mark);
                        isBreaker = onIndex(mark,true);
                        roll();
                    }
                    value:
                    {
                        call().work();
                    }
                    reset:
                    {
                        reset(isBreaker);
                    }
                } while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public void validateAndEnd() {
            onClose(true);
            route.pop();
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            boolean end = JsonMarker.isObjectEnd(dest),
            next = JsonMarker.isNext(dest);
            if(end||next){
                onNext(next);
                return next;
            }else{
                throw new UnexpectedTokenException(source,cursor);
            }
        }

        @Override
        public boolean validateAndStart() {
            onOpen(true);
            char dest = fetchIgnoreisInvisibleChar();
            if (JsonMarker.isObjectEnd(dest)) {
                roll();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public JsonWorker call() {
            char dest;
            if (JsonMarker.isArrayStart(dest = fetchIgnoreisInvisibleChar())) {//ignore invisible char before value
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return INDEXEDPARSER;
            } else if (JsonMarker.isObjectStart(dest)) {
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return STRUCTEDPARSER;
            } else {
                return PRIMITIVEPARSER;
            }
        }
    }

    private class ArrayWorker implements JsonWorker, Enumeration, JsonMaster {

        private JsonWorker PRIMITIVEPARSER;

        public ArrayWorker() {
            super();
            PRIMITIVEPARSER = new IndexedPrimitiveWorker();
        }

        @Override
        public void work() {
            if (validateAndStart()) {
                int i = 0;
                boolean isBreaker = false;
                do {
                    index:
                    {
                        route.pushArrayIndex(i);
                        isBreaker = onIndex(i++,false);
                    }
                    value:
                    {
                        call().work();
                    }
                    reset:
                    {
                        reset(isBreaker);
                    }
                } while (hasNext());
            }
            validateAndEnd();
        }


        @Override
        public void validateAndEnd() {
            onClose(false);
            route.pop();
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            boolean end = JsonMarker.isArrayEnd(dest),
                    next = JsonMarker.isNext(dest);
            if (end||next) {
                onNext(next);
                return next;
            } else {
                throw new UnexpectedTokenException(source,cursor);
            }
        }

        @Override
        public boolean validateAndStart() {
            onOpen(false);
            char dest;
            if (JsonMarker.isArrayEnd(dest = fetchIgnoreisInvisibleChar())) {
                roll();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public JsonWorker call() {
            char dest;
            if (JsonMarker.isArrayStart(dest = fetchIgnoreisInvisibleChar())) {//ignore invisible char before value
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return INDEXEDPARSER;
            } else if (JsonMarker.isObjectStart(dest)) {
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return STRUCTEDPARSER;
            } else {
                return PRIMITIVEPARSER;
            }
        }
    }

    private class PrimitiveWorker implements JsonWorker {
        @Override
        public void work() {
            tokenRead(JsonMarker.NoopGroup);
            onValue(hasWrapper,false,false);
            route.pop();
        }
    }

    private class ObjectPrimitiveWorker implements JsonWorker {
        @Override
        public void work() {
            tokenRead(JsonMarker.ObjectGroup);
            onValue(hasWrapper,false,false);
            route.pop();
        }
    }

    private class IndexedPrimitiveWorker implements JsonWorker {
        @Override
        public void work() {
            tokenRead(JsonMarker.ArrayGroup);
            onValue(hasWrapper,false,false);
            route.pop();
        }
    }

    private void onValue(boolean hasWrapper, boolean isNull, boolean isUndefined){
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
