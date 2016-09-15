package Util;

public class PairComentario {
	private String linha;
	private boolean iniciouComentario;

	public PairComentario (String linha, boolean terminou){
		this.iniciouComentario = terminou;
		this.linha = linha;
	}

	public PairComentario (boolean terminou){
		this.iniciouComentario = terminou;
		linha = "";
	}

	public String getLinha(){
		return linha;
	}

	public boolean isIniciouComentario(){
		return iniciouComentario;
	}

	public void setLinha(String linha){
		this.linha = linha;
	}

	public void setIniciouComentario(boolean iniciouComentario){
		this.iniciouComentario = iniciouComentario;
	}
}
