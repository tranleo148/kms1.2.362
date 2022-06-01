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
 * NPCID : 9201021
 * ScriptName : weddingParty
 * NPCNameFunc : 비비안 - 웨딩 이벤트 도우미
 * Location : 680000600 ( - )
 * Location : 680000300 ( - )
 * Location : 680000401 ( - )
 * Location : 680000400 ( - )
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
        task = 0;
        var entry = cm.getMarriageData();
        var agent = cm.getMarriageAgent();
        if (agent.getDataEntry() != null) {
            if (entry != null && (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && agent.getDataEntry().getGroomId() == cm.getPlayer().getId() || agent.getDataEntry().getBrideId() == cm.getPlayer().getId())) {
                var entry = cm.getMarriageData();
                var agent = cm.getMarriageAgent();
                if (entry != null && agent.getDataEntry() != null) {
                    if (!agent.canEnter() && agent.isEndedCeremony()) {
                        if (agent.isFinaleStarted()) {
                            task = 1;
                            cm.sendYesNo("다음 이벤트로 넘어가고 싶으세요?");
                            return;
                        } else {
                        }
                    }
                }
            } else {
                cm.sendOk("한쌍의 커플이 드디어 맺어졌군요. 정말 아름다워요.");
                cm.dispose();
                return;
            }
        }
        cm.warp(680000500);
        cm.dispose();
    } else if (status == 1) {
        if (task == 1) {
            if (selection == -1) {
                var entry = cm.getMarriageData();
                var agent = cm.getMarriageAgent();
                if (entry != null && agent.getDataEntry() != null) {
                    if (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && entry.getGroomId() == cm.getPlayer().getId() || entry.getBrideId() == cm.getPlayer().getId()) {
                        var entry = cm.getMarriageData();
                        var agent = cm.getMarriageAgent();
                        if (entry != null && agent.getDataEntry() != null) {
                            if (!agent.canEnter() && agent.isEndedCeremony()) {
                                if (agent.isFinaleStarted()) {
                                    agent.doNextFinale();
                                } else {
                                    cm.warp(680000500);
                                }
                                cm.dispose();
                                return;
                            }
                        }
                    }
                }
            } else {
                cm.sendOk("아직 이곳에 더 머물고 싶으신건가요? 제한시간이 다되면 자동으로 넘어가니 그 전에 이동하고 싶다면 제게 다시 말을 걸어주세요.");
                cm.dispose();
                return;
            }
        }
        cm.warp(680000500);
        cm.dispose();
    }
}