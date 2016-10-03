package Util;

public class Simbolo {
	private String id;
	private int tipo;
	private String valor;
	
	public Simbolo(String id, int tipo, String valor){
		this.id = id;
		this.tipo = tipo;
		this.valor = valor;
	}

	public int getTipo() {
		return tipo;
	}

	public String getValor() {
		return valor;
	}

	public String getId() {
		return id;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public void setId(String id) {
		this.id= id;
	}
	
	public String toString(){
		return "["+ Dicionario.conversorIdString(tipo) +"] " + id + " = " + valor;
	}
}
