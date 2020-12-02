
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class problema3{
    
    
    public static void operacion() {
        
        try {
            URL url = new URL("http://sisdis.sytes.net:8080/Servicio/rest/ws/prueba");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            /**Enviar
             * Se deberá enviar los siguientes parámetros:
                a=10
                b=20
                c=30
             */

            // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
            conexion.setRequestMethod("POST");
            
            // indica que la petición estará codificada como URL
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            
            String parametros="a=10&b=20&c=30";

            OutputStream os = conexion.getOutputStream();
        
            os.write(parametros.getBytes());    
            os.flush();
            
            // se debe verificar si hubo error
            if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            
            String respuesta;
            
            // el método web regresa una string en formato JSON
            while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
            

            conexion.disconnect();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }
        
    }
    public static void main(String[] args) {
        operacion();
    }
}