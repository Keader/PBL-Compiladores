package AnalisadorSemantico;

import Util.Dicionario;
import static Util.Dicionario.*;
import Util.Simbolo;
import Util.Token;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Bradley
 */
public class TabelaDeSimbolos implements Dicionario{

    private int tipoAtual;
    private String identificador;
    private String valor;
    private List<Token> tokens;
    private List<Hashtable<String, Simbolo>> tabelas;
    private Hashtable<String, Simbolo> tabela;
    private int cont;
    private Token t;
    private int dimensoes;
    private List<Integer> tipoParametros;
    private List<String> idParametros;

    public TabelaDeSimbolos(List<Token> lTokens){
        tokens = new ArrayList<>();
        tokens.addAll(lTokens);
        tabelas = new ArrayList<>();
        tabela = new Hashtable<>();
        cont = 0;
        identificador = "";
        valor = "";
        tipoAtual = 0;
        dimensoes = 0;
        tipoParametros = new ArrayList<>();
        idParametros = new ArrayList<>();
    }

    public synchronized void montarTabela(){

        for (cont = 0; cont < tokens.size(); cont++) {
            t = tokens.get(cont);

            if (t.getIdUnico() == TK_CONST) {

                //Pula o const e o inicio
                cont += 2;
                t = tokens.get(cont);

                while (t.getIdUnico() != TK_FIM) {

                    //Pega o tipo
                    t = tokens.get(cont);
                    tipoAtual = t.getIdUnico();
                    cont++;

                   //Metodo que pega o identificador e valor
                   percorreCriandoSimboloConst();

                    if (t.getIdUnico() == TK_VIRGULA) {
                        while (t.getIdUnico() != TK_PONTOVIRGULA) {
                            cont++;
                            percorreCriandoSimboloConst();
                            cont++;
                        }
                        //Atualiza o token para pegar apos o ponto e virgula
                        t = tokens.get(cont);
                    }
                    //Significa que agora eh um novo tipo
                    else if (t.getIdUnico() == TK_PONTOVIRGULA) {
                        tipoAtual = 0;
                        cont++;
                    }
                }
            }
            else if (t.getIdUnico() == TK_VAR){
                //Pula o var e o inicio
                cont += 2;
                t = tokens.get(cont);

                while (t.getIdUnico() != TK_FIM){

                   //Pega o tipo
                    t = tokens.get(cont);
                    tipoAtual = t.getIdUnico();
                    cont++;

                    //Metodo pega o identificador e as dimensoes (matrizes/vetores)
                    percorreCriandoSimboloVar();

                     if (t.getIdUnico() == TK_VIRGULA) {
                        while (t.getIdUnico() != TK_PONTOVIRGULA) {
                            cont++;
                            t = tokens.get(cont);
                            percorreCriandoSimboloVar();
                        }
                        //Atualiza o token para pegar apos o ponto e virgula
                        cont++;
                        t = tokens.get(cont);
                    }
                    //Significa que agora eh um novo tipo
                    else if (t.getIdUnico() == TK_PONTOVIRGULA) {
                        tipoAtual = 0;
                        cont++;
                        //Atualiza o token para pegar apos o ponto e virgula
                        t = tokens.get(cont);
                    }

                }
            }

            else if (t.getIdUnico() == TK_PROGRAMA){
                cont++;
                t = tokens.get(cont);

                if (t.getIdUnico() == TK_INICIO){
                    criaNovoEscopo();
                    cont++;
                    t = tokens.get(cont);
                }
            }

            else if (t.getIdUnico() == TK_FUNCAO){
                cont++;
                t = tokens.get(cont);

                //Se a funcao tem um retorno (tipo de retorno)
                if (ehTipo()){
                    tipoAtual = t.getIdUnico();
                    cont++;
                    t = tokens.get(cont);
                }

                //Pega identificador
                identificador = t.getLexema();
                cont++;
                t = tokens.get(cont);

                //Pula o abre parentese
                cont++;
                t = tokens.get(cont);

                while (t.getIdUnico() != TK_PARENTESE_F){
                    //Pega o tipo do parametro
                    t = tokens.get(cont);
                    tipoParametros.add(t.getIdUnico());
                    cont++;

                    //Pega o identificador do parametro
                    String acumulador ="";
                    while (!checaTerminadoresParametros()){
                        t = tokens.get(cont);
                        acumulador+= t.getLexema();
                        cont++;
                    }
                    idParametros.add(acumulador);
                    acumulador = "";
                    if (t.getIdUnico() == TK_VIRGULA)
                        cont++;
                }

                //Cria o simbolo da funcao
                Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor);
                simbolo.setEhFuncao(true);
                //Adiciona a lista de parametros
                for (int i =0; i<idParametros.size();i++){
                    String id = idParametros.get(i);
                    int tipo = tipoParametros.get(i);
                    Simbolo s = new Simbolo(id, tipo, valor);
                    simbolo.getParametros().add(s);
                }
                //Saindo do fecha parentese
                cont++;
                t = tokens.get(cont);

                 //o token agora eh inicio
                 criaNovoEscopo();

                cont++;
                t = tokens.get(cont);
                //Continua a analise normal de agora em diante
            }

        }
    }

    private Simbolo criaSimbolo(String id, int tipo, String valor){
        Simbolo simbolo = new Simbolo(id, tipo, valor);

        if(!tabela.contains(id)){
            tabela.put(id,simbolo);
             System.out.println(simbolo);
        }
        else
            System.err.println("O Token de identificador: "+id+" ja foi definido");

        return simbolo;
    }

    private void percorreCriandoSimboloConst() {
        valor = "";
        identificador = "";
        //Pega o identificador
        t = tokens.get(cont);
        identificador = t.getLexema();
        cont++;

        //Pula o igual
        cont++;
        t = tokens.get(cont);

        //Percorre o valor ate achar virgula ou ponto e virgula
        while (checaTerminadores()) {
            valor += t.getLexema();
            cont++;
            t = tokens.get(cont);
        }

        Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor);
        simbolo.setEhConstante(true);
    }

    private void percorreCriandoSimboloVar(){
        identificador = "";
        //Apenas garantindo que sempre nao tera valor
        valor = "";

        t = tokens.get(cont);

        //Eh vetor/matriz
        if (t.getIdUnico() == TK_MENOR){
            //Pula o menor atual e o proximo
            cont+=2;
            t = tokens.get(cont);

            while (t.getIdUnico() != TK_MAIOR){
                //Ignora a virgula, para nao contar nas dimensoes
                if (t.getIdUnico() != TK_VIRGULA)
                    dimensoes++;

                cont++;
                t = tokens.get(cont);
            }
            //Pula o maior atual e o proximo
            cont+=2;
        }

        //Pega o identificador
        t = tokens.get(cont);
        identificador = t.getLexema();
        cont++;

        //atualiza o tokenAtual para a virgula ou ponto e virgula.
         t = tokens.get(cont);

        Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor);

        if (dimensoes > 0){
            simbolo.setEhMatriz(true);
            simbolo.setDimensoes(dimensoes);
            dimensoes = 0;
        }

    }

    private boolean ehTipo(){
        return t.getIdUnico() == TK_INTEIRO || t.getIdUnico() == TK_REAL || t.getIdUnico() == TK_CADEIA || t.getIdUnico() == TK_CARACTERE || t.getIdUnico() == TK_BOOLEANO;
    }

    private boolean checaTerminadores(){
        if (t.getIdUnico() == TK_VIRGULA || t.getIdUnico() == TK_PONTOVIRGULA)
        return false;

        return true;
    }

    private boolean checaTerminadoresParametros(){
        return t.getIdUnico() == TK_VIRGULA || t.getIdUnico() == TK_PARENTESE_F;
    }

    private void criaNovoEscopo() {
        if (!tabelas.contains(tabela))
            tabelas.add(tabela);
        else
            System.err.println("A tabela do escopo " +(tabelas.size()-1)+" ja existe");
        //criando uma nova tabela de simbolos do escopo
        tabela = new Hashtable<>();
        System.out.println("Escopo: "+tabelas.size());
    }

}
