package Util;

import java.util.ArrayList;
import java.util.List;

public class SincronizadorSintaticoFollow implements Dicionario{
	private static List<Integer> programa, inicio_const_var_func, dec_const_var_derivada, inicio_var_func, inicio_func, dec_const, dec_const_continuo, dec_const_i,
	dec_const_ii, dec_var, dec_var_continuo, dec_var_i, dec_var_ii, dec_func, dec_func_i, parametros, parametros_i, dec_main, exp_relacional_bool,
	exp_conjunta, exp_conjunta_i, exp_relacional, exp_relacional_i, operar_relacionalmente, op_relacional, not_opc, exp_simples, termo, termo_i, fator, fator_i,
	op_mais_menos, op_multi_div, id_funcao_e_outros,	id_funcao_e_outros_derivado, possible_func, retorno_func, corpo, comandos, chama_ou_atribui, whos_next,
	novo_escopo, array, array_i, array_indexes, array_param, array_param_i, array_indexes_opt, dec_leitura,	leitura_i, dec_escrita,	escrevivel_i,	escrevivel,
	termo_e, termo_i_e, fator_e, fator_i_e, dec_se, else_opc, dec_enquanto, passa_param, passa_param_i, tipo;

	public static List<Integer> getFollows(int regra){
		switch (regra){
			case R_PROGRAMA_C3 :
			case R_PROGRAMA_C2 :
			case R_PROGRAMA : return r_programa();

			case R_INICIO_CONST_VAR_FUNC: return r_inicio_const_var_func();

			case R_DEC_CONST_VAR_DERIVADA_C2:
			case R_DEC_CONST_VAR_DERIVADA: return r_dec_const_var_derivada();

			case R_INICIO_VAR_FUNC: return r_inicio_var_func();

			case R_INICIO_FUNC: return r_inicio_func();

			case R_DEC_CONST: return r_dec_const();

			case R_DEC_CONST_CONTINUO: return r_dec_const_continuo();

			case R_DEC_CONST_I: return r_dec_const_i();

			case R_DEC_CONST_II: return r_dec_const_ii();

			case R_DEC_VAR: return r_dec_var();

			case R_DEC_VAR_CONTINUO: return r_dec_var_continuo();

			case R_DEC_VAR_I: return r_dec_var_i();

			case R_DEC_VAR_II: return r_dec_var_ii();

			case R_DEC_FUNC: return r_dec_func();

			case R_DEC_FUNC_I_C2:
			case R_DEC_FUNC_I: return r_dec_func_i();

			case R_PARAMETROS: return r_parametros();

			case R_PARAMETROS_I: return r_parametros_i();

			case R_DEC_MAIN: return r_dec_main();

			case R_EXP_RELACIONAL_BOOL: return r_exp_relacional_bool();

			case R_EXP_CONJUNTA: return r_exp_conjunta();

			case R_EXP_CONJUNTA_I: return r_exp_conjunta_i();

			case R_EXP_RELACIONAL: return r_exp_relacional();

			case R_EXP_RELACIONAL_I: return r_exp_relacional_i();

			case R_OPERAR_RELACIONALMENTE: return r_operar_relacionalmente();

			case R_OP_RELACIONAL_C5:
			case R_OP_RELACIONAL_C4:
			case R_OP_RELACIONAL_C3:
			case R_OP_RELACIONAL_C2:
			case R_OP_RELACIONAL: return r_op_relacional();

			case R_NOT_OPC: return r_not_opc();

			case R_EXP_SIMPLES_C2:
			case R_EXP_SIMPLES: return r_exp_simples();

			case R_TERMO: return r_termo();

			case R_TERMO_I: return r_termo_i();

			case R_FATOR_C7:
			case R_FATOR_C6:
			case R_FATOR_C5:
			case R_FATOR_C4:
			case R_FATOR_C3:
			case R_FATOR_C2:
			case R_FATOR: return r_fator();

			case R_FATOR_I: return r_fator_i();

			case R_OP_MAIS_MENOS_C2:
			case R_OP_MAIS_MENOS: return r_op_mais_menos();

			case R_OP_MULTI_DIV_C2:
			case R_OP_MULTI_DIV: return r_op_multi_div();

			case R_ID_FUNCAO_E_OUTROS_C2:
			case R_ID_FUNCAO_E_OUTROS: return r_id_funcao_e_outros();

			case R_ID_FUNCAO_E_OUTROS_DERIVADO: return r_id_funcao_e_outros_derivado();

			case R_POSSIBLE_FUNC: return r_possible_func();

			case R_RETORNO_FUNC: return r_retorno_func();

			case R_CORPO: return r_corpo();

			case R_COMANDOS_C7:
			case R_COMANDOS_C6:
			case R_COMANDOS_C5:
			case R_COMANDOS_C4:
			case R_COMANDOS_C3:
			case R_COMANDOS_C2:
			case R_COMANDOS: return r_comandos();


			case R_CHAMA_OU_ATRIBUI_C2:
			case R_CHAMA_OU_ATRIBUI: return r_chama_ou_atribui();

			case R_WHOS_NEXT_C2:
			case R_WHOS_NEXT: return r_whos_next();

			case R_NOVO_ESCOPO: return r_novo_escopo();

			case R_ARRAY: return r_array();

			case R_ARRAY_I: return r_array_i();

			case R_ARRAY_INDEXES: return r_array_indexes();

			case R_ARRAY_PARAM: return r_array_param();

			case R_ARRAY_PARAM_I: return r_array_param_i();

			case R_ARRAY_INDEXES_OPT: return r_array_indexes_opt();

			case R_DEC_LEITURA: return r_dec_leitura();

			case R_LEITURA_I: return r_leitura_i();

			case R_DEC_ESCRITA: return r_dec_escrita();

			case R_ESCREVIVEL_I: return r_escrevivel_i();

			case R_ESCREVIVEL_C2:
			case R_ESCREVIVEL: return r_escrevivel();

			case R_TERMO_E: return r_termo_e();

			case R_TERMO_I_E: return r_termo_i_e();

			case R_FATOR_E_C5:
			case R_FATOR_E_C4:
			case R_FATOR_E_C3:
			case R_FATOR_E_C2:
			case R_FATOR_E: return r_fator_e();

			case R_FATOR_I_E: return r_fator_i_e();

			case R_DEC_SE: return r_dec_se();

			case R_ELSE_OPC: return r_else_opc();

			case R_DEC_ENQUANTO: return r_dec_enquanto();

			case R_PASSA_PARAM: return r_passa_param();

			case R_PASSA_PARAM_I: return r_passa_param_i();

			case R_TIPO_C5:
			case R_TIPO_C4:
			case R_TIPO_C3:
			case R_TIPO_C2:
			case R_TIPO: return r_tipo();

			case R_EPSILON:
			default: return null;
		}
	}

