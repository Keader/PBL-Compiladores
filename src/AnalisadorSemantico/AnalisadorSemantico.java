package AnalisadorSemantico;

import Util.Dicionario;
import static Util.Dicionario.*;
import Util.Simbolo;
import Util.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private List<Token> valor;
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
    private boolean retorno;
    private String funcaoAtual;
    private int contExp;
    private final String pastaSaida = "saida_semantica";
    private String arquivo;

    public AnalisadorSemantico(List<Token> lTokens, String arquivo){
        tokens = new ArrayList<>();
        tokens.addAll(lTokens);
        tabelas = new ArrayList<>();
        tabela = new Hashtable<>();
        cont = 0;
        identificador = "";
        valor = new ArrayList<>();
        tipoAtual = 0;
        dimensoes = 0;
        tipoParametros = new ArrayList<>();
        idParametros = new ArrayList<>();
        erros = new ArrayList<>();
        tabelaGlobal = null;
        funcoesAlcancadas = false;
        montarTabela();
        retorno = false;
        funcaoAtual = "";
        contExp = 0;
        this.arquivo = arquivo;
    }

    private synchronized void montarTabela(){

        for (cont = 0; cont < tokens.size(); cont++) {
            t = tokens.get(cont);

            if (t.getIdUnico() == TK_CONST) {
                //Se as funcoes nao foram alcansadas ainda, significa que existe escopo global
                if (!funcoesAlcancadas)
                    tabelaGlobal = tabela;
                else //Nao verifica corpo
                    continue;

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
                        t = tokens.get(cont);
                    }
                }
            }

            else if (t.getIdUnico() == TK_VAR){
                //Se as funcoes nao foram alcancadas ainda, significa que existe escopo global
                if (!funcoesAlcancadas)
                    tabelaGlobal = tabela;
                else //Nao verifica corpo
                    continue;

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

                valor = new ArrayList<>();
                tipoAtual = TIPO_VAZIO;
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
                Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor,tabelaGlobal, t.getnLinha());
                simbolo.setEhFuncao(true);
                //Adiciona a lista de parametros
                for (int i =0; i<idParametros.size();i++) {
                    String id = idParametros.get(i);
                    int tipo = tipoParametros.get(i);
                    Simbolo s = new Simbolo(id, tipo, valor);
                    simbolo.getParametros().add(s);
                    if (id.contains("<<"))
                        s.setEhMatriz(true);
                }
                idParametros.clear();
                tipoParametros.clear();
                //Saindo do fecha parentese
                cont++;
                t = tokens.get(cont);

                 //o token agora eh inicio
                 criaNovoEscopo(true);

                 //Pelo que eu havia discutido com Seara, sempre que receber parametros em uma funcao
                 //As variaveis vindas do parametro devem estar no escopo da funcao.
                 for (Simbolo sim : simbolo.getParametros())
                     criaSimbolo(sim.getId(), sim.getTipo(), sim.getValor(), t.getnLinha());

                //Continua a analise normal de agora em diante
            }
        }
        //Salva o ultimo escopo (salva o escopo da main)
        //Condicao para quando so existe o escopo main :)
        if (tabelas.size() > 1)
            criaNovoEscopo(false);
    }

    public void iniciarAnalise() {
        int escopoAtual = 0;
        tabela = tabelas.get(escopoAtual);
        funcoesAlcancadas = false;

        for (cont = 0; cont < tokens.size(); cont++) {

            t = tokens.get(cont);
             //Identifica variaveis dentro de funcoes
             if (t.getIdUnico() == TK_VAR){
                //Se nao veio funcoes, significa que eh variavel global, essas ja foram adicionadas na tabela.
                if (!funcoesAlcancadas){
                    while (t.getIdUnico() != TK_FIM){
                        cont++;
                        t = tokens.get(cont);
                    }
                    continue;
                }

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
            //Atualiza a tabela para o escopo atual e pula os parametros
            else if (t.getIdUnico() == TK_FUNCAO || t.getIdUnico() == TK_PROGRAMA){
                funcoesAlcancadas = true;
                boolean idAlcancado = false;
                //Ignora tipo e parametros (ja foram adicionados)
                while (t.getIdUnico() != TK_INICIO){
                    if (t.getIdUnico() == TK_ID && !idAlcancado){
                        idAlcancado = true;
                        funcaoAtual = t.getLexema();
                    }
                    cont++;
                    t = tokens.get(cont);
                }
                escopoAtual++;
                if (escopoAtual < tabelas.size())
                    tabela = tabelas.get(escopoAtual);
            }

            //Analise de Corpo
            else {
                identificador = "";

                //Atribuicao de vetor
                if(t.getIdUnico() == TK_MENOR)
                    verificaMatriz();

                //Comeca com identificador, logo eh chamada de funcao ou atribuicao.
                if (t.getIdUnico() == TK_ID){
                    //Evita re-analisar constantes e variaveis.
                    if (!funcoesAlcancadas) {
                        while (t.getIdUnico() != TK_FIM) {
                            cont++;
                            t = tokens.get(cont);
                        }
                        continue;
                    }
                    identificador = t.getLexema();
                    int tipo = 0;
                    //Pula o identificador
                    cont++;
                    t = tokens.get(cont);

                    //Verifica funcoes sem capturar retorno.
                    //Se o proximo Token eh parentese, significa que eh funcao obrigatoriamente...
                    //Pois para ser uma expressao relacional ou matematica, precisaria de um operador antes.
                    if (t.getIdUnico() == TK_PARENTESE_A) {
                        //Saindo da verificaFuncao, sempre o Token estara em fecha parentese.
                        //E neste caso, sempre apos o fecha parentese vira um ponto e virgula.
                        verificaFuncao(identificador);

                        //Pula o fecha parentese
                        cont++;
                        t = tokens.get(cont);
                    }

                    Simbolo variavel = tabela.get(identificador);

                    //Variavel/Const nao existe no escopo local
                    if (variavel == null) {
                        //Verifica no escopo global
                        variavel = tabelaGlobal.get(identificador);
                        if (variavel == null) {
                            ErroSemantico erro = new ErroSemantico(identificador, VAR_NAO_DECL, t.getnLinha());
                            if (!erros.contains(erro))
                                erros.add(erro);
                            //Ignora tudo ateh o ponto e virgula
                            while (t.getIdUnico() != TK_PONTOVIRGULA) {
                                cont++;
                                t = tokens.get(cont);
                            }
                            continue;
                        }
                    }
                    //Se chegou neste ponto, variavel existe.
                    if (variavel.ehConstante()) {
                        ErroSemantico erro = new ErroSemantico(identificador, ATRIBUICAO_INVALIDA_CONST, t.getnLinha());
                        if (!erros.contains(erro))
                            erros.add(erro);
                    }

                    tipo = variavel.getTipo();

                    //Eh Atribuicao
                    if (t.getIdUnico() == TK_IGUAL){
                        //Pula o igual
                        cont++;
                        t = tokens.get(cont);

                        //Transforma tudo que vem depois em uma "expressao"
                        List<Token> expressao = new ArrayList<>();

                        while (t.getIdUnico() != TK_PONTOVIRGULA){
                            expressao.add(t);
                            cont++;
                            t = tokens.get(cont);
                        }

                        contExp = 0;
                        int retorno = verificaExpressoes(expressao);
                        if (retorno != tipo) {
                            if (!(tipo == TK_REAL && retorno == TK_INTEIRO)) {
                                String exp = "";
                                for (Token tk : expressao)
                                    exp = exp + tk.getLexema() + " ";

                                ErroSemantico erro = new ErroSemantico(exp, TIPOS_INCOMPATIVEIS, t.getnLinha());
                                if (!erros.contains(erro)) {
                                    erros.add(erro);
                                }
                            }
                        }
                    }
                }

                //Se comeca com Igual, eh retorno
                else if (t.getIdUnico() == TK_IGUAL){
                    //Pula o igual e o maior
                    cont+=2;
                    t = tokens.get(cont);

                    //Pega tudo que tem entre os 2 =>
                    List<Token> expressao = new ArrayList<>();
                    Token aux = tokens.get(cont);
                    int contador = cont;
                    while (aux.getIdUnico() != TK_IGUAL){
                        expressao.add(aux);
                        contador++;
                        aux = tokens.get(contador);
                        if (aux.getIdUnico() == TK_IGUAL && contador+1 < tokens.size()
                            && tokens.get(contador+1).getIdUnico() != TK_MAIOR){
                            expressao.add(aux);
                            contador++;
                            aux = tokens.get(contador);
                        }
                    }

                    //Lidar com matrizes
                    if (t.getIdUnico() == TK_MENOR)
                        verificaMatriz();

                    Token temp = t;
                    //Verifico rapido o proximo elemento para ver se eh funcao
                    cont++;
                    t = tokens.get(cont);

                    if (temp.getIdUnico() == TK_ID && t.getIdUnico() == TK_PARENTESE_A){
                        verificaFuncao(temp.getLexema());
                        cont++;
                    }

                    //Se o atual eh igual a parentese ou o proximo eh um operador (diferente de igual), significa que eh expressao.
                    else if (temp.getIdUnico() == TK_SUBTRACAO || temp.getIdUnico() == TK_PARENTESE_A || (ehOperador(t.getIdUnico())) && t.getIdUnico() != TK_IGUAL || temp.getIdUnico() == TK_NAO){
                        contExp = 0;
                        int tipo = verificaExpressoes(expressao);
                        Simbolo tabelado = tabelaGlobal.get(funcaoAtual);
                        if (tabelado.getTipo() != tipo) {
                            if (!(tabelado.getTipo() == TK_REAL && tipo == TK_INTEIRO)) {
                                String exp = "";
                                for (Token tk : expressao)
                                    exp = exp + tk.getLexema() + " ";

                                String retorno = "A expressao: " + exp + ", retornou o tipo: " + conversorIdString(tipo) + ", no retorno da funcao: " + funcaoAtual;
                                erros.add(new ErroSemantico(retorno, TIPO_ATRIBUICAO_INVALIDA, t.getnLinha()));
                                cont = contador;
                                //Pula o =>
                                cont += 2;
                                t = tokens.get(cont);
                                continue;
                            }
                        }
                        else{
                            cont = contador;
                            //Pula o =>
                            cont+=2;
                            t = tokens.get(cont);
                            continue;
                        }
                    }

                    t = temp;

                    Simbolo variavel = tabela.get(t.getLexema());
                    //Teoricamente, tabelado nunca sera null.
                    Simbolo tabelado = tabelaGlobal.get(funcaoAtual);

                    //Cuida de tipos padroes, caracteres, inteiros, etc.
                    if (ehLiteral(t.getIdUnico()))
                        variavel = new Simbolo(t.getLexema(), converteTipo(t), t);

                    //Variavel/Const nao existe no escopo local
                    if (variavel == null) {
                        //Verifica no escopo global
                        variavel = tabelaGlobal.get(t.getLexema());
                        if (variavel == null)
                            erros.add(new ErroSemantico(t.getLexema(), VAR_NAO_DECL, t.getnLinha()));
                    }

                    if (variavel != null && variavel.ehMatriz() && variavel.getDimensoes() != dimensoes){
                        ErroSemantico erro = new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha());
                        if (!erros.contains(erro))
                            erros.add(erro);
                    }
                    if (variavel != null && variavel.getTipo() != tabelado.getTipo()) {
                        if (variavel != null && !(tabelado.getTipo() == TK_REAL && variavel.getTipo() == TK_INTEIRO))
                            erros.add(new ErroSemantico(t.getLexema(), RETORNO_INVALIDO, t.getnLinha()));
                    }
                    //Pula o =>
                    cont+=2;
                    t = tokens.get(cont);
                }

                else if (t.getIdUnico() == TK_LEIA){
                    //Pula o leia e o abre parentese
                    cont+=2;
                    t = tokens.get(cont);
                    while (t.getIdUnico() != TK_PARENTESE_F) {

                        if (t.getIdUnico() == TK_VIRGULA){
                            cont++;
                            t = tokens.get(cont);
                            continue;
                        }

                        //Lidar com matrizes
                        if (t.getIdUnico() == TK_MENOR)
                            verificaMatriz();

                        Simbolo variavel = tabela.get(t.getLexema());

                        //Variavel/Const nao existe no escopo local
                        if (variavel == null) {
                            //Verifica no escopo global
                            variavel = tabelaGlobal.get(t.getLexema());
                            if (variavel == null) {
                                erros.add(new ErroSemantico(t.getLexema(), VAR_NAO_DECL, t.getnLinha()));
                            }
                        }

                        if (variavel != null && variavel.ehMatriz() && variavel.getDimensoes() != dimensoes){
                            ErroSemantico erro = new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha());
                            if (!erros.contains(erro))
                                erros.add(erro);
                        }

                        else if (variavel != null && variavel.ehConstante())
                            erros.add(new ErroSemantico (variavel.getId(), ATRIBUICAO_INVALIDA_CONST, t.getnLinha()));

                        cont++;
                        t = tokens.get(cont);
                    }
                }
                else if (t.getIdUnico() == TK_SE){
                    //Pula o se e o abre parentese
                    cont+=2;
                    t = tokens.get(cont);
                    List<Token> expressao = new ArrayList<>();

                    while(!quebraCapturaExpressao(TK_ENTAO)){
                        expressao.add(t);
                        cont++;
                        t = tokens.get(cont);
                    }

                    contExp = 0;
                    int tipo = verificaExpressoes(expressao);
                    if (tipo != TK_BOOLEANO){
                        String resultado = "";
                        for (Token tk: expressao)
                            resultado = resultado + tk.getLexema() + " ";
                        ErroSemantico erro = new ErroSemantico(resultado, TIPOS_INCOMPATIVEIS, t.getnLinha());
                        if (!erros.contains(erro))
                            erros.add(erro);
                    }
                    //Pula o fecha parentese
                    cont++;
                    t.getLexema();
                }
                else if (t.getIdUnico() == TK_ENQUANTO){
                     //Pula o enquanto e o abre parentese
                    cont+=2;
                    t = tokens.get(cont);
                    List<Token> expressao = new ArrayList<>();

                    while(!quebraCapturaExpressao(TK_FACA)){
                        expressao.add(t);
                        cont++;
                        t = tokens.get(cont);
                    }

                    contExp = 0;
                    int tipo = verificaExpressoes(expressao);
                    if (tipo != TK_BOOLEANO){
                        String resultado = "";
                        for (Token tk: expressao)
                            resultado = resultado + tk.getLexema() + " ";
                        ErroSemantico erro = new ErroSemantico(resultado, TIPOS_INCOMPATIVEIS, t.getnLinha());
                        if (!erros.contains(erro))
                            erros.add(erro);
                    }
                    //Pula o fecha parentese
                    cont++;
                    t.getLexema();
                }
                else if (t.getIdUnico() == TK_ESCREVA){
                    //Pula o escreva e o abre parentese
                    cont+=2;
                    t = tokens.get(cont);
                    List<Token> expressao = new ArrayList<>();

                    while(!quebraCapturaExpressao(TK_PONTOVIRGULA)){
                        expressao.add(t);
                        cont++;
                        t = tokens.get(cont);
                    }

                    /*
                    Explicando a gambiarra envolvendo aqui.
                    no escreva, ele pode ter infinitos valores, separados por virgula.
                    Entao eh preciso quebrar a expressao por virgula.
                    Porem para quebrar por virgula, daria ruim em casos de matrizes.
                    Por isso, crio uma lista e separo em menores...
                    */
                    List<List<Token>> menores = new ArrayList<>();
                    List<Token> miniexpressao = new ArrayList<>();

                    //Big fucking gambiarra. Tentando nao quebrar quando tiver "Array" no escreva.
                    //Infelizmente... quebra no caso de Array dentro de Array, apenas no escreva.
                    boolean ehArray = false;
                    for (Token tk : expressao){
                        if (tk.getIdUnico() == TK_MENOR)
                            ehArray = true;

                        else if (tk.getIdUnico() == TK_MAIOR)
                            ehArray = false;

                        if(!ehArray && tk.getIdUnico() == TK_VIRGULA){
                            menores.add(miniexpressao);
                            miniexpressao = new ArrayList<>();
                            continue;
                        }
                       miniexpressao.add(tk);
                    }
                    //Add a ultima expressao, se n ela ficara de fora haha
                    menores.add(miniexpressao);

                    for (List<Token> lista : menores) {
                        contExp = 0;
                        int tipo = verificaExpressoes(lista);
                        if (tipo != TK_CADEIA && tipo != TK_CARACTERE && tipo != TK_INTEIRO && tipo != TK_REAL) {
                            String resultado = "";
                            for (Token tk : lista)
                                resultado = resultado + tk.getLexema() + " ";

                            ErroSemantico erro = new ErroSemantico(resultado, TIPOS_INCOMPATIVEIS, t.getnLinha());
                            if (!erros.contains(erro))
                                erros.add(erro);
                        }
                    }

                    //Pula o fecha parentese
                    cont++;
                    t.getLexema();
                }
            }
        }

        if (erros.isEmpty())
            System.out.println("Sem erros");
        else
            for(ErroSemantico erro: erros)
                System.out.println(erro);

        gerarSaidaSemantica();
    }

    public synchronized void gerarSaidaSemantica() {
        try {
            File pasta = new File(pastaSaida);
            pasta.mkdir();
            File n = new File(pasta.getName() + "//SS_" + arquivo);
            BufferedWriter bw = new BufferedWriter(new FileWriter(n));
            if (erros.isEmpty()) {
                bw.write("Sucesso!");
                bw.flush();
            }
            else {

                for (ErroSemantico erro : erros) {
                    bw.write(erro.toString());
                    bw.newLine();
                    bw.flush();
                }

            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean quebraCapturaExpressao(int finalizador){
        if (t.getIdUnico() != TK_PARENTESE_F)
            return false;

        Token proximo = null;
        if (cont+1 < tokens.size())
            proximo = tokens.get(cont+1);

        if (proximo.getIdUnico() == finalizador)
            return true;

        return false;
    }

    private boolean condicaoDeParadaFuncao(Token t, int contador){
        return t.getIdUnico() == TK_PARENTESE_F && contador == 0;
    }


    private void verificaFuncao(String identificador) {
        //Se a funcao existe
        if (tabelaGlobal.containsKey(identificador)) {
            Simbolo simbolo = tabelaGlobal.get(identificador);
            List<Simbolo> parametros = new ArrayList<>();
            List<Token> expressao = new ArrayList<>();
            int contadorParametros = 0;

             while (!condicaoDeParadaFuncao(t, contadorParametros)) {
                 cont++;
                 t = tokens.get(cont);

                 if (t.getIdUnico() == TK_PARENTESE_A)
                     contadorParametros++;

                 else if (t.getIdUnico() == TK_PARENTESE_F){
                     if (contadorParametros-1 >= 0)
                         contadorParametros--;
                     else if (contadorParametros == 0)
                         continue;
                 }

                 expressao.add(t);
             }

            List<List<Token>> menores = new ArrayList<>();
            List<Token> miniexpressao = new ArrayList<>();

            //Big fucking gambiarra. Tentando nao quebrar quando tiver "Array" no escreva.
            //Infelizmente... quebra no caso de Array dentro de Array, apenas no escreva.
            boolean ehArray = false;
            for (Token tk : expressao) {
                if (tk.getIdUnico() == TK_MENOR)
                    ehArray = true;
                else if (tk.getIdUnico() == TK_MAIOR)
                    ehArray = false;

                if (!ehArray && tk.getIdUnico() == TK_VIRGULA) {
                    menores.add(miniexpressao);
                    miniexpressao = new ArrayList<>();
                    continue;
                }
                miniexpressao.add(tk);
            }

             //Add a ultima expressao, se n ela ficara de fora haha
             if(!miniexpressao.isEmpty())
                menores.add(miniexpressao);

            int tamanhoParametros = menores.size();

            //Quantidade de parametros invalida, nao verifico os tipos
            if (simbolo.getParametros().size() != tamanhoParametros){
                erros.add(new ErroSemantico(simbolo.getId(), QNT_PARAM_INVALIDOS, t.getnLinha()));
                return;
            }

            List<Token> lista = null;
            for (int i=0; i < menores.size();i++) {
                contExp = 0;
                int tipoEsperado = simbolo.getParametros().get(i).getTipo();
                lista = menores.get(i);
                int tipoVindo = verificaExpressoes(lista);

                if(tipoEsperado != tipoVindo){
                   if (!(tipoEsperado == TK_REAL && tipoVindo == TK_INTEIRO)) {
                        ErroSemantico erro = new ErroSemantico(identificador, TIPOS_PARAM_INVALIDOS, t.getnLinha());
                        if (!erros.contains(erro))
                            erros.add(erro);
                   }
                }
            }
        }
        //Funcao nao existe
        else{
            int contadorParametros = 0;
            //Ignora tudo ateh o fecha parentese.
             while (t.getIdUnico() != TK_PARENTESE_F && contadorParametros != 0) {
                 cont++;
                 t = tokens.get(cont);

                 if (t.getIdUnico() == TK_PARENTESE_A)
                     contadorParametros++;

                 else if (t.getIdUnico() == TK_PARENTESE_F)
                     if (contadorParametros-1 > 0)
                         contadorParametros--;
             }
        }
            /*
            //Checa parametros
            while (t.getIdUnico() != TK_PARENTESE_F) {
                cont++;
                t = tokens.get(cont);

                //Quando uma funcao nao tem parametros, acontece de PARENTESE_F ser a 1ª coisa que ele ler.
                if (t.getIdUnico() == TK_PARENTESE_F)
                    continue;

                Simbolo variavel = tabela.get(t.getLexema());

                //Cuida de tipos padroes, caracteres, inteiros, etc.
                if (ehLiteral(t.getIdUnico()))
                    variavel = new Simbolo(t.getLexema(), converteTipo(t), t);

                //Variavel/Const nao existe no escopo local
                if (variavel == null) {
                    //Verifica no escopo global
                    variavel = tabelaGlobal.get(t.getLexema());
                    if (variavel == null) {
                        erros.add(new ErroSemantico(t.getLexema(), VAR_NAO_DECL, t.getnLinha()));
                        //Ignora todos os parametros
                        while (t.getIdUnico() != TK_PARENTESE_F) {
                            cont++;
                            t = tokens.get(cont);
                        }
                        return;
                    }
                }

                //Lida com funcoes dentro de funcoes
                if (variavel.ehFuncao()){
                    String funcao = variavel.getId();
                    //A funcao VerificaFuncao sempre comeca a analisar como o "token atual" sendo (
                    //Entao avancando a entrada...
                    cont++;
                    t = tokens.get(cont);
                    verificaFuncao(funcao);
                }

                //Move a entrada consumindo coisas relacionada a variavel
                while (!checaTerminadoresParametros()) {
                    cont++;
                    t = tokens.get(cont);
                }
                parametros.add(variavel);
            }
            //Checa quantidade de parametros
            if (parametros.size() != simbolo.getParametros().size()){
                erros.add(new ErroSemantico(simbolo.getId(), QNT_PARAM_INVALIDOS, t.getnLinha()));
                //Ignora todos os parametros
                while (t.getIdUnico() != TK_PARENTESE_F){
                    cont++;
                    t = tokens.get(cont);
                }
                return;
            }

            //Checa se o tipo de parametro esta correto
            for (int i = 0; i < parametros.size(); i++) {
                Simbolo tabelado = simbolo.getParametros().get(i);
                Simbolo parametro = parametros.get(i);

                //Se um for matriz e o outro nao
                if (tabelado.ehMatriz() && !parametro.ehMatriz() || parametro.ehMatriz() && !tabelado.ehMatriz())
                    erros.add(new ErroSemantico(identificador, TIPOS_PARAM_INVALIDOS, t.getnLinha()));

                //Se tiverem tipos diferentes
                if (tabelado.getTipo() != parametro.getTipo())
                    erros.add(new ErroSemantico(identificador, TIPOS_PARAM_INVALIDOS, t.getnLinha()));
            }
        } //Funcao nao declarada
        else{
            erros.add(new ErroSemantico(identificador, FUNC_NAO_DECL, t.getnLinha()));
            //Ignora todos os parametros q ela pode ter
            while (t.getIdUnico() != TK_PARENTESE_F){
                cont++;
                t = tokens.get(cont);
            }
        }
            */
    }

    /**
     * Metodo que verifica funcoes para expressoes.
     * Ao sair deste metodo o proximo token eh o ).
     * @param identificador id da funcao
     * @param t token atual
     * @param tokens  lista de tokens da expressao
     */
    private void verificaFuncao(String identificador, Token t, List<Token> tokens) {
        //Se a funcao existe
        if (tabelaGlobal.containsKey(identificador)) {
            Simbolo simbolo = tabelaGlobal.get(identificador);
            List<Simbolo> parametros = new ArrayList<>();
            List<Token> expressao = new ArrayList<>();
            int contadorParametros = 0;

             while (!condicaoDeParadaFuncao(t,contadorParametros)) {
                 contExp++;
                 t = tokens.get(contExp);

                 if (t.getIdUnico() == TK_PARENTESE_A)
                     contadorParametros++;

                 else if (t.getIdUnico() == TK_PARENTESE_F){
                     if (contadorParametros-1 >= 0)
                         contadorParametros--;
                     else if (contadorParametros == 0)
                         continue;
                 }

                 expressao.add(t);
             }

            List<List<Token>> menores = new ArrayList<>();
            List<Token> miniexpressao = new ArrayList<>();

            //Big fucking gambiarra. Tentando nao quebrar quando tiver "Array" no escreva.
            //Infelizmente... quebra no caso de Array dentro de Array, apenas no escreva.
            boolean ehArray = false;
            for (Token tk : expressao) {
                if (tk.getIdUnico() == TK_MENOR)
                    ehArray = true;
                else if (tk.getIdUnico() == TK_MAIOR)
                    ehArray = false;

                if (!ehArray && tk.getIdUnico() == TK_VIRGULA) {
                    menores.add(miniexpressao);
                    miniexpressao = new ArrayList<>();
                    continue;
                }
                miniexpressao.add(tk);
            }

             //Add a ultima expressao, se n ela ficara de fora haha
             if(!miniexpressao.isEmpty())
                menores.add(miniexpressao);

            int tamanhoParametros = menores.size();

            //Quantidade de parametros invalida, nao verifico os tipos
            if (simbolo.getParametros().size() != tamanhoParametros){
                erros.add(new ErroSemantico(simbolo.getId(), QNT_PARAM_INVALIDOS, t.getnLinha()));
                return;
            }

            List<Token> lista = null;
            for (int i=0; i < menores.size();i++) {
                int cache = contExp;
                contExp = 0;
                int tipoEsperado = simbolo.getParametros().get(i).getTipo();
                lista = menores.get(i);
                int tipoVindo = verificaExpressoes(lista);
                contExp = cache;
                if(tipoEsperado != tipoVindo){
                    ErroSemantico erro = new ErroSemantico(identificador, TIPOS_PARAM_INVALIDOS, t.getnLinha());
                    if (!erros.contains(erro))
                        erros.add(erro);
                }
            }
        }
        //Funcao nao existe
        else{
            int contadorParametros = 0;
            //Ignora tudo ateh o fecha parentese.
             while (t.getIdUnico() != TK_PARENTESE_F && contadorParametros != 0) {
                 contExp++;
                 t = tokens.get(contExp);

                 if (t.getIdUnico() == TK_PARENTESE_A)
                     contadorParametros++;

                 else if (t.getIdUnico() == TK_PARENTESE_F)
                     if (contadorParametros-1 > 0)
                         contadorParametros--;
             }
        }
        /*
        //Se a funcao existe
        if (tabelaGlobal.containsKey(identificador)) {
            Simbolo simbolo = tabelaGlobal.get(identificador);
            List<Simbolo> parametros = new ArrayList<>();

            //Checa parametros
            while (t.getIdUnico() != TK_PARENTESE_F) {
                contExp++;
                t = tokens.get(contExp);

                //Quando uma funcao nao tem parametros, acontece de PARENTESE_F ser a 1ª coisa que ele ler.
                if (t.getIdUnico() == TK_PARENTESE_F)
                    continue;

                if (t.getIdUnico() == TK_VIRGULA)
                    continue;

                Simbolo variavel = tabela.get(t.getLexema());

                //Cuida de tipos padroes, caracteres, inteiros, etc.
                if (ehLiteral(t.getIdUnico()))
                    variavel = new Simbolo(t.getLexema(), converteTipo(t), t);

                //Variavel/Const nao existe no escopo local
                if (variavel == null) {
                    //Verifica no escopo global
                    variavel = tabelaGlobal.get(t.getLexema());
                    if (variavel == null) {
                        erros.add(new ErroSemantico(t.getLexema(), VAR_NAO_DECL, t.getnLinha()));
                        //Ignora todos os parametros
                        while (t.getIdUnico() != TK_PARENTESE_F) {
                            contExp++;
                            t = tokens.get(contExp);
                        }
                        return;
                    }
                }

                //Lida com funcoes dentro de funcoes
                if (variavel.ehFuncao()){
                    String funcao = variavel.getId();
                    //A funcao VerificaFuncao sempre comeca a analisar como o "token atual" sendo (
                    //Entao avancando a entrada...
                    contExp++;
                    t = tokens.get(contExp);
                    verificaFuncao(funcao, t, tokens);
                    t = tokens.get(contExp);
                }

                parametros.add(variavel);
                t = tokens.get(contExp);
            }
            //Checa quantidade de parametros
            if (parametros.size() != simbolo.getParametros().size()){
                erros.add(new ErroSemantico(simbolo.getId(), QNT_PARAM_INVALIDOS, t.getnLinha()));
                //Ignora todos os parametros
                while (t.getIdUnico() != TK_PARENTESE_F){
                    contExp++;
                    t = tokens.get(contExp);
                }
                return;
            }

            //Checa se o tipo de parametro esta correto
            for (int i = 0; i < parametros.size(); i++) {
                Simbolo tabelado = simbolo.getParametros().get(i);
                Simbolo parametro = parametros.get(i);

                //Se um for matriz e o outro nao
                if (tabelado.ehMatriz() && !parametro.ehMatriz() || parametro.ehMatriz() && !tabelado.ehMatriz())
                    erros.add(new ErroSemantico(identificador, TIPOS_PARAM_INVALIDOS, t.getnLinha()));

                //Se tiverem tipos diferentes
                if (tabelado.getTipo() != parametro.getTipo())
                    erros.add(new ErroSemantico(identificador, TIPOS_PARAM_INVALIDOS, t.getnLinha()));
            }
        } //Funcao nao declarada
        else{
            erros.add(new ErroSemantico(identificador, FUNC_NAO_DECL, t.getnLinha()));
            //Ignora todos os parametros q ela pode ter
            while (t.getIdUnico() != TK_PARENTESE_F){
                contExp++;
                t = tokens.get(contExp);
            }
        }*/
    }

    private Simbolo criaSimbolo(String id, int tipo, List<Token> valor, int linha){
        Simbolo simbolo = new Simbolo(id, tipo, valor);

        if (tabelaGlobal.containsKey(id)){
            Simbolo sim = tabelaGlobal.get(id);
            if (sim.ehFuncao()){
                erros.add(new ErroSemantico(simbolo.getId(), VAR_JA_DECL, linha));
                return simbolo;
            }
        }

        if(!tabela.containsKey(id)){
            tabela.put(id, simbolo);
            System.out.println(simbolo);
        }
        else
            erros.add(new ErroSemantico(simbolo.getId(), VAR_JA_DECL, linha));

        return simbolo;
    }

    private Simbolo criaSimbolo(String id, int tipo, List<Token> valor, Hashtable<String, Simbolo> tabelaE, int linha){
        Simbolo simbolo = new Simbolo(id, tipo, valor);

         if (tabelaGlobal.containsKey(id)){
            Simbolo sim = tabelaGlobal.get(id);
            if (sim.ehFuncao()){
                erros.add(new ErroSemantico(simbolo.getId(), VAR_JA_DECL, linha));
                return simbolo;
            }
        }

        if(!tabelaE.containsKey(id)){
            tabelaE.put(id,simbolo);
            System.out.println(simbolo);
        }
        else
            erros.add(new ErroSemantico(simbolo.getId(), VAR_JA_DECL, linha));

        return simbolo;
    }

    private void percorreCriandoSimboloConst() {
        valor = new ArrayList<>();
        identificador = "";
        //Pega o identificador
        t = tokens.get(cont);
        identificador = t.getLexema();
        cont++;

        //Pula o igual
        cont++;
        t = tokens.get(cont);


        //Percorre o valor ate achar virgula ou ponto e virgula
        while (!checaTerminadores()) {

            valor.add(t);
            cont++;
            t = tokens.get(cont);

            //Gambiarra para Arrays, horrenda por sinal.
            //Serve pra quando tiver arrays no meio de declaracao de constante.
            //A virgula (separador das dimensoes) da pau na virgula de separar constantes.
            if (checaTerminadores())
                if (cont+1 < tokens.size() && tokens.get(cont+1).getIdUnico() == TK_NUMERO){
                    valor.add(t);
                    cont++;
                    t = tokens.get(cont);
                }
        }

        //Busca funcoes
        if (valor.size() > 1) {
            for (int i =0; i<valor.size();i++){
                Token token = valor.get(i);
                if (token.getIdUnico() == TK_PARENTESE_A){
                    if (i - 1 >= 0 && valor.get(i-1).getIdUnico() == TK_ID){
                        erros.add(new ErroSemantico(identificador, FUNC_EM_CONST, token.getnLinha()));
                        Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
                        simbolo.setEhConstante(true);
                        return;
                    }
                }
                else if (token.getIdUnico() == TK_MENOR){
                    if (i + 1 < valor.size() && valor.get(i+1).getIdUnico() == TK_MENOR){
                        erros.add(new ErroSemantico(identificador, ARRAY_EM_CONST, token.getnLinha()));
                        Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
                        simbolo.setEhConstante(true);
                        return;
                    }
                }
            }

            //Evita que tenha variaveis dentro de expressoes, quando for expressoes de contantes
            for (Token tk : valor){
                if(tk.getIdUnico() == TK_ID){
                    Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
                    simbolo.setEhConstante(true);
                    erros.add(new ErroSemantico(tk.getLexema(), VAR_NAO_DECL, tk.getnLinha()));
                    return;
                }
            }
            contExp = 0;
            int tipo = verificaExpressoes(valor);
            if (tipo != tipoAtual){
                //Adicionando uma excecao porque decidiram aceitar
                if (!(tipoAtual == TK_REAL && tipo == TK_INTEIRO)) {
                    String valores = "";
                    for (Token token : valor)
                        valores = valores + token.getLexema() + " ";
                    String resultado = "Na atribuicao da constante: "+identificador+", a expressao: "+valores+ ", retornou o tipo invalido: "+conversorIdString(tipo);
                    erros.add(new ErroSemantico(resultado, TIPO_ATRIBUICAO_INVALIDA, valor.get(0).getnLinha()));
                }
            }

        }
        //Identifica tipos diferentes
        else if (valor.size() == 1){
            Token token = valor.get(0);
            if (tabelaGlobal.containsKey(token.getLexema())){
                Simbolo sim = tabelaGlobal.get(token.getLexema());
                if (!sim.ehConstante()){
                    Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
                    simbolo.setEhConstante(true);
                    erros.add(new ErroSemantico(token.getLexema(), VAR_EM_CONST, token.getnLinha()));
                    return;
                }
                else if (sim.ehConstante() && sim.getTipo() != tipoAtual){
                    Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
                    simbolo.setEhConstante(true);
                    erros.add(new ErroSemantico(token.getLexema(), TIPOS_INCOMPATIVEIS, token.getnLinha()));
                    return;
                }
            }
            else if (!ehLiteral(token.getIdUnico())){
                Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
                simbolo.setEhConstante(true);
                erros.add(new ErroSemantico(token.getLexema(), VAR_NAO_DECL, token.getnLinha()));
                return;
            }
            //Lida com tipos diretos.
            if (token.getIdUnico() != TK_ID){
                int tipo = converteTipo(token);
                if (tipo != tipoAtual)
                    if (!(tipoAtual == TK_REAL && tipo == TK_INTEIRO)) //Adicionando uma excecao porque decidiram aceitar
                        erros.add(new ErroSemantico(token.getLexema(), TIPOS_INCOMPATIVEIS, token.getnLinha()));
            }
        }

        Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());
        simbolo.setEhConstante(true);
    }

    private void verificaMatriz() {
        dimensoes = 0;
        //Pula o menor atual e o proximo
        cont += 2;
        t = tokens.get(cont);

        while (t.getIdUnico() != TK_MAIOR) {
            //Ignora a virgula, para nao contar nas dimensoes
            if (t.getIdUnico() != TK_VIRGULA) {
                dimensoes++;

                Token temp = t;
                Token proximo = t;
                Token proximoDenovo = t;

                if (cont +1 < tokens.size())
                    proximo = tokens.get(cont+1);
                if (cont +2 < tokens.size())
                    proximoDenovo = tokens.get(cont+2);

                //Verificando array dentro de arrays
                if (t.getIdUnico() == TK_MENOR){
                    int dimensoesAtuais = dimensoes;
                    verificaMatriz();

                    Simbolo variavel = tabela.get(t.getLexema());
                    //Variavel/Const nao existe no escopo local
                    if (variavel == null)
                        variavel = tabelaGlobal.get(t.getLexema()); //Verifica no escopo global

                    if(variavel != null && variavel.getTipo() != TK_INTEIRO)
                        erros.add(new ErroSemantico(variavel.getId(), TIPOS_INCOMPATIVEIS, t.getnLinha()));

                    if (variavel != null && variavel.getDimensoes() != dimensoes)
                        erros.add(new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha()));
                    dimensoes = dimensoesAtuais;
                    cont++;
                    t = tokens.get(cont);
                    continue;
                }

                //Eh expressao !
                else if (temp.getIdUnico() == TK_SUBTRACAO || temp.getIdUnico() == TK_PARENTESE_A || (ehOperador(proximo.getIdUnico())) && proximo.getIdUnico() != TK_MAIOR && proximoDenovo.getIdUnico() != TK_MAIOR){
                    List<Token> expressao = new ArrayList<>();
                    int c = cont;
                    Token aux = t;
                    while (aux.getIdUnico() != TK_VIRGULA && !ehQuebradorDeVetor(c, tokens, aux)){
                        expressao.add(aux);
                        c++;
                        aux = tokens.get(c);
                    }
                    contExp = 0;
                    int tipo = verificaExpressoes(expressao);

                    if (tipo != TK_INTEIRO){
                        String retorno = "";
                        for (Token tk: expressao)
                            retorno = retorno + tk.getLexema() + " ";
                        ErroSemantico erro = new ErroSemantico(retorno, TIPOS_INCOMPATIVEIS, t.getnLinha());
                        if(!erros.contains(erro))
                            erros.add(erro);
                    }
                    cont = c;
                    cont++;
                    t = tokens.get(cont);
                    continue;
                }

                //Eh uma variavel ou uma funcao
                else if (t.getIdUnico() == TK_ID) {
                    String id = t.getLexema();

                    Token tk = t;
                    //Pega o proximo elemento
                    if (cont+1 < tokens.size())
                        tk = tokens.get(cont+1);

                    if (tk.getIdUnico() == TK_PARENTESE_A){
                        cont++;
                        t = tokens.get(cont);
                        verificaFuncao(id);
                    }

                    Simbolo variavel = tabela.get(id);

                    //Variavel/Const nao existe no escopo local
                    if (variavel == null) {
                        //Verifica no escopo global
                        variavel = tabelaGlobal.get(id);
                        if (variavel == null) {
                            ErroSemantico error = new ErroSemantico(id, VAR_NAO_DECL, t.getnLinha());
                            if (!erros.contains(error))
                                erros.add(error);
                        }
                    }

                    if (variavel != null && variavel.ehFuncao() && variavel.getTipo() != TK_INTEIRO)
                        erros.add(new ErroSemantico(variavel.getId(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
                    else if (variavel != null && variavel.getTipo() != TK_INTEIRO)
                        erros.add(new ErroSemantico(variavel.getId(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
                    else if (variavel == null)
                        erros.add(new ErroSemantico(id, TIPOS_INCOMPATIVEIS, t.getnLinha()));
                }
                else if (ehLiteral(t.getIdUnico()) && converteTipo(t) != TK_INTEIRO)
                    erros.add(new ErroSemantico(t.getLexema(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
            }

            cont++;
            t = tokens.get(cont);
        }
        //Pula o maior atual e o proximo
        cont += 2;
        t = tokens.get(cont);

        //Confere as dimensoes
        Simbolo variavel = tabela.get(t.getLexema());

        //Variavel/Const nao existe no escopo local
        if (variavel == null) {
            //Verifica no escopo global
            variavel = tabelaGlobal.get(t.getLexema());
        }

        if (variavel != null && variavel.getDimensoes() != dimensoes)
            erros.add(new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha()));
    }

    private boolean ehQuebradorDeVetor(int posicao, List<Token> tokens, Token atual){

        if (atual.getIdUnico() != TK_MAIOR)
            return false;

        Token proximo = null;
        if (posicao+1 < tokens.size())
            proximo = tokens.get(posicao+1);

        if (proximo != null && proximo.getIdUnico() != TK_MAIOR)
            return false;

        return true;
    }

    private void verificaMatriz(Token t, List<Token> tokens) {
        dimensoes = 0;
        //Pula o menor atual e o proximo
        contExp += 2;
        t = tokens.get(contExp);

        while (t.getIdUnico() != TK_MAIOR) {
            //Ignora a virgula, para nao contar nas dimensoes
            if (t.getIdUnico() != TK_VIRGULA) {
                dimensoes++;

                Token temp = t;
                Token proximo = t;
                Token proximoDenovo = t;

                if (contExp +1 < tokens.size())
                    proximo = tokens.get(contExp+1);
                if (contExp +2 < tokens.size())
                    proximoDenovo = tokens.get(contExp+2);

                //Verificando array dentro de arrays
                if (t.getIdUnico() == TK_MENOR){
                    int dimensoesAtuais = dimensoes;
                    verificaMatriz(t,tokens);

                    Simbolo variavel = tabela.get(t.getLexema());
                    //Variavel/Const nao existe no escopo local
                    if (variavel == null)
                        variavel = tabelaGlobal.get(t.getLexema()); //Verifica no escopo global

                    if(variavel != null && variavel.getTipo() != TK_INTEIRO)
                        erros.add(new ErroSemantico(variavel.getId(), TIPOS_INCOMPATIVEIS, t.getnLinha()));

                    if (variavel != null && variavel.getDimensoes() != dimensoes)
                        erros.add(new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha()));
                    dimensoes = dimensoesAtuais;
                    contExp++;
                    t = tokens.get(contExp);
                    continue;
                }

                //Eh expressao !
                else if (temp.getIdUnico() == TK_SUBTRACAO || temp.getIdUnico() == TK_PARENTESE_A || (ehOperador(proximo.getIdUnico())) && proximo.getIdUnico() != TK_MAIOR && proximoDenovo.getIdUnico() != TK_MAIOR){
                    List<Token> expressao = new ArrayList<>();
                    int c = contExp;
                    Token aux = t;
                    while (aux.getIdUnico() != TK_VIRGULA && !ehQuebradorDeVetor(c, tokens, aux)){
                        expressao.add(aux);
                        c++;
                        aux = tokens.get(c);
                    }
                    contExp = 0;
                    int tipo = verificaExpressoes(expressao);

                    if (tipo != TK_INTEIRO){
                        String retorno = "";
                        for (Token tk: expressao)
                            retorno = retorno + tk.getLexema() + " ";
                        ErroSemantico erro = new ErroSemantico(retorno, TIPOS_INCOMPATIVEIS, t.getnLinha());
                        if(!erros.contains(erro))
                            erros.add(erro);
                    }
                    contExp = c;
                    contExp++;
                    t = tokens.get(contExp);
                    continue;
                }

                //Eh uma variavel ou uma funcao
                else if (t.getIdUnico() == TK_ID) {
                    String id = t.getLexema();

                    Token tk = t;
                    //Pega o proximo elemento
                    if (contExp+1 < tokens.size())
                        tk = tokens.get(contExp+1);

                    if (tk.getIdUnico() == TK_PARENTESE_A){
                        contExp++;
                        t = tokens.get(contExp);
                        verificaFuncao(id);
                    }

                    Simbolo variavel = tabela.get(id);

                    //Variavel/Const nao existe no escopo local
                    if (variavel == null) {
                        //Verifica no escopo global
                        variavel = tabelaGlobal.get(id);
                        if (variavel == null) {
                            ErroSemantico error = new ErroSemantico(id, VAR_NAO_DECL, t.getnLinha());
                            if (!erros.contains(error))
                                erros.add(error);
                        }
                    }

                    if (variavel != null && variavel.ehFuncao() && variavel.getTipo() != TK_INTEIRO)
                        erros.add(new ErroSemantico(variavel.getId(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
                    else if (variavel != null && variavel.getTipo() != TK_INTEIRO)
                        erros.add(new ErroSemantico(variavel.getId(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
                    else if (variavel == null)
                        erros.add(new ErroSemantico(id, TIPOS_INCOMPATIVEIS, t.getnLinha()));
                }
                //Literais
                else if (ehLiteral(t.getIdUnico()) && converteTipo(t) != TK_INTEIRO)
                    erros.add(new ErroSemantico(t.getLexema(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
            }

            contExp++;
            t = tokens.get(contExp);
        }

        //Pula o maior atual e o proximo
        contExp += 2;
        t = tokens.get(contExp);

        //Verifica tamanho das dimensoes
        Simbolo variavel = tabela.get(t.getLexema());

        //Variavel/Const nao existe no escopo local
        if (variavel == null) {
            //Verifica no escopo global
            variavel = tabelaGlobal.get(t.getLexema());
        }

        if (variavel != null && variavel.getDimensoes() != dimensoes)
            erros.add(new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha()));

    }

    private void percorreCriandoSimboloVar(){
        identificador = "";
        //Apenas garantindo que sempre nao tera valor
        valor = new ArrayList<>();

        t = tokens.get(cont);

        //Eh vetor/matriz
        if (t.getIdUnico() == TK_MENOR)
            verificaMatriz();


        //Pega o identificador
        t = tokens.get(cont);
        identificador = t.getLexema();
        cont++;

        //atualiza o tokenAtual para a virgula ou ponto e virgula.
        t = tokens.get(cont);

        Simbolo simbolo = criaSimbolo(identificador, tipoAtual, valor, t.getnLinha());

        if (dimensoes > 0){
            simbolo.setEhMatriz(true);
            simbolo.setDimensoes(dimensoes);
            dimensoes = 0;
        }
    }

    private boolean ehTipo(){
        return t.getIdUnico() == TK_INTEIRO || t.getIdUnico() == TK_REAL || t.getIdUnico() == TK_CADEIA || t.getIdUnico() == TK_CARACTERE || t.getIdUnico() == TK_BOOLEANO;
    }

    private boolean ehLiteral(int tipo){
        return tipo == TK_CADEIA_DE_CARACTERES || tipo == TK_CARACTERE_L || tipo == TK_NUMERO || tipo == TK_VERDADEIRO || tipo == TK_FALSO;
    }

    private boolean checaTerminadores(){
        return t.getIdUnico() == TK_VIRGULA || t.getIdUnico() == TK_PONTOVIRGULA;
    }

    private boolean checaTerminadoresParametros(){
        return t.getIdUnico() == TK_VIRGULA || t.getIdUnico() == TK_PARENTESE_F;
    }

    /**
     * Metodo adiciona o escopo atual na tabela de escopos, e cria um novo.
     * @param novaTabela define se eh necessario criar um novo, ou apenas salvar o escopo atual.
     */
    private void criaNovoEscopo(boolean novaTabela) {
        tabelas.add(tabela);

        //Criando nova tabela de simbolos
        if (novaTabela)
            tabela = new Hashtable<>();
        else
            tabela = null;

        System.out.println("Escopo: "+tabelas.size());
    }

    /**
     * Metodo que lida com os tipos de funcoes e identificadores
     * Ao sair deste metodo o proximo token eh o que vier depois do identificador ou o que vier depois do ).
     * @param expressao expressao que esta sendo analisada
     * @param atual token atual
     * @return tipo da funcao/identificador
     */
    private int lidaComTipoDeFuncoes(List<Token> expressao, Token atual) {
        //Pega o identificador da funcao
        String id = atual.getLexema();

        //Pega o proximo elemento
        if (contExp+1 < expressao.size()){
            contExp++;
            atual = expressao.get(contExp);
        }

        //Cuida de funcoes
        if (atual.getIdUnico() == TK_PARENTESE_A){
            //Ao sair daqui o token eh )
            verificaFuncao(id, atual, expressao);
        }

        Simbolo variavel = tabela.get(id);
        //Variavel/Const/Funcao nao existe no escopo local
        if (variavel == null) {
            //Verifica no escopo global
            variavel = tabelaGlobal.get(id);
            if (variavel == null)
                erros.add(new ErroSemantico(id, VAR_NAO_DECL, atual.getnLinha()));
        }

        if (variavel != null)
            return variavel.getTipo();

        return TIPO_INVALIDO;
    }

    private int verificaExpressoes(List<Token> expressao){
        int tipoAtual = TIPO_INVALIDO;
        int operador = TIPO_INVALIDO;
        int proximo = TIPO_INVALIDO;
        Token atual = null;
        String error = "";
        //Essas variaveis servem para criar o error depois.
        for( ; contExp < expressao.size(); contExp++){
            atual = expressao.get(contExp);
            operador = TIPO_INVALIDO;
            proximo = TIPO_INVALIDO;

            /***************************************VERIFICANDO PRIMEIRO ELEMENTO ********************/

            //Sempre que achar um fecha parentese, retorna a recursao.
            if (atual.getIdUnico() == TK_PARENTESE_F)
                return tipoAtual;

            //Comeca com identificador (Verifica se eh identificador e se eh funcao)
            if (atual.getIdUnico() == TK_ID && tipoAtual == TIPO_INVALIDO){
                int tipoTemp = lidaComTipoDeFuncoes(expressao, atual);
                if (tipoTemp != TIPO_INVALIDO)
                    tipoAtual = tipoTemp;
                atual = expressao.get(contExp);
            }

            //Inicia com matriz.
            else if (atual.getIdUnico() == TK_MENOR && tipoAtual == TIPO_INVALIDO) {
                //Saindo daqui o proximo elmento eh o que vim depois dos maiores (fim do array/matriz)
                verificaMatriz(atual, expressao);
                //Pega o id da matriz/array
                atual = expressao.get(contExp);
                if (atual.getIdUnico() == TK_ID) {
                    int tipoTemp = lidaComTipoDeFuncoes(expressao, atual);
                    if (tipoTemp != TIPO_INVALIDO)
                        tipoAtual = tipoTemp;

                    atual = expressao.get(contExp);
                }
            }
            //Inicia com parentese
            else if (atual.getIdUnico() == TK_PARENTESE_A){
                //Pula o abre parentese
                contExp++;
                atual = expressao.get(contExp);
                return verificaExpressoes(expressao);
            }

            //Comeca com valores diretos.
            if (tipoAtual == TIPO_INVALIDO){
                tipoAtual = converteTipo(atual);
                if (atual.getIdUnico() == TK_NAO){
                    tipoAtual = TK_BOOLEANO;
                    //Pega tudo que tem depois do nao para analisar.
                    List<Token> miniExpressao = new ArrayList<>();
                    int c = 0;
                    Token temp = null;
                    for (c = contExp+1; c < expressao.size(); c++) {
                        temp = expressao.get(c);
                        miniExpressao.add(temp);
                    }
                    return verificaExpressoes(miniExpressao);
                }
                contExp++;
            }
            //Atualizacao do atual, apos passar pelo primeiro tipo.
            if (contExp < expressao.size())
                atual = expressao.get(contExp);
            else
                return tipoAtual;

            //Para casos de apenas 1 elemento
            if ((expressao.size() == 1 && tipoAtual != TIPO_INVALIDO) || (contExp+1 >=expressao.size() && tipoAtual != TIPO_INVALIDO) )
                return tipoAtual;

            //Pega o operador. (Existem casos que nao tera operador ex: 5+4*(4);
            //Verifica se eh operador relacional ou logico (tem maior precendencia)
            if (ehOperadorRelacional(atual.getIdUnico()) || ehOperadorLogico(atual.getIdUnico())){
                List<Token> miniExpressao = new ArrayList<>();
                //Pega o operador
                operador = atual.getIdUnico();
                contExp++;
                atual = expressao.get(contExp);

                int c = 0;
                Token temp = null;
                for (c = contExp; c <expressao.size(); c++){
                    temp = expressao.get(c);
                    if (ehOperadorLogico(temp.getIdUnico())){
                        c--;
                        break;
                    }
                    miniExpressao.add(temp);
                }
                contExp = 0;
                proximo = verificaExpressoes(miniExpressao);
                contExp = c;
            }
            //Verifica se eh operador arimetico
            else if (ehOperadorAritmetico(atual.getIdUnico())) {
                operador = atual.getIdUnico();
                //Se veio operador antes, pega o proximo elemento
                contExp++;
                atual = expressao.get(contExp);
            }

            /***************************************VERIFICANDO SEGUNDO ELEMENTO ********************/

            //Sempre que achar um fecha parentese, retorna a recursao.
            if (atual.getIdUnico() == TK_PARENTESE_F)
                return tipoAtual;

            //Verifica se o proximo elemento eh variavel ou funcao
            if (atual.getIdUnico() == TK_ID && proximo == TIPO_INVALIDO){
                int tipoTemp = lidaComTipoDeFuncoes(expressao, atual);
                if (tipoTemp != TIPO_INVALIDO)
                    proximo = tipoTemp;
                atual = expressao.get(contExp);
            }

            //Tem matriz no meio
            //Inicia com matriz.
            else if (atual.getIdUnico() == TK_MENOR && proximo == TIPO_INVALIDO) {
                //Saindo daqui o proximo elmento eh o que vim depois dos maiores (fim do array/matriz)
                verificaMatriz(atual, expressao);
                //Pega o id da matriz/array
                atual = expressao.get(contExp);
                if (atual.getIdUnico() == TK_ID) {
                    int tipoTemp = lidaComTipoDeFuncoes(expressao, atual);
                    if (tipoTemp != TIPO_INVALIDO)
                        proximo = tipoTemp;

                    atual = expressao.get(contExp);
                }
            }

            //Se vim parentese
            else if (atual.getIdUnico() == TK_PARENTESE_A && proximo == TIPO_INVALIDO){
                //Pula o abre parentese
                contExp++;
                atual = expressao.get(contExp);
                proximo = verificaExpressoes(expressao);
            }

            //Eh um tipo comum, numero, string, etc.
            else if (ehLiteral(atual.getIdUnico()) && proximo == TIPO_INVALIDO)
                proximo = converteTipo(atual);

            int tempTipo = converteRegraTipos(tipoAtual, proximo, operador);
            if (tempTipo == TIPO_INVALIDO){
                String resultado = "";
                for (Token tk : expressao)
                    resultado = resultado + tk.getLexema() + " ";
                System.err.println(resultado);
                erros.add(new ErroSemantico(resultado, TIPOS_INCOMPATIVEIS, atual.getnLinha()));
            }
            else
                tipoAtual = tempTipo;
        }

        if (tipoAtual == TK_NAO)
            return TK_BOOLEANO;

        return tipoAtual;
    }
}
