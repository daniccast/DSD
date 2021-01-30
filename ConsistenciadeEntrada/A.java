import java.lang.Thread;
//Sin lock

class A extends Thread{
    static long n=0;
    public void run() {
        for(int i=0; i<100; i++)
            n++;
    }
    public static void main(String[] args)  throws Exception {
        A t1= new A();
        A t2= new A();
        A t3= new A();

        t1.start();
        t2.start();
        t3.start();
        
        t1.join();
        t2.join();
        t3.join();

        System.out.println(n);
    }
}