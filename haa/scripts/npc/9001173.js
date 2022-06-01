/*

	메이플스토리 14주년 와글와글 하우스 이벤트
	
*/

var status = -1;
var sel = 0;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        status--;
    }
    switch (status) {
        case 0:
            var t = "우리의 약속을 지키도록 해. 우리의 새집을 만드는 거야.\r\n\r\n";
            t += "#L0##b와글와글 하우스에 참여하고 싶어요.\r\n";
            t += "#L1#와글와글 하우스에 대해 알고 싶어요.\r\n";
            t += "#L2#우주 최고의 건축가 아이템을 받고 싶어요.#l";
            cm.sendSimple(t);
            break;
        case 1:
            sel = selection;
            if (selection == 0) {
                cm.sendYesNo("지금바로 #b#e와글와글 하우스#n#k를 시작할거야?\r\n어쩌면 이미 와글와글 하우스인지도 모르지만.\r\n\r\n#r#e(게임 중에는 해상도가 1024x768로 변경 됩니다.)#n#k");
            } else if (selection == 1) {
                cm.sendNext("#r#e와글와글 하우스#n#k는 매우 짓기 쉽지. 하지만 섬세함도 필요한 작업이야.");
            } else if (selection == 2) {
                cm.sendOk("우주최고 건축가 아이템 보상은 준비중이야!");
                cm.dispose();
            }
            break;
        case 2:
            if (sel == 1) {
                cm.sendNextPrev("한 층 한 층이 옆에서 나타날 거야. #r#espace#n#k키를 눌러서 멈출 수 있는데. 바로 이지점에 섬세함이 필요하지.");
            } else if (sel == 0) {
				if (cm.getPlayer().getLevel() < 160) {
            cm.sendOk("#e#h ##n님의 레벨이 #e#b160 이상#k#n이 되지 않으므로 와글와글 하우스에 입장 할 수 없습니다.");
            cm.dispose();
				}else{
                cm.warp(993017200);
                cm.StartBlockGame();
                cm.dispose();
				}
            }
            break;
        case 3:
            cm.sendNextPrev("일정 범위 내에 층이 멈추면 반짝이는 이펙트와 함께 많은 양의 노바코인을 획득할 수 있지.\r\n\r\n");
            break;
        case 4:
            cm.sendNextPrev("뭐 범위 내에 층을 멈추지 못했다고 하더라도 무조건 실패하는 건 아니야. 다만 쌓는 층의 폭이 줄어들 뿐이지.");
            break;
        case 5:
            cm.sendNextPrev("폭이 줄어들수록 높이 쌓기 더 어렵다는 건 말 안 해도 알겠지? 그래서 섬세함이 필요한 작업이라는 거야.");
            break;
        case 6:
            cm.sendNextPrev("10층마다 기념의 깃발이 층 양 옆에 생겨나니 몇 층까지 쌓았는지 궁금하면 고개를 들어 깃발을 바라봐.");
            break;
        case 7:
            cm.sendPrev("열심히 집을 쌓아 올리도록 해! 난 여기서 열심히 지켜보고 있겠어.");
            cm.dispose();
            return;
            break;
    }
}
