package server;

import client.MapleCharacter;
import client.MapleQuestStatus;
import client.StructPotentialItem;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import provider.MapleDataType;
import server.enchant.EnchantFlag;
import server.enchant.EquipmentEnchant;
import server.enchant.StarForceStats;
import server.quest.MapleQuest;
import tools.Pair;
import tools.Triple;
import tools.packet.CWvsContext;

public class MapleItemInformationProvider {

    private static final MapleItemInformationProvider instance = new MapleItemInformationProvider();

    protected final MapleDataProvider chrData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Character.wz"));

    protected final MapleDataProvider etcData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Etc.wz"));

    protected final MapleDataProvider itemData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Item.wz"));

    protected final MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/String.wz"));

    protected final Map<Integer, ItemInformation> dataCache = new HashMap<>();

    protected final Map<String, List<Triple<String, Point, Point>>> afterImage = new HashMap<>();

    protected final Map<Integer, List<StructPotentialItem>> potentialCache = new HashMap<>();

    protected final Map<Integer, SecondaryStatEffect> itemEffects = new HashMap<>();

    protected final Map<Integer, SecondaryStatEffect> itemEffectsEx = new HashMap<>();

    protected final Map<Integer, Integer> mobIds = new HashMap<>();

    protected final Map<Integer, Pair<Integer, Integer>> potLife = new HashMap<>();

    protected final Map<Integer, Triple<Pair<List<Integer>, List<Integer>>, List<Integer>, Integer>> androids = new HashMap<>();

    protected final Map<Integer, Triple<Integer, List<Integer>, List<Integer>>> monsterBookSets = new HashMap<>();

    protected final Map<Integer, StructSetItem> setItems = new HashMap<>();

    protected final List<Pair<Integer, String>> itemNameCache = new ArrayList<>();

    protected final Map<Integer, Integer> scrollUpgradeSlotUse = new HashMap<>();

    protected final Map<Integer, Integer> cursedCache = new HashMap<>();

    protected final Map<Integer, Integer> successCache = new HashMap<>();

    protected final Map<Integer, List<Triple<Boolean, Integer, Integer>>> potentialOpCache = new HashMap<>();

    public void runEtc() {
        if (!this.setItems.isEmpty() || !this.potentialCache.isEmpty()) {
            return;
        }
        MapleData setsData = this.etcData.getData("SetItemInfo.img");
        for (MapleData dat : setsData) {
            StructSetItem itemz = new StructSetItem();
            itemz.setItemID = Integer.parseInt(dat.getName());
            itemz.completeCount = (byte) MapleDataTool.getIntConvert("completeCount", dat, 0);
            itemz.jokerPossible = (MapleDataTool.getIntConvert("jokerPossible", dat, 0) > 0);
            itemz.zeroWeaponJokerPossible = (MapleDataTool.getIntConvert("zeroWeaponJokerPossible", dat, 0) > 0);
            for (MapleData level : dat.getChildByPath("ItemID")) {
                if (level.getType() != MapleDataType.INT) {
                    for (MapleData leve : level) {
                        if (!leve.getName().equals("representName") && !leve.getName().equals("typeName")) {
                            itemz.itemIDs.add(Integer.valueOf(MapleDataTool.getInt(leve)));
                        }
                    }
                    continue;
                }
                itemz.itemIDs.add(Integer.valueOf(MapleDataTool.getInt(level)));
            }
            for (MapleData level : dat.getChildByPath("Effect")) {
                StructSetItem.SetItem itez = new StructSetItem.SetItem();
                itez.incPDD = MapleDataTool.getIntConvert("incPDD", level, 0);
                itez.incMDD = MapleDataTool.getIntConvert("incMDD", level, 0);
                itez.incSTR = MapleDataTool.getIntConvert("incSTR", level, 0);
                itez.incDEX = MapleDataTool.getIntConvert("incDEX", level, 0);
                itez.incINT = MapleDataTool.getIntConvert("incINT", level, 0);
                itez.incLUK = MapleDataTool.getIntConvert("incLUK", level, 0);
                itez.incACC = MapleDataTool.getIntConvert("incACC", level, 0);
                itez.incPAD = MapleDataTool.getIntConvert("incPAD", level, 0);
                itez.incMAD = MapleDataTool.getIntConvert("incMAD", level, 0);
                itez.incSpeed = MapleDataTool.getIntConvert("incSpeed", level, 0);
                itez.incMHP = MapleDataTool.getIntConvert("incMHP", level, 0);
                itez.incMMP = MapleDataTool.getIntConvert("incMMP", level, 0);
                itez.incMHPr = MapleDataTool.getIntConvert("incMHPr", level, 0);
                itez.incMMPr = MapleDataTool.getIntConvert("incMMPr", level, 0);
                itez.incAllStat = MapleDataTool.getIntConvert("incAllStat", level, 0);
                itez.option1 = MapleDataTool.getIntConvert("Option/1/option", level, 0);
                itez.option2 = MapleDataTool.getIntConvert("Option/2/option", level, 0);
                itez.option1Level = MapleDataTool.getIntConvert("Option/1/level", level, 0);
                itez.option2Level = MapleDataTool.getIntConvert("Option/2/level", level, 0);
                if (level.getChildByPath("activeSkill") != null) {
                    for (MapleData skill : level.getChildByPath("activeSkill")) {
                        itez.activeSkills.put(Integer.valueOf(MapleDataTool.getIntConvert("id", skill, 0)), Byte.valueOf((byte) MapleDataTool.getIntConvert("level", skill, 0)));
                    }
                }
                itemz.items.put(Integer.valueOf(Integer.parseInt(level.getName())), itez);
            }
            this.setItems.put(Integer.valueOf(itemz.setItemID), itemz);
        }
        MapleDataDirectoryEntry e = (MapleDataDirectoryEntry) this.etcData.getRoot().getEntry("Android");
        for (MapleDataEntry d : e.getFiles()) {
            MapleData iz = this.etcData.getData("Android/" + d.getName());
            int gender = 0;
            List<Integer> hair = new ArrayList<>(), face = new ArrayList<>(), skin = new ArrayList<>();
            for (MapleData ds : iz.getChildByPath("costume/hair")) {
                hair.add(Integer.valueOf(MapleDataTool.getInt(ds, 30000)));
            }
            for (MapleData ds : iz.getChildByPath("costume/face")) {
                face.add(Integer.valueOf(MapleDataTool.getInt(ds, 20000)));
            }
            for (MapleData ds : iz.getChildByPath("costume/skin")) {
                skin.add(Integer.valueOf(MapleDataTool.getInt(ds, 0)));
            }
            for (MapleData ds : iz.getChildByPath("info")) {
                if (ds.getName().equals("gender")) {
                    gender = MapleDataTool.getInt(ds, 0);
                }
            }
            this.androids.put(Integer.valueOf(Integer.parseInt(d.getName().substring(0, 4))), new Triple<>(new Pair<>(hair, face), skin, Integer.valueOf(gender)));
        }
        MapleData lifesData = this.etcData.getData("ItemPotLifeInfo.img");
        for (MapleData d : lifesData) {
            if (d.getChildByPath("info") != null && MapleDataTool.getInt("type", d.getChildByPath("info"), 0) == 1) {
                this.potLife.put(Integer.valueOf(MapleDataTool.getInt("counsumeItem", d.getChildByPath("info"), 0)), new Pair<>(Integer.valueOf(Integer.parseInt(d.getName())), Integer.valueOf(d.getChildByPath("level").getChildren().size())));
            }
        }
        List<Triple<String, Point, Point>> thePointK = new ArrayList<>();
        List<Triple<String, Point, Point>> thePointA = new ArrayList<>();
        MapleDataDirectoryEntry a = (MapleDataDirectoryEntry) this.chrData.getRoot().getEntry("Afterimage");
        for (MapleDataEntry b : a.getFiles()) {
            MapleData iz = this.chrData.getData("Afterimage/" + b.getName());
            List<Triple<String, Point, Point>> thePoint = new ArrayList<>();
            Map<String, Pair<Point, Point>> dummy = new HashMap<>();
            for (MapleData i : iz) {
                for (MapleData xD : i) {
                    if (xD.getName().contains("prone") || xD.getName().contains("double") || xD.getName().contains("triple")) {
                        continue;
                    }
                    if ((b.getName().contains("bow") || b.getName().contains("Bow")) && !xD.getName().contains("shoot")) {
                        continue;
                    }
                    if ((b.getName().contains("gun") || b.getName().contains("cannon")) && !xD.getName().contains("shot")) {
                        continue;
                    }
                    if (dummy.containsKey(xD.getName())) {
                        if (xD.getChildByPath("lt") != null) {
                            Point point1 = (Point) xD.getChildByPath("lt").getData();
                            Point ourLt = (Point) ((Pair) dummy.get(xD.getName())).left;
                            if (point1.x < ourLt.x) {
                                ourLt.x = point1.x;
                            }
                            if (point1.y < ourLt.y) {
                                ourLt.y = point1.y;
                            }
                        }
                        if (xD.getChildByPath("rb") != null) {
                            Point point1 = (Point) xD.getChildByPath("rb").getData();
                            Point ourRb = (Point) ((Pair) dummy.get(xD.getName())).right;
                            if (point1.x > ourRb.x) {
                                ourRb.x = point1.x;
                            }
                            if (point1.y > ourRb.y) {
                                ourRb.y = point1.y;
                            }
                        }
                        continue;
                    }
                    Point lt = null, rb = null;
                    if (xD.getChildByPath("lt") != null) {
                        lt = (Point) xD.getChildByPath("lt").getData();
                    }
                    if (xD.getChildByPath("rb") != null) {
                        rb = (Point) xD.getChildByPath("rb").getData();
                    }
                    dummy.put(xD.getName(), new Pair<>(lt, rb));
                }
            }
            for (Map.Entry<String, Pair<Point, Point>> ez : dummy.entrySet()) {
                if (((String) ez.getKey()).length() > 2 && ((String) ez.getKey()).substring(((String) ez.getKey()).length() - 2, ((String) ez.getKey()).length() - 1).equals("D")) {
                    thePointK.add(new Triple<>(ez.getKey(), (Point) ((Pair) ez.getValue()).left, (Point) ((Pair) ez.getValue()).right));
                    continue;
                }
                if (((String) ez.getKey()).contains("PoleArm")) {
                    thePointA.add(new Triple<>(ez.getKey(), (Point) ((Pair) ez.getValue()).left, (Point) ((Pair) ez.getValue()).right));
                    continue;
                }
                thePoint.add(new Triple<>(ez.getKey(), (Point) ((Pair) ez.getValue()).left, (Point) ((Pair) ez.getValue()).right));
            }
            this.afterImage.put(b.getName().substring(0, b.getName().length() - 4), thePoint);
        }
        this.afterImage.put("katara", thePointK);
        this.afterImage.put("aran", thePointA);
    }

