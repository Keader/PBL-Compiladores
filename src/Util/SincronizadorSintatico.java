package Util;

import java.util.ArrayList;
import java.util.List;

public class SincronizadorSintatico implements Dicionario{
	private static List<Integer> r_programa, r_inicio_const_var_func, r_dec_const_var_derivada, r_inicio_var_func, r_inicio_func, r_dec_const, r_dec_const_continuo, r_dec_const_i,  
	r_dec_const_ii, r_dec_var, r_dec_var_continuo, r_dec_var_i, r_dec_var_ii, r_dec_func, r_dec_func_i, r_parametros, r_parametros_i, r_dec_main, r_exp_relacional_bool, 
	r_exp_conjunta, r_exp_conjunta_i, r_exp_relacional, r_exp_relacional_i, r_operar_relacionalmente, r_op_relacional, r_not_opc, r_exp_simples, r_termo, r_termo_i, r_fator, r_fator_i,	
	r_op_mais_menos, r_op_multi_div, r_id_funcao_e_outros,	r_id_funcao_e_outros_derivado, r_possible_func, r_retorno_func, r_corpo, r_comandos, r_chama_ou_atribui, r_whos_next, 
	r_novo_escopo, r_array, r_array_i, r_array_indexes, r_array_param, r_array_param_i, r_array_indexes_opt, r_dec_leitura,	r_leitura_i, r_dec_escrita,	r_escrevivel_i,	r_escrevivel,	
	r_termo_e, r_termo_i_e, r_fator_e, r_fator_i_e, r_dec_se, r_else_opc, r_dec_enquanto, r_passa_param, r_passa_param_i, r_tipo;
	
	public static List<Integer> getFollow(int regra){
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
		//instanciando a lista
		if(r_programa == null){
			r_programa = new ArrayList<Integer>();
			r_programa.add(TK_EOF);
		}
		return r_programa;
	}
	
	private static List<Integer> r_inicio_const_var_func() {
		//instanciando a lista
		if(r_inicio_const_var_func == null){
			r_inicio_const_var_func = new ArrayList<Integer>();
			r_inicio_const_var_func.add(TK_EOF);
		}
		return r_inicio_const_var_func;
	}
	
	private static List<Integer> r_dec_const_var_derivada() {
		//instanciando a lista
		if(r_dec_const_var_derivada == null)
			r_dec_const_var_derivada = new ArrayList<Integer>();
		
		return r_dec_const_var_derivada;
	}
	
	private static List<Integer> r_inicio_var_func() {
		//instanciando a lista
		if(r_inicio_var_func == null)
			r_inicio_var_func = new ArrayList<Integer>();
		
		return r_inicio_var_func;
	}
	
	private static List<Integer> r_inicio_func() {
		//instanciando a lista
		if(r_inicio_func == null)
			r_inicio_func = new ArrayList<Integer>();
		
		return r_inicio_func;
	}
	
	private static List<Integer> r_dec_const() {
		//instanciando a lista
		if(r_dec_const == null)
			r_dec_const = new ArrayList<Integer>();
		
		return r_dec_const;
	}
	
	private static List<Integer> r_dec_const_continuo() {
		//instanciando a lista
		if(r_dec_const_continuo == null)
			r_dec_const_continuo = new ArrayList<Integer>();
		
		return r_dec_const_continuo;
	}
	
	private static List<Integer> r_dec_const_i() {
		//instanciando a lista
		if(r_dec_const_i == null)
			r_dec_const_i = new ArrayList<Integer>();
		
		return r_dec_const_i;
	}
	
	private static List<Integer> r_dec_const_ii() {
		//instanciando a lista
		if(r_dec_const_ii == null)
			r_dec_const_ii = new ArrayList<Integer>();
		
		return r_dec_const_ii;
	}
	
	private static List<Integer> r_dec_var() {
		//instanciando a lista
		if(r_dec_var == null)
			r_dec_var = new ArrayList<Integer>();
		
		return r_dec_var;
	}
	
	private static List<Integer> r_dec_var_continuo() {
		//instanciando a lista
		if(r_dec_var_continuo == null)
			r_dec_var_continuo = new ArrayList<Integer>();
		
		return r_dec_var_continuo;
	}
	
	private static List<Integer> r_dec_var_i() {
		//instanciando a lista
		if(r_dec_var_i == null)
			r_dec_var_i = new ArrayList<Integer>();
		
		return r_dec_var_i;
	}
	
	private static List<Integer> r_dec_var_ii() {
		//instanciando a lista
		if(r_dec_var_ii == null)
			r_dec_var_ii = new ArrayList<Integer>();
		
		return r_dec_var_ii;
	}
	
	private static List<Integer> r_dec_func() {
		//instanciando a lista
		if(r_dec_func == null)
			r_dec_func = new ArrayList<Integer>();
		
		return r_dec_func;
	}
	
	private static List<Integer> r_dec_func_i() {
		//instanciando a lista
		if(r_dec_func_i == null)
			r_dec_func_i = new ArrayList<Integer>();
		
		return r_dec_func_i;
	}
	
