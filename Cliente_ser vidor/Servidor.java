import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.*;
class Servidor
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    //Crear un socket
    ServerSocket servidor = new ServerSocket(50000);
    //accept devuelve un socket (que es con el que se establece la conexión)
    Socket conexion = servidor.accept();

    //Creamos los streams de salida y de entrada
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    /*            ...::: ACT IND :::...
      Modificación 1, recibir con writedouble en cliente

      double x;
      System.out.println(System.currentTimeMillis());
      for(int i=0; i<10000; i++){
        x = entrada.readDouble();
        System.out.println(x);
      }
      System.out.println(System.currentTimeMillis());
    */
    
    /*        ...::: ACT IND :::...
      Modificación 1, recibir con byteBuffer en cliente
    
    */
    byte[] a = new byte[10000*8];
    System.out.println(System.currentTimeMillis());
    read(entrada,a,0,10000*8);
    ByteBuffer b = ByteBuffer.wrap(a); //Pasar a bytebuffer
    System.out.println(System.currentTimeMillis());
    for (int i = 0; i < 10000; i++)
      System.out.println(b.getDouble());   



    /* 
      ---PARTE DEL EJEMPLO DEL PROFESOR
    // recibe un entero de 32 bits
    int n = entrada.readInt();
    System.out.println(n);

    // recibe un numero punto flotante
    double x = entrada.readDouble();
    System.out.println(x);

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    // envia una cadena
    salida.write("HOLA".getBytes());

    // recibe 5 numeros punto flotante
    byte[] a = new byte[5*8];
    read(entrada,a,0,5*8);
    ByteBuffer b = ByteBuffer.wrap(a); //Pasar a bytebuffer
    for (int i = 0; i < 5; i++)
      System.out.println(b.getDouble());    
    */
    salida.close();
    entrada.close();
    conexion.close();
  }
}
