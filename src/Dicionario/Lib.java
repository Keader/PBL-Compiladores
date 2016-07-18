package Dicionario;


public interface Lib {
    // TIPOS
    public  final int PALAVRA_RESERVADA =  0;
    public  final int IDENTIFICADOR     =  1;
    public  final int NUMERO            =  2;
    public  final int DIGITO            =  3;
    public  final int LETRA             =  4;
    public  final int OP_ARITMETICOS    =  5;
    public  final int OP_RELACIONAIS    =  6;
    public  final int OP_LOGICOS        =  7;
    public  final int DEL_COMENTARIOS   =  8;
    public  final int DELIMITADORES     =  9;
    public  final int CADEIA_DE_CHAR    = 10;
    public  final int CARACTERE         = 11;
    public  final int ERROR             = 12;
    public  final int MAX               = 13;
    
    public enum PadraoRegex {
	PALAVRA_RESERVADA(""), //Esta usando atualmente a lista de reservados, possivelmente mudara para o Regex
	IDENTIFICADOR("[a-zA-Z]([a-zA-Z]|\\d|_)*?"),
	NUMERO("\\d+(\\.\\d+)?"),
	OP_ARITMETICO("+|-|*|/"),
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