	private static List<Integer> r_programa() {
		if(programa == null){
			programa = new ArrayList<Integer>();
			programa.add(TK_EOF);
		}
		return programa;
	}

	private static List<Integer> r_inicio_const_var_func() {
		if(inicio_const_var_func == null){
			inicio_const_var_func = new ArrayList<Integer>();
			inicio_const_var_func.add(TK_EOF);
		}
		return inicio_const_var_func;
	}

	private static List<Integer> r_dec_const_var_derivada() {
		if(dec_const_var_derivada == null){
			dec_const_var_derivada = new ArrayList<Integer>();
			dec_const_var_derivada.add(TK_EOF);
		}
		return dec_const_var_derivada;
	}

	private static List<Integer> r_inicio_var_func() {
		if(inicio_var_func == null){
			inicio_var_func = new ArrayList<Integer>();
			inicio_var_func.add(TK_EOF);
		}
		return inicio_var_func;
	}

	private static List<Integer> r_inicio_func() {
		if(inicio_func == null){
			inicio_func = new ArrayList<Integer>();
			inicio_func.add(TK_EOF);
		}
		return inicio_func;
	}

	private static List<Integer> r_dec_const() {
		if(dec_const == null){
			dec_const = new ArrayList<Integer>();
			dec_const.add(TK_FUNCAO);
			dec_const.add(TK_PROGRAMA);
			dec_const.add(TK_VAR);
		}
		return dec_const;
	}

	private static List<Integer> r_dec_const_continuo() {
		if(dec_const_continuo == null){
			dec_const_continuo = new ArrayList<Integer>();
			dec_const_continuo.add(TK_FIM);
		}
		return dec_const_continuo;
	}

	private static List<Integer> r_dec_const_i() {
		if(dec_const_i == null){
			dec_const_i = new ArrayList<Integer>();
			dec_const_i.add(TK_PONTOVIRGULA);
		}
		return dec_const_i;
	}

