package Dicionario;

import java.util.*;


public interface Lib {
    // TIPOS
    public static final int PALAVRA_RESERVADA =  0;
    public static final int IDENTIFICADOR     =  1;
    public static final int NUMERO            =  2;
    public static final int DIGITO            =  3;
    public static final int LETRA             =  4;
    public static final int OP_ARITMETICOS    =  5;
    public static final int OP_RELACIONAIS    =  6;
    public static final int OP_LOGICOS        =  7;
    public static final int DEL_COMENTARIOS   =  8;
    public static final int DELIMITADORES     =  9;
    public static final int CADEIA_DE_CHAR    = 10;
    public static final int CARACTERE         = 11;
    public static List<String> palavrasReservadas = new LinkedList();
    
    public static void inicializaPalavrasReservadas(){
        palavrasReservadas.add("programa");
        palavrasReservadas.add("const");
        palavrasReservadas.add("var");
        palavrasReservadas.add("funcao");
        palavrasReservadas.add("inicio");
        palavrasReservadas.add("fim");
        palavrasReservadas.add("se");
        palavrasReservadas.add("entao");
        palavrasReservadas.add("senao");
        palavrasReservadas.add("enquanto");
        palavrasReservadas.add("faca");
        palavrasReservadas.add("leia");
        palavrasReservadas.add("escreva");
        palavrasReservadas.add("inteiro");
        palavrasReservadas.add("real");
        palavrasReservadas.add("booleano");
        palavrasReservadas.add("verdadeiro");
        palavrasReservadas.add("falso");
        palavrasReservadas.add("cadeia");
        palavrasReservadas.add("caractere");
    }
}
