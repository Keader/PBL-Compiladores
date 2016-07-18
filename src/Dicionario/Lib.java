package Dicionario;

public interface Lib {

    public enum PadraoRegex {
        PALAVRA_RESERVADA("programa|const|var|funcao|inicio|fim|se|entao|senao|enquanto|faca|leia|escreva|inteiro|real|booleano|verdadeiro|falso|cadeia|caractere"), //Esta usando atualmente a lista de reservados, possivelmente mudara para o Regex
        IDENTIFICADOR("[a-zA-Z]([a-zA-Z]|\\d|_)*?"),
        NUMERO("\\d+(\\.\\d+)?"),
        OP_ARITMETICO("\\+|\\-|\\*|\\/"),
        OP_RELACIONAL("(<>)|=|<|(<=)|>|(>=)"),
        OP_LOGICO("nao|e|ou"),
        DELIMITADOR(";|.|\\(|\\)"),
        CADEIA_CARACTERIES("\"[a-zA-Z]([a-zA-Z]|\\d|\\p{Blank})*?\""),
        CARACTERE("'([a-zA-Z]|\\d)'");
        public String valor;

        private PadraoRegex(String valor) {
            this.valor = valor;
        }
    }

}
