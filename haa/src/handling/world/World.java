// 
// Decompiled by Procyon v0.5.36
// 

package handling.world;

import handling.world.guild.MapleGuildAlliance;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuild;
import client.BuddyList;
import client.BuddylistEntry;
import client.MapleClient;
import log.LogType;
import log.DBLogger;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import database.DatabaseConnection;
import tools.packet.CWvsContext;
import client.inventory.Item;
import tools.packet.CField;
import client.inventory.MapleInventoryType;
import handling.RecvPacketOpcode;
import tools.data.LittleEndianAccessor;
import java.util.concurrent.atomic.AtomicInteger;
import handling.channel.PlayerStorage;
import tools.CollectionUtil;
import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import handling.farm.FarmServer;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import java.util.ArrayList;
import client.MapleCharacter;
import java.util.List;

public class World
{
    public static void init() {
        Find.findChannel(0);
        Messenger.getMessenger(0);
        Party.getParty(0);
    }
    
    public static List<MapleCharacter> getAllCharacters() {
        final List<MapleCharacter> temp = new ArrayList<MapleCharacter>();
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            for (final MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                if (!temp.contains(chr)) {
                    temp.add(chr);
                }
            }
        }
        for (final MapleCharacter chr2 : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
            temp.add(chr2);
        }
        for (final MapleCharacter chr2 : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
            temp.add(chr2);
        }
        for (final MapleCharacter chr2 : FarmServer.getPlayerStorage().getAllCharacters().values()) {
            temp.add(chr2);
        }
        return temp;
    }
    
    public static MapleCharacter getChar(final int id) {
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            for (final MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                if (chr.getId() == id) {
                    return chr;
                }
            }
        }
        for (final MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
            if (csplayer.getId() == id) {
                return csplayer;
            }
        }
        for (final MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
            if (csplayer.getId() == id) {
                return csplayer;
            }
        }
        for (final MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
            if (csplayer.getId() == id) {
                return csplayer;
            }
        }
        return null;
    }
    
    public static String getStatus() {
        final StringBuilder ret = new StringBuilder();
        int totalUsers = 0;
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            ret.append("Channel ");
            ret.append(cs.getChannel());
            ret.append(": ");
            final int channelUsers = cs.getConnectedClients();
            totalUsers += channelUsers;
            ret.append(channelUsers);
            ret.append(" users\n");
        }
        ret.append("Total users online: ");
        ret.append(totalUsers);
        ret.append("\n");
        return ret.toString();
    }
    
    public static Map<Integer, Integer> getConnected() {
        final Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        int total = 0;
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            final int curConnected = cs.getConnectedClients();
            ret.put(cs.getChannel(), curConnected);
            total += curConnected;
        }
        ret.put(0, total);
        return ret;
    }
    
    public static List<CheaterData> getCheaters() {
        final List<CheaterData> allCheaters = new ArrayList<CheaterData>();
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            allCheaters.addAll(cs.getCheaters());
        }
        Collections.sort(allCheaters);
        return CollectionUtil.copyFirst(allCheaters, 20);
    }
    
    public static List<CheaterData> getReports() {
        final List<CheaterData> allCheaters = new ArrayList<CheaterData>();
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            allCheaters.addAll(cs.getReports());
        }
        Collections.sort(allCheaters);
        return CollectionUtil.copyFirst(allCheaters, 20);
    }
    
    public static boolean isConnected(final String charName) {
        return Find.findChannel(charName) > 0;
    }
    
    public static void toggleMegaphoneMuteState() {
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.toggleMegaphoneMuteState();
        }
    }
    
    public static void ChannelChange_Data(final CharacterTransfer Data, final int characterid, final int toChannel) {
        getStorage(toChannel).registerPendingPlayer(Data, characterid);
    }
    
    public static void isCharacterListConnected(final String name, final List<String> charName) {
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            for (final String c : charName) {
                if (cs.getPlayerStorage().getCharacterByName(c) != null) {
                    cs.getPlayerStorage().deregisterPlayer(cs.getPlayerStorage().getCharacterByName(c));
                }
            }
        }
    }
    
    public static PlayerStorage getStorage(final int channel) {
        if (channel == -10) {
            return CashShopServer.getPlayerStorage();
        }
        if (channel == -20) {
            return AuctionServer.getPlayerStorage();
        }
        return ChannelServer.getInstance(channel).getPlayerStorage();
    }
    
    public static int getPendingCharacterSize() {
        int ret = 0;
        for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
            ret += cserv.getPlayerStorage().pendingCharacterSize();
        }
        return ret;
    }
    
    public static boolean isChannelAvailable(final int ch) {
        return ChannelServer.getInstance(ch) != null && ChannelServer.getInstance(ch).getPlayerStorage() != null && ChannelServer.getInstance(ch).getPlayerStorage().getConnectedClients() < ((ch == 1) ? 600 : 400);
    }
    
    public static class Party
    {
        private static Map<Integer, MapleParty> parties;
        private static final AtomicInteger runningPartyId;
        private static final AtomicInteger runningExpedId;
        
        public static void partyChat(final MapleCharacter chr, final String chattext, final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
            partyChat(chr, chattext, 1, slea, recv);
        }
        
        public static void partyPacket(final int partyid, final byte[] packet, final MaplePartyCharacter exception) {
            final MapleParty party = getParty(partyid);
            if (party == null) {
                return;
            }
            for (final MaplePartyCharacter partychar : party.getMembers()) {
                final int ch = Find.findChannel(partychar.getName());
                if (ch > 0 && (exception == null || partychar.getId() != exception.getId())) {
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (chr == null) {
                        continue;
                    }
                    chr.getClient().getSession().writeAndFlush((Object)packet);
                }
            }
        }
        
        public static void partyChat(final MapleCharacter player, final String chattext, final int mode, final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
            final MapleParty party = getParty(player.getParty().getId());
            if (party == null) {
                return;
            }
            Item item = null;
            if (recv == RecvPacketOpcode.PARTYCHATITEM && player != null) {
                final byte invType = (byte)slea.readInt();
                final short pos = (short)slea.readInt();
                item = player.getInventory(MapleInventoryType.getByType((byte)((pos > 0) ? invType : -1))).getItem(pos);
            }
            for (final MaplePartyCharacter partychar : party.getMembers()) {
                final int ch = Find.findChannel(partychar.getName());
                if (ch > 0) {
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (chr == null || chr.getName().equalsIgnoreCase(player.getName())) {
                        continue;
                    }
                    chr.getClient().getSession().writeAndFlush((Object)CField.multiChat(player, chattext, mode, item));
                    if (chr.getClient().isMonitored()) {}
                }
            }
        }
        
        public static void partyMessage(final int partyid, final String chattext) {
            final MapleParty party = getParty(partyid);
            if (party == null) {
                return;
            }
            for (final MaplePartyCharacter partychar : party.getMembers()) {
                final int ch = Find.findChannel(partychar.getName());
                if (ch > 0) {
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (chr == null) {
                        continue;
                    }
                    chr.dropMessage(5, chattext);
                }
            }
        }
        
        public static void updateParty(final int partyid, final PartyOperation operation, final MaplePartyCharacter target) {
            final MapleParty party = getParty(partyid);
            if (party == null) {
                return;
            }
            final int oldSize = party.getMembers().size();
            final int oldInd = -1;
            switch (operation) {
                case JOIN: {
                    party.addMember(target);
                    break;
                }
                case EXPEL:
                case LEAVE: {
                    party.removeMember(target);
                    break;
                }
                case DISBAND: {
                    disbandParty(partyid);
                    break;
                }
                case SILENT_UPDATE:
                case LOG_ONOFF: {
                    party.updateMember(target);
                    break;
                }
                case CHANGE_LEADER:
                case CHANGE_LEADER_DC: {
                    party.setLeader(target);
                    break;
                }
                default: {
                    throw new RuntimeException("Unhandeled updateParty operation " + operation.name());
                }
            }
            if (operation == PartyOperation.LEAVE || operation == PartyOperation.EXPEL) {
                final int chz = Find.findChannel(target.getName());
                if (chz > 0) {
                    final MapleCharacter chr = World.getStorage(chz).getCharacterByName(target.getName());
                    if (chr != null) {
                        chr.setParty(null);
                        chr.getClient().getSession().writeAndFlush((Object)CWvsContext.PartyPacket.updateParty(chr.getClient().getChannel(), party, operation, target));
                    }
                }
                if (target.getId() == party.getLeader().getId() && party.getMembers().size() > 0) {
                    MaplePartyCharacter lchr = null;
                    for (final MaplePartyCharacter pchr : party.getMembers()) {
                        if (pchr != null && (lchr == null || lchr.getLevel() < pchr.getLevel())) {
                            lchr = pchr;
                        }
                    }
                    if (lchr != null) {
                        updateParty(partyid, PartyOperation.CHANGE_LEADER_DC, lchr);
                    }
                }
            }
            if (party.getMembers().size() <= 0) {
                disbandParty(partyid);
            }
            for (final MaplePartyCharacter partychar : party.getMembers()) {
                if (partychar == null) {
                    continue;
                }
                final int ch = Find.findChannel(partychar.getName());
                if (ch <= 0) {
                    continue;
                }
                final MapleCharacter chr2 = World.getStorage(ch).getCharacterByName(partychar.getName());
                if (chr2 == null) {
                    continue;
                }
                if (operation == PartyOperation.DISBAND) {
                    chr2.setParty(null);
                }
                else {
                    chr2.setParty(party);
                }
                chr2.getClient().getSession().writeAndFlush((Object)CWvsContext.PartyPacket.updateParty(chr2.getClient().getChannel(), party, operation, target));
                chr2.getStat().recalcLocalStats(chr2);
            }
        }
        
        public static MapleParty createParty(final MaplePartyCharacter chrfor) {
            final MapleParty party = new MapleParty(Party.runningPartyId.getAndIncrement(), chrfor);
            Party.parties.put(party.getId(), party);
            return party;
        }
        
        public static MapleParty getParty(final int partyid) {
            return Party.parties.get(partyid);
        }
        
        public static MapleParty disbandParty(final int partyid) {
            final MapleParty ret = Party.parties.remove(partyid);
            if (ret == null) {
                return null;
            }
            ret.disband();
            return ret;
        }
        
        static {
            Party.parties = new HashMap<Integer, MapleParty>();
            runningPartyId = new AtomicInteger(1);
            runningExpedId = new AtomicInteger(1);
            Connection con = null;
            PreparedStatement ps = null;
            final ResultSet rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("UPDATE characters SET party = -1, fatigue = 0");
                ps.executeUpdate();
                ps.close();
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
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
                }
                catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            finally {
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
                }
                catch (SQLException se2) {
                    se2.printStackTrace();
                }
            }
        }
    }
    
    public static class Buddy
    {
        public static void buddyChat(final int[] recipientCharacterIds, final MapleCharacter player, final String chattext, final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
            String targets = "";
            Item item = null;
            if (recv == RecvPacketOpcode.PARTYCHATITEM && player != null) {
                final byte invType = (byte)slea.readInt();
                final byte pos = (byte)slea.readInt();
                item = player.getInventory(MapleInventoryType.getByType((byte)((pos > 0) ? invType : -1))).getItem(pos);
            }
            for (final int characterId : recipientCharacterIds) {
                final int ch = Find.findChannel(characterId);
                if (ch > 0) {
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(characterId);
                    if (chr != null && player != null && chr.getBuddylist().containsVisible(player.getAccountID())) {
                        targets = targets + chr.getName() + ", ";
                        chr.getClient().getSession().writeAndFlush((Object)CField.multiChat(player, chattext, 0, item));
                        if (chr.getClient().isMonitored()) {}
                    }
                }
            }
            DBLogger.getInstance().logChat(LogType.Chat.Buddy, player.getId(), player.getName(), chattext, "\uc218\uc2e0 : " + targets);
        }
        
        public static void updateBuddies(final String name, final int characterId, final int channel, final int[] buddies, final int accId, final boolean offline) {
            for (final int buddy : buddies) {
                final int ch = Find.findAccChannel(buddy);
                if (ch > 0) {
                    final MapleClient c = ChannelServer.getInstance(ch).getPlayerStorage().getClientById(buddy);
                    if (c != null && c.getPlayer() != null) {
                        final BuddylistEntry ble = c.getPlayer().getBuddylist().get(accId);
                        if (ble != null && ble.isVisible()) {
                            int mcChannel;
                            if (offline) {
                                ble.setChannel(-1);
                                mcChannel = -1;
                            }
                            else {
                                ble.setChannel(channel);
                                mcChannel = channel - 1;
                            }
                            ble.setName(name);
                            ble.setCharacterId(characterId);
                            c.getSession().writeAndFlush((Object)CWvsContext.BuddylistPacket.updateBuddyChannel(ble.getCharacterId(), accId, mcChannel, name));
                        }
                    }
                }
            }
        }
        
        public static void buddyChanged(final int cid, final int cidFrom, final String name, final int channel, final BuddyList.BuddyOperation operation, final int level, final int job, final int accId, final String memo) {
            final int ch = Find.findChannel(cid);
            if (ch > 0) {
                final MapleCharacter addChar = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
                if (addChar != null) {
                    final BuddyList buddylist = addChar.getBuddylist();
                    switch (operation) {
                        case ADDED: {
                            if (buddylist.contains(accId)) {
                                buddylist.put(new BuddylistEntry(name, name, accId, cidFrom, "\uadf8\ub8f9 \ubbf8\uc9c0\uc815", channel, true, level, job, memo));
                                addChar.getClient().getSession().writeAndFlush((Object)CWvsContext.BuddylistPacket.updateBuddyChannel(cidFrom, accId, channel, buddylist.get(accId).getName()));
                                break;
                            }
                            break;
                        }
                        case DELETED: {
                            if (buddylist.contains(accId)) {
                                buddylist.put(new BuddylistEntry(name, name, accId, cidFrom, "\uadf8\ub8f9 \ubbf8\uc9c0\uc815", -1, buddylist.get(accId).isVisible(), level, job, memo));
                                addChar.getClient().getSession().writeAndFlush((Object)CWvsContext.BuddylistPacket.updateBuddyChannel(cidFrom, accId, -1, buddylist.get(accId).getName()));
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        public static BuddyList.BuddyAddResult requestBuddyAdd(final String addName, final int accid, final int channelFrom, final int cidFrom, final String nameFrom, final int levelFrom, final int jobFrom, final String groupName, final String memo) {
            for (final ChannelServer server : ChannelServer.getAllInstances()) {
                final MapleCharacter addChar = server.getPlayerStorage().getCharacterByName(addName);
                if (addChar != null) {
                    final BuddyList buddylist = addChar.getBuddylist();
                    if (buddylist.isFull()) {
                        return BuddyList.BuddyAddResult.BUDDYLIST_FULL;
                    }
                    if (!buddylist.contains(accid)) {
                        buddylist.addBuddyRequest(addChar.getClient(), accid, cidFrom, nameFrom, nameFrom, channelFrom, levelFrom, jobFrom, groupName, memo);
                    }
                    else {
                        if (buddylist.containsVisible(accid)) {
                            return BuddyList.BuddyAddResult.ALREADY_ON_LIST;
                        }
                        continue;
                    }
                }
            }
            return BuddyList.BuddyAddResult.OK;
        }
        
        public static void loggedOn(final String name, final int characterId, final int channel, final int accId, final int[] buddies) {
            updateBuddies(name, characterId, channel, buddies, accId, false);
        }
        
        public static void loggedOff(final String name, final int characterId, final int channel, final int accId, final int[] buddies) {
            updateBuddies(name, characterId, channel, buddies, accId, true);
        }
    }
    
    public static class Messenger
    {
        private static Map<Integer, MapleMessenger> messengers;
        private static final AtomicInteger runningMessengerId;
        
        public static MapleMessenger createMessenger(final MapleMessengerCharacter chrfor) {
            final int messengerid = Messenger.runningMessengerId.getAndIncrement();
            final MapleMessenger messenger = new MapleMessenger(messengerid, chrfor);
            Messenger.messengers.put(messenger.getId(), messenger);
            return messenger;
        }
        
        public static void declineChat(final String target, final String namefrom) {
            final int ch = Find.findChannel(target);
            if (ch > 0) {
                final ChannelServer cs = ChannelServer.getInstance(ch);
                final MapleCharacter chr = cs.getPlayerStorage().getCharacterByName(target);
                if (chr != null) {
                    final MapleMessenger messenger = chr.getMessenger();
                    if (messenger != null) {
                        chr.getClient().getSession().writeAndFlush((Object)CField.messengerNote(namefrom, 5, 0));
                    }
                }
            }
        }
        
        public static MapleMessenger getMessenger(final int messengerid) {
            return Messenger.messengers.get(messengerid);
        }
        
        public static void leaveMessenger(final int messengerid, final MapleMessengerCharacter target) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            final int position = messenger.getPositionByName(target.getName());
            messenger.removeMember(target);
            for (final MapleMessengerCharacter mmc : messenger.getMembers()) {
                if (mmc != null) {
                    final int ch = Find.findChannel(mmc.getId());
                    if (ch <= 0) {
                        continue;
                    }
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(mmc.getName());
                    if (chr == null) {
                        continue;
                    }
                    chr.getClient().getSession().writeAndFlush((Object)CField.removeMessengerPlayer(position));
                }
            }
        }
        
        public static void silentLeaveMessenger(final int messengerid, final MapleMessengerCharacter target) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            messenger.silentRemoveMember(target);
        }
        
        public static void silentJoinMessenger(final int messengerid, final MapleMessengerCharacter target) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            messenger.silentAddMember(target);
        }
        
        public static void updateMessenger(final int messengerid, final String namefrom, final int fromchannel) {
            final MapleMessenger messenger = getMessenger(messengerid);
            final int position = messenger.getPositionByName(namefrom);
            for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                    final int ch = Find.findChannel(messengerchar.getName());
                    if (ch <= 0) {
                        continue;
                    }
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr == null) {
                        continue;
                    }
                    final MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(namefrom);
                    chr.getClient().getSession().writeAndFlush((Object)CField.updateMessengerPlayer(namefrom, from, position, fromchannel - 1));
                }
            }
        }
        
        public static void joinMessenger(final int messengerid, final MapleMessengerCharacter target, final String from, final int fromchannel) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            messenger.addMember(target);
            final int position = messenger.getPositionByName(target.getName());
            for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null) {
                    final int mposition = messenger.getPositionByName(messengerchar.getName());
                    final int ch = Find.findChannel(messengerchar.getName());
                    if (ch <= 0) {
                        continue;
                    }
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr == null) {
                        continue;
                    }
                    if (!messengerchar.getName().equals(from)) {
                        final MapleCharacter fromCh = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(from);
                        if (fromCh == null) {
                            continue;
                        }
                        chr.getClient().getSession().writeAndFlush((Object)CField.addMessengerPlayer(from, fromCh, position, fromchannel - 1));
                        fromCh.getClient().getSession().writeAndFlush((Object)CField.addMessengerPlayer(chr.getName(), chr, mposition, messengerchar.getChannel() - 1));
                    }
                    else {
                        chr.getClient().getSession().writeAndFlush((Object)CField.joinMessenger(mposition));
                    }
                }
            }
        }
        
        public static void messengerChat(final int messengerid, final String charname, final String text, final String namefrom) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                    final int ch = Find.findChannel(messengerchar.getName());
                    if (ch <= 0) {
                        continue;
                    }
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr == null) {
                        continue;
                    }
                    chr.getClient().getSession().writeAndFlush((Object)CField.messengerChat(charname, text));
                }
            }
        }
        
        public static void messengerChatNow(final int messengerid, final String charname, final String text, final String namefrom) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                    final int ch = Find.findChannel(messengerchar.getName());
                    if (ch <= 0) {
                        continue;
                    }
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr == null) {
                        continue;
                    }
                    chr.getClient().getSession().writeAndFlush((Object)CField.messengerChatNow(chr, messengerid, charname, text));
                }
            }
        }
        
        public static void messengerWhisperChat(final int messengerid, final String charname, final String text, final String namefrom) {
            final MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                    final int ch = Find.findChannel(messengerchar.getName());
                    if (ch <= 0) {
                        continue;
                    }
                    final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr == null) {
                        continue;
                    }
                    chr.getClient().getSession().writeAndFlush((Object)CField.messengerWhisperChat(charname, text));
                }
            }
        }
        
        public static void messengerInvite(final String sender, final int messengerid, final String target, final int fromchannel, final boolean gm) {
            if (World.isConnected(target)) {
                final int ch = Find.findChannel(target);
                if (ch > 0) {
                    final MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(sender);
                    final MapleCharacter targeter = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target);
                    if (targeter != null && targeter.getMessenger() == null) {
                        if (!targeter.isIntern() || gm) {
                            targeter.getClient().getSession().writeAndFlush((Object)CField.messengerInvite(sender, messengerid));
                            from.getClient().getSession().writeAndFlush((Object)CField.messengerNote(target, 4, 1));
                        }
                        else {
                            from.getClient().getSession().writeAndFlush((Object)CField.messengerNote(target, 4, 0));
                        }
                    }
                    else {
                        from.getClient().getSession().writeAndFlush((Object)CField.messengerChat(sender, " : " + target + " is already using Maple Messenger"));
                    }
                }
            }
        }
        
        static {
            Messenger.messengers = new HashMap<Integer, MapleMessenger>();
            (runningMessengerId = new AtomicInteger()).set(1);
        }
    }
    
    public static class Guild {

        /*  751 */ private static final Map<Integer, MapleGuild> guilds = new ConcurrentHashMap<>();

        public static void addLoadedGuild(MapleGuild f) {
            /*  754 */ if (f.isProper()) {
                /*  755 */ guilds.put(Integer.valueOf(f.getId()), f);
            }
        }

        public static int createGuild(int leaderId, String name) {
            /*  760 */ return MapleGuild.createGuild(leaderId, name);
        }

        public static Collection<MapleGuild> getGuilds() {
            /*  764 */ return guilds.values();
        }

        public static MapleGuild getGuild(int id) {
            /*  768 */ MapleGuild ret = guilds.get(Integer.valueOf(id));
            /*  769 */ if (ret == null) {
                /*  770 */ ret = new MapleGuild(id);
                /*  771 */ if (ret == null || ret.getId() <= 0 || !ret.isProper()) {
                    /*  772 */ return null;
                }
                /*  774 */ guilds.put(Integer.valueOf(id), ret);
            }
            /*  776 */ return ret;
        }

        public static List<MapleGuild> getGuildsByName(String name) {
            /*  780 */ List<MapleGuild> ret = new ArrayList<>();
            /*  781 */ for (MapleGuild g : guilds.values()) {
                /*  782 */ if (g.getName().matches(name)) {
                    /*  783 */ ret.add(g);
                }
            }
            /*  786 */ return ret;
        }

        public static List<MapleGuild> getGuildsByName(String name, boolean option, byte type) {
            /*  790 */ List<MapleGuild> ret = new ArrayList<>();
            /*  791 */ for (MapleGuild g : guilds.values()) {
                /*  792 */ if (option) {
                    /*  793 */ if (type == 1) {
                        /*  794 */ if (g.getName().contains(name)) /*  795 */ {
                            ret.add(g);
                        }
                        continue;
                    }
                    /*  797 */ if (type == 2) {
                        /*  798 */ if (g.getLeaderName().contains(name)) /*  799 */ {
                            ret.add(g);
                        }
                        continue;
                    }
                    /*  801 */ if (type == 3) {
                        /*  802 */ if (g.getName().matches(name)) {
                            /*  803 */ ret.add(g);
                        }
                        /*  805 */ if (g.getLeaderName().matches(name)) /*  806 */ {
                            ret.add(g);
                        }
                    }
                    continue;
                }
                /*  810 */ if (type == 1) {
                    /*  811 */ if (g.getName().contains(name)) /*  812 */ {
                        ret.add(g);
                    }
                    continue;
                }
                /*  814 */ if (type == 2) {
                    /*  815 */ if (g.getLeaderName().contains(name)) /*  816 */ {
                        ret.add(g);
                    }
                    continue;
                }
                /*  818 */ if (type == 3) {
                    /*  819 */ if (g.getName().contains(name)) {
                        /*  820 */ ret.add(g);
                    }
                    /*  822 */ if (g.getLeaderName().contains(name)) {
                        /*  823 */ ret.add(g);
                    }
                }
            }

            /*  828 */ return ret;
        }

        public static List<MapleGuild> getGuilds(int minlevel, int maxlevel, int minsize, int maxsize, int minmlevel, int maxmlevel) {
            /*  832 */ List<MapleGuild> ret = new ArrayList<>();
            /*  833 */ for (MapleGuild guild : guilds.values()) {
                /*  834 */ if (guild.getLevel() >= minlevel && guild.getLevel() <= maxlevel && guild.getMembers().size() >= minsize && guild.getMembers().size() <= maxsize && guild.getLevel() >= minmlevel && guild.getLevel() <= maxmlevel) {
                    /*  835 */ ret.add(guild);
                }
            }
            /*  838 */ return ret;
        }

        public static MapleGuild getGuildByName(String guildName) {
            /*  842 */ for (MapleGuild g : guilds.values()) {
                /*  843 */ if (g.getName().equalsIgnoreCase(guildName)) {
                    /*  844 */ return g;
                }
            }
            /*  847 */ return null;
        }

        public static MapleGuild getGuild(MapleCharacter mc) {
            /*  851 */ return getGuild(mc.getGuildId());
        }

        public static void setGuildMemberOnline(MapleGuildCharacter mc, boolean bOnline, int channel) {
            /*  855 */ MapleGuild g = getGuild(mc.getGuildId());
            /*  856 */ if (g != null) {
                /*  857 */ g.setOnline(mc.getId(), bOnline, channel);
            }
        }

        public static void guildPacket(int gid, byte[] message) {
            /*  862 */ MapleGuild g = getGuild(gid);
            /*  863 */ if (g != null) {
                /*  864 */ g.broadcast(message);
            }
        }

        public static int addGuildMember(MapleGuildCharacter mc) {
            /*  869 */ MapleGuild g = getGuild(mc.getGuildId());
            /*  870 */ if (g != null) {
                /*  871 */ return g.addGuildMember(mc);
            }
            /*  873 */ return 0;
        }

        public static void leaveGuild(MapleGuildCharacter mc) {
            /*  877 */ MapleGuild g = getGuild(mc.getGuildId());
            /*  878 */ if (g != null) {
                /*  879 */ g.leaveGuild(mc);
            }
        }

        public static void guildChat(MapleCharacter chr, String msg, LittleEndianAccessor slea, RecvPacketOpcode recv) {
            /*  884 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  885 */ if (g != null) {
                /*  886 */ g.guildChat(chr, msg, slea, recv);
            }
        }

        public static void changeRank(int gid, int cid, int newRank) {
            /*  891 */ MapleGuild g = getGuild(gid);
            /*  892 */ if (g != null) {
                /*  893 */ g.changeRank(cid, newRank);
            }
        }

        public static void expelMember(MapleGuildCharacter initiator, String name, int cid) {
            /*  898 */ MapleGuild g = getGuild(initiator.getGuildId());
            /*  899 */ if (g != null) {
                /*  900 */ g.expelMember(initiator, name, cid);
            }
        }

        public static void setGuildNotice(MapleCharacter chr, String notice) {
            /*  905 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  906 */ if (g != null) {
                /*  907 */ g.setGuildNotice(chr, notice);
            }
        }

        public static void setGuildLeader(int gid, int cid) {
            /*  912 */ MapleGuild g = getGuild(gid);
            /*  913 */ if (g != null) {
                /*  914 */ g.changeGuildLeader(cid);
            }
        }

        public static int getSkillLevel(int gid, int sid) {
            /*  919 */ MapleGuild g = getGuild(gid);
            /*  920 */ if (g != null) {
                /*  921 */ return g.getSkillLevel(sid);
            }
            /*  923 */ return 0;
        }

        public static boolean purchaseSkill(int gid, int sid, String name, int cid) {
            /*  927 */ MapleGuild g = getGuild(gid);
            /*  928 */ if (g != null) {
                /*  929 */ return g.purchaseSkill(sid, name, cid);
            }
            /*  931 */ return false;
        }

        public static boolean activateSkill(int gid, int sid, String name) {
            /*  935 */ MapleGuild g = getGuild(gid);
            /*  936 */ if (g != null) {
                /*  937 */ return g.activateSkill(sid, name);
            }
            /*  939 */ return false;
        }

        public static void memberLevelJobUpdate(MapleGuildCharacter mc) {
            /*  943 */ MapleGuild g = getGuild(mc.getGuildId());
            /*  944 */ if (g != null) {
                /*  945 */ g.memberLevelJobUpdate(mc);
            }
        }

        public static void changeRankTitle(MapleCharacter chr, String[] ranks) {
            /*  950 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  951 */ if (g != null) {
                /*  952 */ g.changeRankTitle(chr, ranks);
            }
        }

        public static void changeRankRole(MapleCharacter chr, int[] roles) {
            /*  957 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  958 */ if (g != null) {
                /*  959 */ g.changeRankRole(chr, roles);
            }
        }

        public static void changeRankTitleRole(MapleCharacter chr, String ranks, int roles, byte type) {
            /*  964 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  965 */ if (g != null) {
                /*  966 */ g.changeRankTitleRole(chr, ranks, roles, type);
            }
        }

        public static void setGuildEmblem(MapleCharacter chr, short bg, byte bgcolor, short logo, byte logocolor) {
            /*  971 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  972 */ if (g != null) {
                /*  973 */ g.setGuildEmblem(chr, bg, bgcolor, logo, logocolor);
            }
        }

        public static void setGuildCustomEmblem(MapleCharacter chr, byte[] imgdata) {
            /*  978 */ MapleGuild g = getGuild(chr.getGuildId());
            /*  979 */ if (g != null) {
                /*  980 */ g.setGuildCustomEmblem(chr, imgdata);
            }
        }

        public static void disbandGuild(int gid) {
            /*  985 */ MapleGuild g = getGuild(gid);
            /*  986 */ if (g != null) {
                /*  987 */ g.disbandGuild();
                /*  988 */ guilds.remove(Integer.valueOf(gid));
            }
        }

        public static void deleteGuildCharacter(int guildid, int charid) {
            /*  996 */ MapleGuild g = getGuild(guildid);
            /*  997 */ if (g != null) {
                /*  998 */ MapleGuildCharacter mc = g.getMGC(charid);
                /*  999 */ if (mc != null) {
                    /* 1000 */ if (mc.getGuildRank() > 1) {

                        /* 1002 */ g.leaveGuild(mc);
                    } else {
                        /* 1004 */ g.disbandGuild();
                    }
                }
            }
        }

        public static boolean increaseGuildCapacity(int gid, boolean b) {
            /* 1011 */ MapleGuild g = getGuild(gid);
            /* 1012 */ if (g != null) {
                /* 1013 */ return g.increaseCapacity(b);
            }
            /* 1015 */ return false;
        }

        public static void gainContribution(int gid, int amount) {
            /* 1019 */ MapleGuild g = getGuild(gid);
            /* 1020 */ if (g != null) {
                /* 1021 */ g.gainGP(amount);
            }
        }

        public static void gainContribution(int gid, int amount, int cid) {
            /* 1026 */ MapleGuild g = getGuild(gid);
            /* 1027 */ if (g != null) {
                /* 1028 */ g.gainGP(amount, true, cid);
            }
        }

        public static int getGP(int gid) {
            /* 1033 */ MapleGuild g = getGuild(gid);
            /* 1034 */ if (g != null) {
                /* 1035 */ return g.getGP();
            }
            /* 1037 */ return 0;
        }

        public static int getInvitedId(int gid) {
            /* 1041 */ MapleGuild g = getGuild(gid);
            /* 1042 */ if (g != null) {
                /* 1043 */ return g.getInvitedId();
            }
            /* 1045 */ return 0;
        }

        public static void setInvitedId(int gid, int inviteid) {
            /* 1049 */ MapleGuild g = getGuild(gid);
            /* 1050 */ if (g != null) {
                /* 1051 */ g.setInvitedId(inviteid);
            }
        }

        public static int getGuildLeader(int guildName) {
            /* 1056 */ MapleGuild mga = getGuild(guildName);
            /* 1057 */ if (mga != null) {
                /* 1058 */ return mga.getLeaderId();
            }
            /* 1060 */ return 0;
        }

        public static int getGuildLeader(String guildName) {
            /* 1064 */ MapleGuild mga = getGuildByName(guildName);
            /* 1065 */ if (mga != null) {
                /* 1066 */ return mga.getLeaderId();
            }
            /* 1068 */ return 0;
        }

        public static void save() {
            /* 1072 */ System.out.println("Saving guilds...");
            /* 1073 */ for (MapleGuild a : guilds.values()) {
                /* 1074 */ a.writeToDB(false);
            }
        }

        public static void load() {
            /* 1079 */ System.out.println("Load guilds...");
            /* 1080 */ Connection con = null;
            /* 1081 */ PreparedStatement ps = null;
            /* 1082 */ ResultSet rs = null;
            try {
                /* 1084 */ con = DatabaseConnection.getConnection();
                /* 1085 */ ps = con.prepareStatement("SELECT * FROM guilds");

                /* 1087 */ rs = ps.executeQuery();

                /* 1089 */ while (rs.next()) {
                    /* 1090 */ getGuild(rs.getInt("guildid"));
                }
                /* 1092 */ rs.close();
                /* 1093 */ ps.close();
                /* 1094 */            } catch (SQLException ex) {
                /* 1095 */ ex.printStackTrace();
            } finally {
                /* 1097 */ if (ps != null) {
                    try {
                        /* 1099 */ ps.close();
                        /* 1100 */                    } catch (SQLException ex) {
                        /* 1101 */ Logger.getLogger(World.class.getName()).log(Level.SEVERE, (String) null, ex);
                    }
                }
                /* 1104 */ if (rs != null) {
                    try {
                        /* 1106 */ rs.close();
                        /* 1107 */                    } catch (SQLException ex) {
                        /* 1108 */ Logger.getLogger(World.class.getName()).log(Level.SEVERE, (String) null, ex);
                    }
                }
                /* 1111 */ if (con != null) {
                    try {
                        /* 1113 */ con.close();
                        /* 1114 */                    } catch (SQLException e) {

                        /* 1116 */ e.printStackTrace();
                    }
                }
            }
        }

        public static void changeEmblem(MapleCharacter chr, int affectedPlayers, MapleGuild mgs) {
            /* 1123 */ World.Broadcast.sendGuildPacket(affectedPlayers, CWvsContext.GuildPacket.guildEmblemChange(chr, (short) mgs.getLogoBG(), (byte) mgs.getLogoBGColor(), (short) mgs.getLogo(), (byte) mgs.getLogoColor()), -1, chr.getGuildId());
            /* 1124 */ setGuildAndRank(affectedPlayers, -1, -1, -1, -1);
        }

        public static void setGuildAndRank(int cid, int guildid, int rank, int contribution, int alliancerank) {
            boolean bDifferentGuild;
            /* 1128 */ int ch = World.Find.findChannel(cid);
            /* 1129 */ if (ch == -1) {
                return;
            }

            /* 1133 */ MapleCharacter mc = World.getStorage(ch).getCharacterById(cid);
            /* 1134 */ if (mc == null) {
                return;
            }

            /* 1138 */ if (guildid == -1 && rank == -1) {
                /* 1139 */ bDifferentGuild = true;
            } else {
                /* 1141 */ bDifferentGuild = (guildid != mc.getGuildId());
                /* 1142 */ mc.setGuildId(guildid);
                /* 1143 */ mc.setGuildRank((byte) rank);
                /* 1144 */ mc.setGuildContribution(contribution);
                /* 1145 */ mc.setAllianceRank((byte) alliancerank);
                /* 1146 */ mc.saveGuildStatus();
            }
            /* 1148 */ if (bDifferentGuild && ch > 0) {
                /* 1150 */ mc.getMap().broadcastMessage(mc, CField.loadGuildIcon(mc), false);
            }
        }
    }
    
    public static class Broadcast
    {
        public static long chatDelay;
        
        public static void broadcastSmega(final byte[] message) {
            for (final ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastSmega(message);
            }
        }
        
        public static void broadcastGMMessage(final byte[] message) {
            for (final ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastGMMessage(message);
            }
        }
        
        public static void broadcastMessage(final byte[] message) {
            for (final ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastMessage(message);
            }
        }
        
        public static void sendPacket(final List<Integer> targetIds, final byte[] packet, final int exception) {
            for (final int i : targetIds) {
                if (i == exception) {
                    continue;
                }
                final int ch = Find.findChannel(i);
                if (ch < 0) {
                    continue;
                }
                final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(i);
                if (c == null) {
                    continue;
                }
                c.getClient().getSession().writeAndFlush((Object)packet);
            }
        }
        
        public static void sendPacket(final int targetId, final byte[] packet) {
            final int ch = Find.findChannel(targetId);
            if (ch < 0) {
                return;
            }
            final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetId);
            if (c != null) {
                c.getClient().getSession().writeAndFlush((Object)packet);
            }
        }
        
        public static void sendGuildPacket(final int targetIds, final byte[] packet, final int exception, final int guildid) {
            if (targetIds == exception) {
                return;
            }
            final int ch = Find.findChannel(targetIds);
            if (ch < 0) {
                return;
            }
            final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetIds);
            if (c != null && c.getGuildId() == guildid) {
                c.getClient().getSession().writeAndFlush((Object)packet);
            }
        }
        
        static {
            Broadcast.chatDelay = 0L;
        }
    }
    
    public static class Find
    {
        private static Map<Integer, Integer> idToChannel;
        private static Map<String, Integer> nameToChannel;
        private static Map<Integer, Integer> accIdToChannel;
        
        public static void forceDeregister(final int id) {
            Find.idToChannel.remove(id);
        }
        
        public static void forceDeregister(final String id) {
            Find.nameToChannel.remove(id.toLowerCase());
        }
        
        public static void register(final int id, final int accId, final String name, final int channel) {
            Find.idToChannel.put(id, channel);
            Find.nameToChannel.put(name.toLowerCase(), channel);
            Find.accIdToChannel.put(accId, channel);
        }
        
        public static void forceAccDeregister(final int id) {
            Find.accIdToChannel.remove(id);
        }
        
        public static void forceDeregister(final int id, final int accId, final String name) {
            Find.idToChannel.remove(id);
            Find.nameToChannel.remove(name.toLowerCase());
            Find.accIdToChannel.remove(accId);
        }
        
        public static int findChannel(final int id) {
            final Integer ret = Find.idToChannel.get(id);
            if (ret == null) {
                return -1;
            }
            if (ret != -10 && ChannelServer.getInstance(ret) == null) {
                forceDeregister(id);
                return -1;
            }
            return ret;
        }
        
        public static int findChannel(final String st) {
            final Integer ret = Find.nameToChannel.get(st.toLowerCase());
            if (ret == null) {
                return -1;
            }
            if (ret != -10 && ChannelServer.getInstance(ret) == null) {
                forceDeregister(st);
                return -1;
            }
            return ret;
        }
        
        public static int findAccChannel(final int id) {
            final Integer ret = Find.accIdToChannel.get(id);
            if (ret == null) {
                return -1;
            }
            if (ret != -10 && ChannelServer.getInstance(ret) == null) {
                forceAccDeregister(id);
                return -1;
            }
            return ret;
        }
        
        public static AccountIdChannelPair[] multiBuddyFind(final BuddyList bl, final int charIdFrom, final int[] accIds) {
            final List<AccountIdChannelPair> foundsChars = new ArrayList<AccountIdChannelPair>(accIds.length);
            for (final int i : accIds) {
                final int ret = findAccChannel(i);
                if (ret > 0) {
                    final MapleClient c = ChannelServer.getInstance(ret).getPlayerStorage().getClientById(i);
                    if (bl.contains(i) && c != null) {
                        final BuddylistEntry ble = bl.get(i);
                        ble.setCharacterId(c.getPlayer().getId());
                        ble.setName(c.getPlayer().getName());
                    }
                }
                foundsChars.add(new AccountIdChannelPair(i, ret));
            }
            Collections.sort(foundsChars);
            return foundsChars.toArray(new AccountIdChannelPair[foundsChars.size()]);
        }
        
        static {
            Find.idToChannel = new ConcurrentHashMap<Integer, Integer>();
            Find.nameToChannel = new ConcurrentHashMap<String, Integer>();
            Find.accIdToChannel = new ConcurrentHashMap<Integer, Integer>();
        }
    }
    
    public static class Alliance
    {
        private static final Map<Integer, MapleGuildAlliance> alliances;
        
        public static MapleGuildAlliance getAlliance(final int allianceid) {
            MapleGuildAlliance ret = Alliance.alliances.get(allianceid);
            if (ret == null) {
                ret = new MapleGuildAlliance(allianceid);
                if (ret == null || ret.getId() <= 0) {
                    return null;
                }
                Alliance.alliances.put(allianceid, ret);
            }
            return ret;
        }
        
        public static int getAllianceLeader(final int allianceid) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            if (mga != null) {
                return mga.getLeaderId();
            }
            return 0;
        }
        
        public static void updateAllianceRanks(final int allianceid, final String[] ranks) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            if (mga != null) {
                mga.setRank(ranks);
            }
        }
        
        public static void updateAllianceNotice(final int allianceid, final String notice) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            if (mga != null) {
                mga.setNotice(notice);
            }
        }
        
        public static boolean canInvite(final int allianceid) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.getCapacity() > mga.getNoGuilds();
        }
        
        public static boolean changeAllianceLeader(final int allianceid, final int cid) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.setLeaderId(cid);
        }
        
        public static boolean changeAllianceLeader(final int allianceid, final int cid, final boolean sameGuild) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.setLeaderId(cid, sameGuild);
        }
        
        public static boolean changeAllianceRank(final int allianceid, final int cid, final int change) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.changeAllianceRank(cid, change);
        }
        
        public static boolean changeAllianceCapacity(final int allianceid) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.setCapacity();
        }
        
        public static boolean disbandAlliance(final int allianceid) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.disband();
        }
        
        public static boolean addGuildToAlliance(final int allianceid, final int gid) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.addGuild(gid);
        }
        
        public static boolean removeGuildFromAlliance(final int allianceid, final int gid, final boolean expelled) {
            final MapleGuildAlliance mga = getAlliance(allianceid);
            return mga != null && mga.removeGuild(gid, expelled);
        }
        
        public static void sendGuild(final int allianceid) {
            final MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                sendGuild(CWvsContext.AlliancePacket.getAllianceUpdate(alliance), -1, allianceid);
                sendGuild(CWvsContext.AlliancePacket.getGuildAlliance(alliance), -1, allianceid);
            }
        }
        
        public static void sendGuild(final byte[] packet, final int exceptionId, final int allianceid) {
            final MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                for (int i = 0; i < alliance.getNoGuilds(); ++i) {
                    final int gid = alliance.getGuildId(i);
                    if (gid > 0 && gid != exceptionId) {
                        Guild.guildPacket(gid, packet);
                    }
                }
            }
        }
        
        public static boolean createAlliance(final String alliancename, final int cid, final int cid2, final int gid, final int gid2) {
            final int allianceid = MapleGuildAlliance.createToDb(cid, alliancename, gid, gid2);
            if (allianceid <= 0) {
                return false;
            }
            final MapleGuild g = Guild.getGuild(gid);
            final MapleGuild g_ = Guild.getGuild(gid2);
            g.setAllianceId(allianceid);
            g_.setAllianceId(allianceid);
            g.changeARank(true);
            g_.changeARank(false);
            final MapleGuildAlliance alliance = getAlliance(allianceid);
            sendGuild(CWvsContext.AlliancePacket.createGuildAlliance(alliance), -1, allianceid);
            sendGuild(CWvsContext.AlliancePacket.getAllianceInfo(alliance), -1, allianceid);
            sendGuild(CWvsContext.AlliancePacket.getGuildAlliance(alliance), -1, allianceid);
            sendGuild(CWvsContext.AlliancePacket.changeAlliance(alliance, true), -1, allianceid);
            return true;
        }
        
        public static void allianceChat(final MapleCharacter player, final String msg, final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
            final MapleGuild g = Guild.getGuild(player.getGuildId());
            if (g != null) {
                final MapleGuildAlliance ga = getAlliance(g.getAllianceId());
                if (ga != null) {
                    for (int i = 0; i < ga.getNoGuilds(); ++i) {
                        final MapleGuild g_ = Guild.getGuild(ga.getGuildId(i));
                        if (g_ != null) {
                            g_.allianceChat(player, msg, slea, recv);
                            if (i == 0) {
                                DBLogger.getInstance().logChat(LogType.Chat.Guild, player.getId(), player.getName(), msg, "[" + ga.getName() + " - \uc5f0\ud569 ]");
                            }
                        }
                    }
                }
            }
        }
        
        public static void setNewAlliance(final int gid, final int allianceid) {
            final MapleGuildAlliance alliance = getAlliance(allianceid);
            final MapleGuild guild = Guild.getGuild(gid);
            if (alliance != null && guild != null) {
                for (int i = 0; i < alliance.getNoGuilds(); ++i) {
                    if (gid == alliance.getGuildId(i)) {
                        guild.setAllianceId(allianceid);
                        guild.broadcast(CWvsContext.AlliancePacket.getAllianceInfo(alliance));
                        guild.broadcast(CWvsContext.AlliancePacket.getGuildAlliance(alliance));
                        guild.broadcast(CWvsContext.AlliancePacket.changeAlliance(alliance, true));
                        guild.changeARank();
                        guild.writeToDB(false);
                    }
                    else {
                        final MapleGuild g_ = Guild.getGuild(alliance.getGuildId(i));
                        if (g_ != null) {
                            g_.broadcast(CWvsContext.AlliancePacket.addGuildToAlliance(alliance, guild));
                            g_.broadcast(CWvsContext.AlliancePacket.changeGuildInAlliance(alliance, guild, true));
                        }
                    }
                }
            }
        }
        
        public static void setOldAlliance(final int gid, final boolean expelled, final int allianceid) {
            final MapleGuildAlliance alliance = getAlliance(allianceid);
            final MapleGuild g_ = Guild.getGuild(gid);
            if (alliance != null) {
                for (int i = 0; i < alliance.getNoGuilds(); ++i) {
                    final MapleGuild guild = Guild.getGuild(alliance.getGuildId(i));
                    if (guild == null) {
                        if (gid != alliance.getGuildId(i)) {
                            alliance.removeGuild(gid, false, true);
                        }
                    }
                    else if (g_ == null || gid == alliance.getGuildId(i)) {
                        guild.changeARank(5);
                        guild.setAllianceId(0);
                        guild.broadcast(CWvsContext.AlliancePacket.disbandAlliance(allianceid));
                    }
                    else if (g_ != null) {
                        guild.broadcast(CWvsContext.serverNotice(5, "", "[" + g_.getName() + "] Guild has left the alliance."));
                        guild.broadcast(CWvsContext.AlliancePacket.changeGuildInAlliance(alliance, g_, false));
                        guild.broadcast(CWvsContext.AlliancePacket.removeGuildFromAlliance(alliance, g_, expelled));
                    }
                }
            }
            if (gid == -1) {
                Alliance.alliances.remove(allianceid);
            }
        }
        
        public static List<byte[]> getAllianceInfo(final int allianceid, final boolean start) {
            final List<byte[]> ret = new ArrayList<byte[]>();
            final MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                if (start) {
                    ret.add(CWvsContext.AlliancePacket.getAllianceInfo(alliance));
                    ret.add(CWvsContext.AlliancePacket.getGuildAlliance(alliance));
                }
                ret.add(CWvsContext.AlliancePacket.getAllianceUpdate(alliance));
            }
            return ret;
        }
        
        public static void save() {
            System.out.println("Saving alliances...");
            for (final MapleGuildAlliance a : Alliance.alliances.values()) {
                a.saveToDb();
            }
        }
        
        static {
            alliances = new ConcurrentHashMap<Integer, MapleGuildAlliance>();
            final Collection<MapleGuildAlliance> allGuilds = MapleGuildAlliance.loadAll();
            for (final MapleGuildAlliance g : allGuilds) {
                Alliance.alliances.put(g.getId(), g);
            }
        }
    }
}
