package Util;

public class PairIntBool {
	private int coluna;
	private boolean iniciouComentario;
	
	public PairIntBool (int coluna, boolean terminou){
		this.iniciouComentario = terminou;
		this.coluna = coluna;
	}
	
	public int getColuna(){
		return coluna;
	}
	
	public boolean isIniciouComentario(){
		return iniciouComentario;
	}
	
	public void setColuna(int coluna){
		this.coluna = coluna;
	}
	
	public void setIniciouComentario(boolean iniciouComentario){
		this.iniciouComentario = iniciouComentario;
	}
}
