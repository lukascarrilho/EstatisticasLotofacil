package castro.de.carrilho.lotofacil.util;

/**
 * ZipDownloader
 * 
 * A simple app to demonstrate downloading and unpacking a .zip file
 * as a background task.
 * 
 * Copyright (c) 2011 Michael J. Portuesi (http://www.jotabout.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import android.app.ProgressDialog;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utility methods for downloading files.
 * 
 * @author portuesi
 *
 */
public class DownloadFile {
	
    private static final int BUFFER_SIZE = 8192;

	/**
	 * Download a file from a URL somewhere.  The download is atomic;
	 * that is, it downloads to a temporary file, then renames it to
	 * the requested file name only if the download successfully
	 * completes.
	 * 
	 * Returns TRUE if download succeeds, FALSE otherwise.
	 * 
	 * @param site				Source URL
	 * @param output			Path to output file
	 * @param tmpDir			Place to put file download in progress
	 */
	public static void download( String site, File output, File tmpDir ) {

/*        URL url = null;
        try {
            url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            File tmp = File.createTempFile( "download", ".tmp", tmpDir );
            Log.i ("tamanho: ", String.valueOf(connection.getContentLength()));
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(tmpDir + "/teste");
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            Log.i("passei aqui", "1");
            byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                Log.i("estou aqui", String.valueOf(x));
                bout.write(data, 0, x);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("erro ", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("erro ", e.toString());
        }
*/
		InputStream is = null;
		OutputStream os = null;
		File tmp = null;

		try {

            URL url = new URL(site);
            URLConnection conn = url.openConnection();
            //esse cookie na linha abaixo é exigido pelo servidor da caixa. Ou seja, não fazia parte
            //da biblioteca original. basta apagá-lo
            conn.setRequestProperty("Cookie", "security=true");
           tmp = File.createTempFile( "download", ".tmp", tmpDir );
            is = conn.getInputStream();
 			os = new BufferedOutputStream( new FileOutputStream( tmp ) );
 			copyStream( is, os );
			tmp.renameTo( output );
			tmp = null;
		} catch ( IOException e ) {
            Log.i("erro", e.toString());
			throw new RuntimeException( e );
		} finally {
			if ( tmp != null ) { try { tmp.delete(); tmp = null; } catch (Exception ignore) {;} }
			if ( is != null  ) { try { is.close();   is = null;  } catch (Exception ignore) {;} }
			if ( os != null  ) { try { os.close();   os = null;  } catch (Exception ignore) {;} }
		}
	}
    
	/**
	 * Copy from one stream to another.  Throws IOException in the event of error
	 * (for example, SD card is full)
	 * 
	 * @param is		Input stream.
	 * @param os		Output stream.
	 */
	public static void copyStream( InputStream is, OutputStream os ) throws IOException {
		byte[] buffer = new byte[ BUFFER_SIZE ];
		copyStream( is, os, buffer, BUFFER_SIZE );
    }

	/**
	 * Copy from one stream to another.  Throws IOException in the event of error
	 * (for example, SD card is full)
	 * 
	 * @param is			Input stream.
	 * @param os			Output stream.
	 * @param buffer		Temporary buffer to use for copy.
	 * @param bufferSize	Size of temporary buffer, in bytes.
	 */
	public static void copyStream( InputStream is, OutputStream os,
								      byte[] buffer, int bufferSize ) throws IOException {
		try {
			for (;;) {
				int count = is.read( buffer, 0, bufferSize );
				if ( count == -1 ) { break; }
				os.write( buffer, 0, count );
			}
		} catch ( IOException e ) {
			throw e;
		}
	}
}
