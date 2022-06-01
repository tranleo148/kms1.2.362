importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var seld2 = -1;

var selditem;

var 벤1 = [1112001, 1112002, 1112003, 1112005, 1112007, 1112015, 1112012, 1112006];
var 벤2 = [1112817, 1112800, 1112801, 1112811, 1112812, 1112816, 1112802, 1112810];

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == -1) {
		cm.dispose();
	}
	if (mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}
	if (status == 0) {
		//if (!cm.getPlayer().isGM()) {
			//cm.dispose();
			//return;
		//}
		var msg ="#fs11#안녕하세요! #h #님\r\n#fs11##d"+enter;
		msg += "#L1#캐시 아이템을 옮기고 싶습니다."+enter;
		msg += "#L2#캐시 아이템을 찾고 싶습니다.";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (seld) {
			case 1:
				var msg = "어떤 캐릭터에게 옮기시겠습니까?#fs11#"+enter;
				msg += cm.getCharacterList(cm.getClient().getAccID());
				cm.sendSimple(msg);
			break;
			case 2:
				var msg = "어떤 아이템을 찾고 싶으십니까?#fs11#"+enter;
				msg += cm.getCashStorages1(cm.getPlayer().getId());
				cm.sendSimple(msg);
			break;
		}
	} else if (status == 2) {
		seld2 = sel;
		switch (seld) {
			case 1:
				var msg = "어떤 아이템을 옮기시겠습니까?#fs11#"+enter;
				msg += " + 선택하신 캐릭터 : #b"+charname(seld2)+"#k"+enter;
            			for (i = 0; i < cm.getInventory(5).getSlotLimit(); i++) {
                				if (cm.getInventory(5).getItem(i) != null && cm.isCash(cm.getInventory(5).getItem(i).getItemId())) {
                    					msg+= "#L" + i + "# #i" + cm.getInventory(5).getItem(i).getItemId() + "# #b#z" + cm.getInventory(5).getItem(i).getItemId() + "##k#l"+enter;
                				}
            			}
				cm.sendSimple(msg);
			break;
			case 2:
				if (!cm.canHold(1702792, 1)) {
					cm.sendOk("캐시칸에 공간이 적어도 한 칸 이상 있어야 합니다.");
					cm.dispose();
					return;
				}
				cm.gainItemInStorages1(seld2);
				cm.sendOk("완료되었습니다.");
				cm.dispose();
			break;
		}
	} else if (status == 3) {
		switch (seld) {
			case 1:
				selditem = cm.getInventory(5).getItem(sel);

				for (i = 0; i < 벤1.length; ++i) {
				    if (벤1[i] == selditem.getItemId()) {
					cm.sendOk("이 아이템은 옮기실수가 없습니다");
					cm.dispose();
					return;
				    }
				}

				for (i = 0; i < 벤2.length; ++i) {
				    if (벤2[i] == selditem.getItemId()) {
					cm.sendOk("이 아이템은 옮기실수가 없습니다");
					cm.dispose();
					return;
				    }
				}
				
				storeItem(seld2, selditem.getItemId(), selditem.getQuantity());
				Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CASH, sel, cm.getInventory(5).getItem(sel).copy().getQuantity(), true);
				cm.sendOk("완료되었습니다.");
				cm.dispose();
			break;
		}
	}
}

function charname(i) {
	var ret = "";
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
	ps.setInt(1, i);
	var rs = ps.executeQuery();
	if (rs.next()) {
		ret = rs.getString("name");
	}
	rs.close();
	ps.close();
	return ret;
}

function storeItem(charid, itemid, quantity) {
	var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("INSERT INTO cashstorages1 (charid, itemid, quantity) VALUES(?, ?, ?)");
            ps.setInt(1, charid);
            ps.setInt(2, itemid);
            ps.setShort(3, quantity);
            ps.executeUpdate();
	ps.close();
}