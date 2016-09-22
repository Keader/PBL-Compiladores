package AnalisadorSintatico;

/**
 *
 * @author Bradley
 */
public class ArvoreSintatica {

    private No raiz;
    private No atual;
    private int nElementos;

    public ArvoreSintatica(){
        raiz = null;
        atual = null;
        nElementos = 0;
    }

    public void add(int valor){
        if(raiz == null){
            raiz = new No(valor);
            raiz.setPai(null);
            atual = raiz;
            nElementos++;
        }
        else {
            No novo = new No(valor);
            atual.getFilhos().add(novo);
            novo.setPai(atual);
            atual = novo;
            nElementos++;
        }
    }

    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No raiz) {
        this.raiz = raiz;
    }

    public No getAtual() {
        return atual;
    }

    public void setAtual(No atual) {
        this.atual = atual;
    }

    public boolean isEmpty(){
        return nElementos == 0;
    }

    public int getNumeroElementos() {
        return nElementos;
    }

    public void voltaProPai(){
        //Pai de atual so sera null se ele for a raiz
       if (atual.getPai() == null)
           atual = raiz;
       else
           atual = atual.getPai();
    }
}
