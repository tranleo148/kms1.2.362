importPackage(java.lang);
importPackage(java.io);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S)
{
	if(M != 1)
	{
		cm.dispose();
		return;
	}

	if(M == 1)
	St++;

	if(St == 0)
	{
		if(!cm.haveItem(2633552))
		{
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getJob() == 10112) {

		cm.sendOkS("#fs11##fc0xFF000000#자네의 여정에 힘이 됐으면 하는 마음에 #fc0xFFFF3300##e쓸만한 옵션#fc0xFF000000##n을 부여해놨다네! #fc0xFF990033#[ZERO]#fc0xFF000000#에서 즐거운 시간 보내시게\r\n#b"

			+ "  #i3700503:# #t3700503:#\r\n"
			+ "  #i1122372:# #t1122372:#\r\n"
			+ "  #i5002239:# #t5002239:# (자석펫)\r\n"
			+ "  #i2430732:# #t2430732:# (특수 보조장비(소비칸))\r\n"
			+ "  #i"+S+":# #t"+S+":# #e#k#n\r\n", 4, 9062294);
		               cm.gainItem(2633552, -1);
		               cm.gainItem(3700503, 1);
		               cm.gainItem(2430732, 1);

		Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002239, 1, "", Packages.client.inventory.MaplePet.createPet(5002239, -1), 15, "", false);

	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1122372);
	item.setStr(item.getStr() + 20);
	item.setDex(item.getDex() + 20);
	item.setInt(item.getInt() + 20);
	item.setLuk(item.getLuk() + 20);
	item.setWatk(item.getWatk() + 30);
	item.setMatk(item.getMatk() + 30);
	item.setEnhance(item.getEnhance() + 15);
	item.setHp(item.getHp() + 2000);
	item.setMp(item.getMp() + 2000);
	item.setState(19);
	item.setPotential1(30055);
	item.setPotential2(40086);
	item.setPotential3(40086);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
		cm.dispose();
	}

		wList = [];
		getWeapon(cm.getPlayer().getJob());
		selStr = "#fs 11##fc0xFF000000#버닝 캐릭터를 위한 지원장비일세! #b직업군에 맞는 무기#fc0xFF000000#를 골라보게나.\r\n";
		for(i = 0; i < wList.length; i++)
		{
			selStr += "#b#L"+wList[i]+"##i"+wList[i]+":# #t"+wList[i]+":##l\r\n";
		}

		cm.sendSimpleS(selStr, 4, 9062294);
		
	}

	else if(St == 1)
	{
		if(S == 0 || S == 1213020)
		{
			cm.getPlayer().dropMessage(5, "상자 사용을 취소했습니다.");
			cm.dispose();
			return;
		}

		cm.sendOkS("#fs11##fc0xFF000000#자네의 여정에 힘이 됐으면 하는 마음에 #fc0xFFFF3300##e쓸만한 옵션#fc0xFF000000##n을 부여해놨다네! #fc0xFF990033#[ZERO]#fc0xFF000000#에서 즐거운 시간 보내시게\r\n#b"
			+ "  #i3700503:# #t3700503:#\r\n"
			+ "  #i1122372:# #t1122372:#\r\n"
			+ "  #i5002239:# #t5002239:# (자석펫)\r\n"
			+ "  #i2430732:# #t2430732:# (특수 보조장비(소비칸))\r\n"
			+ "  #i"+S+":# #t"+S+":# #e#k#n\r\n", 4, 9062294);
			if (wList.indexOf(S) == -1) {
				cm.sendOk("비정상적인 접근");
				cm.dispose();
				return;
			}
		               addOption(S, true);
		               cm.gainItem(2633552, -1);
		               cm.gainItem(3700503, 1);
		               cm.gainItem(2430732, 1);

		Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002239, 1, "", Packages.client.inventory.MaplePet.createPet(5002239, -1), 15, "", false);

	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1122372); //도미
	item.setStr(item.getStr() + 20);
	item.setDex(item.getDex() + 20);
	item.setInt(item.getInt() + 20);
	item.setLuk(item.getLuk() + 20);
	item.setWatk(item.getWatk() + 30);
	item.setMatk(item.getMatk() + 30);
	item.setEnhance(item.getEnhance() + 15);
	item.setHp(item.getHp() + 2000);
	item.setMp(item.getMp() + 2000);
	item.setState(19);
	item.setPotential1(30055);
	item.setPotential2(40086);
	item.setPotential3(40086);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
			cm.dispose();
			return;

	}
}