	private static List<Integer> r_parametros_i() {
		//instanciando a lista
		if(r_parametros_i == null)
			r_parametros_i = new ArrayList<Integer>();
		
		return r_parametros_i;
	}
	
	private static List<Integer> r_dec_main() {
		//instanciando a lista
		if(r_dec_main == null)
			r_dec_main = new ArrayList<Integer>();
		
		return r_dec_main;
	}
	
	private static List<Integer> r_exp_relacional_bool() {
		//instanciando a lista
		if(r_exp_relacional_bool == null)
			r_exp_relacional_bool = new ArrayList<Integer>();
		
		return r_exp_relacional_bool;
	}
	
	private static List<Integer> r_exp_conjunta() {
		//instanciando a lista
		if(r_exp_conjunta == null)
			r_exp_conjunta = new ArrayList<Integer>();
		
		return r_exp_conjunta;
	}
	
	private static List<Integer> r_exp_conjunta_i() {
		//instanciando a lista
		if(r_exp_conjunta_i == null)
			r_exp_conjunta_i = new ArrayList<Integer>();
		
		return r_exp_conjunta_i;
	}
	
	private static List<Integer> r_exp_relacional() {
		//instanciando a lista
		if(r_exp_relacional == null)
			r_exp_relacional = new ArrayList<Integer>();
		
		return r_exp_relacional;
	}
	
	private static List<Integer> r_exp_relacional_i() {
		//instanciando a lista
		if(r_exp_relacional_i == null)
			r_exp_relacional_i = new ArrayList<Integer>();
		
		return r_exp_relacional_i;
	}
	
	private static List<Integer> r_operar_relacionalmente() {
		//instanciando a lista
		if(r_operar_relacionalmente == null)
			r_operar_relacionalmente = new ArrayList<Integer>();
		
		return r_operar_relacionalmente;
	}
	
	private static List<Integer> r_op_relacional() {
		//instanciando a lista
		if(r_op_relacional == null)
			r_op_relacional = new ArrayList<Integer>();
		
		return r_op_relacional;
	}
	
	private static List<Integer> r_not_opc() {
		//instanciando a lista
		if(r_not_opc == null)
			r_not_opc = new ArrayList<Integer>();
		
		return r_not_opc;
	}
	
	private static List<Integer> r_exp_simples() {
		//instanciando a lista
		if(r_exp_simples == null)
			r_exp_simples = new ArrayList<Integer>();
		
		return r_exp_simples;
	}
	
	private static List<Integer> r_termo() {
		//instanciando a lista
		if(r_termo == null)
			r_termo = new ArrayList<Integer>();
		
		return r_termo;
	}
	
	private static List<Integer> r_termo_i() {
		//instanciando a lista
		if(r_termo_i == null)
			r_termo_i = new ArrayList<Integer>();
		
		return r_termo_i;
	}
	
	private static List<Integer> r_fator() {
		//instanciando a lista
		if(r_fator == null)
			r_fator = new ArrayList<Integer>();
		
		return r_fator;
	}
	
	private static List<Integer> r_fator_i() {
		//instanciando a lista
		if(r_fator_i == null)
			r_fator_i = new ArrayList<Integer>();
		
		return r_fator_i;
	}
	
	private static List<Integer> r_op_mais_menos() {
		//instanciando a lista
		if(r_op_mais_menos == null)
			r_op_mais_menos = new ArrayList<Integer>();
		
		return r_op_mais_menos;
	}
	
	private static List<Integer> r_op_multi_div() {
		//instanciando a lista
		if(r_op_multi_div == null)
			r_op_multi_div = new ArrayList<Integer>();
		
		return r_op_multi_div;
	}
	
	private static List<Integer> r_id_funcao_e_outros_derivado() {
		//instanciando a lista
		if(r_id_funcao_e_outros_derivado == null)
			r_id_funcao_e_outros_derivado = new ArrayList<Integer>();
		
		return r_id_funcao_e_outros_derivado;
	}
	
	private static List<Integer> r_id_funcao_e_outros() {
		//instanciando a lista
		if(r_id_funcao_e_outros == null)
			r_id_funcao_e_outros = new ArrayList<Integer>();
		
		return r_id_funcao_e_outros;
	}
	
	private static List<Integer> r_possible_func() {
		//instanciando a lista
		if(r_possible_func == null)
			r_possible_func = new ArrayList<Integer>();
		
		return r_possible_func;
	}
	
	private static List<Integer> r_retorno_func() {
		//instanciando a lista
		if(r_retorno_func == null)
			r_retorno_func = new ArrayList<Integer>();
		
		return r_retorno_func;
	}
	
	private static List<Integer> r_corpo() {
		//instanciando a lista
		if(r_corpo == null)
			r_corpo = new ArrayList<Integer>();
		
		return r_corpo;
	}
	
	private static List<Integer> r_comandos() {
		//instanciando a lista
		if(r_comandos == null)
			r_comandos = new ArrayList<Integer>();
		
		return r_comandos;
	}
	
	private static List<Integer> r_chama_ou_atribui() {
		//instanciando a lista
		if(r_chama_ou_atribui == null) {
			r_chama_ou_atribui = new ArrayList<Integer>();
		}
		
		return r_chama_ou_atribui;
	}
	
