package Util;

public class PairComentario {
	private int coluna;
	private String linha;
	private boolean iniciouComentario;
	
	public PairComentario (String linha, boolean terminou){
		this.iniciouComentario = terminou;
		this.linha = linha;
		coluna = -1;
	}
	
	public PairComentario (int coluna, boolean terminou){
		this.iniciouComentario = terminou;
		this.coluna = coluna;
		linha = "";
	}
	
	public PairComentario (int coluna, String linha, boolean terminou){
		this.iniciouComentario = terminou;
		this.coluna = coluna;
		this.linha = linha;
	}
	
	public int getColuna(){
		return coluna;
	}
	
	public String getLinha(){
		return linha;
	}
	
	public boolean isIniciouComentario(){
		return iniciouComentario;
	}
	
	public void setColuna(int coluna){
		this.coluna = coluna;
	}
	
	public void setLinha(String linha){
		this.linha = linha;
	}
	
	public void setIniciouComentario(boolean iniciouComentario){
		this.iniciouComentario = iniciouComentario;
	}
}
