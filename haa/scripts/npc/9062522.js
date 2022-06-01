


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    신궁 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062513

    엔피시 이름 : 나무의 정령

    엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

    엔피시 설명 : 블루밍 쉐어 코인샵


*/

var status = -1;
var blueflower = false;

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
        if (cm.getPlayer().getKeyValue(100790, "shopTutoM") == 1) {
            if (cm.getClient().getKeyValue("BloomingTuto") != null) {
                if (parseInt(cm.getClient().getKeyValue("BloomingTuto")) > 3) {
                    //푸른꽃을 피웠을때
                    blueflower = true;
                }
            }
            if (blueflower) {
                cm.openShop(25);
                cm.dispose();
                return;
            } else {
                cm.sendNextPrevS("흠, 아직 #b#e푸른 꽃#n#k을 피우지 못했군.", 4, 9062522);
            }
        } else {
            cm.sendNextS("정말 아름다운 곳이야.\r\n이곳에 #b#e다양한 꽃이#n#k 핀다면 더욱 아름답겠지.", 4, 9062522);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getKeyValue(100790, "shopTutoM") == 1) {
            if (!blueflower) {
                cm.sendNextPrevS("원래는 안 되지만... 특별히 너에게는 보여주겠어.", 4, 9062522);
            }
        } else {
            cm.sendNextPrevS("이 숲에 #b#e푸른 꽃#n#k을 피우면 나를 찾아오도록 해.\r\n다른 곳에서 #b#e쉽게 구하기 어려운 물건#n#k들을 보여줄게.\r\n\r\n#e※블루밍 메소샵 이용 기간#r\r\n6월 20일 오후 11시 59분까지#k#n", 4, 9062522);
        }
    } else if (status == 2) {
        if (cm.getPlayer().getKeyValue(100790, "shopTutoM") == 1) {
            if (!blueflower) {
                cm.openShop(25);
                cm.dispose();
            }
        } else {
            cm.getPlayer().setKeyValue(100790, "shopTutoM", "1");
            cm.dispose();
            cm.openNpc(9062522);
        }
    }
}
