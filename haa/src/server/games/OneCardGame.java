package server.games;

import client.MapleCharacter;
import constants.GameConstants;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import server.Randomizer;
import server.Timer;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.SLFCGPacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author SLFCG
 */
public class OneCardGame {

    private int objectId, fire;
    private List<OneCard> oneCardDeckInfo = new ArrayList<>();
    private boolean bClockWiseTurn = true;
    private OneCard lastCard;
    private OneCardPlayer lastPlayer;

    public class OneCard {

        /*
         * type
         * 0 ~ 5 -> 1 ~ 6
         * 6 : 공격기 2개
         * 7 : 공격기 3개
         * 8 : 색변환 카드
         * 9 : 1 + 1
         * 10 : 건너뛰기
         * 11 : 리버스
         * 12 : 특수 공격기
         */
 /*
         * color
         * 0 : 빨강
         * 1 : 노랑
         * 2 : 파랑
         * 3 : 초록
         */
        private int color, type, objectId;

        public OneCard(int... args) {
            objectId = args[0];
            color = args[1];
            setType(args[2]);
        }

        public int getColor() {
            return color;
        }

        public void setColor(final int a) {
            color = a;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }
    }

    public class OneCardPlayer {

        private byte position;
        private List<OneCard> cards;
        private MapleCharacter chr;

        public OneCardPlayer(MapleCharacter player, byte position) {
            chr = player;
            cards = new ArrayList<>();
            this.position = position;
        }

        public MapleCharacter getPlayer() {
            return chr;
        }

        public List<OneCard> getCards() {
            return cards;
        }

        public void setCards(List<OneCard> cards) {
            this.cards = cards;
        }

        public byte getPosition() {
            return position;
        }

        public void setPosition(byte position) {
            this.position = position;
        }
    }

    public static class oneCardMatchingInfo {

        public List<MapleCharacter> players = new ArrayList<>();

        public oneCardMatchingInfo(List<MapleCharacter> chrs) {
            for (MapleCharacter chr : chrs) {
                players.add(chr);
            }
        }
    }

    public static List<MapleCharacter> oneCardMatchingQueue = new ArrayList<>();
    private ScheduledFuture<?> oneCardTimer = null;
    private List<OneCardPlayer> Players = new ArrayList<>();
    private Point lastPoint = new Point(0, 0);

    public OneCardGame(List<MapleCharacter> chrs) {
        this.objectId = 100000;
        for (int i = 0; i < chrs.size(); i++) {
            getPlayers().add(new OneCardPlayer(chrs.get(i), (byte) i));
        }
    }

    public OneCardPlayer getPlayer(MapleCharacter chr) {
        for (OneCardPlayer ocp : getPlayers()) {
            if (ocp.chr.getId() == chr.getId()) {
                return ocp;
            }
        }
        return null;
    }

    public void resetDeck() {

        List<Pair<Integer, Integer>> cardData = new ArrayList<>();

        for (OneCardPlayer ocp : getPlayers()) {
            for (OneCard card : ocp.cards) {
                cardData.add(new Pair<>(card.color, card.type));
            }
        }

        if (this.lastCard != null) {
            cardData.add(new Pair<>(this.lastCard.color, this.lastCard.type));
        }

        for (int i = 0; i <= 3; ++i) {
            for (int k = 0; k <= 12; ++k) {
                if (!cardData.contains(new Pair<>(i, k))) {
                    oneCardDeckInfo.add(new OneCard(++this.objectId, i, k));
                }
            }
        }

        oneCardDeckInfo.add(new OneCard(++this.objectId, 4, 12)); // 특수카드는 5개
    }

    private void StartGame(MapleCharacter chr) {
        oneCardDeckInfo.clear();
        fire = 0;
        resetDeck();

        OneCardPlayer first = this.Players.get(Randomizer.nextInt(this.Players.size()));
        sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onStart(this.getPlayers()));