	private static List<Integer> r_dec_const_ii() {
		if(dec_const_ii == null){
			dec_const_ii = new ArrayList<Integer>();
			dec_const_ii.add(TK_FIM);
		}
		return dec_const_ii;
	}

	private static List<Integer> r_dec_var() {
		if(dec_var == null){
			dec_var = new ArrayList<Integer>();
			dec_var.add(TK_FUNCAO);
			dec_var.add(TK_PROGRAMA);
			dec_var.add(TK_LEIA);
			dec_var.add(TK_INICIO);
			dec_var.add(TK_VAR);
			dec_var.add(TK_MENOR);
			dec_var.add(TK_ESCREVA);
			dec_var.add(TK_IGUAL);
			dec_var.add(TK_SE);
			dec_var.add(TK_ENQUANTO);
			dec_var.add(TK_FIM);
			dec_var.add(TK_ID);
		}
		return dec_var;
	}

	private static List<Integer> r_dec_var_continuo() {
		if(dec_var_continuo == null){
			dec_var_continuo = new ArrayList<Integer>();
			dec_var_continuo.add(TK_FIM);
		}
		return dec_var_continuo;
	}

	private static List<Integer> r_dec_var_i() {
		if(dec_var_i == null){
			dec_var_i = new ArrayList<Integer>();
			dec_var_i.add(TK_PONTOVIRGULA);
		}
		return dec_var_i;
	}

	private static List<Integer> r_dec_var_ii() {
		if(dec_var_ii == null){
			dec_var_ii = new ArrayList<Integer>();
			dec_var_ii.add(TK_FIM);
		}
		return dec_var_ii;
	}

	private static List<Integer> r_dec_func() {
		if(dec_func == null){
			dec_func = new ArrayList<Integer>();
			dec_func.add(TK_PROGRAMA);
		}
		return dec_func;
	}

	private static List<Integer> r_dec_func_i() {
		if(dec_func_i == null){
			dec_func_i = new ArrayList<Integer>();
			dec_func_i.add(TK_FUNCAO);
			dec_func_i.add(TK_PROGRAMA);
		}
		return dec_func_i;
	}

	private static List<Integer> r_parametros_i() {
		if(parametros_i == null){
			parametros_i = new ArrayList<Integer>();
			parametros_i.add(TK_PARENTESE_A);
		}
		return parametros_i;
	}

	private static List<Integer> r_dec_main() {
		if(dec_main == null){
			dec_main = new ArrayList<Integer>();
			dec_main.add(TK_EOF);
		}
		return dec_main;
	}

	private static List<Integer> r_exp_relacional_bool() {
		if(exp_relacional_bool == null){
			exp_relacional_bool = new ArrayList<Integer>();
			exp_relacional_bool.add(TK_PONTOVIRGULA);
			exp_relacional_bool.add(TK_PARENTESE_A);
			exp_relacional_bool.add(TK_IGUAL);
			exp_relacional_bool.add(TK_VIRGULA);
		}
		return exp_relacional_bool;
	}

	private static List<Integer> r_exp_conjunta() {
		if(exp_conjunta == null){
			exp_conjunta = new ArrayList<Integer>();
			exp_conjunta.add(TK_OU);
			exp_conjunta.add(TK_PONTOVIRGULA);
			exp_conjunta.add(TK_PARENTESE_A);
			exp_conjunta.add(TK_IGUAL);
			exp_conjunta.add(TK_VIRGULA);
		}
		return exp_conjunta;
	}

	private static List<Integer> r_exp_conjunta_i() {
		if(exp_conjunta_i == null){
			exp_conjunta_i = new ArrayList<Integer>();
			exp_conjunta_i.add(TK_PONTOVIRGULA);
			exp_conjunta_i.add(TK_PARENTESE_A);
			exp_conjunta_i.add(TK_IGUAL);
			exp_conjunta_i.add(TK_VIRGULA);
		}
		return exp_conjunta_i;
	}

	private static List<Integer> r_exp_relacional() {
		if(exp_relacional == null){
			exp_relacional = new ArrayList<Integer>();
			exp_relacional.add(TK_OU);
			exp_relacional.add(TK_PONTOVIRGULA);
			exp_relacional.add(TK_E);
			exp_relacional.add(TK_PARENTESE_A);
			exp_relacional.add(TK_IGUAL);
			exp_relacional.add(TK_VIRGULA);
		}
		return exp_relacional;
	}

