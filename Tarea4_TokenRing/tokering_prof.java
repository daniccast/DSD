import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.nio.ByteBuffer;


class tokenring_prof{
    static DataInputStream entrada;
    static DataOutputStream salida;
    static boolean primera_vez= true;
    static String ip;
    static long token= 0;
    static int nodo;

    static class Worker extends Thread{
        public void run() {
            //Algoritmo 1
            try {
                ServerSocket servidor = new ServerSocket(50000);
                Socket conexion = servidor.accept();
                entrada = new DataInputStream(conexion.getInputStream());
                //System.out.println("Server creado");
            } catch (Exception e) {
                System.out.println(e);
            }
            }
    }

    public static void main(String[] args) throws Exception{
        if (args.length !=2 ){
            System.err.println("Se debe pasar como parametros el numero de nodo y la IP del siguiente nodo");
            System.exit(1);
        }

        nodo= Integer.valueOf(args[0]);
        ip= args[1];
        //System.out.println("Here" + ip);
        //Algoritmo 2
        Worker w = new Worker();
        w.start();
        Socket conexion = null;

        while(true){
            try {
                conexion= new Socket(ip,50000);
                break;
            } catch (Exception e) {
                Thread.sleep(500);
            }
        }

        salida= new DataOutputStream(conexion.getOutputStream());
        w.join();

        while(true){
            if(nodo == 0){
                if(primera_vez){
                    primera_vez= false;
                    //System.out.println("Here");
                } else{
                    token= entrada.readLong();
                }
            }  else{
                token= entrada.readLong();
                token+=1;
                System.out.println("Salida: "+ token);
                salida.writeLong(token);
            }
        }

    }
    
}