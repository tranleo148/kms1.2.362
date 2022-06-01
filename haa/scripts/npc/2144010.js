


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    은월 에 의해 만들어 졌습니다.

    엔피시아이디 : 2144010

    엔피시 이름 : 아카이럼

    엔피시가 있는 맵 : 차원의 틈 : 아카이럼의 제단 (272020200)

    엔피시 설명 : MISSINGNO


*/
importPackage(Packages.constants);
importPackage(Packages.server.life);
importPackage(java.awt);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (GameConstants.isZero(cm.getPlayer().getJob())) {
            cm.sendAcceptDeclineNoESC("오호, 이게 누구신가. 여신의 후계자, 새로운 초월자로군. 윌의 손아귀에게 놀아나는 꼴이 제법 우습더니 이젠 나를 찾아온 모양이군. 같은 편이 되는 건 나중 일이고, 일단 그 실력부터 보자.");
        } else {
            cm.sendAcceptDeclineNoESC("내 오랜 계획을 물거품으로 만든 녀석들이 이렇게 제 발로 찾아와주니 정말 기쁘기 그지 없군. 그 댓가로 세상에서 제일 고통스러운 죽음을 선사해주마.");
        }
    } else if (status == 1) {
        mobid = cm.getPlayer().getMapId() == 272020210 ? 8860007 : 8860010;
        mob = MapleLifeFactory.getMonster(mobid);
        cm.removeNpc(2144010);
        cm.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, new Point(320, -181));
        cm.getPlayer().getMap().killMonster(mob);
        cm.dispose();
    }
}
