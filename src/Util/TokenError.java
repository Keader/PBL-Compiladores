
package Util;


public class TokenError {
    private String lexema;
    private String tipo;
    private int linha;
    
    public TokenError(String lexema, String tipo, int linha){
        this.lexema = lexema;
        this.tipo = tipo;
        this.linha = linha;
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
        return ""+linha+" - "+tipo+" : "+lexema;
    }
}
