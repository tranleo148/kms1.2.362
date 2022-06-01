importPackage(Packages.handling.channel);
importPackage(java.lang);

var seld = -1;
var enter = "\r\n";

var limit = 3;
var dc = 5;


var gdiff;
var inboss = false;
var gstage;
var gdc;

var inmap = false;
var first = false;
var year, month, date2, date, day;

var boss = [
	{'name' : "왼쪽 어깨", 'mobid' : 9390612, 'mapid' : 863010240, 'hp' : 168000000000000, 'xy' : [10, 87]},
	{'name' : "오른쪽 어깨", 'mobid' : 9390610, 'mapid' : 863010330, 'hp' : 168000000000000, 'xy' : [3, 69]},
	{'name' : "심장", 'mobid' : 9390611, 'mapid' : 863010430, 'hp' : 252000000000000, 'xy' : [77, 71]},
	{'name' : "머리", 'mobid' : 8645066, 'mapid' : 863010600, 'hp' : 336000000000000, 'xy' : [27, 62]}
]

var diff = [
	{'level' : "#i3994115#", hpx : 1},
	{'level' : "#i3994116#", hpx : 2},
	{'level' : "#i3994117#", hpx : 3},
	{'level' : "#i3994118#", hpx : 4}
]

/*
80002264 -30, 0
80002232 -50, 20
80002419, -70, 30
80002337, -90, 50
*/

