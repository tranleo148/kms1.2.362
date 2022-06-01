/*
	This file is part of the OdinMS Maple Story Server
	Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
					   Matthias Butz <matze@odinms.de>
					   Jan Christian Meyer <vimes@odinms.de>

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU Affero General Public License as
	published by the Free Software Foundation version 3 as published by
	the Free Software Foundation. You may not use, modify or distribute
	this program under any other version of the GNU Affero General Public
	License.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Affero General Public License for more details.

	You should have received a copy of the GNU Affero General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * Guild Alliance NPC
 */

var status;
var choice;
var guildName;
var partymembers;

function start() {
	//cm.sendOk("The Guild Alliance is currently under development.");
	//cm.dispose();
	partymembers = cm.getPartyMembers();
	status = -1;
	action(1,0,0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendSimple("안녕하세요! 저는 길드연합 담당 #b레나리우#k라고 합니다.\r\n#b#L0#길드 연합이 뭔지 알려주세요!#l\r\n#L1#어떻게 하면 길드 연합을 만들 수 있나요?#l\r\n#L2#길드 연합을 만들고 싶습니다.#l\r\n#L3#길드 연합의 길드 수를 늘리고 싶어요.#l\r\n#L4#길드 연합을 해체하고 싶어요.#l");
	} else if (status == 1) {
		choice = selection;
	    if (selection == 0) {
		    cm.sendOk("길드 연합은 말 그대로 길드들을 묶는 큰 연합입니다, 저는 그 연합을 관리하고 있죠.");
			cm.dispose();
		} else if (selection == 1) {
			cm.sendOk("길드 연합을 형성하기 위해선 2명의 길드 마스터가 서로 파티를 맺어야합니다. 파티장이 길드 연합의 연합장이 되죠.");
			cm.dispose();
		} else if(selection == 2) {
			if (cm.getPlayer().getParty() == null || partymembers == null || partymembers.size() != 2 || !cm.isLeader()) {
				cm.sendOk("파티에 최소 두 명의 길드 마스터가 있어야합니다."); //Not real text
				cm.dispose();
			} else if (partymembers.get(0).getGuildId() <= 0 || partymembers.get(0).getGuildRank() > 1) {
				cm.sendOk("파티원 중에 길드가 없거나 길드 마스터가 아닌 사람이 있습니다.");
				cm.dispose();
			} else if (partymembers.get(1).getGuildId() <= 0 || partymembers.get(1).getGuildRank() > 1) {
				cm.sendOk("파티원 중에 길드가 없거나 길드 마스터가 아닌 사람이 있습니다.");
				cm.dispose();
			} else {
				var gs = cm.getGuild(cm.getPlayer().getGuildId());
				var gs2 = cm.getGuild(partymembers.get(1).getGuildId());
				if (gs.getAllianceId() > 0) {
					cm.sendOk("이미 길드 연합이 있습니다.");
					cm.dispose();
				} else if (gs2.getAllianceId() > 0) {
					cm.sendOk("상대 길드는 이미 연합이 있습니다.");
					cm.dispose();
				} else if (cm.partyMembersInMap() < 2) {
					cm.sendOk("파티원 전부 같은 맵에 있어야 합니다.");
					cm.dispose();
				} else
                			cm.sendYesNo("오, 정말로 연합을 형성하시겠어요?");
			}
		} else if (selection == 3) {
			if (cm.getPlayer().getGuildRank() == 1 && cm.getPlayer().getAllianceRank() == 1) {
				cm.sendYesNo("수용 가능 길드의 수를 늘리기 위해선 10,000,000 메소를 지불해야 합니다. 정말 늘리시겠습니까?"); //ExpandGuild Text
			} else {
			    cm.sendOk("오직 길드 마스터 만이 할 수 있는 업무입니다.");
				cm.dispose();
			}
		} else if(selection == 4) {
			if (cm.getPlayer().getGuildRank() == 1 && cm.getPlayer().getAllianceRank() == 1) {
				cm.sendYesNo("정말로 해체하시겠습니까?");
			} else {
				cm.sendOk("오직 길드 마스터 만이 할 수 있는 업무입니다.");
				cm.dispose();
			}
		}
	} else if(status == 2) {
	    if (choice == 2) {
		    cm.sendGetText("그럼 원하는 길드 연합 이름을 입력해주세요. (영문 12자, 한글 6자까지)");
		} else if (choice == 3) {
			if (cm.getPlayer().getGuildId() <= 0) {
				cm.sendOk("당신은 할 수 없습니다.");
				cm.dispose();
			} else {
				if (cm.addCapacityToAlliance()) {
					cm.sendOk("완료되었습니다.");
				} else {
					cm.sendOk("이미 꽉 차있네요. 5길드가 한계입니다.");
				}
				cm.dispose();
			}
		} else if (choice == 4) {
			if (cm.getPlayer().getGuildId() <= 0) {
				cm.sendOk("길드도 없는데 무슨 해체를 하신다고 그러세용.");
				cm.dispose();
			} else {
				if (cm.disbandAlliance()) {
					cm.sendOk("해체되었습니다.");
				} else {
					cm.sendOk("연합을 해체하는데 오류가 발생했습니다.");
				}
				cm.dispose();
			}
		}
	} else if (status == 3) {
		guildName = cm.getText();
	    cm.sendYesNo("길드 연합의 이름이 #b"+ guildName + "#k(이)가 맞습니까?");
	} else if (status == 4) {
			if (!cm.createAlliance(guildName)) {
				cm.sendNext("이미 있는 이름이네요. 다른 이름으로 하세요."); //Not real text
				status = 1;
				choice = 2;
			} else
				cm.sendOk("성공적으로 이루어졌습니다.");
			cm.dispose();
	}
}