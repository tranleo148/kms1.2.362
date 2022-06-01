importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);

req = [
[0, [[4310266,300]], 500000000],
[0, [[4310266,700]], 2000000000],
[0, [[4310266,1300]], 5000000000],
[0, [[4310266,2300]], 15000000000],
[0, [[4310266,4500]], 30000000000],
[0, [[4310266,6000]], 50000000000],
[0, [[4310266,9500]], 75000000000],
[0, [[4310266,15000]], 90000000000],
//[0, [[4310266,9000]], 90000000000],
//[0, [[4310269,500], [4310261,200]], 100000000000] // 10성

]
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {     
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (gK()-1 >= 10) {
            cm.sendOk("최고단계까지 승급을 하여 더 이상 승급을 하실 수 없습니다.");
            cm.dispose();
            return;
        }

 if (gK() <= 7) {
        말 = "#fs11##fc0xFF990033##e메인랭크 승급 시스템#n#fc0xFF000000#이라네\r\n#b메인랭크 승급#fc0xFF000000#을 통해 더욱 더 강해져보지 않겠나!?\r\n\r\n"
        말+= "#L0##b다음 랭크로 승급하고 싶습니다.#l#k#n\r\n";
        말+= "#L1##b랭크의 혜택이 궁금합니다.#l#k#n";
}
 if (gK() >= 8) {
        말 = "#fs11#자네는 이미 최대 랭크에 도달했다네";
}
        //말+= "#L1##d설명을 듣는다.#l"
        cm.sendSimple(말);
    } else if (status == 1) { 
        if (selection == 0) {
            말 = "#fs11#다음 랭크로 승급을 하기 위해선 아래와 같은 재료가 필요하다네\r\n\r\n"
            말+= "#k\r\n";
            for (i=0; i<req[gK()][1].length; i++) {
                말 += "#i"+req[gK()][1][i][0]+"# #b#z"+req[gK()][1][i][0]+"##r "+req[gK()][1][i][1]+"개#k\r\n"; // 개수 : cm.itemQuantity(req[gK()][1][i][0])
            }
            말+= "#i4031138# #b메소 #r"+req[gK()][2]+"#k\r\n\r\n"
            말+= "#r#e주의 : 아이템을 지급받을 장비 칸과 소비칸을 5칸 이상 비워주게.#k#n\r\n"
            말+= " #r정말 승급을 하겠나?#k"
            cm.sendYesNo(말);
        } else {
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9062294, "mainrankinfo");
        }
    } else if (status == 2) {
        for (i=0; i<req[gK()][1].length; i++) {
            if (cm.itemQuantity(req[gK()][1][i][0]) < req[gK()][1][i][1]) {
                cm.sendOk("승급에 필요한 #e재료#n가 부족한게 아닌가?");
                cm.dispose();
                return;
            }
        }
        if (cm.getPlayer().getMeso() < req[gK()][2]) {
            cm.sendOk("승급에 필요한 #e메소#n가 부족한게 아닌가?");
            cm.dispose();
            return;
        }
        before = gK();
        if (Math.floor(Math.random() * 100) < 100) {
	if (before == 0) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Bronze]로 승급하셨습니다."));
}
	if (before == 1) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Silver]로 승급하셨습니다."));
}
	if (before == 2) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Gold]로 승급하셨습니다."));
}
	if (before == 3) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Platinum]로 승급하셨습니다."));
}
	if (before == 4) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Diamond]로 승급하셨습니다."));
}
	if (before == 5) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Master]로 승급하셨습니다."));
}
	if (before == 6) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Grand Master]로 승급하셨습니다."));
}
	if (before == 7) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Challenger]로 승급하셨습니다."));
}
	if (before == 8) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] "+cm.getPlayer().getName()+"님이 [Overload]로 승급하셨습니다."));
}
           말 = "승급에 성공하셨습니다.\r\n\r\n"
           cm.showEffect(false,"tdAnbur/idea_hyperMagic");
	cm.setZodiacGrade(gK() + 1);
           cm.sendOk(말);
        } else {
           cm.sendOk("승급에 실패 하였습니다.")
        }

        for (i=0; i<req[gK()][1].length; i++) {
            //Packages.server.MapleInventoryManipulator.removeById(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, req[gK()][1][i][1], 1, false, false);
            //cm.gainItem(4310261, -100);
        }
        cm.gainMeso(-req[before][2]);
 if (before == 0) {
        cm.gainItem(4310266, -300);
}
 if (before == 1) {
        cm.gainItem(4310266, -700);
}
 if (before == 2) {
        cm.gainItem(4310266, -1300);
}
 if (before == 3) {
        cm.gainItem(4310266, -2000);
}
 if (before == 4) {
        cm.gainItem(4310266, -2500);
}
 if (before == 5) {
        cm.gainItem(4310266, -4000);
}
 if (before == 6) {
        cm.gainItem(4310266, -6500);
}
 if (before == 7) {
        cm.gainItem(4310266, -7000);
}
 if (before == 8) {
        cm.gainItem(4310266, -9000);
}
        cm.dispose();
    }
}

function gK() {
    return cm.getPlayer().getKeyValue(190823, "grade");
}
