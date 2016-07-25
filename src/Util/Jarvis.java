package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        CADEIA_CARACTERIES("\"[a-zA-Z]([a-zA-Z]|\\d|\\p{Blank})*?\""),
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
        SEPARADORES("\"|\\s|'|\\(|\\)"),
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
                            verificaRegex(linhaAtualizada);
                            
                        } 
                        else 
                           tipoBusca = -1;
                        
                        //atuliza contador de linha
                        nLinha++;
                    }
                    if (tipoBusca < 0)
                        tokensError.add(new TokenError("nao sei o que voce escreveu", "COMENTARIO_MAL_FORMADO", comentarioLinha));
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
     * @deprecated s� serve para analisadores char a char
     */
    public int analisadorComentarioChar(String linha, int posAchou) {
        int fim = linha.length();

        if (posAchou >= 0) {
            //pega posicacao da primeira }
            fim = linha.indexOf("}");
            //se ela estiver depois de { retorna a posicao dela
            if (fim > posAchou) {
                return fim;
            } else {
                return -1;
            }
        } else {
            return linha.indexOf("}");
        }
    }

    /**
     * *
     * o que que isso faz? ele recebe a linha e o tipo de coisa que ele ta
     * buscando ele apaga tudo que estiver como comentario
     *
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
                if (linha.contains("\"")) {
                    int inicioString = linha.indexOf('\"'), fimString = linha.length();
                    //o coment�rio come�a depois da string?
                    if (in > inicioString) {
                        //buscando pelo final da string
                        for (int i = inicioString + 1; i < linha.length(); i++) {
                            if (linha.toCharArray()[i] == '\"') {
                                fimString = i;
                                break;
                            }
                        }
                        //se o inicio do coment�rio for menor que o fim da string, retorna a posicao do fim da string
                        if (in < fimString) {
                            return analisadorComentario(linha, fimString);
                        }
                    }
                }

                if (in < 0) {
                    return linha;
                }
                if (fm > in) {
                    return analisadorComentario(apagaComentario(linha, in, fm), 0);
                } else if (fm < 0) {
                    return analisadorComentario(apagaComentario(linha, in, linha.length()), 0);
                }
            }
            //se n�o existe nenhum comentario nessa linha, so retorna
            return linha;
        } //se estiver buscando por fechar coment�rio
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
        if (posF + 1 > linha.length()) {
            sumir = linha.substring(posI, posF);
        } else {
            sumir = linha.substring(posI, posF + 1);
        }

        System.out.println(linha.replace(sumir, ""));
        return linha.replace(sumir, "");
    }

    /*Ainda falta melhorar. Sempre que uma String nao for valida, tenta quebrar em pedacos menores
    verificando caractere por caractere.Fazendo primeiro o basicao*/
    private void verificaRegex(String entrada) {
        
        char analisar[] = entrada.toCharArray();
        String acumulador = "";

        for (int i = 0; i < analisar.length; i++) {
            
            acumulador += analisar[i];
            
            String atual = ""+analisar[i];
            Pattern patern = Pattern.compile(AuxRegex.SEPARADORES.valor);
            Matcher m = patern.matcher(atual);
            //*********************************** PEGANDO SEPARADORES ************************************
            if (m.matches()) { //Eh um separador
                acumulador = removeUltimoElemento(acumulador); //Remove o separador (o ultimo elemento adicionado)
                if (!acumulador.equals("")){
                    if (!verificaRegexCriandoToken(acumulador, i)) { //Se passa no regex, cria token
                        tokensError.add(new TokenError(acumulador, nLinha, i)); //Se nao passa, cria token de error
                    }
                }
                acumulador = "";
                if (!m.group().equals(" ")){ //Se nao for um espaco
                    char separador = m.group().charAt(0);
                    int formador = buscadorDeSeparador(separador, entrada, i+1);
                    if(formador > 0){ // Se encontrou o outro separador
                        String palavra = "";
                        for (int z = i; z <= formador; z++) { //Remonta a palavra
                            palavra += analisar[z];
                        }
                        if (!verificaRegexCriandoToken(palavra, i)) { //Se passa no regex, cria token
                            if (m.group().equals("\"")) //Se deu error em string
                                tokensError.add(new TokenError(palavra,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha, i));
                            else //Outros erros (ele auto-identifica)
                                tokensError.add(new TokenError(palavra, nLinha, i));
                        }
                        i = formador;
                        continue;
                    }
                    else{ //Terminou a linha e nao achou o separador
                        String palavra = "";
                        for (int z = i; z < analisar.length; z++) { //Remonta a palavra
                            palavra += analisar[z];
                        }
                        if (m.group().equals("\"")) //Se deu error em string
                              tokensError.add(new TokenError(palavra,"CADEIA_DE_CARACTERES_MAL_FORMADA", nLinha, i));
                        else
                            tokensError.add(new TokenError(palavra, nLinha, i));
                        i = analisar.length;
                        continue;
                    }
                }
                else //Caso seja espaco, continua
                    continue;
            }
            //***************************************** FIM SEPARADORES ***********************
            
            if (Pattern.matches(AuxRegex.CASO_ESPECIAL.valor, acumulador)){ //Trata caso especial de 5.0
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
            
            if (!isEntradaValida(acumulador) && i == 0) { //Deu error no primeiro elemento
                tokensError.add(new TokenError(acumulador, nLinha, i));
                acumulador = "";
            }
            else if (isEntradaValida(acumulador) && i + 1 == analisar.length){ //Eh ultima posicao
                verificaRegexCriandoToken(acumulador, i);
            }
            else if (!isEntradaValida(acumulador)) { //Nao eh o primeiro e nao eh caso especial
                boolean precisaCompensar = false;
                if(acumulador.length() > 1)
                {
                    acumulador = removeUltimoElemento(acumulador);
                    precisaCompensar = true;
                }
                
                if(!verificaRegexCriandoToken(acumulador, i))
                    tokensError.add(new TokenError(acumulador, nLinha, i));
                
                acumulador = "";
                if (precisaCompensar)
                    i--; //Ja que eu removi o ultimo elemento, decremento o contador para criar a nova sentenca
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

    private boolean verificaRegexCriandoToken(String entrada, int pos) {
        for (PadraoRegex regex : PadraoRegex.values()) {
            if (Pattern.matches(regex.valor, entrada)) {
                tokens.add(new Token(regex.ordinal(), entrada, nLinha, pos));
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

    private String removeUltimoElemento(String original) {
        String modificada = "";
        char mod[] = original.toCharArray();
        for (int i = 0; i < mod.length - 1; i++) {
            modificada += mod[i];
        }
        return modificada;
    }
    
    private int buscadorDeSeparador(char separador, String palavra, int inicio){
        //Separador especial
        if(separador == '(')
            separador = ')';
        else if(separador == ')')
            return -1;
        
        char[] buscador = palavra.toCharArray();
        for (int i = inicio ;i < buscador.length; i++){
            if (separador == buscador[i])
                return i;
        }
        return -1;
    }
}