function isMagician(i)
{
	switch(Math.floor(i / 100))
	{
			case 2:
			case 12:
			case 22:
			case 27:
			case 32:
			case 142:
			case 152:
			case 162:
			return true;
			break;

			default:
			return false;
			break;
	}
}

function getWeapon(i)
{
	switch(Math.floor(cm.getPlayer().getJob()))
	{
		/* 전사 */
		case 100:
		wList.push(1302344); // 한손검
		wList.push(1312204); // 한손도끼
		wList.push(1322256); // 한손둔기
		wList.push(1402260); // 두손검
		wList.push(1412182); // 두손도끼
		wList.push(1422190); // 두손둔기
		wList.push(1432219); // 창
		wList.push(1442276); // 폴암
		break;

		case 110: //히어로
		case 111:
		case 112:
		wList.push(1302344); // 한손검
		wList.push(1402260); // 두손검
		break;

		case 120: //팔라딘
		case 121:
		case 122:
		wList.push(1302344); // 한손검
		wList.push(1312204); // 한손도끼
		wList.push(1322256); // 한손둔기
		wList.push(1402260); // 두손검
		wList.push(1412182); // 두손도끼
		wList.push(1422190); // 두손둔기
		break;

		case 130: //다크 나이트
		case 131:
		case 132:
		wList.push(1432219); // 창
		wList.push(1442276); // 폴암
		break;

		case 1100: //소울 마스터
		case 1110:
		case 1111:
		case 1112:
		wList.push(1402260); // 두손검
		break;

		case 2100:
		case 2110:
		case 2111:
		case 2112:
		wList.push(1442276); // 폴암
		break;

		case 3100:
		case 3110:
		case 3111:
		case 3112:
		case 3101:
		case 3120:
		case 3121:
		case 3122:
		wList.push(1312204); // 한손도끼
		wList.push(1322256); // 한손둔기
		wList.push(1232114); // 데스페라도
		break;

		case 3700:
		case 3710:
		case 3711:
		case 3712:
		wList.push(1582027); // 건틀렛 리볼버
		break;

		case 5100:
		case 5110: //미하일
		case 5111:
		case 5112:
		wList.push(1302344); // 한손검
		break;

		case 6100:
		case 6110:
		case 6111:
		case 6112:
		wList.push(1402260); // 두손검
		break;

		case 15002:
		case 15100:
		case 15110:
		case 15111:
		case 15112:
		wList.push(1213028); // 튜너
		break;

		/* 마법사 */
		case 200:
		case 210:
		case 211:
		case 212:
		case 220:
		case 221:
		case 222:
		case 230:
		case 231:
		case 232:
		case 1200:
		case 1210:
		case 1211:
		case 1212:
		case 2200:
		case 2210:
		case 2211:
		case 2212:
		wList.push(1372229); // 완드
		wList.push(1382266); // 스태프
		break;

		case 2700:
		case 2710:
		case 2711:
		case 2712:
		wList.push(1212121); // 샤이닝로드
		break;

		case 3200:
		case 3210:
		case 3211:
		case 3212:
		wList.push(1382266); // 스태프
		break;

		case 14200:
		case 14210:
		case 14211:
		case 14212:
		wList.push(1262040); // ESP 리미터
		break;

		case 15200:
		case 15210:
		case 15211:
		case 15212:
		wList.push(1282022); // 매직 건틀렛
		break;

		/* 궁1수 */
		case 300:
		wList.push(1452258); // 활
		wList.push(1462244); // 석궁
		break;

		case 310:
		case 311:
		case 312:
		case 1300:
		case 1310:
		case 1311:
		case 1312:
		wList.push(1452258); // 활
		break;

		case 320:
		case 321:
		case 322:
		case 3300:
		case 3310:
		case 3311:
		case 3312:
		wList.push(1462244); // 석궁
		break;

		case 301:
		case 330:
		case 331:
		case 332:
		wList.push(1592028); // 에인션트 보우
		break;

		case 2300:
		case 2310:
		case 2311:
		case 2312:
		wList.push(1522144); // 듀얼 보우건
		break;

		case 400:
		case 410:
		case 411:
		case 412:
		case 420:
		case 421:
		case 422:
		wList.push(1332280); // 단검
		wList.push(1472266); // 아대
		break;

		case 1400:
		case 1410:
		case 1411:
		case 1412:
		wList.push(1472266); // 아대
		break;

		case 430:
		case 431:
		case 432:
		case 433:
		case 434:
		wList.push(1332280); // 단검
		break;

		case 2400:
		case 2410:
		case 2411:
		case 2412:
		wList.push(1362141); // 케인
		break;

		case 3600:
		case 3610:
		case 3611:
		case 3612:
		wList.push(1242120); // 에너지소드
		break;

		case 6400:
		case 6410:
		case 6411:
		case 6412:
		wList.push(1272021); // 체인
		break;

		case 6300:
		case 6310:
		case 6311:
		case 6312:
		wList.push(1214028); // 브레스 슈터
		break;

		//해적
		case 500:
		wList.push(1482222); // 너클
		wList.push(1492236); // 건
		wList.push(1532148); // 핸드캐논
		break;

		case 510:
		case 511:
		case 512:
		case 1500:
		case 1510:
		case 1511:
		case 1512:
		case 2500:
		case 2510:
		case 2511:
		case 2512:
		case 15500:
		case 15510:
		case 15511:
		case 15512:
		wList.push(1482222); // 너클
		break;

		case 520:
		case 521:
		case 522:
		case 3500:
		case 3510:
		case 3511:
		case 3512:
		wList.push(1492236); // 건
		break;

		case 530:
		case 531:
		case 532:
		case 501:
		wList.push(1532148); // 핸드캐논
		break;

		case 16400:
		case 16410:
		case 16411:
		case 16412:
		wList.push(1292028); // 부채
		break;

		case 16200: //라라
		case 16210:
		case 16211:
		case 16212:
		wList.push(1372229); // 완드
		break;

		case 6500:
		case 6510:
		case 6511:
		case 6512:
		wList.push(1222114); // 소울슈터
		break;

		case 10110:
		break;
		default:
		wList.push(1213028);
		break;
	}
}
function hpJobCheck(i)
{
	if(Math.floor(i / 10) === 312)
	return true;
}

