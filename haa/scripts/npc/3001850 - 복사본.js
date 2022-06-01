importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var seldreward = -1;
var seld2 = -1;
var seld3 = -1;

var name, comment;
var youtube, blog, etc;

var year, month, date2, date, day

var grade = [
	[0, "일반"],
	[1, "비기닝"],
	[2, "라이징"],
	[3, "플라잉"],
	[4, "샤이닝"],
	[5, "아이돌"],
	[6, "슈퍼스타"]
]

var shop = [
	{'n' : 0, 'items' : [
			{'itemid' : 1147004, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 1500, 'special' : -1},
			{'itemid' : 2438698, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 3000, 'special' : -1},
			{'itemid' : 2438699, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 3000, 'special' : -1},
			{'itemid' : 2438700, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 3000, 'special' : -1},
			{'itemid' : 2438701, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 3000, 'special' : -1},
			{'itemid' : 5152301, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 1000, 'special' : -1},
			{'itemid' : 5152300, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 4000, 'special' : -1},
			{'itemid' : 1113055, 'qty' : 1, 'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
		]
	},
	{'n' : 1, 'items' : [
			{'itemid' : 3010039, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010037, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010042, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010076, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010089, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010070, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010038, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010059, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},
			{'itemid' : 3010090, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 5000, 'special' : -1},


		]
	},
	{'n' : 2, 'items' : [
			{'itemid' : 2430017, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
			{'itemid' : 2430018, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
			{'itemid' : 2430019, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
			{'itemid' : 2430022, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
			{'itemid' : 2430023, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
			{'itemid' : 2430024, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 50000, 'special' : -1},
		]
	},
	{'n' : 3, 'items' : [
			{'itemid' : 1113070, 'qty' : 1, 'atk' : 30, 'allstat' : 50, 'price' : 100000, 'special' : 5},
		]
	},
	{'n' : 4, 'items' : [
			{'itemid' : 3010116, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 30000, 'special' : -1},
			{'itemid' : 3010117, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 30000, 'special' : -1},
			{'itemid' : 3010118, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 30000, 'special' : -1},
			{'itemid' : 3010119, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 30000, 'special' : -1},
			{'itemid' : 3010120, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 30000, 'special' : -1},
		]
	},
	{'n' : 5, 'items' : [
			{'itemid' : 3010121, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
			{'itemid' : 3010122, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
			{'itemid' : 3010123, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
			{'itemid' : 3010124, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
			{'itemid' : 3010125, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
			{'itemid' : 3010126, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
			{'itemid' : 3010127, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 40000, 'special' : -1},
		]
	},
	{'n' : 6, 'items' : [
			{'itemid' : 3010091, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010093, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010094, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010095, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010096, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010098, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010099, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010100, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010101, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010102, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010103, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010104, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010105, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010106, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010111, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 3010112, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'price' : 70000, 'special' : -1},
			{'itemid' : 1113055, 'qty' : 1,  'atk' : 50, 'allstat' : 100, 'price' : 200000, 'special' : 10},
		]
	},
]

var NumReward = [
	{'n' : 1, 'items' : [
				{'itemid' : 1147004, 'qty' : 1, 'atk' : 150, 'allstat' : 60, 'select' : false},
				{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2430058, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : [4000000, 4000001, 4000002], 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : true},
				]
	},
	{'n' : 2, 'items' : [
				{'itemid' : [1147009, 1147010], 'qty' : 1, 'atk' : 250, 'allstat' : 100, 'select' : true},
				{'itemid' : 2430058, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 3010091, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 3010093, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				]
	},
	{'n' : 3, 'items' : [
				{'itemid' : [1146990, 1146991, 1146992, 1147005, 1147006, 1147007, 1147008], 'qty' : 1, 'atk' : 300, 'allstat' : 150, 'select' : true},
				{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
				]
	},
	{'n' : 5, 'items' : [
				{'itemid' : [1146990, 1146991, 1146992, 1147005, 1147006, 1147007, 1147008], 'qty' : 1, 'atk' : 300, 'allstat' : 150, 'select' : true},
				{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
				]
	},
	{'n' : 6, 'items' : [
				{'itemid' : 1147004, 'qty' : 1, 'atk' : 150, 'allstat' : 60, 'select' : false},
				{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2430058, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
				{'itemid' : 5060048, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
				]
	},
]

var reward = 0;

var modify = "";
var modifychr;

var max = 0;

var g = -1;
var k = -1;

var hasSelect = false;

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
		getData();
		if (cm.getClient().getKeyValue("pGrade") == null)
			cm.getClient().setKeyValue("pGrade", "0");

		var msg = "";
		msg += "#fc0xFFFF3300##h ##fc0xFF000000# 님의 서포트 포인트 : #fc0xFFFF3300#"+cm.getPlayer().getHPoint()+"P#fc0xFF000000##n"+enter;
		//msg += "#fc0xFFFF3300##h ##fc0xFF000000# 님의 서포터 등급 : #fc0xFFFF3300#"+cm.getPlayer().getPgrades()+"#fc0xFF000000##n#b"+enter;
		msg += "#fc0xFFFF3300##h ##fc0xFF000000# 님의 누적 등급 : #fc0xFFFF3300#"+cm.getPlayer().getClient().getKeyValue("PNumber")+" 등급#k#b"+enter+enter;
		msg += "#L1#서포트 포인트를 수령하겠습니다."+enter;
		msg += "#L5#서포트 포인트 상점을 이용하겠습니다."+enter;
		msg += "#L7#누적 횟수 보상 수령하기"+enter;
		if (cm.getPlayer().isGM()) {
			msg += "#fc0xFFFF3300##L3#홍보 포인트 지급하기"+enter;
			//msg += "#L4#홍보 유저 관리하기"+enter;
			msg += "#L6#누적 등급 수정#fc0xFF000000#";
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.sendSimple(getList());
			break;
			case 2:
				cm.sendOkS(getRanking(10), 2);
				cm.dispose();
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				cm.sendGetText("지급 대상의 닉네임을 적어주세요.");
			break;
			case 4:
				if (!cm.getPlayer().isGM())
					return;
				var msg = "원하시는 항목을 선택해주세요.#b"+enter;
				msg += "#L2#유저의 홍보 등급 조정";
				cm.sendSimple(msg);
			break;
			case 5:

				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001850, "supportshop");
			break;
			case 6:
				if (!cm.getPlayer().isGM())
					return;
				cm.sendGetText("수정 대상의 닉네임을 적어주세요.");
			break;
			case 7:
				for (i = 0; i < NumReward.length; i++) {
					if (cm.getClient().getKeyValue("pnd_"+NumReward[i]['n']) == null && Integer.parseInt(cm.getPlayer().getClient().getKeyValue("PNumber")) >= NumReward[i]['n']) {
						g = i;
						break;
					}
				}
				if (g == -1) {
					cm.sendOk("받을 수 있는 누적 보상이 없습니다.");
					cm.dispose();
					return;
				}
				if (Integer.parseInt(cm.getPlayer().getClient().getKeyValue("PNumber")) < NumReward[g]['n']) {
					cm.sendOk("등급 보상을 받을 수 없는 상태입니다.");
					cm.dispose();
					return;
				}
				if (cm.getClient().getKeyValue("pnd_"+NumReward[g]['n']) != null) {
					cm.sendOk("등급 보상을 받을 수 없는 상태입니다.");
					cm.dispose();
					return;
				}
				var msg = "#b누적 홍보 "+NumReward[g]['n']+" 등급#k을 달성하신 것을 축하합니다!"+enter;
				msg += "낮은 등급의 보상부터 차례차례 받게 됩니다."+enter;
				msg += "현재 받을 보상은 #b홍보 "+NumReward[g]['n']+"등급의 보상입니다."+enter+enter;
		
				msg += "#r※경고! 이 보상은 계정당 1회 받을 수 있습니다."+enter;
				msg += "정말 이 캐릭터에서 받으시려면 '예'를 눌러주세요.";
				cm.sendYesNo(msg);
			break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				getList2(getsex(sel));
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				name = cm.getText();
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "스트리밍 보상을 얼마나 지급하시겠습니까?";
				cm.sendGetNumber(msg, 0, 0, 100000);
			break;
			case 4:
				seld3 = sel;
				if (!cm.getPlayer().isGM())
					return;
				switch (seld3) {
					case 2:
						cm.sendGetText("등급을 조정할 유저의 닉네임을 적어주세요.");
					break;
				}
			break;
			case 5:
				seld2 = sel;
				a = 0;
				itx = null;
				for (i = 0; i < shop.length; i++) {
					if (shop[i]['n'] <= cm.getPlayer().getPgrade()) {
						for (j = 0; j < shop[i]['items'].length; ++j) {
							if (a == seld2) {
								itx = shop[i]['items'][j];
								break;
							} else {
								a++;
							}
						}
					}
					if (itx != null) {
						break;
					}
				}

				if (itx == null) {
					cm.sendOk("오류가 발생했습니다. 운영자님께 문의해주세요.");
					cm.dispose();
					return;
				}

				msg = "선택하신 아이템 : #b#i" + itx['itemid'] + "##z" + itx['itemid'] + "##k" + enter;
				msg += "지급되는 갯수 : #d" + itx['qty'] + "개#k" + enter;
				if (itx['allstat'] > 0) {
					msg += "추가 올스탯 옵션 : #b+" + itx['allstat'] + "#k" + enter;
				}
				if (itx['atk'] > 0) {
					msg += "추가 공격력/마력 옵션 : #b+" + itx['atk'] + "#k" + enter;
				}
				if (itx['special'] > 0) {
					msg += "보스 공격력 / 방어력 무시 / 데미지 : #b+"+ itx['special']+ "%#k" + enter + enter;
				}
				if (itx['allstat'] > 0 || itx['atk'] > 0 || itx['special'] > 0) {
					msg += "#e이 옵션은 이노센트 주문서 사용 시 복구되지 않습니다.#n" + enter;
				}
				msg += enter + enter;
				msg += "구매하실 아이템 정보가 맞다면 #e예#n를 눌러주세요." + enter;
				msg += "홍보 포인트 #r#e" + itx['price'] + "P#k#n만큼 차감됩니다.";

				cm.sendYesNo(msg);
			break;
			case 6:
				if (!cm.getPlayer().isGM())
					return;
				name = cm.getText();
				modifychr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name);
				var msg = "#b"+name+"#k의 홍보등급을 몇 등급으로 설정 할거냐\r\n다운ㅋㅋ띡~~"+enter;
				if (modifychr == null) {
					cm.sendOk("현재 채널에 #b"+name+"#k님이 존재하지 않습니다. 같은 채널에서 진행해주세요.");
					cm.dispose();
					return;
				}
				cm.sendGetNumber(msg, 0, 0, 5000);
			break;
			case 7:
				if (g == -1) {
					cm.sendOk("받을 수 있는 누적 보상이 없습니다.");
					cm.dispose();
					return;
				}
				if (Integer.parseInt(cm.getPlayer().getClient().getKeyValue("PNumber")) < NumReward[g]['n']) {
					cm.sendOk("등급 보상을 받을 수 없는 상태입니다.");
					cm.dispose();
					return;
				}
				if (cm.getClient().getKeyValue("pnd_"+NumReward[g]['n']) != null) {
					cm.sendOk("등급 보상을 받을 수 없는 상태입니다.");
					cm.dispose();
					return;
				}
				
				for (j = 0; j < NumReward[g]['items'].length; j++) {
					if (NumReward[g]['items'][j]['select']) {
						hasSelect = true;
					}
				}

				if (hasSelect) {
					var k = -1;
					var msg = "선택할 수 있는 아이템이 존재합니다!"+enter;
					for (j = 0; j < NumReward[g]['items'].length; j++) {
						if (NumReward[g]['items'][j]['select']) {
							k = j;
						}
					}
					if (k == -1) {
						cm.sendOk("-1");
						cm.dispose();
						return;
					}

					for (i = 0; i < NumReward[g]['items'][k]['itemid'].length; i++) {
						msg += "#L"+i+"##i"+NumReward[g]['items'][k]['itemid'][i]+"##z"+NumReward[g]['items'][k]['itemid'][i]+"#"+enter;
					}
					cm.sendSimple(msg);
				} else {
					for (j = 0; j < NumReward[g]['items'].length; j++) {
						if (Math.floor(NumReward[g]['items'][j]['itemid'] / 10000) == 1)
							gainItemS(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty'], NumReward[g]['items'][j]['allstat'], NumReward[g]['items'][j]['atk'])
						else
							cm.gainItem(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty']);
					}
					cm.getPlayer().getClient().setKeyValue("pnd_"+NumReward[g]['n'], "1");
					cm.getPlayer().setPgrade(g + 3);
					cm.sendOk("지급이 완료되었습니다.");
					cm.dispose();
					return;
				}
			break;
		}
	} else if (status == 3) {
		switch (seld) {
			case 1:
				seldreward = sel;
				reward = getQ(seldreward);
				cm.sendYesNo("정말 해당 보상을 지급받으시겠습니까?\r\n총 #b"+reward+" 홍보포인트#k를 얻게됩니다.");
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				youtube = sel;
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "스트리밍 보상 : #b"+youtube+"#k"+enter;
				msg += "포스팅 보상을 얼마나 지급하시겠습니까?";
				cm.sendGetNumber(msg, 0, 0, 100000);
			break;
			case 4:
				seld2 = sel;
				switch (seld3) {
					case 2:
						modify = cm.getText();
						modifychr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(modify);
						var msg = "#b"+modify+"#k의 홍보등급을 어떤 것으로 변경하시겠습니까?"+enter;
						if (modifychr == null)
							msg += enter+"현재 채널에 #b"+modify+"#k님이 존재하지 않습니다. #b접속 중이지 않은 것이 #b확실#k하다면 계속 진행해주세요.#k 만약 접속 중이라면 해당 캐릭터와 #b#e같은 채널#k#n에서 진행해주세요."+enter;
						for (i = 0; i < grade.length; i++) {
							msg += "#L"+i+"#"+grade[i][1]+enter;
						}
						cm.sendSimple(msg);
					break;
				}
			break;
			case 5:
				if (cm.getPlayer().getHPoint() < itx['price']) {
					cm.sendOk("홍보포인트가 부족합니다.");
					cm.dispose();
					return;
				}
				
				if (!cm.canHold(itx['itemid'], itx['qty'])) {
					cm.sendOk("인벤토리의 공간이 부족하거나, 더 이상 가질 수 없는 아이템입니다.");
					cm.dispose();
					return;
				}

				cm.getPlayer().gainHPoint(-itx['price']);

				if (Math.floor(itx['itemid'] / 1000000) == 1)
					gainItemT(itx['itemid'], itx['allstat'], itx['atk'], itx['special']);
				else
					cm.gainItem(itx['itemid'], itx['qty']);

				msg = "선택하신 아이템 : #b#i" + itx['itemid'] + "##z" + itx['itemid'] + "##k" + enter;
				msg += "#h 0#님께 정상적으로 #d" + itx['qty'] + "개#k 지급해 드렸습니다." + enter + enter;
				msg += "현재 #fc0xFF6600CC##h 0#님의 남은 홍보포인트 : #e#fc0xFFFF3300#" + cm.getPlayer().getHPoint() + "P#k#n";
				cm.sendOk(msg);
				cm.dispose();
			break;
			case 6:
				if (!cm.getPlayer().isGM())
					return;
				modifychr.getClient().setKeyValue("PNumber", ""+sel);
				cm.sendOk("설정이 완료되었습니다.");
				cm.dispose();
			break;
			case 7:
				for (j = 0; j < NumReward[g]['items'].length; j++) {
					if (NumReward[g]['items'][j]['select']) {
						if (Math.floor(NumReward[g]['items'][j]['itemid'][sel] / 10000) == 1)
							gainItemS(NumReward[g]['items'][j]['itemid'][sel], NumReward[g]['items'][j]['qty'], NumReward[g]['items'][j]['allstat'], NumReward[g]['items'][j]['atk'])
						else
							cm.gainItem(NumReward[g]['items'][j]['itemid'][sel], NumReward[g]['items'][j]['qty']);
					} else {
						if (Math.floor(NumReward[g]['items'][j]['itemid'] / 10000) == 1)
							gainItemS(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty'], NumReward[g]['items'][j]['allstat'], NumReward[g]['items'][j]['atk'])
						else
							cm.gainItem(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty']);
					}
				}
				cm.getPlayer().getClient().setKeyValue("pnd_"+NumReward[g]['n'], "1");
				cm.getPlayer().setPgrade(g + 3);
				cm.sendOk("지급이 완료되었습니다.");
				cm.dispose();
				break;
		}
	} else if (status == 4) {
		switch (seld) {
			case 1:
				/*
					수령 (지급이 완료되었습니다)
				*/
				getReward(seldreward);
				cm.getPlayer().gainHPoint(reward);
				//cm.getPlayer().gainnHPoint(reward);
				cm.sendOk("수령이 완료되었습니다.");
				cm.dispose();
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				blog = sel;
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "스트리밍 보상 : #b"+youtube+"#k"+enter;
				msg += "포스팅 보상 : #b"+blog+"#k"+enter;
				msg += "기타 보상을 얼마나 지급하시겠습니까?";
				cm.sendGetNumber(msg, 0, 0, 100000);
			break;
			case 4:
				switch (seld3) {
					case 2:
						if (modifychr != null) {
							modifychr.setPgrade(sel);
						} else {
							var con = DatabaseConnection.getConnection();
							asdid = getAccId(modify);
							var ps = con.prepareStatement("UPDATE acckeyvalue SET `value` = ? WHERE `id` = ? and `key` = ?");
            						ps.setString(1, sel);
            						ps.setInt(2, asdid);
							ps.setString(3, "pGrade");
           						ps.executeUpdate();
            						ps.close();
						}
						cm.sendOk("변경이 완료되었습니다.");
						cm.dispose();
					break;
				}
			break;
		}
	} else if (status == 5) {
		if (!cm.getPlayer().isGM())
			return;
		etc = sel;
		var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
		msg += "스트리밍 보상 : #b"+youtube+"#k"+enter;
		msg += "포스팅 보상 : #b"+blog+"#k"+enter;
		msg += "기타 보상 : #b"+etc+"#k"+enter;
		msg += "#b마지막! 비고를 적어주세요.#k";
		cm.sendGetText(msg);
	} else if (status == 6) {
		if (!cm.getPlayer().isGM())
			return;
		comment = cm.getText();
		var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
		msg += "스트리밍 보상 : #b"+youtube+"#k"+enter;
		msg += "포스팅 보상 : #b"+blog+"#k"+enter;
		msg += "기타 보상 : #b"+etc+"#k"+enter;
		msg += "비고 : #b"+comment+"#k"+enter;
		msg += "#b정말로 지급하시겠습니까?";
		cm.sendYesNo(msg);
	} else if (status == 7) {
		if (!cm.getPlayer().isGM())
			return;
		send(name, youtube, blog, etc, comment);
		cm.sendOk("#fs11#지급 완료");
		cm.dispose();
	}
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	date = year+"."+month+"."+date2;
	day = time.getDay();
}

function getRanking(asd) {
		var ret = "#fs11#랭킹은 최대 "+asd+"명까지만 보여집니다.\r\n";
		var as = 0;
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM kms.acckeyvalue WHERE `key` = 'nHpoint' ORDER BY CAST(`value` AS UNSIGNED INT) DESC LIMIT "+asd);
            var rs = ps.executeQuery();

            while (rs.next()) {
		as += 1;
		var ps2 = con.prepareStatement("SELECT `name` FROM kms.characters WHERE `accountid` = ? ORDER BY `level` DESC LIMIT 1");
		ps2.setInt(1, rs.getInt("id"));
		rs2 = ps2.executeQuery();
		var name = "";
		if (rs2.next()) {
		    name = rs2.getString("name");
		}
		rs2.close();
		ps2.close();

                ret += as + ". #b"+ name +"#k 누적 홍보포인트 : #d" + rs.getString("value") + "#k\r\n";
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "랭킹이 없습니다.";
        	return ret;
}

function send(a, b, c, d, e) {
	var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("INSERT INTO hongbo (name, youtube, blog, etc, comment, date, cid) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, a);
            ps.setInt(2, b);
            ps.setInt(3, c);
            ps.setInt(4, d);
            ps.setString(5, e);
            ps.setString(6, date);
		ps.setInt(7, MapleCharacterUtil.getIdByName(a));
            ps.executeUpdate();
	ps.close();
}

function getList() {
            var ret = "";
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0");
            var rs = ps.executeQuery();

            while (rs.next()) {
                if (names.indexOf(rs.getString("name")) == -1) {
                    names.push(rs.getString("name"));
                    ret += "#L"+rs.getInt("id")+"##b"+rs.getString("name");
			if (rs.getInt("cid") == cm.getPlayer().getId() || rs.getString("name").equals(cm.getPlayer().getName()))
		    		ret += " (지급대상)\r\n";
			else
				ret += "\r\n";
                }
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "#fs11#현재 수령 가능한 홍보 보상이 없습니다.";
        	return ret;
}

function getsex(sad) {
            var ret = "";
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0 AND `id` = ?");
		ps.setInt(1, sad);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getString("name");
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "SSSSSSSSS";
        	return ret;
}

function getList2(n) {
	var mine = false;
	var ret = "#fs11#어떤 보상을 받으시겠어요?\r\n----------------------------------------------\r\n";
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0 AND `name` = ?");
	ps.setString(1, n);
	var rs = ps.executeQuery();
	while (rs.next()) {
              	if (rs.getInt("cid") == cm.getPlayer().getId() || rs.getString("name").equals(cm.getPlayer().getName())) {
                    	ret += "#L"+(rs.getInt("id"))+"#";
			mine = true;
		}
                	ret += "#fs14#닉네임 : #b"+rs.getString("name")+"#k #fs11#지급 날짜 : #d"+rs.getString("date")+"#k \r\n";
                 	ret += "    스트리밍 : #b"+rs.getInt("youtube") + "#k / 포스팅 : #b" + rs.getInt("blog") + "#k / 기타 : #b" + rs.getInt("etc") + "#k\r\n";
                	ret += "    비고 : #d" + rs.getString("comment") + "#k#l#fs12#\r\n\r\n----------------------------------------------\r\n";
            }
            rs.close();
            ps.close();
        	if (mine) {
			cm.sendSimple(ret);
		} else {
			cm.sendOk(ret);
			cm.dispose();
			return;
		}
}

function getQ(id) {
	var ret = 0;
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0 AND `id` = ? AND (`name` = ? or `cid` = ?)");
            ps.setInt(1, id);
            ps.setString(2, cm.getPlayer().getName());
		ps.setInt(3, cm.getPlayer().getId());
	var rs = ps.executeQuery();
	while (rs.next()) {
		ret += rs.getInt("youtube");
		ret += rs.getInt("blog");
		ret += rs.getInt("etc");
            }
            rs.close();
            ps.close();
        	if (ret.equals("")) return "홍보 보상이 없습니다.";
        	return ret;
}

function getAccId(a) {
	var ret = -1;
            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM characters WHERE `name` = ?");
		ps.setString(1, a);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getInt("accountid");
            }
            rs.close();
            ps.close();
	return ret;
}
function getReward(id) {
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("UPDATE hongbo SET `check` = 1 WHERE `id` = ? AND (`name` = ? or `cid` = ?)");
            ps.setInt(1, id);
            ps.setString(2, cm.getPlayer().getName());
		ps.setInt(3, cm.getPlayer().getId());
            ps.executeUpdate();
            ps.close();
}

function gainItemT(id, as, atk, spe) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(id);
	if (as > -1) {
		item.setStr(as);
		item.setDex(as);
		item.setInt(as);
		item.setLuk(as);
	}
	if (atk > -1) {
		item.setWatk(atk);
		item.setMatk(atk);
	}
	if (spe > -1) {
		item.addBossDamage(spe);
		item.addIgnoreWdef(spe);
		item.addTotalDamage(spe);
	}
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemS(id, as, atk) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(id);
	if (as > -1) {
		item.setStr(as);
		item.setDex(as);
		item.setInt(as);
		item.setLuk(as);
	}
	if (atk > -1) {
		item.setWatk(atk);
		item.setMatk(atk);
	}
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}