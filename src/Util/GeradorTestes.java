package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeradorTestes {
	public static void geraTestes(){
		int qtdPal = 10000;
		File n = new File("teste_cabuloso_qtd" + qtdPal + ".txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(n));

			String c = "abcefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\'\"";
			String[] operadores = {"+ ", "- ", "* ", "/ "};
			String[] variaveis = {"teste ", "sei ", "la ", "qual ", "foi ", "xablau ", "nao ", "sei ", "mesmo ", "so ", "pra ", "testar "};
			String[] reservadas = {"programa ", "const ", "var ", "funcao ", "inicio ", "fim ", "se ", "entao ", " senao", "enquanto ", "faca ", "leia ", "escreva ", "inteiro ",
					"real ", "booleano ", "verdadeiro ", "falso ", "cadeia ", "caractere "};

			Random sorteio = new Random();
			int sortNum;
			String texto = "";

			for(int i = 0; i < qtdPal; i++){
				sortNum = sorteio.nextInt(6);
				if(sortNum == 0) {
					sortNum = sorteio.nextInt(variaveis.length);
					texto = variaveis[sortNum];
				}
				else if(sortNum == 1){
					sortNum = sorteio.nextInt(reservadas.length);
					texto = reservadas[sortNum];
				}
				else if(sortNum == 2){
					texto = i + " ";
				}
				else if(sortNum == 3){
					sortNum = sorteio.nextInt(operadores.length);
					texto = operadores[sortNum];
				}
				else if(sortNum == 4){
					sortNum = sorteio.nextInt(c.length());
					texto = "\'" + c.charAt(sortNum) + "\' ";
				}
				else if(sortNum == 5){
					sortNum = sorteio.nextInt(variaveis.length);
					texto = "\"" + variaveis[sortNum] + "\" ";
				}

				bw.write(texto);
				if(i % 14 == 0 && i > 0)
					bw.newLine();
				bw.flush();
			}
			bw.close();
		}
		catch (FileNotFoundException e) {}
		catch (IOException e) {}
	}
}
