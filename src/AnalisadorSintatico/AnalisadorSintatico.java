package AnalisadorSintatico;

import static Util.Dicionario.getIdProducao;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import Util.Debug;
import Util.Dicionario;
import Util.No;
import Util.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Alberto Junior
 * @author Bradley
 */
public class AnalisadorSintatico implements Dicionario, Runnable {
	private List<Token> tokens;
	private Stack<Integer> pilha;
	private String arquivo;
    private List<ErroSintatico> erros;
    private ArvoreSintatica arvore;

	public AnalisadorSintatico(List<Token> tokens, String arquivo){
		this.tokens = new ArrayList<>();
		//criando uma copia da lista
		this.tokens.addAll(tokens);
		this.arquivo = arquivo;
		pilha = new Stack<>();
        erros = new ArrayList<>();
        arvore = new ArvoreSintatica();
	}

	@Override
	public void run(){
		try {
			iniciarAnalise();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void iniciarAnalise() {
		int posicao = 0;
		int maxQtdTokens = tokens.size() - 1;
		int tokenAtual = 0;

		pilha.push(TK_EOF);
		pilha.push(R_PROGRAMA);

		//repete enquanto a pilha nao estiver vazia
		while (!pilha.isEmpty()) {
            if(pilha.peek() == VOLTA_PRO_PAI){
                arvore.voltaProPai();
                pilha.pop();
                continue;
            }

			//Entrada acabou, verifica se o ultimo elemento da pilha eh EOF
			if (posicao > maxQtdTokens) {
				//verifica se o proximo eh o ultimo
				if (pilha.peek() == TK_EOF) {
					pilha.pop();
                    Debug.messagePane("Analise Sintatica do arquivo [" + arquivo + "] concluida.", "Sucesso", Debug.PADRAO);
					break;
				}
                //Entrada acabou antes da pilha, o que fazer?
				else
					Debug.messagePane("Entrada acabou mas na pilha nao tem o EOF", "Erro", Debug.ERRO);
			}

			tokenAtual = tokens.get(posicao).getIdUnico();

			//Verifica se no topo da pilha eh um terminal e se da match.
			if (tokenAtual == pilha.peek()) {
				pilha.pop();
				posicao++;
                arvore.add(tokenAtual);
			}
			//Se eh um terminal e nao da match com o topo da pilha
			else if (pilha.peek() < MAX_TOKEN_VALUE) {
				erros.add(new ErroSintatico(tokenAtual, pilha.peek()));
                pilha.pop();
			}
			else {
				int producao = getIdProducao(pilha.peek(), tokenAtual);

				if (producao != -1) {
					gerarProducao(producao);
                    arvore.add(producao);
                }
                //Gerou producao -1
				else {
                    //Desempilha tudo gerado por aquela producao
                    List<Integer> desempilha = new ArrayList<>();
                    for (No no : arvore.getAtual().getPai().getFilhos())
                        desempilha.add(no.getId());

                    while (desempilha.contains(pilha.peek()))
                        pilha.pop();

                    //Aqui precisa entrar o comando que pegara a lista de follows e syncs
                    //Apos a lista pronta, buscar qual terminal gera algo com um daqueles follows
				}
			}
		}
        //Pilha acabou antes da entrada
        if (posicao < maxQtdTokens){
            while (posicao > maxQtdTokens)
                erros.add(new ErroSintatico(tokens.get(posicao).getIdUnico()));
        }

        gerarSaidaSintatica();
	}

    public synchronized void gerarSaidaSintatica(){
        try {
            File pasta = new File("saida_sintatica");
            pasta.mkdir();
            File n = new File(pasta.getName()+"//SS_" + arquivo);
            BufferedWriter bw = new BufferedWriter(new FileWriter(n));
            if(erros.isEmpty()){
                bw.write("Sucesso!");
                bw.flush();
            }
            else{
                bw.write("Erros Detectados");
                for (ErroSintatico erro : erros){
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

	public synchronized void gerarProducao(int valor){
		switch(valor){
			case R_PROGRAMA:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_INICIO_CONST_VAR_FUNC);
				break;
			case R_PROGRAMA_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_INICIO_VAR_FUNC);
				break;
			case R_PROGRAMA_C3:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_INICIO_FUNC);
				break;
			case R_INICIO_CONST_VAR_FUNC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_CONST_VAR_DERIVADA);
				pilha.push(R_DEC_CONST);
				break;
			case R_DEC_CONST_VAR_DERIVADA_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_MAIN);
				pilha.push(R_DEC_FUNC);
				break;
			case R_INICIO_VAR_FUNC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_MAIN);
				pilha.push(R_DEC_FUNC);
				pilha.push(R_DEC_VAR);
				break;
			case R_INICIO_FUNC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_MAIN);
				pilha.push(R_DEC_FUNC);
				break;
			case R_DEC_CONST:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_DEC_CONST_CONTINUO);
				pilha.push(TK_INICIO);
				pilha.push(TK_CONST);
				break;
			case R_DEC_CONST_CONTINUO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_CONST_II);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(R_DEC_CONST_I);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_IGUAL);
				pilha.push(TK_ID);
				pilha.push(R_TIPO);
				break;
			case R_DEC_CONST_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_CONST_I);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_IGUAL);
				pilha.push(TK_ID);
				pilha.push(TK_VIRGULA);
				break;
			case R_DEC_CONST_II:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_CONST_II);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(R_DEC_CONST_I);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_IGUAL);
				pilha.push(TK_ID);
				pilha.push(R_TIPO);
				break;
			case R_DEC_VAR:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_DEC_VAR_CONTINUO);
				pilha.push(TK_INICIO);
				pilha.push(TK_VAR);
				break;
			case R_DEC_VAR_CONTINUO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_VAR_II);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(R_DEC_VAR_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY);
				pilha.push(R_TIPO);
				break;
			case R_DEC_VAR_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_VAR_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY);
				pilha.push(TK_VIRGULA);
				break;
			case R_DEC_VAR_II:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_VAR_II);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(R_DEC_VAR_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY);
				pilha.push(R_TIPO);
				break;
			case R_DEC_FUNC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_FUNC);
				pilha.push(R_DEC_FUNC_I);
				pilha.push(TK_FUNCAO);
				break;
			case R_DEC_FUNC_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_PARAMETROS);
				pilha.push(TK_PARENTESE_A);
				pilha.push(TK_ID);
				break;
			case R_DEC_FUNC_I_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_RETORNO_FUNC);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_PARAMETROS);
				pilha.push(TK_PARENTESE_A);
				pilha.push(TK_ID);
				pilha.push(R_TIPO);
				break;
			case R_PARAMETROS:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_PARAMETROS_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY_PARAM);
				pilha.push(R_TIPO);
				break;
			case R_PARAMETROS_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_PARAMETROS_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY_PARAM);
				pilha.push(R_TIPO);
				pilha.push(TK_VIRGULA);
				break;
			case R_DEC_MAIN:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				pilha.push(TK_PROGRAMA);
				break;
			case R_EXP_RELACIONAL_BOOL:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_EXP_CONJUNTA_I);
				pilha.push(R_EXP_CONJUNTA);
				break;
			case R_EXP_CONJUNTA:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_EXP_RELACIONAL_I);
				pilha.push(R_EXP_RELACIONAL);
				break;
			case R_EXP_CONJUNTA_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_EXP_CONJUNTA_I);
				pilha.push(R_EXP_CONJUNTA);
				pilha.push(TK_OU);
				break;
			case R_EXP_RELACIONAL:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_OPERAR_RELACIONALMENTE);
				pilha.push(R_EXP_SIMPLES);
				pilha.push(R_NOT_OPC);
				break;
			case R_EXP_RELACIONAL_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_EXP_RELACIONAL_I);
				pilha.push(R_EXP_RELACIONAL);
				pilha.push(TK_E);
				break;
			case R_OPERAR_RELACIONALMENTE:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_EXP_SIMPLES);
				pilha.push(R_NOT_OPC);
				pilha.push(R_OP_RELACIONAL);
				break;
			case R_OP_RELACIONAL:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MAIOR);
				break;
			case R_OP_RELACIONAL_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MAIORIGUAL);
				break;
			case R_OP_RELACIONAL_C3:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MENOR);
				break;
			case R_OP_RELACIONAL_C4:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MENORIGUAL);
				break;
			case R_OP_RELACIONAL_C5:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_DIFERENTE);
				break;
			case R_NOT_OPC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_NOT_OPC);
				pilha.push(TK_NAO);
				break;
			case R_EXP_SIMPLES:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_TERMO_I);
				pilha.push(R_TERMO);
				pilha.push(R_OP_MAIS_MENOS);
				break;
			case R_EXP_SIMPLES_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_TERMO_I);
				pilha.push(R_TERMO);
				break;
			case R_TERMO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_FATOR_I);
				pilha.push(R_FATOR);
				break;
			case R_TERMO_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_TERMO_I);
				pilha.push(R_TERMO);
				pilha.push(R_OP_MAIS_MENOS);
				break;
			case R_FATOR:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ID_FUNCAO_E_OUTROS);
				break;
			case R_FATOR_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_NUMERO);
				break;
			case R_FATOR_C3:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_VERDADEIRO);
				break;
			case R_FATOR_C4:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FALSO);
				break;
			case R_FATOR_C5:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_CADEIA_DE_CARACTERES);
				break;
			case R_FATOR_C6:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_CARACTERE_L);
				break;
			case R_FATOR_C7:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_PARENTESE_A);
				break;
			case R_FATOR_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_FATOR_I);
				pilha.push(R_FATOR);
				pilha.push(R_OP_MULTI_DIV);
				break;
			case R_OP_MAIS_MENOS:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_SOMA);
				break;
			case R_OP_MAIS_MENOS_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_SUBTRACAO);
				break;
			case R_OP_MULTI_DIV:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MULTIPLICACAO);
				break;
			case R_OP_MULTI_DIV_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_DIVISAO);
				break;
			case R_ID_FUNCAO_E_OUTROS:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_ID);
				pilha.push(TK_MAIOR);
				pilha.push(TK_MAIOR);
				pilha.push(R_ARRAY_I);
				pilha.push(R_ARRAY_INDEXES);
				pilha.push(TK_MENOR);
				pilha.push(TK_MENOR);
				break;
			case R_ID_FUNCAO_E_OUTROS_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ID_FUNCAO_E_OUTROS_DERIVADO);
				break;
			case R_ID_FUNCAO_E_OUTROS_DERIVADO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_POSSIBLE_FUNC);
				pilha.push(TK_ID);
				break;
			case R_POSSIBLE_FUNC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_PASSA_PARAM);
				pilha.push(TK_PARENTESE_A);
				break;
			case R_RETORNO_FUNC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(TK_MAIOR);
				pilha.push(TK_IGUAL);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_MAIOR);
				pilha.push(TK_IGUAL);
				break;
			case R_CORPO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_CORPO);
				pilha.push(R_COMANDOS);
				break;
			case R_COMANDOS:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_LEITURA);
				break;
			case R_COMANDOS_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_ESCRITA);
				break;
			case R_COMANDOS_C3:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_SE);
				break;
			case R_COMANDOS_C4:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_ENQUANTO);
				break;
			case R_COMANDOS_C5:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_CHAMA_OU_ATRIBUI);
				break;
			case R_COMANDOS_C6:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_DEC_VAR);
				break;
			case R_COMANDOS_C7:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_NOVO_ESCOPO);
				break;
			case R_CHAMA_OU_ATRIBUI:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_WHOS_NEXT);
				pilha.push(TK_ID);
				break;
			case R_CHAMA_OU_ATRIBUI_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_IGUAL);
				pilha.push(TK_ID);
				pilha.push(TK_MAIOR);
				pilha.push(TK_MAIOR);
				pilha.push(R_ARRAY_I);
				pilha.push(R_ARRAY_INDEXES);
				pilha.push(TK_MENOR);
				pilha.push(TK_MENOR);
				break;
			case R_WHOS_NEXT:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_IGUAL);
				break;
			case R_WHOS_NEXT_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_PASSA_PARAM);
				pilha.push(TK_PARENTESE_A);
				break;
			case R_NOVO_ESCOPO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				break;
			case R_ARRAY:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MAIOR);
				pilha.push(TK_MAIOR);
				pilha.push(R_ARRAY_I);
				pilha.push(R_ARRAY_INDEXES);
				pilha.push(TK_MENOR);
				pilha.push(TK_MENOR);
				break;
			case R_ARRAY_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ARRAY_I);
				pilha.push(R_ARRAY_INDEXES);
				pilha.push(TK_VIRGULA);
				break;
			case R_ARRAY_INDEXES:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_EXP_SIMPLES);
				break;
			case R_ARRAY_PARAM:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_MAIOR);
				pilha.push(TK_MAIOR);
				pilha.push(R_ARRAY_PARAM_I);
				pilha.push(R_ARRAY_INDEXES_OPT);
				pilha.push(TK_MENOR);
				pilha.push(TK_MENOR);
				break;
			case R_ARRAY_PARAM_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ARRAY_PARAM_I);
				pilha.push(R_ARRAY_INDEXES_OPT);
				pilha.push(TK_VIRGULA);
				break;
			case R_ARRAY_INDEXES_OPT:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ARRAY_INDEXES);
				break;
			case R_DEC_LEITURA:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_LEITURA_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY);
				pilha.push(TK_PARENTESE_A);
				pilha.push(TK_LEIA);
				break;
			case R_LEITURA_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_LEITURA_I);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY);
				pilha.push(TK_VIRGULA);
				break;
			case R_DEC_ESCRITA:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PONTOVIRGULA);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_ESCREVIVEL_I);
				pilha.push(R_ESCREVIVEL);
				pilha.push(TK_PARENTESE_A);
				pilha.push(TK_ESCREVA);
				break;
			case R_ESCREVIVEL_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ESCREVIVEL_I);
				pilha.push(R_ESCREVIVEL);
				pilha.push(TK_VIRGULA);
				break;
			case R_ESCREVIVEL:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_TERMO_I_E);
				pilha.push(R_TERMO_E);
				pilha.push(R_OP_MAIS_MENOS);
				break;
			case R_ESCREVIVEL_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_TERMO_I_E);
				pilha.push(R_TERMO_E);
				break;
			case R_TERMO_E:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_FATOR_I_E);
				pilha.push(R_FATOR_E);
				break;
			case R_TERMO_I_E:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_TERMO_I_E);
				pilha.push(R_TERMO_E);
				pilha.push(R_OP_MAIS_MENOS);
				break;
			case R_FATOR_E:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_ID);
				pilha.push(R_ARRAY);
				break;
			case R_FATOR_E_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_NUMERO);
				break;
			case R_FATOR_E_C3:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_CADEIA_DE_CARACTERES);
				break;
			case R_FATOR_E_C4:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_CARACTERE_L);
				break;
			case R_FATOR_E_C5:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_ESCREVIVEL);
				pilha.push(TK_PARENTESE_A);
				break;
			case R_FATOR_I_E:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_FATOR_I_E);
				pilha.push(R_FATOR_E);
				pilha.push(R_OP_MULTI_DIV);
				break;
			case R_DEC_SE:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_ELSE_OPC);
				pilha.push(TK_FIM);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				pilha.push(TK_ENTAO);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_PARENTESE_A);
				pilha.push(TK_SE);
				break;
			case R_ELSE_OPC:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				pilha.push(TK_SENAO);
				break;
			case R_DEC_ENQUANTO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_FIM);
				pilha.push(R_CORPO);
				pilha.push(TK_INICIO);
				pilha.push(TK_FACA);
				pilha.push(TK_PARENTESE_F);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_PARENTESE_A);
				pilha.push(TK_ENQUANTO);
				break;
			case R_PASSA_PARAM:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_PASSA_PARAM_I);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				break;
			case R_PASSA_PARAM_I:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(R_PASSA_PARAM_I);
				pilha.push(R_EXP_RELACIONAL_BOOL);
				pilha.push(TK_VIRGULA);
				break;
			case R_TIPO:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_INTEIRO);
				break;
			case R_TIPO_C2:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_REAL);
				break;
			case R_TIPO_C3:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_CADEIA);
				break;
			case R_TIPO_C4:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_BOOLEANO);
				break;
			case R_TIPO_C5:
				pilha.pop();
                pilha.push(VOLTA_PRO_PAI);
				pilha.push(TK_CARACTERE);
				break;
			case R_EPSILON:
				pilha.pop();
				break;
			default:
				Debug.ErrPrintln("Entrou no Default de gerarProducao com o valor: " + valor);
				break;
		}
	}
}
