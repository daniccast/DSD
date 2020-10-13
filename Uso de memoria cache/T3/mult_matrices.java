import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.nio.ByteBuffer;
import java.lang.*;

class mult_matrices{
    static int N =1000;  // 100, 200, 300, 500, 1000
    static int[][] A = new int[N][N];
    static int[][] B = new int[N][N];
    static int[][] C = new int[N][N];

    static void imprimir_matriz(int [][] m, int N, int M) {
        int i,j;
        for(i=0;i<N;i++){
            for(j=0;j<M;j++){
                System.out.print(m[i][j]+"\t");
            }     
            System.out.print("\n");
        }
    }

    static long checksum(){
        int i,j;
        long sum=0;
        for(i=0;i<N;i++)
            for(j=0;j<N;j++)
                sum+= C[i][j];
        return sum;
        
    }

    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0)
        {
        int n = f.read(b,posicion,longitud); 
        posicion += n;
        longitud -= n;
        }
    }

    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion= conexion;
        }
        public void run(){
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                int nodo,i,j;

                //RECIBE NODO DE ENTRADA
                nodo=entrada.readInt();


                if(nodo==1 || nodo==2){
                     //MANDA MATRIZ A1 
                    ByteBuffer A1= ByteBuffer.allocate(N*N*4);
                    for(i=0;i<N/2;i++)
                        for(j=0;j<N;j++){
                            A1.putInt(A[i][j]);
                        }
                    byte[] send= A1.array();
                    salida.write(send);
                } else if(nodo==3 || nodo==4){
                     //MANDA MATRIZ A2 
                     ByteBuffer A2= ByteBuffer.allocate(N*N*4);
                     for(i=N/2;i<N;i++)
                         for(j=0;j<N;j++){
                             A2.putInt(A[i][j]);
                         }
                     byte[] send= A2.array();
                     salida.write(send);
                }

                if(nodo==1 || nodo ==3){
                    //MANDA MATRIZ B1
                    ByteBuffer B1= ByteBuffer.allocate(N*N*4);
                    for(i=0;i<N/2;i++)
                        for(j=0;j<N;j++){
                            B1.putInt(B[i][j]);
                        }
                    byte[] send= B1.array();
                    salida.write(send);
                }else if(nodo==2 || nodo ==4){
                     //MANDA MATRIZ B2
                     ByteBuffer B2= ByteBuffer.allocate(N*N*4);
                     for(i=N/2;i<N;i++)
                         for(j=0;j<N;j++){
                             B2.putInt(B[i][j]);
                         }
                    byte[] send= B2.array();
                     salida.write(send);
                }

                //RECIBE MATRIZ Cn
                int ri=0,rf=0,ci=0,cf=0;

                if(nodo==1){
                    ri=0;
                    rf=N/2;
                    ci=0;
                    cf=N/2;
                }else if(nodo==2){
                    ri=0;
                    rf=N/2;
                    ci=N/2;
                    cf=N;
                }else if(nodo==3){
                    ri=N/2;
                    rf=N;
                    ci=0;
                    cf=N/2;
                }else if(nodo==4){
                    ri=N/2;
                    rf=N;
                    ci=N/2;
                    cf=N;
                }
                
                //RECIBIR Cn
                byte[] entermat= new byte[N*N*2];
                read(entrada,entermat,0,N*N*2);
                ByteBuffer matrizCn= ByteBuffer.wrap(entermat);
                for(i=ri;i<rf;i++)
                     for(j=ci;j<cf;j++){
                        C[i][j]= matrizCn.getInt();
                    }

                salida.close();
                entrada.close();
                conexion.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {

         if (args.length != 1){ //Verificar que hay un nodo
            System.err.println("Uso:");
            System.err.println("java PI <nodo>");
            System.exit(0);
          }
      
          int nodo = Integer.valueOf(args[0]);

          if(nodo==0){ //Si el nodo es el 0 (server)
            ServerSocket servidor = new ServerSocket(50000);
            Worker w[]= new Worker[4];

            //Llenar matrices A Y B
            for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                A[i][j] = 2 * i + j;
                B[i][j] = 2 * i - j;
                C[i][j] = 0;
            }

            //Transponer B A Bt
            int x=0;
            // transpone la matriz B, la matriz traspuesta queda en B
            for (int i = 0; i < N; i++)
                for (int j = 0; j < i; j++){
                    x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }

            for(int i=0;i<4; i++){
                Socket conexion = servidor.accept();
                w[i]= new Worker(conexion);
                w[i].start();
            }
            
            for(int i=0;i<4;i++)
                w[i].join();

            
            //Transponer de nuevo B
            for (int i = 0; i < N; i++)
            for (int j = 0; j < i; j++){
                x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }

           
            System.out.println("Checksum "+checksum());

            if(N==4){
                System.out.println("MATRIZ A");
                imprimir_matriz(A,N,N);   
    
                System.out.println("\n\nMATRIZ B");
                imprimir_matriz(B,N,N); 

                System.out.println("\n\nMATRIZ C");
                imprimir_matriz(C,N,N);     
            }
           
          }

          else{
            Socket conexion = null;
            int numeroRA,numeroRB;
            for(;;) //reintentos de conexión
                try
                {
                    conexion = new Socket("localhost",50000);
                    break;
                }
                catch (Exception e)
                {
                    Thread.sleep(100);
                }
            
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            
            salida.writeInt(nodo); //Manda el nodo que tiene
            int[][] matA= new int [N/2][N];
            int[][] matB= new int [N/2][N];
            int[][] matC= new int[N/2][N/2];

            //RECIBIR An
            byte[] mr= new byte[N*N*4];
            read(entrada,mr,0,N*N*4);
            ByteBuffer matriz= ByteBuffer.wrap(mr);

            for(int i=0;i<N/2;i++)
             for(int j=0;j<N;j++){
                matA[i][j]= matriz.getInt();
            }

            //imprimir_matriz(matA,N/2,N);

            //RECIBIR Bn
            read(entrada,mr,0,N*N*4);
            matriz= ByteBuffer.wrap(mr);

            for(int i=0;i<N/2;i++)
             for(int j=0;j<N;j++){
                matB[i][j]= matriz.getInt();
            }
            //imprimir_matriz(matB,N/2,N);

            
            //REALIZAR MULTIPLICACION
            for (int i = 0; i < N/2; i++)
                for (int j = 0; j < N/2; j++)
                    for (int k = 0; k < N; k++)
                        matC[i][j] += matA[i][k] * matB[j][k];
        
            //imprimir_matriz(matC,N/2,N/2);

            //Mandar MATRIZ Cn
            ByteBuffer Cn= ByteBuffer.allocate(N*N*2);
            for(int i=0;i<N/2;i++)
                for(int j=0;j<N/2;j++){
                    Cn.putInt(matC[i][j]);
                }
            byte[] send= Cn.array();
            salida.write(send);

            salida.close();
            entrada.close();
            conexion.close(); 

          }
    }
}