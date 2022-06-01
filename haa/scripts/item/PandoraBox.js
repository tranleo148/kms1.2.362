importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);


var coin = new Array(2049300, 4001785, 4001784, 4310008, 4310066, 4310015, 4310066, 4310015, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4310008, 4310066, 4310015, 4310066, 4310015, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4310008, 4310066, 4310015, 4310066, 4310015, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4310008, 4310066, 4310015, 4310066, 4310015, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126, 4001126);
var dropItems = new Array(2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 2049100, 1012376, 1122252, 1003863, 1102562, 1052612, 1212066, 1222061, 1232060, 1242065, 1302277, 1312155, 1322205, 1332227, 1362092, 1372179, 1382211, 1402199, 1412137, 1422142, 1432169, 1442225, 1452207, 1462195, 1472216, 1482170, 1492181, 1522096, 1532100, 2049100, 2049116, 2049122, 2049153, 2048704, 2048702, 2048703, 2048701, 2049300, 2049100, 2049116, 2049122, 2049153, 2048704, 2048702, 2048703, 2048701, 2049300);
var dropItems2 = new Array(2049100, 2049116, 2048702, 2048703, 2048701, 2049300, 2049100, 2049116, 2048702, 2048703, 2048701, 2049300, 1002551, 1082168, 1072273, 1092060, 1052075, 1302059, 1312031, 1322052, 1232010, 1402036, 1412026, 1432038, 1442045, 1002773, 1082164, 1072268, 1052076, 1382036, 1372032, 1212010, 1332049, 1332050, 1342010, 1362015, 1242010, 1472051, 1472052, 1002550, 1052072, 1082167, 1072272, 1452044, 1462039, 1522014, 1002551, 1082168, 1072273, 1092060, 1052075, 1302059, 1312031, 1322052, 1232010, 1402036, 1412026, 1432038, 1442045, 1002773, 1082164, 1072268, 1052076, 1382036, 1372032, 1212010, 1332049, 1332050, 1342010, 1362015, 1242010, 1472051, 1472052, 1002550, 1052072, 1082167, 1072272, 1452044, 1462039, 1522014, 1002547, 1052071, 1082163, 1072269, 1222010, 1482013, 1492013, 1532014, 1002649, 1052134, 1082216, 1072321, 2049100, 2049116, 2049300, 2049100, 2049116, 2049300);

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
	cm.sendNext("다음에 또 이용해주세요!");
	cm.dispose();
	return;
    }
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
            if (cm.getPlayer().getPandoraBoxFever() == 100) {
            var itemda = dropItems[Randomizer.rand(0, dropItems.length - 1)];
            var itemname = MapleItemInformationProvider.getInstance().getName(itemda);
            cm.gainItem(itemda, 1);
            cm.sendYesNo("판도라의 열쇠로 판도라의 상자를 열었습니다.\r\n판도라의 상자를 한 번 더 열어보시겠습니까?\r\n#e현재 Fever게이지:#r"+ cm.getPlayer().getPandoraBoxFever() + "%#k#n\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v" + itemda + "##z" + itemda +"# 1개");
            World.Broadcast.broadcastMessage(CField.getGameMessage(20, new StringBuilder().append(cm.getPlayer().getName()).append("님이 판도라상자 스페셜 에서 ").append(itemname).append(" 를 얻었습니다.").toString()));
            } else {
            var itemda1 = dropItems2[Math.floor(Math.random() * dropItems2.length)];
            cm.gainItem(itemda1, 1);
            cm.sendYesNo("판도라의 열쇠로 판도라의 상자를 열었습니다.\r\n판도라의 상자를 한 번 더 열어보시겠습니까?\r\n#e현재 Fever게이지:#r"+ cm.getPlayer().getPandoraBoxFever() + "%#k#n\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v" + itemda1 + "##z" + itemda1 +"# 1개");
            }
    } else {
            if (!cm.haveItem(5060028, 1) || !cm.haveItem(4170049, 1)) {
            cm.sendOk("상자를 열려면 판도라의 상자, 판도라의 열쇠 각각 1개씩 필요합니다.");
            cm.dispose();
            }
            var coin1 = coin[Randomizer.rand(0, coin.length - 1)];
            var quantity = Randomizer.rand(1, 5);
            if ((cm.getPlayer().getPandoraBoxFever() == 0) || (cm.getPlayer().getPandoraBoxFever() == 90)) {
                 cm.getPlayer().addPandoraBoxFever(10);
            } else if (cm.getPlayer().getPandoraBoxFever() == 10) {
                 cm.getPlayer().addPandoraBoxFever(8);
            } else if (cm.getPlayer().getPandoraBoxFever() == 100) {
                 cm.gainItem(coin1, quantity);
                 cm.getPlayer().setPandoraBoxFever(10);
            } else {
                 cm.getPlayer().addPandoraBoxFever(9);
            }
            var text = "";
            switch (cm.getPlayer().getPandoraBoxFever()) {
                   case 10:
                        text = "03 67 1A 00 73 75 6D 3D 31 30 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 31 30";
                        break;
                   case 18:
                        text = "03 67 1A 00 73 75 6D 3D 31 38 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 31 38";
                        break;
                   case 27:
                        text = "03 67 1A 00 73 75 6D 3D 32 37 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 32 37";
                        break;
                   case 36:
                        text = "03 67 1A 00 73 75 6D 3D 33 36 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 33 36";
                        break;
                   case 45:
                        text = "03 67 1A 00 73 75 6D 3D 34 35 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 34 35";
                        break;
                   case 54:
                        text = "03 67 1A 00 73 75 6D 3D 35 34 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 35 34";
                        break;
                   case 63:
                        text = "03 67 1A 00 73 75 6D 3D 36 33 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 36 33";
                        break;
                   case 72:
                        text = "03 67 1A 00 73 75 6D 3D 37 32 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 37 32";
                        break;
                   case 81:
                        text = "03 67 1A 00 73 75 6D 3D 38 31 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 38 31";
                        break;
                   case 90:
                        text = "03 67 1A 00 73 75 6D 3D 39 30 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 39 30";
                        break;
                   case 100:
                        text = "03 67 1C 00 73 75 6D 3D 31 30 38 3B 69 64 3D 35 30 36 30 30 32 38 3B 67 61 75 67 65 3D 31 30 30";
            }
	    cm.getClient().getSession().write(CWvsContext.InfoPacket.updatePandoraBox(text));
            if (cm.getPlayer().getPandoraBoxFever() == 100) {
            var itemda = dropItems[Randomizer.rand(0, dropItems.length - 1)];
            var itemname = MapleItemInformationProvider.getInstance().getName(itemda);
            cm.gainItem(itemda, 1);
            cm.sendYesNo("판도라의 열쇠로 판도라의 상자를 열었습니다.\r\n판도라의 상자를 한 번 더 열어보시겠습니까?\r\n#e현재 Fever게이지:#r"+ cm.getPlayer().getPandoraBoxFever() + "%#k#n\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v" + itemda + "##z" + itemda +"# 1개");
            World.Broadcast.broadcastMessage(CField.getGameMessage(20, new StringBuilder().append(cm.getPlayer().getName()).append("님이 판도라상자 스페셜 에서 ").append(itemname).append(" 를 얻었습니다.").toString()));
            } else {
            var itemda1 = dropItems2[Math.floor(Math.random() * dropItems2.length)];
            cm.gainItem(itemda1, 1);
            cm.sendYesNo("판도라의 열쇠로 판도라의 상자를 열었습니다.\r\n판도라의 상자를 한 번 더 열어보시겠습니까?\r\n#e현재 Fever게이지:#r"+ cm.getPlayer().getPandoraBoxFever() + "%#k#n\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v" + itemda1 + "##z" + itemda1 +"# 1개");
            }
            cm.gainItem(5060028, -1);
            cm.gainItem(4170049, -1);
}
}