	private static List<Integer> r_exp_relacional_i() {
		if(exp_relacional_i == null){
			exp_relacional_i = new ArrayList<Integer>();
			exp_relacional_i.add(TK_OU);
			exp_relacional_i.add(TK_PONTOVIRGULA);
			exp_relacional_i.add(TK_PARENTESE_A);
			exp_relacional_i.add(TK_IGUAL);
			exp_relacional_i.add(TK_VIRGULA);
		}
		return exp_relacional_i;
	}

	private static List<Integer> r_operar_relacionalmente() {
		if(operar_relacionalmente == null){
			operar_relacionalmente = new ArrayList<Integer>();
			operar_relacionalmente.add(TK_OU);
			operar_relacionalmente.add(TK_PONTOVIRGULA);
			operar_relacionalmente.add(TK_E);
			operar_relacionalmente.add(TK_PARENTESE_A);
			operar_relacionalmente.add(TK_IGUAL);
			operar_relacionalmente.add(TK_VIRGULA);
		}
		return operar_relacionalmente;
	}

	private static List<Integer> r_op_relacional() {
		if(op_relacional == null){
			//TODO TENHO QUASE CERTEZA QUE O NOME DOS TOKENS ME CONFUNDIU, TESTA AI
			op_relacional = new ArrayList<Integer>();
			op_relacional.add(TK_NUMERO);
			op_relacional.add(TK_BOOLEANO);
			op_relacional.add(TK_CADEIA_DE_CARACTERES);
			op_relacional.add(TK_CARACTERE_L);
			op_relacional.add(TK_ID);
			
			op_relacional.add(TK_SUBTRACAO);
			op_relacional.add(TK_MENOR);
			op_relacional.add(TK_NAO);
			op_relacional.add(TK_SOMA);
			op_relacional.add(TK_PARENTESE_F);
		}
		return op_relacional;
	}

	private static List<Integer> r_not_opc() {
		if(not_opc == null){
			not_opc = new ArrayList<Integer>();
			//TODO TENHO QUASE CERTEZA QUE O NOME DOS TOKENS ME CONFUNDIU, TESTA AI
			not_opc.add(TK_NUMERO);
			not_opc.add(TK_BOOLEANO);
			not_opc.add(TK_CARACTERE_L);
			not_opc.add(TK_CADEIA_DE_CARACTERES);
			not_opc.add(TK_ID);

			not_opc.add(TK_SUBTRACAO);
			not_opc.add(TK_MENOR);
			not_opc.add(TK_SOMA);
			not_opc.add(TK_PARENTESE_F);
		}
		return not_opc;
	}

	private static List<Integer> r_exp_simples() {
		if(exp_simples == null){
			exp_simples = new ArrayList<Integer>();
			exp_simples.add(TK_MAIORIGUAL);
			exp_simples.add(TK_DIFERENTE);
			exp_simples.add(TK_OU);
			exp_simples.add(TK_PONTOVIRGULA);
			exp_simples.add(TK_MENOR);
			exp_simples.add(TK_E);
			exp_simples.add(TK_MAIOR);
			exp_simples.add(TK_PARENTESE_A);
			exp_simples.add(TK_IGUAL);
			exp_simples.add(TK_MENORIGUAL);
			exp_simples.add(TK_VIRGULA);
		}
		return exp_simples;
	}

	private static List<Integer> r_termo() {
		if(termo == null){
			termo = new ArrayList<Integer>();
			termo.add(TK_MAIORIGUAL);
			termo.add(TK_DIFERENTE);
			termo.add(TK_OU);
			termo.add(TK_PONTOVIRGULA);
			termo.add(TK_MAIOR);
			termo.add(TK_MENOR);
			termo.add(TK_PARENTESE_A);
			termo.add(TK_IGUAL);
			termo.add(TK_SUBTRACAO);
			termo.add(TK_E);
			termo.add(TK_MENORIGUAL);
			termo.add(TK_VIRGULA);
			termo.add(TK_SOMA);
		}
		return termo;
	}

