importPackage(Packages.handling.world);
importPackage(Packages.handling.channel);
var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    switch (status)
    {
	case 0:
	if ((cm.getPlayer().getJob() != 2500 && cm.getPlayer().getLevel() != 10)) {
		cm.getPlayer().setKeyValue(2, "newchar", "1");
	}
	if (cm.getPlayer().getKeyValue(2, "newchar") == -1)
	{
	cm.gainItem(1142249, 1); // 훈장
	cm.gainItem(2028208, 1); // 무기
	cm.gainItem(2028209, 1); // 방어구
	cm.gainItem(2433509, 1); // 무기
	cm.gainItem(2433510, 1); // 보조무기
	cm.gainItem(1004404, 1); // 모자
	cm.gainItem(1052893, 1); // 한벌옷
	cm.gainItem(1102799, 1); // 망토
	cm.gainPet(5000473, "Pixi★", 1, 100, 100, 30, 103); // 쁘띠 우르스 30일
	cm.gainItem(2000005, 300); // 파워 엘릭서 300개
	cm.getPlayer().setKeyValue(2, "newchar", "1");
	if (cm.getClient().loadCharacters(0).size() == 1) // 놀장강은 최초 1회 지급
	{
		cm.gainItem(2049360, 5); // 놀장강 5장
		cm.gainItem(2434006, 1); // 보스장신구
	}

	}

	cm.sendSimpleS("#d저는 #b서버 도우미#k#d입니다! 혹시 궁금한 사항이 있으신가요?#k\r\n#fc0xFFCC6666#"
			+ "#L0#서버의 배율은 어떻게 되나요?#l\r\n"
			+ "#L1#추천인 / 홍보 / 후원에 관하여 알고 싶어요!#l\r\n"
//			+ "#L2#혹시 잠금된 직업은 언제 플레이 할 수 있나요?#l\r\n"
			+ "#L3#제작에 대해 설명해주세요!#l\r\n"
			+ "#L4#서버의 다양한 컨텐츠에 관한 설명을 듣고싶어요!#l\r\n"
			+ "#L5#각 아이템의 쓰임새에 대해 알고싶어요!#l\r\n", 4);
	break;
	case 1:
	if (selection == 0)
	{
	cm.dispose();
	cm.sendOk("#b픽시의 배율은 100 (경험치) / 7 (메소) / 2 (드롭) 입니다. #k #d재물 획득의 비약, 경험치 2배 쿠폰 등 특정 아이템을 이용해 캐릭터마다 #r배율이 달라질 수#k 있습니다.#k");
	}
	if (selection == 1)
	{
	cm.sendSimpleS("#d궁금하신 사항을 선택해주세요.#k\r\n#fc0xFFCC00FF#"
			+ "#L0#추천인 시스템을 이용하고 싶어요!#l\r\n"
			+ "#L1#추천인 등록 시 얻을 수 있는 혜택이 궁금합니다!#l\r\n"
			+ "#L2#홍보를 통해 얻을 수 있는 보상이 궁금해요!#l\r\n"
			+ "#L3#후원을 통해 얻을 수 있는 보상이 궁금해요!#l\r\n", 4);
	}
	if (selection == 2)
	{
	cm.dispose();
	cm.sendOk("현재 잠금된 직업은 #b와일드헌터, 제로#k입니다. 각각의 직업은 #r빠른 패치를 통해#k 근시일내에 플레이 할 수 있도록 노력하겠습니다. 조금만 기다려주세요!");
	}
	if (selection == 3)
	{
	cm.dispose();
	cm.sendOk("#b제작#k은 재료를 통해 아이템을 제작하는 것을 의미합니다.   크게 #d재료 제작, 장비 제작, 데미지 스킨 제작#k으로 나뉘어지며 추후 추가될 수 있습니다.");
	}
	if (selection == 4)
	{
	cm.sendSimpleS("#d설명을 원하시는 컨텐츠를 골라주세요.#k\r\n#fc0xFF660033#"
			+ "#L10#무릉도장 컨텐츠#l\r\n"
			+ "#L11#낚시 컨텐츠#l\r\n"
			+ "#L12#몬스터파크 컨텐츠#l\r\n"
			+ "#L13#소울 컨텐츠#l\r\n"
			+ "#L14#믹스염색 컨텐츠#l\r\n"
			+ "#L15#길드 컨텐츠#l\r\n"
			+ "#L16#도박 컨텐츠#l\r\n"
			+ "#L17#환생 컨텐츠#l\r\n"
			+ "#L18#변생 컨텐츠#l\r\n"
			+ "#L19#어빌리티 컨텐츠#l\r\n"
			+ "#L20#훈장 컨텐츠#l\r\n"
			+ "#L21#마스터리북 & 레시피 컨텐츠#l\r\n"
			+ "#L22#레벨제한 감소 컨텐츠#l\r\n"
			+ "#L23#메이플 포인트 컨텐츠#l\r\n"
			+ "#L24#잠재능력 고정 컨텐츠#l\r\n"
			+ "#L25#잠재능력 선택 컨텐츠#l\r\n"
			+ "#L26#매트릭스 컨텐츠#l\r\n"
			+ "#L27#유니온 컨텐츠#l\r\n"
			+ "#L28#데일리 기프트 컨텐츠#l\r\n"
			+ "#L29#추가 강화 컨텐츠#l\r\n", 4);
	}
	if (selection == 5)
	{
	cm.sendSimpleS("#d설명을 원하시는 아이템을 골라주세요.#k\r\n#fc0xFFCC6633#"
			+ "#L4001126##i4001126:##z4001126:##l\r\n"
			+ "#L4310210##i4310210:##z4310210:##l\r\n"
			+ "#L4310066##i4310066:##z4310066:##l\r\n"
			+ "#L4310218##i4310218:##z4310218:##l\r\n"
			+ "#L4310225##i4310225:##z4310225:##l\r\n"
			+ "#L4001878##i4001878:##z4001878:##l\r\n"
			+ "#L4001879##i4001879:##z4001879:##l\r\n"
			+ "#L4032618##i4032618:##z4032618:##l\r\n"
			+ "#L4032178##i4032178:##z4032178:##l\r\n"
			+ "#L4032617##i4032617:##z4032617:##l\r\n"
			+ "#L4001109##i4001109:##z4001109:##l\r\n"
			+ "#L2434280##i2434280:##z2434280:##l\r\n"
			+ "#L4001187##i4001187:##z4001187:##l\r\n"
			+ "#L4001188##i4001188:##z4001188:##l\r\n"
			+ "#L4001189##i4001189:##z4001189:##l\r\n"
			+ "#L4021009##i4021009:##z4021009:##l\r\n"
			+ "#L4021010##i4021010:##z4021010:##l\r\n"
			+ "#L4021021##i4021021:##z4021021:##l\r\n", 4);
	}
	break;
	case 2:
	if (selection == 0)
	{
		if (cm.getPlayer().getKeyValue(1, "recommand") == 1)
		{
		cm.dispose();
		cm.sendOk("이미 추천인으로 등록한 캐릭터가 존재합니다.");
		}
		else if (cm.getPlayer().getKeyValue(1, "recommand") == 0)
		{
		cm.dispose();
		cm.sendOk("이미 누군가가 #h 0#님을 추천인으로 등록했습니다.");
		}
		else
		{
		cm.sendGetText("추천인으로 등록할 유저의 이름을 적어주세요. #b해당 유저가 현재 서버에 접속중이여야 합니다.#k");
		}
	}
	if (selection == 1)
	{
	cm.dispose();
	cm.sendOk("추천인 등록 시, 추천받은 사람과 추천한 사람 모두에게 #b#i4310066:##z4310066:# 15개#k가 지급됩니다.");
	}
	if (selection == 2)
	{
	cm.dispose();
	cm.sendOk("홍보로는 블로그 홍보, 유튜브 홍보가 있으며\r\n#b유튜브 20분당 홍보코인 1개 ( 마을잠수 X , 낚시터 허용 )#k, #d블로그 포스팅 사진 갯수 10개당 2개 . ( 뻘 글및 뻘 사진 X )#k 의 보상을 받으실 수 있습니다.");
	}
	if (selection == 3)
	{
	cm.dispose();
	cm.openNpc(1540405);
	}
	if (selection == 10)
	{
	cm.dispose();
	cm.sendOk("무릉도장 컨텐츠는 주어진 시간 내에 보스를 최대한 많이 처치하는 것입니다. 무릉도장에서는 #b감소된 데미지#k가 적용되고, 각 층을 격파하면 #b#i4310225:##z4310225:##k를 얻으실 수 있습니다.");
	}
	if (selection == 11)
	{
	cm.dispose();
	cm.sendOk("낚시터에서 낚시 의자에 앉으면 자동으로 낚시를 합니다.\r\n낚시를 하면 #b#i4001187:##z4001187:# #i4001188:##z4001188:# #i4001189:##z4001189:##k 3가지의 물고기를 얻으실 수 있으며, 각 물고기는 유용한 물품으로 교환이 가능합니다.");
	}
	if (selection == 12)
	{
	cm.dispose();
	cm.sendOk("#r매일 7회#k 몬스터 파크를 클리어 하실 수 있습니다. 몬스터 파크를 클리어하면 #b각 요일에 맞는 상자#k가 드롭되고, 상자에서 여러 물품을 얻으실 수 있습니다.");
	}
	if (selection == 13)
	{
	cm.dispose();
	cm.sendOk("#b#i4001879:##z4001879:##k 아이템을 이용하여 마을에서 원하시는 소울을 구매하실 수 있습니다.");
	}
	if (selection == 14)
	{
	cm.dispose();
	cm.sendOk("캐시샵을 통해 원하시는 색으로 믹스염색이 가능합니다.");
	}
	if (selection == 15)
	{
	cm.dispose();
	cm.sendOk("길드에 가입하면 #b길드 스킬, 길드 출석, 길드 레이드#k등의 컨텐츠를 진행하실 수 있습니다. 각 컨텐츠에서 유용한 물품을 얻으실 수도 있습니다.");
	}
	if (selection == 16)
	{
	cm.dispose();
	cm.sendOk("#b원하시는 양만큼의 메소를 배팅#k하여 매 10분마다 도박 결과를 통해 2배 혹은 절반의 메소를 얻으실 수 있습니다.");
	}
	if (selection == 17)
	{
	cm.dispose();
	cm.sendOk("레벨 120 이상에서 #b환생#K을 이용하여 원하시는 레벨로 환생하고, #b환생 포인트#k를 얻으실 수 있습니다.");
	}
	if (selection == 18)
	{
	cm.dispose();
	cm.sendOk("변생 컨텐츠는 현재 준비중입니다.");
	}
	if (selection == 19)
	{
	cm.dispose();
	cm.sendOk("캐시샵을 통해 어빌리티를 개방하실 수 있고, #b명성치 또는 서큘레이터#k로 어빌리티를 재설정할 수 있습니다.");
	}
	if (selection == 20)
	{
	cm.dispose();
	cm.sendOk("#b특정 업적 달성#k 시 훈장 획득이 가능합니다. 일부 업적은 공개되어있지 않기도 합니다.");
	}
	if (selection == 21)
	{
	cm.dispose();
	cm.sendOk("마스터리 북과 레시피를 마을의 엔피시에게 판매하여 메소를 획득하실 수 있습니다.");
	}
	if (selection == 22)
	{
	cm.dispose();
	cm.sendOk("#b#i4310225:##z4310225:##k 아이템을 통해 장비 아이템의 레벨 제한을 감소할 수 있습니다.");
	}
	if (selection == 23)
	{
	cm.dispose();
	cm.sendOk("환생 포인트와 메이플 포인트는 같은 개념이며, 메이플 포인트로 전환하여 유저와의 교환이 가능합니다.");
	}
	if (selection == 24)
	{
	cm.dispose();
	cm.sendOk("환생 포인트를 이용하여 #b잠재능력을 고정#k하실 수 있습니다.");
	}
	if (selection == 25)
	{
	cm.dispose();
	cm.sendOk("#b#i4032618:##z4032618:##k 아이템을 통해 원하시는 아이템의 잠재능력 설정이 가능합니다. \r\n#b보스 몬스터 #e#r스우, 데미안, 루시드#k#n#b에게 얻을 수 있습니다.");
	}
	if (selection == 26)
	{
	cm.dispose();
	cm.sendOk("마을의 매트릭스 관리자를 통해 쉽게 분해 및 강화가 가능하고, 캐시샵의 매트릭스로 코어 생성이 가능합니다.");
	}
	if (selection == 27)
	{
	cm.dispose();
	cm.sendOk("마을의 엔피시를 통해 매일 유니온 보상을 받으실 수 있습니다. #r계정 캐릭터들의 레벨 합을 통해 유니온 효과를 증폭시킬 수 있습니다.");
	}
	if (selection == 28)
	{
	cm.dispose();
	cm.sendOk("#b하루에 한 번#k, 데일리 기프트를 통해 아이템을 얻으실 수 있습니다.");
	}
	if (selection == 29)
	{
	cm.dispose();
	cm.sendOk("#b#i4001109:##z4001109:# 아이템을 통해 아이템을 추가로 30성까지 강화하실 수 있습니다. \r\n#b보스 몬스터 #e#r스우, 데미안, 루시드#k#n#b에게 얻을 수 있습니다.");
	}
	if (selection == 4001126)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "# 은 주요 화폐로서, 사냥을 통해 얻으실 수 있고, #r#i4310210##z4310210# 합성, 강화 아이템 구매#k 등에 사용됩니다.");
	}
	if (selection == 4310210)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 #i4001126##z4001126# 합성을 통해 얻으실 수 있으며, 상점에서 자주 쓰이고, #r특수 장비 및 앱솔랩스 구매, 핑크빛 성배 구매#k 등에 사용됩니다.");
	}
	if (selection == 4310066)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 홍보 코인으로, #r홍보를 통해 얻으실 수 있으며#k 홍보상점에서 사용 가능합니다.");
	}
	if (selection == 4310218)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 후원 코인으로, #r후원을 통해 얻으실 수 있으며#k 후원상점에서 사용 가능합니다.");
	}
	if (selection == 4310225)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 무릉도장 또는 끈기의 숲 및 인내의 숲에서 얻으실 수 있으며 #r아이템의 레벨 제한을 낮출 때#k 사용됩니다.");
	}
	if (selection == 4001878)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 #i4310225##z4310225# 합성을 통해 얻으실 수 있으며, #r아케인 셰이드 방어구#k 구매에 사용됩니다.");
	}
	if (selection == 4001879)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 #i4310210##z4310210# 합성을 통해 얻으실 수 있으며, #r아케인 셰이드 무기#k 구매에 사용됩니다.");
	}
	if (selection == 4032618)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 아이템의 잠재 능력 설정에 사용되며, #b보스 몬스터 #e#r스우, 데미안, 루시드#k#n#b에게 얻을 수 있습니다. ");
	}
	if (selection == 4032178)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 뱃지 / 엠블렘 / 보조무기의 옵션 설정에 사용되며, #b보스 몬스터 #e#r스우, 데미안, 루시드#k#n#b에게 얻을 수 있습니다.");
	}
	if (selection == 4032617)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 캐시 아이템의 잠재 능력 설정에 사용되며, 후원을 통해 얻으실 수 있습니다.");
	}
	if (selection == 4001109)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 아이템의 추가 강화에 사용되며, #b보스 몬스터 #e#r스우, 데미안, 루시드#k#n#b에게 얻을 수 있습니다.");
	}
	if (selection == 2434280)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 20억의 가치를 지닌 아이템으로, 화폐로서 사용됩니다.");
	}
	if (selection == 4001187)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#는 낚시를 통해 얻을 수 있는 물고기의 한 종류로, 유용한 아이템 구매에 사용됩니다.");
	}
	if (selection == 4001188)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#는 낚시를 통해 얻을 수 있는 물고기의 한 종류로, 유용한 아이템 구매에 사용됩니다.");
	}
	if (selection == 4001189)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#는 낚시를 통해 얻을 수 있는 물고기의 한 종류로, 유용한 아이템 구매에 사용됩니다.");
	}
	if (selection == 4021009)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 조합을 통해 얻을 수 있는 재료의 한 종류로, 장신구 조합에 사용됩니다.");
	}
	if (selection == 4021010)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 조합을 통해 얻을 수 있는 재료의 한 종류로, 방어구 조합에 사용됩니다.");
	}
	if (selection == 4021021)
	{
	cm.dispose();
	cm.sendOk("#i" + selection + "##z" + selection + "#은 조합을 통해 얻을 수 있는 재료의 한 종류로, 반지 조합에 사용됩니다.");
	}
	break;
	case 3:
	cm.dispose();
	if (cm.getText().equals(cm.getName()))
	{
	cm.sendOk("자기 자신은 추천인으로 등록하실 수 없습니다.");
	return;
	}
	ch = World.Find.findChannel(cm.getText());
	if (ch != -1)
	{
	victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(cm.getText());
	victim.dropMessage(-1, cm.getName() + " 유저가 당신을 추천인으로 등록했습니다.");
	victim.setKeyValue(1, "recommand", "0");
	victim.gainItem(4310066, 15);
	cm.gainItem(4310066, 15);
	cm.getPlayer().setKeyValue(1, "recommand", "1");
	cm.sendOk("아이템이 지급 완료되었습니다.");
	}
	else
	{
	cm.sendOk(cm.getText() + " 유저는 현재 서버에 접속중이지 않습니다.");
	cm.dispose();
	}
    }
}