    public void runItems() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM wz_itemdata");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemInformation(rs);
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM wz_itemequipdata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemEquipData(rs);
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM wz_itemadddata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemAddData(rs);
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM wz_itemrewarddata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemRewardData(rs);
            }
            rs.close();
            ps.close();
            for (Map.Entry<Integer, ItemInformation> entry : this.dataCache.entrySet()) {
                if (GameConstants.getInventoryType(((Integer) entry.getKey()).intValue()) == MapleInventoryType.EQUIP || GameConstants.getInventoryType(((Integer) entry.getKey()).intValue()) == MapleInventoryType.CODY) {
                    finalizeEquipData(entry.getValue());
                }
            }
            cachePotentialItems();
            cachePotentialOption();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public final int getPotentialOptionID(int level, boolean additional, int itemtype) {
        List<Integer> potentials = new ArrayList<>();
        int i = 0;
        while (potentials.size() <= 0 || potentials.isEmpty()) {
            potentials = new ArrayList<>();
            potentialSet(potentials, level, additional, itemtype);
            if (i++ == 10) {
                break;
            }
        }
        if (potentials.size() <= 0 || potentials.isEmpty()) {
            System.out.println(level + "레벨 " + itemtype + "타입 아이템의 잠재능력 리스트 0개 -_- / 에디셔널 여부 : " + additional);
        }
        return ((Integer) potentials.get(Randomizer.nextInt(potentials.size()))).intValue();
    }

    private void potentialSet(List<Integer> potentials, int level, boolean additional, int itemtype) {
        if (isWeaponPotential(itemtype)) {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(10)), level, additional, itemtype);
        } else {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(11)), level, additional, itemtype);
        }
        if (!isWeaponPotential(itemtype)) {
            if (isAccessoryPotential(itemtype)) {
                addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(40)), level, additional, itemtype);
            } else if (additional) {
                addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(20)), level, additional, itemtype);
            }
        }
        if (itemtype / 10 == 100) {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(51)), level, additional, itemtype);
        }
        if (itemtype / 10 == 104) {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(52)), level, additional, itemtype);
        }
        if (itemtype / 10 == 106) {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(53)), level, additional, itemtype);
        }
        if (itemtype / 10 == 107) {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(55)), level, additional, itemtype);
        }
        if (itemtype / 10 == 108) {
            addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(54)), level, additional, itemtype);
        }
        addPotential(potentials, this.potentialOpCache.get(Integer.valueOf(-1)), level, additional, itemtype);
    }

    private void addPotential(List<Integer> potentials, List<Triple<Boolean, Integer, Integer>> list, int level, boolean additional, int itemtype) {
        for (Triple<Boolean, Integer, Integer> potential : list) {
            if (additional) {
                if (!((Boolean) potential.left).booleanValue()) {
                    continue;
                }
                if ((itemtype == 1190 || itemtype == 1191) && ((Integer) potential.right).intValue() % 1000 >= 601 && ((Integer) potential.right).intValue() % 1000 <= 604) {
                    continue;
                }
                if (((Integer) potential.right).intValue() % 1000 < 40) {
                    continue;
                }
                if (level != ((Integer) potential.right).intValue() / 10000) {
                    continue;
                }
                potentials.add((Integer) potential.right);
                continue;
            }
            if (((Boolean) potential.left).booleanValue()) {
                continue;
            }
            if ((itemtype == 1190 || itemtype == 1191) && ((Integer) potential.right).intValue() % 1000 >= 601 && ((Integer) potential.right).intValue() % 1000 <= 604) {
                continue;
            }
            if (((Integer) potential.right).intValue() % 1000 < 40) {
                continue;
            }
            if (level != ((Integer) potential.right).intValue() / 10000) {
                continue;
            }
            potentials.add((Integer) potential.right);
        }
    }

    private boolean isWeaponPotential(int itemtype) {
        return (GameConstants.isWeapon(itemtype * 1000) || itemtype == 1098 || itemtype == 1092 || itemtype == 1099 || itemtype == 1190 || itemtype == 1191);
    }

    private boolean isAccessoryPotential(int itemtype) {
        return ((itemtype >= 1112 && itemtype <= 1115) || itemtype == 1122 || itemtype == 1012 || itemtype == 1022 || itemtype == 1032);
    }

    public final List<StructPotentialItem> getPotentialInfo(int potId) {
        return this.potentialCache.get(Integer.valueOf(potId));
    }

    public void cachePotentialOption() {
        MapleData potsData = this.itemData.getData("ItemOption.img");
        for (MapleData data : potsData) {
            int potentialID = Integer.parseInt(data.getName());
            int type = MapleDataTool.getInt("info/optionType", data, -1);
            int reqLevel = MapleDataTool.getInt("info/reqLevel", data, 0);
            switch (potentialID) {
                case 32052:
                case 32054:
                case 32058:
                case 32059:
                case 32060:
                case 32061:
                case 32062:
                case 32071:
                case 32087:
                case 32116:
                case 40081:
                case 42052:
                case 42054:
                case 42058:
                case 42063:
                case 42064:
                case 42065:
                case 42066:
                case 42071:
                case 42087:
                case 42116:
                case 42291:
                case 42601:
                case 42650:
                case 42656:
                case 42661:
                    continue;
            }
            boolean additional = (potentialID % 10000 / 1000 == 2);
            if (this.potentialOpCache.get(Integer.valueOf(type)) == null) {
                List<Triple<Boolean, Integer, Integer>> potentialIds = new ArrayList<>();
                potentialIds.add(new Triple<>(Boolean.valueOf(additional), Integer.valueOf(reqLevel), Integer.valueOf(potentialID)));
                this.potentialOpCache.put(Integer.valueOf(type), potentialIds);
                continue;
            }
            ((List) this.potentialOpCache.get(Integer.valueOf(type))).add(new Triple<>(Boolean.valueOf(additional), Integer.valueOf(reqLevel), Integer.valueOf(potentialID)));
        }
    }

    public void cachePotentialItems() {
        MapleData potsData = this.itemData.getData("ItemOption.img");
        for (MapleData data : potsData) {
            List<StructPotentialItem> items = new LinkedList<>();
            for (MapleData level : data.getChildByPath("level")) {
                StructPotentialItem item = new StructPotentialItem();
                item.optionType = MapleDataTool.getIntConvert("info/optionType", data, 0);
                item.reqLevel = MapleDataTool.getIntConvert("info/reqLevel", data, 0);
                item.weight = MapleDataTool.getIntConvert("info/weight", data, 0);
                item.string = MapleDataTool.getString("info/string", level, "");
                item.face = MapleDataTool.getString("face", level, "");
                item.boss = (MapleDataTool.getIntConvert("boss", level, 0) > 0);
                item.potentialID = Integer.parseInt(data.getName());
                item.attackType = (short) MapleDataTool.getIntConvert("attackType", level, 0);
                item.incMHP = (short) MapleDataTool.getIntConvert("incMHP", level, 0);
                item.incMMP = (short) MapleDataTool.getIntConvert("incMMP", level, 0);
                item.incSTR = (byte) MapleDataTool.getIntConvert("incSTR", level, 0);
                item.incDEX = (byte) MapleDataTool.getIntConvert("incDEX", level, 0);
                item.incINT = (byte) MapleDataTool.getIntConvert("incINT", level, 0);
                item.incLUK = (byte) MapleDataTool.getIntConvert("incLUK", level, 0);
                item.incACC = (byte) MapleDataTool.getIntConvert("incACC", level, 0);
                item.incEVA = (byte) MapleDataTool.getIntConvert("incEVA", level, 0);
                item.incSpeed = (byte) MapleDataTool.getIntConvert("incSpeed", level, 0);
                item.incJump = (byte) MapleDataTool.getIntConvert("incJump", level, 0);
                item.incPAD = (byte) MapleDataTool.getIntConvert("incPAD", level, 0);
                item.incMAD = (byte) MapleDataTool.getIntConvert("incMAD", level, 0);
                item.incPDD = (byte) MapleDataTool.getIntConvert("incPDD", level, 0);
                item.incMDD = (byte) MapleDataTool.getIntConvert("incMDD", level, 0);
                item.prop = (byte) MapleDataTool.getIntConvert("prop", level, 0);
                item.time = (byte) MapleDataTool.getIntConvert("time", level, 0);
                item.incSTRr = (byte) MapleDataTool.getIntConvert("incSTRr", level, 0);
                item.incDEXr = (byte) MapleDataTool.getIntConvert("incDEXr", level, 0);
                item.incINTr = (byte) MapleDataTool.getIntConvert("incINTr", level, 0);
                item.incLUKr = (byte) MapleDataTool.getIntConvert("incLUKr", level, 0);
                item.incMHPr = (byte) MapleDataTool.getIntConvert("incMHPr", level, 0);
                item.incMMPr = (byte) MapleDataTool.getIntConvert("incMMPr", level, 0);
                item.incACCr = (byte) MapleDataTool.getIntConvert("incACCr", level, 0);
                item.incEVAr = (byte) MapleDataTool.getIntConvert("incEVAr", level, 0);
                item.incPADr = (byte) MapleDataTool.getIntConvert("incPADr", level, 0);
                item.incMADr = (byte) MapleDataTool.getIntConvert("incMADr", level, 0);
                item.incPDDr = (byte) MapleDataTool.getIntConvert("incPDDr", level, 0);
                item.incMDDr = (byte) MapleDataTool.getIntConvert("incMDDr", level, 0);
                item.incCr = (byte) MapleDataTool.getIntConvert("incCr", level, 0);
                item.incDAMr = (byte) MapleDataTool.getIntConvert("incDAMr", level, 0);
                item.RecoveryHP = (byte) MapleDataTool.getIntConvert("RecoveryHP", level, 0);
                item.RecoveryMP = (byte) MapleDataTool.getIntConvert("RecoveryMP", level, 0);
                item.HP = (byte) MapleDataTool.getIntConvert("HP", level, 0);
                item.MP = (byte) MapleDataTool.getIntConvert("MP", level, 0);
                item.level = (byte) MapleDataTool.getIntConvert("level", level, 0);
                item.ignoreTargetDEF = (byte) MapleDataTool.getIntConvert("ignoreTargetDEF", level, 0);
                item.ignoreDAM = (byte) MapleDataTool.getIntConvert("ignoreDAM", level, 0);
                item.DAMreflect = (byte) MapleDataTool.getIntConvert("DAMreflect", level, 0);
                item.mpconReduce = (byte) MapleDataTool.getIntConvert("mpconReduce", level, 0);
                item.mpRestore = (byte) MapleDataTool.getIntConvert("mpRestore", level, 0);
                item.incMesoProp = (byte) MapleDataTool.getIntConvert("incMesoProp", level, 0);
                item.incRewardProp = (byte) MapleDataTool.getIntConvert("incRewardProp", level, 0);
                item.incAllskill = (byte) MapleDataTool.getIntConvert("incAllskill", level, 0);
                item.ignoreDAMr = (byte) MapleDataTool.getIntConvert("ignoreDAMr", level, 0);
                item.RecoveryUP = (byte) MapleDataTool.getIntConvert("RecoveryUP", level, 0);
                item.reduceCooltime = (byte) MapleDataTool.getIntConvert("reduceCooltime", level, 0);
                switch (item.potentialID) {
                    case 31001:
                    case 31002:
                    case 31003:
                    case 31004:
                        item.skillID = item.potentialID - 23001;
                        break;
                    case 41005:
                    case 41006:
                    case 41007:
                        item.skillID = item.potentialID - 33001;
                        break;
                    default:
                        item.skillID = 0;
                        break;
                }
                items.add(item);
            }
            this.potentialCache.put(Integer.valueOf(Integer.parseInt(data.getName())), items);
        }
    }

    public final Collection<Integer> getMonsterBookList() {
        return this.mobIds.values();
    }

    public final Map<Integer, Integer> getMonsterBook() {
        return this.mobIds;
    }

    public final Pair<Integer, Integer> getPot(int f) {
        return this.potLife.get(Integer.valueOf(f));
    }

    public static final MapleItemInformationProvider getInstance() {
        return instance;
    }

    public final List<Pair<Integer, String>> getAllEquips() {
        List<Pair<Integer, String>> itemPairs = new ArrayList<>();
        MapleData itemsData = this.stringData.getData("Eqp.img").getChildByPath("Eqp");
        for (MapleData eqpType : itemsData.getChildren()) {
            for (MapleData itemFolder : eqpType.getChildren()) {
                itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
            }
        }
        return itemPairs;
    }

    public final List<Pair<Integer, String>> getAllItems() {
        if (!this.itemNameCache.isEmpty()) {
            return this.itemNameCache;
        }
        List<Pair<Integer, String>> itemPairs = new ArrayList<>();
        MapleData itemsData = this.stringData.getData("Cash.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        itemsData = this.stringData.getData("Consume.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        itemsData = this.stringData.getData("Eqp.img").getChildByPath("Eqp");
        for (MapleData eqpType : itemsData.getChildren()) {
            for (MapleData itemFolder : eqpType.getChildren()) {
                itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
            }
        }
        itemsData = this.stringData.getData("Etc.img").getChildByPath("Etc");
        for (MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        itemsData = this.stringData.getData("Ins.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        itemsData = this.stringData.getData("Pet.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.valueOf(Integer.parseInt(itemFolder.getName())), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        return itemPairs;
    }

    public final Triple<Pair<List<Integer>, List<Integer>>, List<Integer>, Integer> getAndroidInfo(int i) {
        return this.androids.get(Integer.valueOf(i));
    }

    public final Triple<Integer, List<Integer>, List<Integer>> getMonsterBookInfo(int i) {
        return this.monsterBookSets.get(Integer.valueOf(i));
    }

    public final Map<Integer, Triple<Integer, List<Integer>, List<Integer>>> getAllMonsterBookInfo() {
        return this.monsterBookSets;
    }

    protected final MapleData getItemData(int itemId) {
        MapleData ret = null;
        String idStr = "0" + String.valueOf(itemId);
        MapleDataDirectoryEntry root = this.itemData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
                    ret = this.itemData.getData(topDir.getName() + "/" + iFile.getName());
                    if (ret == null) {
                        return null;
                    }
                    ret = ret.getChildByPath(idStr);
                    return ret;
                }
                if (iFile.getName().equals(idStr.substring(1) + ".img")) {
                    return this.itemData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        root = this.chrData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr + ".img")) {
                    return this.chrData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        return ret;
    }

    public Integer getItemIdByMob(int mobId) {
        return this.mobIds.get(Integer.valueOf(mobId));
    }

    public Integer getSetId(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return Integer.valueOf(i.cardSet);
    }

    public final short getSlotMax(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.slotMax;
    }

    public final int getUpgradeScrollUseSlot(int itemid) {
        if (this.scrollUpgradeSlotUse.containsKey(Integer.valueOf(itemid))) {
            return ((Integer) this.scrollUpgradeSlotUse.get(Integer.valueOf(itemid))).intValue();
        }
        int useslot = MapleDataTool.getIntConvert("info/tuc", getItemData(itemid), 1);
        this.scrollUpgradeSlotUse.put(Integer.valueOf(itemid), Integer.valueOf(useslot));
        return ((Integer) this.scrollUpgradeSlotUse.get(Integer.valueOf(itemid))).intValue();
    }

    public final int getWholePrice(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.wholePrice;
    }

    public final double getPrice(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return -1.0D;
        }
        return i.price;
    }

    protected int rand(int min, int max) {
        return Math.abs(Randomizer.rand(min, max));
    }

    public Equip levelUpEquip(Equip equip, Map<String, Integer> sta) {
        Equip nEquip = (Equip) equip.copy();
        try {
            for (Map.Entry<String, Integer> stat : sta.entrySet()) {
                if (((String) stat.getKey()).equals("STRMin")) {
                    nEquip.setStr((short) (nEquip.getStr() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("STRMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("DEXMin")) {
                    nEquip.setDex((short) (nEquip.getDex() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("DEXMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("INTMin")) {
                    nEquip.setInt((short) (nEquip.getInt() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("INTMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("LUKMin")) {
                    nEquip.setLuk((short) (nEquip.getLuk() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("LUKMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("PADMin")) {
                    nEquip.setWatk((short) (nEquip.getWatk() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("PADMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("PDDMin")) {
                    nEquip.setWdef((short) (nEquip.getWdef() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("PDDMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("MADMin")) {
                    nEquip.setMatk((short) (nEquip.getMatk() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("MADMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("MDDMin")) {
                    nEquip.setMdef((short) (nEquip.getMdef() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("MDDMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("ACCMin")) {
                    nEquip.setAcc((short) (nEquip.getAcc() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("ACCMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("EVAMin")) {
                    nEquip.setAvoid((short) (nEquip.getAvoid() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("EVAMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("SpeedMin")) {
                    nEquip.setSpeed((short) (nEquip.getSpeed() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("SpeedMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("JumpMin")) {
                    nEquip.setJump((short) (nEquip.getJump() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("JumpMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("MHPMin")) {
                    nEquip.setHp((short) (nEquip.getHp() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("MHPMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("MMPMin")) {
                    nEquip.setMp((short) (nEquip.getMp() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("MMPMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("MaxHPMin")) {
                    nEquip.setHp((short) (nEquip.getHp() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("MaxHPMax")).intValue())));
                    continue;
                }
                if (((String) stat.getKey()).equals("MaxMPMin")) {
                    nEquip.setMp((short) (nEquip.getMp() + rand(((Integer) stat.getValue()).intValue(), ((Integer) sta.get("MaxMPMax")).intValue())));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return nEquip;
    }

    public final List<Triple<String, String, String>> getEquipAdditions(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.equipAdditions;
    }

    public final String getEquipAddReqs(int itemId, String key, String sub) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        for (Triple<String, String, String> data : i.equipAdditions) {
            if (((String) data.getLeft()).equals("key") && ((String) data.getMid()).equals("con:" + sub)) {
                return data.getRight();
            }
        }
        return null;
    }

    public final Map<Integer, Map<String, Integer>> getEquipIncrements(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.equipIncs;
    }

    public final List<Integer> getEquipSkills(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.incSkill;
    }

    public final boolean canEquip(Map<String, Integer> stats, int itemid, int level, int job, int fame, int str, int dex, int luk, int int_, int supremacy) {
        if (str >= (stats.containsKey("reqSTR") ? ((Integer) stats.get("reqSTR")).intValue() : 0) && dex >= (stats.containsKey("reqDEX") ? ((Integer) stats.get("reqDEX")).intValue() : 0) && luk >= (stats.containsKey("reqLUK") ? ((Integer) stats.get("reqLUK")).intValue() : 0) && int_ >= (stats.containsKey("reqINT") ? ((Integer) stats.get("reqINT")).intValue() : 0)) {
            Integer fameReq = stats.get("reqPOP");
            if (fameReq != null && fame < fameReq.intValue()) {
                return false;
            }
            return true;
        }
        if (GameConstants.isDemonAvenger(job)) {
            return true;
        }
        return false;
    }

    public final Map<String, Integer> getEquipStats(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.equipStats;
    }

    public final int getReqLevel(int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("reqLevel")) {
            return 0;
        }
        return ((Integer) getEquipStats(itemId).get("reqLevel")).intValue();
    }

    public final int getReqJob(int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("reqJob")) {
            return 0;
        }
        return ((Integer) getEquipStats(itemId).get("reqJob")).intValue();
    }

    public final int getSlots(int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("tuc")) {
            return 0;
        }
        return ((Integer) getEquipStats(itemId).get("tuc")).intValue();
    }

    public final Integer getSetItemID(int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("setItemID")) {
            return Integer.valueOf(0);
        }
        return getEquipStats(itemId).get("setItemID");
    }

    public final boolean isOnlyEquip(int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("onlyEquip")) {
            return false;
        }
        return (((Integer) getEquipStats(itemId).get("onlyEquip")).intValue() > 0);
    }

    public final StructSetItem getSetItem(int setItemId) {
        return this.setItems.get(Integer.valueOf(setItemId));
    }

    public final int getCursed(int itemId, MapleCharacter player) {
        return getCursed(itemId, player, null);
    }

    public final int getCursed(int itemId, MapleCharacter player, Item equip) {
        if (this.cursedCache.containsKey(Integer.valueOf(itemId))) {
            return ((Integer) this.cursedCache.get(Integer.valueOf(itemId))).intValue();
        }
        MapleData item = getItemData(itemId);
        if (item == null) {
            return -1;
        }
        int success = 0;
        success = MapleDataTool.getIntConvert("info/cursed", item, -1);
        this.cursedCache.put(Integer.valueOf(itemId), Integer.valueOf(success));
        return success;
    }

    public final List<Integer> getScrollReqs(int itemId) {
        List<Integer> ret = new ArrayList<>();
        MapleData data = getItemData(itemId).getChildByPath("req");
        if (data == null) {
            return ret;
        }
        for (MapleData req : data.getChildren()) {
            ret.add(Integer.valueOf(MapleDataTool.getInt(req)));
        }
        return ret;
    }

    public final Item scrollEquipWithId(Item equip, Item scrollId, boolean ws, MapleCharacter chr) {
        if (equip.getType() == 1) {
            MapleQuest quest;
            List<Pair<EnchantFlag, Integer>> statz;
            short watk, matk;
            int flag;
            List<Pair<Integer, Integer>> list;
            String stringa;
            int allstat;
            short str;
            int randomstat;
            MapleQuestStatus queststatus;
            int rand;
            short dex;
            int randb;
            short int_, luk;
            Equip nEquip = (Equip) equip;
            Equip zeroEquip = null;
            if (GameConstants.isAlphaWeapon(nEquip.getItemId())) {
                zeroEquip = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
            } else if (GameConstants.isBetaWeapon(nEquip.getItemId())) {
                zeroEquip = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
            }
            Map<String, Integer> stats = getEquipStats(scrollId.getItemId());
            Map<String, Integer> eqstats = getEquipStats(equip.getItemId());
            boolean failed = false;
            switch (scrollId.getItemId()) {
                case 2049000:
                case 2049001:
                case 2049002:
                case 2049004:
                case 2049005:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        failed = true;
                        break;
                    }
                    if (nEquip.getLevel() + nEquip.getUpgradeSlots() < ((Integer) eqstats.get("tuc")).intValue() + ((nEquip.getViciousHammer() > 0) ? 1 : 0)) {
                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                        if (zeroEquip != null) {
                            zeroEquip.setUpgradeSlots((byte) (zeroEquip.getUpgradeSlots() + 1));
                        }
                    }
                    break;
                case 2048900:
                case 2048901:
                case 2048902:
                case 2048903:
                case 2048904:
                case 2048905:
                case 2048906:
                case 2048907:
                case 2048912:
                case 2048913:
                case 2048915:
                case 2048918:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        failed = true;
                        break;
                    }
                    quest = MapleQuest.getInstance(41907);
                    stringa = String.valueOf(GameConstants.getLuckyInfofromItemId(scrollId.getItemId()));
                    chr.setKeyValue(46523, "luckyscroll", stringa);
                    queststatus = new MapleQuestStatus(quest, 1);
                    queststatus.setCustomData((stringa == null) ? "0" : stringa);
                    chr.updateQuest(queststatus, true);
                    break;
                case 2049006:
                case 2049007:
                case 2049008:
                    if (nEquip.getLevel() + nEquip.getUpgradeSlots() < ((Integer) eqstats.get("tuc")).intValue() + ((nEquip.getViciousHammer() > 0) ? 1 : 0)) {
                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 2));
                        if (zeroEquip != null) {
                            zeroEquip.setUpgradeSlots((byte) (zeroEquip.getUpgradeSlots() + 2));
                        }
                    }
                    break;
                case 2046025:
                case 2046026:
                case 2046119:
                    statz = new ArrayList<>();
                    allstat = 3;
                    rand = Randomizer.rand(18, 20);
                    nEquip.addStr((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addStr((short) allstat);
                    }
                    nEquip.addDex((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addDex((short) allstat);
                    }
                    nEquip.addInt((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addInt((short) allstat);
                    }
                    nEquip.addLuk((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk((short) allstat);
                    }
                    if (scrollId.getItemId() == 2046026) {
                        nEquip.addMatk((short) rand);
                        if (zeroEquip != null) {
                            zeroEquip.addMatk((short) rand);
                        }
                        statz.add(new Pair<>(EnchantFlag.Matk, Integer.valueOf(rand)));
                    } else {
                        nEquip.addWatk((short) rand);
                        if (zeroEquip != null) {
                            zeroEquip.addWatk((short) rand);
                        }
                        statz.add(new Pair<>(EnchantFlag.Watk, Integer.valueOf(rand)));
                    }
                    equip.setShowScrollOption(new StarForceStats(statz));
                    break;
                case 2049135:
                    statz = new ArrayList<>();
                    allstat = 3;
                    rand = Randomizer.rand(4, 6);
                    randb = Randomizer.rand(1, 3);
                    nEquip.addStr((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addStr((short) allstat);
                    }
                    nEquip.addDex((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addDex((short) allstat);
                    }
                    nEquip.addInt((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addInt((short) allstat);
                    }
                    nEquip.addLuk((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk((short) allstat);
                    }
                    nEquip.addMatk((short) rand);
                    if (zeroEquip != null) {
                        zeroEquip.addMatk((short) rand);
                    }
                    statz.add(new Pair<>(EnchantFlag.Matk, Integer.valueOf(rand)));
                    nEquip.addWatk((short) rand);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk((short) rand);
                    }
                    statz.add(new Pair<>(EnchantFlag.Watk, Integer.valueOf(rand)));
                    nEquip.addBossDamage((byte) randb);
                    if (zeroEquip != null) {
                        zeroEquip.addBossDamage((byte) randb);
                    }
                    equip.setShowScrollOption(new StarForceStats(statz));
                    break;
                case 2049136:
                    statz = new ArrayList<>();
                    allstat = Randomizer.rand(12, 14);
                    rand = Randomizer.rand(10, 11);
                    nEquip.addStr((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addStr((short) allstat);
                    }
                    nEquip.addDex((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addDex((short) allstat);
                    }
                    nEquip.addInt((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addInt((short) allstat);
                    }
                    nEquip.addLuk((short) allstat);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk((short) allstat);
                    }
                    statz.add(new Pair<>(EnchantFlag.Str, Integer.valueOf(allstat)));
                    statz.add(new Pair<>(EnchantFlag.Dex, Integer.valueOf(allstat)));
                    statz.add(new Pair<>(EnchantFlag.Int, Integer.valueOf(allstat)));
                    statz.add(new Pair<>(EnchantFlag.Luk, Integer.valueOf(allstat)));
                    nEquip.addMatk((short) rand);
                    if (zeroEquip != null) {
                        zeroEquip.addMatk((short) rand);
                    }
                    statz.add(new Pair<>(EnchantFlag.Matk, Integer.valueOf(rand)));
                    nEquip.addWatk((short) rand);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk((short) rand);
                    }
                    statz.add(new Pair<>(EnchantFlag.Watk, Integer.valueOf(rand)));
                    equip.setShowScrollOption(new StarForceStats(statz));
                    break;
                case 2046054:
                case 2046055:
                case 2046056:
                case 2046057:
                case 2046058:
                case 2046059:
                case 2046094:
                case 2046095:
                case 2046120:
                case 2046138:
                case 2046139:
                case 2046140:
                case 2046162:
                case 2046163:
                case 2046251:
                case 2046340:
                case 2046341:
                case 2046374:
                case 2046564:
                case 2049052:
                case 2049049:
                case 2049050:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                            chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                        }
                        failed = true;
                        break;
                    }
                    switch (scrollId.getItemId()) {
                        case 2049049: // 데미지 주문서 뎀퍼 1~3퍼 오르는 주문서 mush
                            nEquip.setTotalDamage((byte) (short) (nEquip.getTotalDamage() + 1));
                            //nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                            break;
                        case 2049050:// 보공 주문서 보공 1~3퍼 오르는 주문서 mushgetBossDamage 이거 줌서영 
                            nEquip.setBossDamage((short) (nEquip.getBossDamage() + 1));
                            //nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                            break;
                        case 2049052: //업글 횟수추가 mush
                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                            break;
                        case 2046025:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(7, 8)));
                            break;
                        case 2046026:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(7, 8)));
                            break;
                        case 2046340:
                            nEquip.setWatk((short) (nEquip.getWatk() + 1));
                            break;
                        case 2046341:
                            nEquip.setMatk((short) (nEquip.getMatk() + 1));
                            break;
                        case 2046119:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(7, 8)));
                            break;
                        case 2046120:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(7, 8)));
                            break;
                        case 2046251:
                            nEquip.setStr((short) (nEquip.getStr() + 3));
                            nEquip.setInt((short) (nEquip.getInt() + 3));
                            nEquip.setDex((short) (nEquip.getDex() + 3));
                            nEquip.setLuk((short) (nEquip.getLuk() + 3));
                            break;
                        case 2046054:
                        case 2046055:
                        case 2046056:
                        case 2046057:
                        case 2046138:
                        case 2046139:
                            if (scrollId.getItemId() == 2046055 || scrollId.getItemId() == 2046057) {
                                nEquip.setMatk((short) (nEquip.getMatk() + 5));
                            } else {
                                nEquip.setWatk((short) (nEquip.getWatk() + 5));
                            }
                            nEquip.setStr((short) (nEquip.getStr() + 3));
                            nEquip.setDex((short) (nEquip.getDex() + 3));
                            nEquip.setInt((short) (nEquip.getInt() + 3));
                            nEquip.setLuk((short) (nEquip.getLuk() + 3));
                            nEquip.setAcc((short) (nEquip.getAcc() + 15));
                            break;
                        case 2046058:
                        case 2046059:
                        case 2046140:
                            if (scrollId.getItemId() == 2046059) {
                                nEquip.setMatk((short) (nEquip.getMatk() + 2));
                            } else {
                                nEquip.setWatk((short) (nEquip.getWatk() + 2));
                            }
                            nEquip.setStr((short) (nEquip.getStr() + 1));
                            nEquip.setDex((short) (nEquip.getDex() + 1));
                            nEquip.setInt((short) (nEquip.getInt() + 1));
                            nEquip.setLuk((short) (nEquip.getLuk() + 1));
                            nEquip.setAcc((short) (nEquip.getAcc() + 5));
                            break;
                        case 2046374:
                            nEquip.setWatk((short) (nEquip.getWatk() + 3));
                            nEquip.setMatk((short) (nEquip.getMatk() + 3));
                            nEquip.setWdef((short) (nEquip.getWdef() + 25));
                            nEquip.setMdef((short) (nEquip.getMdef() + 25));
                            nEquip.setStr((short) (nEquip.getStr() + 3));
                            nEquip.setDex((short) (nEquip.getDex() + 3));
                            nEquip.setInt((short) (nEquip.getInt() + 3));
                            nEquip.setLuk((short) (nEquip.getLuk() + 3));
                            nEquip.setAvoid((short) (nEquip.getAvoid() + 30));
                            nEquip.setAcc((short) (nEquip.getAcc() + 30));
                            nEquip.setSpeed((short) (nEquip.getSpeed() + 3));
                            nEquip.setJump((short) (nEquip.getJump() + 2));
                            nEquip.setMp((short) (nEquip.getMp() + 25));
                            nEquip.setHp((short) (nEquip.getHp() + 25));
                            break;
                        case 2046094:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(7, 9)));
                            break;
                        case 2046095:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(7, 9)));
                            break;
                        case 2046162:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(7, 9)));
                            break;
                        case 2046163:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(7, 9)));
                            break;
                        case 5530336:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(2, 4)));
                            break;
                        case 5530337:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(2, 4)));
                            break;
                        case 2048082:
                        case 2048827:
                        case 2048832:
                        case 5530338:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(2, 4)));
                            break;
                        case 2048094:
                        case 2048804:
                        case 2048836:
                        case 2048838:
                        case 5530442:
                            nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(4, 5)));
                            break;
                        case 2048083:
                        case 2048828:
                        case 2048833:
                        case 5530339:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(2, 4)));
                            break;
                        case 2048095:
                        case 2048805:
                        case 2048837:
                        case 2048839:
                        case 5530443:
                            nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(4, 5)));
                            break;
                        case 2046564:
                            nEquip.setStr((short) (nEquip.getStr() + 5));
                            nEquip.setInt((short) (nEquip.getInt() + 5));
                            nEquip.setDex((short) (nEquip.getDex() + 5));
                            nEquip.setLuk((short) (nEquip.getLuk() + 5));
                            break;
                    }
                    break;
                case 2049166:
                case 2049167:
                    statz = new ArrayList<>();
                    str = 5;
                    dex = 5;
                    int_ = 5;
                    luk = 5;
                    short s1 = (short) Randomizer.rand(15, 17);
                    short s2 = (short) Randomizer.rand(15, 17);

                    nEquip.addWatk(s1);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk(s1);
                    }
                    statz.add(new Pair(EnchantFlag.Watk, Integer.valueOf(s1)));

                    nEquip.addMatk(s2);
                    if (zeroEquip != null) {
                        zeroEquip.addMatk(s2);
                    }
                    statz.add(new Pair(EnchantFlag.Matk, Integer.valueOf(s2)));

                    nEquip.addStr(str);
                    if (zeroEquip != null) {
                        zeroEquip.addStr(str);
                    }

                    nEquip.addDex(dex);
                    if (zeroEquip != null) {
                        zeroEquip.addDex(dex);
                    }

                    nEquip.addInt(int_);
                    if (zeroEquip != null) {
                        zeroEquip.addInt(int_);
                    }

                    nEquip.addLuk(luk);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk(int_);
                    }
                    equip.setShowScrollOption(new StarForceStats(statz));
                    break;

                case 2049168:
                    statz = new ArrayList<>();
                    str = (short) Randomizer.rand(6, 9);
                    dex = (short) Randomizer.rand(6, 9);
                    int_ = (short) Randomizer.rand(6, 9);
                    luk = (short) Randomizer.rand(6, 9);
                    s1 = (short) Randomizer.rand(6, 9);
                    s2 = (short) Randomizer.rand(6, 9);

                    nEquip.addWatk(s1);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk(s1);
                    }
                    statz.add(new Pair(EnchantFlag.Watk, Integer.valueOf(s1)));

                    nEquip.addMatk(s2);
                    if (zeroEquip != null) {
                        zeroEquip.addMatk(s2);
                    }
                    statz.add(new Pair(EnchantFlag.Matk, Integer.valueOf(s2)));

                    nEquip.addStr(str);
                    if (zeroEquip != null) {
                        zeroEquip.addStr(str);
                    }
                    statz.add(new Pair(EnchantFlag.Str, Integer.valueOf(str)));

                    nEquip.addDex(dex);
                    if (zeroEquip != null) {
                        zeroEquip.addDex(dex);
                    }
                    statz.add(new Pair(EnchantFlag.Dex, Integer.valueOf(dex)));

                    nEquip.addInt(int_);
                    if (zeroEquip != null) {
                        zeroEquip.addInt(int_);
                    }
                    statz.add(new Pair(EnchantFlag.Int, Integer.valueOf(int_)));

                    nEquip.addLuk(luk);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk(luk);
                    }
                    statz.add(new Pair(EnchantFlag.Luk, Integer.valueOf(luk)));
                    equip.setShowScrollOption(new StarForceStats(statz));
                    break;
                case 2046996:
                case 2047818:
                    watk = (short) Randomizer.rand(10, 10);
                    str = (short) Randomizer.rand(3, 3);
                    dex = (short) Randomizer.rand(3, 3);
                    int_ = (short) Randomizer.rand(3, 3);
                    luk = (short) Randomizer.rand(3, 3);
                    nEquip.addWatk(watk);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk(watk);
                    }
                    nEquip.addStr(str);
                    if (zeroEquip != null) {
                        zeroEquip.addStr(str);
                    }
                    nEquip.addDex(dex);
                    if (zeroEquip != null) {
                        zeroEquip.addDex(dex);
                    }
                    nEquip.addInt(int_);
                    if (zeroEquip != null) {
                        zeroEquip.addInt(int_);
                    }
                    nEquip.addLuk(luk);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk(int_);
                    }
                    break;
                case 2046997:
                    matk = (short) Randomizer.rand(10, 10);
                    str = (short) Randomizer.rand(3, 3);
                    dex = (short) Randomizer.rand(3, 3);
                    int_ = (short) Randomizer.rand(3, 3);
                    luk = (short) Randomizer.rand(3, 3);
                    nEquip.addMatk(matk);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk(matk);
                    }
                    nEquip.addStr(str);
                    if (zeroEquip != null) {
                        zeroEquip.addStr(str);
                    }
                    nEquip.addDex(dex);
                    if (zeroEquip != null) {
                        zeroEquip.addDex(dex);
                    }
                    nEquip.addInt(int_);
                    if (zeroEquip != null) {
                        zeroEquip.addInt(int_);
                    }
                    nEquip.addLuk(luk);
                    if (zeroEquip != null) {
                        zeroEquip.addLuk(int_);
                    }
                    break;
                case 2046841:
                case 2046842:
                case 2046967:
                case 2046971:
                case 2047803:
                case 2047917:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                            chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                        }
                        failed = true;
                        break;
                    }
                    switch (scrollId.getItemId()) {
                        case 2046841:
                            nEquip.setWatk((short) (nEquip.getWatk() + 1));
                            break;
                        case 2046842:
                            nEquip.setMatk((short) (nEquip.getMatk() + 1));
                            break;
                        case 2046967:
                            nEquip.setWatk((short) (nEquip.getWatk() + 9));
                            nEquip.setStr((short) (nEquip.getStr() + 3));
                            nEquip.setInt((short) (nEquip.getInt() + 3));
                            nEquip.setDex((short) (nEquip.getDex() + 3));
                            nEquip.setLuk((short) (nEquip.getLuk() + 3));
                            break;
                        case 2046971:
                            nEquip.setMatk((short) (nEquip.getMatk() + 9));
                            nEquip.setStr((short) (nEquip.getStr() + 3));
                            nEquip.setInt((short) (nEquip.getInt() + 3));
                            nEquip.setDex((short) (nEquip.getDex() + 3));
                            nEquip.setLuk((short) (nEquip.getLuk() + 3));
                            break;
                        case 2047803:
                            nEquip.setWatk((short) (nEquip.getWatk() + 9));
                            nEquip.setStr((short) (nEquip.getStr() + 3));
                            nEquip.setInt((short) (nEquip.getInt() + 3));
                            nEquip.setDex((short) (nEquip.getDex() + 3));
                            nEquip.setLuk((short) (nEquip.getLuk() + 3));
                            break;
                        case 2047917:
                            nEquip.setStr((short) (nEquip.getStr() + 9));
                            nEquip.setInt((short) (nEquip.getInt() + 9));
                            nEquip.setDex((short) (nEquip.getDex() + 9));
                            nEquip.setLuk((short) (nEquip.getLuk() + 9));
                            break;
                    }
                    break;
                case 2049700:
                case 2049701:
                case 2049702:
                case 2049703:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                            chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                        }
                        failed = true;
                        break;
                    }
                    if (nEquip.getState() <= 17) {
                        nEquip.setState((byte) 2);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 2);
                        }
                        if (Randomizer.nextInt(100) < 30) {
                            nEquip.setLines((byte) 3);
                            if (zeroEquip != null) {
                                zeroEquip.setLines((byte) 3);
                            }
                            break;
                        }
                        nEquip.setLines((byte) 2);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 2);
                        }
                    }
                    break;
                case 2049750:
                case 2049751:
                case 2049752:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                            chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                        }
                        failed = true;
                        break;
                    }
                    if (nEquip.getState() <= 19) {
                        nEquip.setState((byte) 3);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 3);
                        }
                        if (Randomizer.nextInt(100) < 30) {
                            nEquip.setLines((byte) 3);
                            if (zeroEquip != null) {
                                zeroEquip.setLines((byte) 3);
                            }
                            break;
                        }
                        nEquip.setLines((byte) 2);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 2);
                        }
                    }
                    break;
                case 2048306:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                            chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                        }
                        failed = true;
                        break;
                    }
                    if (nEquip.getState() <= 17) {
                        nEquip.setState((byte) 4);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 4);
                        }
                        if (Randomizer.nextInt(100) < 30) {
                            nEquip.setLines((byte) 3);
                            if (zeroEquip != null) {
                                zeroEquip.setLines((byte) 3);
                            }
                            break;
                        }
                        nEquip.setLines((byte) 2);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 2);
                        }
                    }
                    break;
                case 2531000:
                case 2531001:
                case 2531005:
                    flag = nEquip.getFlag();
                    flag += ItemFlag.PROTECT_SHIELD.getValue();
                    nEquip.setFlag(flag);
                    if (zeroEquip != null) {
                        zeroEquip.setFlag(flag);
                    }
                    break;
                case 2532000:
                case 2532002:
                case 2532005:
                    flag = nEquip.getFlag();
                    flag += ItemFlag.SAFETY_SHIELD.getValue();
                    nEquip.setFlag(flag);
                    if (zeroEquip != null) {
                        zeroEquip.setFlag(flag);
                    }
                    break;
                case 2533000:
                    flag = nEquip.getFlag();
                    flag += ItemFlag.RECOVERY_SHIELD.getValue();
                    nEquip.setFlag(flag);
                    if (zeroEquip != null) {
                        zeroEquip.setFlag(flag);
                    }
                    break;
                case 2643128:
                    if (nEquip.getItemId() == 1114300) {
                        nEquip.addStr((short) 1);
                        nEquip.addDex((short) 1);
                        nEquip.addInt((short) 1);
                        nEquip.addLuk((short) 1);
                        nEquip.addWatk((short) 1);
                        nEquip.addMatk((short) 1);
                        nEquip.addHp((short) 100);
                        nEquip.addMp((short) 100);
                    }
                    break;
                case 2643130:
                    if (nEquip.getItemId() == 1114303) {
                        nEquip.addStr((short) 1);
                        nEquip.addDex((short) 1);
                        nEquip.addInt((short) 1);
                        nEquip.addLuk((short) 1);
                        nEquip.addWatk((short) 1);
                        nEquip.addMatk((short) 1);
                        nEquip.addHp((short) 100);
                        nEquip.addMp((short) 100);
                    }
                    break;
                case 2049704:
                case 5063000:
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr)));
                        failed = true;
                        break;
                    }
                    if (nEquip.getState() <= 17) {
                        nEquip.setState((byte) 4);
                        if (zeroEquip != null) {
                            zeroEquip.setState((byte) 4);
                        }
                        if (Randomizer.nextInt(100) < 30) {
                            nEquip.setLines((byte) 3);
                            if (zeroEquip != null) {
                                zeroEquip.setLines((byte) 3);
                            }
                            break;
                        }
                        nEquip.setLines((byte) 2);
                        if (zeroEquip != null) {
                            zeroEquip.setLines((byte) 2);
                        }
                    }
                    break;
                case 2530000:
                case 2530001:
                case 2530002:
                    flag = nEquip.getFlag();
                    flag += ItemFlag.LUCKY_PROTECT_SHIELD.getValue();
                    nEquip.setFlag(flag);
                    if (zeroEquip != null) {
                        zeroEquip.setFlag(flag);
                    }
                    break;
                case 2047405:
                case 2047406:
                    list = new ArrayList<>();
                    list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(80)));
                    list.add(new Pair<>(Integer.valueOf(5), Integer.valueOf(20)));
                    randomstat = GameConstants.isRandStat(list, 100);
                    if (scrollId.getItemId() == 2047406) {
                        nEquip.addMatk((short) randomstat);
                        if (zeroEquip != null) {
                            zeroEquip.addMatk((short) randomstat);
                        }
                        break;
                    }
                    nEquip.addWatk((short) randomstat);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk((short) randomstat);
                    }
                    break;
                case 2046991:
                case 2046992:
                case 2047814:
                    list = new ArrayList<>();
                    list.add(new Pair<>(Integer.valueOf(9), Integer.valueOf(50)));
                    list.add(new Pair<>(Integer.valueOf(10), Integer.valueOf(40)));
                    list.add(new Pair<>(Integer.valueOf(11), Integer.valueOf(10)));
                    randomstat = GameConstants.isRandStat(list, 100);
                    nEquip.addStr((short) 3);
                    nEquip.addDex((short) 3);
                    nEquip.addInt((short) 3);
                    nEquip.addLuk((short) 3);
                    if (scrollId.getItemId() == 2046992) {
                        nEquip.addMatk((short) randomstat);
                        if (zeroEquip != null) {
                            zeroEquip.addMatk((short) randomstat);
                            zeroEquip.addStr((short) 3);
                            zeroEquip.addDex((short) 3);
                            zeroEquip.addInt((short) 3);
                            zeroEquip.addLuk((short) 3);
                        }
                        break;
                    }
                    nEquip.addWatk((short) randomstat);
                    if (zeroEquip != null) {
                        zeroEquip.addWatk((short) randomstat);
                        zeroEquip.addStr((short) 3);
                        zeroEquip.addDex((short) 3);
                        zeroEquip.addInt((short) 3);
                        zeroEquip.addLuk((short) 3);
                    }
                    break;
                case 2046856:
                case 2046857:
                    list = new ArrayList<>();
                    list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(85)));
                    list.add(new Pair<>(Integer.valueOf(5), Integer.valueOf(15)));
                    randomstat = GameConstants.isRandStat(list, 100);
                    if (scrollId.getItemId() == 2046857) {
                        nEquip.addMatk((short) randomstat);
                        break;
                    }
                    nEquip.addWatk((short) randomstat);
                    break;
                case 2048094:
                case 2048804:
                case 2048836:
                case 2048838:
                    list = new ArrayList<>();
                    list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(85)));
                    list.add(new Pair<>(Integer.valueOf(5), Integer.valueOf(15)));
                    randomstat = GameConstants.isRandStat(list, 100);
                    nEquip.setWatk((short) (nEquip.getWatk() + randomstat));
                    break;
                case 2048095:
                case 2048805:
                case 2048837:
                case 2048839:
                    list = new ArrayList<>();
                    list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(85)));
                    list.add(new Pair<>(Integer.valueOf(5), Integer.valueOf(15)));
                    randomstat = GameConstants.isRandStat(list, 100);
                    nEquip.setMatk((short) (nEquip.getMatk() + randomstat));
                    break;
                case 2048809:
                    nEquip.setWatk((short) (nEquip.getWatk() + 2));
                    break;
                case 2048810:
                    nEquip.setMatk((short) (nEquip.getMatk() + 2));
                    break;
                case 2645000:
                case 2645001:
                    switch (nEquip.getItemId()) {
                        case 1032220:
                        case 1032221:
                        case 1032222:
                        case 1113072:
                        case 1113073:
                        case 1113074:
                        case 1122264:
                        case 1122265:
                        case 1122266:
                        case 1132243:
                        case 1132244:
                        case 1132245:
                            nEquip.addStr((short) 3);
                            nEquip.addDex((short) 3);
                            nEquip.addInt((short) 3);
                            nEquip.addLuk((short) 3);
                            if (scrollId.getItemId() == 2645000) {
                                nEquip.addWatk((short) 3);
                                break;
                            }
                            nEquip.addMatk((short) 3);
                            break;
                    }
                    break;
                case 2645002:
                case 2645003:
                    switch (nEquip.getItemId()) {
                        case 1032223:
                        case 1113075:
                        case 1122267:
                        case 1132246:
                            nEquip.addStr((short) Randomizer.rand(10, 30));
                            nEquip.addDex((short) Randomizer.rand(10, 30));
                            nEquip.addInt((short) Randomizer.rand(10, 30));
                            nEquip.addLuk((short) Randomizer.rand(10, 30));
                            if (scrollId.getItemId() == 2645002) {
                                nEquip.addWatk((short) Randomizer.rand(10, 20));
                                break;
                            }
                            nEquip.addMatk((short) Randomizer.rand(10, 20));
                            break;
                    }
                    break;
                default:
                    if (GameConstants.isChaosScroll(scrollId.getItemId())) {
                        if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                            if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr, nEquip))
                                    && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                                chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                            }
                            failed = true;
                            break;
                        }
                        List<Pair<EnchantFlag, Integer>> list1 = new ArrayList<>();
                        if (GameConstants.getChaosNumber(scrollId.getItemId()) != 999) {
                            if (nEquip.getStr() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (i != 999) {
                                    nEquip.addStr((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addStr((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Str, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getDex() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (i != 999) {
                                    nEquip.addDex((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addDex((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Dex, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getInt() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (i != 999) {
                                    nEquip.addInt((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addInt((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Int, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getLuk() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (i != 999) {
                                    nEquip.addLuk((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addLuk((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Luk, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getWatk() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (i != 999) {
                                    nEquip.addWatk((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addWatk((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Watk, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getMatk() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (i != 999) {
                                    nEquip.addMatk((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addMatk((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Matk, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getHp() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId()) * 10;
                                if (i != 999) {
                                    nEquip.addHp((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addHp((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Hp, Integer.valueOf(i)));
                                }
                            }
                            if (nEquip.getMp() > 0) {
                                int i = GameConstants.getChaosNumber(scrollId.getItemId()) * 10;
                                if (i != 999) {
                                    nEquip.addMp((short) i);
                                    if (zeroEquip != null) {
                                        zeroEquip.addMp((short) i);
                                    }
                                    list1.add(new Pair<>(EnchantFlag.Mp, Integer.valueOf(i)));
                                }
                            }
                            equip.setShowScrollOption(new StarForceStats(list1));
                        }
                        break;
                    }
                    if (scrollId.getItemId() == 2049360 || scrollId.getItemId() == 2049361) {
                        int chane, ordinary, data[];
                        MapleData IData = getItemData(nEquip.getItemId());
                        MapleData info = IData.getChildByPath("info");
                        int level = MapleDataTool.getInt("reqLevel", info, 0);
                        if (level > 150) {
                            chr.dropMessage(6, "150레벨 이하의 장비 아이템에만 사용하실 수 있습니다.");
                            break;
                        }
                        switch (nEquip.getEnhance()) {
                            case 0:
                                chane = 60;
                                break;
                            case 1:
                                chane = 55;
                                break;
                            case 2:
                                chane = 50;
                                break;
                            case 3:
                                chane = 40;
                                break;
                            case 4:
                                chane = 30;
                                break;
                            case 5:
                                chane = 20;
                                break;
                            case 6:
                                chane = 19;
                                break;
                            case 7:
                                chane = 18;
                                break;
                            case 8:
                                chane = 17;
                                break;
                            case 9:
                                chane = 16;
                                break;
                            case 10:
                                chane = 14;
                                break;
                            case 11:
                                chane = 12;
                                break;
                            default:
                                chane = 10;
                                break;
                        }
                        if (chr.getGMLevel() > 0) {
                            chane = 100;
                        }
                        if (!Randomizer.isSuccess(chane)) {
                            if (ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                                chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                                break;
                            }
                            return null;
                        }
                        if (EquipmentEnchant.isMagicWeapon(GameConstants.getWeaponType(nEquip.getItemId()))) {
                            ordinary = nEquip.getMatk() + nEquip.getEnchantMatk();
                        } else {
                            ordinary = nEquip.getWatk() + nEquip.getEnchantWatk();
                        }
                        if (nEquip.getFire() > 0L) {
                            long fire1 = nEquip.getFire() % 1000L / 10L;
                            long fire2 = nEquip.getFire() % 1000000L / 10000L;
                            long fire3 = nEquip.getFire() % 1000000000L / 10000000L;
                            long fire4 = nEquip.getFire() % 1000000000000L / 10000000000L;
                            for (int i = 0; i < 4; i++) {
                                int dat = (int) ((i == 0) ? fire1 : ((i == 1) ? fire2 : ((i == 2) ? fire3 : fire4)));
                                if (dat == (EquipmentEnchant.isMagicWeapon(GameConstants.getWeaponType(nEquip.getItemId())) ? 18 : 17)) {
                                    int value;
                                    if (i == 0) {
                                        value = (int) (nEquip.getFire() % 10L / 1L);
                                    } else if (i == 1) {
                                        value = (int) (nEquip.getFire() % 10000L / 1000L);
                                    } else if (i == 2) {
                                        value = (int) (nEquip.getFire() % 10000000L / 1000000L);
                                    } else {
                                        value = (int) (nEquip.getFire() % 10000000000L / 1000000000L);
                                    }
                                    switch (value) {
                                        case 3:
                                            if (getReqLevel(nEquip.getItemId()) <= 150) {
                                                ordinary -= (short) (ordinary * 1200 / 10000 + 1);
                                                break;
                                            }
                                            if (getReqLevel(nEquip.getItemId()) <= 160) {
                                                ordinary -= (short) (ordinary * 1500 / 10000 + 1);
                                                break;
                                            }
                                            ordinary -= (short) (ordinary * 1800 / 10000 + 1);
                                            break;
                                        case 4:
                                            if (getReqLevel(nEquip.getItemId()) <= 150) {
                                                ordinary -= (short) (ordinary * 1760 / 10000 + 1);
                                                break;
                                            }
                                            if (getReqLevel(nEquip.getItemId()) <= 160) {
                                                ordinary -= (short) (ordinary * 2200 / 10000 + 1);
                                                break;
                                            }
                                            ordinary -= (short) (ordinary * 2640 / 10000 + 1);
                                            break;
                                        case 5:
                                            if (getReqLevel(nEquip.getItemId()) <= 150) {
                                                ordinary -= (short) (ordinary * 2420 / 10000 + 1);
                                                break;
                                            }
                                            if (getReqLevel(nEquip.getItemId()) <= 160) {
                                                ordinary -= (short) (ordinary * 3025 / 10000 + 1);
                                                break;
                                            }
                                            ordinary -= (short) (ordinary * 3630 / 10000 + 1);
                                            break;
                                        case 6:
                                            if (getReqLevel(nEquip.getItemId()) <= 150) {
                                                ordinary -= (short) (ordinary * 3200 / 10000 + 1);
                                                break;
                                            }
                                            if (getReqLevel(nEquip.getItemId()) <= 160) {
                                                ordinary -= (short) (ordinary * 4000 / 10000 + 1);
                                                break;
                                            }
                                            ordinary -= (short) (ordinary * 4800 / 10000 + 1);
                                            break;
                                        case 7:
                                            if (getReqLevel(nEquip.getItemId()) <= 150) {
                                                ordinary -= (short) (ordinary * 4100 / 10000 + 1);
                                                break;
                                            }
                                            if (getReqLevel(nEquip.getItemId()) <= 160) {
                                                ordinary -= (short) (ordinary * 5125 / 10000 + 1);
                                                break;
                                            }
                                            ordinary -= (short) (ordinary * 6150 / 10000 + 1);
                                            break;
                                    }
                                }
                            }
                        }
                        int weaponwatk = ordinary / 50 + 1;
                        int weaponmatk = ordinary / 50 + 1;
                        int reallevel = level / 10 * 10;
                        switch (reallevel) {
                            case 80:
                                data = new int[]{
                                    2, 3, 5, 8, 12, 2, 3, 4, 5, 6,
                                    7, 9, 10, 11};
                                break;
                            case 90:
                                data = new int[]{
                                    4, 5, 7, 10, 14, 3, 4, 5, 6, 7,
                                    8, 10, 11, 12, 13};
                                break;
                            case 100:
                                data = new int[]{
                                    7, 8, 10, 13, 17, 4, 5, 6, 7, 8,
                                    9, 11, 12, 13, 14};
                                break;
                            case 110:
                                data = new int[]{
                                    9, 10, 12, 15, 19, 5, 6, 7, 8, 9,
                                    10, 12, 13, 14, 15};
                                break;
                            case 120:
                                data = new int[]{
                                    12, 13, 15, 18, 22, 6, 7, 8, 9, 10,
                                    11, 13, 14, 15, 16};
                                break;
                            case 130:
                                data = new int[]{
                                    14, 15, 17, 20, 24, 7, 8, 9, 10, 11,
                                    12, 14, 15, 16, 17};
                                break;
                            case 140:
                                data = new int[]{
                                    17, 18, 20, 23, 27, 8, 9, 10, 11, 12,
                                    13, 15, 16, 17, 18};
                                break;
                            case 150:
                                data = new int[]{
                                    19, 20, 22, 25, 29, 9, 10, 11, 12, 13,
                                    14, 16, 17, 18, 19};
                                break;
                            default:
                                data = new int[]{
                                    1, 2, 4, 7, 11, 1, 2, 3, 4, 5,
                                    6, 8, 9, 10, 11};
                                break;
                        }
                        if (nEquip.getEnhance() < 5) {
                            nEquip.addStr((short) data[nEquip.getEnhance()]);
                            nEquip.addDex((short) data[nEquip.getEnhance()]);
                            nEquip.addInt((short) data[nEquip.getEnhance()]);
                            nEquip.addLuk((short) data[nEquip.getEnhance()]);
                        } else {
                            nEquip.addWatk((short) data[nEquip.getEnhance()]);
                            nEquip.addMatk((short) data[nEquip.getEnhance()]);
                        }
                        if (GameConstants.isWeapon(nEquip.getItemId())) {
                            nEquip.addWatk((short) weaponwatk);
                            nEquip.addMatk((short) weaponmatk);
                            if (Randomizer.nextBoolean()) {
                                nEquip.addWatk((short) 1);
                                nEquip.addMatk((short) 1);
                            }
                        } else if (GameConstants.isAccessory(nEquip.getItemId())
                                && Randomizer.nextBoolean()) {
                            if (level < 120) {
                                if (nEquip.getEnhance() < 5) {
                                    nEquip.addStr((short) 1);
                                    nEquip.addDex((short) 1);
                                    nEquip.addInt((short) 1);
                                    nEquip.addLuk((short) 1);
                                } else {
                                    nEquip.addStr((short) 2);
                                    nEquip.addDex((short) 2);
                                    nEquip.addInt((short) 2);
                                    nEquip.addLuk((short) 2);
                                }
                            } else if (nEquip.getEnhance() < 5) {
                                nEquip.addStr((short) Randomizer.rand(1, 2));
                                nEquip.addDex((short) Randomizer.rand(1, 2));
                                nEquip.addInt((short) Randomizer.rand(1, 2));
                                nEquip.addLuk((short) Randomizer.rand(1, 2));
                            } else {
                                nEquip.addStr((short) 2);
                                nEquip.addDex((short) 2);
                                nEquip.addInt((short) 2);
                                nEquip.addLuk((short) 2);
                            }
                        }
                        nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                        nEquip.setEquipmentType(nEquip.getEquipmentType() | 0x600);
                        break;
                    }
                    if (GameConstants.isStarForceScroll(scrollId.getItemId()) > 0) {
                        int maxEnhance, max = GameConstants.isStarForceScroll(scrollId.getItemId());
                        boolean isSuperiol = ((isSuperial(nEquip.getItemId())).left != null);
                        int reqLevel = getReqLevel(nEquip.getItemId());
                        if (reqLevel < 95) {
                            maxEnhance = isSuperiol ? 3 : 5;
                        } else if (reqLevel <= 107) {
                            maxEnhance = isSuperiol ? 5 : 8;
                        } else if (reqLevel <= 119) {
                            maxEnhance = isSuperiol ? 8 : 10;
                        } else if (reqLevel <= 129) {
                            maxEnhance = isSuperiol ? 10 : 15;
                        } else if (reqLevel <= 139) {
                            maxEnhance = isSuperiol ? 12 : 20;
                        } else {
                            maxEnhance = isSuperiol ? 15 : 25;
                        }
                        if (maxEnhance < max) {
                            max = maxEnhance;
                        }
                        while (nEquip.getEnhance() < max) {
                            StarForceStats starForceStats = EquipmentEnchant.starForceStats(nEquip);
                            nEquip.setEnchantBuff((short) 0);
                            nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                            for (Pair<EnchantFlag, Integer> stat : starForceStats.getStats()) {
                                if (EnchantFlag.Watk.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantWatk((short) (nEquip.getEnchantWatk() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantWatk((short) (zeroEquip.getEnchantWatk() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Matk.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantMatk((short) (nEquip.getEnchantMatk() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantMatk((short) (zeroEquip.getEnchantMatk() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Str.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantStr((short) (nEquip.getEnchantStr() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantStr((short) (zeroEquip.getEnchantStr() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Dex.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantDex((short) (nEquip.getEnchantDex() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantDex((short) (zeroEquip.getEnchantDex() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Int.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantInt((short) (nEquip.getEnchantInt() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantInt((short) (zeroEquip.getEnchantInt() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Luk.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantLuk((short) (nEquip.getEnchantLuk() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantLuk((short) (zeroEquip.getEnchantLuk() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Wdef.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantWdef((short) (nEquip.getEnchantWdef() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantWdef((short) (zeroEquip.getEnchantWdef() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Mdef.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantMdef((short) (nEquip.getEnchantMdef() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantMdef((short) (zeroEquip.getEnchantMdef() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Hp.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantHp((short) (nEquip.getEnchantHp() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantHp((short) (zeroEquip.getEnchantHp() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Mp.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantMp((short) (nEquip.getEnchantMp() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantMp((short) (zeroEquip.getEnchantMp() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Acc.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantAcc((short) (nEquip.getEnchantAcc() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantAcc((short) (zeroEquip.getEnchantAcc() + ((Integer) stat.right).intValue()));
                                    }
                                }
                                if (EnchantFlag.Avoid.check(((EnchantFlag) stat.left).getValue())) {
                                    nEquip.setEnchantAvoid((short) (nEquip.getEnchantAvoid() + ((Integer) stat.right).intValue()));
                                    if (zeroEquip != null) {
                                        zeroEquip.setEnchantAvoid((short) (zeroEquip.getEnchantAvoid() + ((Integer) stat.right).intValue()));
                                    }
                                }
                            }
                        }
                        EquipmentEnchant.checkEquipmentStats(chr.getClient(), nEquip);
                        if (zeroEquip != null) {
                            EquipmentEnchant.checkEquipmentStats(chr.getClient(), zeroEquip);
                        }
                        break;
                    }
                    if (GameConstants.isEquipScroll(scrollId.getItemId())) {
                        if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                            if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr, nEquip))) {
                                if (ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                                    chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                                } else {
                                    return null;
                                }
                            }
                            failed = true;
                            break;
                        }
                        for (int i = 1; i <= MapleDataTool.getIntConvert("info/forceUpgrade", getItemData(scrollId.getItemId()), 1); i++) {
                            if (GameConstants.isSuperior(nEquip.getItemId())) {
                                int slevel = getReqLevel(nEquip.getItemId());
                                int senhance = nEquip.getEnhance();
                                if (senhance < 1) {
                                    nEquip.setStr((short) (nEquip.getStr() + ((slevel > 70) ? 2 : ((slevel > 100) ? 9 : ((slevel > 140) ? 19 : 1)))));
                                    nEquip.setDex((short) (nEquip.getDex() + ((slevel > 70) ? 2 : ((slevel > 100) ? 9 : ((slevel > 140) ? 19 : 1)))));
                                    nEquip.setInt((short) (nEquip.getInt() + ((slevel > 70) ? 2 : ((slevel > 100) ? 9 : ((slevel > 140) ? 19 : 1)))));
                                    nEquip.setLuk((short) (nEquip.getLuk() + ((slevel > 70) ? 2 : ((slevel > 100) ? 9 : ((slevel > 140) ? 19 : 1)))));
                                    nEquip.setEnhance((byte) 1);
                                } else if (senhance == 1) {
                                    nEquip.setStr((short) (nEquip.getStr() + ((slevel > 70) ? 3 : ((slevel > 100) ? 10 : ((slevel > 140) ? 20 : 2)))));
                                    nEquip.setDex((short) (nEquip.getDex() + ((slevel > 70) ? 3 : ((slevel > 100) ? 10 : ((slevel > 140) ? 20 : 2)))));
                                    nEquip.setInt((short) (nEquip.getInt() + ((slevel > 70) ? 3 : ((slevel > 100) ? 10 : ((slevel > 140) ? 20 : 2)))));
                                    nEquip.setLuk((short) (nEquip.getLuk() + ((slevel > 70) ? 3 : ((slevel > 100) ? 10 : ((slevel > 140) ? 20 : 2)))));
                                    nEquip.setEnhance((byte) 2);
                                } else if (senhance == 2) {
                                    nEquip.setStr((short) (nEquip.getStr() + ((slevel > 70) ? 5 : ((slevel > 100) ? 12 : ((slevel > 140) ? 22 : 4)))));
                                    nEquip.setDex((short) (nEquip.getDex() + ((slevel > 70) ? 5 : ((slevel > 100) ? 12 : ((slevel > 140) ? 22 : 4)))));
                                    nEquip.setInt((short) (nEquip.getInt() + ((slevel > 70) ? 5 : ((slevel > 100) ? 12 : ((slevel > 140) ? 22 : 4)))));
                                    nEquip.setLuk((short) (nEquip.getLuk() + ((slevel > 70) ? 5 : ((slevel > 100) ? 12 : ((slevel > 140) ? 22 : 4)))));
                                    nEquip.setEnhance((byte) 3);
                                } else if (senhance == 3) {
                                    nEquip.setStr((short) (nEquip.getStr() + ((slevel > 70) ? 8 : ((slevel > 100) ? 15 : ((slevel > 140) ? 25 : 7)))));
                                    nEquip.setDex((short) (nEquip.getDex() + ((slevel > 70) ? 8 : ((slevel > 100) ? 15 : ((slevel > 140) ? 25 : 7)))));
                                    nEquip.setInt((short) (nEquip.getInt() + ((slevel > 70) ? 8 : ((slevel > 100) ? 15 : ((slevel > 140) ? 25 : 7)))));
                                    nEquip.setLuk((short) (nEquip.getLuk() + ((slevel > 70) ? 8 : ((slevel > 100) ? 15 : ((slevel > 140) ? 25 : 7)))));
                                    nEquip.setEnhance((byte) 4);
                                } else if (senhance == 4) {
                                    nEquip.setStr((short) (nEquip.getStr() + ((slevel > 70) ? 12 : ((slevel > 100) ? 19 : ((slevel > 140) ? 29 : 11)))));
                                    nEquip.setDex((short) (nEquip.getDex() + ((slevel > 70) ? 12 : ((slevel > 100) ? 19 : ((slevel > 140) ? 29 : 11)))));
                                    nEquip.setInt((short) (nEquip.getInt() + ((slevel > 70) ? 12 : ((slevel > 100) ? 19 : ((slevel > 140) ? 29 : 11)))));
                                    nEquip.setLuk((short) (nEquip.getLuk() + ((slevel > 70) ? 12 : ((slevel > 100) ? 19 : ((slevel > 140) ? 29 : 11)))));
                                    nEquip.setEnhance((byte) 5);
                                } else if (senhance == 5) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + ((slevel > 70) ? 2 : ((slevel > 100) ? 5 : ((slevel > 140) ? 9 : 2)))));
                                    nEquip.setMatk((short) (nEquip.getMatk() + ((slevel > 70) ? 2 : ((slevel > 100) ? 5 : ((slevel > 140) ? 9 : 2)))));
                                    nEquip.setEnhance((byte) 6);
                                } else if (senhance == 6) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + ((slevel > 70) ? 3 : ((slevel > 100) ? 6 : ((slevel > 140) ? 10 : 3)))));
                                    nEquip.setMatk((short) (nEquip.getMatk() + ((slevel > 70) ? 3 : ((slevel > 100) ? 6 : ((slevel > 140) ? 10 : 3)))));
                                    nEquip.setEnhance((byte) 7);
                                } else if (senhance == 7) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + ((slevel > 70) ? 4 : ((slevel > 100) ? 7 : ((slevel > 140) ? 11 : 5)))));
                                    nEquip.setMatk((short) (nEquip.getMatk() + ((slevel > 70) ? 4 : ((slevel > 100) ? 7 : ((slevel > 140) ? 11 : 5)))));
                                    nEquip.setEnhance((byte) 8);
                                } else if (senhance == 8) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + ((slevel > 70) ? 5 : ((slevel > 100) ? 8 : ((slevel > 140) ? 12 : 8)))));
                                    nEquip.setMatk((short) (nEquip.getMatk() + ((slevel > 70) ? 5 : ((slevel > 100) ? 8 : ((slevel > 140) ? 12 : 8)))));
                                    nEquip.setEnhance((byte) 9);
                                } else if (senhance == 9) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + ((slevel > 70) ? 6 : ((slevel > 100) ? 9 : ((slevel > 140) ? 13 : 12)))));
                                    nEquip.setMatk((short) (nEquip.getMatk() + ((slevel > 70) ? 6 : ((slevel > 100) ? 9 : ((slevel > 140) ? 13 : 12)))));
                                    nEquip.setEnhance((byte) 10);
                                } else {
                                    nEquip.setStr((short) (nEquip.getStr() + ((slevel > 70) ? 15 : ((slevel > 100) ? 20 : ((slevel > 140) ? 30 : 10)))));
                                    nEquip.setDex((short) (nEquip.getDex() + ((slevel > 70) ? 15 : ((slevel > 100) ? 20 : ((slevel > 140) ? 30 : 10)))));
                                    nEquip.setInt((short) (nEquip.getInt() + ((slevel > 70) ? 15 : ((slevel > 100) ? 20 : ((slevel > 140) ? 30 : 10)))));
                                    nEquip.setLuk((short) (nEquip.getLuk() + ((slevel > 70) ? 15 : ((slevel > 100) ? 20 : ((slevel > 140) ? 30 : 10)))));
                                    nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                                }
                            } else {
                                if (nEquip.getStr() > 0) {
                                    nEquip.setStr((short) (nEquip.getStr() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(0, 1))));
                                }
                                if (nEquip.getDex() > 0) {
                                    nEquip.setDex((short) (nEquip.getDex() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(0, 1))));
                                }
                                if (nEquip.getInt() > 0) {
                                    nEquip.setInt((short) (nEquip.getInt() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(0, 1))));
                                }
                                if (nEquip.getLuk() > 0) {
                                    nEquip.setLuk((short) (nEquip.getLuk() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(0, 1))));
                                }
                                if (nEquip.getWatk() > 0) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + getEquipLevel(getReqLevel(nEquip.getItemId()))));
                                }
                                if (nEquip.getWdef() > 0) {
                                    nEquip.setWdef((short) (nEquip.getWdef() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(1, 2))));
                                }
                                if (nEquip.getMatk() > 0) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + getEquipLevel(getReqLevel(nEquip.getItemId()))));
                                }
                                if (nEquip.getMdef() > 0) {
                                    nEquip.setMdef((short) (nEquip.getMdef() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(1, 2))));
                                }
                                if (nEquip.getAcc() > 0) {
                                    nEquip.setAcc((short) (nEquip.getAcc() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(1, 2))));
                                }
                                if (nEquip.getAvoid() > 0) {
                                    nEquip.setAvoid((short) (nEquip.getAvoid() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(1, 2))));
                                }
                                if (nEquip.getHp() > 0) {
                                    nEquip.setHp((short) (nEquip.getHp() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(1, 2))));
                                }
                                if (nEquip.getMp() > 0) {
                                    nEquip.setMp((short) (nEquip.getMp() + getEquipLevel(getReqLevel(nEquip.getItemId()) + Randomizer.rand(1, 2))));
                                }
                                nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                            }
                        }
                        break;
                    }
                    if (GameConstants.isPotentialScroll(scrollId.getItemId())) {
                        if (nEquip.getState() == 0) {
                            if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                                if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                        && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                                    chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                                }
                                failed = true;
                                break;
                            }
                            int state = 1;
                            switch (scrollId.getItemId()) {
                                case 2049762:
                                case 2079790:
                                    state = 3;
                                    break;
                            }
                            nEquip.setState((byte) state);
                            if (zeroEquip != null) {
                                zeroEquip.setState((byte) state);
                            }
                        }
                        break;
                    }
                    if (GameConstants.isRebirthFireScroll(scrollId.getItemId())) {
                        if (GameConstants.isZero(chr.getJob()) && nEquip.getPosition() == -11) {
                            nEquip.resetRebirth(getReqLevel(nEquip.getItemId()));
                            if (zeroEquip != null) {
                                zeroEquip.resetRebirth(getReqLevel(nEquip.getItemId()));
                                zeroEquip.setZeroRebirth(chr, getReqLevel(zeroEquip.getItemId()), scrollId.getItemId());
                            }
                        } else {
                            nEquip.resetRebirth(getReqLevel(nEquip.getItemId()));
                            nEquip.setFire(nEquip.newRebirth(getReqLevel(nEquip.getItemId()), scrollId.getItemId(), true));
                        }
                        return nEquip;
                    }
                    if (!Randomizer.isSuccess(getSuccess(scrollId.getItemId(), chr, nEquip))) {
                        if (Randomizer.isSuccess(getCursed(scrollId.getItemId(), chr))
                                && ItemFlag.PROTECT_SHIELD.check(nEquip.getFlag())) {
                            chr.dropMessage(5, "주문서의 효과로 아이템이 파괴되지 않았습니다.");
                        }
                        failed = true;
                        break;
                    }
                    for (Map.Entry<String, Integer> stat : stats.entrySet()) {
                        String key = stat.getKey();
                        if (key.equals("STR")) {
                            nEquip.setStr((short) (nEquip.getStr() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("DEX")) {
                            nEquip.setDex((short) (nEquip.getDex() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("INT")) {
                            nEquip.setInt((short) (nEquip.getInt() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("LUK")) {
                            nEquip.setLuk((short) (nEquip.getLuk() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("PAD")) {
                            nEquip.setWatk((short) (nEquip.getWatk() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("PDD")) {
                            nEquip.setWdef((short) (nEquip.getWdef() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("MAD")) {
                            nEquip.setMatk((short) (nEquip.getMatk() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("MDD")) {
                            nEquip.setMdef((short) (nEquip.getMdef() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("ACC")) {
                            nEquip.setAcc((short) (nEquip.getAcc() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("EVA")) {
                            nEquip.setAvoid((short) (nEquip.getAvoid() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("Speed")) {
                            nEquip.setSpeed((short) (nEquip.getSpeed() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("Jump")) {
                            nEquip.setJump((short) (nEquip.getJump() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("MHP")) {
                            nEquip.setHp((short) (nEquip.getHp() + ((Integer) stat.getValue()).intValue()));
                        } else if (key.equals("MMP")) {
                            nEquip.setMp((short) (nEquip.getMp() + ((Integer) stat.getValue()).intValue()));
                        }
                        if (zeroEquip != null) {
                            if (key.equals("STR")) {
                                zeroEquip.setStr((short) (zeroEquip.getStr() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("DEX")) {
                                zeroEquip.setDex((short) (zeroEquip.getDex() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("INT")) {
                                zeroEquip.setInt((short) (zeroEquip.getInt() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("LUK")) {
                                zeroEquip.setLuk((short) (zeroEquip.getLuk() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("PAD")) {
                                zeroEquip.setWatk((short) (zeroEquip.getWatk() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("PDD")) {
                                zeroEquip.setWdef((short) (zeroEquip.getWdef() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("MAD")) {
                                zeroEquip.setMatk((short) (zeroEquip.getMatk() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("MDD")) {
                                zeroEquip.setMdef((short) (zeroEquip.getMdef() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("ACC")) {
                                zeroEquip.setAcc((short) (zeroEquip.getAcc() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("EVA")) {
                                zeroEquip.setAvoid((short) (zeroEquip.getAvoid() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("Speed")) {
                                zeroEquip.setSpeed((short) (zeroEquip.getSpeed() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("Jump")) {
                                zeroEquip.setJump((short) (zeroEquip.getJump() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("MHP")) {
                                zeroEquip.setHp((short) (zeroEquip.getHp() + ((Integer) stat.getValue()).intValue()));
                                continue;
                            }
                            if (key.equals("MMP")) {
                                zeroEquip.setMp((short) (zeroEquip.getMp() + ((Integer) stat.getValue()).intValue()));
                            }
                        }
                    }
                    break;
            }
            if (!GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId()) && !GameConstants.isRebirthFireScroll(scrollId.getItemId()) && scrollId.getItemId() != 2049360 && scrollId.getItemId() != 2049361) {
                if (ItemFlag.SAFETY_SHIELD.check(nEquip.getFlag()) && failed) {
                    chr.dropMessage(5, "주문서의 효과로 업그레이드 가능 횟수가 차감되지 않았습니다.");
                } else {
                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - getUpgradeScrollUseSlot(scrollId.getItemId())));
                    if (zeroEquip != null) {
                        zeroEquip.setUpgradeSlots((byte) (zeroEquip.getUpgradeSlots() - getUpgradeScrollUseSlot(scrollId.getItemId())));
                    }
                }
                if (!failed) {
                    nEquip.setLevel((byte) (nEquip.getLevel() + 1));
                    if (zeroEquip != null) {
                        zeroEquip.setLevel((byte) (zeroEquip.getLevel() + 1));
                        chr.getClient().send(CWvsContext.InventoryPacket.addInventorySlot(MapleInventoryType.EQUIPPED, zeroEquip));
                    }
                }
            }
        }
        return equip;
    }

    private static int getEquipLevel(int level) {
        int stat = 0;
        if (level >= 0 && level <= 50) {
            stat = 1;
        } else if (level >= 51 && level <= 100) {
            stat = 2;
        } else {
            stat = 3;
        }
        return stat;
    }

    public final int getSuccess(int itemId, MapleCharacter player, Item equip) {
        if (player.getGMLevel() > 0) {
            return 100;
        }
        if (equip == null) {
            System.err.println("[오류] 주문서의 성공확률을 구하던 중, 장비 아이템 값에 널 값이 입력되었습니다." + itemId);
            player.dropMessage(5, "[오류] 현재 주문서의 성공확률을 구하는데 실패하였습니다.");
            player.gainItem(itemId, (short) 1, false, -1L, "주문서 성공확률 얻기 실패로 얻은 주문서");
            player.getClient().getSession().writeAndFlush(CWvsContext.enableActions(player));
            return 0;
        }
        Equip t = (Equip) equip.copy();
        if (itemId / 100 == 20493) {
            int i = 0;
            Equip lev = (Equip) equip.copy();
            byte leve = lev.getEnhance();
            switch (itemId) {
                case 2049300:
                case 2049303:
                case 2049306:
                case 2049323:
                    if (leve == 0) {
                        i = 100;
                    } else if (leve == 1) {
                        i = 90;
                    } else if (leve == 2) {
                        i = 80;
                    } else if (leve == 3) {
                        i = 70;
                    } else if (leve == 4) {
                        i = 60;
                    } else if (leve == 5) {
                        i = 50;
                    } else if (leve == 6) {
                        i = 40;
                    } else if (leve == 7) {
                        i = 30;
                    } else if (leve == 8) {
                        i = 20;
                    } else if (leve == 9) {
                        i = 10;
                    } else if (leve >= 10) {
                        i = 5;
                    }
                    return i;
                case 2049301:
                case 2049307:
                    if (leve == 0) {
                        i = 80;
                    } else if (leve == 1) {
                        i = 70;
                    } else if (leve == 2) {
                        i = 60;
                    } else if (leve == 3) {
                        i = 50;
                    } else if (leve == 4) {
                        i = 40;
                    } else if (leve == 5) {
                        i = 30;
                    } else if (leve == 6) {
                        i = 20;
                    } else if (leve == 7) {
                        i = 10;
                    } else if (leve >= 8) {
                        i = 5;
                    }
                    return i;
            }
        }
        switch (itemId) {
            case 2046841:
            case 2046842:
            case 2046967:
            case 2046971:
            case 2047803:
            case 2047917:
                return 20;
        }
        if (this.successCache.containsKey(Integer.valueOf(itemId))) {
            return ((Integer) this.successCache.get(Integer.valueOf(itemId))).intValue();
        }
        MapleData item = getItemData(itemId);
        if (item == null) {
            System.err.println("[오류] 주문서의 성공확률을 구하던 중, 주문서 데이터 값에 널 값이 입력되었습니다." + itemId);
            player.dropMessage(5, "[오류] 현재 주문서의 성공확률을 구하는데 실패하였습니다.");
            player.gainItem(itemId, (short) 1, false, -1L, "주문서 성공확률 얻기 실패로 얻은 주문서");
            player.getClient().getSession().writeAndFlush(CWvsContext.enableActions(player));
            return 0;
        }
        int success = 0;
        if (item.getChildByPath("info/successRates") != null) {
            success = MapleDataTool.getIntConvert(t.getLevel() + "", item.getChildByPath("info/successRates"), 20);
        } else {
            success = MapleDataTool.getIntConvert("info/success", item, 100);
        }
        if (!GameConstants.isPotentialScroll(itemId) && !GameConstants.isEquipScroll(itemId)
                && ItemFlag.LUCKY_PROTECT_SHIELD.check(t.getFlag())) {
            success += 10;
        }
        this.successCache.put(Integer.valueOf(itemId), Integer.valueOf(success));
        return success;
    }

    public final Item getEquipById(int equipId) {
        return getEquipById(equipId, -1L, true);
    }

    public final Item getEquipById(int equipId, boolean rebirth) {
        return getEquipById(equipId, -1L, rebirth);
    }

    public final Item getEquipById(int equipId, long ringId) {
        return getEquipById(equipId, ringId, true);
    }

    public final Item getEquipById(int equipId, long ringId, boolean rebirth) {
        ItemInformation i = getItemInformation(equipId);
        if (i == null) {
            return new Equip(equipId, (short) 0, ringId, 0);
        }
        Item eq = i.eq.copy();
        eq.setUniqueId(ringId);
        Equip eqz = (Equip) eq;
        if (!isCash(equipId) && rebirth) {
            eqz.setFire(eqz.newRebirth(getReqLevel(equipId), 0, true));
            if (ItemFlag.UNTRADEABLE.check(eqz.getFlag()) && eqz.getKarmaCount() < 0 && (isKarmaEnabled(equipId) || isPKarmaEnabled(equipId))) {
                eqz.setKarmaCount((byte) 10);
            }
        }
        return eq;
    }

    public final int getTotalStat(Equip equip) {
        return equip.getStr() + equip.getDex() + equip.getInt() + equip.getLuk() + equip.getMatk() + equip.getWatk() + equip.getAcc() + equip.getAvoid() + equip.getJump() + equip
                .getHands() + equip.getSpeed() + equip.getHp() + equip.getMp() + equip.getWdef() + equip.getMdef();
    }

    public final SecondaryStatEffect getItemEffect(int itemId) {
        SecondaryStatEffect ret = this.itemEffects.get(Integer.valueOf(itemId));
        if (ret == null) {
            MapleData item = getItemData(itemId);
            if (item == null || item.getChildByPath("spec") == null) {
                return null;
            }
            ret = SecondaryStatEffect.loadItemEffectFromData(item.getChildByPath("spec"), itemId);
            this.itemEffects.put(Integer.valueOf(itemId), ret);
        }
        return ret;
    }

    public final SecondaryStatEffect getItemEffectEX(int itemId) {
        SecondaryStatEffect ret = this.itemEffectsEx.get(Integer.valueOf(itemId));
        if (ret == null) {
            MapleData item = getItemData(itemId);
            if (item == null || item.getChildByPath("specEx") == null) {
                return null;
            }
            ret = SecondaryStatEffect.loadItemEffectFromData(item.getChildByPath("specEx"), itemId);
            this.itemEffectsEx.put(Integer.valueOf(itemId), ret);
        }
        return ret;
    }

    public final int getCreateId(int id) {
        ItemInformation i = getItemInformation(id);
        if (i == null) {
            return 0;
        }
        return i.create;
    }

    public final int getCardMobId(int id) {
        ItemInformation i = getItemInformation(id);
        if (i == null) {
            return 0;
        }
        return i.monsterBook;
    }

    public final int getBagType(int id) {
        ItemInformation i = getItemInformation(id);
        if (i == null) {
            return 0;
        }
        return i.flag & 0xF;
    }

    public final int getWatkForProjectile(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null || i.equipStats == null || i.equipStats.get("incPAD") == null) {
            return 0;
        }
        return ((Integer) i.equipStats.get("incPAD")).intValue();
    }

    public final boolean canScroll(int scrollid, int itemid) {
        return (scrollid / 100 % 100 == itemid / 10000 % 100);
    }

    public final String getName(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.name;
    }

    public final String getDesc(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.desc;
    }

    public final String getMsg(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.msg;
    }

    public final short getItemMakeLevel(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.itemMakeLevel;
    }

    public final boolean isDropRestricted(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (((i.flag & 0x200) != 0 || (i.flag & 0x400) != 0 || GameConstants.isDropRestricted(itemId)) && (itemId == 3012000 || itemId == 3012015 || itemId / 10000 != 301) && itemId != 2041200 && itemId != 5640000 && itemId != 4170023 && itemId != 2040124 && itemId != 2040125 && itemId != 2040126 && itemId != 2040211 && itemId != 2040212 && itemId != 2040227 && itemId != 2040228 && itemId != 2040229 && itemId != 2040230 && itemId != 1002926 && itemId != 1002906 && itemId != 1002927);
    }

    public final boolean isPickupRestricted(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (((i.flag & 0x80) != 0 || GameConstants.isPickupRestricted(itemId)) && itemId != 4001168 && itemId != 4031306 && itemId != 4031307);
    }

    public final boolean isAccountShared(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x100) != 0);
    }

    public final int getStateChangeItem(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.stateChange;
    }

    public final int getMeso(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.meso;
    }

    public final boolean isShareTagEnabled(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x800) != 0);
    }

    public final boolean isKarmaEnabled(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.karmaEnabled == 1);
    }

    public final boolean isPKarmaEnabled(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.karmaEnabled == 2);
    }

    public final boolean isPickupBlocked(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x40) != 0);
    }

    public final boolean isLogoutExpire(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x20) != 0);
    }

    public final boolean cantSell(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x10) != 0);
    }

    public final Pair<Integer, List<StructRewardItem>> getRewardItem(int itemid) {
        ItemInformation i = getItemInformation(itemid);
        if (i == null) {
            return null;
        }
        return new Pair<>(Integer.valueOf(i.totalprob), i.rewardItems);
    }

    public final boolean isMobHP(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x1000) != 0);
    }

    public final boolean isQuestItem(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x200) != 0 && itemId / 10000 != 301);
    }

    public final Pair<Integer, List<Integer>> questItemInfo(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return new Pair<>(Integer.valueOf(i.questId), i.questItems);
    }

    public final Pair<Integer, String> replaceItemInfo(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return new Pair<>(Integer.valueOf(i.replaceItem), i.replaceMsg);
    }

    public final List<Triple<String, Point, Point>> getAfterImage(String after) {
        return this.afterImage.get(after);
    }

    public final String getAfterImage(int itemId) {
        ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.afterImage;
    }

    public final boolean isJokerToSetItem(int itemId) {
        if (getEquipStats(itemId) == null) {
            return false;
        }
        return getEquipStats(itemId).containsKey("jokerToSetItem");
    }

    public final boolean itemExists(int itemId) {
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.UNDEFINED) {
            return false;
        }
        return (getItemInformation(itemId) != null);
    }

    public final boolean isCash(int itemId) {
        if (getEquipStats(itemId) != null && getEquipStats(itemId).get("cash") != null) {
            return true;
        }
        return false;
    }

    public final ItemInformation getItemInformation(int itemId) {
        if (itemId <= 0) {
            return null;
        }
        return this.dataCache.get(Integer.valueOf(itemId));
    }

    private ItemInformation tmpInfo = null;

    public void initItemRewardData(ResultSet sqlRewardData) throws SQLException {
        int itemID = sqlRewardData.getInt("itemid");
        if (this.tmpInfo == null || this.tmpInfo.itemId != itemID) {
            if (!this.dataCache.containsKey(Integer.valueOf(itemID))) {
                System.out.println("[initItemRewardData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            this.tmpInfo = this.dataCache.get(Integer.valueOf(itemID));
        }
        if (this.tmpInfo.rewardItems == null) {
            this.tmpInfo.rewardItems = new ArrayList<>();
        }
        StructRewardItem add = new StructRewardItem();
        add.itemid = sqlRewardData.getInt("item");
        add.period = ((add.itemid == 1122017) ? Math.max(sqlRewardData.getInt("period"), 7200) : sqlRewardData.getInt("period"));
        add.prob = sqlRewardData.getInt("prob");
        add.quantity = sqlRewardData.getShort("quantity");
        add.worldmsg = (sqlRewardData.getString("worldMsg").length() <= 0) ? null : sqlRewardData.getString("worldMsg");
        add.effect = sqlRewardData.getString("effect");
        this.tmpInfo.rewardItems.add(add);
    }

    public void initItemAddData(ResultSet sqlAddData) throws SQLException {
        int itemID = sqlAddData.getInt("itemid");
        if (this.tmpInfo == null || this.tmpInfo.itemId != itemID) {
            if (!this.dataCache.containsKey(Integer.valueOf(itemID))) {
                System.out.println("[initItemAddData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            this.tmpInfo = this.dataCache.get(Integer.valueOf(itemID));
        }
        if (this.tmpInfo.equipAdditions == null) {
            this.tmpInfo.equipAdditions = new LinkedList<>();
        }
        while (sqlAddData.next()) {
            this.tmpInfo.equipAdditions.add(new Triple<>(sqlAddData.getString("key"), sqlAddData.getString("subKey"), sqlAddData.getString("value")));
        }
    }

    public void initItemEquipData(ResultSet sqlEquipData) throws SQLException {
        int itemID = sqlEquipData.getInt("itemid");
        if (this.tmpInfo == null || this.tmpInfo.itemId != itemID) {
            if (!this.dataCache.containsKey(Integer.valueOf(itemID))) {
                System.out.println("[initItemEquipData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            this.tmpInfo = this.dataCache.get(Integer.valueOf(itemID));
        }
        if (this.tmpInfo.equipStats == null) {
            this.tmpInfo.equipStats = new HashMap<>();
        }
        int itemLevel = sqlEquipData.getInt("itemLevel");
        if (itemLevel == -1) {
            this.tmpInfo.equipStats.put(sqlEquipData.getString("key"), Integer.valueOf(sqlEquipData.getInt("value")));
        } else {
            if (this.tmpInfo.equipIncs == null) {
                this.tmpInfo.equipIncs = new HashMap<>();
            }
            Map<String, Integer> toAdd = this.tmpInfo.equipIncs.get(Integer.valueOf(itemLevel));
            if (toAdd == null) {
                toAdd = new HashMap<>();
                this.tmpInfo.equipIncs.put(Integer.valueOf(itemLevel), toAdd);
            }
            toAdd.put(sqlEquipData.getString("key"), Integer.valueOf(sqlEquipData.getInt("value")));
        }
    }

    public void finalizeEquipData(ItemInformation item) {
        int itemId = item.itemId;
        if (item.equipStats == null) {
            item.equipStats = new HashMap<>();
        }
        item.eq = new Equip(itemId, (short) 0, -1L, 0);
        short stats = GameConstants.getStat(itemId, 0);
        if (stats > 0) {
            item.eq.setStr(stats);
            item.eq.setDex(stats);
            item.eq.setInt(stats);
            item.eq.setLuk(stats);
        }
        stats = GameConstants.getATK(itemId, 0);
        if (stats > 0) {
            item.eq.setWatk(stats);
            item.eq.setMatk(stats);
        }
        stats = GameConstants.getHpMp(itemId, 0);
        if (stats > 0) {
            item.eq.setHp(stats);
            item.eq.setMp(stats);
        }
        stats = GameConstants.getDEF(itemId, 0);
        if (stats > 0) {
            item.eq.setWdef(stats);
            item.eq.setMdef(stats);
        }
        if (item.equipStats.size() > 0) {
            for (Map.Entry<String, Integer> stat : item.equipStats.entrySet()) {
                String key = stat.getKey();
                if (key.equals("STR")) {
                    item.eq.setStr(GameConstants.getStat(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("DEX")) {
                    item.eq.setDex(GameConstants.getStat(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("INT")) {
                    item.eq.setInt(GameConstants.getStat(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("LUK")) {
                    item.eq.setLuk(GameConstants.getStat(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("PAD")) {
                    item.eq.setWatk(GameConstants.getATK(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("PDD")) {
                    item.eq.setWdef(GameConstants.getDEF(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("MAD")) {
                    item.eq.setMatk(GameConstants.getATK(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("MDD")) {
                    item.eq.setMdef(GameConstants.getDEF(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("ACC")) {
                    item.eq.setAcc((short) ((Integer) stat.getValue()).intValue());
                    continue;
                }
                if (key.equals("EVA")) {
                    item.eq.setAvoid((short) ((Integer) stat.getValue()).intValue());
                    continue;
                }
                if (key.equals("Speed")) {
                    item.eq.setSpeed((short) ((Integer) stat.getValue()).intValue());
                    continue;
                }
                if (key.equals("Jump")) {
                    item.eq.setJump((short) ((Integer) stat.getValue()).intValue());
                    continue;
                }
                if (key.equals("MHP")) {
                    item.eq.setHp(GameConstants.getHpMp(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("MMP")) {
                    item.eq.setMp(GameConstants.getHpMp(itemId, ((Integer) stat.getValue()).intValue()));
                    continue;
                }
                if (key.equals("tuc")) {
                    item.eq.setUpgradeSlots(((Integer) stat.getValue()).byteValue());
                    continue;
                }
                if (key.equals("Craft")) {
                    item.eq.setHands(((Integer) stat.getValue()).shortValue());
                    continue;
                }
                if (key.equals("durability")) {
                    item.eq.setDurability(((Integer) stat.getValue()).intValue());
                    continue;
                }
                if (key.equals("charmEXP")) {
                    item.eq.setCharmEXP(((Integer) stat.getValue()).shortValue());
                    continue;
                }
                if (key.equals("PVPDamage")) {
                    item.eq.setPVPDamage(((Integer) stat.getValue()).shortValue());
                    continue;
                }
                if (key.equals("bdR")) {
                    item.eq.setBossDamage(((Integer) stat.getValue()).shortValue());
                    continue;
                }
                if (key.equals("imdR")) {
                    item.eq.setIgnorePDR(((Integer) stat.getValue()).shortValue());
                    continue;
                }
                if (key.equals("attackSpeed")) {
                    item.eq.setAttackSpeed(((Integer) stat.getValue()).intValue());
                }
            }
            if (item.equipStats.get("cash") != null && item.eq.getCharmEXP() <= 0) {
                short exp = 0;
                int identifier = itemId / 10000;
                if (GameConstants.isWeapon(itemId) || identifier == 106) {
                    exp = 60;
                } else if (identifier == 100) {
                    exp = 50;
                } else if (GameConstants.isAccessory(itemId) || identifier == 102 || identifier == 108 || identifier == 107) {
                    exp = 40;
                } else if (identifier == 104 || identifier == 105 || identifier == 110) {
                    exp = 30;
                }
                item.eq.setCharmEXP(exp);
            }
        }
    }

    public void initItemInformation(ResultSet sqlItemData) throws SQLException {
        ItemInformation ret = new ItemInformation();
        int itemId = sqlItemData.getInt("itemid");
        ret.itemId = itemId;
        ret.slotMax = (GameConstants.getSlotMax(itemId) > 0) ? GameConstants.getSlotMax(itemId) : sqlItemData.getShort("slotMax");
        ret.price = Double.parseDouble(sqlItemData.getString("price"));
        ret.wholePrice = sqlItemData.getInt("wholePrice");
        ret.stateChange = sqlItemData.getInt("stateChange");
        ret.name = sqlItemData.getString("name");
        ret.desc = sqlItemData.getString("desc");
        ret.msg = sqlItemData.getString("msg");
        ret.flag = sqlItemData.getInt("flags");
        ret.karmaEnabled = sqlItemData.getByte("karma");
        ret.meso = sqlItemData.getInt("meso");
        ret.monsterBook = sqlItemData.getInt("monsterBook");
        ret.itemMakeLevel = sqlItemData.getShort("itemMakeLevel");
        ret.questId = sqlItemData.getInt("questId");
        ret.create = sqlItemData.getInt("create");
        ret.replaceItem = sqlItemData.getInt("replaceId");
        ret.replaceMsg = sqlItemData.getString("replaceMsg");
        ret.afterImage = sqlItemData.getString("afterImage");
        ret.chairType = sqlItemData.getString("chairType");
        ret.nickSkill = sqlItemData.getInt("nickSkill");
        ret.cardSet = 0;
        if (ret.monsterBook > 0 && itemId / 10000 == 238) {
            this.mobIds.put(Integer.valueOf(ret.monsterBook), Integer.valueOf(itemId));
            for (Map.Entry<Integer, Triple<Integer, List<Integer>, List<Integer>>> set : this.monsterBookSets.entrySet()) {
                if (((List) ((Triple) set.getValue()).mid).contains(Integer.valueOf(itemId))) {
                    ret.cardSet = ((Integer) set.getKey()).intValue();
                    break;
                }
            }
        }
        String scrollRq = sqlItemData.getString("scrollReqs");
        if (scrollRq.length() > 0) {
            ret.scrollReqs = new ArrayList<>();
            String[] scroll = scrollRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.scrollReqs.add(Integer.valueOf(Integer.parseInt(s)));
                }
            }
        }
        String consumeItem = sqlItemData.getString("consumeItem");
        if (consumeItem.length() > 0) {
            ret.questItems = new ArrayList<>();
            String[] scroll = scrollRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.questItems.add(Integer.valueOf(Integer.parseInt(s)));
                }
            }
        }
        ret.totalprob = sqlItemData.getInt("totalprob");
        String incRq = sqlItemData.getString("incSkill");
        if (incRq.length() > 0) {
            ret.incSkill = new ArrayList<>();
            String[] scroll = incRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.incSkill.add(Integer.valueOf(Integer.parseInt(s)));
                }
            }
        }
        this.dataCache.put(Integer.valueOf(itemId), ret);
    }

    public Pair<String, Boolean> isSuperial(int itemid) {
        if ((itemid >= 1102471 && itemid <= 1102475) || (itemid >= 1072732 && itemid <= 1072736) || (itemid >= 1132164 && itemid <= 1132168)) {
            return new Pair<>("Helisium", Boolean.valueOf(true));
        }
        if ((itemid >= 1102476 && itemid <= 1102480) || (itemid >= 1072737 && itemid <= 1072741) || (itemid >= 1132169 && itemid <= 1132173)) {
            return new Pair<>("Nova", Boolean.valueOf(true));
        }
        if ((itemid >= 1102481 && itemid <= 1102485) || (itemid >= 1072743 && itemid <= 1072747) || (itemid >= 1132174 && itemid <= 1132178) || (itemid >= 1082543 && itemid <= 1082547)) {
            return new Pair<>("Tilent", Boolean.valueOf(true));
        }
        if (itemid >= 1122241 && itemid <= 1122245) {
            return new Pair<>("MindPendent", Boolean.valueOf(true));
        }
        return new Pair<>(null, Boolean.valueOf(false));
    }

    public final Equip fuse(Equip equip1, Equip equip2) {
        if (equip1.getItemId() != equip2.getItemId()) {
            return equip1;
        }
        Equip equip = (Equip) getEquipById(equip1.getItemId());
        equip.setStr(getRandStatFusion(equip.getStr(), equip1.getStr(), equip2.getStr()));
        equip.setDex(getRandStatFusion(equip.getDex(), equip1.getDex(), equip2.getDex()));
        equip.setInt(getRandStatFusion(equip.getInt(), equip1.getInt(), equip2.getInt()));
        equip.setLuk(getRandStatFusion(equip.getLuk(), equip1.getLuk(), equip2.getLuk()));
        equip.setMatk(getRandStatFusion(equip.getMatk(), equip1.getMatk(), equip2.getMatk()));
        equip.setWatk(getRandStatFusion(equip.getWatk(), equip1.getWatk(), equip2.getWatk()));
        equip.setAcc(getRandStatFusion(equip.getAcc(), equip1.getAcc(), equip2.getAcc()));
        equip.setAvoid(getRandStatFusion(equip.getAvoid(), equip1.getAvoid(), equip2.getAvoid()));
        equip.setJump(getRandStatFusion(equip.getJump(), equip1.getJump(), equip2.getJump()));
        equip.setHands(getRandStatFusion(equip.getHands(), equip1.getHands(), equip2.getHands()));
        equip.setSpeed(getRandStatFusion(equip.getSpeed(), equip1.getSpeed(), equip2.getSpeed()));
        equip.setWdef(getRandStatFusion(equip.getWdef(), equip1.getWdef(), equip2.getWdef()));
        equip.setMdef(getRandStatFusion(equip.getMdef(), equip1.getMdef(), equip2.getMdef()));
        equip.setHp(getRandStatFusion(equip.getHp(), equip1.getHp(), equip2.getHp()));
        equip.setMp(getRandStatFusion(equip.getMp(), equip1.getMp(), equip2.getMp()));
        return equip;
    }

    protected final short getRandStatFusion(short defaultValue, int value1, int value2) {
        if (defaultValue == 0) {
            return 0;
        }
        int range = (value1 + value2) / 2 - defaultValue;
        int rand = Randomizer.nextInt(Math.abs(range) + 1);
        return (short) (defaultValue + ((range < 0) ? -rand : rand));
    }

    public final Equip randomizeStats(Equip equip) {
        equip.setStr(getRandStat(equip.getStr(), 5));
        equip.setDex(getRandStat(equip.getDex(), 5));
        equip.setInt(getRandStat(equip.getInt(), 5));
        equip.setLuk(getRandStat(equip.getLuk(), 5));
        equip.setMatk(getRandStat(equip.getMatk(), 5));
        equip.setWatk(getRandStat(equip.getWatk(), 5));
        equip.setAcc(getRandStat(equip.getAcc(), 5));
        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
        equip.setJump(getRandStat(equip.getJump(), 5));
        equip.setHands(getRandStat(equip.getHands(), 5));
        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
        equip.setWdef(getRandStat(equip.getWdef(), 10));
        equip.setMdef(getRandStat(equip.getMdef(), 10));
        equip.setHp(getRandStat(equip.getHp(), 10));
        equip.setMp(getRandStat(equip.getMp(), 10));
        return equip;
    }

    protected final short getRandStat(short defaultValue, int maxRange) {
        if (defaultValue == 0) {
            return 0;
        }
        int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1D), maxRange);
        return (short) (defaultValue - lMaxRange + Randomizer.nextInt(lMaxRange * 2 + 1));
    }
}
