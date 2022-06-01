// 
// Decompiled by Procyon v0.5.36
// 

package client.inventory;

import static client.inventory.EquipAdditions.values;
import java.util.Arrays;
import java.util.List;

public enum EquipAdditions
{
    elemboost("elemVol", "elemVol", true), 
    mobcategory("category", "damage"), 
    critical("prob", "damage"), 
    boss("prob", "damage"), 
    mobdie("hpIncOnMobDie", "mpIncOnMobDie"), 
    hpmpchange("hpChangerPerTime", "mpChangerPerTime"), 
    skill("id", "level");
    
    private final String i1;
    private final String i2;
    private final boolean element;
    
    private EquipAdditions(final String i1, final String i2) {
        this.i1 = i1;
        this.i2 = i2;
        this.element = false;
    }
    
    private EquipAdditions(final String i1, final String i2, final boolean element) {
        this.i1 = i1;
        this.i2 = i2;
        this.element = element;
    }
    
    public final String getValue1() {
        return this.i1;
    }
    
    public final String getValue2() {
        return this.i2;
    }
    
    public final boolean isElement() {
        return this.element;
    }
    
    public static final EquipAdditions fromString(final String str) {
        for (final EquipAdditions s : values()) {
            if (s.name().equalsIgnoreCase(str)) {
                return s;
            }
        }
        return null;
    }
    
    public enum RingSet
    {
        Source_Ring(new Integer[] { 1112435, 1112436, 1112437, 1112438, 1112439 }), 
        Angelic_Ring(new Integer[] { 1112585, 1112586, 1112594 }), 
        Job_Ring(new Integer[] { 1112427, 1112428, 1112429, 1112405, 1112445, 1112591, 1112592 }), 
        Evolving_Ring(new Integer[] { 1112499, 1112500, 1112501, 1112502, 1112503, 1112504, 1112505, 1112506, 1112507, 1112508, 1112509, 1112510, 1112511, 1112512, 1112513, 1112514, 1112515, 1112516, 1112517, 1112518, 1112519, 1112520, 1112521, 1112522, 1112523, 1112524, 1112525, 1112526, 1112527, 1112528, 1112529, 1112530, 1112531, 1112532, 1112533 }), 
        Evolving_Ring_II(new Integer[] { 1112614, 1112615, 1112616, 1112617, 1112618, 1112619, 1112620, 1112621, 1112622, 1112623, 1112624, 1112625, 1112626, 1112627, 1112628, 1112629, 1112630, 1112631, 1112632, 1112633, 1112634, 1112635, 1112636, 1112637, 1112638, 1112639, 1112640, 1112641, 1112642, 1112643, 1112644, 1112645, 1112646, 1112647, 1112648 });
        
        public List<Integer> id;
        
        private RingSet(final Integer[] ids) {
            this.id = Arrays.asList(ids);
        }
    }
}
