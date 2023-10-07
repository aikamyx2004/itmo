package md2html.markup;

import java.util.function.Function;

public class Triple {
    private final String code, wrongCode, htmlCode;
    private final Function<String,? extends Token> function;

    public Triple(String code, String wrongCode, String htmlCode, Function<String,? extends Token> classOfToken) {
        this.code = code;
        this.wrongCode = wrongCode;
        this.htmlCode = htmlCode;
        this.function = classOfToken;
    }

    public String getCode() {
        return code;
    }

    public Function<String, ? extends Token> getFunction() {
        return function;
    }

    public String getWrongCode() {
        return wrongCode;
    }
}
