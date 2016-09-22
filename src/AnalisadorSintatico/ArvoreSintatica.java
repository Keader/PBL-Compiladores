package AnalisadorSintatico;

import Util.No;
import Util.Dicionario;
import static Util.Dicionario.*;
/**
 *
 * @author Bradley
 */
public class ArvoreSintatica implements Dicionario {

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
            //Tecnicamente o R_Epsilon eh um terminal, entao nao desce o atual da arvore ateh ele
            //O mesmo vale para todos os terminais.
            if (novo.getId() != R_EPSILON && novo.getId() > MAX_TOKEN_VALUE)
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

    public void imprimirArvoe(){
        imprimeNo(raiz);
    }

     private void imprimeNo(No no){
        if(no == null)
            return;

        //Terminais e R_Epsilon nao sao pais de ninguem.
        if(no.getId() > MAX_TOKEN_VALUE && no.getId() != R_EPSILON)
            System.out.println("Pai: "+conversorIdString(no.getId()));

        //Imprime todos os filhos do No Atual.
        for (No filho : no.getFilhos())
            System.err.println(conversorIdString(filho.getId()));

        //Manda imprimir os filhos dos filhos.
        for (No filho: no.getFilhos())
            imprimeNo(filho);
    }

}
