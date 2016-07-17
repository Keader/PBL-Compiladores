package Util;

import Dicionario.Lib;
import java.util.*;

/**
 * Classe responsavel por auxiliar o controle de tudo
 * No fim das contas eh um conroller.
 *
 */
public class Jarvis implements Lib {
    private final List<String> palavrasReservadas;
    
    public Jarvis(){
        palavrasReservadas = new LinkedList();
        inicialize();
    }
    
    private void inicialize() {
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
    
    public static String conversor(int tipo) {

        switch (tipo) {
            case PALAVRA_RESERVADA:
                return "Palavra Reservada";
            case IDENTIFICADOR:
                return "Identificador";
            case NUMERO:
                return "Numero";
            case DIGITO:
                return "Digito";
            case LETRA:
                return "Letra";
            case OP_ARITMETICOS:
                return "Operador Aritmetico";
            case OP_RELACIONAIS:
                return "Operador Relacional";
            case OP_LOGICOS:
                return "Operador Logico";
            case DEL_COMENTARIOS:
                return "Delimitador de Comentarios";
            case DELIMITADORES:
                return "Delimitador";
            case CADEIA_DE_CHAR:
                return "Cadeia de Caracteries";
            case CARACTERE:
                return "Caractere";
            case ERROR:
                return "Erro";
            default:
                return "Desconhecido";
        }
    }

        public boolean ehPalavraReservada(String palavra){
            return palavrasReservadas.contains(palavra);
        }
}
