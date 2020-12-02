import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

class problema2{
    //clase coordenada
    static class Coordenada{
        int x;
        int y;
        int z;
    }

    //leer el archivo de coordenadas
    static byte[] lee_archivo(String nombre_archivo) throws Exception{
        FileInputStream f = new FileInputStream(nombre_archivo);
        byte[] buffer;
        try{
            buffer = new byte[f.available()];
            f.read(buffer);
        }
        finally{
            f.close();
        }
        return buffer;
  }

  public static void main(String[] args) throws Exception{
      /**
       * Leer el archivo coordenadas.txt.
        Utilizar GSON para convertir el contenido del archivo en un arreglo de objetos Java de tipo Coordenada. Sea V el arreglo obtenido.
        Calcular la sumatoria de V[i].x, i=0 hasta i=999.
        Calcular la sumatoria de V[i].y, i=0 hasta i=999.
        Calcular la sumatoria de V[i].z, i=0 hasta i=999.
       */

       byte[] contenido= lee_archivo("coordenadas.txt");
       Gson builder = new GsonBuilder().create();
       String s = new String(contenido, StandardCharsets.UTF_8);

       Coordenada[] V = (Coordenada[])builder.fromJson(s, Coordenada[].class);

       int sumatoria=0, i=0;

       for(i=0; i<=999; i++)
        sumatoria+=V[i].x;
        System.out.println("Sumatoria X= "+ sumatoria);

        sumatoria=0;
        for(i=0; i<=999; i++)
            sumatoria+=V[i].y;
        System.out.println("Sumatoria Y= "+ sumatoria);

        sumatoria=0;
        for(i=0; i<=999; i++)
            sumatoria+=V[i].z;
        System.out.println("Sumatoria Z= "+ sumatoria);
  }
}