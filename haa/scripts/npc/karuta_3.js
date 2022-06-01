var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    itemlist = [1003797, 1003798, 1003799, 1003780, 1003781];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "#b#e찬스타임 게이지:#r0%(임시)#k#n\r\n\r\n"
        talk += "현재 직업이 착용 가능한 장비를 우선 추천해 드립니다.\r\n받으실 방어구를 선택해 주세요.\r\n"
        getClass = getClassById(cm.getPlayer().getJob());
        if (getClass == -1) {
            cm.sendOk("초보자는 조각을 사용하실 수 없습니다.");
            cm.dispose();
            return;
        }
        talk += "#L" + getClass + "# #i" + itemlist[getClass] + "# #b#z" + itemlist[getClass] + "##k#l\r\n"
        cm.sendSimple(talk);
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