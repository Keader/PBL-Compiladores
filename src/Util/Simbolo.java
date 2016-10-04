package Util;

import static Util.Dicionario.conversorIdString;
import java.util.ArrayList;
import java.util.List;

public class Simbolo implements Dicionario{
	private String id;
	private int tipo;
	private String valor;
    private int dimensoes;
    private boolean ehFuncao;
    private boolean ehConstante;
    private boolean ehMatriz;
    private List<Simbolo> parametros;

	public Simbolo(String id, int tipo, String valor){
		this.id = id;
		this.tipo = tipo;
		this.valor = valor;
        dimensoes = 0;
        ehFuncao = false;
        ehConstante = false;
        ehMatriz = false;
        parametros = new ArrayList<>();
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

    public int getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(int dimensoes) {
        this.dimensoes = dimensoes;
    }

    public boolean ehFuncao() {
        return ehFuncao;
    }

    public void setEhFuncao(boolean ehFuncao) {
        this.ehFuncao = ehFuncao;
    }

    public boolean isEhConstante() {
        return ehConstante;
    }

    public void setEhConstante(boolean ehConstante) {
        this.ehConstante = ehConstante;
    }

    public boolean ehMatriz() {
        return ehMatriz;
    }

    public void setEhMatriz(boolean ehMatriz) {
        this.ehMatriz = ehMatriz;
    }

    public List<Simbolo> getParametros() {
        return parametros;
    }
    
	public String toString(){
        if (ehMatriz){
            return  "["+ conversorIdString(tipo) +"]  [D:" +dimensoes+"] " + id + " = " + valor;
        }
		return "["+ conversorIdString(tipo) +"] " + id + " = " + valor;
	}

}
