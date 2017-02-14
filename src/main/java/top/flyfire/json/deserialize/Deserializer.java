package top.flyfire.json.deserialize;

import top.flyfire.json.Parser;
import top.flyfire.json.Peeker;
import top.flyfire.json.Structure;
import top.flyfire.json.Token;
import top.flyfire.json.JsonComponent;
import top.flyfire.json.deserialize.exception.UnexpectedEndTokenException;
import top.flyfire.json.deserialize.exception.UnexpectedTokenException;

/**
 * Created by shyy_work on 2016/6/21.
 */
@SuppressWarnings("all")
public class Deserializer implements Peeker,Parser {

    private String source;

    private JsonComponent component;

    private int cursor, cursorBound, level;

    private Parser STRUCTEDPARSER;

    private Parser INDEXEDPARSER;

    private Parser PRIMITIVEPARSER;

    public Deserializer(String source, JsonComponent component) {
        this.source = source;
        this.cursor = this.level = 0;
        this.cursorBound = source.length();
        this.component = component;
        STRUCTEDPARSER = new ObjectParser();
        INDEXEDPARSER = new ArrayParser();
        PRIMITIVEPARSER = new PrimitiveParser();
    }

    @Override
    public void parse() {
        peek().parse();
    }


    @Override
    public Parser peek() {
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


    private String tokenRead(int... endToken) {
        char token = '\0';
        int quoteWrapper,
                escape = 0; //0,1
        StringBuilder builder = new StringBuilder();
        readFirst:
        {
            for (token = fetch(); !notIn(token = fetch(), ' '); roll()) ;
            if (token == '"' || token == '\'') quoteWrapper = token;
            else if(!notIn(token,endToken)) throw new UnexpectedTokenException(source,cursor);
            else quoteWrapper = 0;
        }
        read2End:
        {
            if (quoteWrapper > 0) {
                while (roll()) {
                    token = fetch();
                    if (escape == 0) {
                        if (token == '\\') {
                            escape = 1;
                        } else if ((token ^ quoteWrapper) == 0) {
                            quoteWrapper = 0;
                            break;
                        } else {
                            builder.append(token);
                        }
                    } else {
                        if (token == 't') {
                            builder.append('\t');
                        } else if (token == 'r') {
                            builder.append('\r');
                        } else if (token == 'n') {
                            builder.append('\n');
                        } else if (token == 'f') {
                            builder.append('\f');
                        } else if (token == 'b') {
                            builder.append('\b');
                        } else if (token == '/') {
                            builder.append('/');
                        } else if (token == '\\') {
                            builder.append('\\');
                        } else if (token == '"') {
                            builder.append('"');
                        } else if (token == '\'') {
                            builder.append('\'');
                        } else {
                            builder.append(token);
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
                builder.append(token);
                while (roll() && notIn(token = fetch(), endToken)) {
                    if(notSkip) {
                        if(Tokenizer.isInvisibleChar(token)){
                            notSkip = false;
                        }else {
                            builder.append(token);
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
        return builder.toString();
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

    private class ObjectParser implements Parser, Structure, Peeker {

        private Parser PRIMITIVEPARSER;

        public ObjectParser() {
            super();
            PRIMITIVEPARSER = new ObjectPrimitiveParser();
        }

        @Override
        public void parse() {
            if (validateAndStart()) {
                do {

                    key:
                    {
                        component.indexing(tokenRead(':'), level);
                        roll();
                    }

                    value:
                    {
                        peek().parse();
                    }

                } while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public void validateAndEnd() {
            component.closeObject(--level);
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            if (Tokenizer.isObjectEnd(dest)) {
                return false;
            } else if (Tokenizer.isNext(dest)) {
                component.toNext(level);
                return true;
            } else {
                throw new UnexpectedTokenException(source,cursor);
            }
        }

        @Override
        public boolean validateAndStart() {
            component.openObject(level++);
            char dest = fetchIgnoreisInvisibleChar();
            if (Tokenizer.isObjectEnd(dest)) {
                roll();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public Parser peek() {
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

    private class ArrayParser implements Parser, Structure, Peeker {

        private Parser PRIMITIVEPARSER;

        public ArrayParser() {
            super();
            PRIMITIVEPARSER = new IndexedPrimitiveParser();
        }

        @Override
        public void parse() {
            if (validateAndStart()) {
                int i = 0;
                do {
                    index:
                    {
                        component.indexing(i++, level);
                    }
                    value:
                    {
                        peek().parse();
                    }
                } while (hasNext());
            }
            validateAndEnd();
        }


        @Override
        public void validateAndEnd() {
            component.closeArray(--level);
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            if (Tokenizer.isArrayEnd(dest)) {
                return false;
            } else if (Tokenizer.isNext(dest)) {
                component.toNext(level);
                return true;
            } else {
                throw new UnexpectedTokenException(source,cursor);
            }
        }

        @Override
        public boolean validateAndStart() {
            component.openArray(level++);
            char dest;
            if (Tokenizer.isArrayEnd(dest = fetchIgnoreisInvisibleChar())) {
                roll();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public Parser peek() {
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

    private class PrimitiveParser implements Parser {
        @Override
        public void parse() {
            component.value(source, level);
        }
    }

    private class ObjectPrimitiveParser implements Parser {
        @Override
        public void parse() {
            component.value(tokenRead(Token.NEXT, Token.STC_END), level);
        }
    }

    private class IndexedPrimitiveParser implements Parser {
        @Override
        public void parse() {
            component.value(tokenRead(Token.NEXT, Token.INX_END), level);
        }
    }

}
