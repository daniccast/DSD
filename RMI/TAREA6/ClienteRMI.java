import java.rmi.Naming;

public class ClienteRMI{
  static int N= 4;
  static int[][] A = new int[N][N];
  static int[][] B = new int[N][N];
  static int[][] C = new int[N][N];

  static int[][] parte_matriz(int[][] A,int inicio){
    int[][] M = new int[N/2][N];
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N; j++)
        M[i][j] = A[i + inicio][j];
    return M;
  }

  static void acomoda_matriz(int[][] C,int[][] A,int renglon,int columna){
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N/2; j++)
        C[i + renglon][j + columna] = A[i][j];
  }

  static void llenar_Matriz(){
    //Llenar matrices A Y B
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++){
        A[i][j] = 2 * i - j;
        B[i][j] = 2 * i + j;
        C[i][j] = 0;
      }
  }

  static void transponer_b() {
    //Transponer B A Bt
    int x=0;
    // transpone la matriz B, la matriz traspuesta queda en B
    for (int i = 0; i < N; i++)
        for (int j = 0; j < i; j++){
            x = B[i][j];
            B[i][j] = B[j][i];
            B[j][i] = x;
        }
  }

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

  public static void main(String args[]) throws Exception{
    //Urls de los nodos
    String url0 = "rmi://localhost/prueba";
    String url1 = "rmi://13.66.219.78/prueba";
    String url2 = "rmi://52.151.12.1/prueba";
    String url3 = "rmi://52.235.88.20/prueba";



    llenar_Matriz();
    transponer_b();

    // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
    InterfaceRMI r0 = (InterfaceRMI)Naming.lookup(url0);
    InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(url1);
    InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(url2);
    InterfaceRMI r3 = (InterfaceRMI)Naming.lookup(url3);
   
    
    int[][] A1 = parte_matriz(A,0);
    int[][] A2 = parte_matriz(A,N/2);
    int[][] B1 = parte_matriz(B,0);
    int[][] B2 = parte_matriz(B,N/2);

    int[][] C1 = r0.multiplica_matrices(A1,B1);
    int[][] C2 = r1.multiplica_matrices(A1,B2);
    int[][] C3 = r2.multiplica_matrices(A2,B1);
    int[][] C4 = r3.multiplica_matrices(A2,B2);

    
    acomoda_matriz(C,C1,0,0);
    acomoda_matriz(C,C2,0,N/2);
    acomoda_matriz(C,C3,N/2,0);
    acomoda_matriz(C,C4,N/2,N/2);


    transponer_b();
    if(N==4){
      System.out.println("MATRIZ A");
      imprimir_matriz(A,N,N);   

      System.out.println("\n\nMATRIZ B");
      imprimir_matriz(B,N,N); 

      System.out.println("\n\nMATRIZ C");
      imprimir_matriz(C,N,N);  
    }
    
    System.out.println("Checksum: " + checksum());

  }
}