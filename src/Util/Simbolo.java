package Util;

import static Util.Dicionario.conversorIdString;
import java.util.ArrayList;
import java.util.List;

public class Simbolo implements Dicionario{
	private String id;
	private int tipo;
	private String valor;
    private int escopo;
    private List<Integer> dimensoes;
    private boolean ehFuncao;


	public Simbolo(String id, int tipo, String valor){
		this.id = id;
		this.tipo = tipo;
		this.valor = valor;
        escopo = ESCOPO_GLOBAL;
        dimensoes = new ArrayList<>();
	}

    public Simbolo(String id, int tipo, String valor, int escopo){
		this.id = id;
		this.tipo = tipo;
		this.valor = valor;
        this.escopo = escopo;
        dimensoes = new ArrayList<>();
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

    public int getEscopo() {
        return escopo;
    }

    public List<Integer> getDimensoes() {
        return dimensoes;
    }

    public boolean ehFuncao() {
        return ehFuncao;
    }

    public void setEhFuncao(boolean ehFuncao) {
        this.ehFuncao = ehFuncao;
    }

	public String toString(){
		return "["+ conversorIdString(tipo) +"] " + id + " = " + valor;
	}

}