var eeeee = 0;

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
	for (i = 0; i < boss.length; i++) {
		if (boss[i]['mapid'] == cm.getPlayer().getMapId()) {
			inmap = true;
			break;
		}
	}
	if (inmap) {
		if (cm.getPlayer().getParty().getLeader().getId() != cm.getPlayer().getId()) {
			cm.sendOk("파티장이 아니라면 골럭스 관련 업무를 진행할 수 없습니다.");
			cm.dispose();
			return;
		}
		if (status == 0) {
			gclear = getK("golrux_clear") == 1 ? true : false;
			if (!gclear) 
				cm.sendYesNo("골럭스 토벌을 포기하시겠습니까?");
			else 
				cm.sendYesNo("골럭스의 일부분을 토벌하신걸 축하드립니다. 돌아가시겠습니까?");
		} else if (status == 1) {
			if (!gclear) {
				setK("golrux_in", "0");
				clear();
				cm.sendOk("원정대 해체가 완료되었습니다.");
			}
			if (cm.getPlayer().getParty().getMembers().size() != 1) {
				var it = cm.getPlayer().getParty().getMembers().iterator();
				var countPass = true;
				while (it.hasNext()) {
					var chr = it.next();

					var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
					pchr.warp(680000715);
				}
			} else
				cm.warp(680000715);
			cm.sendOk("제게 다시 말을 걸어주시길 바랍니다.");
			cm.dispose();
		}
	} else {
		inboss = getK("golrux_in") == 1 ? true : false;
		
		if (inboss && !first) {
			genter = getK("golrux_enter") == 1 ? true : false;
			gclear = getK("golrux_clear") == 1 ? true : false;
			if (genter) {
				if (gclear) {
					cm.getPlayer().dropMessage(6, (getK("golrux_stage"))+"");
					setK("golrux_stage", (getK("golrux_stage") + 1)+"");
					setK("golrux_enter", "0");
					setK("golrux_clear", "0");
				} else {
					setK("golrux_in", "0");
					clear();
					cm.sendOk("토벌에 실패하신 것으로 간주하여 해당 원정대는 해체됩니다.");
					cm.dispose();
					return;
				}
			}
			gstage = getK("golrux_stage");
			gdc = getK("golrux_dc");
			gdiff = getK("golrux_diff");
			if (status == 0) {
				if (gstage == 4) {
					var msg = "골럭스 토벌 성공을 축하드립니다.";
					msg += "#b#h ##k 원정대의 토벌 기록은 다음과 같습니다."+enter;
					//todo 기록 보여주기.
					ssstime = new Date().getTime() - getK("gol_t");
					ssstime = Math.floor(ssstime / 1000); // 초
					sssmin = Math.floor(ssstime / 60);
					ssssec = ssstime % 60;
					if (getK("golrux_party") != -12345) {
						var csv = cm.getClient().getChannelServer();
						var chr = csv.getPlayerStorage().getCharacterById(getK("golrux_party"));
						if (chr == null) {
							cm.sendOk("파티의 상태가 변경되어 일부 정보를 표시할 수 없습니다.");
							cm.dispose();
							return;
						}
						if (getK("golrux_dmg") < chr.getKeyValue(200106, "golrux_dmg")) {
							dper = chr.getKeyValue(200106, "golrux_dmg") / ((getK("golrux_dmg") + chr.getKeyValue(200106, "golrux_dmg")) / 100);
							dname = chr.getName();
						} else {
							dper = getK("golrux_dmg") / ((getK("golrux_dmg") + chr.getKeyValue(200106, "golrux_dmg")) / 100);
							dname = cm.getPlayer().getName();
						}
					} else {
						dper = 100;
						dname = cm.getPlayer().getName();
					}
					
					msg += "격파 시간 : "+sssmin+"분 "+ssssec+"초"+enter;
					
					msg += "누적 데미지 1등 : #b"+dname+"#k ("+Math.floor(dper)+"%)"+enter;
					setK("golrux_in", "0");
					clear();
					//cm.warp(680000715);
					cm.sendOk(msg);
					cm.dispose();
					return;
				} else {
					if (cm.getPlayer().getParty() == null) {
						cm.sendOk("파티의 상태가 바뀌어 입장할 수 없습니다.");
						cm.dispose();
						return;
					}
					if (getK("golrux_party") != -12345) {
						partyok = cm.getPlayer().getParty().getMemberById(getK("golrux_party")) == null ? false : true;
						if (!partyok) {
							cm.sendOk("파티의 상태가 바뀌어 입장할 수 없습니다.");
							cm.dispose();
							return;
						}
					}
					if (cm.getPlayer().getParty().getLeader().getId() != cm.getPlayer().getId()) {
						cm.sendOk("파티장이 아니라면 골럭스 관련 업무를 진행할 수 없습니다.");
						cm.dispose();
						return;
					}
					var msg = "골럭스를 꼭 토벌해주시길 바랍니다."+enter;
					for (i = 0; i < boss.length; i++) {
						if (gstage == i) msg += "#e"+enter;
						msg += "#L"+i+"##b골럭스의 "+boss[i]['name'];
						if (gstage > i) msg += " #d(처치완료)#k"+enter;
						else if (gstage == i) msg += " (입장가능)#n#k"+enter;
						else msg += " #r(입장불가)#k"+enter;
					}
					msg += "#L999##r#e토벌을 포기하겠습니다.#k#n";
					cm.sendSimple(msg);
				}
			} else if (status == 1) {
				seld = sel;
				if (seld == 999) {
					cm.sendYesNo("정말 토벌을 포기하시겠습니까?");
				} else {
					if (gstage > seld) {
						cm.sendOk("이미 클리어 하셨습니다."+gstage+"/"+seld+"/"+sel);
						cm.dispose();
						return;
					}
					if (gstage < seld) {
						cm.sendOk("입장할 수 없습니다.");
						cm.dispose();
						return;
					}
					var msg = "현재 #b#h ##k 골럭스 원정대의 정보는 다음과 같습니다."+enter;
					msg += "남은 데스카운트 : #b"+gdc+"#k"+enter;
					msg += "난이도 : #b"+getName(gdiff)+"#k"+enter;
					msg += "선택한 부위 : #b골럭스의 "+boss[seld]['name']+"#k"+enter;
					msg += "정말 토벌을 진행하시겠습니까?";
					cm.sendYesNo(msg);
				}
			} else if (status == 2) {
				if (seld == 999) {
					setK("golrux_in", "0");
					clear();
					cm.warp(680000715);
					cm.sendOk("원정대 해체가 완료되었습니다.");
					cm.dispose();
				} else {
					cm.spawnGolrux((boss[seld]['hp'] * diff[gdiff]['hpx']), boss[seld]['mobid'], boss[seld]['mapid'], boss[seld]['xy'][0], boss[seld]['xy'][1]);
					if (cm.getPlayer().getParty().getMembers().size() != 1) {
						var it = cm.getPlayer().getParty().getMembers().iterator();
						var countPass = true;
						while (it.hasNext()) {
							var chr = it.next();
	
							var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
							pchr.warp(boss[seld]['mapid']);
							
							setK("golrux_party", chr.getId()+"");
						}
					} else {
						cm.warp(boss[seld]['mapid']);
					}
					setK("golrux_enter", "1");
					cm.dispose();
				}
			}
		} else {
			if (status == 0) {
				getData();
				if (getK("golrux_e_"+date) == -1)
					setK("golrux_e_"+date, limit+"");

				var msg = "#r#e<골럭스의 길>#n#k"+enter;
				msg += "이 앞은 골럭스가 위치하고 있어 골럭스를 토벌하려고 하는 용사 외엔 출입을 통제하고 있습니다."+enter;
				msg += "#L1#골럭스를 토벌한다. ("+getK("golrux_e_"+date)+"회 입장 가능)"+enter;
				msg += "#L2#골럭스가 무엇인지 확인한다.";
				cm.sendSimple(msg);
			} else if (status == 1) {
				seld = sel;
				switch (seld) {
					case 1:
						if (cm.getPlayer().getParty() == null) {
							cm.sendOk("파티가 없으면 진행할 수 없습니다.");
							cm.dispose();
							return;
						}
						if (cm.getPlayer().getParty().getLeader().getId() != cm.getPlayer().getId()) {
							cm.sendOk("파티장이 아니라면 골럭스 관련 업무를 진행할 수 없습니다.");
							cm.dispose();
							return;
						}
						var msg = "토벌하실 골럭스의 난이도를 선택해주세요.#b"+enter;
						msg += "각 난이도 별 세부 사항은 설명을 확인해주세요."+enter;
						for (i = 0; i < diff.length; i++) {
							msg += "#L"+(i + 1)+"#"+diff[i]['level']+"#l　";
						}
						cm.sendSimple(msg);
					break;
					case 2:
						var msg = "골럭스 왈라왈라";
						cm.sendOk(msg);
						cm.dispose();
					break;
				}
			} else if (status == 2) {
				// 파티원 입장횟수 체크하기.
				if (getK("golrux_e_"+date) == -1)
					setK("golrux_e_"+date, limit+"");

				if (getK("golrux_e_"+date) <= 0) {
					cm.sendOk("입장횟수를 확인해주시길 바랍니다.");
					cm.dispose();
					return;
				}
				if (cm.getPlayer().getParty().getLeader().getId() != cm.getPlayer().getId()) {
					cm.sendOk("파티장이 아니라면 골럭스 관련 업무를 진행할 수 없습니다.");
					cm.dispose();
					return;
				}
				if (cm.getPlayer().getParty().getMembers().size() > 2) {
					cm.sendOk("골럭스를 토벌하기 위해선 파티의 인원수가 두 명 이하여야 합니다.");
					cm.dispose();
					return;
				}
				if (cm.getPlayer().getParty().getMembers().size() != 1) {
					var it = cm.getPlayer().getParty().getMembers().iterator();
					var countPass = true;
					while (it.hasNext()) {
						var chr = it.next();
						if (cm.getPlayer().getMapId() != chr.getMapid()) {
							cm.sendOk("골럭스를 토벌하기 위해선 파티원 모두가 같은 채널, 같은 맵에 있어야합니다.");
							cm.dispose();
							return;
						}
						if (chr.getChannel() != cm.getClient().getChannel()) {
							cm.sendOk("골럭스를 토벌하기 위해선 파티원 모두가 같은 채널, 같은 맵에 있어야합니다.");
							cm.dispose();
							return;
						}
					}
				}
				if (cm.getPlayer().getParty().getMembers().size() != 1) {
					var it = cm.getPlayer().getParty().getMembers().iterator();
					var countPass = true;
					while (it.hasNext()) {
						var chr = it.next();
						var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
						if (pchr.getKeyValue(200106, "golrux_e_"+date) == -1) {
							pchr.setKeyValue(200106, "golrux_e_"+date, limit+"");
						}
						if (pchr.getKeyValue(200106, "golrux_e_"+date) <= 0) {
							cm.sendOk("파티원의 입장횟수를 확인해주시길 바랍니다.");
							cm.dispose();
							return;
						}
					}
				}
				gdiff = sel;
				var msg = "선택하신 난이도는 #b"+getName(gdiff)+"#k입니다."+enter;
				msg += "'예'를 누를 시 토벌 진행 처리 되어, 입장횟수가 바로 차감됩니다."+enter;
				msg += "정말 토벌을 진행하시겠습니까?";
				cm.sendYesNo(msg);
			} else if (status == 3) {
				first = true;
				setK("golrux_in", "1");
				setK("golrux_stage", "0");
				setK("golrux_dc", dc);
				setK("golrux_diff", gdiff);
				
				// 파티원 입장횟수 깎기
				//setK("golrux_e_"+date, (getK("golrux_e_"+date) - 1)+"");
				if (cm.getPlayer().getParty().getMembers().size() != 1) {
					var it = cm.getPlayer().getParty().getMembers().iterator();
					var countPass = true;
					while (it.hasNext()) {
						var chr = it.next();
						var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
						pchr.setKeyValue(200106, "golrux_e_"+date, (pchr.getKeyValue(200106, "golrux_e_"+date) - 1)+"");
						pchr.dropMessage(5, "골럭스의 입장횟수가 1 차감되었습니다.");
					}
				}
				gstage = getK("golrux_stage");
				gdc = getK("golrux_dc");
				gdiff = getK("golrux_diff");
				var msg = "골럭스를 꼭 토벌해주시길 바랍니다."+enter;
				msg += "#r#e※주의! 바로 입장되니 주의 바랍니다.#k#n"+enter;
				for (i = 0; i < boss.length; i++) {
					if (gstage == i) msg += "#e"+enter;
					msg += "#L"+i+"##b골럭스의 "+boss[i]['name'];
					if (gstage > i) msg += " #d(처치완료)#k"+enter;
					else if (gstage == i) msg += " (입장가능)#n#k"+enter;
					else msg += " #r(입장불가)#k"+enter;
				}
				msg += "#L999##r#e토벌을 포기하겠습니다.#k#n";
				cm.sendSimple(msg);
			} else if (status == 4) {
				seld = sel;
				if (seld == 999) {
					setK("golrux_in", "0");
					clear();
					cm.warp(680000715);
					cm.sendOk("원정대 해체가 완료되었습니다.");
					cm.dispose();
					return;
				}
				if (seld != 0) {
					cm.sendOk("입장할 수 없습니다.");
					cm.dispose();
					return;
				}
				cm.spawnGolrux((boss[seld]['hp'] * diff[gdiff - 1]['hpx']), boss[seld]['mobid'], boss[seld]['mapid'], boss[seld]['xy'][0], boss[seld]['xy'][1]);
				//cm.warp(boss[seld]['mapid']);
				setK("golrux_stage", "0");
				if (cm.getPlayer().getParty().getMembers().size() != 1) {
					var it = cm.getPlayer().getParty().getMembers().iterator();
					var countPass = true;
					while (it.hasNext()) {
						var chr = it.next();

						var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
						pchr.warp(boss[seld]['mapid']);
						setK("golrux_party", chr.getId()+"");
					}
				} else {
					cm.warp(boss[seld]['mapid']);
					setK("golrux_party", "-12345");
				}

				setK("golrux_dmg", "0");
				setK("golrux_enter", "1");
				setK("golrux_clear", "0");
				setK("gol_t", new Date().getTime()+"");
				cm.dispose();
			}
		}
	}
}

function getName(i) {
	return i == 1 ? "이지" : i == 2 ? "노멀" : i == 3 ? "하드" : "헬";
}

function clear() {
	var asd = ["golrux_dmg", "golrux_enter", "golrux_clear", "golrux_stage", "golrux_in", "gol_t", "golrux_diff", "golrux_dc", "golrux_party"];
	for (j = 0; j < asd.length; j++)
		setK(asd[j], "0");
}

function getK(key) {
	return cm.getPlayer().getKeyValue(200106, key);
}

function setK(key, value) {
	cm.getPlayer().setKeyValue(200106, key, value);
}


function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	date = Integer.parseInt(year+""+month+""+date2);
	day = time.getDay();
}