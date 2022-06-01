importPackage(Packages.constants);
importPackage(Packages.packet.creators);importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.life);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world)
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.life);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world)
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
	rotation = 0;
	action(1, 0, 0);
}

function send(i, str)
{
	/*
		20 : "아쿠아"
		21 : "아이템 확성기"
		22 : "파란색 [노란색] 파란색"
		23 : "골드"
		24 : "파란색 [노란색(굵게)] 파란색"
		25 : "이름 : 내용 (보라 확성기)"
		26 : "연보라 [노랑] 연보라"
		27 : "골드" (=23)
		28 : "오렌지" (나눔고딕)
		30 : "보라"
	*/
	cm.getPlayer().dropMessage(6, str);
}

function Comma(i)
{
	var reg = /(^[+-]?\d+)(\d{3})/;
	i+= '';
	while (reg.test(i))
	i = i.replace(reg, '$1' + ',' + '$2');
	return i;
}

function action(M, T, S)
{
	if(M != 1)
	{
		cm.dispose();
		return;
	}


	if(M == 1)
        {
	    St++;
	} 
        else
        {
            St--;
        }

	if(St == 0)
	{
		if(!cm.getPlayer().isGM())
		{
			cm.sendOk("안녕하세요? 메이플월드를 여행하는 일은 즐거우신가요?");
			cm.dispose();
			return;
		}
		cm.sendSimple("안녕하세요? 메이플월드를 여행하는 일은 즐거우신가요?\r\n"
			+ "#L0##r대화를 끝낸다.#l\r\n"
			+ "#L1##b아이템의 옵션을 변경한다.#l\r\n"
			+ "#L2##b잠재능력 코드를 확인한다.#l");
	}

	else if(St == 1)
	{
		S1 = S;
		switch(S1)
		{
			case 1:
			inz = cm.getInventory(1)
			txt = "현재 #b#h ##k 님이 보유하고 있는 장비 아이템 목록입니다. 인벤토리에 정렬된 순서로 출력되었으니 #r옵션을 변경하고 싶은 아이템#k을 선택해주세요.\r\n#b#fs11#";
			for(w = 0; w < inz.getSlotLimit(); w++)
			{
				if(!inz.getItem(w))
				{
					continue;
				}
				txt += "#L"+ w +"##i"+inz.getItem(w).getItemId()+":# #t"+inz.getItem(w).getItemId()+"##l\r\n";
			}
			cm.sendSimple(txt);
			break;

			case 2:
			send(00, "　　");
			send(00, "　　");
			send(00, "　　");
			send(10, "　< 주요 스탯% 관련 잠재능력 코드 >");
			send(20, "　힘　: +3%(10041)　힘　: +6%(20041)　힘　: +9%(30041)　힘　: +12%(40041)"); 
			send(20, "　덱스: +3%(10042)　덱스: +6%(20042)　덱스: +9%(30042)　덱스: +12%(40042)");
			send(20, "　인트: +3%(10043)　인트: +6%(20043)　인트: +9%(30043)　인트: +12%(40043)");
			send(20, "　럭　: +3%(10044)　럭　: +6%(20044)　럭　: +9%(30044)　럭　: +12%(40044)"); 
			send(20, "　올스텟: +9%(40086)　　     올스텟: +12%(40081)　　　  올스텟: +20%(60002)");
			send(27, "　　");
			send(10, "　< 기타 스탯% 관련 잠재능력 코드 >");
			send(20, "　최대체력: +3%(10045)　최대체력: +6%(20045)　최대체력: +9% (30045)　최대체력: +12%(40045)");
			send(20, "　최대마나: +3%(10046)　최대마나: +6%(20046)　최대마나: +9% (30046)　최대마나: +12%(40046)");
			send(20, "　회피치　: +3%(10048)　회피치　: +6%(20048)　회피치　: +9% (30048)　회피치　: +12%(40048)");
			send(27, "　　");
			send(10, "　< 무기 관련 잠재능력 코드 >");
			send(27, "　데미지: +6%(20070)　데미지: +9%(30070)　데미지: +12%(40070)");
			send(27, "　공격력: +6%(20051)　공격력: +9%(30051)　공격력: +12%(40051)");
			send(27, "　마력　: +6%(20052)　마력　: +9%(30052)　마력　: +12%(40052)");
			send(27, "　　");
			send(10, "　< 몬스터 방어율 무시 관련 잠재능력 코드 >");
			send(27, "　+15%(10291)　+20%(20291)　+30%(30291)　+35%(40291)　+40%(40292)");
			send(27, "　　");
			send(10, "　< 보스 몬스터 공격시 데미지 관련 잠재능력 코드 >");
			send(27, "　+20%(30601)　+25%(40601)　+30%(30602)　+35%(40602)　+40%(40603)");
			send(27, "　　");
			send(10, "　< 크리티컬 관련 잠재능력 코드 >");
			send(27, "　크리티컬 발동: +8%(20055)　크리티컬 발동: +10%(30055)　크리티컬 발동: +12%(40055)");
			send(27, "　크리티컬 최소 데미지: +15%(40056)　　　　　　　  크리티컬 최대 데미지: +15%(40057)");
			send(27, "　　");
			send(10, "　< 장신구 · 방어구 관련 잠재능력 코드 >");
			send(05, "　메소 획득량 증가: +20%(40650)　아이템 획득 확률 증가: +20%(40656)");
			send(05, "　피격 후 무적시간: 1초(20366)　　피격 후 무적시간: 2초(30366)　　피격 후 무적시간: 3초(40366)");
			send(27, "　　");
			send(10, "　< 쓸만한 스킬 관련 잠재능력 코드 >");
			send(05, "　(유니크)　 헤이스트(31001)　미스틱 도어(31002)　샤프 아이즈(31003)　하이퍼 바디(31004)");
			send(05, "　(레전드리) 컴뱃 오더스(41005)　　　  어드밴스드 블레스(41006)　　　  윈드 부스터(41007)");
			cm.getPlayer().dropMessage(1, "채팅창을 최대로 확대하면 모든 내용이 표시됩니다.");
                              //World.Broadcast.broadcastMessage(CField.getGameMessage(28, "test"));
			cm.dispose();
			break;
			default:
			cm.dispose();
			break;
		}
	}

	else if(St > 1)
	{
		if(rotation != -1)
		{
			switch(St)
			{
				case 2: S2 = S; break;
				case 3: S3 = S; break;
				case 4: S4 = S; break;
			}
                        if (St ==4 && rotation == 2)
			{
			inz.setArc(3000);
				switch(S3)
				{
					case 0: inz.setStr(S4); break;
					case 1: inz.setDex(S4); break;
					case 2: inz.setInt(S4); break;
					case 3: inz.setLuk(S4); break;
					case 4: inz.setHp(S4); break;
					case 5: inz.setMp(S4); break;
					case 6: inz.setWatk(S4); break;
					case 7: inz.setMatk(S4); break;
					case 8: inz.setWdef(S4); break;
					case 9: inz.setMdef(S4); break;
					case 10: inz.setAcc(S4); break;
					case 11: inz.setAvoid(S4); break;
					case 12: inz.setSpeed(S4); break;
					case 13: inz.setJump(S4); break;
					case 14: inz.setLevel(S4); break;
					case 15: inz.setUpgradeSlots(S4); break;
					case 16: inz.setEnhance(S4); break;
					case 17: inz.setAmazingequipscroll(true); break;
					case 18: inz.setBossDamage(S4); break;
					case 19: inz.setIgnorePDR(S4); break;
					case 20: inz.setTotalDamage(S4); break;
					case 21: inz.setAllStat(S4); break;
					case 22: inz.setDownLevel(-S4); break;
					case 23: inz.setState(S4); break;
					case 24: inz.setPotential1(S4); break;
					case 25: inz.setPotential2(S4); break;
					case 26: inz.setPotential3(S4); break;
					case 27: inz.setPotential4(S4); break;
					case 28: inz.setPotential5(S4); break;
					case 29: inz.setPotential6(S4); break;
					case 30: inz.setArcLevel(S4); break;
					case 31: inz.setArc(S4); break;
					case 32: inz.setArcEXP(S4); break;
				}
		        cm.getPlayer().forceReAddItem(inz, Packages.client.inventory.MapleInventoryType.EQUIP);
                        rotation = 0;
			St = 2;
                        }
		}
		else
		{
			S2 = S2;
			rotation++;
		}
                
		addItemInfo();
	}
}

