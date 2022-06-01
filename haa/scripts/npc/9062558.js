


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	히어로 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062558

	엔피시 이름 : 블랙빈

	엔피시가 있는 맵 : 메이플 LIVE : LIVE 스튜디오 (993194000)

	엔피시 설명 : 검은콩샵


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
        if (cm.getClient().getCustomData(501469, "shopTuto2") != null) {
            cm.openShop(29);
            cm.dispose();
        } else{
            cm.sendNextS("흠... 보아하니 #r#e<메이플 LIVE>#n#k가 뭔지도 모르는\r\n#b#e초보  크리에이터#n#k군?", 4);
        }
    } else if (status == 1) {
        cm.sendNextPrevS("흥!\r\n이 블랙빈님의 상점은 크리에이터 중에서도 #r#e강함을 증명한 크리에이터#n#k만 구경할 수 있어!", 4)
    } else if (status == 2) {
        cm.sendNextPrevS("강한 보스들을 처치하면 구독자님들이 #i4310313# #b#e#z4310313##n#k을 보내주실 테니 분발해 봐!", 4);
    } else if (status == 3) {
        //상점오픈
        cm.getClient().setCustomData(501469, "shopTuto2", "1");
        cm.openShop(29);
        cm.dispose();
    }
} 