	private static List<Integer> r_whos_next() {
		//instanciando a lista
		if(r_whos_next == null)
			r_whos_next = new ArrayList<Integer>();
		
		return r_whos_next;
	}
	
	private static List<Integer> r_novo_escopo() {
		//instanciando a lista
		if(r_novo_escopo == null)
			r_novo_escopo = new ArrayList<Integer>();
		
		return r_novo_escopo;
	}
	
	private static List<Integer> r_array() {
		//instanciando a lista
		if(r_array == null)
			r_array = new ArrayList<Integer>();
		
		return r_array;
	}
	
	private static List<Integer> r_array_i() {
		//instanciando a lista
		if(r_array_i == null)
			r_array_i = new ArrayList<Integer>();
		
		return r_array_i;
	}
	
	private static List<Integer> r_array_indexes() {
		//instanciando a lista
		if(r_array_indexes == null)
			r_array_indexes = new ArrayList<Integer>();
		
		return r_array_indexes;
	}
	
	private static List<Integer> r_array_param() {
		//instanciando a lista
		if(r_array_param == null)
			r_array_param = new ArrayList<Integer>();
		
		return r_array_param;
	}
	
	private static List<Integer> r_array_param_i() {
		//instanciando a lista
		if(r_array_param_i == null)
			r_array_param_i = new ArrayList<Integer>();
		
		return r_array_param_i;
	}
	
	private static List<Integer> r_array_indexes_opt() {
		//instanciando a lista
		if(r_array_indexes_opt == null)
			r_array_indexes_opt = new ArrayList<Integer>();
		
		return r_array_indexes_opt;
	}
	
	private static List<Integer> r_dec_leitura() {
		//instanciando a lista
		if(r_dec_leitura == null)
			r_dec_leitura = new ArrayList<Integer>();
		
		return r_dec_leitura;
	}
	
	private static List<Integer> r_leitura_i() {
		//instanciando a lista
		if(r_leitura_i == null)
			r_leitura_i = new ArrayList<Integer>();
		
		return r_leitura_i;
	}
	
	private static List<Integer> r_dec_escrita() {
		//instanciando a lista
		if(r_dec_escrita == null)
			r_dec_escrita = new ArrayList<Integer>();
		
		return r_dec_escrita;
	}
	
	private static List<Integer> r_escrevivel_i() {
		//instanciando a lista
		if(r_escrevivel_i == null)
			r_escrevivel_i = new ArrayList<Integer>();
		
		return r_escrevivel_i;
	}
	
	private static List<Integer> r_escrevivel() {
		//instanciando a lista
		if(r_escrevivel == null)
			r_escrevivel = new ArrayList<Integer>();
		
		return r_escrevivel;
	}
	
	private static List<Integer> r_termo_e() {
		//instanciando a lista
		if(r_termo_e == null)
			r_termo_e = new ArrayList<Integer>();
		
		return r_termo_e;
	}
	
	private static List<Integer> r_termo_i_e() {
		//instanciando a lista
		if(r_termo_i_e == null)
			r_termo_i_e = new ArrayList<Integer>();
		
		return r_termo_i_e;
	}
	
	private static List<Integer> r_fator_e() {
		//instanciando a lista
		if(r_fator_e == null)
			r_fator_e = new ArrayList<Integer>();
		
		return r_fator_e;
	}
	
	private static List<Integer> r_fator_i_e() {
		//instanciando a lista
		if(r_fator_i_e == null)
			r_fator_i_e = new ArrayList<Integer>();
		
		return r_fator_i_e;
	}
	
	private static List<Integer> r_dec_se() {
		//instanciando a lista
		if(r_dec_se == null)
			r_dec_se = new ArrayList<Integer>();
		
		return r_dec_se;
	}
	
	private static List<Integer> r_else_opc() {
		//instanciando a lista
		if(r_else_opc == null)
			r_else_opc = new ArrayList<Integer>();
		
		return r_else_opc;
	}
	
	private static List<Integer> r_dec_enquanto() {
		//instanciando a lista
		if(r_dec_enquanto == null)
			r_dec_enquanto = new ArrayList<Integer>();
		
		return r_dec_enquanto;
	}
	
	private static List<Integer> r_passa_param() {
		//instanciando a lista
		if(r_passa_param == null)
			r_passa_param = new ArrayList<Integer>();
		
		return r_passa_param;
	}
	
	private static List<Integer> r_passa_param_i() {
		//instanciando a lista
		if(r_passa_param_i == null)
			r_passa_param_i = new ArrayList<Integer>();
		
		return r_passa_param_i;
	}
	
	private static List<Integer> r_tipo() {
		//instanciando a lista
		if(r_tipo == null)
			r_tipo = new ArrayList<Integer>();
		
		return r_tipo;
	}
	
	private static List<Integer> r_parametros() {
		//instanciando a lista
		if(r_parametros == null)
			r_parametros = new ArrayList<Integer>();
		
		return r_parametros;
	}
	
	public static void main(String []a){

	}
}
