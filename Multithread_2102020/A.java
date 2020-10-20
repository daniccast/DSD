import java.lang.Thread;

class A1 extends Thread{
        static long n;
        static Object obj= new Object();

        public void run() {
            for(int i=0; i<100000;i++){
                n++;
            }
        }

    

    public static void main(String[] args) {
        A1 t1= new A1();
        A1 t2= new A1();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(n);    
    }
}