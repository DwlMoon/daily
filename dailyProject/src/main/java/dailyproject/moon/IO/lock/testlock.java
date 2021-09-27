package dailyproject.moon.IO.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-09-22 19:47
 **/

public class testlock {

    public static int m=0;

    public static Lock lock=new Mlock();

    public static void main (String[] args) throws Exception {

        Thread[] threads = new Thread[100];
        for (int i = 0; i <threads.length ; i++) {
            threads[i]=new Thread(){
                @Override
                public void run () {
                    try {
                        lock.lock();
                        for (int j = 0; j <100 ; j++) {
                            m++;
                        }
                    }finally {
                        lock.unlock();
                    }
                }
            };
        }

        for (Thread t:threads) t.start();
        for (Thread t:threads) t.join();
        System.out.println(m);

    }
}
