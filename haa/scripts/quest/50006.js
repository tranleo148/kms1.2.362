/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
주황 = "#fc0xFFEDA900#"

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 3) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "이봐 #h #! #b<블루밍 포레스트>#k에 조그만 정령이 자네를 찾고있더군.\r\n"
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("내가 듣기론 #b<블루밍 포레스트>에서 이곳저곳 돌아다니다 #b엄청난 장소#k를 발견했다고 했다던데...", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("그 장소를 #b조그만 정령#k이 친구들끼리 경쟁할 수 있는 재미난 장소로 만들었다고 하던데.. 이름이.. #b블루밍 레이스#k 라고 하던가..? 여튼, 자네도 한번 레이스에 도전해보게!", 0x04, 9401232);
    } else if (status == 3) {
        말 = "#b<블루밍 레이스>#k는 #b오전 10시#k부터 #b자정 전#k까지 #r매시 15분, 45분#k에 머리 위 #r초대장#k을 통해 입장할 수 있다더군!"
        qm.sendYesNoS(말, 4, 9401232);
    } else if (status == 4) {
        qm.sendOkS(" 머리 위 #r초대장#k을 통해 입장 후 #b블루밍 레이스#k를 1회 완주해보자!", 2);
        qm.forceStartQuest();
        qm.dispose();
    }
}

status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("조그만 정령의 #b<블루밍 레이스>#k는 어떤가? 나도 한번 참여해보고 싶구만. 크크크.\r\n\r\n그 외에도 #b<블루밍 포레스트>#k에서 #r다양한 이벤트#k를 하고 있다고 하니 둘러보게나.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "다음엔 나랑 한번 경쟁 해보는게 어떤가! 자네의 실력 기대하고 있겠네 크크.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i4310310:# #b#t4310310:# 1,000개\r\n";
        말 += "#i4310012:# #b#t4310012:# 300개#k\r\n";
        qm.sendOkS(말, 0x04, 9401232);
    } else if (status == 2) {
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[6] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.getPlayer().setKeyValue(100794, "point", (qm.getPlayer().getKeyValue(100794, "point") + 1000) + "");
        qm.gainItem(4310012, 300)
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