	private static List<Integer> r_termo_i() {
		if(termo_i == null){
			termo_i = new ArrayList<Integer>();
			termo_i.add(TK_MAIOR);
			termo_i.add(TK_DIFERENTE);
			termo_i.add(TK_OU);
			termo_i.add(TK_PONTOVIRGULA);
			termo_i.add(TK_MENOR);
			termo_i.add(TK_E);
			termo_i.add(TK_MAIOR);
			termo_i.add(TK_PARENTESE_A);
			termo_i.add(TK_IGUAL);
			termo_i.add(TK_MENORIGUAL);
			termo_i.add(TK_VIRGULA);
		}
		return termo_i;
	}

	private static List<Integer> r_fator() {
		if(fator == null){
			fator = new ArrayList<Integer>();
			fator.add(TK_MAIORIGUAL);
			fator.add(TK_DIFERENTE);
			fator.add(TK_OU);
			fator.add(TK_PONTOVIRGULA);
			fator.add(TK_MENOR);
			fator.add(TK_MULTIPLICACAO);
			fator.add(TK_MAIOR);
			fator.add(TK_PARENTESE_A);
			fator.add(TK_IGUAL);
			fator.add(TK_DIVISAO);
			fator.add(TK_SUBTRACAO);
			fator.add(TK_E);
			fator.add(TK_MENORIGUAL);
			fator.add(TK_VIRGULA);
			fator.add(TK_SOMA);
		}
		return fator;
	}

	private static List<Integer> r_fator_i() {
		if(fator_i == null){
			fator_i = new ArrayList<Integer>();
			fator_i.add(TK_MAIORIGUAL);
			fator_i.add(TK_DIFERENTE);
			fator_i.add(TK_OU);
			fator_i.add(TK_PONTOVIRGULA);
			fator_i.add(TK_MENOR);
			fator_i.add(TK_MAIOR);
			fator_i.add(TK_PARENTESE_A);
			fator_i.add(TK_IGUAL);
			fator_i.add(TK_SUBTRACAO);
			fator_i.add(TK_E);
			fator_i.add(TK_MENORIGUAL);
			fator_i.add(TK_VIRGULA);
			fator_i.add(TK_SOMA);
		}
		return fator_i;
	}

	private static List<Integer> r_op_mais_menos() {
		if(op_mais_menos == null){
			op_mais_menos = new ArrayList<Integer>();
			//TODO TENHO QUASE CERTEZA QUE O NOME DOS TOKENS ME CONFUNDIU, TESTA AI
			op_mais_menos.add(TK_NUMERO);
			op_mais_menos.add(TK_BOOLEANO);
			op_mais_menos.add(TK_CARACTERE_L);
			op_mais_menos.add(TK_CADEIA_DE_CARACTERES);
			op_mais_menos.add(TK_ID);

			op_mais_menos.add(TK_MENOR);
			op_mais_menos.add(TK_PARENTESE_F);
		}
		return op_mais_menos;
	}

	private static List<Integer> r_op_multi_div() {
		if(op_multi_div == null){
			op_multi_div = new ArrayList<Integer>();
			//TODO TENHO QUASE CERTEZA QUE O NOME DOS TOKENS ME CONFUNDIU, TESTA AI
			op_multi_div.add(TK_NUMERO);
			op_multi_div.add(TK_BOOLEANO);
			op_multi_div.add(TK_CARACTERE_L);
			op_multi_div.add(TK_CADEIA_DE_CARACTERES);
			op_multi_div.add(TK_ID);
			
			op_multi_div.add(TK_MENOR);
			op_multi_div.add(TK_PARENTESE_F);
		}
		return op_multi_div;
	}

	private static List<Integer> r_id_funcao_e_outros_derivado() {
		if(id_funcao_e_outros_derivado == null){
			id_funcao_e_outros_derivado = new ArrayList<Integer>();
			id_funcao_e_outros_derivado.add(TK_MAIORIGUAL);
			id_funcao_e_outros_derivado.add(TK_MAIOR);
			id_funcao_e_outros_derivado.add(TK_DIFERENTE);
			id_funcao_e_outros_derivado.add(TK_OU);
			id_funcao_e_outros_derivado.add(TK_PONTOVIRGULA);
			id_funcao_e_outros_derivado.add(TK_MENOR);
			id_funcao_e_outros_derivado.add(TK_MULTIPLICACAO);
			id_funcao_e_outros_derivado.add(TK_PARENTESE_A);
			id_funcao_e_outros_derivado.add(TK_IGUAL);
			id_funcao_e_outros_derivado.add(TK_DIVISAO);
			id_funcao_e_outros_derivado.add(TK_SUBTRACAO);
			id_funcao_e_outros_derivado.add(TK_E);
			id_funcao_e_outros_derivado.add(TK_MENORIGUAL);
			id_funcao_e_outros_derivado.add(TK_VIRGULA);
			id_funcao_e_outros_derivado.add(TK_SOMA);
		}
		return id_funcao_e_outros_derivado;
	}

