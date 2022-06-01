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
         * NPCID : 9201005
         * ScriptName : cathedral
         * NPCNameFunc : 클라랜스 수녀님 - 결혼식장 도우미
         * Location : 100000000 ( - )
         * Location : 680000200 ( - )
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
    if (cm.getPlayer().getMapId() == 100000000) {
        if (status == 0) {
            cm.sendSimple("결혼을 도와드릴게요.\r\n#b #L0#결혼할 준비가 되었어요.#l\r\n #L1#결혼에 초대받았어요!#l");
        } else if (status == 1) {
            if (selection == 0) {
                var data = cm.getMarriageData(cm.getPlayer().getMarriageId());
                if (data == null || data.getWeddingStatus() < 7) {
                    cm.sendOk("결혼식이 예약되어 있지 않으신 것 같군요. 결혼식에 초대받으셨다면 제게 다시 말을 거신 후 #b결혼에 초대받았어요!#k를 클릭하세요.");
                    cm.dispose();
                    return;
                }
                if (!cm.haveItem(4213001, 1)) {
                    cm.sendOk("신부님의 주례 승낙서가 없다면 결혼식을 예약하실 수 없습니다.");
                    cm.dispose();
                    return;
                }
                var agent = cm.getMarriageAgent();
                if (!agent.canStart()) {
                    cm.sendOk("이미 다른 커플이 이 채널에서 결혼식을 진행중이에요. 결혼식이 끝날때 까지 기다리거나, 다른 채널에서 결혼식을 진행해 주세요.");
                    cm.dispose();
                    return;
                }
                var ret = cm.checkWeddingStart();
                if (ret > 0) {
                    cm.sendOk("결혼은 약혼 상태에서 신랑과 신부가 될 분 두분이 파티를 맺고, 현재 맵에 모여 계셔야 가능합니다.");
                    cm.dispose();
                    return;
                }
                cm.gainItem(4213001, -1);
                agent.registerEvent(cm.getPlayer());
                cm.warpParty(680000200);
                cm.partyMessage(5, "제한시간 내에 웨딩홀 내 안나 수녀님을 통해 결혼식을 시작해야 이벤트와 각종 보상을 얻을 수 있습니다.");
                cm.dispose();
            } else if (selection == 1) {
                var agent = cm.getMarriageAgent();
                if (agent.getDataEntry() != null) {
                    var entry = agent.getDataEntry();
                    if (entry.getReservedPeopleList().contains(java.lang.Integer.valueOf(cm.getPlayer().getId()))) {
                        if (agent.canEnter()) {
                            if (!entry.getEnteredPeopleList().contains(java.lang.Integer.valueOf(cm.getPlayer().getId()))) {
                                cm.warp(680000200);
                                entry.getEnteredPeopleList().add(java.lang.Integer.valueOf(cm.getPlayer().getId()));
                            } else {
                                cm.sendOk("이미 결혼식에 입장하셨었기 때문에 더 이상 입장할 수 없습니다.");
                            }
//                                cm.playerMessage(6, "안젤리크에게 결혼 예물을 보낸 후, 결혼식 시작 전에 발렌티나 수녀님을 통해 웨딩홀로 입장해주세요.")
                        } else
                            cm.sendOk("이미 결혼식이 시작되어 입장이 불가능합니다.");
                        cm.dispose();
                    } else {
                        cm.sendOk("결혼식에 초대받은 분만 입장 가능합니다.");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("현재 시작되어 있는 결혼식이 없군요.");
                    cm.dispose();
                }
            }
        }
    } else {
        if (status == 0) {
            cm.sendYesNo("정말 이곳에서 나가 웨딩빌리지로 돌아가시고 싶으신가요?");
        } else if (status == 1) {
            if (selection == 1) {
                cm.warp(680000500);
                cm.dispose();
            } else {
                cm.sendOk("잠시 후 결혼식이 시작되니 발렌티나 수녀님을 통해 입장하실 수 있습니다.");
                cm.dispose();
            }
        }
    }
}