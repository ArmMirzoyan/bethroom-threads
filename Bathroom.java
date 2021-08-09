import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.*;

public class Bathroom {

    final static int MAX_SPACE = 3;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition menWaiting = lock.newCondition();
    private final Condition womenWaiting = lock.newCondition();
    private int womenWaitingN = 0;
    private int menWaitingN = 0;
    private int womenUsingN = 0;
    private int menUsingN = 0;
    private int free_places = 0;

    public static void main(String[] args) {
        final Bathroom bathroom = new Bathroom();

        Thread male1 = new Thread(() -> bathroom.achieveMen("Aram"));
        Thread male2 = new Thread(() -> bathroom.achieveMen("Hayk"));
        Thread male3 = new Thread(() -> bathroom.achieveMen("Armen"));
        Thread male4 = new Thread(() -> bathroom.achieveMen("Gago"));
        Thread male5 = new Thread(() -> bathroom.achieveMen("Vazgen"));


        Thread female1 = new Thread(() -> bathroom.achieveWomen("Anna"));
        Thread female2 = new Thread(() -> bathroom.achieveWomen("Armine"));
        Thread female3 = new Thread(() -> bathroom.achieveWomen("Nelly"));
        Thread female4 = new Thread(() -> bathroom.achieveWomen("Varduhi"));
        Thread female5 = new Thread(() -> bathroom.achieveWomen("Lusine"));

        male1.start();
        male2.start();
        male3.start();
        male4.start();
        male5.start();
        female1.start();
        female2.start();
        female3.start();
        female4.start();
        female5.start();
    }

    public void achieveWomen(String name) {
        lock.lock();
        womenWaitingN += 1;

        while (free_places == MAX_SPACE || menUsingN > 0) {
            try {
                womenWaiting.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        free_places += 1;
        womenUsingN += 1;
        womenWaitingN -= 1;
        lock.unlock();
        System.out.println(name + " is in bathroom");
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " is freed bathroom");

        lock.lock();
        womenUsingN -= 1;
        free_places -= 1;
        if (menWaitingN > 0) {
            menWaiting.signal();
        } else {
            womenWaiting.signal();
        }
        lock.unlock();
    }

    public void achieveMen(String name) {
        lock.lock();
        menWaitingN += 1;

        while (free_places == MAX_SPACE || womenUsingN > 0) {
            try {
                menWaiting.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        free_places += 1;
        menUsingN += 1;
        menWaitingN -= 1;
        lock.unlock();
        System.out.println(name + " is in the bathroom");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " is freed bathroom");
        lock.lock();
        menUsingN -= 1;
        free_places -= 1;
        if (womenWaitingN > 0) {
            womenWaiting.signal();
        } else {
            menWaiting.signal();
        }
        lock.unlock();
    }
}