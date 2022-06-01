


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
        if (cm.getClient().getCustomData(501469, "shopTuto") != null) {
            cm.openShop(23);
            cm.dispose();
        } else {
            cm.sendNextS("(분홍콩샵에 처음 온 #b#e초보 크리에이터#n#k로군...?)", 4);
        }
    } else if (status == 1) {
        cm.sendNextPrevS("하핫! 선생님은 저희 분홍콩샵에 처음 오셨군요.\r\n#r#e#i4310312# #t4310312##n#k을 가져오시면 제가 귀한 물건을 보여드리지요.", 4)
    } else if (status == 2) {
        cm.sendNextPrevS("물론 초보 크리에이터가 살 수 있는 물건은 별로 없겠지만...\r\n하핫! 얼른 유명해져서 #r#e#i4310312# #t4310312##n#k을 많이 모아오시죠.", 4);
    } else if (status == 3) {
        //상점오픈
        cm.getClient().setCustomData(501469, "shopTuto", "1");
        cm.openShop(23);
        cm.dispose();
    }
}
