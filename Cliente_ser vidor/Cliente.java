import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.*;
//Ejemplo de socket TCP
class Cliente
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
    
    Socket conexion = new Socket("localhost",50000);  //Crear socket de conexión

    //Crear stream de salida para mandarselo al servidor
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    //Crear stream de entrada para leer los datos del servidor.
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    

    /*        ...:::ACT IND:::... 
      Modificar paara que envie 10000 numeros de punto flotante con writeDouble
    

    double number=1.0;
    System.out.println(System.currentTimeMillis());
    for(int i=0; i<10000; i++){
      salida.writeDouble(number+i);
    }
    System.out.println(System.currentTimeMillis());

    */

    /*          ...::: ACT IND :::...
      Modificar para que envie 10000 utilizando ByteBuffer
    */

    ByteBuffer b = ByteBuffer.allocate(10000*8);
    double x =1.0;
    for(int i=0; i<10000; i++){
      b.putDouble(x+i);
    }
    byte[] a = b.array();
    System.out.println(System.currentTimeMillis());
    salida.write(a);
    System.out.println(System.currentTimeMillis());


    /* 
      PARTE DEL EJEMPLO INICIAL DEL PROFESOR

    // enva un entero de 32 bits
    salida.writeInt(123);

    // envia un numero punto flotante
    salida.writeDouble(1234567890.1234567890);

    // envia una cadena
    salida.write("hola".getBytes());  //El método write envia bytes, por eso se convierte

    // recibe una cadena de 4 bytes
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4); //El método .read puede leer solo una fracción del mensaje por eso se usa la función
    System.out.println(new String(buffer,"UTF-8"));

    // envia 5 numeros punto flotante de 64bits
    //Primero se empacan los numeros con BYteBUffer 5 numeros * 8bytes (64 bits)
    ByteBuffer b = ByteBuffer.allocate(5*8);
    //Agregamos al objeto
    b.putDouble(1.1);
    b.putDouble(1.2);
    b.putDouble(1.3);
    b.putDouble(1.4);
    b.putDouble(1.5);
    //Para mandarlo lo convertimos a un arreglo de Bytes
    byte[] a = b.array();
    salida.write(a);
    */


    //Cerramos las conexiones
    salida.close();
    entrada.close();
    conexion.close();    
  }
}
