package Testing.JDBC;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class WriteManager {
    private static final BlockingQueue<WriteTask> queue = new LinkedBlockingQueue<>();
    private static final AtomicBoolean isUserWriting = new AtomicBoolean(false);
    private static final Object writeLock = new Object();  // untuk sinkronisasi yang aman

    static {
        Thread writeWorker = new Thread(() -> {
            while (true) {
                try {
                    WriteTask task = queue.take(); // Ambil task dari antrian

                    if (task.isFlushSignal()) {
                        task.getRunnable().run();
                        continue;
                    }

                    if (task.isUser()) {
                        synchronized (writeLock) {
                            isUserWriting.set(true); // Setel bahwa user sedang menulis
                            task.getRunnable().run();
                            isUserWriting.set(false); // User selesai menulis
                            writeLock.notifyAll();    // Bangunkan bot yang sedang menunggu
                        }
                    } else {
                        synchronized (writeLock) {
                            // Bot hanya menulis jika tidak ada user yang sedang menulis
                            while (isUserWriting.get()) {
                                writeLock.wait(); // Tunda sampai user selesai
                            }
                            task.getRunnable().run();
                        }
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "WriteWorker");
        writeWorker.setDaemon(true);
        writeWorker.start();
    }

    public static void writeAsUser(Runnable r) {
        queue.add(new WriteTask(r, true));
    }

    public static void writeAsBot(Runnable r) {
        queue.add(new WriteTask(r, false));
    }

    public static void flushAndRun(Runnable afterFlush) {
        queue.add(new WriteTask(afterFlush, false, true));
    }

    private static class WriteTask {
        private final Runnable runnable;
        private final boolean isUser;
        private final boolean isFlushSignal;

        public WriteTask(Runnable runnable, boolean isUser) {
            this(runnable, isUser, false);
        }

        public WriteTask(Runnable runnable, boolean isUser, boolean isFlushSignal) {
            this.runnable = runnable;
            this.isUser = isUser;
            this.isFlushSignal = isFlushSignal;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public boolean isUser() {
            return isUser;
        }

        public boolean isFlushSignal() {
            return isFlushSignal;
        }
    }
}
