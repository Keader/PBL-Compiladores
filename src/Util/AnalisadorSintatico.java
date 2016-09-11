package Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Bradley
 */
public class AnalisadorSintatico implements Dicionario {
    private List<Token> tokens;
    private Stack<Integer> stack;
    private static int [][]tabela;
    private static String arquivoTabela = "Matriz_Compiladores_teste.csv";

    public AnalisadorSintatico(List<Token> tokens){
        this.tokens = tokens;
        stack = new Stack<>();
    }

    public void iniciarAnalise(){ }

    public static void main(String args[]){
    	montarTabelaPredicao(arquivoTabela);
    }
    
    public static void montarTabelaPredicao(String arquivoTabela){
    	try {
			BufferedReader leitor = new BufferedReader(new FileReader(arquivoTabela));
			//capturando quantidade de regras
			String [] primeiraLinha = leitor.readLine().split(";");
			int qtdRegras = Integer.parseInt(primeiraLinha[0]);
			int qtdTerminais = Integer.parseInt(primeiraLinha[1]);
			tabela = new int[qtdRegras][qtdTerminais];
			String []auxLinha;
					
			//preenche a tabela
			for(int i = 0; i < qtdRegras; i++){
				Arrays.fill(tabela[i], -1);
				auxLinha = leitor.readLine().split(";");
				for(int x = 1; x < auxLinha.length; x++)
					tabela[i][x-1] = Dicionario.getRegraId(auxLinha[x]);				
			}

			leitor.close();

		} 
    	catch (FileNotFoundException e) {
    		System.out.println("Erro no arquivo da Tabela");
		} 
    	catch (IOException e) {
    		System.out.println("Erro no arquivo da Tabela");
    	}
    }
    
    public int getIdProducao(int regra, int token){
    	if(tabela == null)
    		montarTabelaPredicao(arquivoTabela);

		return tabela[regra][token];
    }

