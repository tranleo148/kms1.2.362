importPackage(Packages.client.inventory);
importPackage(Packages.packet.creators);
importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(java.lang);


function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S)
{
	if(M != 1)
	cm.dispose();

	else
	St++;

	if(St == 0)
	{
		SET_GIV = cm.getClient().getKeyValue("GM_SETTING_GIV");
		SET_DEL = cm.getClient().getKeyValue("GM_SETTING_DEL");

		if(!cm.getPlayer().isGM())
		{
			cm.dispose();
			return;
		}

		//log("access", "엔피시 접속 \r\n");
		cm.sendSimpleS("운영자 #b#e#h ##k#n님, 반갑습니다. 무슨 일을 도와드릴까요?\r\n#b"
			+ "#L0#캐릭터 조회하기#l\r\n"
			+ "#L1#설정 변경#l\r\n\r\n", 4);
		}

	else if(St == 1)
	{
		S1 = S;
		switch(S1)
		{

			//checkOnOff()
			case 1:
			selStr = "현재 설정은 다음과 같아요. 선택지를 누르시면 #bON#k/#rOFF#k를 변경할 수 있습니다.\r\n"
			selStr += "#L10#"+checkOnOff(SET_GIV, "color")+"아이템 지급 시 안내 공지 전송 #e("+checkOnOff(SET_GIV, "string")+")#n\r\n";
			selStr += "#L11#"+checkOnOff(SET_DEL, "color")+"아이템 제거 시 안내 공지 전공 #e("+checkOnOff(SET_DEL, "string")+")#n\r\n"; 
			cm.sendSimpleS(selStr, 4);
			break;

			case 0:
			cm.sendGetText("운영자 #b#e#h ##k#n님, 반갑습니다. 대상 캐릭터의 닉네임을 입력해 주세요. 현재 접속 중인 캐릭터만 가능합니다.");
			break;
		}
	}

	else if(St == 2)
	{
		S2 = S;

		switch(S1)
		{
			case 1: //설정 변경
			switch(S2)
			{
				case 10:
				if(SET_GIV == -1)
				cm.getClient().setKeyValue("GM_SETTING_GIV", 1);

				else
				cm.getClient().setKeyValue("GM_SETTING_GIV", -1);

				cm.getPlayer().dropMessage(5, "아이템 지급 시 안내 공지 전송 설정이 "+checkOnOff(SET_GIV * -1, "string")+"으로 변경됐습니다.");
				break;

				case 11:
				if(SET_DEL == -1) //OFF
				cm.getClient().setKeyValue("GM_SETTING_DEL", 1);

				else
				cm.getClient().setKeyValue("GM_SETTING_DEL", -1);

				cm.getPlayer().dropMessage(5, "아이템 제거 시 안내 공지 전송 설정이 "+checkOnOff(SET_DEL * -1, "string")+"으로 변경됐습니다.");
1
				break;
			}
			cm.dispose();
			cm.openNpc(9010017);
			break;

			case 0:
			name = cm.getText();
			ch = Packages.handling.world.World.Find.findChannel(name);
			if (ch >= 0)
			{
				chr = Packages.handling.channel.ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
				if (chr != null)
				{
					//log("access", ""+name+"("+chr.getId()+") 캐릭터에 접속\r\n");
					cm.sendSimpleS("#b#e"+chr.getName()+"#k#n 캐릭터에 접속했습니다. 무엇을 도와드릴까요?#b\r\n\r\n#fn돋움체##fs11#"
						+ "   - 보유 메소 량 | #fn돋움#"+Comma(chr.getMeso())+" (10억 주머니 : "+Comma(chr.itemQuantity(4001716))+"개)#fn돋움체#\r\n"
						+ "   - 보유 해피 코인 량 | "+Comma(chr.itemQuantity(4310237))+"개\r\n"
						+ "   - 보유 네오 스톤 | "+Comma(chr.getKeyValue(100711, "point"))+"\r\n"
						+ "   - 보유 네오 잼 | "+Comma(chr.getKeyValue(100712, "point"))+"\r\n"
						+ "   - 보유 네오 코어 | "+Comma(chr.getKeyValue(501215, "point"))+"\r\n"
						+ "   - 보유 홍보 포인트 | "+Comma(chr.getHPoint())+"\r\n"
						+ "   - 보유 후원 포인트 | "+Comma(chr.getDonationPoint())+"\r\n#fs12##fn돋움#"
						//+ "#L16#홍보포인트 지급#l #L17#후원포인트 지급#l\r\n"
						+ "#L13#네오스톤 지급#l#L14#네오잼 지급#l#L15#네오코어 지급#l\r\n\r\n"
						+ "#L0#장착 중인 인벤토리 확인하기#l\r\n"
						+ "#L1#장비 인벤토리 확인하기#l\r\n"
						+ "#L2#소비 인벤토리 확인하기#l\r\n"
						+ "#L3#설치 인벤토리 확인하기#l\r\n"
						+ "#L4#기타 인벤토리 확인하기#l\r\n"
						+ "#L5#캐시 인벤토리 확인하기#l\r\n"
						+ "#L6#치장 인벤토리 확인하기#l\r\n\r\n"
						+ "#L7#창고 확인하기#l\r\n\r\n"
						+ "#L10#캐릭터에게 메시지 보내기#l\r\n"
						+ "#L11#캐릭터에게 아이템 지급하기#l\r\n"
						+ "#L12#매크로 조회#l\r\n", 4);
				}
				else
				{
					cm.sendOk("캐릭터에 접근할 수 없습니다. 아마도 채널 이동 중이거나, 접속 종료 중일 수도 있습니다.");
					cm.dispose();
					return;
				}
			}
			else
			{
				cm.sendOk("#b#e"+name+"#k#n 캐릭터는 현재 접속 중이 아닙니다.");
				//log("fail", "조회 실패	"+name+"	미접속\r\n");
				cm.dispose();
				return;
			}
		}
	}

	else if(St == 3)
	{
		S3 = S;
		switch(S1)
		{

			case 0:
			if(chr == null)
			{
				cm.sendOkS("해당 캐릭터가 접속 등을 종료하여 연결이 끊어졌습니다.");
				//log("fail", "조회 실패	"+name+"	조회 중 접속 종료\r\n");
				cm.dispose();
				return;
			}

			if(S3 == 999)
			{
				chr.addKV("Hard_Will", "0");
				chr.dropMessage(5, "횟수가 복구되었습니다.");
				cm.dispose();
				return;
			}

			if(S3 == 10)
			{
				cm.sendGetText(""+chr.getName()+"에게 보낼 메시지를 입력해 주세요. 너무 길면 취소될 수 있습니다.");
				return;
			}

			if(S3 == 11)
			{
				cm.sendGetNumber(""+chr.getName()+"에게 보낼 아이템 코드를 입력해주세요.", 0, 0, 5999999);
				return;
			}


			if(S3 == 12)
			{
				if(chr == null)
				{
					cm.sendOkS("해당 캐릭터가 접속 등을 종료하여 연결이 끊어졌습니다.");
					//log("fail", "조회 실패	"+name+"	조회 중 접속 종료\r\n");
					cm.dispose();
					return;
				}

				cm.dispose();
				cm.openNpcCustom2(chr.getClient(), 2007, "macro");
				cm.getPlayer().dropMessage(5, ""+name+" 캐릭터에게 매크로 테스트를 실시했습니다.");
				return;
			}

			if(S3 >= 13 && S3 <= 15)
			{
				cm.sendGetNumber(""+chr.getName()+"에게 지급할 갯수를 입력해주세요.", 1, 1, 9999);
				return;
			}

			if(S3 >= 16 && S3 <= 17)
			{
				cm.sendGetNumber(""+chr.getName()+"에게 지급할 포인트를 입력해주세요.", 1, 1, 999999);
				return;
			}

			if(S3 == 7)
			{
				//log("access", ""+name+"("+chr.getId()+") 캐릭터의 창고에 접속\r\n");

				selStr = "#b#e"+chr.getName()+"#k#n 캐릭터의 #e#r창고#k#n에 접속했습니다. 창고 아이템은 조회만 가능합니다.\r\n\r\n#b";

				for (w = 0; w < chr.getStorage().getItems().size(); w++)
				selStr += "#i"+chr.getStorage().getItems().get(w).getItemId()+"# #z"+chr.getStorage().getItems().get(w).getItemId()+"# #e"+chr.getStorage().getItems().get(w).getQuantity()+"개#n\r\n";

				cm.sendOkS(selStr, 4);
				cm.dispose();
				return;
			}

			if(S3 < 7) // 인벤토리
			{
				invType = S3 == 0 ? "장착" : S3 == 1 ? "장비" : S3 == 2 ? "소비" : S3 == 3 ? "설치" : S3 == 4 ? "기타" : S3 == 5 ? "캐시" : "치장";

				//log("access", ""+name+"("+chr.getId()+") 캐릭터의 "+invType+" 인벤토리에 접속\r\n");

				selStr = "#b#e"+chr.getName()+"#k#n 캐릭터의 #e#r"+invType+"#k#n 인벤토리에 접속했습니다. 어떤 아이템을 확인하시겠습니까?\r\n#b";
				inv = (S3 != 0) ? chr.getInventory(S3) : chr.getInventory(MapleInventoryType.EQUIPPED);

				if(S3 != 0)
				{
					for(z = 0; z < inv.getSlotLimit(); z++)
					{
						if(inv.getItem(z) == null)
						continue;
	
						selStr += "#L"+z+"# #i"+inv.getItem(z).getItemId()+":# #t"+inv.getItem(z).getItemId()+":# (수량 : "+inv.getItem(z).getQuantity()+"개)\r\n";
					}
				}
				else
				{
					for(z = 0; z < 100; z++)
					{
						a = -1 * z;
						if(inv.getItem(a) == null)
						continue;
	
						selStr += "#L"+z+"# "+z+". #i"+inv.getItem(a).getItemId()+":# #t"+inv.getItem(a).getItemId()+":#\r\n";

						if(z > 100)
						break;
					}

				}
				cm.sendSimpleS(selStr, 4);
			}
			else
			{
				cm.sendOk("해당 기능은 미구현입니다.");
				cm.dispose();
			}
			break;
		}
	}

	else if(St == 4)
	{
		S4 = (S3 != 0) ? S : -1 * S;
		switch(S1)
		{
			case 0:
			if(chr == null)
			{
				cm.sendOkS("해당 캐릭터가 접속 등을 종료하여 연결이 끊어졌습니다.");
				//log("fail", "조회 실패	"+name+"	인벤토리 조회 중 접속 종료\r\n");
				cm.dispose();
				return;
			}

			if(S3 < 10) // 인벤토리
			{
				//log("access", ""+name+"("+chr.getId()+") 캐릭터의 "+invType+" 인벤토리의 "+inv.getItem(S4).getItemId()+" 아이템에 접속\r\n");

				selStr = "#b#e"+chr.getName()+"#k#n 캐릭터의 #e#r"+invType+"#k#n 인벤토리에 접속했습니다.\r\n";
				selStr += "현재 선택한 아이템은 #e#b#t"+inv.getItem(S4).getItemId()+":##k#n 입니다.\r\n#b"; 
				if(S3 < 2) //장비
				{
					selStr += "#L20010#선택한 아이템 내 인벤토리로 #e복사#n하기#l\r\n";

					if(S3 == 1)
					selStr += "#L20000#선택한 아이템 내 인벤토리로 #r#e삭제#n#b하기#l";

					selStr += "\r\n\r\n\r\n#e[상세스탯]#k#n\r\n#fs11#";
					selStr += "  - 힘 #e+"+inv.getItem(S4).getStr()+"#n, 덱스 #e+"+inv.getItem(S4).getDex()+"#n, 인트 #e+"+inv.getItem(S4).getInt()+"#n, 럭 #e+"+inv.getItem(S4).getLuk()+"#n\r\n"
					selStr += "  - 공격력 #e+"+inv.getItem(S4).getWatk()+"#n, 마력 #e+"+inv.getItem(S4).getMatk()+"#n\r\n";
					selStr += "  - 특수옵션 상태 : #e"+inv.getItem(S4).getOwner()+"#n\r\n";

					selStr += "\r\n\r\n#e#fs12##b[잠재능력]#k#n\r\n#fs11#";
					selStr += "  - 1번째 줄 : #e"+toString(inv.getItem(S4).getPotential1())+"#n\r\n";
					selStr += "  - 2번째 줄 : #e"+toString(inv.getItem(S4).getPotential2())+"#n\r\n";
					selStr += "  - 3번째 줄 : #e"+toString(inv.getItem(S4).getPotential3())+"#n\r\n";
					selStr += "  - 4번째 줄 : #e"+toString(inv.getItem(S4).getPotential4())+"#n\r\n";
					selStr += "  - 5번째 줄 : #e"+toString(inv.getItem(S4).getPotential5())+"#n\r\n";
					selStr += "  - 6번째 줄 : #e"+toString(inv.getItem(S4).getPotential6())+"#n\r\n";


					cm.sendSimpleS(selStr, 4);
				}
				else
				{
					selStr += "\r\n#k현재 해당 슬롯에 #e#r"+inv.getItem(S4).getQuantity()+"개#k#n를 보유하고 있습니다.\r\n#e#r삭제할 수량을 입력해 주세요.";
					cm.sendGetNumber(selStr, 0, 1, inv.getItem(S4).getQuantity());
				}
			}

			if(S3 == 10)
			{
				txt = cm.getText();
				if(txt.contains("cm") || txt.contains("Packages"))
				{
					cm.dispose();
					return;
				}
				selStr  = "정말 #e"+name+"#n 에게 아래의 메시지를 보내겠습니까? 메시지 전달 유형을 선택하세요.\r\n\r\n#d"+txt+"#r\r\n";
				selStr += "#L5#분홍색 공지#l\r\n";
				selStr += "#L6#파란색 공지\r\n";

				if(txt.length < 40)
				selStr += "#L1#팝업 공지사항 (40자 이내만 전송 가능)#l\r\n";
				cm.sendSimpleS(selStr, 4);
			}

            if(S3 == 11)
			{
				number = S4;
				ii = Packages.server.MapleItemInformationProvider.getInstance();
				if (!ii.itemExists(number)) {
				    cm.sendOk("존재하지 않는 아이템입니다.");
				    cm.dispose();
				    return;
				}
				cm.sendGetNumber(""+chr.getName()+"에게 보낼 아이템 갯수를 입력해주세요.", 1, 1, 32767);
				return;
			}
            if(S3 >= 13 && S3 <= 15)
			{
				number = S4;
                if(S3 == 13){  
                    txt = name+" 캐릭터에게 네오스톤을 "+number+"개를 지급하였습니다.";
                    chr.setKeyValue(100711, "point", chr.getKeyValue(100711, "point")+number);
                } else if(S3 == 14) {
                    txt = name+" 캐릭터에게 네오 잼을 "+number+"개를 지급하였습니다.";
                    chr.setKeyValue(100712, "point", chr.getKeyValue(100712, "point")+number);
                } else if(S3 == 15) {
                    txt = name+" 캐릭터에게 네오 코어를 "+number+"개를 지급하였습니다.";
                    chr.setKeyValue(501215, "point", chr.getKeyValue(501215, "point")+number);
                }
                cm.getPlayer().dropMessage(6, txt);
                cm.dispose();
				return;
			}
            if(S3 >= 16 && S3 <= 17)
			{
				number = S4;
                if(S3 == 13){  
                    txt = name+" 캐릭터에게 홍보포인트를 "+number+"P 지급하였습니다.";
                    chr.setKeyValue(100711, "point", chr.getKeyValue(100711, "point")+number);
                } else if(S3 == 14) {
                    txt = name+" 캐릭터에게 후원포인트를 "+number+"P 지급하였습니다.";
                    chr.setKeyValue(100712, "point", chr.getKeyValue(100712, "point")+number);
                }
                cm.getPlayer().dropMessage(6, txt);
                cm.dispose();
				return;
			}
			break;
		}
	}

	else if(St == 5)
	{
		S5 = S;
		switch(S1)
		{
			case 0:
			if(chr == null)
			{
				cm.sendOkS("해당 캐릭터가 접속 등을 종료하여 연결이 끊어졌습니다.");
				//log("fail", "조회 실패	"+name+"	인벤토리 조회 중 접속 종료\r\n");
				cm.dispose();
				return;
			}

			if(S3 < 10) // 인벤토리
			{
				if(S5 != 20010) // 복사가 아니라면,
				{
					selStr = "\r\n#e#r삭제 이유#k#n를 입력해 주세요. 아래의 번호를 입력하면 내용이 자동으로 작성됩니다.\r\n\r\n기타 사유가 있을 경우 직접 작성해주세요.\r\n";
					selStr += "#e1 : #n오지급으로 인한 아이템 회수\r\n"
					selStr += "#e2 : #n비정상적인 경로로 획득한 아이템 회수\r\n"
					selStr += "#e3 : #n거래 사기로 획득한 아이템 회수\r\n\r\n";
				}
				else
				{
					selStr = "\r\n#e#r복사 이유#k#n를 입력해 주세요. 아래의 번호를 입력하면 내용이 자동으로 작성됩니다.\r\n\r\n기타 사유가 있을 경우 직접 작성해주세요.\r\n";
					selStr += "#e4 : #n거래 사기로 획득한 아이템 회수\r\n"
					selStr += "#e5 : #n비정상적인 경로로 획득한 아이템 보존\r\n"
				}
				cm.sendGetText(selStr);
			}


			if(S3 == 10) // 공지전송
			{

				//log("notice", "전송 성공	"+name+"	"+S5+"	"+txt+"\r\n");

				chr.dropMessage(S5, txt);
				cm.getPlayer().dropMessage(5, ""+name+"에게 공지를 전송하였습니다. 다음 작업을 진행해주세요.");
				cm.dispose();
				cm.openNpc(9010017);
				return;
			}
			if(S3 == 11)
			{
				count = S5;
				if (count > 32767 || count <= 0) {
				    cm.sendOk("잘못된 수치입니다. 다시 입력해주세요.");
				    cm.dispose();
				    return;
				}
				if (!Packages.server.MapleInventoryManipulator.checkSpace(chr.getClient(), number, count, "")) {
				    cm.sendOk("대상의 인벤토리에 공간이 부족합니다.");
				    cm.dispose();
				    return;
				}
				if (count > 1 && Math.floor(number / 1000000) == 1) {
				    for (i = 0; i < count; ++i) {
					chr.gainItem(number, 1);
				    }
				} else if (Packages.constants.GameConstants.isPet(number)) {
				    Packages.server.MapleInventoryManipulator.addId(chr.getClient(), number, 1, "", Packages.client.inventory.MaplePet.createPet(number, -1), 1, "", false);
				} else {
				    chr.gainItem(number, count);
				}
				cm.getPlayer().dropMessage(5, ""+name+"에게 아이템을 전송하였습니다. 다음 작업을 진행해주세요.");
				cm.dispose();
				cm.openNpc(9010017);
				return;
			}
		}
	}
	else if(St == 6)
	{
		S6 = S;
		switch(S1)
		{
			case 0:

			if(chr == null)
			{
				cm.sendOkS("해당 캐릭터가 접속 등을 종료하여 연결이 끊어졌습니다.");
				//log("fail", "조회 실패	"+name+"	인벤토리 수정 중 접속 종료\r\n");
				cm.dispose();
				return;
			}

			if(S3 < 10) // 인벤토리
			{
				REASON = function() {
						switch(cm.getText())
						{
							case "1": return "오지급으로 인한 아이템 회수";
							case "2": return "비정상적인 경로로 인한 아이템 회수";
							case "3": return "거래 사기로 획득한 아이템 회수";
							case "4": return "거래 사기로 획득한 아이템 회수";
							case "5": return "비정상적인 경로로 획득한 아이템 보존";
							default: return cm.getText();
						}
					}
			
				TYPES = S3 == 0 ? MapleInventoryType.EQUIPPED :
					S3 == 1 ? MapleInventoryType.EQUIP : 
					S3 == 2 ? MapleInventoryType.USE :
					S3 == 3 ? MapleInventoryType.SETUP :
					S3 == 4 ? MapleInventoryType.ETC :
					MapleInventoryType.CASH;


				switch(S5)
				{
					case 20000: // 삭제하기
					reason = S5 == 20000 ? "아이템 오지급" : S5 == 20001 ? "아이템 회수" : "기타";

					if(SET_DEL == 1)
					chr.dropMessage(5, " ");
					chr.dropMessage(5, "[안내] "+cm.getItemNameById(inv.getItem(S4).getItemId())+" 아이템이 '"+REASON()+"'의 사유로 삭제되었습니다.");
					chr.dropMessage(5, " ");

					//log("success", "삭제	"+chr.getId()+"	"+name+"	"+invType+"	"+inv.getItem(S4).getItemId()+"	1개	"+REASON()+"\r\n");
					MapleInventoryManipulator.removeFromSlot(chr.getClient(), TYPES, S4, 1, false);
					cm.sendOkS("아이템 삭제가 완료되었습니다.", 4);

	 
					cm.dispose();
					break;

					case 20010: // 내 인벤토리로 복사하기
					//log("success", "복사	"+chr.getId()+"	"+name+"	"+invType+"	"+inv.getItem(S4).getItemId()+"	1개	"+REASON()+"\r\n");
					items = inv.getItem(S4).copy();
					MapleInventoryManipulator.addFromDrop(cm.getClient(), items, true);
					cm.sendOkS("아이템 복사가 완료되었습니다.", 4);

					cm.dispose();
					break;

					default:

					if(SET_DEL == 1)
					chr.dropMessage(5, " ");
					chr.dropMessage(5, "[안내] "+cm.getItemNameById(inv.getItem(S4).getItemId())+" 아이템 "+S5+"개가 '"+REASON()+"'의 사유로 삭제되었습니다.");
					chr.dropMessage(5, " ");

					//log("success", "삭제	"+chr.getId()+"	"+name+"	"+invType+"	"+inv.getItem(S4).getItemId()+"	"+S5+"개	"+REASON()+"\r\n");
					MapleInventoryManipulator.removeFromSlot(chr.getClient(), TYPES, S4, S5, false);

					cm.sendOkS("아이템 삭제가 완료되었습니다.", 4);
					cm.dispose();

				}
			}
		}
	}
}

