package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
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
		CADEIA_CARACTERES("\"[a-zA-Z]([a-zA-Z]|\\d|\\p{Blank})*?\""),
		CARACTERE("'([a-zA-Z]|\\d)'");

		public String valor;
		private PadraoRegex(String valor) {
			this.valor = valor;
		}
	}

	public enum ErrorRegex {
		NUMERO_MAL_FORMADO("(\\.\\d+(.+)?)|(\\d+\\.(.+)?)"),
		CARACTERE_MAL_FORMADO("'\\w*'?|'\\W+'?"),
		DELIMITADOR_MAL_FORMADO("^\\((.+)?[^\\)]$|^[^\\(](.+)?[\\)]$");

		//Error na String e Comentario eh lancado sem o uso desse Enum.
		public String valor;
		private ErrorRegex(String valor) {
			this.valor = valor;
		}
	}

	public enum AuxRegex {
		SEPARADORES("\"|\\s|'"),
		CASO_ESPECIAL("\\d+\\.");

		public String valor;
		private AuxRegex(String valor) {
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
				if (listaDeArquivos[i].isDirectory()) 
					continue;

				//Lembrar de remover isso depois //XXX triplo x
				if (!listaDeArquivos[i].getName().endsWith(".txt") || listaDeArquivos[i].getName().contains("_Saida_") || listaDeArquivos[i].getName().contains("_SaidaErro_")) 
					continue;

				File arq = new File(listaDeArquivos[i].getName());

				//verificando se o arquivo existe para comecar a analisar
				if (arq.exists()) {
					BufferedReader leitor = new BufferedReader(new FileReader(arq));
					int comentarioLinha = 0, comentarioColuna = 0;
					boolean iniciouComentario = false;
					nLinha = 0;

					//pair para verificar o estado dos comentarios
					PairIntBool pIB;
					//Lendo do Arquivo
					for (String linha = leitor.readLine(); linha != null; linha = leitor.readLine()) {
						//verifica se abriu algum comentario                            
						pIB = verificaRegex(linha, iniciouComentario);
						iniciouComentario = pIB.isIniciouComentario();

						//se n�o fechou o coment�rio
						if(iniciouComentario) {
							comentarioLinha = nLinha;
							if(pIB.getColuna() >= 0)
								comentarioColuna = pIB.getColuna();
						}
						//atuliza contador de linha
						nLinha++;
					}
					//se n�o fechou coment�rio
					if(iniciouComentario) 
						tokensError.add(new TokenError("{","COMENTARIO_MAL_FORMADO", comentarioLinha, comentarioColuna));

					gerarSaida(tokens, arq.getName());
					if(tokensError.size() > 0)
						gerarSaida2(tokensError, arq.getName());

					leitor.close();
				}
				// Fim do Arquivo Atual
			}
		} 
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (NullPointerException | IOException ex) {
			ex.printStackTrace();
		} 
	}

	/*Ainda falta melhorar. Sempre que uma String nao for valida, tenta quebrar em pedacos menores
    verificando caractere por caractere.Fazendo primeiro o basicao*/
	private PairIntBool verificaRegex(String entrada, boolean iniciouComentario) {
		char analisar[] = entrada.toCharArray();
		String acumulador = "";

		//TODO inicio parte comentario
		PairIntBool pairComentario = new PairIntBool(-1, iniciouComentario);
		boolean isString = false; 
		char c;

		//se estiver esperando fechar comentario e nao existir, retorna logo
		if(!(entrada.contains("}")) && pairComentario.isIniciouComentario())
			return pairComentario;

		for (int i = 0; i < analisar.length; i++) {
			c = analisar[i];
			//verifica se nao abriu um comentario
			if(!pairComentario.isIniciouComentario()){
				//iniciou coment�rio e n�o � string
				if(c == '{' && !isString) {
					pairComentario.setIniciouComentario(true);
					pairComentario.setColuna(i);
					continue;
				}
				else {
					//come�ou ou terminou string ou char
					if(c == '\"' || c == '\''){
						isString = !isString;
						pairComentario.setIniciouComentario(false);
					}
				}
			}
			//se abriu um comentario, verifica se fechou e nao eh uma string
			else if(c == '}' && !isString) {
				pairComentario.setIniciouComentario(false);
				continue;
			}
			else
				continue;
			//TODO fim parte comentario

			acumulador += analisar[i];
			String atual = "" + analisar[i];
			Pattern patern = Pattern.compile(AuxRegex.SEPARADORES.valor);
			Matcher m = patern.matcher(atual);

			//*********************************** PEGANDO SEPARADORES ************************************
			if (m.matches()) { //Eh um separador
				//Remove o separador (o ultimo elemento adicionado)
				acumulador = removeUltimoElemento(acumulador);
				if (!acumulador.equals("")){
					//Se passa no regex, cria token
					if (!verificaRegexCriandoToken(acumulador, i)) {
						//Se nao passa, cria token de error
						tokensError.add(new TokenError(acumulador, nLinha, i)); 
					}
				}
				acumulador = "";
				//Se nao for um espaco
				if (!m.group().equals(" ")){ 
					char separador = m.group().charAt(0);
					int formador = buscadorDeSeparador(separador, entrada, i+1);
					// Se encontrou o outro separador
					if(formador > 0) {
						String palavra = "";
						for (int z = i; z <= formador; z++) {
							//Remonta a palavra
							palavra += analisar[z];
						}

						//Se passa no regex, cria token
						if (!verificaRegexCriandoToken(palavra, i)) {
							//Se deu error em string
							if (m.group().equals("\""))
								tokensError.add(new TokenError(palavra,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha, i));
							//Outros erros (ele auto-identifica)
							else 
								tokensError.add(new TokenError(palavra, nLinha, i));
						}
						i = formador;
						continue;
					}
					//Terminou a linha e nao achou o separador
					else { 
						String palavra = "";
						for (int z = i; z < analisar.length; z++) {
							//Remonta a palavra
							palavra += analisar[z];
						}
						//Se deu error em string
						if (m.group().equals("\""))
							tokensError.add(new TokenError(palavra,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha, i));
						else
							tokensError.add(new TokenError(palavra, nLinha, i));
						i = analisar.length;
						continue;
					}
				}
				//Caso seja espaco, continua
				else 
					continue;
			}
			//***************************************** FIM SEPARADORES ***********************
			//Trata caso especial de 5.0
			if (Pattern.matches(AuxRegex.CASO_ESPECIAL.valor, acumulador)){ 
				int proximo = i + 1;
				if (proximo < analisar.length){
					String prox = ""+analisar[proximo];
					try {
						int temp = Integer.parseInt(prox);
						acumulador += temp;
						i = proximo;
					} catch (NumberFormatException e) {
						tokensError.add(new TokenError(acumulador, nLinha, i));
						acumulador = "";
						continue;
					}
				}
			}
			//Eh ultima posicao
			if (isEntradaValida(acumulador) && i + 1 == analisar.length)
				verificaRegexCriandoToken(acumulador, i);
			else if (!isEntradaValida(acumulador)) {
				boolean precisaCompensar = false;
				if(acumulador.length() > 1) {
					acumulador = removeUltimoElemento(acumulador);
					precisaCompensar = true;
				}

				if(!verificaRegexCriandoToken(acumulador, i))
					tokensError.add(new TokenError(acumulador, nLinha, i));

				acumulador = "";
				if (precisaCompensar)
					//Ja que eu removi o ultimo elemento, decremento o contador para criar a nova sentenca
					i--; 
			}
		}

		return pairComentario;
	}

	private boolean isEntradaValida(String entrada) {
		for (PadraoRegex regex : PadraoRegex.values()) 
			if (Pattern.matches(regex.valor, entrada))
				return true;
		return false;
	}

	private boolean verificaRegexCriandoToken(String entrada, int pos) {
		for (PadraoRegex regex : PadraoRegex.values()) {
			if (Pattern.matches(regex.valor, entrada)) {
				tokens.add(new Token(regex.ordinal(), entrada, nLinha, pos));
				return true;
			}
		}
		return false;
	}

	public String removeUltimoElemento(String original) {
		return original.substring(0, original.length() - 1);
	}

	private int buscadorDeSeparador(char separador, String palavra, int inicio){
		char[] buscador = palavra.toCharArray();
		for (int i = inicio; i < buscador.length; i++){
			if (separador == buscador[i])
				return i;
		}
		return -1;
	}

	private void gerarSaida(List<Token> tokens, String arquivo){
		try {
			File arq = new File("_Saida_" + arquivo);
			BufferedWriter bw = new BufferedWriter(new FileWriter(arq));
			for(Token t : tokens)
				bw.write(t.toString());
			bw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		//XXX isso � uma gambiarra, se n�o quiser que ela exista, ao inves de usar a lista de tokens como atributo vamos por como variavel local
		tokens.clear();
	}
	
	private void gerarSaida2(List<TokenError> tokens, String arquivo){
		File n = new File("_SaidaErro_" + arquivo);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(n));
			for(TokenError t : tokens) 
				bw.write(t.toString());
			
			bw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		//XXX isso � uma gambiarra, se n�o quiser que ela exista, ao inves de usar a lista de tokens como atributo vamos por como variavel local
		tokens.clear();
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<TokenError> getTokensError() {
		return tokensError;
	}
}
