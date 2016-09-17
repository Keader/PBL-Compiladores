package Util;

import javax.swing.JOptionPane;

public class Debug {
	public static boolean exibir = true;
	public static int ERRO = 0;
	public static int PADRAO = 1;
	public static int AVISO = 2;

	public static void print(Object string){
		if(exibir)
			System.out.print(string.toString());
	}

	public static void print(Number string){
		if(exibir)
			System.out.print(string);
	}

	public static void ErrPrint(Number string){
		if(exibir)
			System.err.print(string);
	}

	public static void ErrPrint(Object string){
		if(exibir)
			System.err.print(string.toString());
	}

	public static void println(Number string){
		if(exibir)
			System.out.println(string);
	}

	public static void ErrPrintln(Number string){
		if(exibir)
			System.err.println(string);
	}

	public static void println(Object string){
		if(exibir)
			System.out.println(string);
	}

	public static void ErrPrintln(Object string){
		if(exibir)
			System.err.println(string.toString());
	}

	public static void messagePane(String mensagem, String titulo){
		messagePane(mensagem, titulo, 1);
	}

	public static void messagePane(String mensagem, String titulo, int tipo){
		JOptionPane.showMessageDialog(null, mensagem, titulo, tipo);
	}

}
