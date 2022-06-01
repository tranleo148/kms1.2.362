/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
        /*
         * NPCID : 9201007
         * ScriptName : beginCeremony
         * NPCNameFunc : 안나 수녀님 - 결혼식장 도우미
         * Location : 680000210 ( - )
         * 
         * @author T-Sun
         *
         */

        var status = -1;
function action(mode, type, selection) {
    if (mode == 1 && type != 1) {
        status++;
    } else {
        if (type == 1 && mode == 1) {
            status++;
            selection = 1;
        } else if (type == 1 && mode == 0) {
            status++;
            selection = 0;
        } else {
            cm.dispose();
            return;
        }
    }
    if (status == 0) {
        var agent = cm.getMarriageAgent();
        var entry = cm.getMarriageData();
        if (agent.getDataEntry() != null) {
            if (!agent.canEnter()) {
                cm.sendOk("정말 너무나 잘 어울리는 커플이군요~ 식이 끝났다면 발렌티나 수녀님에게 말을 걸어 결혼식 이벤트를 진행할 수 있습니다.");
                cm.dispose();
                return;
            }
            if (entry != null && (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId())) {
                if (cm.checkWeddingStart() > 0) {
                    cm.sendOk("신랑과 신부가 파티를 맺고 웨딩홀에 모여 있는지 확인해주세요.");
                    cm.dispose();
                    return;
                }
//                if (entry.getReservedPeopleList().size() + 2 == cm.getPlayer().getMap().getCharacters().size()) {
                    cm.sendYesNo("두분 정말 너무나 잘 어울려요. 지금 결혼식을 시작하시겠어요? 결혼식을 시작하게 되면 하객분들은 더 이상 입장하실 수 없으니 하객분들이 모두 입장하신 후 시작하시기 바래요.");
//                }
//                else {
//                    cm.sendYesNo("아직 하객분들이 모두 입장하지 않은 것 같군요. 두분 정말 너무나 잘 어울려요. 지금 결혼식을 시작하시겠어요?");
//                }
            } else {
                cm.sendOk("결혼식이 곧 시작되니 잠시만 기다리세요.")
                cm.dispose();
            }
        } else {
            cm.sendOk("음.. 무언가 오류가 생겼군요.");
            cm.dispose();
        }
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendOk("아직 하객분들이 다 입장하지 못한 모양이지요?");
            cm.dispose();
            return;
        } else {
            var agent = cm.getMarriageAgent();
            agent.startEvent();
            cm.dispose();
        }
    }
}