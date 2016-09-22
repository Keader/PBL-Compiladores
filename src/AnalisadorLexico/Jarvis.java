package AnalisadorLexico;

import java.io.File;

import Util.Debug;

public class Jarvis {

    public static void main(String args[]){
    	//pegando os arquivos
		File dir = new File(System.getProperty("user.dir"));

		//verificando se pode pegar os arquivos
		if (!dir.exists()) {
			Debug.messagePane("Programa nao possui autorizacao para ler pastas do usuario", "Erro", Debug.ERRO);
	        System.exit(0);
		}
		//se pode pegar os arquivos, inicia a analise
		else {
	    	AnalisadorLexico lexico = new AnalisadorLexico();
	    	lexico.Executar(dir.listFiles());
		}
        System.exit(0);
    }

}
