// 
// Decompiled by Procyon v0.5.36
// 

package client.inventory;

import java.io.Serializable;

public class MapleImp implements Serializable
{
    private static final long serialVersionUID = 91795493413738569L;
    private int itemid;
    private short fullness;
    private short closeness;
    private byte state;
    private byte level;
    
    public MapleImp(final int itemid) {
        this.fullness = 0;
        this.closeness = 0;
        this.state = 1;
        this.level = 1;
        this.itemid = itemid;
    }
    
    public final int getItemId() {
        return this.itemid;
    }
    
    public final byte getState() {
        return this.state;
    }
    
    public final void setState(final int state) {
        this.state = (byte)state;
    }
    
    public final byte getLevel() {
        return this.level;
    }
    
    public final void setLevel(final int level) {
        this.level = (byte)level;
    }
    
    public final short getCloseness() {
        return this.closeness;
    }
    
    public final void setCloseness(final int closeness) {
        this.closeness = (short)Math.min(100, closeness);
    }
    
    public final short getFullness() {
        return this.fullness;
    }
    
    public final void setFullness(final int fullness) {
        this.fullness = (short)Math.min(1000, fullness);
    }
    
    public enum ImpFlag
    {
        REMOVED(1), 
        SUMMONED(2), 
        TYPE(4), 
        STATE(8), 
        FULLNESS(16), 
        CLOSENESS(32), 
        CLOSENESS_LEFT(64), 
        MINUTES_LEFT(128), 
        LEVEL(256), 
        FULLNESS_2(512), 
        UPDATE_TIME(1024), 
        CREATE_TIME(2048), 
        AWAKE_TIME(4096), 
        SLEEP_TIME(8192), 
        MAX_CLOSENESS(16384), 
        MAX_DELAY(32768), 
        MAX_FULLNESS(65536), 
        MAX_ALIVE(131072), 
        MAX_MINUTES(262144);
        
        private final int i;
        
        private ImpFlag(final int i) {
            this.i = i;
        }
        
        public final int getValue() {
            return this.i;
        }
        
        public final boolean check(final int flag) {
            return (flag & this.i) == this.i;
        }
    }
}
