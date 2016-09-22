package AnalisadorSintatico;

/**
 *
 * @author Bradley
 */
public class ArvoreSintatica {

    private No raiz;
    private No atual;
    private int numeroElementos;

    public ArvoreSintatica(){
        raiz = null;
        atual = null;
        numeroElementos = 0;
    }

    public void add(int valor){
        if(raiz == null){
            raiz = new No(valor);
            atual = raiz;
            numeroElementos++;
        }
        else {
            No novo = new No(valor);
            atual.getFilhos().add(novo);
            novo.setPai(atual);
            atual = novo;
            numeroElementos++;
        }
    }


}
