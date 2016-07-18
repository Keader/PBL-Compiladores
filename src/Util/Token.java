package Util;

import Util.Jarvis.PadraoRegex;

public class Token {

	private int id;
	private int nLinha;

	private String lexema;
	private String classe;

	public Token(int id, String lexema, int nLinha) {
		this.id = id;
		this.lexema = lexema;
		this.nLinha = nLinha;
	}

	public int getId() {
		return id;
	}

	public String getLexema() {
		return lexema;
	}

	public String getClasse() {
		if(classe == null)
			classe = PadraoRegex.values()[id].name();
		return classe;
	}

	@Override
	public String toString() {
		return "Lexema: " + lexema + " Tipo: " + Jarvis.PadraoRegex.values()[id].name() + " Criado na linha: " + nLinha;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Token) {
			Token aux = (Token) obj;
			if(aux.getId() == this.id && aux.getLexema().equals(this.lexema))
				return true;
		}

		return false;
	}
}
