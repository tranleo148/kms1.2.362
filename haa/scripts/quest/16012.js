
var status = -1;

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "안녕하세요 용사님!\r\n"
        talk += "#b메이플 유니온#k은 잘 꾸려 나가고 계신가요?\r\n"
        talk += "용사님과 #b메이플 유니온#k이 함께 성장하는 모습을 보니 이 일을 시작하길 참 잘했다는 생각이 들어요."
        qm.sendNextS(talk, 0x04, 9010106);
    } else if (status == 1) {
        talk = "용사님의 발전을 도와드리기 위해 #r임무#k를 하나 준비했어요.\r\n\r\n"
        talk += "#r거대 드래곤#k을 호위하는 #b미니 드래곤#k을 퇴치하다보면 아주\r\n가~끔 희귀한 #r골든 와이번#k이 나타난다고 해요. 그 녀석들을\r\n#b20 마리#k 퇴치하고 오시면 #i4310229# #b#z4310229# 150개#k를 보상으로 드릴게요.\r\n임무를 수행 하시겠어요?\r\n\r\n#r※계정당 1회만 수행 가능한 퀘스트 입니다.#k"
        qm.sendYesNoS(talk, 0x04, 9010106);
    } else if (status == 2) {
        talk = "역시 도전을 즐기실 줄 아시는 분이시군요!\r\n"
        talk += "#r골든 와이번#k은 #r용의 전장터#k에서 #b유니온 레이드#k를 통해 사냥 하실 수 있어요.\r\n그리고 일일 퀘스트를 완료하시려면 마을에 있는 저를 직접 찾아오셔야 해요.\r\n그럼 건승을 빌게요!"
        qm.sendOkS(talk, 0x04, 9010106);
        qm.forceStartQuest();
        qm.dispose();
    }
}

var status2 = -1;

function end(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status2--;
    }
    if (mode == 1) {
        status2++;
    }

    if (status2 == 0) {
        qm.sendNextS("임무를 무사히 마치고 돌아오셨네요. 말씀드린대로 #i4310229# #b#z4310229# 150개#k를 보상으로 드릴게요 ", 0x04, 9010106);
    } else if (status2 == 1) {
        qm.getPlayer().AddAllUnionCoin(150);
        qm.getClient().setKeyValue("UnionQuest2", "1");
        qm.forceCompleteQuest();
        qm.dispose();
    }
}