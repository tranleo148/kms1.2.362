function start()
{
	status = -1;
	action(1, 0, 0);
} 

function action(mode, type, selection)
{
	if(mode != 1)
	{
		cm.dispose();
		return;
	}
	else
	{
		status++;
	}

	if (status == 0)
	{
		codyList = [];
		gColor = !cm.getPlayer().getGender() ? "#fc0xFFFF00DD#여성" : "#fc0xFF3DB7CC#남성";
		selStr = "안녕. 난 빅 헤드국의 #b빅 헤드워드#k라고 해. 나한테 너의 외모를 맡겨보는 게 어때?";
		selStr += "\r\n\r\n#e#r[헤어 변경하기]　　　　　[성형 및 렌즈 변경]#b#n\r\n";
		selStr += "#L1#헤어①#l#L3#헤어③#l　　　　#e #n#L5#얼굴①#l#L6#얼굴②#l\r\n";
		selStr += "#L2#헤어②#l#L4#헤어④#l　　　　#e #n#L7##e렌즈색#n 변경하기#l\r\n";

		selStr += "\r\n\r\n\r\n#e#r[색상 변경하기]　　　　　[성전환 · 검색하기]#b#n\r\n";
		selStr += "#L8##e헤어색#n 변경하기#l　　　　 #L10##e"+gColor+"#n으로 성전환하기#l#b\r\n";
		selStr += "#L9##e피부색#n 변경하기#l　　　　 #d#L11#헤어 #e·#n 성형 #e검색#n하기#l\r\n ";
		cm.sendSimpleS(selStr, 4, 1012117);
	}

	else if(status == 1)
	{
		SEL_00 = selection;
		Gender = cm.getPlayer().getGender();
		if(SEL_00 == 11)
		{
			cm.dispose();
			cm.openNpc(1012117);
			return;
		}

		if(SEL_00 == 10)
		{
			if(cm.getPlayer().getJob() == 10112)
			{
				cm.sendOkS("제로 직업군은 성전환을 할 수 없습니다.", 4, 1012117);
				cm.dispose();
				return;
			}
			cm.sendYesNoS("#e"+gColor+"#n#k으로 성전환을 하시겠습니까? 더 이상 해당 성별에 맞는 1장비를 착용할 수 없습니다.", 4, 1012117);
		}

		Beauty = SEL_00 < 2 ? "#e헤어#n를" : SEL_00 == 2 ? "#e얼굴#n을" : "#e색깔#n을"; 
		selStr = "#fn돋움##fc0xFFFFFFFF#지금의 "+Beauty+" 전혀 새로운 스타일로 바꿔 줄 수 있지. 지금 모습이 지겨워 졌다면 바꾸고 싶은 헤어를 천천히 고민해 봐";
		switch(SEL_00)
		{
			case 1: // 헤어1
			if(!Gender)
			codyList = [30000, 30020, 30030, 30040, 30050, 30060, 30100, 30110, 30120, 30130, 30140, 30150, 30160, 30170, 30180, 30190, 30200, 30210, 30220, 30230, 30240, 30250, 30260, 30270, 30280, 30290, 30300, 30310, 30320, 30330, 30340, 30350, 30360, 30370, 30400, 30410, 30420, 30440, 30450, 30460, 30470, 30480, 30490, 30510, 30520, 30530, 30540, 30560, 30570, 30590, 30610, 30620, 30630, 30640, 30650, 30660, 30670, 30680, 30700, 30710, 30730, 30760, 30770, 30790, 30800, 30810, 30820, 30830, 30840, 30850, 30860, 30870, 30880, 30910, 30930, 30940, 30950, 33030, 33060, 33070, 33080, 33090, 33110, 33120, 33130, 33150, 33170, 33180, 33190, 33210, 33220, 33250, 33260, 33270, 33280, 33310, 33330, 33350, 33360, 33370];

			else
			codyList = [31000, 31010, 31020, 31030, 31040, 31050, 31060, 31070, 31080, 31090, 31100, 31110, 31120, 31130, 31140, 31150, 31160, 31170, 31180, 31190, 31200, 31210, 31220, 31230, 31240, 31250, 31260, 31270, 31280, 31290, 31300, 31310, 31320, 31330, 31340, 31350, 31400, 31410, 31420, 31440, 31450, 31460, 31470, 31480, 31490, 31510, 31520, 31530, 31540, 31550, 31560, 31590, 31610, 31620, 31630, 31640, 31650, 31670, 31680, 31690, 31700, 31710, 31720, 31740, 31750, 31780, 31790, 31800, 31810, 31820, 31840, 31850, 31860, 31880, 31890, 31910, 31920, 31930, 31940, 31950];
			break;

			case 2: // 헤어2
			if(!Gender)
			codyList = [33380, 33390, 33400, 33410, 33430, 33440, 33450, 33460, 33480, 33500, 33510, 33520, 33530, 33550, 33580, 33590, 33600, 33610, 33620, 33630, 33640, 33660, 33670, 33680, 33690, 33700, 33710, 33720, 33730, 33740, 33750, 33760, 33770, 33780, 33790, 33800, 33810, 33820, 33830, 33930, 33940, 33950, 33960, 33990, 35010, 35020, 35030, 35040, 35050, 35060, 35070, 35080, 35090, 35100, 35150, 35180, 35190, 35200, 35210, 35250, 35260, 35280, 35290, 35300, 35310, 35330, 35350, 35360, 35420, 35430, 35440, 35460, 35470, 35480, 35490, 35500, 35510, 35520, 35530, 35540, 35550, 35560, 35570, 35600, 35620, 35630, 35640, 35650, 35660, 35680, 35690, 35700, 35710, 35720, 35780, 35790, 35950, 35960, 36010, 36020];

			else
			codyList = [31990, 34040, 34070, 34080, 34090, 34100, 34110, 34120, 34130, 34140, 34150, 34160, 34170, 34180, 34190, 34210, 34220, 34230, 34240, 34250, 34260, 34270, 34310, 34320, 34330, 34340, 34360, 34370, 34380, 34400, 34410, 34420, 34430, 34440, 34450, 34470, 34480, 34490, 34510, 34540, 34560, 34590, 34600, 34610, 34620, 34630, 34640, 34660, 34670, 34680, 34690, 34700, 34710, 34720, 34730, 34740, 34750, 34760, 34770, 34780, 34790, 34800, 34810, 34820, 34830, 34840, 34850, 34860, 34870, 34880, 34900, 34910, 34940, 34950, 34960, 34970, 35000, 36620, 36630, 36640];
			break;

			case 3: // 헤어3
			if(!Gender)
			codyList = [33380, 33390, 33400, 33410, 33430, 33440, 33450, 33460, 33480, 33500, 33510, 33520, 33530, 33550, 33580, 33590, 33600, 33610, 33620, 33630, 33640, 33660, 33670, 33680, 33690, 33700, 33710, 33720, 33730, 33740, 33750, 33760, 33770, 33780, 33790, 33800, 33810, 33820, 33830, 33930, 33940, 33950, 33960, 33990, 35010, 35020, 35030, 35040, 35050, 35060, 35070, 35080, 35090, 35100, 35150, 35180, 35190, 35200, 35210, 35250, 35260, 35280, 35290, 35300, 35310, 35330, 35350, 35360, 35420, 35430, 35440, 35460, 35470, 35480, 35490, 35500, 35510, 35520, 35530, 35540, 35550, 35560, 35570, 35600, 35620, 35630, 35640, 35650, 35660, 35680, 35690, 35700, 35710, 35720, 35780, 35790, 35950, 35960, 36010, 36020];

			else
			codyList = [36650, 36670, 36680, 36690, 36700, 36710, 36720, 36730, 36740, 36750, 36760, 36770, 36780, 36790, 36800, 36810, 36820, 36830, 36840, 36850, 36860, 36900, 36910, 36920, 36940, 36950, 36980, 38410, 38420, 38430, 38440, 38460, 38470, 38490, 38520, 38540, 38550, 38560, 38570, 38580, 38590, 38600, 38610, 38620, 38630, 38640, 38650, 38660, 38670, 38680, 38690, 38700, 38730, 38740, 38750, 38760, 38800, 38810, 38820, 38840, 38860, 38880, 38910, 38940, 39090, 41060, 41070, 41080, 41090, 41100, 41110, 41120, 41150, 41160, 41200, 41220, 41340, 41350, 41360, 41370];
			break;

			case 4: // 헤어4
			if(!Gender)
			codyList = [36030, 36040, 36050, 36070, 36080, 36090, 36100, 36130, 36140, 36150, 36160, 36170, 36180, 36190, 36200, 36210, 36220, 36230, 36240, 36250, 36300, 36310, 36330, 36340, 36350, 36380, 36390, 36400, 36410, 36420, 36430, 36440, 36450, 36460, 36470, 36480, 36510, 36520, 36530, 36560, 36570, 36580, 36590, 36620, 36630, 36640, 36650, 36670, 36680, 36690, 36700, 36710, 36720, 36730, 36740, 36750, 36760, 36770, 36780, 36790, 36800, 36810, 36820, 36830, 36840, 36850, 36860, 36900, 36910, 36920, 36940, 36950, 36980, 36990, 40000, 40010, 40020, 40050, 40060, 40090, 40100, 40120, 40250, 40260, 40270, 40280, 40290, 40300, 40310, 40320, 40330, 40390, 40400, 40410, 40420, 40440, 40450, 40460, 40470, 40480, 40490, 40500, 40510, 40570, 40580, 40600, 40610, 40640, 40660, 40670, 40690, 40720, 40730, 40740, 40810, 40820];

			else
			codyList = [41380, 41390, 41400, 41440, 41470, 41480, 41490, 41510, 41520, 41530, 41560, 41570, 41590, 41600, 41670, 41700, 41720, 41730, 41740, 41750, 41850, 41860, 41880, 41890, 41920, 41930, 41950, 44010];
			break;

			case 5: // 얼굴1
			if(!Gender)
			codyList = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20009, 20010, 20011, 20012, 20013, 20014, 20015, 20016, 20017, 20018, 20019, 20020, 20021, 20022, 20024, 20025, 20027, 20028, 20029, 20030, 20031, 20032, 20036, 20037, 20040, 20041, 20042, 20043, 20044, 20045, 20046, 20047, 20048, 20049, 20050, 20051, 20052, 20053, 20055, 20056, 20057, 20058, 20059, 20060, 20061, 20062, 20063, 20064, 20065, 20066, 20067, 20068, 20069, 20070, 20074, 20075, 20076, 20077, 20080, 20081, 20082, 20083, 20084, 20085, 20086, 20087, 20088, 20089, 20090, 20093, 20094,];

			else
			codyList = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21009, 21010, 21011, 21012, 21013, 21014, 21015, 21016, 21017, 21018, 21019, 21020, 21021, 21023, 21024, 21026, 21027, 21028, 21029, 21030, 21031, 21033, 21035, 21036, 21038, 21039, 21040, 21041, 21042, 21043, 21044, 21045, 21046, 21047, 21048, 21049, 21050, 21052, 21053, 21054, 21055, 21056, 21057, 21058, 21059, 21060, 21061, 21062, 21063, 21064, 21065, 21069, 21070, 21071, 21072, 21073, 21074, 21075, 21077, 21078, 21079, 21080, 21081, 21082, 21083, 21084, 21085, 21086, 21089, 21090, 21091];
			break;

			case 6: // 얼굴2
			if(!Gender)
			codyList = [20095, 20097, 20098, 23000, 23001, 23002, 23003, 23005, 23006, 23008, 23010, 23011, 23012, 23015, 23016, 23017, 23018, 23019, 23020, 23023, 23024, 23025, 23026, 23027, 23028, 23029, 23031, 23032, 23033, 23034, 23035, 23038, 23039, 23040, 23041, 23042, 23043, 23044, 23053, 23054, 23056, 23057, 23060, 23061, 23062, 23063, 23067, 23068, 23069, 23072, 23073, 23074, 23075, 23079, 23080, 23081, 23082, 23083, 23084, 23085, 23086, 23087, 23088, 23089, 23090, 23091, 23092, 23095, 23096, 23097, 25000, 25004, 25005, 25006, 25007, 25008, 25011, 25014, 25015, 25016, 25017, 25021, 25022, 25023, 25024, 25025, 25026, 25027, 25033];

			else
			codyList = [21092, 21093, 21094, 21095, 21096, 21097, 21098, 24001, 24002, 24003, 24004, 24007, 24008, 24011, 24012, 24013, 24014, 24015, 24016, 24018, 24019, 24020, 24021, 24022, 24023, 24024, 24026, 24027, 24028, 24029, 24031, 24035, 24036, 24037, 24038, 24039, 24040, 24041, 24050, 24051, 24054, 24055, 24058, 24059, 24060, 24061, 24062, 24066, 24067, 24068, 24071, 24072, 24073, 24074, 24075, 24077, 24078, 24079, 24080, 24081, 24082, 24083, 24084, 24085, 24086, 24087, 24088, 24091, 24092, 24093, 24094, 24095, 24097, 24098, 26003, 26004, 26005, 26008, 26009, 26014, 26017, 26022, 26023, 26027, 26028, 26029, 26030, 26031, 26032];
			break;


			case 7:
			for(i = 0; i < 8; i++)
			codyList.push(Math.floor(cm.getPlayer().getFace() / 1000) * 1000 + (cm.getPlayer().getFace() % 100) + i * 100);
			break;

			case 8:
			for(i = 0; i < 8; i++)
			codyList.push(Math.floor(cm.getPlayer().getHair() / 10) * 10 + i);
			break;

			case 9:
			codyList = [0, 1, 2, 3, 4, 9, 10, 11, 12, 13];
			break;

			default:
			break;
		}

		if(SEL_00 < 10)
		cm.sendStyle(selStr, codyList);
	}

	else if(status == 2)
	{
		if(SEL_00 == 10)
		{
			if(!Gender)
			{
				cm.getPlayer().setHair(31002);
				cm.getPlayer().setFace(21700);
				cm.getPlayer().setGender(1);
			}
			else
			{
				cm.getPlayer().setHair(30000);
				cm.getPlayer().setFace(20100);
				cm.getPlayer().setGender(0);
			}
			cm.dispose();
			cm.fakeRelog();
			cm.updateChar();
			return;
		}

		newItem = selection & 0xFF
		if(cm.getPlayer().getJob() == 10112 && Gender)
		{
			switch(SEL_00)
			{
				case 0:
				case 1:
				case 3:
				cm.getPlayer().setSecondHair(codyList[newItem]);
				break;

				case 2:
				case 4:
				cm.getPlayer().setSecondFace(codyList[newItem]);
				break;

				case 5:
				cm.getPlayer().setSecondSkinColor(codyList[newItem]);
				break;
			}
		}
		else
		{
			cm.setAvatar(4000000, codyList[newItem]);
		}
		cm.dispose();
		cm.updateChar();
	}
}