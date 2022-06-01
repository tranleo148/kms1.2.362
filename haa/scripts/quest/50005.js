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
        말 = "#h #!! 소식은 들었나?\r\n"
        말 += "표정을 보아하니 듣지 못한게로군. 최근에 #r에르다의 빛#k으로 #b정령의 숲 아르카나#k에 특별한 변화가 찾아왔다고 한다네!"
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("#b아르카나 깊은 숲속#k에 마치 봄이 온 것처럼 #r아름다운 꽃#k이 잔뜩 피어났다고 하더군.", 0x04, 9401232);
    } else if (status == 2) {
        말 = "그래서 #b아르카나#k의 정령들이 #e꽃이 피는 숲#n #r<블루밍 포레스트>#k에 사람들을 초대하고 있다고 하는거 같군!"
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 3) {
        말 = "아쉽지만, 이 슈피겔만 님은 #b블랙 페스티벌#k의 새로운 용사들을 맞이해야 해서 갈 수가 없네, 크크. 자네가 나 대신 정령들의 부름에 응해주게!"
        qm.sendYesNoS(말, 4, 9401232);
    } else if (status == 4) {
        qm.sendOkS("좌측 이벤트 알림이를 통해 #b블루밍 포레스트 선행 퀘스트#k를 완료하자!", 2);
        if (qm.getPlayer().getQuestStatus(100790) == 2) {
            qm.getPlayer().setKeyValue(50005, "1", "1");
            qm.getPlayer().dropMessage(5, "<블루밍 포레스트> 선행 퀘스트를 완료했기 때문에 성장 퀘스트가 클리어 됩니다.")
        }
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
            qm.sendOkS("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        qm.sendNextS("#r블루밍 포레스트#k는 어떻던가? 들은 바로는 #b따스한 햇살이 쏟아지는 아주 아름다운 숲#k이라고 하더군.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "나도 꼭 한번 가보고 싶다네...\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i4310310:# #b#t4310310:# 1000개#k\r\n";
        qm.sendOkS(말, 0x04, 9401232);
    } else if (status == 2) {
        qm.getPlayer().setKeyValue(100794, "point", (qm.getPlayer().getKeyValue(100794, "point") + 1000) + "");
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[5] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
