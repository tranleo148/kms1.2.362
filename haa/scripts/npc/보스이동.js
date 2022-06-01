h = "#fUI/UIMiniGame.img/starPlanetRPS/heart#";
g = "#fMap/MapHelper.img/minimap/anothertrader#";
a = "#i3801317#"
b = "#i3801313#"
c = "#i3801314#"
d = "#i3801315#"
p = "#fc0xFFF781D8#"

mTown = ["SixPath", "Henesys", "Ellinia", "Perion", "KerningCity",
	 "Rith", "Dungeon", "Nautilus", "Ereb", "Rien",
	 "Orbis", "ElNath", "Ludibrium", "Folkvillige", "AquaRoad",
	 "Leafre", "Murueng", "WhiteHerb", "Ariant", "Magatia",
	 "Edelstein", "Eurel", "critias", "Haven", "Road of Vanishing", "ChewChew", "Lacheln", "Arcana", "Morass", "esfera", "aliance", "moonBridge", "TheLabyrinthOfSuffering", "Limen"];

cTown = [104020000, 120040000, 101000000, 102000000, 103000000,
	 104000000, 105000000, 120000000, 130000000, 140000000,
	 200000000, 211000000, 220000000, 224000000, 230000000,
	 240000000, 250000000, 251000000, 260000000, 261000000,
	 310000000, 101050000, 241000000, 310070000, 450001000, 450002000, 450003000, 450005000, 450006130, 450007040, 450009050, 450009100, 450011500, 450012300];

