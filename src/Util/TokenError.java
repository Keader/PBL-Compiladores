
package Util;

import Util.Jarvis.ErrorRegex;
import java.util.regex.Pattern;


public class TokenError {
    private String lexema;
    private String tipo;
    private int linha;
    private int coluna;
    
    public TokenError(String lexema, int linha, int coluna){
        this.lexema = lexema;
        this.linha = linha;
        tipo = "";
        autoDetectarError(lexema);
        this.coluna = coluna;
    }
    
    public TokenError(String lexema,String tipo, int linha, int coluna){
        this.lexema = lexema;
        this.linha = linha;
        this.tipo = tipo;
        this.coluna = coluna;
    }
    
    public TokenError(String lexema,String tipo, int linha){
        this.lexema = lexema;
        this.linha = linha;
        this.tipo = tipo;
        coluna = -1;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public int getLinha() {
        return linha;
    }
    
    @Override
    public String toString(){
        if (coluna == -1)
            return "* " + lexema + " * " + tipo + " :" + linha;
        return "* " + lexema + " * " + tipo + " :" + linha + ":" + coluna;
    }
    
    private void autoDetectarError(String error){
        for(ErrorRegex regex:ErrorRegex.values()){
            if(Pattern.matches(regex.valor, error)){
                tipo += regex.name();
                return;
            }
        }
        tipo+= "SIMBOLO_DESCONHECIDO";
    }
}
