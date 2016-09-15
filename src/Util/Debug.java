package Util;

import javax.swing.JOptionPane;

public class Debug {
	public static boolean teste = true;
		
	public static void print(Number string){
		if(teste)
			System.out.print(string);
	}

	public static void print(String string){
		if(teste)
			System.out.print(string);
	}
	
	public static void ErrPrint(Number string){
		if(teste)
			System.err.print(string);
	}

	public static void println(Number string){
		if(teste)
			print(string + "\n");
	}
	
	public static void ErrPrintln(Number string){
		if(teste)
			ErrPrint(string + "\n");
	}

	public static void println(String string){
		if(teste)
			print(string + "\n");
	}

	public static void ErrPrint(String string){
		if(teste)
			System.err.print(string);
	}

	public static void ErrPrintln(String string){
		if(teste)
			System.err.println(string);
	}
	
	public static void messagePane(String string, String titulo){
		messagePane(string, titulo, 1);
	}
	
	public static void messagePane(String string, String titulo, int tipo){
		JOptionPane.showMessageDialog(null, "string", titulo, tipo);
	}

}
