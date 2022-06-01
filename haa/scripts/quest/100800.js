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
        if (status == 11) {
            qm.sendOkS("꽃이 지기 전에 꼭 가봤으면 좋겠는데... \r\n언제든 마음이 바뀌면 말해줘!\r\n\r\n\r\n#b#e※ 이벤트 기간\r\n - 2021년 6월 16일 23시 59분까지#n", 4, 9062527);
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
        qm.sendNextS("#b#e#h0##k#n님, 안녕하세요!", 4, 9062527);
    } else if (status == 1) {
        qm.sendNextPrevS("헤헤. #b#e들꽃의 정령#n#k이 매일 따듯한 햇살을 비춰준 덕분에 깨어날 수 있었어요!", 4, 9062527);
    } else if (status == 2) {
        qm.sendNextPrevS("얼른 #r#e봄기운#n#k을 느끼고 꽃들을 구경하러 바깥 세상에 가고 싶어요!", 4, 9062527);
    } else if (status == 3) {
        qm.sendNextPrevS("음...하지만 꽃은 #b#e블루밍 포레스트#n#k가 제일 많을 걸?", 2);
    } else if (status == 4) {
        qm.sendNextPrevS("아니!\r\n그럼 바깥에는 꽃이 하나도 없는 척박한 곳인가요...?", 4, 9062527);
    } else if (status == 5) {
        qm.sendNextPrevS("봄에는 메이플 월드 어디라도 아름다운 꽃이 피어야죠!\r\n당장 #r#e꽃씨를 뿌리러 떠나야겠어요!#n#k", 4, 9062527);
    } else if (status == 6) {
        qm.sendYesNoS("#b#e#h0##k#n님... 물론 도와주실 거죠? \r\n저를 도와주시면 #b#e#i4310310:# #t4310310##n#k를 드릴게요!", 4, 9062527);
    } else if (status == 7) {
        qm.getPlayer().changeSkillLevel(80003046, 1, 1);
        qm.getPlayer().setKeyValue(100803, "count", "0");
        qm.forceCompleteQuest();
        qm.getPlayer().dropMessage(5, "지금부터 레벨 범위 몬스터가 등장하는 곳에서 <플로라 블레싱> 스킬을 사용할 수 있습니다.");
        qm.sendNextS("헤헤. 도와주실 줄 알았어요!", 4, 9062527);
    } else if (status == 8) {
        qm.sendNextPrevS("여기 #b#e#i2633343# #t2633343##n#k의 힘을 모을 수 있는 #r#e꽃의 보석#n#k을 드릴게요!", 4, 9062527);
    } else if (status == 9) {
        qm.sendNextPrevS("#t2633343##n#k은 레벨 범위 몬스터를 처치하다 보면 가끔 얻을 수 있어요!", 4, 9062527);
    } else if (status == 10) {
        qm.sendNextPrevS("#t2633343##n#k을 획득하면 #b#e#i4310310:# #t4310310##n#k을 1개 얻을 수 있어요!", 4, 9062527);
    } else if (status == 11) {
        qm.sendNextPrevS("꽃의 보석에 #r#e햇살의 힘을 10번#n#k 채우면 #b#e플로라 블레싱#n#k이 \r\n발동해요! 그럼 제가 몬스터들을 처치하고 #r#e꽃씨도 펑!펑!", 4, 9062527);
    } else if (status == 12) {
        qm.sendNextPrevS("플로라 블레싱이 발동해서 꽃씨를 뿌리게 되면 \r\n#b#e#i4310310:# #t4310310#을 20개#n#k 더 드릴게요!", 4, 9062527);
    } else if (status == 13) {
        qm.sendNextPrevS("그럼 얼른 바깥 세상에 꽃을 피우러 가요!", 4, 9062527);
    } else if (status == 14) {
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}