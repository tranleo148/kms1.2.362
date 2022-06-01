package client.inventory;

import static client.inventory.MaplePet.PetFlag.values;
import database.DatabaseConnection;
import java.awt.Point;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.MapleItemInformationProvider;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;

public class MaplePet
        implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private String name;
    private String ExceptionList;

    public enum PetFlag {
        /*  43 */ ITEM_PICKUP(1, 5190000, 5191000),
        /*  44 */ EXPAND_PICKUP(2, 5190002, 5191002),
        /*  45 */ AUTO_PICKUP(4, 5190003, 5191003),
        /*  46 */ UNPICKABLE(8, 5190005, -1),
        /*  47 */ LEFTOVER_PICKUP(16, 5190004, 5191004),
        /*  48 */ HP_CHARGE(32, 5190001, 5191001),
        /*  49 */ MP_CHARGE(64, 5190006, -1),
        /*  50 */ PET_BUFF(128, 5190010, -1),
        /*  51 */ PET_TRAINING(256, 5190011, -1),
        /*  52 */ PET_GIANT(512, 5190012, -1),
        /*  53 */ PET_SHOP(1024, 5190013, -1);
        private final int i;

        PetFlag(int i, int item, int remove) {
            /*  57 */ this.i = i;
            /*  58 */ this.item = item;
            /*  59 */ this.remove = remove;
        }
        private final int item;
        private final int remove;

        public final int getValue() {
            /*  63 */ return this.i;
        }

        public final boolean check(int flag) {
            /*  67 */ return ((flag & this.i) == this.i);
        }

        public static final PetFlag getByAddId(int itemId) {
            /*  71 */ for (PetFlag flag : values()) {
                /*  72 */ if (flag.item == itemId) {
                    /*  73 */ return flag;
                }
            }
            /*  76 */ return null;
        }

        public static final PetFlag getByDelId(int itemId) {
            /*  80 */ for (PetFlag flag : values()) {
                /*  81 */ if (flag.remove == itemId) {
                    /*  82 */ return flag;
                }
            }
            /*  85 */ return null;
        }
    }

    /*  92 */    private int Fh = 0;
    private int stance = 0;
    private int color = -1;
    private int petitemid;
    private int secondsLeft = 0;
    private int buffSkillId = 0;
    private int buffSkillId2 = 0;
    private int wonderGrade = -1;
    private long uniqueid;
    private Point pos;
    /*  95 */    private byte fullness = 100;
    private byte level = 1;
    private byte summoned = 0;
    /*  96 */    private short inventorypos = 0;
    private short closeness = 0;
    private short flags = 0;
    private short size = 100;
    private boolean changed = false;

    private MaplePet(int petitemid, long uniqueid) {
        /* 100 */ this.petitemid = petitemid;
        /* 101 */ this.uniqueid = uniqueid;
    }

    private MaplePet(int petitemid, long uniqueid, short inventorypos) {
        /* 105 */ this.petitemid = petitemid;
        /* 106 */ this.uniqueid = uniqueid;
        /* 107 */ this.inventorypos = inventorypos;
    }

    public static final MaplePet loadFromDb(int itemid, long petid, short inventorypos) {
        /* 111 */ Connection con = null;
        /* 112 */ PreparedStatement ps = null;
        /* 113 */ ResultSet rs = null;
        try {
            /* 115 */ MaplePet ret = new MaplePet(itemid, petid, inventorypos);

            /* 117 */ con = DatabaseConnection.getConnection();
            /* 118 */ ps = con.prepareStatement("SELECT * FROM pets WHERE petid = ?");
            /* 119 */ ps.setLong(1, petid);

            /* 121 */ rs = ps.executeQuery();
            /* 122 */ if (!rs.next()) {
                /* 123 */ rs.close();
                /* 124 */ ps.close();
                /* 125 */ return null;
            }

            /* 128 */ ret.setName(rs.getString("name"));
            /* 129 */ ret.setCloseness(rs.getShort("closeness"));
            /* 130 */ ret.setLevel(rs.getByte("level"));
            /* 131 */ ret.setFullness(rs.getByte("fullness"));
            /* 132 */ ret.setSecondsLeft(rs.getInt("seconds"));
            /* 133 */ ret.setFlags(rs.getShort("flags"));
            /* 134 */ ret.setBuffSkillId(rs.getInt("petbuff"));
            /* 135 */ ret.setPetSize(rs.getShort("size"));
            /* 136 */ ret.setWonderGrade(rs.getInt("wonderGrade"));
            /* 137 */ ret.setExceptionList(rs.getString("exceptionlist"));
            ret.setBuffSkillId2(rs.getInt("petbuff2"));
            /* 138 */ ret.setChanged(false);
            /* 139 */ rs.close();
            /* 140 */ ps.close();
            /* 141 */ con.close();

            /* 143 */ return ret;
            /* 144 */        } catch (SQLException ex) {
            /* 145 */ Logger.getLogger(MaplePet.class.getName()).log(Level.SEVERE, (String) null, ex);
            /* 146 */ return null;
        } finally {
            try {
                /* 149 */ if (con != null) {
                    /* 150 */ con.close();
                }
                /* 152 */ if (ps != null) {
                    /* 153 */ ps.close();
                }
                /* 155 */ if (rs != null) {
                    /* 156 */ rs.close();
                }
                /* 158 */            } catch (SQLException se) {
                /* 159 */ se.printStackTrace();
            }
        }
    }

    public final void saveToDb(Connection con) {
        /* 165 */ if (!isChanged()) {
            return;
        }
        /* 168 */ PreparedStatement ps = null;
        try {
            /* 170 */ ps = con.prepareStatement("UPDATE pets SET name = ?, level = ?, closeness = ?, fullness = ?, seconds = ?, flags = ?, petbuff = ?, size = ?, wonderGrade = ?, exceptionlist = ?, petbuff2 = ? WHERE petid = ?");
            /* 171 */ ps.setString(1, this.name);
            /* 172 */ ps.setByte(2, this.level);
            /* 173 */ ps.setShort(3, this.closeness);
            /* 174 */ ps.setByte(4, this.fullness);
            /* 175 */ ps.setInt(5, this.secondsLeft);
            /* 176 */ ps.setShort(6, this.flags);
            /* 177 */ ps.setInt(7, this.buffSkillId);
            /* 178 */ ps.setShort(8, this.size);
            /* 179 */ ps.setInt(9, this.wonderGrade);
            /* 180 */ ps.setString(10, (this.ExceptionList == null) ? "" : this.ExceptionList);
            ps.setInt(11, this.buffSkillId2);
            /* 181 */ ps.setLong(12, this.uniqueid);
            /* 182 */ ps.executeUpdate();
            /* 183 */ ps.close();
            /* 184 */ setChanged(false);
            /* 185 */        } catch (SQLException ex) {
            /* 186 */ ex.printStackTrace();
        } finally {
            try {
                /* 189 */ if (ps != null) {
                    /* 190 */ ps.close();
                }
                /* 192 */            } catch (SQLException se) {
                /* 193 */ se.printStackTrace();
            }
        }
    }

    public static final MaplePet createPet(int itemid, long uniqueid) {
        /* 199 */ return createPet(itemid, MapleItemInformationProvider.getInstance().getName(itemid), 1, 0, 100, uniqueid, 18000, (short) 0);
    }

    public static final MaplePet createPet(int itemid, String name, int level, int closeness, int fullness, long uniqueid, int secondsLeft, short flag) {
        /* 203 */ if (uniqueid <= -1L) {
            /* 204 */ uniqueid = MapleInventoryIdentifier.getInstance();
        }
        /* 206 */ Connection con = null;
        /* 207 */ PreparedStatement pse = null;
        /* 208 */ ResultSet rs = null;
        try {
            /* 210 */ con = DatabaseConnection.getConnection();
            /* 211 */ pse = con.prepareStatement("INSERT INTO pets (petid, name, level, closeness, fullness, seconds, flags, size, wonderGrade, exceptionlist) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            /* 212 */ pse.setLong(1, uniqueid);
            /* 213 */ pse.setString(2, name);
            /* 214 */ pse.setByte(3, (byte) level);
            /* 215 */ pse.setShort(4, (short) closeness);
            /* 216 */ pse.setByte(5, (byte) fullness);
            /* 217 */ pse.setInt(6, secondsLeft);
            /* 218 */ pse.setShort(7, flag);
            /* 219 */ pse.setShort(8, (short) 100);
            /* 220 */ pse.setInt(9, PetDataFactory.getWonderGrade(itemid));
            /* 221 */ pse.setString(10, "");
            /* 222 */ pse.executeUpdate();
            /* 223 */ pse.close();
            /* 224 */ con.close();
            /* 225 */        } catch (SQLException ex) {
            /* 226 */ ex.printStackTrace();
            /* 227 */ return null;
        } finally {
            try {
                /* 230 */ if (con != null) {
                    /* 231 */ con.close();
                }
                /* 233 */ if (pse != null) {
                    /* 234 */ pse.close();
                }
                /* 236 */ if (rs != null) {
                    /* 237 */ rs.close();
                }
                /* 239 */            } catch (SQLException se) {
                /* 240 */ se.printStackTrace();
            }
        }
        /* 243 */ MaplePet pet = new MaplePet(itemid, uniqueid);
        /* 244 */ pet.setName(name);
        /* 245 */ pet.setLevel(level);
        /* 246 */ pet.setFullness(fullness);
        /* 247 */ pet.setCloseness(closeness);
        /* 248 */ pet.setFlags(flag);
        /* 249 */ pet.setSecondsLeft(secondsLeft);
        /* 250 */ pet.setWonderGrade(PetDataFactory.getWonderGrade(itemid));
        /* 251 */ pet.setPetSize((short) 100);
        /* 252 */ return pet;
    }

    public final String getName() {
        /* 256 */ return this.name;
    }

    public final void setName(String name) {
        /* 260 */ this.name = name;
        /* 261 */ setChanged(true);
    }

    public final boolean getSummoned() {
        /* 265 */ return (this.summoned > 0);
    }

    public final byte getSummonedValue() {
        /* 269 */ return this.summoned;
    }

    public final void setSummoned(byte summoned) {
        /* 273 */ this.summoned = summoned;
    }

    public final short getInventoryPosition() {
        /* 277 */ return this.inventorypos;
    }

    public final void setInventoryPosition(short inventorypos) {
        /* 281 */ this.inventorypos = inventorypos;
    }

    public long getUniqueId() {
        /* 285 */ return this.uniqueid;
    }

    public final short getCloseness() {
        /* 289 */ return this.closeness;
    }

    public final void setCloseness(int closeness) {
        /* 293 */ this.closeness = (short) closeness;
        /* 294 */ setChanged(true);
    }

    public final byte getLevel() {
        /* 298 */ return this.level;
    }

    public final void setLevel(int level) {
        /* 302 */ this.level = (byte) level;
        /* 303 */ setChanged(true);
    }

    public final byte getFullness() {
        /* 307 */ return 100;
    }

    public final void setFullness(int fullness) {
        /* 311 */ this.fullness = (byte) fullness;
        /* 312 */ setChanged(true);
    }

    public final short getFlags() {
        /* 316 */ return 503;
    }

    public final void setFlags(int fffh) {
        /* 333 */ this.flags = (short) fffh;
        /* 334 */ setChanged(true);
    }

    public final int getBuffSkillId() {
        /* 338 */ return this.buffSkillId;
    }

    public final void setBuffSkillId(int skillId) {
        /* 342 */ this.buffSkillId = skillId;
        /* 343 */ setChanged(true);
    }

    public final int getBuffSkillId2() {
        /* 338 */ return this.buffSkillId2;
    }

    public final void setBuffSkillId2(int skillId) {
        /* 342 */ this.buffSkillId2 = skillId;
        /* 343 */ setChanged(true);
    }
    
    public final int getWonderGrade() {
        /* 347 */ return this.wonderGrade;
    }

    public final void setWonderGrade(int grade) {
        /* 351 */ this.wonderGrade = grade;
        /* 352 */ setChanged(true);
    }

    public final short getPetSize() {
        /* 356 */ return this.size;
    }

    public final void setPetSize(short size) {
        /* 360 */ this.size = size;
        /* 361 */ setChanged(true);
    }

    public void addPetSize(short size) {
        /* 365 */ this.size = (short) (this.size + size);
        /* 366 */ setChanged(true);
    }

    public final int getFh() {
        /* 370 */ return this.Fh;
    }

    public final void setFh(int Fh) {
        /* 374 */ this.Fh = Fh;
    }

    public final Point getPos() {
        /* 378 */ return this.pos;
    }

    public final void setPos(Point pos) {
        /* 382 */ this.pos = pos;
    }

    public final int getStance() {
        /* 386 */ return this.stance;
    }

    public final void setStance(int stance) {
        /* 390 */ this.stance = stance;
    }

    public final int getColor() {
        /* 394 */ return this.color;
    }

    public final void setColor(int color) {
        /* 398 */ this.color = color;
    }

    public final int getPetItemId() {
        /* 402 */ return this.petitemid;
    }

    public final boolean canConsume(int itemId) {
        /* 406 */ MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
        /* 407 */ for (Iterator<Integer> iterator = mii.getItemEffect(itemId).getPetsCanConsume().iterator(); iterator.hasNext();) {
            int petId = ((Integer) iterator.next()).intValue();
            /* 408 */ if (petId == this.petitemid) {
                /* 409 */ return true;
            }
        }

        /* 412 */ return false;
    }

    public final void updatePosition(List<LifeMovementFragment> movement) {
        /* 416 */ for (LifeMovementFragment move : movement) {
            /* 417 */ if (move instanceof LifeMovement) {
                /* 418 */ if (move instanceof server.movement.AbsoluteLifeMovement) {
                    /* 419 */ setPos(((LifeMovement) move).getPosition());
                }
                /* 421 */ setStance(((LifeMovement) move).getNewstate());
            }
        }
    }

    public final int getSecondsLeft() {
        /* 427 */ return this.secondsLeft;
    }

    public final void setSecondsLeft(int sl) {
        /* 431 */ this.secondsLeft = sl;
        /* 432 */ setChanged(true);
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean changed) {
         this.changed = changed;
    }

    public String getExceptionList() {
        /* 444 */ return this.ExceptionList;
    }

    public void setExceptionList(String ExceptionList) {
        /* 448 */ this.ExceptionList = ExceptionList;
        /* 449 */ setChanged(true);
    }
}