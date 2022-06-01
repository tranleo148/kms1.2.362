
importPackage(Packages.client);

var enter = "\r\n";
var seld = -1, seld2 = -1;

var n = 0;
var t = 0;

var jobs = [
	{'jobid' : 112, 'jobname' : "히어로", 'job' : "모험가", 'stat' : 1, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 122, 'jobname' : "팔라딘", 'job' : "모험가", 'stat' : 1, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 132, 'jobname' : "다크나이트", 'job' : "모험가", 'stat' : 1, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 212, 'jobname' : "불독", 'job' : "모험가", 'stat' : 3, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 222, 'jobname' : "썬콜", 'job' : "모험가", 'stat' : 3, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 232, 'jobname' : "비숍", 'job' : "모험가", 'stat' : 3, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 312, 'jobname' : "보우마스터", 'job' : "모험가", 'stat' : 2, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 322, 'jobname' : "신궁", 'job' : "모험가", 'stat' : 2, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 332, 'jobname' : "패스파인더", 'job' : "모험가", 'stat' : 2, 'sk' : [80001152, 1281, 1297, 1298, 12, 73], 'uq' : true, 'ujob' : [301, 330, 331]},
	{'jobid' : 412, 'jobname' : "나이트로드", 'job' : "모험가", 'stat' : 4, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 422, 'jobname' : "섀도어", 'job' : "모험가", 'stat' : 4, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 434, 'jobname' : "듀얼블레이드", 'job' : "모험가", 'stat' : 4, 'sk' : [80001152, 1281, 12, 73], 'uq' : true, 'ujob' : [400, 430, 431, 432, 433]},
	{'jobid' : 512, 'jobname' : "바이퍼", 'job' : "모험가", 'stat' : 1, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 522, 'jobname' : "캡틴", 'job' : "모험가", 'stat' : 2, 'sk' : [80001152, 1281, 12, 73], 'uq' : false},
	{'jobid' : 532, 'jobname' : "캐논슈터", 'job' : "모험가", 'stat' : 1, 'sk' : [80001152, 1281, 110, 109, 111, 1283, 12, 73], 'uq' : true, 'ujob' : [501, 530, 531]},
]
var level = -1;
var coin = -1;
var hpmp = -1;

var final = [];
var finaljob;
var jrand = -1;
var ast = -1;

