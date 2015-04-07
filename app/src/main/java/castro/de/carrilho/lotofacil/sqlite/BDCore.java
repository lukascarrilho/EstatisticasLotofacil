package castro.de.carrilho.lotofacil.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import castro.de.carrilho.lotofacil.file_reader.Combinacoes;

public class BDCore extends SQLiteOpenHelper {
	private static final String NOME_BD = "teste";
	private static final int VERSAO_BD = 1;
    Context context;
	
	public BDCore(Context context){
		super(context, NOME_BD, null, VERSAO_BD);
        this.context = context;
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("create table combinacoes(_id integer primary key autoincrement, num1 integer not null, num2 integer not null, " +
                "num3 integer not null, num4 integer not null, num5 integer not null, num6 integer not null, num7 integer not null, " +
                "num8 integer not null, num9 integer not null, num10 integer not null, num11 integer not null, num12 integer not null, " +
                "num13 integer not null, num14 integer not null, num15 integer not null, pontos integer);");
//        bd.execSQL("create table combinacoes(_id integer primary key autoincrement, nome text not null, email text not null, senha text not null);");

        //preenche apenas uma vez, na primeira execução
        preenche(bd);

	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
		bd.execSQL("drop table combinacoes;");
		onCreate(bd);
	}

    private void preenche(SQLiteDatabase bd){
        //preencher a tabela;
        Combinacoes comb = new Combinacoes(bd);
    }

}
