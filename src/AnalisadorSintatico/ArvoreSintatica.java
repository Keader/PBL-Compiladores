package AnalisadorSintatico;

import Util.Debug;
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

    public ArvoreSintatica() {
        raiz = null;
        atual = null;
        nElementos = 0;
    }

    public void add(int valor) {
        if (raiz == null) {
            raiz = new No(valor);
            raiz.setPai(null);
            atual = raiz;
            nElementos++;
        } else {
            No novo = new No(valor);
            atual.getFilhos().add(novo);
            novo.setPai(atual);
            //Tecnicamente o R_Epsilon eh um terminal, entao nao desce o atual da arvore ateh ele
            //O mesmo vale para todos os terminais.
            if (novo.getId() != R_EPSILON && novo.getId() > MAX_TOKEN_VALUE) {
                atual = novo;
            }
            nElementos++;
        }
    }

    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No raiz) {
        this.raiz = raiz;
    }

    public boolean isRaiz() {
        return atual == raiz;
    }

    public No getAtual() {
        return atual;
    }

    public void setAtual(No atual) {
        this.atual = atual;
    }

    public boolean isEmpty() {
        return nElementos == 0;
    }

    public int getNumeroElementos() {
        return nElementos;
    }

    public void voltaProPai() {
        //Pai de atual so sera null se ele for a raiz
        if (atual.getPai() == null) {
            atual = raiz;
        } else {
            atual = atual.getPai();
        }
    }

    /**
     * @deprecated Aconselhavel nao utilizar, era para debug no sintatico
     */
    public void imprimirArvoe() {
        imprimeNo(raiz);
    }

    private void imprimeNo(No no) {
        if (no == null)
            return;

        //Terminais e R_Epsilon nao sao pais de ninguem.
        if (no.getId() > MAX_TOKEN_VALUE && no.getId() != R_EPSILON)
            System.out.println("Pai: " + conversorIdString(no.getId()));

        //Imprime todos os filhos do No Atual.
        for (No filho : no.getFilhos())
            System.err.println(conversorIdString(filho.getId()));

        //Manda imprimir os filhos dos filhos.
        for (No filho : no.getFilhos())
            imprimeNo(filho);
    }

    /*
    Ta bugado, causando:
    Exception in thread "main" java.util.ConcurrentModificationException
    Problema do futuro, se precisar remover mesmo

    public  void limparEpsilons(){
        limparEpsilons(raiz);
    }

    private  void limparEpsilons(No no){
        if (no == null)
            return;

        if (no.getId() == R_EPSILON)
            removerNoEpsilon(no);
        else{
            for (No filho : no.getFilhos())
                limparEpsilons(filho);
        }

    }

    private void removerNoEpsilon(No remover){
        if (remover == null)
            return;

        No pai = remover.getPai();

        if (pai == null)
            return;

        if (!pai.getFilhos().remove(remover))
            Debug.ErrPrintln("Na Arvore Sintatica, nao foi possivel remover o epsilon.");

        if (remover.temFilhos())
            Debug.ErrPrintln("Na Arvore Sintatica, Epsilon possuia filhos.");
    }
*/
}
