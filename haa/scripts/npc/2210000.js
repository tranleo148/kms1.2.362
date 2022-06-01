var status = -1;

function start() {
	var aa = cm.itemQuantity(4000878); // 맨티스 전리품
	var ab = cm.itemQuantity(4000881); // 플로라 전리품
	var ac = cm.itemQuantity(4000883); // 다크 플로라 전리품
	var ad = cm.itemQuantity(4000885); // 호넷 전리품
	var ae = cm.itemQuantity(4000887); // 포이즌 호넷 전리품
        var aaa = cm.itemQuantity(4000889); // 제네럴 호넷 전리품
	    if (aa >= 50 && ab >= 50 && ac >= 50 && ad >= 50 && ae >= 50 && aaa >= 50){
		var sel = "#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n#L3##b탐험을 마치고, 아이템을 전부 모아왔습니다.#k#l\r\n\r\n";
		} else {
		sel = "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#L0##r아직 탐험을 마치지 못했습니다.#k#l\r\n\r\n";
            } 
	    if (cm.getPlayer().getMapId() == 100030301){
		var sel2 = "#L1##r[입장]#k #d바로 암벽거인 탐사 본부 로 이동하기#k#l";
		} else {
		sel2 = "#L2##r[나가기]#k #d이만.. 퀘스트의 전당 으로 이동하기#k#l";
            }
	if (cm.getPlayer().getLevel() >= 275 && cm.getPlayer().getMapId() == 100030301) {
	    var chat0 = "#fn나눔고딕 Extrabold#자네는.. 탐험을 좋아하는가?\r\n내 요즘..탐험을 다니는데 말야..\r\n"
	    chat0 += "이 곳 암벽 거인 주변 서식처가 눈에 둘어오는군..\r\n";
	    chat0 += "자네가 나 대신 전리품 수집 좀 도와주게..나를 도와주면\r\n";
	    chat0 += "내 특별히 저번 탐사 현장에서 발견한 이 것을 주도록하지..\r\n";
            chat0 += ""+sel2+"";
	    cm.sendSimple(chat0);
        } else if (cm.getPlayer().getLevel() >= 180 && cm.getPlayer().getMapId() == 240090000) {
            var chat = "#fn나눔고딕 Extrabold#자네는.. 탐험을 좋아하는가?\r\n내 요즘..탐험을 다니는데 말야..\r\n";
	    chat += "이 곳 암벽 거인 주변 서식처가 눈에 둘어오는군..\r\n";
	    chat += "자네가 나 대신 전리품 수집 좀 도와주게..나를 도와주면\r\n";
	    chat += "내 특별히 저번 탐사 현장에서 발견한 이 것을 주도록하지..\r\n";
            chat += "\r\n--------------------------------------------------------------------------------\r\n";
            if (cm.getQuestStatus(300) == 0) {
	    chat += "#r[보상]#k #i2431291# #b#z2431291##k #r3 개#k\r\n#r[보상]#k #i1122058# #b#z1122058##k #r올스탯 20 / 공, 마 20#k\r\n";
            } else {
	    chat += "#r[보상]#k #i2431291# #b#z2431291##k #r1 개#k";
            }
	    chat += "\r\n--------------------------------------------------------------------------------\r\n";
            chat += "                          #d[요청 아이템 목록]#k\r\n";
            chat += "--------------------------------------------------------------------------------\r\n";
	    chat += "#i4000878# #z4000878#                        #d("+aa+"/50)#k\r\n";
	    chat += "#i4000881# #z4000881#                       #d("+ab+"/50)#k\r\n";
	    chat += "#i4000883# #z4000883#              #d("+ac+"/50)#k\r\n";
	    chat += "#i4000885# #z4000885#                                #d("+ad+"/50)#k\r\n";
	    chat += "#i4000887# #z4000887#                    #d("+ae+"/50)#k\r\n";
	    chat += "#i4000889# #z4000889#                    #d("+aaa+"/50)#k\r\n";
	    chat += "--------------------------------------------------------------------------------\r\n";
	    chat += ""+sel+"";
	    chat += "--------------------------------------------------------------------------------\r\n";
            chat += ""+sel2+"";
	    cm.sendSimple(chat);
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold##r* 플레이 조건#k\r\n\r\n#d- 레벨 180 이상 의 캐릭터\r\n- 퀘스트의 전당 또는 암벽거인 탐사 본부 에서 플레이 가능#k",9062004);
	cm.dispose();
        }
}
function action(mode, type, selection) {
        cm.dispose();
	if (selection == 0) {
			 cm.sendOk("#fn나눔고딕 Extrabold##r자네.. 아직 탐험에 흥미가 없는것인가?..\r\n얼른 가서 전리품을 모아오게나..#k");
		         cm.dispose();
	} else if (selection == 1) {
			 cm.warp(240090000,0);
		         cm.dispose();
	} else if (selection == 2) {
			 cm.warp(100030301,0);
		         cm.dispose();
	} else if (selection == 3) {
        if (cm.getQuestStatus(300) == 0) {
	    if (cm.haveItem(4000878, 50) && cm.haveItem(4000881, 50) && cm.haveItem(4000883, 50) && cm.haveItem(4000885, 50) && cm.haveItem(4000887, 50) && cm.haveItem(4000889, 50)) {
		 if (cm.canHold(2431291) && cm.canHold(1122058)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#오호~자네! 탐험가의 소질이 있군!\r\n내 약속대로 이 것을 주겠네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i2431291# #b강화석 상자#k #r3 개#k\r\n#i1122058# #b#z1122058##k #r(올스탯 300 / 공, 마 200)#k");
			 cm.gainItem(4000878, -50);
			 cm.gainItem(4000881, -50);
			 cm.gainItem(4000883, -50);
			 cm.gainItem(4000885, -50);
			 cm.gainItem(4000887, -50);
			 cm.gainItem(4000889, -50);
			 cm.gainItem(2431291, 3);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1122058);
	item.setStr(20);
	item.setDex(20);
	item.setInt(20);
	item.setLuk(20);
	item.setWatk(20);
	item.setMatk(20);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	                 cm.forceStartQuest(300);
	                 cm.showEffect(false,"monsterPark/clear");
                         cm.playSound(false,"Field.img/Party1/Clear");
			 cm.dispose();
		} else {		         
			 cm.sendOk("#fn나눔고딕 Extrabold##r기타 또는 장비 창에 빈 공간 이 없습니다.#k");
		         cm.dispose();	
			}
	    } else {		         
		 cm.sendOk("#fn나눔고딕 Extrabold##r자네.. 아직 탐험에 흥미가 없는것인가?..\r\n얼른 가서 전리품을 모아오게나..#k");
		 cm.dispose();	
		 }
      } else {
	    if (cm.haveItem(4000878, 50) && cm.haveItem(4000881, 50) && cm.haveItem(4000883, 50) && cm.haveItem(4000885, 50) && cm.haveItem(4000887, 50) && cm.haveItem(4000889, 50) && cm.haveItem(4000891, 50) && cm.haveItem(4000892, 50) && cm.haveItem(4000893, 50) && cm.haveItem(4000894, 50)) {
		 if (cm.canHold(2437733)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#오호~자네! 오랫만이군! 결국 탐험가의 길을 걷기로 한겐가?!\r\n내 약속대로 이 것을 주겠네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i2437733# #b강화꾸러미#k #r1 개#k");
			 cm.gainItem(4000878, -50);
			 cm.gainItem(4000881, -50);
			 cm.gainItem(4000883, -50);
			 cm.gainItem(4000885, -50);
			 cm.gainItem(4000887, -50);
			 cm.gainItem(4000889, -50);
			 cm.gainItem(4000891, -50);
			 cm.gainItem(2431291, 1);
	                 cm.showEffect(false,"monsterPark/clear");
                         cm.playSound(false,"Field.img/Party1/Clear");
			 cm.dispose();
		} else {		         
			 cm.sendOk("#fn나눔고딕 Extrabold##r기타 창에 빈 공간 이 없습니다.#k");
		         cm.dispose();	
			}
	    } else {		         
		 cm.sendOk("#fn나눔고딕 Extrabold##r자네.. 아직 탐험에 흥미가 없는것인가?..\r\n얼른 가서 전리품을 모아오게나..#k");
		 cm.dispose();	
		 }
      }
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold##b부디.. 탐험의 매력을 알길 바라네..#k");
	cm.dispose();		
}
}
