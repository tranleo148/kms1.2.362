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
            qm.sendOkS("꽃이 지기 전에 꼭 가봤으면 좋겠는데... \r\n언제든 마음이 바뀌면 말해줘!\r\n\r\n\r\n#b#e※ 이벤트 기간\r\n - 2021년 6월 16일 23시 59분까지#n", 4, 9062508);
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
        qm.sendNextS("#b#h0##k님! 안녕하세여!\r\n선뜻 도와주겠다고 해주셔서 정말 감사해여!", 4, 9062508);
    } else if (status == 1) {
        qm.sendNextPrevS("저랑 같이 #r#e잠들어 있는 꽃 친구#n#k들을 깨우고 ..이 행복한 봄기운을 나누는 거예여!", 4, 9062508);
    } else if (status == 2) {
        qm.sendNextPrevS("이름하여!\r\n\r\n#fs16##r#e<블루밍 플라워> 프로젝트!", 4, 9062508);
    } else if (status == 3) {
        qm.sendNextPrevS("잠들어 있는 친구들을 깨우는 방법은 아주 간단해여!\r\n바로 #r#e따듯한 햇살#n#k을 주는 거예여!\r\n\r\n꽃이 피어나면 제가 #b#e특별한 선물#n#k을 드릴게여!\r\n\r\n\r\n#r ※ 블루밍 플라워를 진행하여 꽃이 피어나면 보상을 받을 \r\n   수 있습니다.", 4, 9062508);
    } else if (status == 4) {
        qm.sendNextPrevS("햇살은 어떻게 주냐고여?\r\n#b하루에 한 번#k, 일일 블루밍 코인 제한량을 모두 모으고 나서 저를 찾아와 #b#e<햇살 주기> 버튼#n#k을 눌러주세여!\r\n\r\n\r\n#r ※ 블루밍 플라워는 일일 블루밍 코인 제한량을 달성한 \r\n   캐릭터만 진행할 수 있습니다.", 4, 9062508);
    } else if (status == 5) {
        qm.sendNextPrevS("그렇지만 기분 좋은 꿈을 꾸고 있을 때 억지로 깨우면 친구들이 싫어할 수도 있겠져?\r\n\r\n잠든 꽃들을 깨워 활짝 피우는 건 #e#r하루에 한 번#k#n이면 충분할 것 같아여~!\r\n\r\n\r\n#r ※ 블루밍 플라워는 월드당 하루에 한 번만 진행할 수 \r\n   있습니다.", 4, 9062508);
    } else if (status == 6) {
        qm.sendNextPrevS("참! 아직 햇살을 받을 준비가 되지 않은 꽃들도 있어여!\r\n#b#e매주 3개의 꽃봉오리#n#k가 햇빛을 받고 싶어서 나뭇잎 사이로 얼굴을 내밀 거예여!\r\n\r\n\r\n#r ※ 블루밍 플라워는 매주 목요일 오전 10시에 3개씩 \r\n    해금됩니다.", 4, 9062508);
    } else if (status == 7) {
        qm.sendNextPrevS("그리고 #r#e세 번째 꽃#k#n이 필 때마다 #e#b더 좋은 보상#n#k과\r\n#e#b더 많은 스킬 포인트#n#k를 드릴게여!\r\n\r\n\r\n#r ※ 블루밍 플라워 진행 시 1/1/3개의 블루밍 스킬 포인트를 \r\n   받을 수 있습니다.", 4, 9062508);
    } else if (status == 8) {
        qm.sendNextPrevS("#b#e<블루밍 스킬>#n#k은 블루밍 축복을 받은 분들만\r\n사용하실 수 있는 #b특별한 능력치#k 스킬이에여!\r\n블루밍 스킬 포인트로 능력치를 올려보세여!\r\n\r\n\r\n#r ※ 획득한 블루밍 스킬 포인트로 능력치를 올릴 수 있습니다.", 4, 9062508);
    } else if (status == 9) {
        qm.sendNextPrevS("지금부터 화면 왼쪽 #b별 알람이 아이콘#k 또는 #b저#k를 통해 언제든\r\n#b#e<블루밍 플라워>#k#n 진행 상태를 확인하실 수 있어여!", 4, 9062508);
    } else if (status == 10) {
        qm.sendNextPrevS("요 며칠간 이곳에 비추는 햇살을 주섬주섬 모아 뿌려두었던\r\n친구가 #r#e곧 깨어날 것 같아#k#n서 신나네여!!\r\n\r\n\r\n#r※ 블루밍 포레스트 중앙에 있는 꽃무리에서 '붉은 꽃'에게\r\n말을 걸어 깨워주세요. ", 4, 9062508);
    } else if (status == 11) {
        qm.sendNextPrevS("흥흥~ 따스한 햇살~ 다른 꽃 정령 친구들도 어서 깨어나\r\n함께 부드러운 봄바람을 만끽하면 좋겠어여~!!! ", 4, 9062508);
    } else if (status == 12) {
        qm.getPlayer().setKeyValue(501387, "flower", "1");
        qm.getClient().setKeyValue("BloomingTuto", "2");
        qm.forceCompleteQuest();
        qm.getClient().send(CField.UIPacket.openUI(1296));
		qm.getClient().send(CField.setMapOBJ("all", 0, 0, 0));
		qm.getClient().send(CField.setMapOBJ("0", 1, 0, 0));//꽃 활성화
        qm.dispose();
    }
}

function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}