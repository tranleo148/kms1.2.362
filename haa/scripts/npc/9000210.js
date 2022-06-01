
importPackage(java.lang);
importPackage(Packages.client);

var enter = "\r\n";
var seld = -1;

var donation = [
	{'t' : "stat", 's' : " 힘 (Str)", 'q' : 50, 'price' : 15000, 'tt' : "str"},
	{'t' : "stat", 's' : "민첩(Dex)", 'q' : 50, 'price' : 15000, 'tt' : "dex"},
	{'t' : "stat", 's' : "지력(Int)", 'q' : 50, 'price' : 15000, 'tt' : "int"},
	{'t' : "stat", 's' : "행운(Luk)", 'q' : 50, 'price' : 15000, 'tt' : "luk"},
	{'t' : "stat", 's' : "체력(Hp)", 'q' : 1000, 'price' : 11000, 'tt' : "hp"},
	{'t' : "stat", 's' : "마나(Mp)", 'q' : 3000, 'price' : 11000, 'tt' : "mp"},
	
]

var skills = [
	["ww_magu", 2001002, "매직 가드", 150000, true],
	["ww_holy", 2311003, "홀리 심볼", 200000, true],
	["ww_sharp", 3121002, "샤프아이즈", 300000, true], 
	["ww_winb", 5121009, "윈드 부스터", 100000, true]
]

var skills1 = [
	["ww_tripling", 13100022, "트라이플링 윔", 5000000, true],
	["ww_tripling", 99999, "트라이플링 윔 추가타", 2000000, true],
	["ww_quiver", 3101009, "퀴버 카트리지", 99999999, true]
]

