importPackage(Packages.provider);
importPackage(Packages.tools);
importPackage(Packages.client.skills);
importPackage(Packages.client);
importPackage(java.lang);
importPackage(java.io);


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

	if (status == 0)
	{
		if(cm.getClient().getUnionLevel() < 500 && !cm.getPlayer().isGM())
		{
			cm.sendOkS("음~ 아직 저를 만나시기엔 조금 이르신 것 같군요?\r\n#b#e유니온 레벨 500 이상#k#n, #r#e보유 캐릭터 3개 이상#k#n인 경우에만 메이플 유니온을 이용할 수 있어요.\r\n\r\n#e※ 유니온 레벨이란?#n\r\n#r월드 내 60 레벨 이상, 2차 전직을 마친 캐릭터의 #b레벨을 모두 합친 값#k으로 40명 이상의 캐릭터를 보유한 경우 높은 레벨부터 40명의 레벨만 합산된다. 단, #r제로#k의 경우 가장 레벨이 높은 1명의 레벨만 합산된다.", 4, 9010106);
			cm.dispose();
			return;
		}

		UNION_GRADE = cm.getPlayer().getKeyValue(1, "UNION_GRADE");

		if(UNION_GRADE == -1)
		{
			cm.getPlayer().setKeyValue(1, "UNION_GRADE", 0);
		}


			cm.getPlayer().setKeyValue(1, "UNION_GRADE", 5);


		cm.sendSimpleS("용사님! 더 강한 유니온을 만들어 보시겠어요?\r\n\r\n"
			+ "#e#r[메이플 유니온 정보]#b#n\r\n"
			+ "#L0#나의 메이플 유니온 정보를 확인한다.#l\r\n"
			+ "#L1#나의 메이플 유니온 스탯을 확인한다.#l\r\n"
			+ "#L2#메이플 유니온에 대한 설명을 든는다.#l\r\n"
			+ "\r\n\r\n#e#r[메이플 유니온 성장]#b#n\r\n"
			+ "#L10#메이플 유니온 등급을 올린다.#l\r\n"
			+ "#L11#메이플 유니온 스탯을 올린다.#l\r\n"
			+ "#L12#오늘의 유니온 보상을 받는다. (1일 1회 수령가능)#l\r\n", 4, 9010106);
	}

	else if(status == 1)
	{
		sel_00 = selection;
		switch(sel_00)
		{
			case 0:
			cm.sendOkS("용사님의 #e메이플 유니온#n 정보를 알려드릴까요?\r\n\r\n"
				 + "  #e #b-#k 유니온 등급 | "+getUnionFullString()+"#b #n("+UNION_GRADE+" 등급)#k\r\n"
				 + "  #e #b-#k 유니온 레벨 | #b#n"+cm.getClient().getUnionLevel()+" 레벨\r\n\r\n", 4, 9010106);
			cm.dispose();
			break;

			case 1:
			txt = "용사님이 투자한 유니온 스탯 상황입니다.\r\n\r\n"
				+ " #r-#k #e사용한 유니온 스탯 포인트 : #r#e"+cm.getPlayer().getKeyValue(1, "UNION_STAT_USED")+" AP#k#n\r\n"
				+ " #r-#k #e사용 가능한 유니온 스탯 포인트 : #r"+cm.getPlayer().getKeyValue(1, "UNION_STAT")+" AP#k#n\r\n\r\n";

			SKILL_LIST = 71004000;
			PER_LEVEL = function(skillid)
					{
						switch(skillid-71004000)
						{
							//STR, DEX, INT, LUK
							case 0:
							case 1:
							case 2:
							case 3:
								return 5;
								break;

							//공(4), 마(5), 내성(9) 크확(11), 보공(12), 스탠스(13), 벞지(14), 방무(15)
							case 4:
							case 5:
							case 9:
							case 11:
							case 12:
							case 13:
							case 14:
							case 15:
								return 1;
								break;

							//최대HP(6), 최대MP(7)
							case 6:
							case 7:
								return 250;
								break;

							//크리티컬 데미지(8), 획득 경험치(10)
							case 8:
							case 10:
								return 0.5;
								break;
						}
					}
				
			for(i = 0; i < 16; i++)
			{
				txt += " #r-#k #e#q"+(SKILL_LIST + i)+"##k#n : #b#e＋"+cm.getPlayer().getSkillLevel((SKILL_LIST + i)) * PER_LEVEL(SKILL_LIST + i)+"#k#n / "+PER_LEVEL(SKILL_LIST + i) * SkillFactory.getSkill(SKILL_LIST + i).getMaxLevel()+"\r\n";
			}
			cm.sendOkS(txt, 4, 9010106);
		}
	}
}

function getUnionFullString()
{
	UNION_GRADE_STRING = ["#fc0xFFCC723D#노비스", "#fc0xFF81929A#베테랑", "#fc0xFFD9B084#마스터", "#fc0xFF8741B5#그랜드 마스터"];
	if(UNION_GRADE > -1)
	i = 0;

	if(UNION_GRADE > 5)
	i = 1;

	if(UNION_GRADE > 10)
	i = 2;

	if(UNION_GRADE > 15)
	i = 3;

	switch(Math.floor(UNION_GRADE % 5))
	{
		case 0:
			ii = 5; break;
		default:
			ii = UNION_GRADE % 5;
	}

	return ""+UNION_GRADE_STRING[i]+" 유니온 "+ii+"단계";
}