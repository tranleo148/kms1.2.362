var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {
		var chat = "#fn나눔고딕 Extrabold#혁신은 항상 세상을 이끌고 변화를 가져오지요..\r\n";
		chat += "당신도 당신 내면에 있는 혁신을 대면하고 싶으신가요?\r\n";
		chat += "원하시면.. 저의 힘을 빌려드리도록 하지요..\r\n\r\n";
		chat += "[ #b혁신의 룰렛#k 가동 조건 : #b#i4310175##k #r30 개#k ]\r\n";
		chat += "\r\n#Cgray#1. 30 ~ 40 점 = 보스 공격력, 데미지, 올스텟 5 % 증가\r\n2. 50 ~ 60 점 = 보스 공격력, 데미지, 올스텟 7 % 증가\r\n3. 60 ~ 70 점 이상 = 보스 공격력, 데미지, 올스텟 11 % 증가#k";
		
                chat += "\r\n\r\n                                           #d[혁신의 링]#k\r\n";
		chat += "\r\n                                #i1112585#     #i1112586#     #i1112663#";
		chat += "\r\n                                  #i1112318#     #i1112319#     #i1112320#";
                chat += "\r\n\r\n                                       #d[혁신의 펜던트]#k\r\n";
		chat += "\r\n                   #i1123007#(STR)     #i1123008#(DEX)     #i1123009#(INT)";
		chat += "\r\n                   #i1123010#(LUK)     #i1123011#(MHP)   #i1123012#(AST)";
		chat += "\r\n\r\n#L0##b네, 지금 당장 진행하고 싶어요.#k#l";
		chat += "\r\n\r\n   #r* 단, 장비 창을 꼭 1 칸 이상 비워야 정상 수령이 가능합니다.#k";
		cm.sendSimple(chat);
        } else if (status == 1) {
		if (cm.haveItem(4310175,30)) {
			cm.sendOk("#fn나눔고딕 Extrabold#당신의 #b내면의 힘#k 을 믿어보세요.\r\n눈을 크게 뜨고 #r룰렛 점수#k 를 #d확인#k 해주세요.");
	   	} else {
			cm.sendOk("#fn나눔고딕 Extrabold##r스타M 코인이 부족하면 당신의 내면을 바라볼 수 없어요.#k");
			cm.dispose();
		}
	} else if (status == 2) {
		cm.gainItem(4310175,-30);
		cm.getPlayer().혁신의룰렛();
		cm.dispose();
	} else {
		cm.dispose();
	}
    }
}