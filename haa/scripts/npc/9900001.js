
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.launch);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.tools);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
importPackage(Packages.server.life);
importPackage(Packages.tools.RandomStream);


var status = 0;
var operation = -1;
var select = -1;
var type;
var ty;
var gc = GameConstants;
var dd = true;
var yes= 1;
var invs = Array(1, 5);
var invv;
var selected;
var slot_1 = Array();
var slot_2 = Array();
var statsSel;
var sel;
var name;
var isban = false;

var banitem = [2437659,3994410,2434583,2430027,2040727,2439302,2437121,2430218,2023072,2430488,2433977,2432305,3994351,2437122,2023287,4033114,4310261,2630442,];


function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			var ask = "#fs11#어느 타입의 아이템을 #r택배#k로 보내시겠습니까?\r\n택배를 보내기위해서는 #i5330000# 1개가 필요합니다.\r\n";
			ask +="#L1##b[장비]#k아이템을 보내겠습니다.\r\n";
			ask +="#L2##b[소비]#k아이템을 보내겠습니다.\r\n";
			ask +="#L3##b[설치]#k아이템을 보내겠습니다.\r\n";
                                  //ask +="#L4##b[기타]#k아이템을 보내겠습니다.\r\n";
                                           ask +="#L5##b[캐시]#k아이템을 보내겠습니다.\r\n";
 			ask +="#L6##b[치장]#k아이템을 보내겠습니다.\r\n";
			cm.sendSimple(ask);

 } else if (cm.getPlayer().getReborns() < 0) {
	   	cm.sendOk("\r\n#b총 10번 이상의 환생 유저만 사용가능.\r\n");
		cm.dispose();

		} else if (status == 1) {
			operation = selection;
			if (operation == 1) {
				type = MapleInventoryType.EQUIP;
				yes = 1;
			} else if (operation == 2) {
				type = MapleInventoryType.USE;
				yes = 2;
			} else if (operation == 4) {
				type = MapleInventoryType.SETUP;
				yes = 4;
			} else if (operation == 3) {
				type = MapleInventoryType.ETC;
				yes = 3;
			} else if (operation == 5) {
				type = MapleInventoryType.CASH;
				yes = 5;
			} else if (operation == 6) {
				type = MapleInventoryType.DECORATION;
				yes = 6;
			}
			if (selection >= 1 && selection <=6) {
				cm.sendGetText("#fs11# 택배받으실 분의 아이디를 입력해주세요.#r\r\n\r\n택배시스템은 아이템의 종류나 옵션에 관계없이 누구에게나 택배할수 있는 시스템입니다. 택배를 보내기 위해선 퀵배송이용권이 필요하며, 같은 채널에 접속중이여야 합니다. 잘못된 사용으로 인한 문제는 운영진 측에서 책임지지 않습니다, 고유 아이템 택배시 이미 아이템을 보유 중 일시 증발되며 이에 대한 복구는 책임지지 않습니다.");
			} else if (selection == 7) {
				cm.sendOk("#fs11# 택배시스템은 아이템의 종류나 옵션에 관계없이 누구에게나 택배할수 있는 시스템입니다. 택배을 하기 위해선 5억 메소가 필요하며, 같은 채널에 접속중이여야 합니다. 잘못된 사용으로 인한 문제는 운영진 측에서 책임지지 않습니다.");
				cm.dispose();
			}
		} else if (status == 2) {
			if (operation == 1) {
				type = MapleInventoryType.EQUIP;
			} else if (operation == 2) {
				type = MapleInventoryType.USE;
			} else if (operation == 3) {
				type = MapleInventoryType.SETUP;
			} else if (operation == 4) {
				type = MapleInventoryType.ETC;
			} else if (operation == 5) {
				type = MapleInventoryType.CASH;
			} else if (operation == 6) {
				type = MapleInventoryType.DECORATION;
			}
				var item = cm.getChar().getInventory(type);
		var text = cm.getText();
		var conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(text);
		if (conn == null){
		cm.sendOk("#fs11# 현재 접속중이 아니거나 채널이 다릅니다. 혹은 존재하지 않는 아이디일 수도 있습니다.");
		cm.dispose();
		}else{
		var ok = false;
		var selStr = "#b"+conn.getName()+"#k님에게 어떤 아이템을 택배하시겠습니까?\r\n";
		for (var x = 1; x < 2; x++) {
			var inv = cm.getInventory(yes);
			for (var i = 0; i <= cm.getInventory(yes).getSlotLimit(); i++) {
				if (x == 0) {
					slot_1.push(i);
				} else {
					slot_2.push(i);
				}
				var it = inv.getItem(i);
				if (it == null) {
					continue;
				}
				var itemid = it.getItemId();
				ok = true;
				if ((itemid >= 1140000 && itemid <= 1143999) && !cm.getPlayer().isGM()) {
						continue;
				}
				selStr += "#L" + (yes * 1000 + i) + "##v" + itemid + "##t" + itemid + "##l\r\n";
			}
		}
		if (!ok) {
			cm.sendOk("#fs11# 택배할 아이템이 없는것 같은데요?");
			cm.dispose();
			return;
		}
		cm.sendSimple(selStr + "#k");
		}
		} else if (status == 3) {
		sel = selection;
			if (operation == 1) {
				type = MapleInventoryType.EQUIP;
			} else if (operation == 2) {
				type = MapleInventoryType.USE;
			} else if (operation == 3) {
				type = MapleInventoryType.SETUP;
			} else if (operation == 4) {
				type = MapleInventoryType.ETC;
			} else if (operation == 5) {
				type = MapleInventoryType.CASH;
			} else if (operation == 6) {
				type = MapleInventoryType.DECORATION;
			}
			var item = cm.getChar().getInventory(type).getItem(selection % 1000).copy();
			var text = cm.getText();
			invv = selection / 1000;
			var inzz = cm.getInventory(invv);
			selected = selection % 1000;
				if (invv == invs[0]) {
					statsSel = inzz.getItem(slot_1[selected]);
				} else {
					statsSel = inzz.getItem(slot_2[selected]);
				}
				if (statsSel == null) {
					cm.sendOk("#fs11# 오류입니다. 운영자에게 보고해주세요.");
					cm.dispose();
					return;
				}
			var text = cm.getText();
			var con = cm.getClient().getChannelServer().isMyChannelConnected(text);
			var conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(text);


				for (a = 0; a < banitem.length; a++) {
					if (banitem[a] == item.getItemId()) {
						isban = true;
						continue;
					}
				}
	if (item.getQuantity() == 1){
		if (cm.haveItem(5330000, 0)) {
			if (GameConstants.isPet(item.getItemId()) == false) {
				if (cm.getPlayer().getName() != text) {
					//if (!conn.canHold(item.getItemId())) {
					//	cm.sendOk("#fs11# 택배을 받을 상대방의 인벤토리에 빈 공간이 없습니다.");
					//	cm.dispose();
					//	return;
					//}
					if (isban) {
						cm.sendOk("금지된 아이템입니다.");
						cm.dispose();
						return;
					}
			MapleInventoryManipulator.removeFromSlot(cm.getC(), type, selection%1000, item.getQuantity(), true);
			MapleInventoryManipulator.addFromDrop(conn.getClient(), item, true);
			cm.gainItem(5330000, -1);
			WriteLog(cm.getPlayer().getName(), text, item.getItemId(), item.getQuantity());
			cm.dispose();
			}	else {
				cm.sendOk("#fs11# 자기 자신에게는 택배할수 없습니다.");
				cm.dispose();
			}
			}else {
				cm.sendOk("#fs11# 펫은 택배할수 없습니다.");
				cm.dispose();
			}
			}else{
			cm.sendOk("이용할 권한이 없습니다.");
			cm.dispose();
			}
			}else {
				if (!isban) {
					cm.sendGetNumber("#fs11# 몇개를 택배보내시겠습니까?\r\n현재 소지중인 #i"+item.getItemId()+"# #b(#t"+item.getItemId()+"#)#k 갯수 : #b"+item.getQuantity()+"#k", 1, 1, item.getQuantity());
				} else {
					cm.sendOk("금지된 아이템입니다.");
					cm.dispose();
				}
			}
			name = text;
		}else if (status==4){

		if (isban) {
			cm.sendOk("오류입니다.");
			cm.dispose();
			return;
		}
		var sele = selection%1000;
		var quan = cm.getText();
			if (operation == 1) {
				type = MapleInventoryType.EQUIP;
			} else if (operation == 2) {
				type = MapleInventoryType.USE;
			} else if (operation == 3) {
				type = MapleInventoryType.SETUP;
			} else if (operation == 4) {
				type = MapleInventoryType.ETC;
			} else if (operation == 5) {
				type = MapleInventoryType.CASH;
			}  else if (operation == 6) {
				type = MapleInventoryType.DECORATION;
			}
			var item = cm.getChar().getInventory(type).getItem(sel%1000).copy();
			var text = cm.getText();
			invv = sel / 1000;
			var inzz = cm.getInventory(invv);
			selected = sel % 1000;
				if (invv == invs[0]) {
					statsSel = inzz.getItem(slot_1[selected]);
				} else {
					statsSel = inzz.getItem(slot_2[selected]);
				}
				if (statsSel == null) {
					cm.sendOk("오류입니다. 운영자에게 보고해주세요.");
					cm.dispose();
					return;
				}

			var text = selection;
			var con = cm.getClient().getChannelServer().isMyChannelConnected(name);
			var conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name);
	if (item.getQuantity() >= text) {
		if (cm.haveItem(5330000, 0)) {
			if (cm.getPlayer().getName() != name) {
			item.setQuantity(text);
			MapleInventoryManipulator.removeFromSlot(cm.getC(), type, sel%1000, item.getQuantity(), true);
			MapleInventoryManipulator.addFromDrop(conn.getClient(), item, true);
            //cm.gainItem(5330000,-1);
			WriteLog(cm.getPlayer().getName(),name, item.getItemId(), item.getQuantity());
			cm.dispose();
			}else {
				cm.sendOk("자기 자신에게는 택배할수 없습니다.");
				cm.dispose();
			}
			}else {
				cm.sendOk("아이템을 택배보내기 위해선 퀵배송 이용권이 필요합니다.");
				cm.dispose();
			}
			}else {
				cm.sendOk("가지고 있는 수보다 더 큰 수를 입력했습니다.");
				cm.dispose();
			}
		}
	}
}		

