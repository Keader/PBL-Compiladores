package Util;

import static Util.Dicionario.conversorIdString;
import java.util.ArrayList;
import java.util.List;

public class Simbolo implements Dicionario{
	private String id;
	private int tipo;
	private List<Token> valor;
    private int dimensoes;
    private boolean ehFuncao;
    private boolean ehConstante;
    private boolean ehMatriz;
    private List<Simbolo> parametros;

	public Simbolo(String id, int tipo, List<Token> valor){
		this.id = id;
		this.tipo = tipo;
		this.valor = valor;
        dimensoes = 0;
        ehFuncao = false;
        ehConstante = false;
        ehMatriz = false;
        parametros = new ArrayList<>();
	}

    public Simbolo(String id, int tipo, Token valor){
		this.id = id;
		this.tipo = tipo;
        dimensoes = 0;
        ehFuncao = false;
        ehConstante = false;
        ehMatriz = false;
        parametros = new ArrayList<>();
        this.valor = new ArrayList<>();
        this.valor.add(valor);
	}

	public int getTipo() {
		return tipo;
	}

	public List<Token> getValor() {
		return valor;
	}

	public String getId() {
		return id;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setValor(List<Token> valor) {
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

    public boolean ehConstante() {
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
        String valores = "";
        for (Token t: valor)
            valores += t.getLexema() + " ";
        
        if (ehMatriz){
            return  "["+ conversorIdString(tipo) +"]  [D:" +dimensoes+"] " + id + " = " + valores;
        }
		return "["+ conversorIdString(tipo) +"] " + id + " = " + valores;
	}

}
