


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	히어로 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062555

	엔피시 이름 : 예티

	엔피시가 있는 맵 : 메이플 LIVE : LIVE 스튜디오 (993194000)

	엔피시 설명 : 메소샵


*/

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
        if (cm.getClient().getCustomData(501469, "MshopTuto") != null) {
            cm.openShop(25);
            cm.dispose();
        } else {
            cm.sendNextS("예티 미안하다.", 4);
        }
    } else if (status == 1) {
        cm.sendNextPrevS("대장이 물건은 아무한테나 파는건 안된다고 했다.", 4);
    } else if (status == 2) {
        cm.sendNextPrevS("적어도 #b#e골드 크리에이터#n#k는 되어야 우리 물건 보여줄 수 있다고 했다. \r\n\r\n #r※ 메소샵은 #e골드 크리에이터 이상#n부터 이용할 수 있습니다.", 4);
    } else if (status == 3) {
        cm.sendNextPrevS("예티 멋지다.", 4);
    } else if (status == 4) {
        cm.sendNextPrevS("멋진 물건 많다. 예티처럼.", 4);
    } else if (status == 5) {
        cm.sendNextPrevS("하지만 대장이 말했다. 할인은 절대 안 된다고...", 4);
    } else if (status == 6) {
        //메소샵오픈
        cm.getClient().setCustomData(501469, "MshopTuto", "1");
        cm.openShop(25);
        cm.dispose();
    }
}
