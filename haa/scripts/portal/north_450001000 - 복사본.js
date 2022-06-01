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
 * PortalName : market00
 * Location : 910000000 (히든스트리트 - 자유시장입구)
 * 
 * @author T-Sun
 *
 */

function enter(pi) {
    var returnMap = pi.getSavedLocation("FREE_MARKET");
    pi.clearSavedLocation("FREE_MARKET");

    if (returnMap < 0) {
	returnMap = 102000000; // to fix people who entered the fm trough an unconventional way
    }
    var target = pi.getMap(returnMap);
    var portal;

    if (returnMap == 230000000) { // aquaroad has a different fm portal - maybe we should store the used portal too?
	portal = target.getPortal("market01");
    } else {
	if (returnMap == 100000000) { 
	portal = target.getPortal("market00");
    }
    if (portal == null) {
	portal = target.getPortal(0);
    }
    if (pi.getMapId() != target) {
	pi.getPlayer().changeMap(target, portal);
    }
	}
}