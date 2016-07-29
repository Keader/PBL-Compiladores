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
	private final List<Token> tokensError;
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

				//Lembrar de remover isso depois
				if (!listaDeArquivos[i].getName().endsWith(".txt") || listaDeArquivos[i].getName().contains("_Saida_"))
					continue;

				File arq = new File(listaDeArquivos[i].getName());

				//verificando se o arquivo existe para comecar a analisar
				if (arq.exists()) {
					BufferedReader leitor = new BufferedReader(new FileReader(arq));
					boolean iniciouComentario = false;
					nLinha = 0;

					//pair para verificar o estado dos comentarios
					PairComentario pIB;
					//Lendo do Arquivo
					for (String linha = leitor.readLine(); linha != null; linha = leitor.readLine()) {
						//verifica se abriu algum comentario
						pIB = analisaComentario(0, iniciouComentario, linha);
						iniciouComentario = pIB.isIniciouComentario();
						verificaRegex(pIB.getLinha());
						//atuliza contador de linha
						nLinha++;
					}
					//se nao fechou comentario
					if(iniciouComentario)
						tokensError.add(new Token("{","COMENTARIO_MAL_FORMADO", nLinha, true));

					//gerando saidas (0 para saida normal, -1 para erro)
					gerarSaida(arq.getName());
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


	private void verificaRegex(String entrada) {
		char analisar[] = entrada.toCharArray();
		String acumulador = "";

		for (int i = 0; i < analisar.length; i++) {
			acumulador += analisar[i];
			String atual = "" + analisar[i];
			Pattern patern = Pattern.compile(AuxRegex.SEPARADORES.valor);
			Matcher m = patern.matcher(atual);

			//*********************************** PEGANDO SEPARADORES ************************************
			 //Eh um separador
			if (m.matches()) {
				//Remove o separador (o ultimo elemento adicionado)
				acumulador = removeUltimoElemento(acumulador);
				if (!acumulador.equals("")){
					//Se passa no regex, cria token
					if (!verificaRegexCriandoToken(acumulador)) {
						//Se nao passa, cria token de error
						tokensError.add(new Token(acumulador, nLinha, true));
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
						if (!verificaRegexCriandoToken(palavra)) {
							//Se deu error em string
							if (m.group().equals("\""))
								tokensError.add(new Token(palavra,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha, true));
							//Outros erros (ele auto-identifica)
							else
								tokensError.add(new Token(palavra, nLinha, true));
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
							tokensError.add(new Token(palavra,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha, true));
						else
							tokensError.add(new Token(palavra, nLinha, true));

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
					}
					catch (NumberFormatException e) {
						tokensError.add(new Token(acumulador, nLinha, true));
						acumulador = "";
						continue;
					}
				}
			}
			//Eh ultima posicao
			if (isEntradaValida(acumulador) && i + 1 == analisar.length)
				verificaRegexCriandoToken(acumulador);

			else if (!isEntradaValida(acumulador)) {
				boolean precisaCompensar = false;
				if(acumulador.length() > 1) {
					acumulador = removeUltimoElemento(acumulador);
					precisaCompensar = true;
				}

				if(!verificaRegexCriandoToken(acumulador))
					tokensError.add(new Token(acumulador, nLinha, true));

				acumulador = "";
				if (precisaCompensar)
					//Ja que eu removi o ultimo elemento, decremento o contador para criar a nova sentenca
					i--;
			}
		}
	}

	private boolean isEntradaValida(String entrada) {
		for (PadraoRegex regex : PadraoRegex.values())
			if (Pattern.matches(regex.valor, entrada))
				return true;
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

	public PairComentario analisaComentario(int is, boolean iniciouComentario, String entrada){
		PairComentario pairComentario = new PairComentario(-1, iniciouComentario);
		boolean isString = false;
		char[] analisar = entrada.toCharArray();

		//se estiver esperando fechar comentario e nao existir, retorna logo
		if((!(entrada.contains("}"))) && pairComentario.isIniciouComentario()) {
			pairComentario.setLinha("");
			return pairComentario;
		}

		for (int i = 0; i < analisar.length; i++) {
			//verifica se nao abriu um comentario
			if(!pairComentario.isIniciouComentario()){
				//iniciou comentario e nao eh string
				if(analisar[i] == '{' && !isString) {
					pairComentario.setIniciouComentario(true);
					pairComentario.setColuna(i);
				}
				else {
					//comecou ou terminou string ou char
					if(analisar[i] == '\"' || analisar[i] == '\''){
						isString = !isString;
						pairComentario.setIniciouComentario(false);
					}
					pairComentario.setLinha(pairComentario.getLinha() + analisar[i]);
				}
			}
			//se abriu um comentario, verifica se fechou e nao eh uma string
			else if(analisar[i] == '}' && !isString)
				pairComentario.setIniciouComentario(false);
		}
		return pairComentario;
	}

	/***
	 * @param arquivo o nome do arquivo inicial
	 */
	private void gerarSaida(String arquivo){
		try {
			File n = new File("_Saida_" + arquivo);
			BufferedWriter bw = new BufferedWriter(new FileWriter(n));
			for(int i = 0; i < tokens.size(); i++){
				bw.write(tokens.get(i).toString());
				bw.newLine();
				bw.flush();
			}

			//se possui erros, escreve eles
			if(!tokensError.isEmpty()) {
				bw.newLine();
				bw.flush();
				for(int i = 0; i < tokensError.size(); i++){
					bw.write(tokensError.get(i).toString());
					bw.newLine();
					bw.flush();
				}
			}

			bw.close();
			//XXX isso e uma gambiarra, se nao quiser que ela exista, ao inves de usar a lista de tokens como atributo vamos por como variavel local
			tokens.clear();
			tokensError.clear();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<Token> getTokensError() {
		return tokensError;
	}
}
