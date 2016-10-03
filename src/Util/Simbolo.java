package Util;

public class Simbolo {
	private int tipo;
	private String valor;
	
	public Simbolo(int tipo, String valor){
		this.tipo = tipo;
		this.valor = valor;
	}

	public int getTipo() {
		return tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String toString(){
		return "["+tipo+"]" + " : " + valor;
	}
}