	private static List<Integer> r_id_funcao_e_outros() {
		if(id_funcao_e_outros == null){
			id_funcao_e_outros = new ArrayList<Integer>();
			id_funcao_e_outros.add(TK_MAIORIGUAL);
			id_funcao_e_outros.add(TK_DIFERENTE);
			id_funcao_e_outros.add(TK_OU);
			id_funcao_e_outros.add(TK_PONTOVIRGULA);
			id_funcao_e_outros.add(TK_MENOR);
			id_funcao_e_outros.add(TK_MULTIPLICACAO);
			id_funcao_e_outros.add(TK_MAIOR);
			id_funcao_e_outros.add(TK_PARENTESE_A);
			id_funcao_e_outros.add(TK_IGUAL);
			id_funcao_e_outros.add(TK_DIVISAO);
			id_funcao_e_outros.add(TK_SUBTRACAO);
			id_funcao_e_outros.add(TK_E);
			id_funcao_e_outros.add(TK_MENORIGUAL);
			id_funcao_e_outros.add(TK_VIRGULA);
			id_funcao_e_outros.add(TK_SOMA);
		}
		return id_funcao_e_outros;
	}

	private static List<Integer> r_possible_func() {
		if(possible_func == null){
			possible_func = new ArrayList<Integer>();
			possible_func.add(TK_MAIORIGUAL);
			possible_func.add(TK_MAIOR);
			possible_func.add(TK_DIFERENTE);
			possible_func.add(TK_OU);
			possible_func.add(TK_PONTOVIRGULA);
			possible_func.add(TK_MENOR);
			possible_func.add(TK_MULTIPLICACAO);
			possible_func.add(TK_PARENTESE_A);
			possible_func.add(TK_IGUAL);
			possible_func.add(TK_DIVISAO);
			possible_func.add(TK_SUBTRACAO);
			possible_func.add(TK_E);
			possible_func.add(TK_MENORIGUAL);
			possible_func.add(TK_VIRGULA);
			possible_func.add(TK_SOMA);
		}
		return possible_func;
	}

	private static List<Integer> r_retorno_func() {
		if(retorno_func == null){
			retorno_func = new ArrayList<Integer>();
			retorno_func.add(TK_FIM);
		}
		return retorno_func;
	}

	private static List<Integer> r_corpo() {
		if(corpo == null){
			corpo = new ArrayList<Integer>();
			corpo.add(TK_IGUAL);
			corpo.add(TK_FIM);
		}
		return corpo;
	}

	private static List<Integer> r_comandos() {
		if(comandos == null){
			comandos = new ArrayList<Integer>();
			comandos.add(TK_LEIA);
			comandos.add(TK_INICIO);
			comandos.add(TK_VAR);
			comandos.add(TK_MENOR);
			comandos.add(TK_ESCREVA);
			comandos.add(TK_IGUAL);
			comandos.add(TK_SE);
			comandos.add(TK_ENQUANTO);
			comandos.add(TK_FIM);
			comandos.add(TK_ID);
		}
		return comandos;
	}

	private static List<Integer> r_chama_ou_atribui() {
		if(chama_ou_atribui == null) {
			chama_ou_atribui = new ArrayList<Integer>();
			chama_ou_atribui.add(TK_LEIA);
			chama_ou_atribui.add(TK_INICIO);
			chama_ou_atribui.add(TK_VAR);
			chama_ou_atribui.add(TK_MENOR);
			chama_ou_atribui.add(TK_ESCREVA);
			chama_ou_atribui.add(TK_IGUAL);
			chama_ou_atribui.add(TK_SE);
			chama_ou_atribui.add(TK_ENQUANTO);
			chama_ou_atribui.add(TK_FIM);
			chama_ou_atribui.add(TK_ID);
		}
		return chama_ou_atribui;
	}

