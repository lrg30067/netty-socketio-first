package com.sinovoice.hcicloud.nettysocketiofirst.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest{

    private static ReentrantLock rLock = new ReentrantLock();

    @Test
    public void test() throws InterruptedException {
        LockockThread lockockThread = new LockockThread();
        TryLockThread tryLockThread = new TryLockThread();
        LockInterruptThread lockInterruptThread = new LockInterruptThread();

        Thread thread1 = new Thread(lockInterruptThread);
        Thread thread2 = new Thread(lockInterruptThread);

        thread1.start();
        thread2.start();

        //Thread.sleep(1000*2);
        //thread2.interrupt();  // 中断线程

        System.out.println("Done");
    }

    class LockInterruptThread implements Runnable {

        @Override
        public void run() {
            try {
                rLock.lockInterruptibly();   //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
                try {
                    System.out.println(Thread.currentThread().getName()+"得到了锁");
                    long startTime = System.currentTimeMillis();
                    for(    ;     ;) {
                        if(System.currentTimeMillis() - startTime >= Integer.MAX_VALUE)
                            break;
                        //插入数据
                    }
                }
                finally {
                    System.out.println(Thread.currentThread().getName()+"执行finally");
                    rLock.unlock();
                    System.out.println(Thread.currentThread().getName()+"释放了锁");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class LockockThread implements Runnable {

        @Override
        public void run() {
            try {
                rLock.lock();
                //休眠5秒 用于第二次请求 抛出异常
                System.out.println("当前【持有锁】的线程编号：" + Thread.currentThread().getId());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("线程：【" + Thread.currentThread().getId() + "】,开始解锁");
                rLock.unlock();
                System.out.println("线程：【" + Thread.currentThread().getId() + "】，解锁完成！");
            }
        }
    }

    /**
     * 测试 trylock
     */
    class TryLockThread implements Runnable {

        @Override
        public void run() {
            try {
                if (rLock.tryLock()) {
                    //休眠5秒 用于第二次请求 抛出异常
                    System.out.println("当前【持有锁】的线程编号：" + Thread.currentThread().getId());
                    Thread.sleep(1000);
                } else {
                    System.out.println("当前资源已被锁定！【" + Thread.currentThread().getId() + "】线程号被踢出！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("线程：【" + Thread.currentThread().getId() + "】,开始解锁");
                    rLock.unlock();
                    System.out.println("线程：【" + Thread.currentThread().getId() + "】，解锁完成！");
                } catch (Exception e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }


}
