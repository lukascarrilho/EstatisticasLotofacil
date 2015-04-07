package castro.de.carrilho.lotofacil.file_reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

import castro.de.carrilho.lotofacil.MainActivity;
import castro.de.carrilho.lotofacil.R;
import castro.de.carrilho.lotofacil.sqlite.BD;
import castro.de.carrilho.lotofacil.sqlite.Bilhete;
import castro.de.carrilho.lotofacil.util.ExternalStorage;

import static castro.de.carrilho.lotofacil.MainActivity.*;

/**
 * Created by Lucas on 01/04/2015.
 */
public class Estatistics {

    UnzipedFileReader myUnzipedFileReader;
    Context context;
    int progresso = 0;

    public Estatistics(Context context){
        this.context = context;

        //primeiro de tudo, chama a classe que le o arquivo e monta a matriz contendo todos os resultados anteriores

        File outputDir = ExternalStorage.getSDCacheDir(context, "unzipped");
        String name_file = context.getString(R.string.file_unziped);
        String url = outputDir + "/" + name_file;
        myUnzipedFileReader = new UnzipedFileReader(url);

        progresso = 0;

    }

    public int getJogos(){
        return myUnzipedFileReader.qtd_jogos;
    }

    public int getQuantosPontos(int[] bilhete, int cod_jogo){
        //cod_jogo inicia em zero

        int pontos = 0;
        for (int i = 0; i < bilhete.length; i++){
            for (int j = 0; j < 15; j++){
                if (bilhete[i] == myUnzipedFileReader.sorteados[cod_jogo][j])
                    pontos++;
            }
        }

        return pontos;
    }

    public void jogoToString(int cod_jogo){
        //cod_jogo inicia em zero
        Log.i("Jogo numero " + String.valueOf(cod_jogo), String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][0]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][1]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][2]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][3]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][4]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][5]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][6]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][7]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][8]) + " " +
                String.valueOf(myUnzipedFileReader.sorteados[cod_jogo][9]));
    }

    public int getConcursosSemPontuar(int[] bilhete, int pontos){
        //retorna zero caso o jogo tenha pontuado na última rodada;
        //retorna há quantas rodadas o jogo pontuou pela última vez
        //retorna -1 caso o jogo nunca tenha sido pontuado

        int total = getJogos(); //pega o total de concursos
        int ultima_vez_pontuado = 0;
        for (int i = total; i > 0; i--){
            //percorre desde o último concurso até o primeiro, em busca da última vez em que foi pontuado
            if (getQuantosPontos(bilhete, i - 1) >= pontos) //se o bilhete foi pontuado
                break;
            else
                ultima_vez_pontuado++;
        }
        if (ultima_vez_pontuado == total) //se ele nunca foi pontuado
            ultima_vez_pontuado = -1;

        return ultima_vez_pontuado;

    }

    public void atualiza_bd_concursos_sem_pontuar(){
        Bilhete b;
        int ultima_vez_pontuado = 0;
        BD bd = new BD(context);
        progresso = getOndeParou();
        for (int i = progresso; i <= 3268760; i++) {
            if (i % 100 == 0)
                setOndeParou(i); //atualiza o progresso atual
            b = bd.buscar(i); //numero do jogo, iniciando em 1
            ultima_vez_pontuado = getConcursosSemPontuar(b.getNum(), 11); //retorna ha quanto tempo esse jogo nao faz ao menos 11 pontos
            b.setPontos(ultima_vez_pontuado); //atualiza no objeto
            bd.atualizar(b); //atualiza no banco de dados
            Log.i(String.valueOf("combinação " + b.getId()), "Sem pontuar ha " + String.valueOf(ultima_vez_pontuado) + String.valueOf(" partidas."));
        }
    }

    public int getOndeParou(){
        SharedPreferences sharedPref = context.getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int onde_parou = sharedPref.getInt("onde_parou", 0); //se o valor não estava setado então recebe zero
        Log.i("Onde parou: ", String.valueOf(onde_parou));
        return onde_parou;
    }

    public void setOndeParou(int valor) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("onde_parou", valor);
        editor.commit();
    }

}
