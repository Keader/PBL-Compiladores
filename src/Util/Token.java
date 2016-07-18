package Util;

import java.util.Objects;

public class Token {

    private int id;
    private String lexema;

    public Token(int id, String lexema) {
        this.id = id;
        this.lexema = lexema;
    }

    public int getId() {
        return id;
    }

    public String getLexema() {
        return lexema;
    }

    /*
    Descomentar aqui quando ter certeza de que forma vai converter para nome
    @Override
    public String toString() {
        return "Lexema: " + lexema + " Tipo: " + Jarvis.conversor(id);
    }*/

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