var price = 0;

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
		hpmp = 1000*n;
		var msg ="#e#fc0xFFFF3300#[자유전직]#n#fc0xFF000000#\r\n\r\n다른 직업으로 바꿔보고 싶은겐가?#b"+enter;
		msg += "#L1#자유전직을 하겠습니다."+enter;
		//msg += "#L2#직업변경이 뭔지 궁금합니다."+enter;
		cm.sendSimple(msg);
	} else {
		seld = seld == -1 ? sel : seld;

		switch (seld) {
			case 1:
				if (status == 1) {
                    if ((Math.floor(cm.getPlayer().getJob() % 10) != 2 && Math.floor(cm.getPlayer().getJob() % 10) != 4 && Math.floor(cm.getPlayer().getJob() % 10) != 7) || cm.getPlayer().getLevel() < 295) {
                        cm.sendOk("직업변경을 하기 위해선 4차전직(5차)을 완료한 상태여야 하며, 레벨이 295 이상인 캐릭터만 가능하다네");
                        cm.dispose();
                        return;
                    }
					if (cm.getPlayer().getJob() == 3122) {
						cm.sendOk("데몬 어벤져 직업군은 현재 직업변경 서비스를 이용할 수 없다네");
						cm.dispose();
						return;
					}
					var msg = "#fc0xFF000000#원하시는 직업을 선택 해보게"+enter;
	msg += "#fc0xFF000000##b직업변경 1회#fc0xFF000000#의 가격은 도네이션 포인트 #fc0xFFFF3300#"+price+"P#fc0xFF000000#입니다."+enter;
	msg += "#fc0xFF000000#현재 #fc0xFFFF3300##h ##fc0xFF000000#님의 도네이션 포인트 : #fc0xFFFF3300#"+cm.getPlayer().getDonationPoint()+"P#fc0xFF000000#"+enter;
	msg+= "#Cgray##fs11#――――――――――――――――――――――――――――――――――――――――#fc0xFF000000#"+enter;
						for (i = 0; i < jobs.length; i++)
							msg += "#L"+i+"##fc0xFF6600CC#"+jobs[i]['jobname']+"#fc0xFF000000#으(로) 직업변경"+enter;
						
						cm.sendSimple(msg);
				} else if (status == 2) {
					seld2 = sel;

					var msg = "#fc0xFF000000#자유전직을 하기 전 모든 장비류는 착용해제 해주시게"+enter;
					msg += "정말 이 직업을 할 것이 아니라면 #b'아니오'#fc0xFF000000#를 누르고 다시 선택하게"+enter;
					msg += enter + "#fs12##b동의#fc0xFF000000#한다면 #b'예'#fc0xFF000000#를 누르게";

					cm.sendYesNo(msg);
				} else if (status == 3) {
                    			tempjob = jobs[seld2];
					changeJobscript(tempjob, 1);
					cm.sendOk("직업 변경이 완료되었습니다.");
					cm.dispose();
				}
				break;
			case 2:
				if (status == 1) {
					var msg = "DEEZ 직ㅇ 시스템이란?"+enter+enter;
					msg += "캐릭터의 레벨이 10이되며 직업이 랜덤으로 변경되는 리스크를 동반하나 스탯이 쌓이고 여러 직업을 체험해 볼 수있는 디에즈만의 독자적인 컨텐츠입니다."+enter+enter;
					msg += "총 5회의 타임리프 끝에 최종 직업군을 선택하게 되며, 각 회차마다 레벨과 디에즈 코인의 갯수가 충족 되어야 합니다."+enter+enter;
					msg += "아케인 심볼의 경우 알맞은 직업군의 주스탯으로 자동변경됩니다."+enter;
					msg += "5차 슬롯강화 및 기존 5차코어들이 초기화 됩니다."+enter+enter;
					msg += "보상 스탯은 누적되지 않습니다."+enter;
					cm.sendOk(msg);
					cm.dispose();
				}
				break;
		}
	}
}

function baseSkill() {
			switch (cm.getJob()) {

				case 2100:
                			cm.teachSkill(20001295, 1, 1);
				break;

				case 6100:
                                        cm.teachSkill(60000219, 1, 1);
                			cm.teachSkill(60001217, 1, 1);
                			cm.teachSkill(60001216, 1, 1);
                			cm.teachSkill(60001218, 1, 1);
                			cm.teachSkill(60001219, 1, 1);
                			cm.teachSkill(60001225, 1, 1);
				break;

				case 6500:
				break;

				case 2700:
                			cm.teachSkill(27000106, 5, 5);
                			cm.teachSkill(27000207, 5, 5);
                			cm.teachSkill(27001201, 20, 20);
                			cm.teachSkill(27001100, 20, 20);
				break;
				case 2500:
					cm.teachSkill(20051284,30,30);
					cm.teachSkill(20050285,30,30);
					cm.teachSkill(20050286,30,30);
					cm.teachSkill(25001000,30,30);
					cm.teachSkill(25001002,30,30);
					cm.teachSkill(25000003,30,30);
				break;

				case 1100:
				case 1200:
				case 1300:
				case 1400:
				case 1500:
					cm.teachSkill(10001251, 1, 1);
					cm.teachSkill(10001252, 1, 1);
					cm.teachSkill(10001253, 1, 1);
					cm.teachSkill(10001254, 1, 1);
					cm.teachSkill(10001255, 1, 1);
				break;
				case 14200:
                                        cm.teachSkill(140000291, 6, 6);
				break;
				case 15200:
                			cm.teachSkill(150000079, 1, 1);
                			cm.teachSkill(150011005, 1, 1);
				break;

                                case 16400:
                			cm.teachSkill(160000001, 1, 1);
                			
				break;
				case 3500:
					cm.teachSkill(30001068, 1, 1);
				break;
			}
}

