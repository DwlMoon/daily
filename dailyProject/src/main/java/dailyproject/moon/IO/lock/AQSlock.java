package dailyproject.moon.IO.lock;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @program: daily_test
 * @description: 基于AQS的锁机制
 * @create: 2021-09-23 10:38
 **/

public class AQSlock implements Lock {

    private myaqs myaqs=new myaqs();

    @Override
    public void lock () {
        myaqs.acquire(1);
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
        myaqs.release(1);
    }

    @Override
    public Condition newCondition () {
        return null;
    }

    //内部类实现
    private class myaqs extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire (int arg) {
            assert arg== 1;
            //使用CAS判断是否能获取state资源，拿到锁（可以将0变为1，则可以获取）
            if (compareAndSetState(0,1)){
                //获取资源后设置互斥锁（排他或独占）
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }


        @Override
        protected boolean tryRelease (int arg) {
            assert arg ==1;
            if (isHeldExclusively()){
                throw new  IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively () {
            //判断拥有锁的线程是否为当前线程
            return getExclusiveOwnerThread()==Thread.currentThread();
        }
    }

}