var p, seld2;
var selditem;
var seldq;


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
		var msg ="#fs11##d현재 #h #님의 후원포인트 : #r"+cm.getPlayer().getDonationPoint()+"P#b"+enter;
                msg += "#L15# #d악의정수 뽑기 시스템#k (가챠)"+enter;
		msg += "#L7# #d후원 포인트 상점#k (아이템)"+enter;
                msg += "#L2# #d후원 포인트 상점#k (스킬)"+enter;
                msg += "#L4# #d후원 포인트 상점#k (캐시아이템 강화)"+enter;
                msg += "#L5# #d후원 포인트 상점#k (뱃지,포켓,캐시아이템 잠재능력부여)"+enter;


		if (cm.getPlayer().getName().equals("GM"))
			msg += "#L3#디버그"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "#b#fs11#"+enter;
				msg += " 현재 #h #님의 후원포인트 : #r"+cm.getPlayer().getDonationPoint()+"P#b"+enter;
				for (i = 0; i < donation.length; i++) {
					ditem = donation[i];
					if (ditem['t'].equals("stat")) msg += "#L"+i+"##i4001527#"+ditem['s']+" "+ditem['q']+"P 구매 #e["+ditem['price']+"P]#n"+enter;
					else msg += "#L"+i+"##i"+ditem['itemid']+"##z"+ditem['itemid']+"# "+ditem['q']+" 개 구매 #e["+ditem['price']+"P]#n"+enter;
				}
			break;
			case 2:
				var msg = "#fn나눔고딕 ExtraBold##fs15#후원 스페셜 스킬 목록#Cgray# #fs10##r(Special Skill)#k#fs12#"+enter;
				msg += " 현재 #h #님의 후원포인트 : #r"+cm.getPlayer().getDonationPoint()+"P#k#fs11#"+enter;
				for (i = 0; i < skills.length; i++) {
					if (skills[i][1] == 99999) {
						msg += "#L"+i+"##s13100022# #e"+skills[i][2]+"#n 구매하기 #e#r("+skills[i][3]+"P)#k#n#l"+enter;
						continue;
					}
					if (skills[i][4])
						msg += "#L"+i+"##s"+skills[i][1]+"# #e"+skills[i][2]+"#n 구매하기 #e#r("+skills[i][3]+"P)#k#n#l"+enter;
					else if (!skills[i][4] && cm.getPlayer().isGM())
						msg += "#L"+i+"##s"+skills[i][1]+"# #e"+skills[i][2]+"#n 구매하기 #e#r("+skills[i][3]+"P)#k#n(GM만 보임)#l"+enter;
				}
				cm.sendSimple(msg);
			break;
			case 3:
				if (!cm.getPlayer().getName().equals("GM")) {
					return;
				}
				var msg = "원하는걸 골라보거라#fs11##b"+enter;
				msg += "#L1#후원포인트 지급"+enter;
				cm.sendSimple(msg);
			break;
                        case 4:
				cm.dispose();
			        cm.openNpc(2192032);
			return;
                        case 5:
				cm.dispose();
			        cm.openNpc(2192031);
			return;
                        case 6:
				cm.dispose();
			        cm.openNpc(9000030);
			return;
                        case 7:
				cm.dispose();
			        cm.openNpc(9001046);
			return;
                        case 8:
				cm.dispose();
			        cm.openNpc(2002000);
			return;
                        case 9:
				var msg = "#fn나눔고딕 ExtraBold##fs15#후원 VIP 스킬 제작#Cgray# #fs10##r(VIP Skill)#k#fs12#"+enter;
				msg += "#fUI/UIWindow8.img/EldaGauge/tooltip/46# 현재 #h #님의 후원포인트 : #r"+cm.getPlayer().getDonationPoint()+"P#k#fs11#"+enter+enter;
				msg += "황금 단풍잎은 #b악의 정수 가챠#k에서 얻으실 수 있습니다."+enter;
				msg += "후원 트라이플링 윔은 기존 윔과 다르게 #r확률100%#k 로 적용되며, 화살갯수를 추가적으로 다실 수 있습니다."+enter;
				for (i = 0; i < skills1.length; i++) {
					if (skills1[i][1] == 99999) {
						msg += "#L"+i+"##s13100022# #e"+skills1[i][2]+"#n 구매하기 #e#r("+skills1[i][3]+"P + #i4001168# 5개)#k#n#l"+enter;
						continue;
					}
					if (skills1[i][4])
						msg += "#L"+i+"##s"+skills1[i][1]+"# #e"+skills1[i][2]+"#n 제작하기 #e#r("+skills1[i][3]+"P + #i4001168# 5개)#k#n#l"+enter;
					else if (!skills1[i][4] && cm.getPlayer().isGM())
						msg += "#L"+i+"##s"+skills1[i][1]+"# #e"+skills1[i][2]+"#n 구매하기 #e#r("+skills1[i][3]+"P)#k#n(GM만 보임)#l"+enter;
				}
				cm.sendSimple(msg);
			break;
                        case 15:
				cm.dispose();
			        cm.openNpc(1540326);
			return;
			case 99:
				buffs = ["ww_buck", "ww_crsca", "ww_magu", "ww_holy", "ww_sharp", "ww_winb", "ww_tri"];
				skills = [5321054, 1311015, 2001002, 2311003, 3121002, 5121009, 1312003];
				for (i = 0; i < buffs.length; i++) {
                		     if (cm.getPlayer().getKeyValue(51384, buffs[i]) > 0 && !cm.hasDonationSkill(skills[i])) {
               			         cm.gainDonationSkill(skills[i]);
                		     }
                                }
				cm.sendOk("복구가 완료되었습니다.");
				cm.dispose();
			return;
		}
		cm.sendSimple(msg);
	} else if (status == 2) {
		seld2 = sel;
		switch (seld) {
			case 1:
				p = cm.getPlayer().getDonationPoint();
				selditem = donation[sel];
				sad = "후원";
				if (p < selditem['price']) {
					cm.sendOk("#fs11# 후원포인트가 부족합니다.");
					cm.dispose();
					return;
				}
				var msg = "";
				if (selditem['t'].equals("stat")) {
					msg += "선택하신 아이템은 #b"+selditem['s']+" "+selditem['q']+"P#k 입니다."+enter;
					msg += "몇 번 구매하고 싶으십니까?";
					cm.sendGetNumber(msg, 1, 1, 100);
				} else {
					msg += "선택하신 아이템은 #i"+selditem['itemid']+"##b#z"+selditem['itemid']+"# "+selditem['q']+"개#k 입니다."+enter;
					msg += "#fs11#가격은 #b"+selditem['price']+"P#k이고 현재 #b#h #님의 "+sad+"포인트는 "+p+"P#k입니다."+enter;
					msg += "몇 개 구매하시겠습니까?";
					cm.sendGetNumber(msg, 1, 1, 100);
				}
			break;
			case 2:
				if (cm.getPlayer().getDonationPoint() < skills[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}

				var view = skills[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendYesNo("정말로 #s"+view+"# #e"+skills[seld2][2]+"#n을(를) 구매하시겠습니까?");
			break;
			case 9:
				if (cm.getPlayer().getDonationPoint() < skills1[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}

				var view = skills1[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendYesNo("정말로 #s"+view+"# #e"+skills1[seld2][2]+"#n을(를) 구매하시겠습니까?");
			break;
			case 3:
				switch (seld2) {
					case 1:
						if (!cm.getPlayer().getName().equals("혀구")) {
							return;
						}
						var msg = "얼마";
						cm.sendGetNumber(msg, 1, 1, 10000000);
					break;
				}
			break;
		}
	} else if (status == 3) {
		switch (seld) {
			case 1:
				if (p < selditem['price']) {
					cm.sendOk("#fs11#  후원포인트가 부족합니다.");
					cm.dispose();
					return;
				}

				if (selditem['t'].equals("item")) {
					seldq = sel;
					var msg = "선택하신 아이템은 #i"+selditem['itemid']+"##b#z"+selditem['itemid']+"# "+(selditem['q'] * seldq)+"개#k 입니다."+enter;
					msg += "#fs11#가격은 #b"+(selditem['price'] * seldq)+"P#k이고 현재 #b#h #님의 "+sad+"포인트는 "+p+"P#k입니다."+enter;
					msg += "정말 구매하시겠습니까?";
					cm.sendYesNo(msg);
				} else {
					seldq = sel;
					var msg = "선택하신 아이템은 #b"+selditem['s']+" "+selditem['q']+"P * "+seldq+"#k 입니다."+enter;
					msg += "#fs11#가격은 #b"+(selditem['price'] * seldq)+"P#k이고 현재 #b#h #님의 "+sad+"포인트는 "+p+"P#k입니다."+enter;
					msg += "정말 구매하시겠습니까?";
					cm.sendYesNo(msg);
						
				}


			break;
			case 2:
				if (cm.getPlayer().getDonationPoint() < skills[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}
				if (cm.hasDonationSkill(skills[seld2][1])) {
					cm.sendOk("이미 구매하신 스킬입니다.");
					cm.dispose();
					return;
				}
				cm.getPlayer().gainDonationPoint(-skills[seld2][3]);
				if (skills[seld2][1] == 3101009 || skills[seld2][1] == 13100022) {
					cm.getPlayer().teachSkill(skills[seld2][1], 30);
                                        cm.getPlayer().teachSkill(13120003, 30);
				}
				if (skills[seld2][1] != 99999) {
					cm.gainDonationSkill(skills[seld2][1]);
				} else {
					var bonusAttack = cm.getPlayer().getKeyValue(99999, "triplingBonus");

					if (bonusAttack == -1) {
						cm.getPlayer().setKeyValue(99999, "triplingBonus", "0");
					}
					
					cm.getPlayer().setKeyValue(99999, "triplingBonus", (cm.getPlayer().getKeyValue(99999, "triplingBonus") + 1) + "");
				}

				var view = skills[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendOk("#s"+view+"# #e"+skills[seld2][2]+"#n 스킬을 성공적으로 지급했습니다.");
				cm.dispose();
			break;

                        case 9:
				if (cm.getPlayer().getDonationPoint() < skills1[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}
                                if (cm.itemQuantity(4001168) <= 5){
					cm.sendOk("황금 단풍잎이 모자란 것 같습니다.");
					cm.dispose();
					return;
				}
				if (cm.hasDonationSkill(skills1[seld2][1])) {
					cm.sendOk("이미 구매하신 스킬입니다.");
					cm.dispose();
					return;
				}
 				cm.getPlayer().gainDonationPoint(-skills1[seld2][3]);
                                if (cm.haveItem(4001168, 5)) {
                                cm.gainItem(4001168, -5);
                                 } else {		         
		 cm.sendOk("아니야..");
		 cm.dispose();	
		 }
				if (skills1[seld2][1] == 3101009 || skills1[seld2][1] == 13100022) {
					cm.getPlayer().teachSkill(skills1[seld2][1], 30);
                                        cm.getPlayer().teachSkill(13120003, 30);
				}
				if (skills1[seld2][1] != 99999) {
					cm.gainDonationSkill(skills1[seld2][1]);
				} else {
					var bonusAttack = cm.getPlayer().getKeyValue(99999, "triplingBonus");

					if (bonusAttack == -1) {
						cm.getPlayer().setKeyValue(99999, "triplingBonus", "0");
					}
					
					cm.getPlayer().setKeyValue(99999, "triplingBonus", (cm.getPlayer().getKeyValue(99999, "triplingBonus") + 1) + "");
				}

				var view = skills1[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendOk("#s"+view+"# #e"+skills1[seld2][2]+"#n 스킬을 성공적으로 지급했습니다.");
				cm.dispose();
			break;

			case 3:
				switch (seld2) {
					case 1:
						if (!cm.getPlayer().getName().equals("혀구")) {
							return;
						}
						cm.getPlayer().gainDonationPoint(sel);
						cm.sendOk("완료");
						cm.dispose();
					break;
				}
			break;
		}
	} else if (status == 4) {
		switch (seld) {
			case 1:
				if (seldq < 0) {
					cm.sendOk("오류입니다.");
					cm.writeLog("Log/음수값.log", cm.getPlayer().getName()+"가 엔피시 ID "+cm.getNpc()+"에서 음수값을 사용하려고 함.\r\n", true);
					cm.dispose();
					return;
				}
				if (p < selditem['price'] * seldq) {
					cm.sendOk("#fs11#  후원포인트가 부족합니다.");
					cm.dispose();
					return;
				}
				if (selditem['t'].equals("stat")) {
					gainStat(selditem['tt'], (selditem['q'] * seldq));
					cm.getPlayer().gainDonationPoint(-(selditem['price'] * seldq));
					cm.sendOk("거래가 완료되었습니다.");
					cm.dispose();
				} else {
					cm.gainItem(selditem['itemid'], (selditem['q'] * seldq));
					cm.getPlayer().gainDonationPoint(-(selditem['price'] * seldq));
					cm.sendOk("거래가 완료되었습니다.");
					cm.dispose();
				}
			break;
		}
	}
}

function gainStat(ty, q) {
	stat = cm.getPlayer().getStat();
	var st;
	var sq = 0;
	switch (ty) {
		case "str":
			st = MapleStat.STR;
			sq = Integer.parseInt(stat.getStr() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setStr(sq, cm.getPlayer());
		break;
		case "dex":
			st = MapleStat.DEX;
			sq = Integer.parseInt(stat.getDex() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setDex(sq, cm.getPlayer());
		break;
		case "int":
			st = MapleStat.INT;
			sq = Integer.parseInt(stat.getInt() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setInt(sq, cm.getPlayer());
		break;
		case "luk":
			st = MapleStat.LUK;
			sq = Integer.parseInt(stat.getLuk() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setLuk(sq, cm.getPlayer());
		break;
		case "hp":
			st = MapleStat.MAXHP;
			sq = Integer.parseInt(stat.getMaxHp() + q);
			if (sq > 500000) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setMaxHp(sq, cm.getPlayer());
		break;
		case "mp":
			st = MapleStat.MAXMP;
			sq = Integer.parseInt(stat.getMaxMp() + q);
			if (sq > 500000) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setMaxMp(sq, cm.getPlayer());
		break;
	}
	cm.getPlayer().updateSingleStat(st, sq);
}