package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * Classe responsavel por auxiliar o controle de tudo No fim das contas eh um
 * conroller.
 *
 */
public class Jarvis {
	private final List<Token> tokens;
	private final List<TokenError> tokensError;
	private int nLinha;

	public Jarvis() {
		tokens = new LinkedList<>();
		tokensError = new LinkedList<>();
		nLinha = 0;
	}

	public enum PadraoRegex {
		PALAVRA_RESERVADA("programa|const|var|funcao|inicio|fim|se|entao|senao|enquanto|faca|leia|escreva|inteiro|real|booleano|verdadeiro|falso|cadeia|caractere"), //Esta usando atualmente a lista de reservados, possivelmente mudara para o Regex
		IDENTIFICADOR("[a-zA-Z]([a-zA-Z]|\\d|_)*?"),
		NUMERO("\\d+(\\.\\d+)?"),
		OP_ARITMETICO("\\+|\\-|\\*|\\/"),
		OP_RELACIONAL("(<>)|=|<|(<=)|>|(>=)"),
		OP_LOGICO("nao|e|ou"),
		DELIMITADOR(";|,|\\(|\\)"),
		CADEIA_CARACTERIES("\"[a-zA-Z]([a-zA-Z]|\\d|\\p{Blank})*?\""),
		CARACTERE("'([a-zA-Z]|\\d)'");
		public String valor;

		private PadraoRegex(String valor) {
			this.valor = valor;
		}
	}

	public enum ErrorRegex {
		NUMERO_MAL_FORMADO("(\\.\\d+(.+)?)|(\\d+\\.(.+)?)"),
		CARACTERE_MAL_FORMADO("'\\w*'?|'\\W+'?");

		//Error na String e Comentario eh lancado sem o uso desse Enum.
		public String valor;
		private ErrorRegex(String valor) {
			this.valor = valor;
		}
	}

	public void Executar(){
		try {

			File dir = new File(System.getProperty("user.dir"));

			if (!dir.exists()){
				JOptionPane.showMessageDialog(null, "Programa nao possui autorizacao para ler pastas do usuario", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}

			File listaDeArquivos[] = dir.listFiles();

			//Percorre os arquivos na pasta
			for (int i = 0; i < listaDeArquivos.length; i++){
				//Se for diretorio, passa pro proximo
				if (listaDeArquivos[i].isDirectory())
					continue;

				//Lembrar de remover isso depois //KEADER - isso aqui é mesmo necessário? acho melhor retirar, porque ela pode testar com qualquer arquivo
				if(!listaDeArquivos[i].getName().endsWith(".txt"))
					continue;

				File arq = new File(listaDeArquivos[i].getName());

				//verificando se o arquivo existe para começar a analisar
				if (arq.exists()){                
					BufferedReader leitor = new BufferedReader(new FileReader(arq));
					nLinha = 0;

					//Lendo do Arquivo
					for (String linha = leitor.readLine(); linha != null; linha = leitor.readLine()) {
						nLinha++;

						//Precisa tratar comentario AQUI
						analisadorComentario();
						
						//Tratamento de String
						//Nao deve remover espacos aqui
						if (linha.contains("\"")) {
							if (verificaRegex(linha, listaDeArquivos[i].getName())) {
								continue;
							}
							else{
								tokensError.add(new TokenError(linha,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha));
								continue;
							}
						}

						String entrada[] = linha.split("\\s+");
						//Apos remover os espacos, mandar os possiveis tokens passar no Regex
						for (int x = 0; x < entrada.length; x++) {
							//Passa pelo Regex
							verificaRegex(entrada[x], listaDeArquivos[i].getName());
						}
					}
				} // Fim do Arquivo Atual
			}

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}


	}
	
	//TODO implementar
	private void analisadorComentario(){
		
	}

	/*Ainda falta melhorar. Sempre que uma String nao for valida, tenta quebrar em pedacos menores
    verificando caractere por caractere.Fazendo primeiro o basicao*/
	private boolean verificaRegex(String entrada, String nomeArquivo) {
		//Primeira verificacao, se a palavra inteira pode virar um token
		for (PadraoRegex regex : PadraoRegex.values()) {
			if (Pattern.matches(regex.valor, entrada)) {
				tokens.add(new Token(regex.ordinal(), entrada, nLinha));
				return true;
			}
		}

		/*A entrada completa nao passou no teste
        Verificar por partes a partir de agora
        Os erros devem ser lancados a partir desta funcao agora
		 */


		return false;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<TokenError> getTokensError() {
		return tokensError;
	}
}

