package dailyproject.moon.IO.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @program: daily_test
 * @description: 自己实现的锁
 * @create: 2021-09-23 10:19
 **/

public class Mlock implements Lock {

    private volatile int i=0;

    @Override
    public void lock () {
        synchronized (this){
            while (i !=0){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i=1;
        }
    }

    @Override
    public void lockInterruptibly () throws InterruptedException {

    }

    @Override
    public boolean tryLock () {
        return false;
    }

    @Override
    public boolean tryLock (long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock () {
        synchronized (this){
            i=0;
            this.notifyAll();
        }
    }

    @Override
    public Condition newCondition () {
        return null;
    }
}
