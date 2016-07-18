package Util;

import java.util.Objects;

public class Token {

    private int id;
    private String lexema;
    private int nLinha;
    private String nomeArquivo;

    public Token(int id, String lexema, int nLinha,String nomeArquivo) {
        this.id = id;
        this.lexema = lexema;
        this.nLinha = nLinha;
        this.nomeArquivo = nomeArquivo;
    }

    public int getId() {
        return id;
    }

    public String getLexema() {
        return lexema;
    }

    @Override
    public String toString() {
        return "Lexema: " + lexema + " Tipo: " + Jarvis.PadraoRegex.values()[id].name() + " Criado na linha: " + nLinha + 
                " Do arquivo: " + nomeArquivo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.lexema, other.lexema)) {
            return false;
        }
        return true;
    }
    
}
