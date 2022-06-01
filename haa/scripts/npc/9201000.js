importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.launch.world);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.packet);

var status = -1;
var sel = 0, seld = -1;
var name = "";

var couplering = [1112001, 1112002, 1112003, 1112005, 1112007, 1112014, 1112015, 1112016, 1112006, 1112013];
var ujongring = [1112817, 1112800, 1112801, 1112802, 1112811, 1112812, 1112816, 1112810];

var price1 = 30000;
var price2 = 30000;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == 1) {
        status++;
    } else {
	cm.dispose();
	return;
    }

	if (status == 0) {
	var chat= "#fs11#자네도 마음의 드는 친구가 생겼나 보군, 다양한 반지가 있으니 천천히 둘러보게!";
	chat += "\r\n";
	chat += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
	for (i = 0; i < couplering.length; i++) {
	    chat += "#L" + couplering[i] + "# #i" + couplering[i] + "# #b#z" + couplering[i] + "##k #e#r#n#b#l\r\n"; 
	}
	chat += "\r\n\r\n#k#fs11#----------------------우정링 목록----------------------#fs11#\r\n";
	for (i = 0; i < ujongring.length; i++) {
	    chat += "#L" + ujongring[i] + "# #i" + ujongring[i] + "# #b#z" + ujongring[i] + "##k #e#r#n#b#l\r\n"; 
	}
	cm.sendSimple(chat);
    	} else if (status == 1) {
	sel = selection;
		var msg = "#fs11#무엇을 지불해서 만드실건가요?#fs11##b\r\n";
		msg += "#L100##fs11# 후원 포인트 (올스텟 100/ 공마 50, "+price1+"포인트 지불)\r\n";
		msg += "#L101##fs11# 홍보 포인트 (올스텟 100/ 공마 50, "+price2+"포인트 지불)\r\n";
		cm.sendSimple(msg);
    } else if (status == 2) {
	seld = selection;
	/*if (seld == 100 && cm.getPlayer().getDPoint() < price1) {
		cm.sendOk("#fs11#후원 포인트가 부족합니다.");
		cm.dispose();
		return;
	} else if (seld == 101 && cm.getPlayer().getHPoint() < price2) {
		cm.sendOk("#fs11#홍보 포인트가 부족합니다.");
		cm.dispose();
		return;
	}*/
	cm.sendGetText("#fs11#반지를 선물할 분의 닉네임을 입력해주세요.");
    } else if (status == 3) {
	/*if (seld == 100 && cm.getPlayer().getDPoint() < price1) {
		cm.sendOk("#fs11#후원 포인트가 부족합니다.");
		cm.dispose();
		return;
	} else if (seld == 101 && cm.getPlayer().getHPoint() < price2) {
		cm.sendOk("#fs11#홍보 포인트가 부족합니다.");
		cm.dispose();
		return;
	}*/
	name = cm.getText();
	
	cm.sendYesNo("#fs11#선택하신 반지 [#b#i" + sel + "# #z" + sel + "##k](을)를 정말로 제작 하시겠습니까?");
    } else if (status == 4) {
	var id = MapleCharacterUtil.getIdByName(cm.getText());
	if (id <= 0) {
		cm.sendOk("#fs11#입력하신 " + name + "님은 존재하지 않는 분입니다. 닉네임을 다시 한 번 확인해 주시길 바랍니다.");
		cm.dispose();
		return;
	}
	var chr = cm.getChar(id);
	if (chr == null) {
		cm.sendOk("#fs11#반지를 받으실 분이 오프라인이네요. " + name + "님이 접속하시면 다시 찾아와 주시길 바랍니다.");
		cm.dispose();
		return;
	}
	if (!(chr.getInventory(GameConstants.getInventoryType(sel)).getNextFreeSlot() > -1)) {
		cm.sendOk("#fs11#반지를 받으실 분의 인벤토리 공간이 부족합니다.");
		cm.dispose();
		return;
	}
	if (!cm.canHold(sel)) {
		cm.sendOk("#fs11#반지를 받기 위해서는 인벤토리에 한 칸 이상의 빈 공간이 필요 합니다.");
		cm.dispose();
		return;
	}
	if (couplering.indexOf(sel) == -1) {
        World.Broadcast.broadcastMessage(CField.getGameMessage(9, "[커플] "+ cm.getPlayer().getName()+"님과 "+ chr.getName() +"님이 우정링을 제작하셨습니다!"));
	} else {
	    World.Broadcast.broadcastMessage(CField.getGameMessage(9, "[커플] "+ cm.getPlayer().getName()+"님과 "+ chr.getName() +"님이 커플링을 제작하셨습니다!"));
	}
	if (seld == 100) {
		//cm.getPlayer().gainDPoint(-price1);
		cm.makeRingRC(sel,chr);
	} else if (seld == 101) {
		//cm.getPlayer().gainHPoint(-price2);
		cm.makeRingHB(sel,chr);
	}
	cm.dispose();
    }
}
