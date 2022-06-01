// 
// Decompiled by Procyon v0.5.36
// 

package constants;

import java.util.ArrayList;
import server.ServerProperties;
import java.util.concurrent.ScheduledFuture;
import tools.Triple;
import tools.Pair;
import server.DimentionMirrorEntry;
import server.QuickMoveEntry;
import server.games.BingoGame;
import java.util.List;

public class ServerConstants
        
{
    
    public static String mrank1 = null;
    public static String prank1 = null;
    public static String crank1 = null;
    
    public static String Gateway_IP;
    public static final short MAPLE_VERSION = 362;
    public static final byte MAPLE_PATCH = 3;
    public static boolean MAPLE_VERSION_IS_TEST;
    public static boolean Use_Fixed_IV;
    public static boolean Use_Localhost;
    public static boolean DEBUG_RECEIVE;
    public static boolean DEBUG_SEND;
    public static boolean ServerTest;
    public static int starForceSalePercent;
    public static int starForceSalePercents;
    public static int starForcePlusProb;
    public static int amazingscrollPlusProb;
    public static int soulPlusProb;
    public static int BuddyChatPort;
    public static int EventBonusExp;
    public static int WeddingExp;
    public static int PartyExp;
    public static int PcRoomExp;
    public static int RainbowWeekExp;
    public static int BoomupExp;
    public static int PortionExp;
    public static int RestExp;
    public static int ItemExp;
    public static int ValueExp;
    public static int IceExp;
    public static int HpLiskExp;
    public static int FieldBonusExp;
    public static int EventBonusExp2;
    public static int FieldBonusExp2;
    public static final byte check = 1;
    public static int StartMap;
    public static int warpMap;
    public static int fishMap;
    public static int csNpc;
    public static int JuhunFever;
    public static String WORLD_UI;
    public static String SUNDAY_TEXT;
    public static String SUNDAY_DATE;
    public static String serverMessage;
    public static String mailid;
    public static String mailpw;
    public static boolean ChangeMapUI;
    public static boolean feverTime;
    public static int ReqDailyLevel;
    public static List<BingoGame> BingoGameHolder;
    public static List<QuickMoveEntry> quicks;
    public static List<DimentionMirrorEntry> mirrors;
    public static List<Pair<Integer, Long>> boss;
    public static List<Pair<Integer, Long>> boss2;
    public static List<Pair<Integer, Integer>> CashMainInfo;
    public static int SgoldappleSuc;
    public static List<Triple<Integer, Integer, Integer>> goldapple;
    public static List<Triple<Integer, Integer, Integer>> Sgoldapple;
    public static List<Pair<Integer, Integer>> NeoPosList;
    public static List<Integer> FirstLogin;
    public static boolean ConnectorSetting;
    public static boolean Event_Blooming;
    public static boolean Event_MapleLive;
    public static String SundayMapleUI;
    public static String SundayMapleTEXTLINE_1;
    public static String SundayMapleTEXTLINE_2;
    public static int reboottime;
    public static Thread t;
    public static ScheduledFuture<?> ts;
    
        // World Boss Settings
    public static int worldNpc = 9000044;
    public static int worldboss = 8881000;
    public static int worldreward = 4031867;
    public static int worldbossmap = 211070100;
    public static int worldbossfirstmap = 680000721;

    // World Boss Item Settings
    public final static int pice = 1;
    //public final static int failitem = 4310080; 실패보상삭제
    public final static int[] hour = {3, 6, 9, 12, 15, 18, 21, 24};
    
