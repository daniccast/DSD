import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.*;

/*Desarrollar un programa en Java que se conecte al host "sisdis.sytes.net" a través del puerto 10000 utilizando sockets TCP.

Una vez realizada la conexión, el programa deberá enviar al servidor: 3 como entero de 32 bits (int), enviar 10 como entero de 32 bits (int) y enviar 50 como entero de 32 bits (int). Entonces el programa deberá recibir del servidor un número entero de 32 bits (int).

¿Qué valor recibe el programa del servidor?*/


class Cliente{
  
    public static void main(String[] args) throws Exception
    {
      
      Socket conexion = new Socket("sisdis.sytes.net",10000);  //Crear socket de conexión
  
      //Crear stream de salida para mandarselo al servidor
      DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
      //Crear stream de entrada para leer los datos del servidor.
      DataInputStream entrada = new DataInputStream(conexion.getInputStream());
  
      //enva un entero de 32 bits
      salida.writeInt(3);
      salida.writeInt(10);
      salida.writeInt(50);
      
      //Recibe entero de 32 bits
      int n = entrada.readInt();
      System.out.println(n);
      
      //Cerramos las conexiones
      salida.close();
      entrada.close();
      conexion.close();    
    }
}