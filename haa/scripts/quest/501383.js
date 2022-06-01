importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.scripting);

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 2) {
            qm.sendOkS("꽃이 잘 자라게 도와주지...\r\n나도 혼자는 힘들어...\r\n\r\n#b#e※ 이벤트 기간\r\n - 2021년 6월 16일 23시 59분까지#n", 4, 9062516);
            qm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        if (qm.getClient().getCustomData(247, "T") != null) {
            qm.forceCompleteQuest(501383);
            qm.dispose();
            NPCScriptManager.getInstance().startQuest(cm.getClient(), 2007, 100804);
        } else {
            qm.sendNextS("(조화의 정령이 뭔가 열심히 하고 있다.)", 2);
        }
    } else if (status == 1) {
        qm.sendNextPrevS("...#b#e잡초#n#k 제거해야 해. 그래야 꽃이 잘 자랄 거야...", 4, 9062516);
    } else if (status == 2) {
        qm.sendYesNoS("#r#e블루밍 포레스트#n#k의 꽃들이 잘 자랐으면 좋겠어.\r\n혹시 #b#e잡초 제거#n#k에 관심 있어...?", 4, 9062516);
    } else if (status == 3) {
        qm.forceCompleteQuest(501383);
        var kc = new Packages.constants.KoreaCalendar();
        var setdate = kc.getYears() + kc.getMonths() + kc.getDays() + kc.getHours() + kc.getMins() + kc.getSecs();
        qm.getClient().setCustomData(247, "reward", setdate+"");
        qm.getClient().setCustomData(247, "T", setdate);
        qm.getClient().setCustomData(247, "lastTime", new Date().getTime()+"");
        qm.getClient().send(SLFCGPacket.ExpPocket(qm.getPlayer(), GameConstants.ExpPocket(qm.getPlayer().getLevel()), 10));
        qm.getClient().send(CField.UIPacket.openUI(1207));
        qm.sendNextS("#r#e블루밍 포레스트#n#k의 꽃이 자라려면 #b#e잡초#n#k를 제거해 줘야 해.", 4, 9062516);
    } else if (status == 4) {
        qm.sendNextPrevS("#b#e잡초 제거#n#k는 아주 간단해.\r\n#r#e메이플 유니온#n#k에 소속된 캐릭터가 알아서 제거해 줄 거야!\r\n\r\n\r\n#r ※ 잡초 제거는 자동으로 진행됩니다.", 4, 9062516);
    } else if (status == 5) {
        qm.sendNextPrevS("제거한 잡초만큼 내 #b#e블루밍 코인#n#k을 나눠줄게. \r\n잡초는 #e#r1시간마다 1개#n#k씩 뽑을 수 있어 또한 블루밍 코인과 더불어서 경험치를 적립해 줄게.\r\n\r\n\r\n#r ※ 유니온 캐릭터 1명이 1시간에 1개의 잡초를 제거합니다.", 4, 9062516);
    } else if (status == 6) {
        qm.sendNextPrevS("메이플 유니온에 소속된 캐릭터를 많이 데려와서 잡초 제거를 빠르게 하면 물론 좋겠지만... \r\n\r\n#e그럼 너무 힘들겠지?", 4, 9062516);
    } else if (status == 7) {
        qm.sendNextS("날 도와줄 수 있는 만큼만 부탁할게! \r\n#b#e유니온 레벨#n#k이 높다면 조금 마음 편히 부탁할 수 있겠어.\r\n\r\n\r\n#r ※ 유니온 레벨이 높을수록 잡초를 제거하는 캐릭터 수가 \r\n   증가합니다.", 4, 9062516);
    } else if (status == 8) {
        qm.sendNextPrevS("너무 많이 부탁하면 미안하니까... \r\n#b#e유니온 레벨#n#k에 따라 이 정도...?\r\n\r\n\r\n#r #e[잡초 제거 캐릭터 수]#n \r\n - 유니온 레벨 2000 미만 : 1명\r\n - 유니온 레벨 3500 미만 : 2명\r\n - 유니온 레벨 5000 미만 : 3명\r\n - 유니온 레벨 6500 미만 : 4명\r\n - 유니온 레벨 8000 미만 : 5명\r\n - 유니온 레벨 8000 이상 : 6명\r\n\r\n", 4, 9062516);
    } else if (status == 9) {
        qm.sendNextPrevS("그리고... 잡초 바구니는 크지 않으니까 가끔 들려서 비워줘!\r\n\r\n\r\n#r ※ 잡초 제거는 최대 #e24시간까지#n만 가능하며, 중간에 \r\n   보상을 수령하는 경우 0부터 다시 시작합니다.", 4, 9062516);
    } else if (status == 10) {
        qm.sendPrevS("도와줘서 고마워! \r\n꽃들이 모두 행복했으면 좋겠다.", 4, 9062516);
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}