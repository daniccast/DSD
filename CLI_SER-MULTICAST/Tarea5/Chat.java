import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.net.DatagramSocket;
import java.lang.Thread;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class Chat{

    static void envia_mensaje(byte[] buffer,String ip,int puerto) throws IOException{
    /*  Función para mandar mensajes a un grupo MULTICAST
        Recibe el mensaje, la direccion IP clase D del grupo y el puerto
    */
    DatagramSocket socket = new DatagramSocket();
    InetAddress grupo = InetAddress.getByName(ip);
    DatagramPacket paquete = new DatagramPacket(buffer,buffer.length,grupo,puerto);
    socket.send(paquete);
    socket.close();
  }

  static byte[] recibe_mensaje(MulticastSocket socket,int longitud) throws IOException{
    /*
        Método para recibir mensajes.
        Recibe: Un socket de tipo Multicast, la longitud del mensaje a recibir
    */
    byte[] buffer = new byte[longitud];
    DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
    socket.receive(paquete);
    //System.out.println("Mensaje recibido" + buffer);
    return buffer;
  }

  static class Worker extends Thread{
    public void run() {
     // En un ciclo infinito se recibirán los mensajes enviados al grupo 
     // 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
    try {
        InetAddress grupo = InetAddress.getByName("230.0.0.0");
        MulticastSocket socket = new MulticastSocket(50000);
        socket.joinGroup(grupo); //Unir el socket al grupo
        while(true){
            byte[] a = recibe_mensaje(socket,4);
            System.out.println(new String(a,"UTF-8"));
         }
    } catch (Exception e) {
       System.out.println(e);
    }
   
    }
  }

  public static void main(String[] args) throws Exception{
    Worker w = new Worker();
    w.start();

    String nombre = args[0] + ":"; //NOmbre de quien usa el programa.
    byte [] name= nombre.getBytes();
    BufferedReader b;
    ByteArrayOutputStream mensaje = new ByteArrayOutputStream();
    // En un ciclo infinito se leerá los mensajes del teclado y se enviarán
    // al grupo 230.0.0.0 a través del puerto 50000.
    while(true){

        b = new BufferedReader(new InputStreamReader(System.in));
        mensaje.write(name);
        mensaje.write(b.toString().getBytes());
        envia_mensaje(mensaje.toByteArray(),"230.0.0.0",50000);

        mensaje.reset();
    }


  }

}