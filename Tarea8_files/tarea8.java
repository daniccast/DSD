import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Timestamp;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * javac -cp gson-2.8.6.jar tarea8.java
 * java -cp gson-2.8.6.jar:. tarea8
 */


public class tarea8{
    static Scanner leer = new Scanner(System.in);
    static Gson j = new GsonBuilder().create();

    static class Usuario{
        String email;
        String nombre;
        String apellido_paterno;
        String apellido_materno;
        String fecha_nacimiento;
        String telefono;
        String genero;
        byte[] foto;
    }



    public static String pedir_email() {
        String email;
        System.out.print("Introduce el email: ");
        email= leer.nextLine();
        return email;
    }

    public static void recoleccion_datos() {
        Usuario persona = new Usuario();
        try {
            System.out.print("Introduce nombre: ");
            persona.nombre= URLEncoder.encode(leer.nextLine(),"UTF-8");

            System.out.print("Introduce apellido paterno: ");
            persona.apellido_paterno= URLEncoder.encode(leer.nextLine(),"UTF-8");

            System.out.print("Introduce apellido materno: ");
            persona.apellido_materno= URLEncoder.encode(leer.nextLine(),"UTF-8");

            System.out.print("Introduce email: ");
            persona.email= URLEncoder.encode(leer.nextLine(),"UTF-8");

            System.out.print("Introduce fecha de naciemiento (aaaa/mm/dd): ");
            persona.fecha_nacimiento= URLEncoder.encode(leer.nextLine(),"UTF-8");

            System.out.print("Introduce telefono: ");
            persona.telefono= URLEncoder.encode(leer.nextLine(),"UTF-8");

            System.out.print("Introduce genero (Solo F o M): ");
            persona.genero= URLEncoder.encode(leer.nextLine(),"UTF-8");
            
            persona.foto=null;  
            
            String s= j.toJson(persona);
            //System.out.println(s);

            operacion(j.toJson(persona).replace("{",URLEncoder.encode("{", "UTF-8")).
            replace("}",URLEncoder.encode("}", "UTF-8")).replace("\"",URLEncoder.encode("\"", "UTF-8"))
            ,"alta");
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }
        
    }

    public static void operacion(String email, String op) {
        
        try {
            URL url = new URL("http://20.190.47.172:8080/Servicio/rest/ws/"+op);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            
            // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
            conexion.setRequestMethod("POST");
            
            // indica que la petición estará codificada como URL
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            
            String parametros;

            if(!op.equals("alta"))
                parametros = "email=" + URLEncoder.encode(email,"UTF-8");
            else
                parametros = "usuario=" + email;
            
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
            
            System.out.println("OK");

            conexion.disconnect();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }
        
    }

    


    public static void main(String[] args) {
        String opcion;
        do{

            System.out.println("Menu");
            System.out.println("a. Alta usuario");
            System.out.println("b. Consulta usuario");
            System.out.println("c. Borra usuario");
            System.out.println("d. Borrar todos los usuarios");
            System.out.println("e. Salir");
            opcion= leer.nextLine();

            switch (opcion) {
                case "a":
                    recoleccion_datos();
                break;
                case "b":
                    operacion(pedir_email(),"consulta");
                break;
                case "c":
                    operacion(pedir_email(),"borra");
                break;
                case "d":
                    operacion("nothing","borrar_usuarios");
                break;
                default:
                    break;
            }

        }while(!opcion.equals("e"));
    }
}