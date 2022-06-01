importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);

status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        qm.sendNextNoESC("도움을 요청했더니, 말랑이, 자네가 오다니!\r\n자네라면 정말 큰 도움이 될걸세.\r\n저쪽에 고맙다고 말이라도 해야겠군.")
    } else if (status == 1) {
        qm.sendNextNoESC("자, 그럼 일 얘기를 해볼까.\r\n이 근처의 #b#o8250013##k들이 갑자기 너무 많아졌어.\r\n도저히 정찰을 할 수가 없다네.\r\n#b#o8250013# 300마리#k를 처치해 주게.\r\n다 처치한 후엔 나에게 알려주면 되지, 잘 부탁하네.");
        qm.dispose();
    }
}


function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        qm.sendNext("이제야 조금 정찰을 할 수 있겠군.\r\n정말 하기 힘든 일인데...\r\n앞으로도 잘 부탁하네, 말랑이.");
    } else if (status == 1) {
        qm.forceCompleteQuest();
        str = qm.getPlayer().getV("ArcQuest6");
        ab = str.split(",");
        var clear = 0;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 2) {
                clear++
            }
        }
        qm.getPlayer().removeKeyValue(39106);
        qm.getPlayer().setKeyValue(39100, "FC", clear + "");
        qm.getClient().send(CField.UIPacket.detailShowInfo("경험치 50,000,000 획득!", 3, 20, 20));
        qm.gainExp(50000000);
        qm.dispose();
    }
}