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
public class AnalisadorSemantico implements Dicionario{

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
    private Hashtable<String, Simbolo> tabelaGlobal;
    private boolean funcoesAlcancadas;
    private List<ErroSemantico> erros;

    public AnalisadorSemantico(List<Token> lTokens){
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
        erros = new ArrayList<>();
        tabelaGlobal = null;
        funcoesAlcancadas = false;
        montarTabela();
    }

    private synchronized void montarTabela(){

        for (cont = 0; cont < tokens.size(); cont++) {
            t = tokens.get(cont);

            if (t.getIdUnico() == TK_CONST) {
                //Se as funcoes nao foram alcansadas ainda, significa que existe escopo global
                if (!funcoesAlcancadas)
                    tabelaGlobal = tabela;

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
                //Se as funcoes nao foram alcansadas ainda, significa que existe escopo global
                if (!funcoesAlcancadas)
                    tabelaGlobal = tabela;

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
                //Quando funcoes sao alcancadas, seta a flag pra true, para criacao do contexto global
                if (!funcoesAlcancadas){
                    funcoesAlcancadas = true;

                    //Acredito que os tipos de funcoes devem sempre ficar num escopo global, entao tecnicamente...
                    //Sempre havera um escopo global, pelo menos com as funcoes
                    if (tabelaGlobal == null)
                        tabelaGlobal = tabela;
                }

                cont++;
                t = tokens.get(cont);

                if (t.getIdUnico() == TK_INICIO)
                    criaNovoEscopo(true);
            }

            else if (t.getIdUnico() == TK_FUNCAO){
                //Quando funcoes sao alcancadas, seta a flag pra true, para criacao do contexto global
                if (!funcoesAlcancadas){
                    funcoesAlcancadas = true;

                    //Acredito que os tipos de funcoes devem sempre ficar num escopo global, entao tecnicamente...
                    //Sempre havera um escopo global, pelo menos com as funcoes
                    if (tabelaGlobal == null)
                        tabelaGlobal = tabela;
                }

                valor = "";
                tipoAtual = 0;
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

                    t = tokens.get(cont);

                    //Pega o identificador do parametro
                    String acumulador ="";
                    while (!checaTerminadoresParametros()){
                        acumulador+= t.getLexema();
                        cont++;
                        t = tokens.get(cont);
                    }
                    idParametros.add(acumulador);
                    acumulador = "";
                    if (t.getIdUnico() == TK_VIRGULA)
                        cont++;
                }

                //Cria o simbolo da funcao, adicionando na tabela global !
                Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor,tabelaGlobal);
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
                 criaNovoEscopo(true);

                 //Pelo que eu havia discutido com Seara, sempre que receber parametros em uma funcao
                 //As variaveis vindas do parametro devem estar no escopo da funcao.
                 for (Simbolo sim : simbolo.getParametros())
                     criaSimbolo(sim.getId(), sim.getTipo(), sim.getValor());

                //Continua a analise normal de agora em diante
            }


        }
        //Salva o ultimo escopo (salva o escopo da main)
        criaNovoEscopo(false);
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

    private Simbolo criaSimbolo(String id, int tipo, String valor, Hashtable<String, Simbolo> tabelaE){
        Simbolo simbolo = new Simbolo(id, tipo, valor);

        if(!tabelaE.contains(id)){
            tabelaE.put(id,simbolo);
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

    /**
     * Metodo adiciona o escopo atual na tabela de escopos, e cria um novo.
     * @param novaTabela define se eh necessario criar um novo, ou apenas salvar o escopo atual.
     */
    private void criaNovoEscopo(boolean novaTabela) {
        if (!tabelas.contains(tabela))
            tabelas.add(tabela);
        else
            System.err.println("A tabela do escopo " +(tabelas.size()-1)+" ja existe");

        //Criando nova tabela de simbolos
        if (novaTabela)
            tabela = new Hashtable<>();
        else
            tabela = null;

        System.out.println("Escopo: "+tabelas.size());
    }

    public void iniciarAnalise(){

    }

}
