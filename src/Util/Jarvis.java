package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
					int abriuComentario = 0;
				
					//Lendo do Arquivo
					for (String linha = leitor.readLine(); linha != null; linha = leitor.readLine()) {
						nLinha++;
						//verifica se abriu algum comentário
						abriuComentario = analisadorComentario(linha, abriuComentario);
						//se 0, pode seguir normalmente
						//se 1, pula pra proxima linha
						if(abriuComentario == 0){
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

	/**
	 * o que que isso faz?
	 * ele recebe a linha e a posicao inicial de "abre chave", verifica se nessa mesma linha tem "fecha chave" e retorna os valores
	 * 
	 * @param linha a string que sera verificada
	 * @param posAchou a posicao inicial de "abre chave" (caso nao esteja nessa mesma linha, colocar valores inferiores a 0)
	 * @return -1 se nao tiver, ele retorna -1 significando pra pular pra proxima linha
	 * @return fim se tiver, ele retorna a posicao do primeiro "fecha chave"
	 */
	public int analisadorComentarioChar(String linha, int posAchou){
		int fim = linha.length();
		
		if(posAchou >= 0){
			//pega posicacao da primeira }
			fim = linha.indexOf("}");
			//se ela estiver depois de { retorna a posicao dela
			if(fim > posAchou)
				return fim;
			else
				return -1;
		}
		else {
			return linha.indexOf("}");
		}
	}

	//TODO implementar
	public int analisadorComentario(String linha, int busca){
		int in = 0, fm = linha.length();
		String aux = null;

		//buscando inicio de comentário
		if(busca == 0){
			//verifica se existe algum comentário
			if(linha.contains("{")){
				in = linha.indexOf("{");
				fm = linha.indexOf("}");
				//	String aux = fm > in ? apagaComentario(linha, in, fm) : apagaComentario(linha, in, linha.length);
				if(fm > in) {
					aux = apagaComentario(linha, in, fm);
					return analisadorComentario(aux, 0);
				}
				else {
					aux = apagaComentario(linha, in, linha.length());
					return -1;
				}
			}
			return 0;
		}
		else if(busca == 1){
			if(linha.contains("}")){
				fm = linha.indexOf("}");
				aux = apagaComentario(linha, 0, fm);
				//continua procurando por mais comentários na linha
				return analisadorComentario(aux, 0);
			}
			return -1;
		}
		return 0;
	}

	private String apagaComentario(String linha, int posI, int posF){
		String sumir = linha.substring(posI, posF + 1);
		System.out.println(linha.replace(sumir, ""));
		return linha.replace(sumir, "");
	}
	
	//TODO da uma olhada nisso
	private int verificadorPorChar(String linha){
		char[] auxLinha = linha.toCharArray();
		List<String> auxPalavras = new ArrayList<String>();
		
		for (int i = 0; i < linha.length(); i++){
			if(auxLinha[i] == '{') {
				int t = analisadorComentarioChar(linha, i);
				//retorna um aviso pra pular a linha caso não tenha achado o final do comentário
				if(t < 0)
					return -1;
				//se achou o final do comentário pula o que estiver dentro
				else if (t > i)
					i = t;
			}
			else {
				//concatena o ultimo objeto formado com o novo char
				String t = auxPalavras.get(auxPalavras.size()) + auxLinha[i];
		
				//TODO Keader, olha isso aqui, pode descomentar, mas tem que fazer o "VerificadorRegex(t)" funfar
				//verifica se passa em algum dos regex (so precisa passar em 1 deles pra continuar, pois o objetivo é achar o ultimo que funciona)
				//if(VerificadorRegex(t))
				//	auxPalavras.add(t);
				//se não passar acaba o for e olha o ultimo que passou
				//else
				//	break;
			}
		}
		
		String lexema = auxPalavras.get(auxPalavras.size());
		//agora eh so olhar a que classe ele pertence
		//if(VerificadorRegex(t))

		auxPalavras.clear();
		return 0;
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

