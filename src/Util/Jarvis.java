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
        CADEIA_CARACTERIES("\"[a-zA-Z]([a-zA-Z]|\\d|\\p{Blank})*?\""),
        CARACTERE("'([a-zA-Z]|\\d)'");
        public String valor;

        private PadraoRegex(String valor) {
            this.valor = valor;
        }
    }
    
     public enum ErrorRegex {
     NUMERO_MAL_FORMADO("(^\\d+.)|(^.\\d+)");
     
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
                
                System.out.println();
                File arq = new File(listaDeArquivos[i].getName());
                
                /*Se arquivo nao existir eh porque foi deletado / movido da pasta antes de ter sido lido
                entao ignora este arquivo e passa para o proximo
                */
                if (!arq.exists())
                    continue;
                
                BufferedReader leitor = new BufferedReader(new FileReader(arq));
                
                String linha = leitor.readLine();
                String entrada[] = linha.split(" ");

                for (int x = 0;x < entrada.length; x++){
                    
                    //Comentario em progresso...
                    if (entrada[x].contains("{")){
                        String comentario[] = entrada[x].split("\\{"); //Quebra o { em 2 pedacos
                        if(comentario.length > 1){
                            
                            while(!linha.contains("}")){
                                linha = leitor.readLine();
                            }
                            
                        }
                            
                    }
                    
                    //Passa pelo automato normal
                    if (verificaRegex(entrada[x])) {
                        continue;
                    }
                        
                }
            }
            
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }

    /*Ainda falta melhorar. Sempre que uma String nao for valida, tenta quebrar em pedacos menores
    verificando caractere por caractere.Fazendo primeiro o basicao*/
    private boolean verificaRegex(String entrada) {
        //Primeira verificacao, se a palavra inteira pode virar um token
        for (PadraoRegex regex : PadraoRegex.values()) {
            if (Pattern.matches(regex.valor, entrada)) {
                tokens.add(new Token(regex.ordinal(), entrada));
                return true;
            }
        }

        /* Ainda em implementacao
        //Verifica entao se pode por partes
        char verificador[] = entrada.toCharArray();
        String acumulador = "";
        int tipo = -1;
        boolean stop = false;

        for (int i = 0; i < verificador.length; i++) {
            String verificar = "" + verificador[i];
            acumulador += verificar;
            for (PadraoRegex regex : PadraoRegex.values()) {
                if (Pattern.matches(regex.valor, acumulador)) {
                    tipo = regex.ordinal();
                }
                else if (i == 0){ //Error na primeira linha
                    tokensError.add(new Token(PadraoRegex.ERROR.ordinal(),acumulador,nLinha));
                    acumulador = "";
                    break;
                }
                else if (!acumulador.equals("") && tipo >= 0) { //Achou um erro no meio, entao cria um token com o que ja tem
                    acumulador = acumulador.substring(i-1);
                    tokens.add(new Token(tipo, acumulador));
                    acumulador = "";
                    break;
                }
            }
        }*/

        return false;
    }
   

}

