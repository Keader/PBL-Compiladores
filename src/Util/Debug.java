package Util;

import javax.swing.JOptionPane;

public class Debug {
	private static boolean exibir = true;
	public static int ERRO = 0;
	public static int PADRAO = 1;
	public static int AVISO = 2;
	
	public synchronized static void print(Object string){
		if(exibir)
			System.out.print(string.toString());
	}

	public synchronized static void print(Number string){
		if(exibir)
			System.out.print(string);
	}

	public synchronized static void ErrPrint(Number string){
		if(exibir)
			System.err.print(string);
	}

	public synchronized static void ErrPrint(Object string){
		if(exibir)
			System.err.print(string.toString());
	}

	public synchronized static void println(Number string){
		if(exibir)
			System.out.println(string);
	}

	public synchronized static void ErrPrintln(Number string){
		if(exibir)
			System.err.println(string);
	}

	public synchronized static void println(Object string){
		if(exibir)
			System.out.println(string);
	}

	public synchronized static void ErrPrintln(Object string){
		if(exibir)
			System.err.println(string.toString());
	}

	public synchronized static void messagePane(String mensagem, String titulo){
		if(exibir)
			messagePane(mensagem, titulo, 1);
	}

	public synchronized static void messagePane(String mensagem, String titulo, int tipo){
		if(exibir)
			JOptionPane.showMessageDialog(null, mensagem, titulo, tipo);
	}

	public synchronized static void setExibir(boolean exibir){
		Debug.exibir = exibir;
	}	
}
