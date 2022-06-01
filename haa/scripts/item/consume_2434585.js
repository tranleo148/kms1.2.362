var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getPlayer().getKeyValue(20190211, "karuta_2") < 0) {
        cm.getPlayer().setKeyValue(20190211, "karuta_2", 0);
    }
    itemlist = [1062165,1062166,1062167,1062168,1062169];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && selection < 100) {
            status++;
        }
        if (status == 0 && selection == 101) {
            cm.dispose();
            return;
        }
        status++;
    }
    if (status == 0) {
        if (!cm.canHold(itemlist[0])) {
            cm.sendOk("장비창을 1칸 이상 비운 뒤 다시 사용해 주세요.");
            cm.dispose();
            return;
        }
        if (cm.itemQuantity(2434585) < 5) {
            cm.sendOk("조각이 모자랍니다");
            cm.dispose();
            return;
        }
        talk = "#b#e찬스타임 게이지:#r"+cm.getPlayer().getKeyValue(20190211, "karuta_2")+"%#k#n\r\n\r\n"
        if (cm.getPlayer().getKeyValue(20190211, "karuta_2") >= 100) {
            talk+= "#r#e찬스타임 발동!#k#n\r\n"
            talk+= "#b#e더 좋은 추가옵션#k#n이 나올 확률이 증가합니다.\r\n\r\n"
        }
        talk += "현재 직업이 착용 가능한 장비를 우선 추천해 드립니다.\r\n받으실 방어구를 선택해 주세요.\r\n"
        getClass = getClassById(cm.getPlayer().getJob());
        if (getClass == -1) {
            cm.getPlayer().dropMessage(5, "초보자는 사용할 수 없습니다. 전직 후 다시 시도해 주세요.");
            cm.dispose();
            return;
        }
        talk += "#L" + getClass + "# #i" + itemlist[getClass] + "# #b#z" + itemlist[getClass] + "##k#l\r\n"
        talk += "\r\n#L100##b전체 아이템 리스트를 본다.#k#l\r\n"
        talk += "#L101##b사용 취소#k#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        talk = "받으실 방어구를 선택해 주세요.\r\n"
        for (i=0; i<itemlist.length; i++) {
            talk+= "#L"+i+"# #i"+itemlist[i]+"# #b#z"+itemlist[i]+"##k#l\r\n";
        }
        cm.sendSimple(talk);
    } else if (status == 2) {
        selected = selection;
        talk = "받고싶은 장비가 이 장비가 확실한가요?\r\n"
        talk+= "#i"+itemlist[selected]+"# #z"+itemlist[selected]+"#\r\n\r\n"
        talk+= "#r#e※주의※#k#n\r\n"
        talk+= "한 번 조각을 아이템으로 교환하면 #r5개의 조각이 차감#k되며, 다시 다른 아이템으로 교환 할 수 없습니다.\r\n\r\n"
        talk+= "#L0##b네 맞습니다.\r\n"
        talk+= "#L1##b아닙니다. 다시 선택하겠습니다.";
        cm.sendSimple(talk);
    } else if (status == 3) {
        if (selection == 1) {
            cm.sendOk("다시 조각을 사용해주세요.");
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getKeyValue(20190211, "karuta_2") == 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_2", 0)
        }
        cm.getPlayer().setKeyValue(20190211, "karuta_2", cm.getPlayer().getKeyValue(20190211, "karuta_2") + Packages.server.Randomizer.rand(7,11));
        if (cm.getPlayer().getKeyValue(20190211, "karuta_2") >= 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_2", 100)
        }
        cm.gainItem(itemlist[selected], 1);
        cm.gainItem(2434585, -5);
        cm.dispose();
    }
}

function getClassById(paramint) {
    getClass = (Math.floor(paramint / 100)) % 10;
    switch (getClass) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            return getClass - 1;
            break;
        case 6:
            return 3;
            break;
        case 7:
            return Math.floor(paramint / 100) == 27 ? 1 : 0;
            break;
        default:
            return -1;
            break;
    }
}