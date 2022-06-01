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
            cm.sendYesNo("#fn나눔고딕 Extrabold#자네.. 나에게 볼 일이 있는가?\r\n어디보자.. 자네는 #r탈레스#k 가 보낸 사람인 것 같군..\r\n내가 당신을 태워주겠네.. 지금 바로 #d출발#k 하겠나?")
    } else if (status == 1) {
		cm.warp (302020000,0);
                cm.sendNext("#fn나눔고딕 Extrabold#내가.. 대려다줄 수 있는 곳은 여기까지군..\r\n아무쪼록 더욱 더 .. 힘내서 잘 가게나..");
                cm.getPlayer().dropMessage(-1,"흠.. 뭐지..? 일단 앞으로.. 나아가 볼까?...");
                cm.getPlayer().dropMessage(5,"흠.. 뭐지..? 일단 앞으로.. 나아가 볼까?..."); 
                cm.dispose();
    }
}


