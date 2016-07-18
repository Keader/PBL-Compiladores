package Util;

import Dicionario.Lib;
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
public class Jarvis implements Lib {

    private final List<Token> tokens;
    private final List<Token> tokensError;

    public Jarvis() {
        tokens = new LinkedList<>();
        tokensError = new LinkedList<>();
    }
 
    /*
    Isso ainda devera ser usado em breve, para converter para string
    Deixar comentado por enquanto
    public static String conversor(int tipo) {

        switch (tipo) {
            case PALAVRA_RESERVADA:
                return "Palavra Reservada";
            case IDENTIFICADOR:
                return "Identificador";
            case NUMERO:
                return "Numero";
            case DIGITO:
                return "Digito";
            case LETRA:
                return "Letra";
            case OP_ARITMETICOS:
                return "Operador Aritmetico";
            case OP_RELACIONAIS:
                return "Operador Relacional";
            case OP_LOGICOS:
                return "Operador Logico";
            case DEL_COMENTARIOS:
                return "Delimitador de Comentarios";
            case DELIMITADORES:
                return "Delimitador";
            case CADEIA_DE_CHAR:
                return "Cadeia de Caracteries";
            case CARACTERE:
                return "Caractere";
            case ERROR:
                return "Erro";
            default:
                return "Desconhecido";
        }
    }*/
    
    public void Executar(){
        try {

            File dir = new File(System.getProperty("user.dir"));
            
            if (!dir.exists()){
               JOptionPane.showMessageDialog(null, "Programa nao possui autorizacao para ler pastas do usuario", "Error", JOptionPane.ERROR_MESSAGE);
               System.exit(-1);
            }
            
            File listaDeArquivos[] = dir.listFiles();
            
            for (int i = 0; i < listaDeArquivos.length; i++){
                
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
        return false;
    }
   
    
}
