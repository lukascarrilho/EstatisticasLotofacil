package castro.de.carrilho.lotofacil.file_reader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import castro.de.carrilho.lotofacil.sqlite.BD;
import castro.de.carrilho.lotofacil.sqlite.Bilhete;

/**
 * Created by Lucas on 01/04/2015.
 */
public class Combinacoes{

    private int numeros[];
    private int quantidade;
    public int resultado[];
    Bilhete b;
    SQLiteDatabase bd;
    int progresso;

    public int count;

    public void busca(int inicio,int fim, int profundidade){

        if ( (profundidade + 1) >= quantidade)
            for(int x = inicio; x <= fim; x++) {

                resultado[profundidade] = numeros[x];

                // adiciona cada linha gerada no banco
                b.setPontos(-1);
                b.setNum(resultado);
                inserir(b);

                if (count % 30 == 0)
                    Log.i("linha", String.valueOf(count));
//                System.out.println(resultado[0] + ", " + resultado[1] + ", " + resultado[2] + "\n");
                count++;

            }
        else
            for(int x = inicio; x <= fim; x++){
                resultado[profundidade] = numeros[x];
                busca(x + 1, fim + 1, profundidade + 1);
            }
    }

    public Combinacoes(SQLiteDatabase bd){

        resultado = new int[15];
        numeros = new int[25];
        b = new Bilhete();
        count = 0;
        quantidade = 15;
        this.bd = bd;

        for (int i = 0; i < 25; i++)
            numeros[i] = (i + 1);

        busca(0, (25 - 15), 0);
        Log.i("Total de combinacoes", String.valueOf(count));


    }

    private void inserir(Bilhete b){
        int[] bilhete = b.getNum().clone();
        //recebe sempre um vetor de 15 posições (os 15 numeros que aposta na lotofácil)
        ContentValues valores = new ContentValues();
        for (int i = 0; i < 15; i++){
            valores.put("num" + String.valueOf(i + 1), bilhete[i]);
        }
        valores.put("pontos", b.getPontos());

        bd.insert("combinacoes", null, valores);
    }
}
