package server;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import server.Randomizer;

public abstract class Timer {
    private ScheduledThreadPoolExecutor ses;
    protected String file;
    protected String name;
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    public void start() {
        if (this.ses != null && !this.ses.isShutdown() && !this.ses.isTerminated()) {
            return;
        }
        this.ses = new ScheduledThreadPoolExecutor(20, new RejectedThreadFactory());
        this.ses.setKeepAliveTime(10L, TimeUnit.MINUTES);
        this.ses.allowCoreThreadTimeOut(true);
        this.ses.setMaximumPoolSize(20);
        this.ses.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
    }

    public ScheduledThreadPoolExecutor getSES() {
        return this.ses;
    }

    public void stop() {
        if (this.ses != null) {
            this.ses.shutdown();
        }
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime, long delay) {
        if (this.ses == null) {
            return null;
        }
        return this.ses.scheduleAtFixedRate(new LoggingSaveRunnable(r, this.file), delay, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime) {
        if (this.ses == null) {
            return null;
        }
        return this.ses.scheduleAtFixedRate(new LoggingSaveRunnable(r, this.file), 0L, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        if (this.ses == null) {
            return null;
        }
        return this.ses.schedule(new LoggingSaveRunnable(r, this.file), delay, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(Runnable r, long timestamp) {
        return this.schedule(r, timestamp - System.currentTimeMillis());
    }

    private class RejectedThreadFactory
    implements ThreadFactory {
        private final AtomicInteger threadNumber2 = new AtomicInteger(1);
        private final String tname;

        public RejectedThreadFactory() {
            this.tname = Timer.this.name + Randomizer.nextInt();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(this.tname + "-W-" + threadNumber.getAndIncrement() + "-" + this.threadNumber2.getAndIncrement());
            return t;
        }
    }

    private static class LoggingSaveRunnable
    implements Runnable {
        Runnable r;
        String file;

        public LoggingSaveRunnable(Runnable r, String file) {
            this.r = r;
            this.file = file;
        }

        @Override
        public void run() {
            try {
                this.r.run();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static class PingTimer
    extends Timer {
        private static PingTimer instance = new PingTimer();

        private PingTimer() {
            this.name = "Pingtimer";
        }

        public static PingTimer getInstance() {
            return instance;
        }
    }

    public static class ShowTimer
    extends Timer {
        private static ShowTimer instance = new ShowTimer();

        private ShowTimer() {
            this.name = "ShowTimer";
        }

        public static ShowTimer getInstance() {
            return instance;
        }
    }

    public static class CheatTimer
    extends Timer {
        private static CheatTimer instance = new CheatTimer();

        private CheatTimer() {
            this.name = "Cheattimer";
        }

        public static CheatTimer getInstance() {
            return instance;
        }
    }

    public static class EtcTimer
    extends Timer {
        private static EtcTimer instance = new EtcTimer();

        private EtcTimer() {
            this.name = "Etctimer";
        }

        public static EtcTimer getInstance() {
            return instance;
        }
    }

    public static class CloneTimer
    extends Timer {
        private static CloneTimer instance = new CloneTimer();

        private CloneTimer() {
            this.name = "Clonetimer";
        }

        public static CloneTimer getInstance() {
            return instance;
        }
    }

    public static class EventTimer
    extends Timer {
        private static EventTimer instance = new EventTimer();

        private EventTimer() {
            this.name = "Eventtimer";
        }

        public static EventTimer getInstance() {
            return instance;
        }
    }

    public static class BuffTimer
    extends Timer {
        private static BuffTimer instance = new BuffTimer();

        private BuffTimer() {
            this.name = "Bufftimer";
        }

        public static BuffTimer getInstance() {
            return instance;
        }

        public void cancelBuffTimer(long time, final ScheduledFuture<?> a) {
            EtcTimer tMan = EtcTimer.getInstance();
            tMan.schedule(new Runnable(){

                @Override
                public void run() {
                    a.cancel(true);
                }
            }, time);
        }
    }

    public static class MobTimer
    extends Timer {
        private static MobTimer instance = new MobTimer();

        private MobTimer() {
            this.name = "MobTimer";
        }

        public static MobTimer getInstance() {
            return instance;
        }
    }

    public static class MapTimer
    extends Timer {
        private static MapTimer instance = new MapTimer();

        private MapTimer() {
            this.name = "Maptimer";
        }

        public static MapTimer getInstance() {
            return instance;
        }
    }

    public static class LogoutTimer
    extends Timer {
        private static LogoutTimer instance = new LogoutTimer();

        private LogoutTimer() {
            this.name = "LogoutTimer";
        }

        public static LogoutTimer getInstance() {
            return instance;
        }
    }

    public static class WorldTimer
    extends Timer {
        private static WorldTimer instance = new WorldTimer();

        private WorldTimer() {
            this.name = "Worldtimer";
        }

        public static WorldTimer getInstance() {
            return instance;
        }
    }
}

