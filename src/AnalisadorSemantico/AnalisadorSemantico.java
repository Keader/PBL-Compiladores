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

    public AnalisadorSemantico(List<Token> lTokens){
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
    }

    private synchronized void montarTabela(){

        for (cont = 0; cont < tokens.size(); cont++) {
            t = tokens.get(cont);

            if (t.getIdUnico() == TK_CONST) {
                //Se as funcoes nao foram alcansadas ainda, significa que existe escopo global
                if (!funcoesAlcancadas)
                    tabelaGlobal = tabela;
                else
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
                else
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
                if (escopoAtual > tabelas.size())
                    tabela = tabelas.get(escopoAtual);
            }

            //Analise de Corpo
            else {
                identificador = "";

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

                        //Pula ponto e virgula
                        cont++;
                        t = tokens.get(cont);
                    }

                    //Eh uma atribuicao
                    else if (t.getIdUnico() == TK_IGUAL){
                        //Se funcoes nao foram alcadas, esta pegando as atribuicoes de const... entao pula
                        if (!funcoesAlcancadas)
                            continue;

                        while (t.getIdUnico() != TK_PONTOVIRGULA){
                            //TODO
                        }

                    }
                }

                //Se comeca com Igual, eh retorno
                else if (t.getIdUnico() == TK_IGUAL){
                    //Pula o igual e o maior
                    cont+=2;
                    t = tokens.get(cont);

                    //Lidar com matrizes
                    if (t.getIdUnico() == TK_MENOR){
                        verificaMatriz();
                    }

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
                        if (variavel == null) {
                            erros.add(new ErroSemantico(t.getLexema(), VAR_NAO_DECL, t.getnLinha()));
                            return;
                        }
                    }

                    if (variavel.ehMatriz() && variavel.getDimensoes() != dimensoes)
                        erros.add(new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha()));

                    if (variavel.getTipo() != tabelado.getTipo())
                        erros.add(new ErroSemantico(t.getLexema(), RETORNO_INVALIDO, t.getnLinha()));

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
                                return;
                            }
                        }

                        if (variavel.ehMatriz() && variavel.getDimensoes() != dimensoes)
                            erros.add(new ErroSemantico(t.getLexema(), DIFF_DIMENSOES, t.getnLinha()));

                        else if (variavel.ehConstante())
                            erros.add(new ErroSemantico (t.getLexema(), ATRIBUICAO_INVALIDA, t.getnLinha()));

                        cont++;
                        t = tokens.get(cont);
                    }
                }
            }
        }
        if (erros.isEmpty())
            System.out.println("Sem erros");
        else
            for(ErroSemantico erro: erros)
                System.out.println(erro);
    }

    private void verificaFuncao(String identificador) {
        //Se a funcao existe
        if (tabelaGlobal.containsKey(identificador)) {
            Simbolo simbolo = tabelaGlobal.get(identificador);
            List<Simbolo> parametros = new ArrayList<>();

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

    }

    private void verificaFuncao(String identificador, Token t, int cont, List<Token> tokens) {
        //Se a funcao existe
        if (tabelaGlobal.containsKey(identificador)) {
            Simbolo simbolo = tabelaGlobal.get(identificador);
            List<Simbolo> parametros = new ArrayList<>();

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
        while (checaTerminadores()) {
            valor.add(t);
            cont++;
            t = tokens.get(cont);
        }

        //Busca funcoes
        if (valor.size() > 1){
            for (int i =0; i<valor.size();i++){
                Token token = valor.get(i);
                if (token.getIdUnico() == TK_PARENTESE_A)
                    if (i - 1 >= 0 && valor.get(i-1).getIdUnico() == TK_ID)
                        erros.add(new ErroSemantico(identificador, FUNC_EM_CONST, token.getnLinha()));
            }
        }
        //Identifica tipos diferentes
        else if (valor.size() == 1){
            Token token = valor.get(0);
            int tipo = converteTipo(token);
            if (tipo != tipoAtual)
                erros.add(new ErroSemantico(token.getLexema(), TIPOS_INCOMPATIVEIS, token.getnLinha()));
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

                //Falta verificar expressoes dentro do Array
                //Eh uma variavel ou uma funcao
                if (t.getIdUnico() == TK_ID) {
                    String id = t.getLexema();

                    //Pega o proximo elemento
                    cont++;
                    t = tokens.get(cont);

                    if (t.getIdUnico() == TK_PARENTESE_A)
                        verificaFuncao(id);

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
                        erros.add(new ErroSemantico(t.getLexema(), TIPOS_INCOMPATIVEIS, t.getnLinha()));
                    else
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
    }

    private void percorreCriandoSimboloVar(){
        identificador = "";
        //Apenas garantindo que sempre nao tera valor
        valor = new ArrayList<>();

        t = tokens.get(cont);

        //Eh vetor/matriz
        if (t.getIdUnico() == TK_MENOR){
            verificaMatriz();
        }

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
        tabelas.add(tabela);

        //Criando nova tabela de simbolos
        if (novaTabela)
            tabela = new Hashtable<>();
        else
            tabela = null;

        System.out.println("Escopo: "+tabelas.size());
    }

    private int lidaComTipoDeFuncoes(List<Token> expressao, int i, Token atual) {
        String id = atual.getLexema();

        //Pega o proximo elemento
        i++;
        atual = expressao.get(i);

        //Cuida de funcoes
        if (atual.getIdUnico() == TK_PARENTESE_A)
            verificaFuncao(id, atual, i, expressao);


        Simbolo variavel = tabela.get(atual.getLexema());
        //Variavel/Const/Funcao nao existe no escopo local
        if (variavel == null) {
            //Verifica no escopo global
            variavel = tabelaGlobal.get(atual.getLexema());
            if (variavel == null)
                erros.add(new ErroSemantico(atual.getLexema(), VAR_NAO_DECL, atual.getnLinha()));

        }

        if (variavel != null)
            return variavel.getTipo();

        return OP_INVALIDA;
    }

    private int verificaExpressoes(List<Token> expressao){
        int tipoAtual = -5;
        Token atual;
        for(int i = 0; i < expressao.size(); i++){
            atual = expressao.get(i);

            //Comeca com identificador
            if (atual.getIdUnico() == TK_ID){
                int tipoTemp = lidaComTipoDeFuncoes(expressao,i,atual);
                if (tipoTemp != OP_INVALIDA)
                    tipoAtual = tipoTemp;
            }

            else if (atual.getIdUnico() == TK_MAIOR){

            }

            //Comeca com valores diretos.
            else if (tipoAtual == VALOR_INICIAL)
                tipoAtual = converteTipo(atual);


            i++;
            atual = expressao.get(i);

        }
        return -1;
    }
}
