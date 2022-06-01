importPackage(java.io);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.constants);

var status = -1;
var con = 1;
var con1 = 1;
var con2 = true;
var list = []; // 아이템 리스트
var garbage = []; // 삭제할 아이템 리스트
var slotnumber = []; // 슬롯번호
// 아이템 리스트 만드는 변수들
var check = true;
var MakeValue = 0;
var jj = 0;
var equip = Packages.client.inventory.MapleInventoryType.EQUIP;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
	if(status == 0 && selection == 1000){
	  con = 1;
	  con1 = 1;
	} else if(status == 0 && selection == 2000) {
	  con = 2;
	  con1 = 2;
	} else if(status == 0 && selection == 3000) {
	  con = 3;
	  con1 = 3;
	} else if(status == 0 && selection == 4000) {
	  con = 4;
	  con1 = 4;
	} else if(status == 0 && selection == 5000) {
	  con = 5;
	  con1 = 5;
	} else if(status == 0 && selection == 6000) {
	  con = 6;
	  con1 = 6;
	} else if(status == 0 && selection == 7000) {
	 con2 = false;
	} else if(status == 0 && selection == 8000) {
	 con2 = true;
	reset();
	} else if(status == 0 && selection == 9000) {
	for(k=0; k<list.length; k++){
	  if(list[k]['star'] == 1){
	    del(list[k]['slnumber'], list[k]['slot']);
	    //cm.getPlayer().dropMessage(5, "값 : "+list[k]['slnumber']+"장비 : "+list[k]['slot']);
	  }
	}
	reset();
	} else if(status == 0 && selection <= 150 && con2 == true) {
	 del(list[selection]['slnumber'], con);
	 reset();
	} else if(status == 0 && selection <= 150 && con2 == false) {
	// 아이템 선택 해제
	if(list[selection]['star'] == 0){
		list[selection]['star'] = 1;
	} else {
		list[selection]['star'] = 0;
	}
	} else {
	status++;

	}
    }
    if (status == 0) {
        talk = "#r(아이템을 버릴 시 드롭되지 않고 사라지며, 복구가 불가능하니 유의해 주세요!)#k\r\n"
        talk += "#b#L1000#장비#l #L2000#소비#l #L4000#기타#l #L3000#설치#l  #L5000#캐시#l  #L6000#치장#k\r\n"
        if(con2 == true){
        talk += "#r#L7000#여러개 버리기#l#k\r\n\r\n"
        } else {
        talk += "#r#L8000#1개씩 버리기#l #L9000#선택한 아이템 삭제하기#l#k\r\n\r\n"
        }
 	if(check == true && list.length == 0){
	listmake();
	check = false;
	con = con1;
	}
            for (i = 0; i < list.length; i++) {

	if(list[i]['star'] != 0 && list[i]['slot'] == con){
                    talk += "#b#L" + i + "# #i"+list[i]['itemid']+"# #z"+list[i]['itemid']+"##k #r["+slotnumber[i]+"번슬롯]#k#l\r\n"
	} else if(list[i]['slot'] == con){
                    talk += "#L" + i + "# #i"+list[i]['itemid']+"# #z"+list[i]['itemid']+"# #r["+slotnumber[i]+"번슬롯]#k#l\r\n"
	}
	}
        cm.sendSimple(talk);
    }
}

function reset(){
	list = [];
	garbage = [];
	slotnumber = [];
	check = true;
}

function listmake(){
	if (check == true){
	   jj = 0;
	} else {
	   jj = slotnumber.length;
	}
	// 위 jj값 slotnumber의 값 측정
	for(MakeValue = 0; MakeValue < 7; MakeValue++){
	con = MakeValue;
	for (i = 0; i < cm.getInventory(con).getSlotLimit(); i++) {
            if (cm.getInventory(con).getItem(i) != null) {
	   slotnumber.push(i);
	}
	}
	for (jj; jj<slotnumber.length; jj++){
	 list[jj] = new Item(cm.getInventory(con).getItem(slotnumber[jj]).getItemId(), 0, con, slotnumber[jj]);
	}
	}
}

function Item(id, va, conid, sl){
	this.itemid = id;
   	this.star = va;
	this.slot = conid;
	this.slnumber = sl;
}

function del(st, con){
   if(con == 1){
	equip = Packages.client.inventory.MapleInventoryType.EQUIP;
  } else if(con == 2){
	equip = Packages.client.inventory.MapleInventoryType.USE;
  } else if(con == 3){
	equip = Packages.client.inventory.MapleInventoryType.SETUP;
  } else if(con == 4){
	equip = Packages.client.inventory.MapleInventoryType.ETC;
  } else if(con == 5){
	equip = Packages.client.inventory.MapleInventoryType.CASH;
	if (GameConstants.isPet(cm.getInventory(5).getItem(st).getItemId())) {
                for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                    if (cm.getPlayer().getPets()[i] != null) {
                        if (cm.getPlayer().getPets()[i].getInventoryPosition() == st) {
                            cm.sendOk("장착중인 펫을 제외하고 삭제되었습니다.");
                            cm.dispose();
                            return;
                        }
                    }
                }
            }
  } else if(con == 6){
	equip = Packages.client.inventory.MapleInventoryType.DECORATION;

  }
	Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), equip, st, cm.getInventory(con).getItem(st).copy().getQuantity(), true);
}