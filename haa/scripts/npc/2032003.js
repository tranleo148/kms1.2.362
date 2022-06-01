var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendNext("#fn나눔고딕 Extrabold#여기까지 운이 좋게.. 찾아 오셨군요..\r\n이런 험난한 #b폐광#k 에는 무슨 용무신가요?");
    } else if (status == 1) { 
        cm.sendNextPrevS("#fn나눔고딕 Extrabold##b혹시 이 근처에서 #fs14#이지병장#fs12# 님을 못보셨나요?#k",2);
    } else if (status == 2) { 
        cm.sendNextPrev("#fn나눔고딕 Extrabold#아! 그.. 멍청한.. 새..ㄲ..\r\n아니.. 그분이라면 아까 제 도움을 받아 먼저 귀환 하셨어요.");
    } else if (status == 3) { 
        cm.sendNextS("#fn나눔고딕 Extrabold##d(정말이지.. 괜한.. 고생을 했군..)#k\r\n\r\n아! 감사드립니다. 그럼 저도 이만 가보도록 하겠습니다.",2);
    } else if (status == 4) { 
        cm.sendNextPrev("#fn나눔고딕 Extrabold#잠깐만요.!! 이곳은 위험하니 제가 돌려 보내 드릴게요.!");
    } else if (status == 5) { 
        cm.sendNextS("#fn나눔고딕 Extrabold##d(얼른 복귀해서 소식을 전해드리자..)#k\r\n\r\n#b엇!! 도옴을 주셔서.. 정말 감사드립니다.!#k",2);
    } else if (status == 6) { 
        cm.sendYesNo("#fn나눔고딕 Extrabold#그럼.. 신의 가호가 당신을 지킬 것입니다.\r\n지금 바로! 밖으로 나가 보시겠어요.?");
    } else if (status == 7) { 
        cm.warp(100030301);
        cm.forceStartQuest(504);
    }
}
