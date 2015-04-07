package castro.de.carrilho.lotofacil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import castro.de.carrilho.lotofacil.sqlite.Bilhete;

/**
 * Created by Lucas on 06/04/2015.
 */
public class BilheteAdapter extends BaseAdapter {

    private Context context;
    private List<Bilhete> list;
    private int[] bilhete;

    public BilheteAdapter(Context context, List<Bilhete> list){
        this.context = context;
        this.list = list;
        bilhete = new int[15];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0).getId();
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        final int auxPosition = position;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bilhete, null);

        TextView tv = (TextView) layout.findViewById(R.id.nome);
        int pontos = list.get(position).getPontos();
        bilhete = list.get(position).getNum().clone();
        String s = String.valueOf(pontos) + " - ";
        Arrays.sort(bilhete);
        for (int i = 0; i < 15; i++)
            s = s + bilhete[i] + " ";

        tv.setText(s);



        return layout;
    }

}
