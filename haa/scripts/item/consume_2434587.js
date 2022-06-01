var status = 0;
var count = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getPlayer().getKeyValue(20190211, "karuta_4") < 0) {
        cm.getPlayer().setKeyValue(20190211, "karuta_4", 0);
    }
    itemlist = [
        [1212063,[271]],
        [1222058,[651]],
        [1232057,[312]], 
        [1242060,[361]],
        [1242137,[361]],
        [1262016,[1421]],
        [1272015,[641]], 
        [1282015,[1521]], 
        [1302275,[11,12,111,511,611]], 
        [1312153,[11,311]], 
        [1322203,[12,311]], 
        [1332225,[42,43]], 
        [1342082,[43]],
        [1362090,[241]], 
        [1372177,[21,22,23,121,221]], 
        [1382208,[21,22,23,121,221,321]], 
        [1402196,[11,12,111,611]], 
        [1412135,[11]], 
        [1422140,[12]], 
        [1432167,[13]], 
        [1442223,[13,211]], 
        [1452205,[31,131]], 
        [1462193,[32,331]], 
        [1472214,[41,141]], 
        [1482168,[51,151,1551,251]], 
        [1492179,[52]], 
        [1522094,[231]], 
        [1532098,[53]], 
        [1582016,[351]],
	[2048905, [1011]]
    ];
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
        if (!cm.canHold(itemlist[0][0])) {
            cm.sendOk("장비창을 1칸 이상 비운 뒤 다시 사용해 주세요.");
            cm.dispose();
            return;
        }
        if (cm.itemQuantity(2434587) < 15) {
            cm.sendOk("조각이 모자랍니다.");
            cm.dispose();
            return;
        }
        talk = "#b#e찬스타임 게이지:#r" + cm.getPlayer().getKeyValue(20190211, "karuta_4") + "%#k#n\r\n\r\n"
        if (cm.getPlayer().getKeyValue(20190211, "karuta_4") >= 100) {
            talk += "#r#e찬스타임 발동!#k#n\r\n"
            talk += "#b#e더 좋은 추가옵션#k#n이 나올 확률이 증가합니다.\r\n\r\n"
        }
        talk += "현재 직업이 착용 가능한 장비를 우선 추천해 드립니다.\r\n받으실 무기를 선택해 주세요.\r\n"
        getClass = Math.floor(cm.getPlayer().getJob()/10)
        for (i=0; i<itemlist.length; i++) {
            for (j=0; j<itemlist[i][1].length; j++) {
                if (getClass == itemlist[i][1][j]) {
                    count++;
                    talk+= "#L"+i+"# #i"+itemlist[i][0]+"# #b#z"+itemlist[i][0]+"##k#l\r\n"
                }
            }
        }
        if (count == 0) {
            cm.getPlayer().dropMessage(5, "초보자는 사용할 수 없습니다. 전직 후 다시 시도해 주세요.");
            cm.dispose();
            return;
        }
        talk += "\r\n#L100##b전체 아이템 리스트를 본다.#k#l\r\n"
        talk += "#L101##b사용 취소#k#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        talk = "받으실 방어구를 선택해 주세요.\r\n"
        for (i = 0; i < itemlist.length; i++) {
            talk += "#L" + i + "# #i" + itemlist[i][0] + "# #b#z" + itemlist[i][0] + "##k#l\r\n";
        }
        cm.sendSimple(talk);
    } else if (status == 2) {
        selected = selection;
        talk = "받고싶은 장비가 이 장비가 확실한가요?\r\n"
        talk += "#i" + itemlist[selected][0] + "# #z" + itemlist[selected][0] + "#\r\n\r\n"
        talk += "#r#e※주의※#k#n\r\n"
        talk += "한 번 조각을 아이템으로 교환하면 #r5개의 조각이 차감#k되며, 다시 다른 아이템으로 교환 할 수 없습니다.\r\n\r\n"
        talk += "#L0##b네 맞습니다.\r\n"
        talk += "#L1##b아닙니다. 다시 선택하겠습니다.";
        cm.sendSimple(talk);
    } else if (status == 3) {
        if (selection == 1) {
            cm.sendOk("다시 조각을 사용해주세요.");
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getKeyValue(20190211, "karuta_4") == 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_4", 0)
        }
        cm.getPlayer().setKeyValue(20190211, "karuta_4", cm.getPlayer().getKeyValue(20190211, "karuta_4") + Packages.server.Randomizer.rand(7, 11));
        if (cm.getPlayer().getKeyValue(20190211, "karuta_4") >= 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_4", 100)
        }
        cm.gainItem(itemlist[selected][0], 1);
        cm.gainItem(2434587, -15);
        cm.dispose();
    }
}