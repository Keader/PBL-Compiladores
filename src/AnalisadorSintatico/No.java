package AnalisadorSintatico;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bradley
 */
public class No {

    private No pai;
    private List<No> filhos;
    private int id;

    public No(int id){
        this.id = id;
        filhos = new ArrayList<>();
    }

    public No getPai() {
        return pai;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }

    public List<No> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<No> filhos) {
        this.filhos = filhos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean temFilhos(){
        return !filhos.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof No) {
            No other = (No) obj;
            if (this.id == other.id) {
                return true;
            }
            return false;
        }
        return false;
    }

}
