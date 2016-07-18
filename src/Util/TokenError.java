
package Util;

import Util.Jarvis.ErrorRegex;
import java.util.regex.Pattern;


public class TokenError {
    private String lexema;
    private String tipo;
    private int linha;
    
    public TokenError(String lexema, int linha){
        this.lexema = lexema;
        this.linha = linha;
        tipo = "";
        autoDetectarError(lexema);
    }
    
    public TokenError(String lexema,String tipo, int linha){
        this.lexema = lexema;
        this.linha = linha;
        this.tipo = tipo;
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
        return linha + " - " + tipo + " : " + lexema;
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
