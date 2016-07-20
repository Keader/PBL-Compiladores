package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	public void Executar() {
		try {

			File dir = new File(System.getProperty("user.dir"));

			if (!dir.exists()) {
				JOptionPane.showMessageDialog(null, "Programa nao possui autorizacao para ler pastas do usuario", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}

			File listaDeArquivos[] = dir.listFiles();

			//Percorre os arquivos na pasta
			for (int i = 0; i < listaDeArquivos.length; i++) {
				//Se for diretorio, passa pro proximo
				if (listaDeArquivos[i].isDirectory()) {
					continue;
				}

				//Lembrar de remover isso depois 
				if (!listaDeArquivos[i].getName().endsWith(".txt")) {
					continue;
				}

				File arq = new File(listaDeArquivos[i].getName());

				//verificando se o arquivo existe para comecar a analisar
				if (arq.exists()) {
					BufferedReader leitor = new BufferedReader(new FileReader(arq));
					nLinha = 0;
					int tipoBusca = 0, comentarioLinha = 0;
					//Lendo do Arquivo
					for (String linha = leitor.readLine(); linha != null; linha = leitor.readLine()) {
						//verifica se abriu algum comentario
						String linhaAtualizada = analisadorComentario(linha, tipoBusca);
						if (linhaAtualizada != null) {
							comentarioLinha = nLinha;
							tipoBusca = 0;
							//Tratamento de String
							//Nao deve remover espacos aqui
							if (linhaAtualizada.contains("\"")) {
								if (verificaRegex(linha))
									continue;
								else {
									tokensError.add(new TokenError(linhaAtualizada, "CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha));
									continue;
								}
							}

							String entrada[] = linhaAtualizada.split("\\s+");
							//Apos remover os espacos, mandar os possiveis tokens passar no Regex
							for (int x = 0; x < entrada.length; x++) {
								//Passa pelo Regex
								verificaRegex(entrada[x]);
							}
						}
						else
							tipoBusca = -1;
						
						//atuliza contador de linha
						nLinha++;
					}
					if(tipoBusca < 0) {
						tokensError.add(new TokenError("nao sei o que voce escreveu", "COMENTARIO_MAL_FORMADO", comentarioLinha));
					}
				} 
				// Fim do Arquivo Atual
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
	 * o que que isso faz? ele recebe a linha e a posicao inicial de "abre
	 * chave", verifica se nessa mesma linha tem "fecha chave" e retorna os
	 * valores
	 *
	 * @param linha a string que sera verificada
	 * @param posAchou a posicao inicial de "abre chave" (caso nao esteja nessa
	 * mesma linha, colocar valores inferiores a 0)
	 * @return -1 se nao tiver, ele retorna -1 significando pra pular pra
	 * proxima linha
	 * @return fim se tiver, ele retorna a posicao do primeiro "fecha chave"
	 * @deprecated só serve para analisadores char a char
	 */
	public int analisadorComentarioChar(String linha, int posAchou) {
		int fim = linha.length();

		if (posAchou >= 0) {
			//pega posicacao da primeira }
			fim = linha.indexOf("}");
			//se ela estiver depois de { retorna a posicao dela
			if (fim > posAchou) {
				return fim;
			} 
			else {
				return -1;
			}
		} 
		else {
			return linha.indexOf("}");
		}
	}

	/***
	 * o que que isso faz? ele recebe a linha e o tipo de coisa que ele ta buscando
	 * ele apaga tudo que estiver como comentario
	 * @param linha
	 * @param busca 0 para buscar inicio de comentario
	 * @param busca n > 0 para buscar inicio de comentario, comecando de n
	 * @param busca -1 para buscar final de comentario
	 * @return linha retorna a linha atualizada depois da operacao
	 */
	public String analisadorComentario(String linha, int busca) {
		int in = 0, fm = linha.length();
		//buscando inicio de comentario a partir do inicio da string ou de outra parte
		if (busca >= 0) {
			//verifica se existe algum comentario
			if (linha.contains("{")) {
				in = linha.indexOf("{", busca);
				fm = linha.indexOf("}", in);
				
				//	String aux = fm > in ? apagaComentario(linha, in, fm) : apagaComentario(linha, in, linha.length);
				if(linha.contains("\"")){
					int inicioString = linha.indexOf('\"'), fimString = linha.length();
					//o comentário começa depois da string?
					if(in > inicioString){
						//buscando pelo final da string
						for(int i = inicioString + 1; i < linha.length(); i++){
							if(linha.toCharArray()[i] == '\"'){
								fimString = i;
								break;
							}
						}
						//se o inicio do comentário for menor que o fim da string, retorna a posicao do fim da string
						if(in < fimString) 
							return analisadorComentario(linha, fimString);
					}
				}		
				
				if(in < 0)
					return linha;
				if(fm > in) 
					return analisadorComentario(apagaComentario(linha, in, fm), 0);
				else if(fm < 0) 
					return analisadorComentario(apagaComentario(linha, in, linha.length()), 0);
			}
			//se não existe nenhum comentario nessa linha, so retorna
			return linha;
		} 
		//se estiver buscando por fechar comentário
		else if (busca < 0) {
			if (linha.contains("}")) {
				fm = linha.indexOf("}");
				//continua procurando por mais comentarios na linha
				return analisadorComentario(apagaComentario(linha, 0, fm), 0);
			}
			//se nao tem nenhum fechar comentario nessa linha, pede a proxima
			return null;
		}
		return linha;
	}

	private String apagaComentario(String linha, int posI, int posF) {
		String sumir;
		if(posF + 1 > linha.length())
			sumir = linha.substring(posI, posF);
		else
			sumir = linha.substring(posI, posF + 1);
		
		System.out.println(linha.replace(sumir, ""));
		return linha.replace(sumir, "");
	}

	/*Ainda falta melhorar. Sempre que uma String nao for valida, tenta quebrar em pedacos menores
    verificando caractere por caractere.Fazendo primeiro o basicao*/
	private boolean verificaRegex(String entrada) {

		//Primeira verificacao, se a palavra inteira pode virar um token
		if (verificaRegexCriandoToken(entrada)) {
			return true;
		}

		/*A entrada completa nao passou no teste
        Verificar por partes a partir de agora
        Os erros devem ser lancados a partir desta funcao agora
		 */
		char analisar[] = entrada.toCharArray();
		String acumulador = "";
		int MAX = analisar.length - 1;
		boolean quebraLoop = false;

		for (int i = 0; i < analisar.length; i++) {

			acumulador += analisar[i];

			//Analisador de Strings
			//**************************************************************************************************************
			if (analisar[i] == '"') {
				//Pega o acumulador ateh entao, e verifica se o Regex eh valido. Se for valido cria token, se nao, gera error
				//Apos isso, comeca a analisar a String propriamente dita
				if (!acumulador.equals("")) {
					if (!verificaRegexCriandoToken(acumulador)) {
						tokensError.add(new TokenError(acumulador, nLinha));
					}
					acumulador = "";
				}

				//Comecando a analise da String
				int comeco = i;
				int fim = -1;
				for (int z = comeco; z < analisar.length; z++) { //Busca ate o fim do arquivo se encontra um fechamento de String
					if (analisar[z] == '"') { // Caso encontre o fim da string, salva a posicao dele
						fim = z + 1;
						break;
					}
				}
				if (fim >= 0) { // Caso o final tenha sido encontrado na mesma linha
					String string = "";
					for (int z = comeco; z < fim; z++) {
						string += analisar[z];
					}
					if (!verificaRegexCriandoToken(string)) { // String foi bem formada, mas nao foi aceita pelo Regex. Caso seja aceito o Token eh criado automaticamente
						tokensError.add(new TokenError(string, "CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha));
					}
					i = fim;
					continue;
				} 
				else { //Chegou no fim da linha e nao achou o " de finalizar
					String error = "";
					for (int g = i; g < analisar.length; g++) {
						error += analisar[g];
					}
					tokensError.add(new TokenError(error, "CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha));
					i = fim;
					continue;
				}
			}

			//****************************************************************************************************************************
			if (!isEntradaValida(acumulador) && i == 0) { //Deu error no rimeiro elemento
				tokensError.add(new TokenError(acumulador, nLinha));
				acumulador = "";
			} else if (!isEntradaValida(acumulador)) { //Nao eh o primeiro
				acumulador = acumulador.substring(i - 1); //Remove o ultimo elemento (significa que os que estao atras dele tecnicamente estao corretos
				verificaRegexCriandoToken(acumulador);
				acumulador = "";
			}

			//PRECISA FAZER CASO ESPECIAL DOS NUMEROS (5.5) por exemplo (Da para usar um Regex nessa situacao)
		}

		return false;
	}

	private boolean isEntradaValida(String entrada) {
		for (PadraoRegex regex : PadraoRegex.values()) {
			if (Pattern.matches(regex.valor, entrada)) {
				return true;
			}
		}
		return false;
	}

	private boolean verificaRegexCriandoToken(String entrada) {
		for (PadraoRegex regex : PadraoRegex.values()) {
			if (Pattern.matches(regex.valor, entrada)) {
				tokens.add(new Token(regex.ordinal(), entrada, nLinha));
				return true;
			}
		}
		return false;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<TokenError> getTokensError() {
		return tokensError;
	}
}