        for (OneCardPlayer bp : this.getPlayers()) {
            List<OneCard> newcards = new ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                int num = Randomizer.nextInt(oneCardDeckInfo.size());
                OneCard card = oneCardDeckInfo.get(num);
                bp.cards.add(card);
                newcards.add(card);
                oneCardDeckInfo.remove(num);
            }
            sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onGetCardResult(bp, newcards));
        }

        OneCard firstCard = oneCardDeckInfo.get(Randomizer.nextInt(oneCardDeckInfo.size()));

        while (firstCard.type >= 6) { // 무적권 숫자로 떠야함
            firstCard = oneCardDeckInfo.get(Randomizer.nextInt(oneCardDeckInfo.size()));
        }

        this.lastCard = firstCard;
        this.lastPlayer = first;

        sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onPutCardResult(null, firstCard));

        oneCardDeckInfo.remove(firstCard);

        List<OneCard> possibleCards = new ArrayList<>();
        for (OneCard card : first.cards) {
            if (card.getType() <= 5) {
                if ((card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) && (getLastCard().getType() <= 5 || (getLastCard().getType() >= 8 && getLastCard().getType() <= 11)) && getFire() == 0) {
                    possibleCards.add(card);
                }
            } else if (card.getType() <= 7) {
                if (getFire() == 0) {
                    if (card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) {
                        possibleCards.add(card);
                    }
                } else {
                    if (card.getType() == 6) {
                        if (card.getColor() == getLastCard().getColor() && (card.getType() == 6 || card.getType() == 7)) {
                            possibleCards.add(card);
                        }
                    } else {
                        if (card.getColor() == getLastCard().getColor() && card.getType() == 7) {
                            possibleCards.add(card);
                        }
                    }
                }
            } else if (card.getType() <= 11) {
                if ((card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) && getFire() == 0) {
                    possibleCards.add(card);
                }
            } else {
                switch (card.getColor()) {
                    case 0: // 오즈
                        if (getLastCard().getColor() == card.getColor()) {
                            possibleCards.add(card);
                        }
                        break;
                    case 1: // 미하일
                        possibleCards.add(card);
                        break;
                    case 2: // 호크아이
                        if (getLastCard().getColor() == card.getColor() && getFire() == 0) {
                            possibleCards.add(card);
                        }
                        break;
                    case 3: // 이리나
                        if (getLastCard().getColor() == card.getColor() && getFire() == 0) {
                            possibleCards.add(card);
                        }
                        break;
                    case 4: // 이카르트
                        possibleCards.add(card);
                        break;
                }
            }
        }

        sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(first, possibleCards, first.cards.size() < 17, isbClockWiseTurn()));

        if (getOneCardTimer() != null) {
            getOneCardTimer().cancel(false);
        }

        setOneCardTimer(Timer.EventTimer.getInstance().schedule(() -> {
            //15초 잠수시 강제로 카드 맥이고 넘어가야함
            skipPlayer();
        }, 15 * 1000));
        //TODO: Run Game Timer
    }

    public void skipPlayer() {

        List<OneCard> newcards = new ArrayList<>();

        for (int i = 0; i < (getFire() > 0 ? getFire() : 1); ++i) {
            if (getOneCardDeckInfo().size() == 0) {
                resetDeck();
                if (getOneCardDeckInfo().size() == 0) {
                    break;
                }
            }
            int num = Randomizer.nextInt(getOneCardDeckInfo().size());
            OneCard card = getOneCardDeckInfo().get(num);
            getLastPlayer().getCards().add(card);
            newcards.add(card);
            getOneCardDeckInfo().remove(num);
        }
        sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onGetCardResult(getLastPlayer(), newcards));

        if (getLastPlayer().getCards().size() >= 17) { // 파산
            setFire(0);
            sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/gameover"));
            getLastPlayer().chr.getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/gameover"));
            sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(5, 0, getLastPlayer().chr.getId(), true));
            playerDead(getLastPlayer(), false);
        } else if (getFire() > 0) {
            setFire(0);
            sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(4, 0, getLastPlayer().chr.getId(), false));
        }

        if (getLastCard().getType() == 11) {
            setbClockWiseTurn(!isbClockWiseTurn());
        }

        OneCardPlayer nextPlayer = setNextPlayer(getLastPlayer(), isbClockWiseTurn());

        this.setLastPlayer(nextPlayer);

        if (nextPlayer != null) {
            List<OneCard> possibleCards = new ArrayList<>();
            for (OneCard card : nextPlayer.getCards()) {
                if (card.getType() <= 5) {
                    if ((card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) && (getLastCard().getType() <= 5 || (getLastCard().getType() >= 8 && getLastCard().getType() <= 11))) {
                        possibleCards.add(card);
                    }
                } else if (card.getType() <= 7) {
                    if (getFire() == 0) {
                        if (card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) {
                            possibleCards.add(card);
                        }
                    } else {
                        if (card.getType() == 6) {
                            if (card.getColor() == getLastCard().getColor() && (card.getType() == 6 || card.getType() == 7)) {
                                possibleCards.add(card);
                            }
                        } else {
                            if (card.getColor() == getLastCard().getColor() && card.getType() == 7) {
                                possibleCards.add(card);
                            }
                        }
                    }
                } else if (card.getType() <= 11) {
                    if (card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) {
                        possibleCards.add(card);
                    }
                } else {
                    switch (card.getColor()) {
                        case 0: // 오즈
                            if (getLastCard().getColor() == card.getColor()) {
                                possibleCards.add(card);
                            }
                            break;
                        case 1: // 미하일
                            possibleCards.add(card);
                            break;
                        case 2: // 호크아이
                            if (getLastCard().getColor() == card.getColor()) {
                                possibleCards.add(card);
                            }
                            break;
                        case 3: // 이리나
                            if (getLastCard().getColor() == card.getColor()) {
                                possibleCards.add(card);
                            }
                            break;
                        case 4: // 이카르트
                            possibleCards.add(card);
                            break;
                    }
                }
            }

            if (getOneCardDeckInfo().size() == 0 || getOneCardDeckInfo().isEmpty()) {
                resetDeck();
                sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(0, 0, 0, false));
                sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, (possibleCards.size() == 0 && nextPlayer.getCards().size() == 16) || nextPlayer.getCards().size() < 16, isbClockWiseTurn()));
            } else {
                sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, (possibleCards.size() == 0 && nextPlayer.getCards().size() == 16) || nextPlayer.getCards().size() < 16, isbClockWiseTurn()));
            }

            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/myturn"));
            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(nextPlayer.getPlayer()));
            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.onShowText("당신의 턴입니다."));
            if (getOneCardTimer() != null) {
                getOneCardTimer().cancel(false);
            }

            setOneCardTimer(Timer.MapTimer.getInstance().schedule(() -> {
                //15초 잠수시 강제로 카드 맥이고 넘어가야함
                skipPlayer();
            }, 15 * 1000));
        }
    }

    public void endGame(OneCardPlayer winner, boolean error) {

        for (OneCardPlayer bp : this.getPlayers()) {
            if (bp.getPlayer().getId() == winner.getPlayer().getId()) {
                if(bp.getPlayer().getKeyValue(501215, "today") <= 0L){
                    bp.getPlayer().setKeyValue(501215, "today", "0");
                }
                if(bp.getPlayer().getKeyValue(501215, "today") >= 50){
                    bp.getPlayer().dropMessage(-8, "하루동안 [미니게임 플레이]로 획득 가능한 보라코인 갯수를 초과하였습니다.");
                }else{
                    bp.getPlayer().setKeyValue(501215, "today", bp.getPlayer().getKeyValue(501215, "today") + 10L + "");
                    bp.getPlayer().dropMessage(-8, "게임에서 승리하여 보라코인 10개를 획득하였습니다.");
                    bp.getPlayer().gainCabinetItemPlayer(4310029, 10, 1, "미니게임 보상 입니다. 보관 기간 내에 수령하지 않을 시 보관함에서 사라집니다.");
                }
                sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/victory"));
            } else {
                if(bp.getPlayer().getKeyValue(501215, "today") <= 0L){
                    bp.getPlayer().setKeyValue(501215, "today", "0");
                }
                if(bp.getPlayer().getKeyValue(501215, "today") >= 50){
                    bp.getPlayer().dropMessage(-8, "하루동안 [미니게임 플레이]로 획득 가능한 보라코인 갯수를 초과하였습니다.");
                }else{
                    bp.getPlayer().setKeyValue(501215, "today", bp.getPlayer().getKeyValue(501215, "today") + 5L + "");
                    bp.getPlayer().dropMessage(-8, "게임에서 패배하여 보라코인 5개를 획득하였습니다.");
                    bp.getPlayer().gainCabinetItemPlayer(4310029, 5, 1, "미니게임 보상 입니다. 보관 기간 내에 수령하지 않을 시 보관함에서 사라집니다.");
                }
                sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/gameover"));
            }
        }

        sendPacketToPlayers(SLFCGPacket.onShowText(winner.chr.getName() + "님의 승리! 게임이 종료됩니다."));

        if (getOneCardTimer() != null) {
            getOneCardTimer().cancel(false);
        }

        Timer.EventTimer.getInstance().schedule(() -> {
            for (OneCardPlayer bp : this.getPlayers()) {
                sendPacketToPlayers(SLFCGPacket.leaveResult(bp.position));
            }
        }, 5 * 1000);

        Timer.EventTimer.getInstance().schedule(() -> {
            for (OneCardPlayer bp : this.getPlayers()) {
                bp.chr.warp(ServerConstants.warpMap);
                bp.chr.setOneCardInstance(null);
            }

        }, 10 * 1000);
    }

    public void playerDead(OneCardPlayer player, boolean exit) {

        for (OneCard card : player.cards) {
            this.oneCardDeckInfo.add(card);
        }

        List<OneCardPlayer> players = new ArrayList<>();

        for (OneCardPlayer chr : this.getPlayers()) {
            if (chr.getPosition() != player.getPosition()) {
                players.add(chr);
            }
        }

        this.getPlayers().clear();

        for (OneCardPlayer chr : players) {
            this.getPlayers().add(chr);
        }

        player.chr.warp(ServerConstants.warpMap);
        player.chr.setOneCardInstance(null);

        if (this.getPlayers().size() == 1) {
            endGame(this.getPlayers().get(0), exit);
        } else if (getLastPlayer().position == player.position) {

            boolean check = false;
            for (int i = 0; i < this.getPlayers().size(); ++i) {
                if (this.getPlayers().get(i).position == (isbClockWiseTurn() ? (player.getPosition() + 1) : (player.getPosition() - 1))) {
                    this.setLastPlayer(this.getPlayers().get(i));
                    check = true;
                    break;
                }
            }

            if (!check) {
                check = true;
                if (isbClockWiseTurn()) {
                    this.setLastPlayer(this.getPlayers().get(0));
                } else {
                    this.setLastPlayer(this.getPlayers().get(this.getPlayers().size() - 1));
                }
            }

            if (check) {
                List<OneCard> possibleCards = new ArrayList<>();
                if (getLastCard().getColor() == 4) {
                    for (OneCard card : getLastPlayer().getCards()) {
                        if (getFire() == 0) {
                            possibleCards.add(card);
                        } else {
                            if (card.getType() == 6 || card.getType() == 7 || (card.getType() == 12 && card.getColor() == 0)) {
                                possibleCards.add(card);
                            }
                        }
                    }
                } else {
                    for (OneCard card : getLastPlayer().getCards()) {
                        if (card.getType() <= 5) {
                            if ((card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) && getFire() == 0) {
                                possibleCards.add(card);
                            }
                        } else if (card.getType() <= 7) {
                            if (getFire() == 0) {
                                if (card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) {
                                    possibleCards.add(card);
                                }
                            } else {
                                if (card.getType() == 6) {
                                    if (getLastCard().getType() == 6) {
                                        possibleCards.add(card);
                                    } else if (getLastCard().getType() == 7 && card.getColor() == getLastCard().getColor()) {
                                        possibleCards.add(card);
                                    }
                                } else {
                                    if (card.getColor() == getLastCard().getColor()) {
                                        if (getLastCard().getType() == 6 || getLastCard().getType() == 7) {
                                            possibleCards.add(card);
                                        }
                                    } else {
                                        if (getLastCard().getType() == 7) {
                                            possibleCards.add(card);
                                        }
                                    }
                                }
                            }
                        } else if (card.getType() <= 11) {
                            if ((card.getColor() == getLastCard().getColor() || card.getType() == getLastCard().getType()) && getFire() == 0) {
                                possibleCards.add(card);
                            }
                        } else {
                            switch (card.getColor()) {
                                case 0: // 오즈
                                    if (getLastCard().getColor() == card.getColor() || getFire() > 0) {
                                        possibleCards.add(card);
                                    }
                                    break;
                                case 1: // 미하일
                                    possibleCards.add(card);
                                    break;
                                case 2: // 호크아이
                                    if (getLastCard().getColor() == card.getColor() && getFire() == 0) {
                                        possibleCards.add(card);
                                    }
                                    break;
                                case 3: // 이리나
                                    if (getLastCard().getColor() == card.getColor() && getFire() == 0) {
                                        possibleCards.add(card);
                                    }
                                    break;
                                case 4: // 이카르트
                                    possibleCards.add(card);
                                    break;
                            }
                        }
                    }
                }
                sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(getLastPlayer(), possibleCards, (possibleCards.size() == 0 && getLastPlayer().getCards().size() == 16) || getLastPlayer().getCards().size() < 16, isbClockWiseTurn()));
                getLastPlayer().getPlayer().getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/myturn"));
                getLastPlayer().getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(getLastPlayer().getPlayer()));
            }
        }
    }

    public static void addQueue(MapleCharacter chr, int req) {
        if (!oneCardMatchingQueue.contains(chr)) {
            oneCardMatchingQueue.add(chr);
            if (oneCardMatchingQueue.size() == req) {
                oneCardMatchingInfo info = new oneCardMatchingInfo(oneCardMatchingQueue);

                List<MapleCharacter> chrs = new ArrayList<>();

                for (MapleCharacter player : info.players) {
                    oneCardMatchingQueue.remove(player);
                    player.warp(910044100);
                    chrs.add(player);
                }

                Timer.EventTimer.getInstance().schedule(() -> {

                    OneCardGame ocg = new OneCardGame(chrs);

                    for (MapleCharacter p : chrs) {
                        p.setOneCardInstance(ocg);
                    }

                    for (OneCardPlayer ocp : ocg.getPlayers()) {
                        ocp.chr.getClient().getSession().writeAndFlush(SLFCGPacket.OneCardGamePacket.CreateUI(ocp.chr, ocp.position, chrs));
                    }

                    Timer.EventTimer.getInstance().schedule(() -> {
                        ocg.StartGame(chr);
                    }, 3 * 1000);
                }, 5 * 1000);
            } else {
                World.Broadcast.broadcastSmega(CWvsContext.serverNotice(19, "", chr.getName() + "님이 원카드 대기열에 캐릭터를 등록했습니다. 남은 인원 : " + (req - oneCardMatchingQueue.size())));
            }
        }
    }

    public static void addQueueParty(MapleCharacter leader) {

        List<MapleCharacter> players = new ArrayList<>();
        for (MaplePartyCharacter p : leader.getParty().getMembers()) {
            int ch = World.Find.findChannel(p.getId());
            if (ch > 0) {
                MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(p.getId());
                if (!oneCardMatchingQueue.contains(chr)) {
                    oneCardMatchingQueue.remove(chr);
                }
                players.add(chr);
            }
        }

        oneCardMatchingInfo info = new oneCardMatchingInfo(players);

        for (MapleCharacter player : info.players) {
            oneCardMatchingQueue.remove(player);
            player.warp(910044100);
        }

        Timer.EventTimer.getInstance().schedule(() -> {

            OneCardGame ocg = new OneCardGame(players);

            for (MapleCharacter p : players) {
                p.setOneCardInstance(ocg);
            }

            for (OneCardPlayer ocp : ocg.getPlayers()) {
                ocp.chr.getClient().getSession().writeAndFlush(SLFCGPacket.OneCardGamePacket.CreateUI(ocp.chr, ocp.position, players));
            }

            Timer.EventTimer.getInstance().schedule(() -> {
                ocg.StartGame(players.get(0));
            }, 3 * 1000);
        }, 5 * 1000);
    }

    public void sendPacketToPlayers(byte[] packet) {
        for (OneCardPlayer player : this.Players) {
            player.getPlayer().getClient().getSession().writeAndFlush(packet);
        }
    }

    public Point getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Point lastPoint) {
        this.lastPoint = lastPoint;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public List<OneCard> getOneCardDeckInfo() {
        return oneCardDeckInfo;
    }

    public void setOneCardDeckInfo(List<OneCard> oneCardDeckInfo) {
        this.oneCardDeckInfo = oneCardDeckInfo;
    }

    public boolean isbClockWiseTurn() {
        return bClockWiseTurn;
    }

    public void setbClockWiseTurn(boolean bClockWiseTurn) {
        this.bClockWiseTurn = bClockWiseTurn;
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public OneCard getLastCard() {
        return lastCard;
    }

    public void setLastCard(OneCard lastCard) {
        this.lastCard = lastCard;
    }

    public List<OneCardPlayer> getPlayers() {
        return Players;
    }

    public void setPlayers(List<OneCardPlayer> players) {
        Players = players;
    }

    public ScheduledFuture<?> getOneCardTimer() {
        return oneCardTimer;
    }

    public void setOneCardTimer(ScheduledFuture<?> oneCardTimer) {
        this.oneCardTimer = oneCardTimer;
    }

    public void setLastPlayer(OneCardPlayer ocp) {
        this.lastPlayer = ocp;
    }

    public OneCardPlayer getLastPlayer() {
        return lastPlayer;
    }

    public OneCardPlayer setNextPlayer(OneCardPlayer lastPlayer, boolean isClock) {
        OneCardPlayer nextPlayer = null;
        if (isClock) {
            if (getPlayers().size() == 2) {
                for (OneCardPlayer pp : getPlayers()) {
                    if (pp.getPlayer().getId() != lastPlayer.getPlayer().getId()) {
                        nextPlayer = pp;
                    }
                }
            } else {
                for (int i = 0; i < getPlayers().size(); ++i) {
                    if (getPlayers().get(i).getPosition() == lastPlayer.getPosition()) {
                        if (i == getPlayers().size() - 1) {
                            nextPlayer = getPlayers().get(0);
                        } else {
                            nextPlayer = getPlayers().get(i + 1);
                        }
                    }
                }
            }
        } else {
            if (getPlayers().size() == 2) {
                for (OneCardPlayer pp : getPlayers()) {
                    if (pp.getPlayer().getId() != lastPlayer.getPlayer().getId()) {
                        nextPlayer = pp;
                    }
                }
            } else {
                for (int i = 0; i < getPlayers().size(); ++i) {
                    if (getPlayers().get(i).getPosition() == lastPlayer.getPosition()) {
                        if (i == 0) {
                            nextPlayer = getPlayers().get(getPlayers().size() - 1);
                        } else {
                            nextPlayer = getPlayers().get(i - 1);
                        }
                    }
                }
            }
        }
        return nextPlayer;
    }
}