	private static List<Integer> r_whos_next() {
		if(whos_next == null){
			whos_next = new ArrayList<Integer>();
			whos_next.add(TK_LEIA);
			whos_next.add(TK_INICIO);
			whos_next.add(TK_VAR);
			whos_next.add(TK_MENOR);
			whos_next.add(TK_ESCREVA);
			whos_next.add(TK_IGUAL);
			whos_next.add(TK_SE);
			whos_next.add(TK_ENQUANTO);
			whos_next.add(TK_FIM);
			whos_next.add(TK_ID);
		}
		return whos_next;
	}

	private static List<Integer> r_novo_escopo() {
		if(novo_escopo == null) {
			novo_escopo = new ArrayList<Integer>();
			novo_escopo.add(TK_LEIA);
			novo_escopo.add(TK_INICIO);
			novo_escopo.add(TK_VAR);
			novo_escopo.add(TK_MENOR);
			novo_escopo.add(TK_ESCREVA);
			novo_escopo.add(TK_IGUAL);
			novo_escopo.add(TK_SE);
			novo_escopo.add(TK_ENQUANTO);
			novo_escopo.add(TK_FIM);
			novo_escopo.add(TK_ID);
		}
		return novo_escopo;
	}

	private static List<Integer> r_array() {
		if(array == null){
			array = new ArrayList<Integer>();
			array.add(TK_ID);
		}
		return array;
	}

	private static List<Integer> r_array_i() {
		if(array_i == null){
			array_i = new ArrayList<Integer>();
			array_i.add(TK_MAIOR);
		}
		return array_i;
	}

	private static List<Integer> r_array_indexes() {
		if(array_indexes == null) {
			array_indexes = new ArrayList<Integer>();
			array_indexes.add(TK_MAIOR);
			array_indexes.add(TK_VIRGULA);
		}
		return array_indexes;
	}

	private static List<Integer> r_array_param() {
		if(array_param == null){
			array_param = new ArrayList<Integer>();
			array_param.add(TK_ID);
		}
		return array_param;
	}

	private static List<Integer> r_array_param_i() {
		if(array_param_i == null){
			array_param_i = new ArrayList<Integer>();
			array_param_i.add(TK_MAIOR);
		}
		return array_param_i;
	}

	private static List<Integer> r_array_indexes_opt() {
		if(array_indexes_opt == null){
			array_indexes_opt = new ArrayList<Integer>();
			array_indexes_opt.add(TK_MAIOR);
			array_indexes_opt.add(TK_VIRGULA);
		}
		return array_indexes_opt;
	}

	private static List<Integer> r_dec_leitura() {
		if(dec_leitura == null){
			dec_leitura = new ArrayList<Integer>();
			dec_leitura.add(TK_LEIA);
			dec_leitura.add(TK_INICIO);
			dec_leitura.add(TK_VAR);
			dec_leitura.add(TK_MENOR);
			dec_leitura.add(TK_ESCREVA);
			dec_leitura.add(TK_IGUAL);
			dec_leitura.add(TK_SE);
			dec_leitura.add(TK_ENQUANTO);
			dec_leitura.add(TK_FIM);
			dec_leitura.add(TK_ID);
		}
		return dec_leitura;
	}

	private static List<Integer> r_leitura_i() {
		if(leitura_i == null){
			leitura_i = new ArrayList<Integer>();
			leitura_i.add(TK_PARENTESE_A);
		}
		return leitura_i;
	}

	private static List<Integer> r_dec_escrita() {
		if(dec_escrita == null){
			dec_escrita = new ArrayList<Integer>();
			dec_escrita.add(TK_LEIA);
			dec_escrita.add(TK_INICIO);
			dec_escrita.add(TK_VAR);
			dec_escrita.add(TK_MENOR);
			dec_escrita.add(TK_ESCREVA);
			dec_escrita.add(TK_IGUAL);
			dec_escrita.add(TK_SE);
			dec_escrita.add(TK_ENQUANTO);
			dec_escrita.add(TK_FIM);
			dec_escrita.add(TK_ID);
		}
		return dec_escrita;
	}

	private static List<Integer> r_escrevivel_i() {
		if(escrevivel_i == null){
			escrevivel_i = new ArrayList<Integer>();
			escrevivel_i.add(TK_PARENTESE_A);
		}
		return escrevivel_i;
	}

