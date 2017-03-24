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

    private char wrapperToken;

    private boolean breakOff;

    private int cursor, cursorBound, level;

    private JsonWorker objectWorker;

    private JsonWorker arrayWoker;

    private JsonWorker primitiveWorker;

    private JsonWorker primitiveWithQuoteWorker;

    public DeserializeWorker(String source, JsonWorkListener markBuilder) {
        this.source = source;
        this.cursor = this.level = 0;
        this.cursorBound = source.length();
        this.workListener = markBuilder;
        this.route = new JsonRoute();
        this.eventPool = new JsonEventPool(this.route);
        this.markCached = new CharCached();
        objectWorker = new ObjectWorker();
        arrayWoker = new ArrayWorker();
        primitiveWorker = new PrimitiveWorker();
        primitiveWithQuoteWorker = new PrimitiveWithQuoteWorker();
    }

    @Override
    public void work() {
        call().work();
    }


    @Override
    public JsonWorker call() {
        char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before value
        if(JsonMarker.isQuote(dest)){
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            wrapperToken = dest;
            return primitiveWithQuoteWorker;
        }else if (JsonMarker.isArrayStart(dest)) {
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            return arrayWoker;
        } else if (JsonMarker.isObjectStart(dest)) {
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            return objectWorker;
        } else {
            return primitiveWorker;
        }
    }


    private void tokenReadWithQuote(MarkGroup endGroup){
        char token;
        int begin = cursor,end = cursor;
        while (roll()){
            token = fetch();
            if (token == JsonMark.ESCAPE) {
                roll();
                markCached.clear();
                markCached.getCharsFrom(source,begin,end-begin);
                markCached.push(JsonMarker.escape(fetch()));
                __tokenReadWithEscape(endGroup);
                return;
            } else if (token == wrapperToken) {
                end = cursor;
                wrapperToken = 0;
                mark = source.substring(begin,end);
                while (roll()) {
                    if(endGroup.exists(token = fetch())){
                        break;
                    }else if (JsonMarker.isInvisibleChar(token)){
                        continue;
                    }else{
                        throw new UnexpectedTokenException(source,cursor);
                    }
                }
                return;
            }
        }
        throw new UnexpectedEndTokenException(source);
    }

    private void __tokenReadWithEscape(MarkGroup endGroup){
        char token;
        while (roll()) {
            token = fetch();
            if (token == JsonMark.ESCAPE) {
                roll();
                markCached.push(JsonMarker.escape(fetch()));
            } else if (token == wrapperToken) {
                wrapperToken = 0;
                mark = markCached.toString();
                while (roll()) {
                    if(endGroup.exists(token = fetch())){
                        break;
                    }else if (JsonMarker.isInvisibleChar(token)){
                        continue;
                    }else{
                        throw new UnexpectedTokenException(source,cursor);
                    }
                }
                return;
            } else {
                markCached.push(token);
            }
        }
        throw new UnexpectedEndTokenException(source);
    }

    private void tokenRead(MarkGroup endGroup) {
        char token = fetch();
        if(null!=mark)markCached.clear();
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
        mark = markCached.toString();
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

    private class ObjectWorker implements JsonWorker, Enumeration {

        private JsonWorker PRIMITIVEPARSER;

        public ObjectWorker() {
            super();
        }

        @Override
        public void work() {
            if (validateAndStart()) {
                boolean isBreaker = false;
                do {
                    key:
                    {
                        wrapperToken = fetchIgnoreisInvisibleChar();
                        if(JsonMarker.isQuote(wrapperToken)) {
                            if (!roll()) throw new UnexpectedEndTokenException(source);
                            tokenReadWithQuote(ObjectK2VGroup);
                        }else{
                            tokenRead(ObjectK2VGroup);
                        }
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

    }

    private class ArrayWorker implements JsonWorker, Enumeration {

        private JsonWorker PRIMITIVEPARSER;

        public ArrayWorker() {
            super();
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
    }

    private class PrimitiveWorker implements JsonWorker {
        @Override
        public void work() {
            tokenRead(markGroups[route.getToken()]);
            onValue(false,false,false);
            route.pop();
        }
    }

    private class PrimitiveWithQuoteWorker implements JsonWorker {

        @Override
        public void work() {
            tokenReadWithQuote(markGroups[route.getToken()]);
            onValue(true,false,false);
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

    interface MarkGroup {
        boolean exists(char in);
    }

    public final static MarkGroup ObjectGroup = new MarkGroup() {
        @Override
        public final boolean exists(char in) {
            return JsonMarker.isNext(in)||JsonMarker.isObjectEnd(in);
        }
    },ObjectK2VGroup = new MarkGroup() {
        @Override
        public final boolean exists(char in) {
            return JsonMarker.isPrp2Val(in);
        }
    },ArrayGroup = new MarkGroup() {
        @Override
        public final boolean exists(char in) {
            return JsonMarker.isNext(in)||JsonMarker.isArrayEnd(in);
        }
    },NoopGroup = new MarkGroup() {
        @Override
        public final boolean exists(char in) {
            return false;
        }
    };

    private final static MarkGroup[] markGroups = new MarkGroup[]{
      NoopGroup,ObjectGroup,ArrayGroup
    };

}
