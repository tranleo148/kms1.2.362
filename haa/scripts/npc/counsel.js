importPackage(Packages.handling.channel);

function start()
{
	status = -1;
	action (1, 0, 0);	
}

function action(mode, type, selection)
{
	if (mode == -1)
	{
		cm.dispose();
		return;
	}
	if (mode == 0)
	{
		status --;
	}
	if (mode == 1)
	{
		status++;
	}

	if (status == 0)
	{
		i = 0;
		for (j = 0; j < ChannelServer.getAllInstances().size(); j++) {
        		i += ChannelServer.getAllInstances().get(j).getPlayerStorage().getAllCharacters().size();
        	}
		txt =   "  #e#bKMS Pixi#k#n 현재 #e#r" + i + "명#k#n 접속중입니다. #L99#마을이동#l\r\n\r\n"
			+ "#e　 　육성　　　　５차　　　　상점　　　　편의　#n\r\n#fc0xFF00ABC0#"
			+ " #L00#이동하기#l   #L10#전직하기#l   #L20#기본상점#l   #L30#길드업무#l\r\n"
			+ " #L01#환생하기#l   #L11#매트릭스#l   #L21#환생상점#l   #L31#이름변경#l\r\n"
			+ " #L02#서버도움#l   #L12#심볼구매#l   #L22#코인상점#l   #L32#도박하기#l\r\n"
			+ " #L03#훈장업적#l   #L13#심볼확장#l   #L23#홍보상점#l   #L33#랭킹조회#l\r\n"
			+ " 　　　　   　　　　   　　   #L24#후원상점#l   #L34#보조무기#l\r\n"
			+ " #k#e　 　코디　　　　기타\r\n#d#n"
			+ " #L40#캐시장비#l   #L50#포켓제작#l   　 #k#e　안내　     　확장#n#d\r\n"
			+ " #L41#헤어성형#l   #L51#장비제작#l   #L60##r후원안내#d   #L70#어빌리티#l\r\n"
			+ " #L42#믹스염색#l   #L52#창고이용#l   #L61##r홍보안내#d   #L71#포켓슬롯#l\r\n"
			if (cm.getPlayer().isGM())
			{
			txt += "#k\r\n#e　 　후원　　　　홍보　　　　템작　　　　키값　#n\r\n"
			+ " #L100#후원보상#l   #L101#홍보보상#l   #L102##r템작하기#d   #L103#키값설정#l\r\n"
			}
		cm.sendSimpleS(txt, 2);
	}

	else if (status == 1)
	{
		cm.dispose();
		switch(selection)
		{
			/* 운영자 */
			case 100:
			cm.openNpc(9900004);
			break;

			case 101:
			cm.openNpc(9900000);
			break;

			case 102:
			cm.openNpc("test1");
			break;

			case 103:
			cm.openNpc(9900002);
			break;

			/* 육성 */
			case 0:
			cm.openNpc("victoria_taxi");
			break;

			case 1:
			cm.openNpc(3003278);
			break;

			case 2:
			cm.openNpc("rising_sygnus");
			break;

			case 3:
			cm.openNpc(9000066);
			break;

			/* ５차 */
			case 10:
			if(cm.getPlayer().getLevel() < 200)
			{
				cm.sendOkS("그대는 아직 새로운 힘을 얻을 준비가 되지 않았군… 적어도 200 레벨은 되어야 새로운 가능성을 볼 수 있을 것이오.", 4, 2140001);
				return;
			}
			if(cm.getPlayer().getQuestStatus(1465) == 2)
			{
				cm.sendOkS("그대는 이미 새로운 힘과 새로운 가능성을 얻었소. 그것을 사용하는 일은 자네의 몫이라오.", 4, 2140001);
				return;
			}			
			cm.sendOkS("이제 그대는 새로운 힘을 얻을 준비가 되어있군… 내가 그대의 힘에 새로운 가능성을 일깨워주었소.", 4, 2140001);
			cm.forceCompleteQuest(1465);
			break;

			case 11:
			cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.openUI(1131));
			break;

			case 12:
			cm.openNpc(1052232);
			break;

			case 13:
			if(cm.getPlayer().getLevel() < 225)
			{
				cm.sendOkS("용사여, 아직은 때가 아니라네. #b225 레벨#k이 된 후 다시 오게나.", 4, 1540945);
				return;
			}
			if(cm.getPlayer().getQuestStatus(34478) == 2)
			{
				cm.sendOkS("용사여, 그대는 나의 힘을 잘 이용하고 있는 것 같군. 지금 가지고 있는 힘을 잘 활용해보게나.", 4, 1540945);
				return;
			}
			cm.sendOkS("용사여, 나의 힘을 그대에게 주겠네. 내가 자네에게 준 새로운 가능성을 믿네.", 4, 1540945);
			cm.forceCompleteQuest(34478);
			break;

			/* 상점 */
			case 20:
			cm.openNpc("npc_2520001");
			break;

			case 21:
			cm.openNpc("gateKeeper");
			break;

			case 22:
			cm.openNpc(1530060);
			break;

			case 23:
			cm.openShop(4);
			break;

			case 24:
			cm.openShop(5);
			break;

			/* 편의 */
			case 30:
			cm.openNpc(2010008);
			break;

			case 31:
			cm.openNpc("characterNameChange");
			break;

			case 32:
			cm.openNpc(2074019);
			break;

			case 33:
			cm.openNpc(2007);
			break;

			case 34:
			cm.openNpc("2011Haloween");
			break;

			/* 뷰티 */
			case 40:
			cm.openNpc("levelUP2");
			break;

			case 41:
			cm.openNpc(1012001);
			break;

			case 42:
			cm.openNpc(9000161);
			break;

			/* 제작 */
			case 50:
			cm.openNpc(9062000);
			break;

			case 51:
			cm.openNpc(1530541);
			break;
			
			case 52:
			cm.openNpc(1022005);
			break;

			/* 안내 */
			case 60:
			cm.openNpc(1540405);
			break;

			case 61:
			cm.openNpc("npc_9010057");
			break;

			/* 확장 */
			case 70:
			cm.openNpc(9000100);
			break;

			case 71:
			if(cm.getPlayer().getQuestStatus(6500) == 2)
			{
				cm.sendOkS("당신은 이미 모든 것을 갖춘 패션 피플입니다. 더 멋진 아이템을 구해 주머니에 넣어 당신만의 패션을 완성시킨다면 더 멋질 거예요.", 4, 1012117);
				return;
			}

			cm.forceCompleteQuest(6500);
			cm.sendOkS("진정한 멋쟁이라면 보이는 곳뿐만 아니라 보이지 않는 곳, 주머니 안쪽까지 신경을 쓰는 법이죠. 주머니 안에 뭘 넣느냐에 따라 자신의 인상이 달라진다는 것! 잊지 마세요. 보이지 않는 곳까지 세심하게 연출하는 것이야말로 패션의 완성이죠.", 4, 1012117);
			break;

			default:
			cm.getPlayer().dropMessage(1, "현재 이용할 수 없습니다.");
			break;
		}
	}
}
