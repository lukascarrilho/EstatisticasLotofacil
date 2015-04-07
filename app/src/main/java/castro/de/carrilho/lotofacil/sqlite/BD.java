package castro.de.carrilho.lotofacil.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BD {
	private SQLiteDatabase bd;
	
	public BD(Context context){
		BDCore auxBd = new BDCore(context);
		bd = auxBd.getWritableDatabase();
	}

    public void inserir(Bilhete b){
        int[] bilhete = b.getNum().clone();
        //recebe sempre um vetor de 15 posições (os 15 numeros que aposta na lotofácil)
        ContentValues valores = new ContentValues();
        for (int i = 0; i < 15; i++){
            valores.put("num" + String.valueOf(i + 1), bilhete[i]);
        }
        valores.put("pontos", b.getPontos());

        bd.insert("combinacoes", null, valores);
    }

    public void atualizar(Bilhete b){
        int[] bilhete = b.getNum().clone();
        ContentValues valores = new ContentValues();
        for (int i = 0; i < 15; i++){
            valores.put("num" + String.valueOf(i + 1), bilhete[i]);
        }
        valores.put("pontos", b.getPontos());

        bd.update("combinacoes", valores, "_id = ?", new String[]{"" + b.getId()});
    }

    public Bilhete buscar(int posicao){

        String[] colunas = new String[]{"_id", "num1", "num2", "num3", "num4", "num5", "num6", "num7", "num8", "num9", "num10", "num11", "num12", "num13", "num14", "num15", "pontos"};
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(posicao)};

        Cursor cursor = bd.query("combinacoes", null, selection, selectionArgs, null, null, null);

        Bilhete b = new Bilhete();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            b.setId(cursor.getLong(0));
            int[] bilhete = new int[15];
            for (int i = 0; i < 15; i++){
                bilhete[i] = cursor.getInt(i + 1);
            }
            b.setNum(bilhete);
            b.setPontos(cursor.getInt(16));

        }

        return(b);
    }

    public List<Bilhete> buscar(){
        List<Bilhete> list = new ArrayList<Bilhete>();
        String[] colunas = new String[]{"_id", "num1", "num2", "num3", "num4", "num5", "num6", "num7", "num8", "num9", "num10", "num11", "num12", "num13", "num14", "num15", "pontos"};
        Cursor cursor = bd.query("combinacoes", colunas, null, null, null, null, "pontos DESC limit 500");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{

                Bilhete b = new Bilhete();
                b.setId(cursor.getLong(0));
                int[] bilhete = new int[15];
                for (int i = 0; i < 15; i++){
                    bilhete[i] = cursor.getInt(i + 1);
                }
                b.setNum(bilhete);
                b.setPontos(cursor.getInt(16));
                list.add(b);

            }while(cursor.moveToNext());
        }

        return(list);
    }

}
