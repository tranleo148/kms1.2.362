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
		if(!cm.haveItem(2430917))
		{
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getJob() == 10112) {

			cm.sendOkS("#b#h ##k#fs 11#님의 여정을 돕고자, 아이템들에 #e#r쓸만한 옵션#k#n을 부여 해드렸습니다. \r\n#r#e(제로 직업군은 무기를 획득할 수 없습니다)#n\r\n\r\n#b"
			+ "  #i1003561:# #t1003561:#\r\n"
			+ "  #i1052467:# #t1052467:#\r\n"
			+ "  #i1032148:# #t1032148:#\r\n"
			+ "  #i1132161:# #t1132161:#\r\n"
			+ "  #i1152099:# #t1152099:#\r\n"
			+ "  #i1102467:# #t1102467:#\r\n"
			+ "  #i1072672:# #t1072672:#\r\n"
			+ "  #i1082438:# #t1082438:#\r\n", 4, 2007);

			if (wList.indexOf(S) == -1) {
				cm.sendOk("비정상적인 접근");
				cm.dispose();
				return;
			}
			addOption(1003561, false);
			addOption(1052467, false);
			addOption(1032148, false);
			addOption(1132161, false);
			addOption(1152099, false);
			addOption(1102467, false);
			addOption(1072672, false);
			addOption(1082438, false);
			cm.gainItem(2430917, -1);

	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142085);
	item.setStr(10);
	item.setDex(10);
	item.setInt(10);
	item.setLuk(10);
	item.setWatk(5);
	item.setMatk(5);
	item.setHp(item.getHp() + 3000);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
		cm.dispose();
	}

		wList = [];
		getWeapon(cm.getPlayer().getJob());
		selStr = "#fs 11##fc0xFF990033#[ZERO]#fc0xFF000000#에 온걸 진심으로 환영하네! 이건 자네의 모험을 도와줄 지원 장비일세, 직업에 맞는 무기를 선택해보게나.\r\n";
		selStr += "#L0##r#e나중에 다시 선택하겠습니다.#b#n#l\r\n\r\n";
		for(i = 0; i < wList.length; i++)
		{
			selStr += "#L"+wList[i]+"##i"+wList[i]+":# #t"+wList[i]+":##l\r\n";
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
			+ "  #i1003561:# #t1003561:#\r\n"
			+ "  #i1052467:# #t1052467:#\r\n"
			+ "  #i1032148:# #t1032148:#\r\n"
			+ "  #i1132161:# #t1132161:#\r\n"
			+ "  #i1152099:# #t1152099:#\r\n"
			+ "  #i1102467:# #t1102467:#\r\n"
			+ "  #i1072672:# #t1072672:#\r\n"
			+ "  #i1082438:# #t1082438:#\r\n"
			+ "  #i"+S+":# #t"+S+":# #e#k#n\r\n", 4, 9062294);

			if (wList.indexOf(S) == -1) {
				cm.sendOk("비정상적인 접근");
				cm.dispose();
				return;
			}
	
			addOption(1003561, false);
			addOption(1052467, false);
			addOption(1032148, false);
			addOption(1132161, false);
			addOption(1152099, false);
			addOption(1102467, false);
			addOption(1072672, false);
			addOption(1082438, false);
		addOption(S, true);
		cm.gainItem(2430917, -1);

	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142085);
	item.setStr(10);
	item.setDex(10);
	item.setInt(10);
	item.setLuk(10);
	item.setWatk(5);
	item.setMatk(5);
	item.setHp(item.getHp() + 3000);
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
		wList.push(1302334); // 한손검
		wList.push(1312200); // 한손도끼
		wList.push(1322251); // 한손둔기
		wList.push(1402252); // 두손검
		wList.push(1412178); // 두손도끼
		wList.push(1422185); // 두손둔기
		wList.push(1432215); // 창
		wList.push(1442269); // 폴암
		break;

		case 110:
		wList.push(1302334); // 한손검
		wList.push(1402252); // 두손검
		break;

		case 120:
		wList.push(1302334); // 한손검
		wList.push(1312200); // 한손도끼
		wList.push(1322251); // 한손둔기
		wList.push(1402252); // 두손검

		wList.push(1412178); // 두손도끼
		wList.push(1422185); // 두손둔기
		break;

		case 130:
		wList.push(1432215); // 창
		wList.push(1442269); // 폴암
		break;

		case 1100:
		case 1110:
		wList.push(1402252); // 두손검
		break;

		case 2100:
		case 2110: 
		wList.push(1442269); // 폴암
		break;

		case 3100:
		case 3101:
		wList.push(1312200); // 한손도끼
		wList.push(1322251); // 한손둔기
		wList.push(1232110); // 데스페라도
		break;

		case 3110:
		wList.push(1312200); // 한손도끼
		wList.push(1322251); // 한손둔기
		break;

		case 3120:
		case 3101:
		wList.push(1232110); // 데스페라도
		break;

		case 3700:
		case 3710:
		wList.push(1582021); // 건틀렛 리볼버
		break;

		case 5100:
		case 5110: //미하일
		wList.push(1302334); // 한손검
		break;

		case 6100:
		case 6110:
		wList.push(1402252); // 두손검
		break;

		case 15002:
		wList.push(1213020); // 튜너
		break;

		/* 마법사 */
		case 200:
		case 210:
		case 220:
		case 1200:
		case 1210:
		case 2200:
		case 2210:
		wList.push(1372223); // 완드
		wList.push(1382260); // 스태프
		break;

		case 2700:
		case 2710:
		wList.push(1212116); // 샤이닝로드
		break;

		case 3200:
		case 3210:
		wList.push(1382260); // 스태프
		break;

		case 14200:
		case 14210:
		wList.push(1262027); // ESP 리미터
		break;

		case 15200:
		case 15210:
		wList.push(1282019); // 매직 건틀렛
		break;

		/* 궁1수 */
		case 300:
		wList.push(1452253); // 활
		wList.push(1462240); // 석궁
		break;

		case 310:
		case 1300:
		case 1310:
		wList.push(1452253); // 활
		break;

		case 320:
		case 3300:
		case 3310:
		wList.push(1462240); // 석궁
		break;

		case 301:
		wList.push(1592008); // 에인션트 보우
		break;

		case 2300:
		case 2310:
		wList.push(1522139); // 듀얼 보우건
		break;

		case 400:
		wList.push(1332275); // 단검
		wList.push(1472262); // 아대
		break;

		case 410:
		case 1400:
		case 1410:
		wList.push(1472262); // 아대
		break;

		case 420:
		case 430:
		wList.push(1332275); // 단검
		break;

		case 2400:
		case 2410:
		wList.push(1362136); // 케인
		break;

		case 3600:
		case 3610:
		wList.push(1242117); // 에너지소드
		break;

		case 6400:
		case 6410:
		wList.push(1272031); // 체인
		break;

		case 6300:
		wList.push(1214020); // 브레스 슈터
		break;

		//해적
		case 500:
		wList.push(1482217); // 너클
		wList.push(1492232); // 건
		wList.push(1532145); // 핸드캐논
		break;

		case 510:
		case 1500:
		case 1510:
		case 2500:
		case 2510:
		case 15500:
		case 15510:
		wList.push(1482217); // 너클
		break;

		case 520:
		case 3500:
		case 3510:
		wList.push(1492232); // 건
		break;

		case 530:
		case 501:
		wList.push(1532145); // 핸드캐논
		break;

		case 16400:
		wList.push(1292023); // 부채
		break;

		case 16200: //라라
		wList.push(1372223); // 완드
		break;

		case 6500:
		case 6510:
		wList.push(1222110); // 소울슈터
		break;
		case 10110:
		break;
		default:
		wList.push(1213023);
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
			item.setStr(item.getStr() + 30);
			item.setDex(item.getDex() + 30);
			item.setInt(item.getInt() + 30);
			item.setLuk(item.getLuk() + 30);
			item.setState(19);
			if(isMagician(cm.getPlayer().getJob()))
			{
				item.setMatk(item.getMatk() + 70);
				item.setPotential1(30052);
				item.setPotential2(30052);
				item.setPotential3(30052);
			}
			else
			{
				item.setWatk(item.getWatk() + 70);
				item.setPotential1(30051);
				item.setPotential2(30051);
				item.setPotential3(30051);
			}
		}
	}
	item.setReqLevel(-90);
	item.setLevel(item.getUpgradeSlots());
	item.setUpgradeSlots(0);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
}