function symbol(j) {
	cm.getPlayer().dropMessage(6, j);
	var inv = cm.getInventory(-1);
	for (i = -1600; i > -1605; i--) {
		item = cm.getInventory(-1).getItem(i);
		if (item == null) continue;
		if (Math.floor(item.getItemId() / 1000) != 1712) continue;
		ial = item.getArcLevel();
		var normal = 100 * (ial + 2);
		var zen = 39 * (ial + 2);
		var dev = 175 * (ial + 2);
		// 1 = s, 2 = d, 3 = i, 4 = l, 5 = z, 6 = h
		var stat = (j >= 1 && j <= 4) ? normal : j == 5 ? zen : dev;
		item.setStr(0);
		item.setDex(0);
		item.setInt(0);
		item.setLuk(0);
		item.setHp(0);

		switch (j) {
			case 1:
				item.setStr(stat);
			break;
			case 2:
				item.setDex(stat);
			break;
			case 3:
				item.setInt(stat);
			break;
			case 4:
				item.setLuk(stat);
			break;
			case 5:
				item.setStr(stat);
				item.setDex(stat);
				item.setLuk(stat);
			break;
			case 6:
				item.setHp(stat);
			break;
		}
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIPPED);
	}
	var inv = cm.getInventory(1);
	for (i = 0; i < inv.getSlotLimit(); i++) {
		item = cm.getInventory(1).getItem(i);
		if (item == null) continue;
		if (Math.floor(item.getItemId() / 1000) != 1712) continue;
		ial = item.getArcLevel();
		var normal = 100 * (ial + 2);
		var zen = 39 * (ial + 2);
		var dev = 175 * (ial + 2);
		// 1 = s, 2 = d, 3 = i, 4 = l, 5 = z, 6 = h
		var stat = (j >= 1 && j <= 4) ? normal : j == 5 ? zen : dev;
		item.setStr(0);
		item.setDex(0);
		item.setInt(0);
		item.setLuk(0);
		item.setHp(0);

		switch (j) {
			case 1:
				item.setStr(stat);
			break;
			case 2:
				item.setDex(stat);
			break;
			case 3:
				item.setInt(stat);
			break;
			case 4:
				item.setLuk(stat);
			break;
			case 5:
				item.setStr(stat);
				item.setDex(stat);
				item.setLuk(stat);
			break;
			case 6:
				item.setHp(stat);
			break;
		}
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIPPED);
	}
}

function changeJobscript(tjob, jt) {
    if (cm.getPlayer().getJob() == 3122)
        resetStatsDV();
    else 
        cm.getPlayer().resetStatDonation((cm.getPlayer().getKeyValue(1912211, "timerf") - 1), cm.getPlayer().getKeyValue(1912211, "timerft"));

	jid = tjob['jobid'];
    	cm.changeJob(jid);
    
	cm.clearSkills();
	cm.getPlayer().getCore().clear();
	cm.getPlayer().getStolenSkills().clear();
	cm.getPlayer().setKeyValue(1477, "count", "0");
	for (i = 0; i < cm.getPlayer().getMatrixs().size(); i++) {
		cm.getPlayer().getMatrixs().get(i).setLevel(0);
    }
    if (tjob['uq']) {
        for (i = 0; i < tjob['ujob'].length; i++) 
            cm.getPlayer().SkillMasterJob(tjob['ujob'][i]);
    } else {
        cm.getPlayer().SkillMasterJob(Math.floor(cm.getPlayer().getJob() / 100) * 100);
        cm.getPlayer().SkillMasterJob(cm.getPlayer().getJob() - (cm.getPlayer().getJob() % 10));
        cm.getPlayer().SkillMasterJob(cm.getPlayer().getJob() - (cm.getPlayer().getJob() % 10) + 1);
    }
	cm.getPlayer().SkillMasterByJob();
	baseSkill();
	symbol(tjob['stat']);
	for (i = 0; i < tjob['sk'].length; i++) {
		cm.teachSkill(tjob['sk'][i], 30, 30);
	}

	if (jid == 6500) {
		cm.getPlayer().setGender(1);
	}
	cm.getPlayer().reloadChar();
	if (!cm.getPlayer().isGM()) {
	cm.getClient().disconnect(true, false);
        cm.getClient().getSession().close();
	}
}