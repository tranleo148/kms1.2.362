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
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "뭐... 좋소. 당신들은 충분한 자격이 되어 보이는군. 무엇을 하시겠소?\r\n\r\n";
        talk += "#L0# #b자쿰에게 바칠 제물을 받는다.\r\n";
        talk += "#L1# #b엘나스로 이동한다.";
        cm.sendSimple(talk);
        //cm.sendOk(cm.isBossAvailable("Zakum",1));
    } else if (status == 1) {
        st = selection;
        if (st == 0) {
            talk = "어느 자쿰에게 바칠 제물이 필요하오?\r\n\r\n";
            talk += "#L1# #b노말/카오스 자쿰#l";
            cm.sendSimple(talk);
        } else {
            cm.sendNext("그럼 엘나스로 보내주겠네.");
        }
    } else if (status == 2) {
        if (st == 0) {
            if (cm.itemQuantity(4001017) == 0) {
                cm.sendNext("자쿰에게 바칠 제물이 필요하군..");
            } else {
               cm.sendOk("이미 자쿰의 제물인 #b#z4001017##k을 가지고 있군.. 다 사용하면 다시 말하게.");
                cm.dispose();
                return;
            }
        } else {
            cm.warp(211000000,0);
            cm.dispose();
            return;
        }
    } else if (status == 3) {
        cm.sendNext("다만 자쿰을 부르는데 제물로 필요한 #b#z4001017##k은 지금 내게는 많이 있으니 그냥 주겠네.");
    } else if (status == 4) {
        cm.sendNext("이것을 자쿰의 제단에 떨어뜨리면 된다네.");
        cm.gainItem(4001017, 1);
        cm.dispose();
        return;
    }
}