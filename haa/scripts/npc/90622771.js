var enter = "\r\n";
var seld = -1, seld2 = -1;

var pink = "#fc0xFFF781D8#";
var black = "#fc0xFF000000#";
var purple = "#fc0xFF7401DF#";
var sky = "#fc0xFF58ACFA#";
var uta = "#fc0xFFF15F5F#";
var dao = "#fc0xFF4374D9#";
var gre = "#fc0xFF77bb00#";
var white = "#fc0xFFFFFFFF#";
var enter = "\r\n";
var space = white+"[][][]#k"+dao;

var icon = "#fUI/UIWindow4.img/pointShop/";
var icon2 = "#fItem/Etc/0431.img/04310237/info/iconShop#";

//앱솔 및 아케인 상점 수정하기

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg = purple+":: 무엇이든지 다 있습니다. 골라보세요! #k\r\n";
		msg +="#L1##fs11##fUI/Basic.img/BtCoin/normal/0##fc0xFFFF3300# 소비상점#l"+space+"#fc0xFF000099##L6#"+icon+"100711/iconShop# 네오스톤#l"+space+"#L11#"+icon+"501053/iconShop##fc0xFFFF3366# 도네이션 (후원)#l\r\n";
		msg +="#L2##fUI/Basic.img/BtCoin/normal/0##fc0xFFFF3300# 기본악세#l"+space+"#fc0xFF000099##L7#"+icon+"100712/iconShop# 휴식상점#l"+space+"#L12#"+icon+"501053/iconShop##fc0xFFFF3366# 서포터즈 (홍보)#l\r\n";
		msg +="#L3##fUI/Basic.img/BtCoin/normal/0##fc0xFFFF3300# 모루상점#l"+space+"#fc0xFF000099##L8#"+icon+"501215/iconShop# 네오코어#l"+space+"#fc0xFF6633FF##L13#"+icon+"500629/iconShop# 유니온샵#l\r\n";
		msg +="#L4##fUI/Basic.img/BtCoin/normal/0##fc0xFFFF3300# 캐시상점#l"+space+"#fc0xFF6633FF##L9#"+icon+"100508/iconShop# 출석상점#l"+space+"#fc0xFF6633FF##L14#"+icon+"100508/iconShop# 뽑기상점#l\r\n";
		msg +="#L5##fUI/Basic.img/BtCoin/normal/0##fc0xFFFF3300# 보조무기#l"+space+"#fc0xFF6633FF##L10#"+icon+"4001886/iconShop# 보스결정#l"+space+"";
msg +="#fc0xFF6633FF##L15#"+icon2+" AQUA 코인#l\r\n\r\n";
		msg +="#Cgray##fs11#――――――――――――――――――――――――――――――――――――――――#k";
		//msg +="#L13#"+icon+"4310065/iconShop##fc0xFF0066CC# 파프니르 상점#fc0xFF000000# 이용하기#l\r\n";
		msg +="#L16#"+icon+"4310156/iconShop##fc0xFFCC0000# 앱솔랩스상점#l"+white+" 상점"+space+"#L20#"+icon+"4310156/iconShop##fc0xFFCC0000# #z4310156##fc0xFF000000# 교환#l\r\n"
		msg +="#L18#"+icon+"4310218/iconShop##fc0xFF000099# 아케인셰이드 상점#l"+space+"#L21#"+icon+"4310156/iconShop##fc0xFFCC0000# #z4310156##fc0xFF000000# 교환#l\r\n"
		msg +=white+"-------------------------#k"
		msg +="#L22#"+icon+"4310218/iconShop##fc0xFF000099# #z4310218##fc0xFF000000# 교환#l\r\n";
		msg +=white+"-------------------------#k"
		msg +="#L23#"+icon+"4310218/iconShop##fc0xFF000099# #z4310218##fc0xFF000000# 교환#l\r\n";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1: // 소비상점
                cm.dispose();
                cm.openShop(1);
			break;
			case 2: // 기본악세
				cm.dispose();
				cm.openShop(9031015);
			break;
			case 3: // 모루상점
				cm.dispose();
				cm.openShop(9031003);
			break;
			case 4: // 캐시상점
				cm.dispose();
				cm.openShop(6);
			break;
			case 5: // 보조무기
				cm.dispose();
				cm.openShop(2);
			break;
			case 6: // 네오스톤
				cm.dispose();
				cm.openShop(9062459);
			break;
			case 7: //휴식상점
				cm.dispose();
				cm.openShop(9001213);
			break;
			case 8: // 네오상점
				cm.dispose();
				cm.openShop(20);
			break;
			case 9: // 출석상점
				cm.dispose();
				cm.openShop(18);
			break;
			case 10: // 보스결정
				cm.dispose();
				cm.openShop(9001212);
			break;
			case 11: // 도네
				cm.dispose();
				cm.openNpc(9001048);
			break;
			case 12: // 홍보
				cm.dispose();
				cm.openNpc(3001850);
			break;
			case 13: // 유니온샵
				cm.dispose();
				cm.openShop(9010107);
			break;
			case 14: // 뽑기상점
                cm.dispose();
                cm.openNpc(1052014);
			break;
			case 15: // 해피상점
				cm.dispose();
				cm.openShop(1302011);
			break;
				//아래메뉴
			case 16: // 앱솔랩스 상점
				cm.dispose();
				cm.openShop(3);
			break;
			case 17: // 앱솔랩스 상점
				cm.dispose();
				cm.openShop(1540894);
			break;
			case 18: // 아케인 상점
				cm.dispose();
				cm.openShop(4);
			break;
			case 19: // 아케인 상점
				cm.dispose();
				cm.openShop(5);
			break;
			case 20: // 앱솔코인
				cm.dispose();
				cm.openNpc(2155009);
			break;
			case 21: // 스티그마코인
				cm.dispose();
				cm.openNpc(1540893);
			break;
			case 22: // 판타즈마 코인
				cm.dispose();
				cm.openNpc(3003105);
			break;
			case 23: //아라크노
				cm.dispose();
				cm.openNpc(3003536);
			break;

		}
	}
}