    static {
        ServerConstants.Gateway_IP = "175.207.0.33";
        ServerConstants.MAPLE_VERSION_IS_TEST = false;
        ServerConstants.Use_Fixed_IV = false;
        ServerConstants.Use_Localhost = false;
        ServerConstants.DEBUG_RECEIVE = false;
        ServerConstants.DEBUG_SEND = false;
        ServerConstants.ServerTest = false;
        ServerConstants.starForceSalePercent = 30;
        ServerConstants.starForceSalePercents = 10;
        ServerConstants.starForcePlusProb = 5;
        ServerConstants.amazingscrollPlusProb = 5;
        ServerConstants.soulPlusProb = 2;
        ServerConstants.BuddyChatPort = Integer.parseInt(ServerProperties.getProperty("ports.buddy"));
        ServerConstants.EventBonusExp = Integer.parseInt(ServerProperties.getProperty("world.eventBonus"));
        ServerConstants.WeddingExp = Integer.parseInt(ServerProperties.getProperty("world.weddingBonus"));
        ServerConstants.PartyExp = Integer.parseInt(ServerProperties.getProperty("world.partyBonus"));
        ServerConstants.PcRoomExp = Integer.parseInt(ServerProperties.getProperty("world.pcBonus"));
        ServerConstants.RainbowWeekExp = Integer.parseInt(ServerProperties.getProperty("world.rainbowBonus"));
        ServerConstants.BoomupExp = Integer.parseInt(ServerProperties.getProperty("world.boomBonus"));
        ServerConstants.PortionExp = Integer.parseInt(ServerProperties.getProperty("world.portionBonus"));
        ServerConstants.RestExp = Integer.parseInt(ServerProperties.getProperty("world.restBonus"));
        ServerConstants.ItemExp = Integer.parseInt(ServerProperties.getProperty("world.itemBonus"));
        ServerConstants.ValueExp = Integer.parseInt(ServerProperties.getProperty("world.valueBonus"));
        ServerConstants.IceExp = Integer.parseInt(ServerProperties.getProperty("world.iceBonus"));
        ServerConstants.HpLiskExp = Integer.parseInt(ServerProperties.getProperty("world.hpLiskBonus"));
        ServerConstants.FieldBonusExp = Integer.parseInt(ServerProperties.getProperty("world.fieldBonus"));
        ServerConstants.EventBonusExp2 = Integer.parseInt(ServerProperties.getProperty("world.eventBonus2"));
        ServerConstants.FieldBonusExp2 = Integer.parseInt(ServerProperties.getProperty("world.fieldBonus2"));
        ServerConstants.StartMap = Integer.parseInt(ServerProperties.getProperty("world.startMap"));
        ServerConstants.warpMap = Integer.parseInt(ServerProperties.getProperty("world.warpMap"));
        ServerConstants.fishMap = 680000711;

        ServerConstants.csNpc = 9001174;
        ServerConstants.JuhunFever = 0;
        ServerConstants.WORLD_UI = "UI/UIWindowEvent.img/sundayMaple";
        ServerConstants.SUNDAY_TEXT = "#sunday# #fn나눔고딕 ExtraBold##fs20##fc0xFFFFFFFF#경험치 3배 쿠폰(15분) #fc0xFFFFD800#5개 #fc0xFFFFFFFF#지급!\\n#sunday# #fs20##fc0xFFFFFFFF#RISE 포인트 획득 가능량 #fc0xFFFFD800#2배!#fc0xFFFFFFFF#";
        ServerConstants.SUNDAY_DATE = "#fn나눔고딕 ExtraBold##fs15##fc0xFFB7EC00#2019년 12월 22일 일요일";
        ServerConstants.serverMessage = "";
        ServerConstants.mailid = "theblackmaplestory";
        ServerConstants.mailpw = "ejqmfforrkt3214!";
        ServerConstants.ChangeMapUI = false;
        ServerConstants.feverTime = false;
        ServerConstants.ReqDailyLevel = 33;
        ServerConstants.BingoGameHolder = new ArrayList<BingoGame>();
        ServerConstants.quicks = new ArrayList<QuickMoveEntry>();
        ServerConstants.mirrors = new ArrayList<DimentionMirrorEntry>();
        ServerConstants.boss = new ArrayList<Pair<Integer, Long>>();
        ServerConstants.boss2 = new ArrayList<Pair<Integer, Long>>();
        ServerConstants.CashMainInfo = new ArrayList<Pair<Integer, Integer>>();
        ServerConstants.goldapple = new ArrayList<Triple<Integer, Integer, Integer>>();
        ServerConstants.Sgoldapple = new ArrayList<Triple<Integer, Integer, Integer>>();
        ServerConstants.NeoPosList = new ArrayList<Pair<Integer, Integer>>();
        ServerConstants.FirstLogin = new ArrayList<Integer>();
        ServerConstants.ConnectorSetting = false;
        ServerConstants.Event_Blooming = true;
        ServerConstants.Event_MapleLive = false;
        ServerConstants.SundayMapleUI = "UI/UIWindowEvent.img/sundayMaple";
        ServerConstants.SundayMapleTEXTLINE_1 = "";
        ServerConstants.SundayMapleTEXTLINE_2 = "";
        
        ServerConstants.reboottime = 0;
        ServerConstants.t = null;
        ServerConstants.ts = null;
    }
    
    
    public enum PlayerGMRank
    {
        NORMAL('@', 0), 
        DONATOR('#', 1), 
        SUPERDONATOR('$', 2), 
        INTERN('%', 3), 
        GM('!', 4), 
        SUPERGM('!', 5), 
        ADMIN('!', 6);
        
        private char commandPrefix;
        private int level;
        
        private PlayerGMRank(final char ch, final int level) {
            this.commandPrefix = ch;
            this.level = level;
        }
        
        public char getCommandPrefix() {
            return this.commandPrefix;
        }
        
        public int getLevel() {
            return this.level;
        }
        
        private static /* synthetic */ PlayerGMRank[] $values() {
            return new PlayerGMRank[] { PlayerGMRank.NORMAL, PlayerGMRank.DONATOR, PlayerGMRank.SUPERDONATOR, PlayerGMRank.INTERN, PlayerGMRank.GM, PlayerGMRank.SUPERGM, PlayerGMRank.ADMIN };
        }
        
        static {
            PlayerGMRank[] $VALUES = $values();
        }
    }
    
    public enum CommandType
    {
        NORMAL(0), 
        TRADE(1);
        
        private int level;
        
        private CommandType(final int level) {
            this.level = level;
        }
        
        public int getType() {
            return this.level;
        }
        
        private static /* synthetic */ CommandType[] $values() {
            return new CommandType[] { CommandType.NORMAL, CommandType.TRADE };
        }
        
        static {
            CommandType[] $VALUES = $values();
        }
    }
}
