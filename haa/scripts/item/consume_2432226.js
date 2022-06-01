importPackage(java.lang);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
        if (mode == 1)
            status++;
        else	
            status--;
        if (status == 0) {
	var chat = " 직업변경을할 직업류를 골라주세요! \r\n\r\n";
        chat += "#L0##r#fn돋움##fs14##e랜덤#n#fs##fn##l"; 
	chat += "#L1##r#fn돋움##fs14##e전사류#n#fs##fn##l";
        chat += "#L2##r#fn돋움##fs14##e법사류#n#fs##fn##l";
        chat += "#L3##r#fn돋움##fs14##e궁수류#n#fs##fn##l";
        chat += "#L4##r#fn돋움##fs14##e도적류#n#fs##fn##l";
        chat += "#L5##r#fn돋움##fs14##e해적류#n#fs##fn##l";
	cm.sendSimpleS(chat,2);
	} else if (status == 1) {
	if (selection != -1 && selection != 0) {
        if(cm.haveItem(4310086,5)) {
        cm.gainItem(4310086, -5);
        cm.승급(selection,false);
        cm.dispose();
         } else {
        cm.sendOk("자네... 분명 필요한 아이템을 제대로 갖고 있는건가? \r\n 직업 변경을위해서는 자유전직코인 5개가 필요하다네 !");
       cm.dispose();
         }
	}
	cm.dispose();
	} else if (status < 0) {
	cm.dispose();
    }
}