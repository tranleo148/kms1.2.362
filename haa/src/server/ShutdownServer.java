// 
// Decompiled by Procyon v0.5.36
// 

package server;

import java.util.Iterator;
import java.util.List;
import handling.cashshop.CashShopServer;
import handling.login.LoginServer;
import handling.channel.ChannelServer;
import java.util.ArrayList;
import server.marriage.MarriageManager;
import handling.auction.AuctionServer;
import handling.world.World;
import tools.packet.CWvsContext;
import java.util.concurrent.atomic.AtomicInteger;

public class ShutdownServer implements ShutdownServerMBean
{
    public static final ShutdownServer instance;
    public static AtomicInteger CompletedLoadingThreads;
    public long startTime;
    public int mode;
    
    public ShutdownServer() {
        this.startTime = 0L;
        this.mode = 0;
    }
    
    public static ShutdownServer getInstance() {
        return ShutdownServer.instance;
    }
    
    @Override
    public void shutdown() {
        this.run();
    }
    
    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        if (this.mode == 0) {
      World.Broadcast.broadcastMessage(CWvsContext.serverNotice(0, "", "서버가 곧 종료됩니다. 안전한 저장을 위해 게임을 종료해주세요."));
             World.Guild.save();
            World.Alliance.save();
            AuctionServer.saveItems();
            MarriageManager.getInstance().saveAll();
            System.out.println("Shutdown 1 has completed.");
            ++this.mode;
        }
        else if (this.mode == 1) {
            ++this.mode;
            System.out.println("Shutdown 2 commencing...");
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(0, "", "서버가 종료됩니다. 안전한 저장을 위해 게임을 종료해주세요."));           final AllShutdown sd = new AllShutdown();
            sd.start();
        }
    }
    
    static {
        instance = new ShutdownServer();
        ShutdownServer.CompletedLoadingThreads = new AtomicInteger(0);
    }
    
    private static class LoadingThread extends Thread
    {
        protected String LoadingThreadName;
        
        private LoadingThread(final Runnable r, final String t, final Object o) {
            super(new NotifyingRunnable(r, o, t));
            this.LoadingThreadName = t;
        }
        
        @Override
        public synchronized void start() {
            System.out.println("[Loading...] Started " + this.LoadingThreadName + " Thread");
            super.start();
        }
    }
    
    private static class NotifyingRunnable implements Runnable
    {
        private String LoadingThreadName;
        private long StartTime;
        private Runnable WrappedRunnable;
        private final Object ToNotify;
        
        private NotifyingRunnable(final Runnable r, final Object o, final String name) {
            this.WrappedRunnable = r;
            this.ToNotify = o;
            this.LoadingThreadName = name;
        }
        
        @Override
        public void run() {
            this.StartTime = System.currentTimeMillis();
            this.WrappedRunnable.run();
            System.out.println("[Loading Completed] " + this.LoadingThreadName + " | Completed in " + (System.currentTimeMillis() - this.StartTime) + " Milliseconds. (" + (ShutdownServer.CompletedLoadingThreads.get() + 1) + "/10)");
            synchronized (this.ToNotify) {
                ShutdownServer.CompletedLoadingThreads.incrementAndGet();
                this.ToNotify.notify();
            }
        }
    }
    
    private class AllShutdown extends Thread
    {
        @Override
        public void run() {
            final List<LoadingThread> loadingThreads = new ArrayList<LoadingThread>();
            final Integer[] array;
            final Integer[] chs = array = ChannelServer.getAllInstance().toArray(new Integer[0]);
            for (final int i : array) {
                try {
                    final LoadingThread thread = new LoadingThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            final ChannelServer cs = ChannelServer.getInstance(i);
                            cs.shutdown();
                        }
                    }, "Channel " + i, (Object)this);
                    loadingThreads.add(thread);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (final Thread t : loadingThreads) {
                t.start();
            }
            synchronized (this) {
                try {
                    this.wait();
                }
                catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            while (ShutdownServer.CompletedLoadingThreads.get() != loadingThreads.size()) {
                synchronized (this) {
                    try {
                        this.wait();
                    }
                    catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            Timer.WorldTimer.getInstance().stop();
            Timer.MapTimer.getInstance().stop();
            Timer.MobTimer.getInstance().stop();
            Timer.BuffTimer.getInstance().stop();
            Timer.CloneTimer.getInstance().stop();
            Timer.EventTimer.getInstance().stop();
            Timer.EtcTimer.getInstance().stop();
            Timer.PingTimer.getInstance().stop();
            LoginServer.shutdown();
            CashShopServer.shutdown();
            AuctionServer.shutdown();
            System.out.println("[Fully Shutdowned in " + (System.currentTimeMillis() - ShutdownServer.this.startTime) / 1000L + " seconds]");
            System.out.println("Shutdown 2 has finished.");
            try {
                Thread.sleep(1000L);
            }
            catch (Exception ex) {}
            finally {
                System.exit(0);
            }
        }
    }
}