var status = -1;

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

	if (status == 0) {
        var msg = "#fs11#    #fUI/UIWindow5.img/Disguise/backgrnd#\r\n";
    //  msg += "                  #L2##fc0xFF6600CC#사냥터#fc0xFF000000#로 이동하겠습니다.#l\r\n";
     // msg += "                  #L1##fc0xFF6600CC#마을#fc0xFF000000#로 이동하겠습니다.#l\r\n";
      msg += "                  #L3##fc0xFF6600CC#보스#fc0xFF000000#로 이동하겠습니다.#l\r\n";
    //  msg += "                  #L4##fc0xFF6600CC#보스#fc0xFF000000#로 이동하겠습니다.#k#l\r\n";
      cm.sendSimple(msg);
	 } else if(status == 1)
	{
		ans_01 = selection;
		selStr = "";
		switch(ans_01)
		{
			case 1:
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 2510024, "마을이동");
			break;

			case 2:
			selStr += "#fs11##fc0xFF000000#어떤 사냥터로 이동할겐가?#k\r\n\r\n";
			selStr += "#L1# #fMap/MapHelper.img/minimap/party##r 초보자 사냥터로 이동하겠습니다.#b (레벨 10 ~ 레벨 200)#l\r\n\r\n";
			selStr += "#Cgray##fs11#――――――――――――――――――――――――――――――――――――――――#k";
			selStr += "#L3# #fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce##fc0xFF0066CC# 아케인리버#fc0xFF000000# 사냥터로 이동하겠습니다.#l\r\n";
			selStr += "#L5# #fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce##fc0xFF0066CC# 셀라스#fc0xFF000000# 사냥터로 이동하겠습니다.#l\r\n";
			selStr += "#L6# #fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce##fc0xFF3366FF# 세르니움#fc0xFF000000# 사냥터로 이동하겠습니다.#l\r\n";
			selStr += "#L7# #fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce##fc0xFF003399# 호텔 아르크스#fc0xFF000000# 사냥터로 이동하겠습니다.#l\r\n\r\n";
			selStr += "#Cgray##fs11#――――――――――――――――――――――――――――――――――――――――#k";
			selStr += "#L2# #fUI/Basic.img/BtCoin/normal/0##fc0xFF339933# 피로도 사냥터로 이동하겠습니다.#b (재화 파밍)#l#fc0xFF000000#\r\n";
			break;

			case 3:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
			selStr += "#fs11##fc0xFF000000#[#e#rEASY#k#l]#k\r\n";
                                    selStr += "#L1##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 자쿰  #l";
                                    selStr += "#L2##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 카웅#l\r\n";
                                    selStr += "#L3##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 혼테일#l";
                                    selStr += "#L4##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 힐라#l\r\n";
                                    selStr += "#L5##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 반레온#l";
                                    selStr += "#L6##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 아카이럼#l\r\n";
                                    selStr += "#L7##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 핑크빈#l";
                                    selStr += "#L8##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 시그너스#l\r\n\r\n";
			selStr += "#fs11##fc0xFF000000#[#e#rNOMARL#k#l]#k\r\n";
                                    selStr += "#L9##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 매그너스#l";
                                    selStr += "#L10##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 루타비스#l\r\n";
                                    selStr += "#L11##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 파풀라투스#l\r\n\r\n";
			selStr += "#fs11##fc0xFF000000#[#e#rHARD#k#l]#k\r\n";
                                    selStr += "#L12##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 스우#l\r\n";
                                    selStr += "#L13##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 데미안#l";
                                    selStr += "#L14##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 루시드#l\r\n";
                                    selStr += "#L15##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 윌    #l";
                                    selStr += "#L16##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 불꽃늑대#l\r\n";
                                    selStr += "#L17##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 우르스#l";
                                    selStr += "#L18##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 진힐라#l\r\n";
                                    selStr += "#L19##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 듄켈   #l";
                                    selStr += "#L20##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 더스크#l\r\n";
                                    selStr += "#L21##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 검마   #l";
                                    selStr += "#L22##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 세렌#l";
			break;1

			case 4:
			//cm.openNpc(9900006);

		cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUIOption(7,2));
		cm.dispose();
		return;
			 case 10:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
			selStr += "#fs11#\r\n엔피시 #e#fc0xFF6799FF#엔피시#k#n와 #e#b엔피시#k#n 확인하고\r\n이동하시기 바랍니다.\r\n#fs11#";
			selStr += "\r\n#e#r[엔피시 요기 다 있음]#d#n\r\n";
                                    selStr += "#L100030300##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 엔피시맵#l\r\n";
                                     
                                   break;            

			case 11:
			cm.dispose();
			cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.closeUI(62));
			cm.warp(925020000, 0);
			return;

			case 12:
			cm.dispose();
			cm.openNpc(9071003);
			return;

			case 13:
			cm.dispose();
			cm.openNpc(9020011);
			return;

		}
		if (ans_01 == 2 || ans_01 == 3)
		cm.sendSimpleS(selStr, 4);
	}

	else if(status == 2)
	{
		ans_02 = selection;
		if (ans_01 == 3) {
			switch (ans_02) {
				case 1:
					cm.dispose();
					cm.warpParty(211042300, 0);
					return;
				break;
				case 2:
					cm.dispose();
					cm.warpParty(221030900, 0);
					return;
				break;
				case 3:
					cm.dispose();
					cm.warpParty(240040700, 0);
					return;
				break;
				case 4:
					cm.dispose();
					cm.warpParty(262000000, 0);
					return;
				break;
				case 5:
					cm.dispose();
					cm.warpParty(211070000, 0);
					return;
				break;
				case 6:
					cm.dispose();
					cm.warpParty(272020110, 0);
					return;
				break;
				case 7:
					cm.dispose();
					cm.warpParty(270040000, 0);
					return;
				break;
				case 8:
					cm.dispose();
					cm.warpParty(271041000, 0);
					return;
				break;
				case 9:
					cm.dispose();
					cm.warpParty(401060000, 0);
					return;
				break;
				case 10:
					cm.dispose();
					cm.warpParty(105200000, 0);
					return;
				break;
				case 11:
					cm.dispose();
					cm.warpParty(220080000, 0);
					return;
				break;
				case 12:
					cm.dispose();
					cm.warpParty(350060300, 0);
					return;
				break;
				case 13:
					cm.dispose();
					cm.warpParty(105300303, 0);
					return;
				break;
				case 14:
					cm.dispose();
					cm.warpParty(450004000, 0);
					return;
				break;
				case 15:
					cm.dispose();
					cm.warpParty(450007240, 0);
					return;
				break;
				case 16:
					cm.dispose();
					cm.openNpc(9120012);
					return;
				break;
				case 17:
					cm.dispose();
					cm.warpParty(970072200, 0);
					return;
				break;
				case 18:
					cm.dispose();
					cm.warpParty(940500100, 0);
					return;
				break;
				case 19:
					cm.dispose();
					cm.warpParty(450012200, 0);
					return;
				break;
				case 20:
					cm.dispose();
					cm.warpParty(450009301, 0);
					return;
				break;
				case 21:
					cm.dispose();
					cm.warpParty(450012500, 0);
					return;
				break;
				case 22:
					cm.dispose();
					cm.warpParty(410000670, 0);
					return;
				break;
			}
		} else {
		selStr = "";
		switch(ans_02)
		{
			case 1:
			selStr += "#fs11#\r\n사냥터에 등장하는 #b몬스터의 평균 레벨#n#k을 확인하고 이동하게나.#fs11#\r\n\r\n";
			selStr += "#b#L931000500##b#fMap/MapHelper.img/minimap/anothertrader# 재규어 서식지 (와일드 헌터 전용)#l\r\n\r\n";
			//selStr += "#r#L993134100#Lv.15~270  │ 유니온 0티어초보자사냥터          │ 유니온0티어사냥터#l\r\n";
			//selStr += "#r#L993134150#Lv.15~270  │ 유니온 0티어초보자사냥터          │ 유니온0티어사냥터#l\r\n";
			selStr += "#b#L100010000##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.10  #d헤네시스 - 헤네시스 북쪽언덕#l\r\n";
			selStr += "#b#L103050340##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.30  #d빅토리아로드 - 수련장2#l\r\n";
			selStr += "#b#L101030500##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.30  #d북쪽 숲 - 숲이 끝나는 곳#l\r\n";
			selStr += "#b#L103020320##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.50  #d커닝시티 지하철- 1호선 3구간#l\r\n";
			selStr += "#b#L102030000##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.55  #d불타버린 땅 - 와일드보어의 땅#l\r\n";
			selStr += "#b#L102040600##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.60  #d유적 발굴지 - 미접근 지역#l\r\n";
			selStr += "#b#L105010000##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.70  #d슬리피우드 - 조용한 습지#l\r\n";
			selStr += "#b#L230040000##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.100  #d아쿠아로드 - 깊은 바다 협곡1#l\r\n";
			selStr += "#b#L220020600##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.120  #d루디브리엄성 - 장난감공장<기계실>#l\r\n";
			selStr += "#b#L250020200##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.130  #d무릉사원 - 중급수련장#l\r\n";
			selStr += "#b#L251010402##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.150  #d무릉도원 - 빨간코 해적단 소굴2#l\r\n";
			selStr += "#b#L221030800##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.180  #dUFO 내부 - 조종간1#l\r\n";
			selStr += "#b#L273040300##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.195  #d황혼의 페리온 - 버려진 발굴지역4#l\r\n";
			selStr += "#b#L241000216##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.200  #d킹덤로드 - 타락한 마력의 숲1#l\r\n";
			selStr += "#b#L241000206##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.200  #d킹덤로드 - 타락한 마력의 숲2#l\r\n";
			selStr += "#b#L310070140##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.205  #d기계무덤 - 기계무덤 언덕4#l\r\n";
			selStr += "#b#L241000226##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.210  #d킹덤로드 - 타락한 마력의 숲3#l\r\n";
			selStr += "#b#L310070210##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.220  #d스카이라인 - 스카이라인1#l\r\n";
                        //selStr += "#d#L310070300#Lv.218 │ 블랙헤븐 갑판     │ 블랙헤븐#l\r\n";
			//selStr += "#r#L105300301#Lv.226 │ 타락한 세계수     │ 상단 줄기 갈림길#l\r\n ";
			break;

			case 2:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/StarForce#";
			selStr += "#fs11#\r\n#fc0xFF000000#피로도 사냥터는 #e#fc0xFFFF9436피로회복제를 통한 회복 후 #피로도#k#n를 소모하며 #b#e일반적인 사냥터#fc0xFF000000##n와는 달리 #b#e많은 아이템 #r[#z4031227#, #z4000916#, #z4310266#, #z4001209#]#b을 획득#fc0xFF000000##n할 수 있네\r\n\r\n#fc0xFF000000#또한 피로도를 모두 소진하면 이용할 수 없네.\r\n#r주의 : 피로도는 자정이 지나면 0으로 초기화됩니다.\r\n\r\n";
	selStr += "#b#L261020700##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.220 #fc0xFFFF9436#알카드노 연구소 - 연구소 A-3 구역 #b(하급)#l\r\n";
	selStr += "#b#L261010103##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.260 #fc0xFFFF9436#제뉴미스트 연구소 - 연구소 203호 #b(중급)#l\r\n";
			break;

			case 3:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce#";
        if (cm.getPlayer().getLevel() < 200) {
selStr += "#fs11##fc0xFF000000#해당 사냥터는 #b200레벨#fc0xFF000000#이상부터 입장 가능하다네";
			cm.dispose();
}
        if (cm.getPlayer().getLevel() >= 200) {
			selStr += "#fs11#\r\n사냥터의 #e#fc0xFF6799FF#아케인포스#k#n와 #b몬스터의 평균 레벨#k#n 확인하고\r\n이동하게나.\r\n#fs11#";
			selStr += "\r\n#e#r[소멸의 여로]#d#n\r\n";
		            selStr += "#L450001010##fc0xFF6799FF#Lv.202#d | "+starImg+" 30 | 풍화된 기쁨의 땅#l\r\n";
		            selStr += "#L450001012##fc0xFF6799FF#Lv.202#d | "+starImg+" 30 | 풍화된 분노의 땅#l\r\n";
		            selStr += "#L450001014##fc0xFF6799FF#Lv.202#d | "+starImg+" 30 | 풍화된 슬픔의 땅#l\r\n";
		            selStr += "#L450001214##fc0xFF6799FF#Lv.207#d | "+starImg+" 60 | 동굴의 동쪽길2#l\r\n";
		            selStr += "#L450001216##fc0xFF6799FF#Lv.207#d | "+starImg+" 60 | 동굴 아래쪽#l\r\n";
		            selStr += "#L450001230##fc0xFF6799FF#Lv.207#d | "+starImg+" 60 | 아르마의 은신처#l\r\n";
		            selStr += "#L450001260##fc0xFF6799FF#Lv.209#d | "+starImg+" 80 | 숨겨진 호숫가#l\r\n";
		            selStr += "#L450001262##fc0xFF6799FF#Lv.209#d | "+starImg+" 80 | 숨겨진 동굴#l\r\n";
			selStr += "\r\n\r\n#e#r[리버스 시티]#d#n\r\n";
		            selStr += "#L450014140##fc0xFF6799FF#Lv.207#d | "+starImg+" 60 | 지하열차1#l\r\n";
		            selStr += "#L450014310##fc0xFF6799FF#Lv.209#d | "+starImg+" 100 | 숨겨진 지하열차#l\r\n";
		            selStr += "#L450014320##fc0xFF6799FF#Lv.209#d | "+starImg+" 100 | 숨겨진 M타워#l\r\n";
			selStr += "\r\n\r\n#e#r[츄츄&얌얌 아일랜드]#d#n\r\n";
			selStr += "#L450002001##fc0xFF6799FF#Lv.210#d | "+starImg+" 100 | 오색동산#l\r\n";
			selStr += "#L450002006##fc0xFF6799FF#Lv.212#d | "+starImg+" 100 | 츄릅 포레스트#l\r\n";
                                    selStr += "#L450002010##fc0xFF6799FF#Lv.212#d | "+starImg+" 100 | 츄릅 포레스트 깊은 곳 #l\r\n";
			selStr += "#L450002012##fc0xFF6799FF#Lv.214#d | "+starImg+" 130 | 격류지대#l\r\n";
			selStr += "#L450002016##fc0xFF6799FF#Lv.217#d | "+starImg+" 160 | 하늘 고래산#l\r\n";
			selStr += "#L450015290##fc0xFF6799FF#Lv.225#d | "+starImg+" 190 | 얌얌 아일랜드 - 숨겨진 일리야드 들판#l\r\n";
			selStr += "\r\n\r\n#e#r[꿈의 도시 레헬른]#d#n\r\n";
			selStr += "#L450003200##fc0xFF6799FF#Lv.220#d | "+starImg+" 190 | 레헬른 뒷골목 무법자의거리#l\r\n";
			selStr += "#L450003300##fc0xFF6799FF#Lv.221#d | "+starImg+" 210 | 레헬른 야시장#l\r\n";
			selStr += "#L450003400##fc0xFF6799FF#Lv.223#d | "+starImg+" 210 | 레헬른 무도회장#l\r\n";
                                         selStr += "#L450003440##fc0xFF6799FF#Lv.225#d | "+starImg+" 210 | 레헬른 춤추는 구두점령지#l\r\n";
			selStr += "\r\n\r\n#e#r[신비의 숲 아르카나]#d#n\r\n";
			selStr += "#L450005131##fc0xFF6799FF#Lv.230#d | "+starImg+" 280 | 햇살과 흙의 숲#l\r\n";
			selStr += "#L450005221##fc0xFF6799FF#Lv.233#d | "+starImg+" 320 | 서리와 번개의 숲 1#l\r\n";
			selStr += "#L450005230##fc0xFF6799FF#Lv.235#d | "+starImg+" 320 | 맹독의 숲#l\r\n";
			selStr += "#L450005500##fc0xFF6799FF#Lv.237#d | "+starImg+" 360 | 다섯 갈래 동굴#l\r\n ";
                        selStr += "\r\n\r\n#e#r[모라스]#d#n\r\n";
                       selStr += "#L450006010##fc0xFF6799FF#Lv.236#d | "+starImg+" 400 | 산호숲으로 가는길#l\r\n";
			selStr += "#L450006140##fc0xFF6799FF#Lv.238#d | "+starImg+" 440 | 형님들 구역#l\r\n";
			selStr += "#L450006210##fc0xFF6799FF#Lv.239#d | "+starImg+" 480 | 그림자가 춤추는 곳#l\r\n";
			selStr += "#L450006300##fc0xFF6799FF#Lv.241#d | "+starImg+" 480 | 폐쇄구역#l\r\n ";
                        selStr += "#L450006410##fc0xFF6799FF#Lv.245#d | "+starImg+" 480 | 그날의 트뤼에페#l\r\n ";
                       selStr += "\r\n\r\n#e#r[에스페라]#d#n\r\n";
                        selStr += "#L450007010##fc0xFF6799FF#Lv.240#d | "+starImg+" 560 | 생명이 시작되는 곳 2#l\r\n";
			selStr += "#L450007050##fc0xFF6799FF#Lv.242#d | "+starImg+" 560 | 생명이 시작되는 곳 5#l\r\n";
			//selStr += "#L450007100##fc0xFF6799FF#Lv.244#d | "+starImg+" 600 | 거울빛에 물든 바다#l\r\n";
			selStr += "#L450007210##fc0xFF6799FF#Lv.248#d | "+starImg+" 640 | 거울에 비친 빛의 신전#l\r\n";
                        selStr += "\r\n\r\n#e#r[문브릿지]#d#n\r\n";
                        selStr += "#L450009110##fc0xFF6799FF#Lv.250#d | "+starImg+" 670 | 사상의 경계#l\r\n";
			selStr += "#L450009210##fc0xFF6799FF#Lv.252#d | "+starImg+" 700 | 미지의 안개#l\r\n";
			selStr += "#L450009310##fc0xFF6799FF#Lv.254#d | "+starImg+" 730 | 공허의 파도#l\r\n";
                        selStr += "\r\n\r\n#e#r[테네브리스]#d#n\r\n";
                        selStr += "#L450011420##fc0xFF6799FF#Lv.255#d | "+starImg+" 760 | 고통의 미궁 내부1#l\r\n";
			selStr += "#L450011510##fc0xFF6799FF#Lv.257#d | "+starImg+" 790 | 고통의 미궁 중심부#l\r\n";
			selStr += "#L450011600##fc0xFF6799FF#Lv.259#d | "+starImg+" 820 | 고통의 미궁 최심부#l\r\n";
                       selStr += "\r\n\r\n#e#r[리멘]#d#n\r\n";
                        selStr += "#L450012030##fc0xFF6799FF#Lv.260#d | "+starImg+" 850 | 세계의 눈물 하단#l\r\n";
			selStr += "#L450012100##fc0xFF6799FF#Lv.261#d | "+starImg+" 850 | 세계의 눈물 중단#l\r\n";
			selStr += "#L450012310##fc0xFF6799FF#Lv.262#d | "+starImg+" 880 | 세계가 끝나는 곳 1-2#l\r\n";
                       //selStr += "#L450012410##fc0xFF6799FF#Lv.262#d "+starImg+" 880 세계가 끝나는 곳 2-2#l\r\n";
                        //selStr += "#L993072000##fc0xFF6799FF#Lv.263#d "+starImg+" 880 레지스탕스 함선 갑판#l\r\n";
                        }
			
			break;
                       
                        case 5:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce#";
        if (cm.getPlayer().getLevel() < 230) {
selStr += "#fs11##fc0xFF000000#해당 사냥터는 #b230레벨#fc0xFF000000#이상부터 입장 가능하다네";
			cm.dispose();
}
        if (cm.getPlayer().getLevel() >= 230) {
			selStr += "#fs11#\r\n사냥터의 #e#fc0xFF6799FF#아케인포스#k#n와 #b몬스터의 평균 레벨#k#n 확인하고\r\n이동하시기 바랍니다.\r\n#fs11#";
			selStr += "\r\n#e#r[셀라스 아케인포스 600]#d#n\r\n";
		            selStr += "#L450016010##fc0xFF6799FF#Lv.245#d | "+starImg+" 600  | 빛이 마지막으로 닿는 곳 1#l\r\n";
			selStr += "#L450016060##fc0xFF6799FF#Lv.245#d | "+starImg+" 600  | 빛이 마지막으로 닿는 곳 6#l\r\n";
			selStr += "\r\n#e#r[셀라스 아케인포스 640]#d#n\r\n";
			selStr += "#L450016130##fc0xFF6799FF#Lv.250#d | "+starImg+" 640  | 끝없이 추락하는 심해3#l\r\n";
		            selStr += "#L450016140##fc0xFF6799FF#Lv.250#d | "+starImg+" 640  | 끝없이 추락하는 심해4#l\r\n";
                                    selStr += "#L450016150##fc0xFF6799FF#Lv.250#d | "+starImg+" 640  | 끝없이 추락하는 심해5#l\r\n";
                                    selStr += "#L450016160##fc0xFF6799FF#Lv.250#d | "+starImg+" 640  | 끝없이 추락하는 심해6#l\r\n";
                                    selStr += "\r\n\r\n#e#r[셀라스 아케인포스 670]#d#n\r\n";
                                    //selStr += "#L100030301##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 재료교환맵#l\r\n";
                                    selStr += "#L450016210##fc0xFF6799FF#Lv.250#d | "+starImg+" 670  | 별이 삼켜진 심해1#l\r\n";
                                    selStr += "#L450016220##fc0xFF6799FF#Lv.250#d | "+starImg+" 670  | 별이 삼켜진 심해2#l\r\n";
                                    selStr += "#L450016230##fc0xFF6799FF#Lv.250#d | "+starImg+" 670  | 별이 삼켜진 심해3#l\r\n";
                                    selStr += "#L450016240##fc0xFF6799FF#Lv.250#d | "+starImg+" 670  | 별이 삼켜진 심해4#l\r\n";
                                    selStr += "#L450016250##fc0xFF6799FF#Lv.250#d | "+starImg+" 670  | 별이 삼켜진 심해5#l\r\n";
                                    selStr += "#L450016260##fc0xFF6799FF#Lv.250#d | "+starImg+" 670  | 별이 삼켜진 심해6#l\r\n";			
 }
			break;

                        case 6:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
        if (cm.getPlayer().getLevel() < 260) {
selStr += "#fs11##fc0xFF000000#해당 사냥터는 #b260레벨#fc0xFF000000#이상부터 입장 가능하다네";
			cm.dispose();
}
        if (cm.getPlayer().getLevel() >= 260) {
			selStr += "#fs11#\r\n사냥터의 #e#fc0xFF6799FF#어센틱포스#k#n와 #b몬스터의 평균 레벨#k#n 확인하고\r\n이동하게나.\r\n#fs11#";
			selStr += "\r\n#e#r[세르니움 어센틱포스 50]#d#n\r\n";
			selStr += "#L410000520##fc0xFF6799FF#Lv.260#d | "+starImg+" 50  | 해변 암석 지대 1#l\r\n";
                                    selStr += "#L410000530##fc0xFF6799FF#Lv.260#d | "+starImg+" 50  | 해변 암석 지대 2#l\r\n";
                                    selStr += "#L410000540##fc0xFF6799FF#Lv.260#d | "+starImg+" 50  | 해변 암석 지대 3#l\r\n";
                                    selStr += "#L410000550##fc0xFF6799FF#Lv.260#d | "+starImg+" 50  | 해변 암석 지대 4#l\r\n";
                                    selStr += "\r\n\r\n#e#r[세르니움 어센틱포스 50]#d#n\r\n";
                                    selStr += "#L410000590##fc0xFF6799FF#Lv.261#d | "+starImg+" 50  | 서쪽성벽1#l\r\n";
                                    selStr += "#L410000600##fc0xFF6799FF#Lv.261#d | "+starImg+" 50  | 서쪽성벽2#l\r\n";
                                    selStr += "#L410000610##fc0xFF6799FF#Lv.261#d | "+starImg+" 50  | 서쪽성벽3#l\r\n";
                                    selStr += "\r\n\r\n#e#r[세르니움 어센틱포스 50]#d#n\r\n";
                                    selStr += "#L410000640##fc0xFF6799FF#Lv.262#d | "+starImg+" 50  | 동쪽성벽1#l\r\n";
                                    selStr += "#L410000650##fc0xFF6799FF#Lv.262#d | "+starImg+" 50  | 동쪽성벽2#l\r\n";
                                    selStr += "#L410000660##fc0xFF6799FF#Lv.262#d | "+starImg+" 50  | 동쪽성벽3#l\r\n";
                                    selStr += "\r\n\r\n#e#r[세르니움 어센틱포스 50]#d#n\r\n";
                                    selStr += "#L410000700##fc0xFF6799FF#Lv.263#d | "+starImg+" 50  | 왕립 도서관 제1구역#l\r\n";
                                    selStr += "#L410000710##fc0xFF6799FF#Lv.263#d | "+starImg+" 50  | 왕립 도서관 제2구역#l\r\n";
                                    selStr += "#L410000720##fc0xFF6799FF#Lv.263#d | "+starImg+" 50  | 왕립 도서관 제3구역#l\r\n";
                                    selStr += "#L410000730##fc0xFF6799FF#Lv.263#d | "+starImg+" 50  | 왕립 도서관 제4구역#l\r\n";
                                    selStr += "#L410000740##fc0xFF6799FF#Lv.263#d | "+starImg+" 50  | 왕립 도서관 제5구역#l\r\n";
                                    selStr += "#L410000750##fc0xFF6799FF#Lv.263#d | "+starImg+" 50  | 왕립 도서관 제6구역#l\r\n";		
                                    selStr += "\r\n\r\n#e#r[불타는세르니움 어센틱포스 70]#d#n\r\n";
                                    selStr += "#L410000920##fc0xFF6799FF#Lv.265#d | "+starImg+" 70  | 격전의 서쪽 성벽1#l\r\n";
                                    selStr += "#L410000930##fc0xFF6799FF#Lv.265#d | "+starImg+" 70  | 격전의 서쪽 성벽2#l\r\n";
                                    selStr += "#L410000940##fc0xFF6799FF#Lv.265#d | "+starImg+" 70  | 격전의 서쪽 성벽3#l\r\n";
                                    selStr += "#L410000950##fc0xFF6799FF#Lv.265#d | "+starImg+" 70  | 격전의 서쪽 성벽4#l\r\n";
                                    selStr += "\r\n\r\n#e#r[불타는세르니움 어센틱포스 100]#d#n\r\n";
                                    selStr += "#L410000980##fc0xFF6799FF#Lv.266#d | "+starImg+" 100  | 격전의 동쪽 성벽1#l\r\n";
                                    selStr += "#L410000990##fc0xFF6799FF#Lv.266#d | "+starImg+" 100  | 격전의 동쪽 성벽2#l\r\n";
                                    selStr += "#L410001000##fc0xFF6799FF#Lv.266#d | "+starImg+" 100  | 격전의 동쪽 성벽3#l\r\n";
                                    selStr += "#L410001010##fc0xFF6799FF#Lv.266#d | "+starImg+" 100  | 격전의 동쪽 성벽4#l\r\n";
                                    selStr += "#L410001020##fc0xFF6799FF#Lv.266#d | "+starImg+" 100  | 격전의 동쪽 성벽5#l\r\n";
                                    selStr += "#L410001030##fc0xFF6799FF#Lv.266#d | "+starImg+" 100  | 격전의 동쪽 성벽6#l\r\n";                                   
                                    selStr += "\r\n\r\n#e#r[불타는세르니움 어센틱포스 100]#d#n\r\n";
                                    selStr += "#L410000840##fc0xFF6799FF#Lv.268#d | "+starImg+" 100  | 불타는 왕립 도서관 제1구역#l\r\n";
                                    selStr += "#L410000850##fc0xFF6799FF#Lv.268#d | "+starImg+" 100  | 불타는 왕립 도서관 제2구역#l\r\n";
                                    selStr += "#L410000860##fc0xFF6799FF#Lv.268#d | "+starImg+" 100  | 불타는 왕립 도서관 제3구역#l\r\n";
                                    selStr += "#L410000870##fc0xFF6799FF#Lv.268#d | "+starImg+" 100  | 불타는 왕립 도서관 제4구역#l\r\n";
                                    selStr += "#L410000880##fc0xFF6799FF#Lv.268#d | "+starImg+" 100  | 불타는 왕립 도서관 제5구역#l\r\n";
                                    selStr += "#L410000890##fc0xFF6799FF#Lv.268#d | "+starImg+" 100  | 불타는 왕립 도서관 제6구역#l\r\n";
 }
			break;
                        
                        case 8:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
			selStr += "#fs11#\r\n중간보스 #e#fc0xFF6799FF#중간보스#k#n와 #e#b데미지가쌔야합니다#k#n 확인하고\r\n이동하게나.\r\n#fs11#";
			selStr += "\r\n#e#r[데미지가 충분한가?]#d#n\r\n";
			selStr += "#L940202049##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 초보중간보스 1#l\r\n";
                                    selStr += "#L940202050##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 중간급 보스#l\r\n";
                                    selStr += "#L940200210##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 고급 보스 #l\r\n";
 
			break;

                        case 9:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
			selStr += "#fs11#\r\n핫타임던전 #e#fc0xFF6799FF#핫타임던전#k#n와 #e#b핫타임던전#k#n 확인하고\r\n이동하게나.\r\n#fs11#";
			selStr += "\r\n#e#r[핫타임던전]#d#n\r\n";
			selStr += "#L993185110##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 1#l\r\n";
                                    selStr += "#L993185120##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 2#l\r\n";
                                    selStr += "#L993185130##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 3 #l\r\n";
                                    selStr += "#L940204350##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 4 #l\r\n";
                                    selStr += "#L940204530##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 5 #l\r\n";
                                    selStr += "#L993134200##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 6 #l\r\n";
 
			break;

                        case 11:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
			selStr += "#fs11#\r\n파티던전 #e#fc0xFF6799FF#핫타임던전#k#n와 #e#b파티던전#k#n 확인하고\r\n이동하게나.\r\n#fs11#";
			selStr += "\r\n#e#r[파티던전]#d#n\r\n";
			selStr += "#L993072000##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 파티초급던전 1#l\r\n";
                                    selStr += "#L993072100##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 파티중급 #l\r\n";
                                    selStr += "#L993072200##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 파티상급 #l\r\n";
                                    //selStr += "#L940204350##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 4 #l\r\n";
                                    //selStr += "#L940204530##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 5 #l\r\n";
                                    //selStr += "#L993134200##fc0xFF6799FF#Lv.200#d | "+starImg+" 0  | 핫타임던전 6 #l\r\n";
 
			break;

                        case 7:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
        if (cm.getPlayer().getLevel() < 260) {
selStr += "#fs11##fc0xFF000000#해당 사냥터는 #b260레벨#fc0xFF000000#이상부터 입장 가능하다네";
			cm.dispose();
}
        if (cm.getPlayer().getLevel() >= 260) {
			selStr += "#fs11#\r\n사냥터의 #e#fc0xFF6799FF#어센틱포스#k#n와 #e#b몬스터의 평균 레벨#k#n 확인하고\r\n이동하게나.\r\n#fs11#";
			selStr += "\r\n#e#r[아르크스 어센틱포스 130]#d#n\r\n";
                                    selStr += "#L410003040##fc0xFF6799FF#Lv.270#d | "+starImg+" 130  | 무법자들이 지배하는 황야 1#l\r\n";
                                    selStr += "#L410003050##fc0xFF6799FF#Lv.270#d | "+starImg+" 130  | 무법자들이 지배하는 황야 2#l\r\n";
                                    selStr += "#L410003060##fc0xFF6799FF#Lv.270#d | "+starImg+" 130  | 무법자들이 지배하는 황야 3#l\r\n";
                                    selStr += "#L410003070##fc0xFF6799FF#Lv.270#d | "+starImg+" 130  | 무법자들이 지배하는 황야 4#l\r\n";
                                    selStr += "\r\n#e#r[아르크스 어센틱포스 160]#d#n\r\n";
                                    selStr += "#L410003090##fc0xFF6799FF#Lv.271#d | "+starImg+" 160  | 낭만이 저무는 자동차 극장 1#l\r\n";
                                    selStr += "#L410003100##fc0xFF6799FF#Lv.271#d | "+starImg+" 160  | 낭만이 저무는 자동차 극장 2#l\r\n";
                                    selStr += "#L410003110##fc0xFF6799FF#Lv.271#d | "+starImg+" 160  | 낭만이 저무는 자동차 극장 3#l\r\n";
                                    selStr += "#L410003120##fc0xFF6799FF#Lv.271#d | "+starImg+" 160  | 낭만이 저무는 자동차 극장 4#l\r\n";
                                    selStr += "#L410003130##fc0xFF6799FF#Lv.271#d | "+starImg+" 160  | 낭만이 저무는 자동차 극장 5#l\r\n";
                                    selStr += "#L410003140##fc0xFF6799FF#Lv.271#d | "+starImg+" 160  | 낭만이 저무는 자동차 극장 6#l\r\n";
                                    selStr += "\r\n#e#r[아르크스 어센틱포스 200]#d#n\r\n";
                                    selStr += "#L410003150##fc0xFF6799FF#Lv.273#d | "+starImg+" 200  | 종착지 없는 횡단 열차1#l\r\n";
                                    selStr += "#L410003160##fc0xFF6799FF#Lv.273#d | "+starImg+" 200  | 종착지 없는 횡단 열차2#l\r\n";
                                    selStr += "#L410003170##fc0xFF6799FF#Lv.273#d | "+starImg+" 200  | 종착지 없는 횡단 열차3#l\r\n";
                                    selStr += "#L410003180##fc0xFF6799FF#Lv.273#d | "+starImg+" 200  | 종착지 없는 횡단 열차4#l\r\n";
                                    selStr += "#L410003190##fc0xFF6799FF#Lv.273#d | "+starImg+" 200  | 종착지 없는 횡단 열차5#l\r\n";
                                    selStr += "#L410003200##fc0xFF6799FF#Lv.273#d | "+starImg+" 200  | 종착지 없는 횡단 열차6#l\r\n";
                                     }
                                   break;
                        
                         case 10:
			starImg = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
			selStr += "#fs11#\r\n엔피시 #e#fc0xFF6799FF#엔피시#k#n와 #e#b엔피시#k#n 확인하고\r\n이동하시기 바랍니다.\r\n#fs11#";
			selStr += "\r\n#e#r[엔피시 요기 다 있음]#d#n\r\n";
                                    selStr += "#L100030300##fc0xFF6799FF#Lv.0#d | "+starImg+" 0  | 엔피시맵#l\r\n";
                                     
                                   break;            
               
                        case 4:
			starImg = "#e#fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce#";
			selStr += "#fs11#\r\n#fc0xFF6799FF#레벨#k#n에 맞는 #b사냥터#k#n 목록만 보인다네\r\n어디로 이동 하겠나?\r\n#fs11#";
			selStr += "#fs11#\r\n#b아래 사냥터에선 승급코인이 드롭된다네#k\r\n\r\n";
        if (cm.getPlayer().getLevel() >= 300) {
                        selStr += "#L993163100##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.300  #d히든 스트리트 - 비극의 성벽 3#l\r\n";
			}
        if (cm.getPlayer().getLevel() >= 340) {
                        selStr += "#L993162600##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.340  #d히든 스트리트 - 성벽 근처 민가 2#l\r\n";
			}
        if (cm.getPlayer().getLevel() >= 400) {
                        selStr += "#L940204490##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.400  #d모라스 - 해방된 폐쇄구역1#l\r\n";
			}
        if (cm.getPlayer().getLevel() >= 440) {
                        selStr += "#L940204510##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.440  #d모라스 - 해방된 폐쇄구역2#l\r\n";
			}
			break;
		}
		cm.sendSimpleS(selStr, 4);
		}
	} else if (status == 3) {
		ans_03 = selection;
		switch(ans_02)
		{
			case 1:
			case 2:
			case 3:
			case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    case 10:
                                    case 11:
			cm.warp(ans_03, "sp");
			cm.dispose();
			break;
		}
	}
}

