importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 7) {
            qm.sendOkS("서운해...\r\n목마른 꽃씨에 비를 내려줘야 하는데...\r\n\r\n\r\n#b#e※ 이벤트 기간\r\n - 2021년 6월 16일 23시 59분까지#n", 4, 9062528);
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
        qm.sendNextS("#b#e#h0##k#n! 반가워!", 4, 9062528);
    } else if (status == 1) {
        qm.sendNextPrevS("히히. 매일 비춰준 #r#e따듯한 햇살#n#k 덕분에 깨어날 수 있었어!", 4, 9062528);
    } else if (status == 2) {
        qm.sendNextPrevS("#r#e하나#n#k랑 메이플 월드 곳곳에 씨앗을 뿌려줬다면서?", 4, 9062528);
    } else if (status == 3) {
        qm.sendNextPrevS("하지만 씨앗은 그냥 뿌리기만 하면 안 돼!", 4, 9062528);
    } else if (status == 4) {
        qm.sendNextPrevS("하지만... 지금까지 열심히 꽃씨를 뿌리기만 했는걸?", 2);
    } else if (status == 5) {
        qm.sendNextPrevS("지금 봄바람을 타고 #r#e목마른 씨앗#n#k들의 목소리가 들려!", 4, 9062528);
    } else if (status == 6) {
        qm.sendNextPrevS("목마른 씨앗들이 나를 기다려!\r\n당장 #b#e비를 내려주러 떠나야겠어!#n#k", 4, 9062528);
    } else if (status == 7) {
        qm.sendYesNoS("#b#e#h0##k#n!\r\n이제 나도 모험에 데리고 가줘!", 4, 9062528);
    } else if (status == 8) {
        qm.getPlayer().changeSkillLevel(80003046, 2, 2);
        qm.forceCompleteQuest();
        qm.getPlayer().dropMessage(5, "<플로라 블레싱> 스킬이 2레벨이 되었습니다.");
        qm.sendNextS("히히. 도와주실 줄 알았어!", 4, 9062528);
    } else if (status == 9) {
        qm.sendNextPrevS("꽃의 보석에 #r#e햇살의 힘을 10번#n#k 채우면 #b#e플로라 블레싱#n#k이 \r\n발동하는 건 알지?", 4, 9062528);
    } else if (status == 10) {
        qm.sendNextPrevS("플로라 블레싱이 발동하면 \r\n\r\n하나가 #r#e꽃씨를 펑!펑!#n#k\r\n그 다음 내가 #b#e비를 쏴아아!", 4, 9062528);
    } else if (status == 11) {
        qm.sendNextPrevS("혹시 운이 좋으면 #r#e꽃비#n#k가 내릴 거야! 꽃비가 내리면\r\n#b#e#i4310310:# #t4310310#을 10개#n#k 더 줄게!", 4, 9062528);
    } else if (status == 12) {
        qm.sendNextPrevS("그럼 얼른 목마른 씨앗에 비를 내려주러 가자!", 4, 9062528);
    } else if (status == 13) {
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}