	private static List<Integer> r_escrevivel() {
		if(escrevivel == null){
			escrevivel = new ArrayList<Integer>();
			escrevivel.add(TK_PARENTESE_A);
			escrevivel.add(TK_VIRGULA);
		}
		return escrevivel;
	}

	private static List<Integer> r_termo_e() {
		if(termo_e == null){
			termo_e = new ArrayList<Integer>();
			termo_e.add(TK_SUBTRACAO);
			termo_e.add(TK_PARENTESE_A);
			termo_e.add(TK_VIRGULA);
			termo_e.add(TK_SOMA);
		}
		return termo_e;
	}

	private static List<Integer> r_termo_i_e() {
		if(termo_i_e == null){
			termo_i_e = new ArrayList<Integer>();
			termo_i_e.add(TK_PARENTESE_A);
			termo_i_e.add(TK_VIRGULA);
		}
		return termo_i_e;
	}

	private static List<Integer> r_fator_e() {
		if(fator_e == null){
			fator_e = new ArrayList<Integer>();
			fator_e.add(TK_DIVISAO);
			fator_e.add(TK_SUBTRACAO);
			fator_e.add(TK_MULTIPLICACAO);
			fator_e.add(TK_PARENTESE_A);
			fator_e.add(TK_VIRGULA);
			fator_e.add(TK_SOMA);
		}
		return fator_e;
	}

	private static List<Integer> r_fator_i_e() {
		if(fator_i_e == null){
			fator_i_e = new ArrayList<Integer>();
			fator_i_e.add(TK_SUBTRACAO);
			fator_i_e.add(TK_PARENTESE_A);
			fator_i_e.add(TK_VIRGULA);
			fator_i_e.add(TK_SOMA);
		}
		return fator_i_e;
	}

	private static List<Integer> r_dec_se() {
		if(dec_se == null){
			dec_se = new ArrayList<Integer>();
			dec_se.add(TK_INICIO);
			dec_se.add(TK_VAR);
			dec_se.add(TK_LEIA);
			dec_se.add(TK_MENOR);
			dec_se.add(TK_ESCREVA);
			dec_se.add(TK_IGUAL);
			dec_se.add(TK_SE);
			dec_se.add(TK_ENQUANTO);
			dec_se.add(TK_ID);
			dec_se.add(TK_FIM);
		}
		return dec_se;
	}

	private static List<Integer> r_else_opc() {
		if(else_opc == null){
			else_opc = new ArrayList<Integer>();
			else_opc.add(TK_LEIA);
			else_opc.add(TK_INICIO);
			else_opc.add(TK_VAR);
			else_opc.add(TK_MENOR);
			else_opc.add(TK_ESCREVA);
			else_opc.add(TK_IGUAL);
			else_opc.add(TK_SE);
			else_opc.add(TK_ENQUANTO);
			else_opc.add(TK_FIM);
			else_opc.add(TK_ID);
		}
		return else_opc;
	}

	private static List<Integer> r_dec_enquanto() {
		if(dec_enquanto == null){
			dec_enquanto = new ArrayList<Integer>();
			dec_enquanto.add(TK_LEIA);
			dec_enquanto.add(TK_INICIO);
			dec_enquanto.add(TK_VAR);
			dec_enquanto.add(TK_MENOR);
			dec_enquanto.add(TK_ESCREVA);
			dec_enquanto.add(TK_IGUAL);
			dec_enquanto.add(TK_SE);
			dec_enquanto.add(TK_ENQUANTO);
			dec_enquanto.add(TK_FIM);
			dec_enquanto.add(TK_ID);
		}
		return dec_enquanto;
	}

	private static List<Integer> r_passa_param() {
		if(passa_param == null){
			passa_param = new ArrayList<Integer>();
			passa_param.add(TK_PARENTESE_A);
		}
		return passa_param;
	}

	private static List<Integer> r_passa_param_i() {
		if(passa_param_i == null){
			passa_param_i = new ArrayList<Integer>();
			passa_param_i.add(TK_PARENTESE_A);
		}
		return passa_param_i;
	}

	private static List<Integer> r_tipo() {
		if(tipo == null){
			tipo = new ArrayList<Integer>();
			tipo.add(TK_ID);
		}
		return tipo;
	}

	private static List<Integer> r_parametros() {
		if(parametros == null){
			parametros = new ArrayList<Integer>();
			parametros.add(TK_PARENTESE_A);
		}
		return parametros;
	}
}