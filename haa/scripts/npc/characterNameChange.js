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
		cm.sendYesNoS("안녕하세요! 당신의 이름을 바꿔드릴 수 있는 #b미스터 뉴네임#k입니다. 캐릭터 이름을 변경하시겠습니까?", 4);

	}

	else if(status == 1)
	{
//		if(!cm.haveItem(4034803))
//		{
//			cm.sendOkS("#b#i4034803:# #t4034803:##k이 없으면 캐릭터 이름을 바꿀 수 없습니다.", 4);
//			cm.dispose();
//			return;
//		}

		cm.sendGetText("\r\n\r\n　　 #fs18##fn나눔고딕 ExtraBold##fc0xFF47C83E##e새로운 캐릭터 이름을 입력해주세요!#fn돋움##fs12##k#n\r\n\r\n"
			+ "\r\n#e#r※ 주의사항#k#n\r\n#fn돋움체#"
			+ "　1. 이미 존재하는 이름으로는 변경할 수 없습니다.\r\n"
			+ "　2. 일부 단어가 포함된 이름으로는 변경할 수 없습니다.\r\n"
			+ "　3. 캐릭터 이름 변경 시 이전에 제작된 아이템에 표시된\r\n　   이름은 변경되지 않습니다.\r\n"
			+ "　4. 길드 스킬 변경 시 기상효과로 표시되는 이름은 변경\r\n　   되지 않습니다.\r\n"
			+ "　5. 동상으로 제작된 #fn돋움#NPC#fn돋움체#의 이름은 변경되지 않습니다.\r\n ");
	}

	else if(status == 2)
	{
		newName = cm.getText();
		if(!Packages.client.MapleCharacterUtil.canCreateChar(cm.getText(), false) || newName.contains("\"") || newName.contains("'") || newName.contains("`") || newName.contains(".") || newName.contains("GM") || newName.contains("운영"))
		{
			cm.getPlayer().dropMessage(1, "[ "+newName+" ]\r\n사용할 수 없는 닉네임입니다.");
			cm.dispose();
			return;
		}

		if(Packages.client.MapleCharacterUtil.getIdByName(newName) != -1)
		{
			cm.getPlayer().dropMessage(1, "[ "+newName+" ]\r\n이미 존재하는 닉네임입니다.");
			cm.dispose();
			return;
		}

		cm.sendYesNoS("정말 #e#b"+newName+"#k#n(으)로 닉네임을 변경하시겠습니까?", 4);
	}

	else if(status == 3)
	{
		beforeName = cm.getPlayer().getName();
		cm.getPlayer().setName(newName);
		cm.fakeRelog();
		cm.updateChar();
		cm.sendOkS("#Cgray##e"+beforeName+"#k#n에서 #e#b"+newName+"#k#n(으)로 닉네임이 변경되었습니다. 이용해주셔서 감사합니다.", 4);
//		cm.gainItem(4034803, -1);
		cm.dispose();
	}
}

