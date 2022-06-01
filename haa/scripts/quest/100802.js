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
        if (status == 6) {
            qm.sendOkS("서운하군...\r\n따스한 햇살을 비춰줘야 하는데 말야...\r\n\r\n\r\n#b#e※ 이벤트 기간\r\n - 2021년 6월 16일 23시 59분까지#n", 4, 9062529);
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
        qm.sendNextS("흐흐. #b#e#h0##k#n! 내가 좀 늦었지?", 4, 9062529);
    } else if (status == 1) {
        qm.sendNextPrevS("#r#e따듯한 햇살#n#k을 비춰주니 나도 모르게 뒹굴뒹굴했지 뭐야?", 4, 9062529);
    } else if (status == 2) {
        qm.sendNextPrevS("하나, 두나랑 함께 #r#e씨앗#n#k도 뿌리고 #b#e물#n#k도 줬다면서?", 4, 9062529);
    } else if (status == 3) {
        qm.sendNextPrevS("하지만 꽃이 피기 위해 가장 중요한 것이 빠졌는걸?!", 4, 9062529);
    } else if (status == 4) {
        qm.sendNextPrevS("어쩐지 꽃이 안 피더라...", 2);
    } else if (status == 5) {
        qm.sendNextPrevS("바로 따스한 햇살이지!\r\n당장 #r#e따스한 햇살을 비춰주러 가자!#n#k", 4, 9062529);
    } else if (status == 6) {
        qm.sendYesNoS("흐흐. #b#e#h0##k#n!\r\n특별히 내가 모험에 합류해 주지!", 4, 9062529);
    } else if (status == 7) {
        qm.getPlayer().changeSkillLevel(80003046, 3, 3);
        qm.forceCompleteQuest();
        qm.getPlayer().dropMessage(5, "<플로라 블레싱> 스킬이 3레벨이 되었습니다.");
        qm.sendNextS("흐흐. 영광으로 알라고!", 4, 9062529);
    } else if (status == 8) {
        qm.sendNextPrevS("꽃의 보석에 #r#e햇살의 힘을 10번#n#k 채우면 #b#e플로라 블레싱#n#k이 \r\n발동하는 건 알지?", 4, 9062529);
    } else if (status == 9) {
        qm.sendNextPrevS("플로라 블레싱이 발동하면 \r\n\r\n하나가 #r#e꽃씨를 펑!펑!#n#k\r\n두나가 #b#e비를 쏴아아!#n#k\r\n마지막! 이 몸이 #e햇살#n을 #e쨍쨍!#n", 4, 9062529);
    } else if (status == 10) {
        qm.sendNextPrevS("그럼 #b#e아름다운 꽃#n#k이 필거야!\r\n흐흐. #r#e전투를 도와주는#n#k 정말 멋진 친구들이지.", 4, 9062529);
    } else if (status == 11) {
        qm.sendNextPrevS("혹시 운이 좋으면 #r#e화려한 꽃#n#k이 필거야!\r\n그럼 #b#e#i4310310:# #t4310310#을 20개#n#k 더 줄게!", 4, 9062529);
    } else if (status == 12) {
        qm.sendNextPrevS("그럼 얼른 따스한 햇살을 비춰주러 가자!", 4, 9062529);
    } else if (status == 12) {
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}