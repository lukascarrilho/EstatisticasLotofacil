package castro.de.carrilho.lotofacil.file_reader;

/**
 * Created by Lucas on 31/03/2015.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UnzipedFileReader {

    public static int qtd_jogos = 0;
    public static int sorteados[][];

    public UnzipedFileReader(String url){

        try {

            FileReader arq = new FileReader(url);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); // lê a primeira linha
            // a variável "linha" recebe o valor "null" quando o processo
            // de repetição atingir o final do arquivo texto
            while (linha != null) {

                // Abaixo uma expressão regular para encontrar datas.
                // Cada jogo possui uma data, logo, cada data encontrada marca o inicio de um jogo
                if (linha.matches("(.*)([0-9]{2})/([0-9]{2})/([0-9]{4})(.*)")) {
                    qtd_jogos++;
                }

                linha = lerArq.readLine(); // lê da segunda até a última linha
            }

            sorteados = new int[qtd_jogos][15];

            //le o arquivo novamente. A primeira leitura foi só pra descobrir a quantidade de jogos
            arq = new FileReader(url);
            lerArq = new BufferedReader(arq);

            linha = lerArq.readLine(); // lê a primeira linha
            qtd_jogos = 0;

            while (linha != null) {

                if (linha.matches("(.*)([0-9]{2})/([0-9]{2})/([0-9]{4})(.*)")) {
                    //os numeros estão nas próximas 15 linhas
                    for (int i = 0; i < 15; i++){
                        lerArq.readLine(); //pula uma linha, pois no arquivo ela fica em branco
                        linha = lerArq.readLine();
                        int n = getNum(linha); //o numero sorteado esta nesta linha
                        sorteados[qtd_jogos][i] = n; //preenche a matriz com o numero sorteado
                    }
                    qtd_jogos++;
                }

                linha = lerArq.readLine(); // lê da segunda até a última linha
            }


            arq.close();
        } catch (IOException e) {
            Log.i ("Erro: ",
                    e.getMessage());
        }

        return;
    }

    private int getNum(String s){

        String s_numero = s.substring(s.length() - 7,s.length() - 5);
//        Log.i("String problematica", s_numero);
        int num = Integer.parseInt(s_numero);
        return num;
    }


}
