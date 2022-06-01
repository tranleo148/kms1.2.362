


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    캐논슈터 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062507

    엔피시 이름 : 돌의 정령

    엔피시가 있는 맵 : 블루밍 포레스트 : 돌의 정령을 도와달람 입장 (993192700)

    엔피시 설명 : 돌의 정령을 도와달람! 


*/
importPackage(Packages.constants);
importPackage(Packages.tools.packet);
var status = -1;
var coin = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.sendOkS("언제든 잡초 제거 보상을 받고 싶으면 다시 말해 줘.", 4, 9062516)
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getKeyValue(100794, "today") < 3000 && !cm.getPlayer().isGM()) {
            cm.sendOkS("#b<유니온 가드닝>#k은 오늘의 블루밍 코인 3000개를 전부 모은 친구들만 보상을 받아갈수 있어.\r\n\r\n#r블루밍 코인 3000개를 모은 후에 다시 찾아와 줘#k.\r\n(오늘 모은 블루밍 코인: #r#e" + cm.getPlayer().getKeyValue(100794, "today") + "#n#k개)", 4, 9062516);
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getLevel() < 200) {
            cm.sendOkS("#b<유니온 가드닝>#k은 #e200 레벨#n 이상인 친구들만 이용이 가능해. 알아두면 좋을거야.", 4, 9062516);
            cm.dispose();
            return;
        }
        level = cm.getPlayer().getAllUnion();
        union = level >= 8000 ? 6 : level < 8000 ? 5 : level < 6500 ? 4 : level < 5000 ? 3 : level < 3500 ? 2 : 1;
        union *= 2;
        coin = Math.round(cm.ExpPocket(1) / 3600 * union);
        if ((cm.ExpPocket(1) / 3600) <= 1) {
            cm.sendOkS("아직 제거한 잡초가 없는걸...\r\n\r\n#r※ 최소 #e1시간#n 이상은 적립해야 보상을 받을 수 있습니다.", 4, 9062516);
            cm.dispose();
            return;
        }
        cm.sendYesNoS("잡초 지금까지 #e#r" + coin + "개#n#k 제거했어.\r\n지금 보상을 받을래?\r\n\r\n#e[잡초 제거 보상]#n\r\n#i4310310:# #b#z4310310:# #e" + coin + "개#n#k\r\n\r\n #r적립된 경험치 #e" + cm.ExpPocket(2) + " 증가#n#k\r\n\r\n#r※ 실제 표기된 경험치 량과 적립된 경험치 량이 틀릴 수 있습니다.", 4, 9062516);
    } else if (status == 1) {
        cm.sendYesNoS("잡초 제거 보상은 #r#e#fs16#월드 당 1번#n#k#fs12#만 받을 수 있어. \r\n#b#e정말 이 캐릭터로 보상을 받을래?#n#k\r\n\r\n\r\n#e[잡초 제거 보상]#n\r\n#i4310310:# #b#z4310310:# #e" + coin + "개#n#k\r\n\r\n #r적립된 경험치 #e" + cm.ExpPocket(2) + " 증가#n#k", 4, 9062516);
    } else if (status == 2) {
        var exp = cm.ExpPocket(2);
        var kc = new Packages.constants.KoreaCalendar();
        var setdate = kc.getYears() + kc.getMonths() + kc.getDays() + kc.getHours() + kc.getMins() + kc.getSecs();
        cm.getPlayer().gainExp(exp, true, true, true);
        cm.getClient().setCustomData(247, "reward", "0");
        cm.getClient().setCustomData(247, "T", setdate+"");
        cm.getClient().setCustomData(247, "lastTime", new Date().getTime() + "");
        if (coin > 0) {
            cm.getPlayer().setKeyValue(100794, "point", (cm.getPlayer().getKeyValue(100794, "point") + coin) + "");
        }
        cm.getClient().send(SLFCGPacket.ExpPocket(cm.getPlayer(), GameConstants.ExpPocket(cm.getPlayer().getLevel()), 10));
        cm.getClient().send(CWvsContext.InfoPacket.updateClientInfoQuest(247, "T=" + cm.getClient().getCustomData(247, "T")));
        cm.getClient().send(CWvsContext.enableActions(cm.getPlayer()));
        cm.sendOkS("도와줘서 고마워!\r\n꽃들이 모두 행복했으면 좋겠다.\r\n\r\n\r\n#e[잡초 제거 보상]#n\r\n#i4310310:# #b#z4310310:# #e" + coin + "개#n#k\r\n\r\n #r적립된 경험치 #e" + exp + "", 4, 9062516);
        cm.dispose();
    }
}
