package AnalisadorLexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import AnalisadorSintatico.AnalisadorSintatico;
import Util.Debug;
import Util.Dicionario;
import Util.PairComentario;
import Util.Token;

/**
 * Analisador Lexico. Funcao: Possui a funcao de separar todo o arquivo lido em
 * TOKENS com lexemas e IDs que serao utilizados no Analisador Sintatico.
 *
 * @see AnalisadorSintatico
 * @see Token
 */
public class AnalisadorLexico implements Dicionario {

    private final String pastaSaida = "saida_lexica";
    private final List<Token> tokens;
    private final List<Token> tokensError;
    private int nLinha;

    public AnalisadorLexico() {
        tokens = new LinkedList<>();
        tokensError = new LinkedList<>();
        nLinha = 1;
    }

    public void Executar(File[] arquivos) {
        try {
            File listaDeArquivos[] = arquivos;

            //Percorre os arquivos na pasta
            for (int i = 0; i < listaDeArquivos.length; i++) {
                //Se for diretorio ou um tipo de arquivo ignorado, passa pro proximo
                if (listaDeArquivos[i].isDirectory() || arquivosIgnorados(listaDeArquivos[i].getName())) {
                    continue;
                }

                //verificando se o arquivo existe para comecar a analisar
                if (listaDeArquivos[i].exists()) {
                    BufferedReader leitor = new BufferedReader(new FileReader(listaDeArquivos[i]));
                    boolean iniciouComentario = false;
                    nLinha = 1;

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
                    //se nao fechou comentario gera token de erro de comentario
                    if (iniciouComentario) {
                        tokensError.add(new Token("{comentario", "COMENTARIO_MAL_FORMADO", nLinha, true));
                    }

                    //gerando saidas
                    gerarSaida(listaDeArquivos[i].getName());
                    leitor.close();
                }
                // Fim do Arquivo Atual
            }
        } catch (FileNotFoundException ex) {
            Debug.messagePane("Arquivo nao encontrado", "Erro", Debug.ERRO);
        } catch (NullPointerException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean arquivosIgnorados(String arquivo) {
        List<String> comeco = new ArrayList<String>();
        List<String> fim = new ArrayList<String>();
        //adicionando os arquivos que serao ignorados baseado no inicio
        comeco.add("s_");

        //adicionando os arquivos que serao ignorados baseado no fim
        fim.add(".jar");
        fim.add(".xlsx");
        fim.add(".classpath");
        fim.add(".project");
        fim.add(".md");
        fim.add(".gitignore");
        fim.add(".csv");

        for (String s : comeco) {
            if (arquivo.startsWith(s)) {
                return true;
            }
        }

        for (String s : fim) {
            if (arquivo.endsWith(s)) {
                return true;
            }
        }
        return false;
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
                if (!acumulador.equals("")) {
                    //Se passa no regex, cria token
                    if (!verificaRegexCriandoToken(acumulador)) {
                        //Se nao passa, cria token de error
                        tokensError.add(new Token(acumulador, nLinha, true));
                    }
                }
                acumulador = "";
                //Se nao for um espaco
                if (!m.group().matches("\\s+")) {
                    char separador = m.group().charAt(0);
                    int formador = buscadorDeSeparador(separador, entrada, i + 1);
                    // Se encontrou o outro separador
                    if (formador > 0) {
                        String palavra = "";
                        for (int z = i; z <= formador; z++) {
                            //Remonta a palavra
                            palavra += analisar[z];
                        }
                        //Se passa no regex, cria token
                        if (!verificaRegexCriandoToken(palavra)) {
                            //Se deu error em string
                            if (m.group().equals("\"")) {
                                tokensError.add(new Token(palavra, "CADEIA_MAL_FORMADA", nLinha, true));
                            } //Outros erros (ele auto-identifica)
                            else {
                                tokensError.add(new Token(palavra, nLinha, true));
                            }
                        }
                        i = formador;
                        continue;
                    } //Terminou a linha e nao achou o separador
                    else {
                        String palavra = "";
                        for (int z = i; z < analisar.length; z++) {
                            //Remonta a palavra
                            palavra += analisar[z];
                        }
                        //Se deu error em string
                        if (m.group().equals("\"")) {
                            tokensError.add(new Token(palavra, "CADEIA_MAL_FORMADA", nLinha, true));
                        } else {
                            tokensError.add(new Token(palavra, nLinha, true));
                        }

                        i = analisar.length;
                        continue;
                    }
                } //Caso seja espaco, continua
                else {
                    continue;
                }
            }
            //***************************************** FIM SEPARADORES ***********************
            //Trata caso especial de 5.0
            if (Pattern.matches(AuxRegex.CASO_ESPECIAL.valor, acumulador)) {
                int proximo = i + 1;
                if (proximo < analisar.length) {
                    String prox = "" + analisar[proximo];
                    try {
                        int temp = Integer.parseInt(prox);
                        acumulador += temp;
                        i = proximo;
                    } catch (NumberFormatException e) {
                        tokensError.add(new Token(acumulador, nLinha, true));
                        acumulador = "";
                        continue;
                    }
                }
            }
            //Eh ultima posicao
            if (isEntradaValida(acumulador) && i + 1 == analisar.length) {
                verificaRegexCriandoToken(acumulador);
            } else if (!isEntradaValida(acumulador)) {
                boolean precisaCompensar = false;
                if (acumulador.length() > 1) {
                    acumulador = removeUltimoElemento(acumulador);
                    precisaCompensar = true;
                }

                if (!verificaRegexCriandoToken(acumulador)) {
                    tokensError.add(new Token(acumulador, nLinha, true));
                }

                acumulador = "";
                if (precisaCompensar) //Ja que eu removi o ultimo elemento, decremento o contador para criar a nova sentenca
                {
                    i--;
                }
            }
        }
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
            Pattern patern = Pattern.compile(regex.valor);
            Matcher m = patern.matcher(entrada);
            if (m.matches()) {
                int grupo = 0;
                for (int i = 1; i <= m.groupCount(); i++) {
                    if (m.group(i) != null) {
                        grupo = i;
                        break;
                    }
                }
                tokens.add(new Token(regex.ordinal(), entrada, nLinha, grupo));
                return true;
            }
        }
        return false;
    }

    public String removeUltimoElemento(String original) {
        return original.substring(0, original.length() - 1);
    }

    private int buscadorDeSeparador(char separador, String palavra, int inicio) {
        char[] buscador = palavra.toCharArray();
        for (int i = inicio; i < buscador.length; i++) {
            if (separador == buscador[i]) {
                return i;
            }
        }
        return -1;
    }

    public PairComentario analisaComentario(int is, boolean iniciouComentario, String entrada) {
        PairComentario pairComentario = new PairComentario(iniciouComentario);
        boolean isString = false, isChar = false;
        char[] analisar = entrada.toCharArray();

        //se estiver esperando fechar comentario e nao existir, retorna logo
        if ((!(entrada.contains("}"))) && pairComentario.isIniciouComentario()) {
            pairComentario.setLinha("");
            return pairComentario;
        }

        for (int i = 0; i < analisar.length; i++) {
            //verifica se nao abriu um comentario
            if (!pairComentario.isIniciouComentario()) {
                //iniciou comentario e nao eh string
                if (analisar[i] == '{' && !isString && !isChar) {
                    pairComentario.setIniciouComentario(true);
                } else {
                    //comecou uma string
                    if (analisar[i] == '\"') {
                        isString = !isString;
                        isChar = false;
                        pairComentario.setIniciouComentario(false);
                    } //comecou um char
                    else if (analisar[i] == '\'') {
                        isChar = !isChar;
                        isString = false;
                        pairComentario.setIniciouComentario(false);
                    }
                    pairComentario.setLinha(pairComentario.getLinha() + analisar[i]);
                }
            } //se abriu um comentario, verifica se fechou e nao eh uma string
            else if (analisar[i] == '}' && !isString && !isChar) {
                pairComentario.setIniciouComentario(false);
            }
        }
        return pairComentario;
    }

    /**
     * *
     * Escreve em um arquivo de texto os tokens obtidos na Analise Lexica
     *
     * @param arquivo nome do arquivo que foi analisado
     */
    private void gerarSaida(String arquivo) {
        try {
            File pasta = new File(pastaSaida);
            pasta.mkdir();
            File n = new File(pasta.getName() + "//s_" + arquivo);
            BufferedWriter bw = new BufferedWriter(new FileWriter(n));
            for (Token t : tokens) {
                bw.write(t.toString());
                bw.newLine();
                bw.flush();
            }
            //se possui erros, escreve eles
            if (!tokensError.isEmpty()) {
                bw.newLine();
                bw.flush();
                for (Token t : tokensError) {
                    bw.write(t.toString());
                    bw.newLine();
                    bw.flush();
                }
            } else if (!tokens.isEmpty()) {
                //Se nao ha erros lexicos, iniciar analise sintatica
                Debug.println("[Log] Analise Lexica para o arquivo: [" + arquivo + "] aprovada.");
                Debug.println("[Log] Iniciando Analise Sintatica para o arquivo: [" + arquivo + "]");
                //dando inicio a thread do Sintatico
                new AnalisadorSintatico(tokens, arquivo).run();
            } else {
                Debug.messagePane("[*] O arquivo: [" + arquivo + "] nao gerou nenhum Token. Pulando analise sintatica.", "Aviso", Debug.AVISO);
            }

            bw.close();
            tokens.clear();
            tokensError.clear();
        } catch (IOException e) {
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