    public void gerarProducao(int valor){
        switch(valor){
            case R_PROGRAMA:
                stack.pop();
                stack.push(R_INICIO_CONST_VAR_FUNC);
                break;
            case R_PROGRAMA_C2:
                stack.pop();
                stack.push(R_INICIO_VAR_FUNC);
                break;
            case R_PROGRAMA_C3:
                stack.pop();
                stack.push(R_INICIO_FUNC);
                break;
            case R_INICIO_CONST_VAR_FUNC:
                stack.pop();
                stack.push(R_DEC_CONST_VAR_DERIVADA);
                stack.push(R_DEC_CONST);
                break;
            case R_DEC_CONST_VAR_DERIVADA_C2:
                stack.pop();
                stack.push(R_DEC_MAIN);
                stack.push(R_DEC_FUNC);
                break;
            case  R_INICIO_VAR_FUNC:
                stack.pop();
                stack.push(R_DEC_MAIN);
                stack.push(R_DEC_FUNC);
                stack.push(R_DEC_VAR);
                break;
            case R_INICIO_FUNC:
                stack.pop();
                stack.push(R_DEC_MAIN);
                stack.push(R_DEC_FUNC);
                break;
            case R_DEC_CONST:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_DEC_CONST_CONTINUO);
                stack.push(TK_INICIO);
                stack.push(TK_CONST);
                break;
            case R_DEC_CONST_CONTINUO:
                stack.pop();
                stack.push(R_DEC_CONST_II);
                stack.push(TK_PONTOVIRGULA);
                stack.push(R_DEC_CONST_I);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_IGUAL);
                stack.push(TK_ID);
                stack.push(R_TIPO);
                break;
            case R_DEC_CONST_I:
                stack.pop();
                stack.push(R_DEC_CONST_I);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_IGUAL);
                stack.push(TK_ID);
                stack.push(TK_VIRGULA);
                break;
            case R_DEC_CONST_II:
                stack.pop();
                stack.push(R_DEC_CONST_II);
                stack.push(TK_PONTOVIRGULA);
                stack.push(R_DEC_CONST_I);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_IGUAL);
                stack.push(TK_ID);
                stack.push(R_TIPO);
                break;
            case R_DEC_VAR:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_DEC_VAR_CONTINUO);
                stack.push(TK_INICIO);
                stack.push(TK_VAR);
                break;
            case R_DEC_VAR_CONTINUO:
                stack.pop();
                stack.push(R_DEC_VAR_II);
                stack.push(TK_PONTOVIRGULA);
                stack.push(R_DEC_VAR_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY);
                stack.push(R_TIPO);
                break;
            case R_DEC_VAR_I:
                stack.pop();
                stack.push(R_DEC_VAR_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY);
                stack.push(TK_VIRGULA);
                break;
            case R_DEC_VAR_II:
                stack.pop();
                stack.push(R_DEC_VAR_II);
                stack.push(TK_PONTOVIRGULA);
                stack.push(R_DEC_VAR_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY);
                stack.push(R_TIPO);
                break;
            case R_DEC_FUNC:
                stack.pop();
                stack.push(R_DEC_FUNC);
                stack.push(R_DEC_FUNC_I);
                stack.push(TK_FUNCAO);
                break;
            case R_DEC_FUNC_I:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                stack.push(TK_PARENTESE_F);
                stack.push(R_PARAMETROS);
                stack.push(TK_PARENTESE_A);
                stack.push(TK_ID);
                break;
            case R_DEC_FUNC_I_C2:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_RETORNO_FUNC);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                stack.push(TK_PARENTESE_F);
                stack.push(R_PARAMETROS);
                stack.push(TK_PARENTESE_A);
                stack.push(TK_ID);
                stack.push(R_TIPO);
                break;
            case R_PARAMETROS:
                stack.pop();
                stack.push(R_PARAMETROS_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY_PARAM);
                stack.push(R_TIPO);
                break;
            case R_PARAMETROS_I:
                stack.pop();
                stack.push(R_PARAMETROS_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY_PARAM);
                stack.push(R_TIPO);
                stack.push(TK_VIRGULA);
                break;
            case R_DEC_MAIN:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                stack.push(TK_PROGRAMA);
                break;
            case R_EXP_RELACIONAL_BOOL:
                stack.pop();
                stack.push(R_EXP_CONJUNTA_I);
                stack.push(R_EXP_CONJUNTA);
                break;
            case R_EXP_CONJUNTA:
                stack.pop();
                stack.push(R_EXP_RELACIONAL_I);
                stack.push(R_EXP_RELACIONAL);
                break;
            case R_EXP_CONJUNTA_I:
                stack.pop();
                stack.push(R_EXP_CONJUNTA_I);
                stack.push(R_EXP_CONJUNTA);
                stack.push(TK_OU);
                break;
            case R_EXP_RELACIONAL:
                stack.pop();
                stack.push(R_OPERAR_RELACIONALMENTE);
                stack.push(R_EXP_SIMPLES);
                stack.push(R_NOT_OPC);
                break;
            case R_EXP_RELACIONAL_I:
                stack.pop();
                stack.push(R_EXP_RELACIONAL_I);
                stack.push(R_EXP_RELACIONAL);
                stack.push(TK_E);
                break;
            case R_OPERAR_RELACIONALMENTE:
                stack.pop();
                stack.push(R_EXP_SIMPLES);
                stack.push(R_NOT_OPC);
                stack.push(R_OP_RELACIONAL);
                break;
            case R_OP_RELACIONAL:
                stack.pop();
                stack.push(TK_MAIOR);
                break;
            case R_OP_RELACIONAL_C2:
                stack.pop();
                stack.push(TK_MAIORIGUAL);
                break;
            case R_OP_RELACIONAL_C3:
                stack.pop();
                stack.push(TK_MENOR);
                break;
            case R_OP_RELACIONAL_C4:
                stack.pop();
                stack.push(TK_MENORIGUAL);
                break;
            case R_OP_RELACIONAL_C5:
                stack.pop();
                stack.push(TK_DIFERENTE);
                break;
            case R_NOT_OPC:
                stack.pop();
                stack.push(R_NOT_OPC);
                stack.push(TK_NAO);
                break;
            case R_EXP_SIMPLES:
                stack.pop();
                stack.push(R_TERMO_I);
                stack.push(R_TERMO);
                stack.push(R_OP_MAIS_MENOS);
                break;
            case R_EXP_SIMPLES_C2:
                stack.pop();
                stack.push(R_TERMO_I);
                stack.push(R_TERMO);
                break;
            case R_TERMO:
                stack.pop();
                stack.push(R_FATOR_I);
                stack.push(R_FATOR);
                break;
            case R_TERMO_I:
                stack.pop();
                stack.push(R_TERMO_I);
                stack.push(R_TERMO);
                stack.push(R_OP_MAIS_MENOS);
                break;
            case R_FATOR:
                stack.pop();
                stack.push(R_ID_FUNCAO_E_OUTROS);
                break;
            case R_FATOR_C2:
                stack.pop();
                stack.push(TK_NUMERO);
                break;
            case R_FATOR_C3:
                stack.pop();
                stack.push(TK_VERDADEIRO);
                break;
            case R_FATOR_C4:
                stack.pop();
                stack.push(TK_FALSO);
                break;
            case R_FATOR_C5:
                stack.pop();
                stack.push(TK_CADEIA_DE_CARACTERES);
                break;
            case R_FATOR_C6:
                stack.pop();
                stack.push(TK_CARACTERE_L);
                break;
            case R_FATOR_C7:
                stack.pop();
                stack.push(TK_PARENTESE_F);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_PARENTESE_A);
                break;
            case R_FATOR_I:
                stack.pop();
                stack.push(R_FATOR_I);
                stack.push(R_FATOR);
                stack.push(R_OP_MULTI_DIV);
                break;
            case R_OP_MAIS_MENOS:
                stack.pop();
                stack.push(TK_SOMA);
                break;
            case R_OP_MAIS_MENOS_C2:
                stack.pop();
                stack.push(TK_SUBTRACAO);
                break;
            case R_OP_MULTI_DIV:
                stack.pop();
                stack.push(TK_MULTIPLICACAO);
                break;
            case R_OP_MULTI_DIV_C2:
                stack.pop();
                stack.push(TK_DIVISAO);
                break;
            case R_ID_FUNCAO_E_OUTROS:
                stack.pop();
                stack.push(TK_ID);
                stack.push(TK_MAIOR);
                stack.push(TK_MAIOR);
                stack.push(R_ARRAY_I);
                stack.push(R_ARRAY_INDEXES);
                stack.push(TK_MENOR);
                stack.push(TK_MENOR);
                break;
            case R_ID_FUNCAO_E_OUTROS_C2:
                stack.pop();
                stack.push(R_ID_FUNCAO_E_OUTROS_DERIVADO);
                break;
            case R_ID_FUNCAO_E_OUTROS_DERIVADO:
                stack.pop();
                stack.push(R_POSSIBLE_FUNC);
                stack.push(TK_ID);
                break;
            case R_POSSIBLE_FUNC:
                stack.pop();
                stack.push(TK_PARENTESE_F);
                stack.push(R_PASSA_PARAM);
                stack.push(TK_PARENTESE_A);
                break;
            case R_RETORNO_FUNC:
                stack.pop();
                stack.push(TK_PONTOVIRGULA);
                stack.push(TK_MAIOR);
                stack.push(TK_IGUAL);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_MAIOR);
                stack.push(TK_IGUAL);
                break;
            case R_CORPO:
                stack.pop();
                stack.push(R_CORPO);
                stack.push(R_COMANDOS);
                break;
            case R_COMANDOS:
                stack.pop();
                stack.push(R_DEC_LEITURA);
                break;
            case R_COMANDOS_C2:
                stack.pop();
                stack.push(R_DEC_ESCRITA);
                break;
            case R_COMANDOS_C3:
                stack.pop();
                stack.push(R_DEC_SE);
                break;
            case R_COMANDOS_C4:
                stack.pop();
                stack.push(R_DEC_ENQUANTO);
                break;
            case R_COMANDOS_C5:
                stack.pop();
                stack.push(R_CHAMA_OU_ATRIBUI);
                break;
            case R_COMANDOS_C6:
                stack.pop();
                stack.push(R_DEC_VAR);
                break;
            case R_COMANDOS_C7:
                stack.pop();
                stack.push(R_NOVO_ESCOPO);
                break;
            case R_CHAMA_OU_ATRIBUI:
                stack.pop();
                stack.push(R_WHOS_NEXT);
                stack.push(TK_ID);
                break;
            case R_CHAMA_OU_ATRIBUI_C2:
                stack.pop();
                stack.push(TK_PONTOVIRGULA);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_IGUAL);
                stack.push(TK_ID);
                stack.push(TK_MAIOR);
                stack.push(TK_MAIOR);
                stack.push(R_ARRAY_I);
                stack.push(R_ARRAY_INDEXES);
                stack.push(TK_MENOR);
                stack.push(TK_MENOR);
                break;
            case R_WHOS_NEXT:
                stack.pop();
                stack.push(TK_PONTOVIRGULA);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_IGUAL);
                break;
            case R_WHOS_NEXT_C2:
                stack.pop();
                stack.push(TK_PONTOVIRGULA);
                stack.push(TK_PARENTESE_F);
                stack.push(R_PASSA_PARAM);
                stack.push(TK_PARENTESE_A);
                break;
            case R_NOVO_ESCOPO:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                break;
            case R_ARRAY:
                stack.pop();
                stack.push(TK_MAIOR);
                stack.push(TK_MAIOR);
                stack.push(R_ARRAY_I);
                stack.push(R_ARRAY_INDEXES);
                stack.push(TK_MENOR);
                stack.push(TK_MENOR);
                break;
            case R_ARRAY_I:
                stack.pop();
                stack.push(R_ARRAY_I);
                stack.push(R_ARRAY_INDEXES);
                stack.push(TK_VIRGULA);
                break;
            case R_ARRAY_INDEXES:
                stack.pop();
                stack.push(R_EXP_SIMPLES);
                break;
            case R_ARRAY_PARAM:
                stack.pop();
                stack.push(TK_MAIOR);
                stack.push(TK_MAIOR);
                stack.push(R_ARRAY_PARAM_I);
                stack.push(R_ARRAY_INDEXES_OPT);
                stack.push(TK_MENOR);
                stack.push(TK_MENOR);
                break;
            case R_ARRAY_PARAM_I:
                stack.pop();
                stack.push(R_ARRAY_PARAM_I);
                stack.push(R_ARRAY_INDEXES_OPT);
                stack.push(TK_VIRGULA);
                break;
            case R_ARRAY_INDEXES_OPT:
                stack.pop();
                stack.push(R_ARRAY_INDEXES);
                break;
            case R_DEC_LEITURA:
                stack.pop();
                stack.push(TK_PONTOVIRGULA);
                stack.push(TK_PARENTESE_F);
                stack.push(R_LEITURA_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY);
                stack.push(TK_PARENTESE_A);
                stack.push(TK_LEIA);
                break;
            case R_LEITURA_I:
                stack.pop();
                stack.push(R_LEITURA_I);
                stack.push(TK_ID);
                stack.push(R_ARRAY);
                stack.push(TK_VIRGULA);
                break;
            case R_DEC_ESCRITA:
                stack.pop();
                stack.push(TK_PONTOVIRGULA);
                stack.push(TK_PARENTESE_F);
                stack.push(R_ESCREVIVEL_I);
                stack.push(R_ESCREVIVEL);
                stack.push(TK_PARENTESE_A);
                stack.push(TK_ESCREVA);
                break;
            case R_ESCREVIVEL_I:
                stack.pop();
                stack.push(R_ESCREVIVEL_I);
                stack.push(R_ESCREVIVEL);
                stack.push(TK_VIRGULA);
                break;
            case R_ESCREVIVEL:
                stack.pop();
                stack.push(R_TERMO_I_E);
                stack.push(R_TERMO_E);
                stack.push(R_OP_MAIS_MENOS);
                break;
            case R_ESCREVIVEL_C2:
                stack.pop();
                stack.push(R_TERMO_I_E);
                stack.push(R_TERMO_E);
                break;
            case R_TERMO_E:
                stack.pop();
                stack.push(R_FATOR_I_E);
                stack.push(R_FATOR_E);
                break;
            case R_TERMO_I_E:
                stack.pop();
                stack.push(R_TERMO_I_E);
                stack.push(R_TERMO_E);
                stack.push(R_OP_MAIS_MENOS);
                break;
            case R_FATOR_E:
                stack.pop();
                stack.push(TK_ID);
                stack.push(R_ARRAY);
                break;
            case R_FATOR_E_C2:
                stack.pop();
                stack.push(TK_NUMERO);
                break;
            case R_FATOR_E_C3:
                stack.pop();
                stack.push(TK_CADEIA_DE_CARACTERES);
                break;
            case R_FATOR_E_C4:
                stack.pop();
                stack.push(TK_CARACTERE_L);
                break;
            case R_FATOR_E_C5:
                stack.pop();
                stack.push(TK_PARENTESE_F);
                stack.push(R_ESCREVIVEL);
                stack.push(TK_PARENTESE_A);
                break;
            case R_FATOR_I_E:
                stack.pop();
                stack.push(R_FATOR_I_E);
                stack.push(R_FATOR_E);
                stack.push(R_OP_MULTI_DIV);
                break;
            case R_DEC_SE:
                stack.pop();
                stack.push(R_ELSE_OPC);
                stack.push(TK_FIM);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                stack.push(TK_ENTAO);
                stack.push(TK_PARENTESE_F);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_PARENTESE_A);
                stack.push(TK_SE);
                break;
            case R_ELSE_OPC:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                stack.push(TK_SENAO);
                break;
            case R_DEC_ENQUANTO:
                stack.pop();
                stack.push(TK_FIM);
                stack.push(R_CORPO);
                stack.push(TK_INICIO);
                stack.push(TK_FACA);
                stack.push(TK_PARENTESE_F);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_PARENTESE_A);
                stack.push(TK_ENQUANTO);
                break;
            case R_PASSA_PARAM:
                stack.pop();
                stack.push(R_PASSA_PARAM_I);
                stack.push(R_EXP_RELACIONAL_BOOL);
                break;
            case R_PASSA_PARAM_I:
                stack.pop();
                stack.push(R_PASSA_PARAM_I);
                stack.push(R_EXP_RELACIONAL_BOOL);
                stack.push(TK_VIRGULA);
                break;
            case R_EPSILON:
                stack.pop();
                break;
            case R_TIPO:
                stack.pop();
                stack.push(TK_INTEIRO);
                break;
            case R_TIPO_C2:
                stack.pop();
                stack.push(TK_REAL);
                break;
            case R_TIPO_C3:
                stack.pop();
                stack.push(TK_BOOLEANO);
                break;
            case R_TIPO_C4:
                stack.pop();
                stack.push(TK_CADEIA);
                break;
            case R_TIPO_C5:
                stack.pop();
                stack.push(TK_CARACTERE);
                break;
            default:
                System.err.println("Entrou no Default de gerarProducao com o valor: " + valor);
                break;
        }
    }

}