function addItemInfo()
{
	if(rotation == 0)
	{
		inz = cm.getInventory(1).getItem(S2);
		txt = "#r#e[아이템 기본 옵션]#n\r\n#b#fs11#";
		sel = ["힘", "덱스", "인트", "럭", "최대 체력(HP)", "최대 마나(MP)", "공격력", "마력", "물리방어력", "마법방어력", "명중률", "회피치", "스피드", "점프력", "주문서 성공 횟수", "업그레이드 가능 횟수", "스타포스 성공 횟수", "가위 사용 가능 횟수", "보스 공격 시 데미지", "몬스터 방어력 무시", "총 데미지", "올스탯", "착용 레벨 감소", "잠재능력 등급", "1 번째 잠재능력", "2 번째 잠재능력", "3 번째 잠재능력", "4 번째 잠재능력", "5 번째 잠재능력", "6 번째 잠재능력", "아케인 포스 레벨", "아케인 포스 수치", "아케인 포스 경험치"];
		for(y = 0; y < sel.length; y++)
		{
			txt += "#L"+ y +"#"+sel[y]+"#l";
			if(y == 5 || y == 9 || y == 15 || y == 19 || y == 23 || y == 26)
			{
				txt += "\r\n";
			}
			if(y == 13)
			{
				txt += "\r\n\r\n\r\n#r#e#fs12#[아이템 강화 옵션]#b#n#fs11#\r\n";
			}
			if(y == 17)
			{
				txt += "\r\n\r\n\r\n#r#e#fs12#[아이템 추가 옵션]#b#n#fs11#\r\n";
			}
			if(y == 22)
			{
				txt += "\r\n\r\n\r\n#r#e#fs12#[아이템 잠재 능력]#b#n#fs11#\r\n";
			}
			if(y == 29)
			{
				txt += "\r\n\r\n\r\n#r#e#fs12#[아케인 포스 능력]#b#n#fs11#\r\n";
			}

		}
		cm.sendSimple(txt);
		rotation++;
	}

	else if(rotation == 1)
	{
		switch(S3)
		{
			//STR, DEX, INT, LUK, MaxHp, MaxMp, Watk, Matk, PDD, MDD, ACC, AVOID, SPEED, JUMP, ARC
			case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 31:
			max = 32767;
			break;

			//LEVEL, SLOT
			case 14: case 15:
			max = 127;
			break;

			//STARFORCE, ENHANCE
			case 16: case 17: case 32:
			max = 255;
			break;

			//ADDOPTIONS
			case 18: case 19: case 20: case 21: case 22:
			max = 100;
			break;

			case 30:
			max = 20;
			break;

			default:
			max = 99999;
			break;
		}

		if(S3 != 23)
		{
			if(max != 99999)
			{
				cm.sendGetNumber("변경을 원하는 #b"+sel[S3]+"#k 수치의 값을 입력해주세요.\r\n#r(#e"+Comma(max)+"#n 보다 높은 값을 입력할 수 없답니다.)", 0, 0, max);
			}
			else
			{
				cm.sendGetNumber("변경을 원하는 #b"+sel[S3]+"#k 수치의 값을 입력해주세요.\r\n#r(잠재능력 코드를 모른다면 저를 통해 확인이 가능하답니다.)", 0, 0, max);
			}				
		}
		else
		{
			cm.sendSimple("변경을 원하는 #b"+sel[S3]+"#k 수치의 값을 선택해주세요.\r\n#fs11##r"
				+ "#L0#잠재능력 등급 없음#l\r\n\r\n\r\n"
				+ "#fs12##e[미확인 잠재능력 등급]#b#n#fs11#\r\n"
				+ "#L1#레어#l#L2#에픽#l#L3#유니크#l#L4#레전드리#l\r\n\r\n\r\n"
				+ "#fs12##e#r[확인된 잠재능력 등급]#b#n#fs11#\r\n"
				+ "#L17#레어#l#L18#에픽#l#L19#유니크#l#L20#레전드리#l\r\n");
		}
		rotation++;
	}
}		