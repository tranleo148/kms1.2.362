/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
초록 = "#fc0xFF0BC904#"

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 4) {
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
        qm.sendNextS("이번엔 #r칭호 승급#k 컨텐츠에 대해 알아 보겠네. 내가 선물로 준 #b#z3700900# 칭호#k를 버리거나 하진 않았겠지? 물론 #r실수로 버려도#k 나를 찾아온다면 다시 #b지급#k해주겠지만 말이야 크크..", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("#r칭호 승급#k이란 무엇이냐? 먼저 내가 자네에게 준 칭호는 #b세 가지 단계#k가 있다네.\r\n\r\n"+남색+"#i3700900# #z3700900#\r\n#i3700901# #z3700901#\r\n#i3700902# #z3700902##k\r\n", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("자네에게 준 #b#z3700900##k 칭호는 #e특정 아이템#n와 함께 #r#e칭호 승급#n#k이 가능하다네.\r\n승급은 #b한 단계씩#k 가능하고 승급에 성공하면 이전 단계보다 #r더 #e강력한 효과#n#k가 부여 된다네.", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendNextPrevS("자네는 아직 강화 아이템이 부족할테지? 크크. 그럴줄 알고 이 세심한 #r슈피겔만#k님이 쉬운 퀘스트로 준비했다네.", 0x04, 9401232);
    } else if (status == 4) {
        qm.sendYesNoS("크크. 누워서 #b코어 젬스톤#k 먹기 수준일거라네 자! #e[#b컨텐츠시스템#k -> #r칭호 승급#k]#n에 대해 설명을 듣고 오게나. 여기서 기다리고 있겠네.", 4, 9401232);
    } else if (status == 5) {
        qm.sendOkS("잊지말게 #e[#b컨텐츠시스템#k -> #r칭호 승급#k]#n에 대해 설명을 듣고 오는거라네!", 4, 9401232);
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
        if (status == 4) {
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
        qm.sendNextS("#e칭호 승급#n에 대해 자세한 설명을 들어보니 어떤가? #b블랙 스킬#k, #r치장 강화#k 만큼이나 중요한 성장 수단이라네, 꼭 잊지말고 승급을 하게나.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "승급에 대해선 궁금한점이 더 없겠지? 크크.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i4310012:# #b#t4310012:# 100개\r\n";
        말 += "#i2433928:# #b#t2433928:# 3개\r\n";
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1 && leftslot1 < 1) {
            qm.sendOkS("#r#h #! 소비창과 기타창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[3] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(4310012, 100);
        qm.gainItem(2433928, 3);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
