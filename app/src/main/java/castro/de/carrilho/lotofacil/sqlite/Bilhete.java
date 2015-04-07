package castro.de.carrilho.lotofacil.sqlite;

/**
 * Created by Lucas on 04/04/2015.
 */
public class Bilhete {

    private int[] numeros;
    private int pontos;
    private long id;

    public Bilhete(){
        numeros = new int[15];
    }

    public void setNum(int[] numeros){
        this.numeros = numeros;
    }

    public int[] getNum(){
        return this.numeros;
    }

    public void setPontos(int pontos){ this.pontos = pontos; }

    public int getPontos(){ return this.pontos; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