/*
function writelog(v, id) {

           	a = new Date();
	temp = Randomizer.rand(0,9999999);
	cn = cm.getPlayer().getName();
           fFile1 = new File("property/Logs/선물/"+temp+"_"+cn+".log");
           if (!fFile1.exists()) {
               	fFile1.createNewFile();
           }
           out1 = new FileOutputStream("property/LogFile/선물/"+temp+"_"+cn+".log",false);
	   	var msg =  "'"+cm.getPlayer().getName()+"'이(가) "+t+"함.\r\n";
           	msg += "'"+a.getFullYear()+"년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'\r\n";
		msg += " 받은사람 : "+v+"\r\n";
		msg += " 아이템 코드 : "+id+"\r\n";
            	out1.write(msg.getBytes());
            	out1.close();
}
*/
function WriteLog(cname, vname, itemid, qty) {
        a = new Date();
	temp = Randomizer.rand(0,9999999);
	fFile1 = new File("LogFile/선물/"+cname+"가 "+vname+"에게 템선물 "+itemid+".log");
        if (!fFile1.exists()) fFile1.createNewFile();
        out1 = new FileOutputStream("LogFile/선물/"+cname+"가 "+vname+"에게 템선물 "+itemid+".log",false);
	var msg = "'"+cname+"'이 '"+vname+"'에게 선물을 보냈습니다.\r\n";
	msg += "보낸이 : "+cname+"\r\n";
	msg += "받은이 : "+vname+"\r\n";
	msg += "보낸 시각 : "+a.getFullYear()+"년 "+Number(a.getMonth() + 1)+"월 "+a.getDate()+"일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초\r\n";
	msg += "보낸 아이템 코드 : "+itemid+"\r\n";
	msg += "보낸 아이템 개수 : "+qty+"\r\n";
        out1.write(msg.getBytes());
        out1.close();
}