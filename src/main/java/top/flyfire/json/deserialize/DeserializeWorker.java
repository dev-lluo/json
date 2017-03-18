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

    private JsonWorkListener markBuilder;

    private JsonRoute route;

    private JsonEventPool markPool;

    private  StringBuilder markCached;

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
        this.markBuilder = markBuilder;
        this.route = new JsonRoute();
        this.markPool = new JsonEventPool(this.route);
        this.markCached = new StringBuilder();
        STRUCTEDPARSER = new ObjectWorker();
        INDEXEDPARSER = new ArrayWorker();
        PRIMITIVEPARSER = new PrimitiveWorker();
    }

    @Override
    public void parse() {
        peek().parse();
    }


    @Override
    public JsonWorker peek() {
        char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before value
        if (Tokenizer.isArrayStart(dest)) {
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            return INDEXEDPARSER;
        } else if (Tokenizer.isObjectStart(dest)) {
            if (!roll()) throw new UnexpectedEndTokenException(this.source);
            return STRUCTEDPARSER;
        } else {
            return PRIMITIVEPARSER;
        }
    }

    private boolean notIn(int c, int... tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (c == tokens[i]) {
                return false;
            }
        }
        return true;
    }


    private boolean tokenRead(int... endToken) {
        char token = '\0';
        int quoteWrapper,
                escape = 0; //0,1
        if(null!=mark)markCached.delete(0, markCached.length());
        readFirst:
        {
            for (token = fetch(); !notIn(token = fetch(), Token.SPACE); roll()) ;
            if (token == Token.DOUBLE_QUOTE || token == Token.SINGLE_QUOTE) quoteWrapper = token;
            else if(!notIn(token,endToken)) throw new UnexpectedTokenException(source,cursor);
            else quoteWrapper = 0;
        }
        read2End:
        {
            if (hasWrapper = (quoteWrapper > 0)) {
                while (roll()) {
                    token = fetch();
                    if (escape == 0) {
                        if (token == Token.ESCAPE) {
                            escape = 1;
                        } else if ((token ^ quoteWrapper) == 0) {
                            quoteWrapper = 0;
                            break;
                        } else {
                            markCached.append(token);
                        }
                    } else {
                        if (token == 't') {
                            markCached.append('\t');
                        } else if (token == 'r') {
                            markCached.append('\r');
                        } else if (token == 'n') {
                            markCached.append('\n');
                        } else if (token == 'f') {
                            markCached.append('\f');
                        } else if (token == 'b') {
                            markCached.append('\b');
                        } else if (token == '/') {
                            markCached.append('/');
                        } else if (token == '\\') {
                            markCached.append('\\');
                        } else if (token == '"') {
                            markCached.append('"');
                        } else if (token == '\'') {
                            markCached.append('\'');
                        } else {
                            markCached.append(token);
                        }
                        escape = 0;
                    }
                }
                while (roll() && notIn(token = fetch(), endToken)) {
                    if (Tokenizer.isInvisibleChar(token))
                        continue;
                    throw new UnexpectedTokenException(source,cursor);
                }
                ;
            } else {
                boolean notSkip = true;
                markCached.append(token);
                while (roll() && notIn(token = fetch(), endToken)) {
                    if(notSkip) {
                        if(Tokenizer.isInvisibleChar(token)){
                            notSkip = false;
                        }else {
                            markCached.append(token);
                        }
                    }else{
                        if(Tokenizer.isInvisibleChar(token))
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
        while (Tokenizer.isInvisibleChar(dest = source.charAt(cursor))) {
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
        public void parse() {
            if (validateAndStart()) {
                boolean isBreaker = false;
                do {
                    key:
                    {
                        tokenRead(':');
                        route.pushObjectKey(mark);
                        isBreaker = markIndex(mark,true);
                        roll();
                    }
                    value:
                    {
                        peek().parse();
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
            markClose(true);
            route.pop();
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            boolean end = Tokenizer.isObjectEnd(dest),
            next = Tokenizer.isNext(dest);
            if(end||next){
                markNext(next);
                return next;
            }else{
                throw new UnexpectedTokenException(source,cursor);
            }
        }

        @Override
        public boolean validateAndStart() {
            markOpen(true);
            char dest = fetchIgnoreisInvisibleChar();
            if (Tokenizer.isObjectEnd(dest)) {
                roll();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public JsonWorker peek() {
            char dest;
            if (Tokenizer.isArrayStart(dest = fetchIgnoreisInvisibleChar())) {//ignore invisible char before value
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return INDEXEDPARSER;
            } else if (Tokenizer.isObjectStart(dest)) {
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
        public void parse() {
            if (validateAndStart()) {
                int i = 0;
                boolean isBreaker = false;
                do {
                    index:
                    {
                        route.pushArrayIndex(i);
                        isBreaker = markIndex(i++,false);
                    }
                    value:
                    {
                        peek().parse();
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
            markClose(false);
            route.pop();
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            boolean end = Tokenizer.isArrayEnd(dest),
                    next = Tokenizer.isNext(dest);
            if (end||next) {
                markNext(next);
                return next;
            } else {
                throw new UnexpectedTokenException(source,cursor);
            }
        }

        @Override
        public boolean validateAndStart() {
            markOpen(false);
            char dest;
            if (Tokenizer.isArrayEnd(dest = fetchIgnoreisInvisibleChar())) {
                roll();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public JsonWorker peek() {
            char dest;
            if (Tokenizer.isArrayStart(dest = fetchIgnoreisInvisibleChar())) {//ignore invisible char before value
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return INDEXEDPARSER;
            } else if (Tokenizer.isObjectStart(dest)) {
                if (!roll()) throw new UnexpectedEndTokenException(source);
                return STRUCTEDPARSER;
            } else {
                return PRIMITIVEPARSER;
            }
        }
    }

    private class PrimitiveWorker implements JsonWorker {
        @Override
        public void parse() {
            tokenRead(Token.NEXT, Token.ARRAY_CLOSE);
            markValue(false,false,false);
            route.pop();
        }
    }

    private class ObjectPrimitiveWorker implements JsonWorker {
        @Override
        public void parse() {
            tokenRead(Token.NEXT, Token.OBJECT_CLOSE);
            markValue(hasWrapper,false,false);
            route.pop();
        }
    }

    private class IndexedPrimitiveWorker implements JsonWorker {
        @Override
        public void parse() {
            tokenRead(Token.NEXT, Token.ARRAY_CLOSE);
            markValue(hasWrapper,false,false);
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
