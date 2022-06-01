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
 * NPCID : 9201006
 * ScriptName : watingCathedral
 * NPCNameFunc : 발렌티나 수녀님 - 결혼식장 도우미
 * Location : 680000210 ( - )
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
    if (cm.getPlayer().getMapId() == 680000200) {
        if (status == 0) {
            var agent = cm.getMarriageAgent();
            var entry = cm.getMarriageData();
            if (agent != null) {
                if (entry != null && (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId())) {
                    cm.sendYesNo("이만 결혼식장으로 입장하시겠어요? 결혼식이 시작되기 전 제한시간이 다 되기전에 입장해 주시기 바랍니다. 또한, 입장하시면 로비에 입장한 하객 전원이 함께 이동됩니다.");
                } else {
                    cm.sendOk("신랑, 혹은 신부가 웨딩홀로 이동할 때 까지 기다려주세요.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("오류가 발생했어요.");
                cm.dispose();
                return;
            }

        } else if (status == 1) {
            if (selection == 0) {
                cm.sendOk("아직 남은 일이 있나요? 결혼식이 시작되기 전에 입장하셔야 합니다.");
                cm.dispose();
                return;
            }
            var agent = cm.getMarriageAgent();
            var entry = cm.getMarriageData();
            if (agent != null) {
                if (entry != null && (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId())) {
                    var chars = cm.getPlayer().getMap().getCharacters().iterator();
                    while (chars.hasNext()) {
                        var chr = chars.next();
                        var toMap = cm.getClient().getChannelServer().getMapFactory().getMap(680000210);
                        if (chr.getId() == agent.getDataEntry().getGroomId() || chr.getId() == agent.getDataEntry().getBrideId()) {
                            chr.changeMap(toMap, toMap.getPortal(2));
                        } else {
                            chr.changeMap(toMap, toMap.getPortal(1));
                        }
                    }
                    cm.dispose();
                } else {
                    cm.sendOk("신랑 혹은 신부가 웨딩홀로 입장하기 전까지 기다려주세요.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("음.. 무언가 오류가 생겼군요.");
                cm.dispose();
            }
        }
    } else if (cm.getPlayer().getMapId() == 680000210) {
        if (status == 0) {
            task = 0;
            var agent = cm.getMarriageAgent();
            var entry = cm.getMarriageData();
            if (agent != null && agent.getDataEntry() != null) {
                if (!agent.canEnter() && !agent.isEndedCeremony()) {
                    cm.sendOk("결혼식이 진행중일때는 나가실 수 없습니다.");
                    cm.dispose();
                    return;
                } else if (!agent.canEnter() && agent.isEndedCeremony()) {
                    task = 1;
                    if (entry != null && (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId())) {
                        cm.sendYesNo("정말 다정한 한쌍의 달팽이 같군요! 이만 결혼식을 마치고 퇴장하시겠어요?");
                    } else {
                        cm.sendOk("결혼식이 진행중일때는 나가실 수 없습니다.");
                        cm.dispose();
                    }
                    return;
                }
                if (entry != null && entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId()) {
                    cm.sendYesNo("웨딩 홀로 돌아가시고 싶으세요? 결혼식이 시작하기 전까지는 언제든지 이곳으로 돌아오실 수 있습니다.");
                } else {
                    cm.sendYesNo("웨딩 홀로 돌아가시고 싶으세요? 결혼식이 시작하기 전까지는 언제든지 이곳으로 돌아오실 수 있습니다.");
                }
            } else {
                cm.warp(680000500, 0);
                cm.dispose();
            }
        } else if (status == 1) {
            if (task == 0) {
                if (selection == 1) {
                    cm.warp(680000200, 0);
                    cm.dispose();
                } else {
                    cm.sendOk("결혼식이 곧 시작되니 조금만 기다려 주세요.");
                    cm.dispose();
                }
            } else if (task == 1) {
                if (selection == -1) {
                    var agent = cm.getMarriageAgent();
                    var entry = cm.getMarriageData();
                    if (entry != null && agent.getDataEntry() != null) {
                        if (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId()) {
                            var entry = cm.getMarriageData();
                            var agent = cm.getMarriageAgent();
                            if (entry != null && agent.getDataEntry() != null) {
                                if (!agent.canEnter() && agent.isEndedCeremony()) {
                                    if (agent.isFinaleStarted()) {
                                        cm.sendOk("이미 이벤트 피날레가 시작되었습니다.");
                                    } else {
                                        agent.finaleEvent();
                                    }
                                    cm.dispose();
                                    return;
                                }
                            }
                        }
                    }
                    cm.warp(680000500, 0);
                    cm.dispose();
                } else {
                    cm.sendOk("시간이 다 되면 자동으로 퇴장되니 그 이전에 나가고 싶으시다면 언제든지 제게 말을 걸어주세요.");
                    cm.dispose();
                }
            }
        }
    }
}