function addOption(i, isWeapon)
{
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(i);
	if(!isWeapon)
	{
		if(hpJobCheck(cm.getPlayer().getJob()))
		{
			item.setHp(item.getHp() + 2500);
			item.setState(20);
			item.setPotential1(40086);
			item.setPotential2(40086);
			item.setPotential3(40086);
			item.setWatk(item.getWatk() + 40);
		}
		else
		{
			item.setStr(item.getStr() + 50);
			item.setDex(item.getDex() + 50);
			item.setInt(item.getInt() + 50);
			item.setLuk(item.getLuk() + 50);

			if(isMagician(cm.getPlayer().getJob()))
			item.setMatk(item.getMatk() + 40);

			else
			item.setWatk(item.getWatk() + 40);

			item.setState(19);
			item.setPotential1(40086);
			item.setPotential2(40086);
			item.setPotential3(40086);
		}
	}
	else
	{

		if(hpJobCheck(cm.getPlayer().getJob()))
		{
			item.setHp(item.getHp() + 5000);
			item.setWatk(item.getWatk() + 40);
			item.setState(19);
			item.setPotential1(30051);
			item.setPotential2(30051);
			item.setPotential3(30051);
		}
		else
		{
			item.setEnhance(item.getEnhance() + 17);
			item.setStr(item.getStr() + 80);
			item.setDex(item.getDex() + 80);
			item.setInt(item.getInt() + 80);
			item.setLuk(item.getLuk() + 80);
			item.setState(19);
			if(isMagician(cm.getPlayer().getJob()))
			{
				item.setMatk(item.getMatk() + 134);
				item.setPotential1(40052);
				item.setPotential2(40052);
				item.setPotential3(40052);
				item.setPotential4(30602);
				item.setPotential5(30291);
				item.setPotential6(30070);
			}
			else
			{
				item.setWatk(item.getWatk() + 134);
				item.setPotential1(40051);
				item.setPotential2(40051);
				item.setPotential3(40051);
				item.setPotential4(30602);
				item.setPotential5(30291);
				item.setPotential6(30070);
			}
		}
	}
	item.setLevel(item.getUpgradeSlots());
	item.setUpgradeSlots(0);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
}