package castro.de.carrilho.lotofacil;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.List;

import castro.de.carrilho.lotofacil.sqlite.BD;
import castro.de.carrilho.lotofacil.sqlite.Bilhete;


public class ListUsersActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_users);

        BD bd = new BD(this);

        List<Bilhete> list = bd.buscar();
        setListAdapter(new BilheteAdapter(this, list));
    }

}