function checkOnOff(i, type)
{
	if(i == -1) // off
	{
		switch(type)
		{
			case "color":
			return "#r";

			case "string":
			return "OFF";
		}
	}
	else
	{
		switch(type)
		{
			case "color":
			return "#b";

			case "string":
			return "ON";
		}
	}
}

//내용 부분 입력
function log(type, i)
{
	switch(type)
	{
		//추가 내용 필요 없음
		case "access": //접속 시
		fn = "1_Access.txt";
		break;

		//추가 내용 : interaction, fail reason
		case "fail": //실패 시
		fn = "2_Failure.txt";
		break;

		//추가 내용 : interaction, 공지 여부
		case "success": //성공 시
		fn = "3_Success.txt";
		break;

		case "notice": //공지사항
		fn = "4_notice.txt";
		break;
	}

	fw = new java.io.FileWriter("SearchingLog/" + fn, true);
	fw.write(new Date() + "	" + cm.getPlayer().getId() + "	" + cm.getPlayer().getName() + "	" + i);
	fw.close();
}

function toString(i)
{
	switch(i)
	{
		case 0:
		return "#Cgray#미설정#k";

		case 10041:
		case 20041:
		case 30041:
		case 40041:
		return "STR%";

		case 10042:
		case 20042:
		case 30042:
		case 40042:
		return "DEX%";

		case 10043:
		case 20043:
		case 30043:
		case 40043:
		return "INT%";

		case 10044:
		case 20044:
		case 30044:
		case 40044:
		return "LUK%";

		case 20086:
		case 30086:
		case 40086:
		return "올스탯%";

		case 10045:
		case 20045:
		case 30045:
		case 40045:
		return "최대 HP%";

		case 10046:
		case 20046:
		case 30046:
		case 40046:
		return "최대 MP%";

		case 10070:
		case 20070:
		case 30070:
		case 40070:
		return "데미지%";

		case 10051:
		case 20051:
		case 30051:
		case 40051:
		return "공격력%";

		case 10052:
		case 20052:
		case 30052:
		case 40052:
		return "마력%";

		case 30601:
		case 40601:
		case 30602:
		case 40602:
		case 40603:
		return "보스 공격 시 데미지%";

		case 10291:
		case 20291:
		case 30291:
		case 40291:
		case 40292:
		return "몬스터 방어력 무시%";

		case 10055:
		case 20055:
		case 30055:
		case 40055:
		return "크리티컬 확률%";

		case 40056:
		return "크리티컬 데미지%";

		case 40650:
		return "메소 획득량%";

		case 40656:
		return "아이템 획득확률%";
	
		case 20366:
		case 30366:
		case 40366:
		return "피격 시 무적시간+";

		case 40556:
		return "재사용 대시기간 -1초";

		case 40557:
		return "재사용 대시기간 -2초";

		default:
		return i + "";
	}
}

function Comma(i)
{
	var reg = /(^[+-]?\d+)(\d{3})/;
	i+= '';
	while (reg.test(i))
	i = i.replace(reg, '$1' + ',' + '$2');
	return i;
}