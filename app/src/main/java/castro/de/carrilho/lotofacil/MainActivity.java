package castro.de.carrilho.lotofacil;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import castro.de.carrilho.lotofacil.file_reader.*;
import castro.de.carrilho.lotofacil.sqlite.BD;
import castro.de.carrilho.lotofacil.sqlite.Bilhete;
import castro.de.carrilho.lotofacil.util.*;

public class MainActivity extends ActionBarActivity {

    protected ProgressDialog mProgressDialog;
    private ProgressBar progressBar;
    private TextView textView;
    private int progressStatus = 0;
    Context context;
    static int progresso;
    Estatistics myEstatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        textView = (TextView) findViewById(R.id.textViewProgressBar);

        if (savedInstanceState == null) {
            //le o arquivo descompactado
//            new ReadFile().execute();
        }

        Intent intent = new Intent(this, ListUsersActivity.class);
        startActivity(intent);

    }

    public void startDownload( View v ) {
        //baixa o arquivo e descompacta
        new DownloadTask().execute();


    }


    //////////////////////////////////////////////////////////////////////////
    // Background Task
    //////////////////////////////////////////////////////////////////////////

    /**
     * Background task to read zipped file and mount a matriz in background.
     */
    public class ReadFile extends AsyncTask<String,Integer,Void> {

        @Override
        protected void onPreExecute() {
            showProgress("Lendo todos os sorteios...");
        }

        protected Void doInBackground(String... params) {
            myEstatistics = new Estatistics(getApplication());
            progressStatus = myEstatistics.getOndeParou();
            publishProgress((int)progressStatus);
            estatisticas();
/*            int num_jogos = myEstatistics.getJogos();
            int[] bilhete = {22, 23, 11, 16, 21, 8, 12, 2, 4, 19, 10, 25, 7, 14, 1};
            int pontos = myEstatistics.getQuantosPontos(bilhete, (1187 - 1));
*/            return null;
        }

        protected void estatisticas(){
            myEstatistics.atualiza_bd_concursos_sem_pontuar();
        }

        protected void onProgressUpdate(Integer... progress){
            dismissProgress();
            progressStatus = progress[0];
            textView.setText(progressStatus+"/"+progressBar.getMax());
            progressBar.setProgress(progressStatus);
        }



        @Override
        protected void onPostExecute(Void unused) {


        }
    }


    public static void progressBar(int progress){

        progresso = progress;
        //colocar aqui um modo de progresso ao criar o banco de dados com 3.200.00 posições. Operação muito demorada
    }



    //////////////////////////////////////////////////////////////////////////
    // Background Task
    //////////////////////////////////////////////////////////////////////////

    /**
     * Background task to download and unpack .zip file in background.
     */
    private class DownloadTask extends AsyncTask<String,Void,Exception> {

        @Override
        protected void onPreExecute() {
            showProgress("Baixando últimos resultados...");
        }

        @Override
        protected Exception doInBackground(String... params) {

            try {
                downloadAllAssets();
            } catch ( Exception e ) { return e; }

            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            dismissProgress();
            if ( result == null ) { return; }
            // something went wrong, post a message to user - you could use a dialog here or whatever
            Toast.makeText(MainActivity.this, result.getLocalizedMessage(), Toast.LENGTH_LONG ).show();

            myEstatistics.setOndeParou(0); //por ter baixado novo arquivo, começa as estatísticas do inicio dele
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // Progress Dialog
    //////////////////////////////////////////////////////////////////////////

    protected void showProgress(String s) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(s);
        mProgressDialog.setMessage( getString(R.string.progress_detail) );
        mProgressDialog.setIndeterminate( true );
        mProgressDialog.setCancelable( false );
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        // You can't be too careful.
        if (mProgressDialog != null && mProgressDialog.isShowing() && mProgressDialog.getWindow() != null) {
            try {
                mProgressDialog.dismiss();
            } catch ( IllegalArgumentException ignore ) { ; }
        }
        mProgressDialog = null;
    }

    //////////////////////////////////////////////////////////////////////////
    // File Download
    //////////////////////////////////////////////////////////////////////////

    /**
     * Download .zip file specified by url, then unzip it to a folder in external storage.
     *
     */
    private void downloadAllAssets() {
        String url = this.getString(R.string.url);
        // Temp folder for holding asset during download
        File zipDir =  ExternalStorage.getSDCacheDir(this, "tmp");
        // File path to store .zip file before unzipping
        File zipFile = new File( zipDir.getPath() + "/temp.zip" );
        // Folder to hold unzipped output
        File outputDir = ExternalStorage.getSDCacheDir( this, "unzipped" );

        try {
            DownloadFile.download( url, zipFile, zipDir );
            unzipFile( zipFile, outputDir );
        } finally {
            zipFile.delete();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // Zip Extraction
    //////////////////////////////////////////////////////////////////////////

    /**
     * Unpack .zip file.
     *
     * @param zipFile
     * @param destination
     */
    protected void unzipFile( File zipFile, File destination ) {
        DecompressZip decomp = new DecompressZip( zipFile.getPath(),
                destination.getPath() + File.separator );
        decomp.unzip();
    }



}
