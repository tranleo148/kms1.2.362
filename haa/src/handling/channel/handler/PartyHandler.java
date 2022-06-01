package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import server.maps.Event_DojoAgent;
import server.maps.FieldLimitType;
import server.quest.MapleQuest;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;

public class PartyHandler {
  public static final void DenyPartyRequest(LittleEndianAccessor slea, MapleClient c) {
    int action = slea.readByte();
    int partyid = slea.readInt();
    if (c.getPlayer().getParty() == null && c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122901)) == null) {
      MapleParty party = World.Party.getParty(partyid);
      if (party != null) {
        if (action == 33) {
          if (party.getMembers().size() < 6) {
            c.getPlayer().setParty(party);
            World.Party.updateParty(partyid, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
            c.getPlayer().receivePartyMemberHP();
            c.getPlayer().updatePartyMemberHP();
          } else {
            c.getPlayer().dropMessage(5, c.getPlayer().getName() + "님이 파티를 수락하셨습니다.");
          } 
        } else if (action == 32) {
          MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterById(party.getLeader().getId());
          if (cfrom != null)
            cfrom.dropMessage(5, c.getPlayer().getName() + "님이 파티 초대를 거절하셨습니다."); 
        } 
      } else {
        c.getPlayer().dropMessage(5, "가입하려는 파티가 존재하지 않습니다.");
      } 
    } else {
      c.getPlayer().dropMessage(5, "이미 파티에 가입되어 있어 파티에 가입할 수 없습니다.");
    } 
  }
  
    public static final void PartyOperation(LittleEndianAccessor slea, MapleClient c) {
        byte visible;
        String titlename;
        int partyid;
        byte visible__;
        String newTitle;
        /*  71 */ int operation = slea.readByte();
        /*  72 */ MapleParty party = c.getPlayer().getParty();
        /*  73 */ MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());
        /*  74 */ switch (operation) {
            case 1:
                titlename = slea.readMapleAsciiString();
                visible = slea.readByte();
                if (party == null) {
                    party = World.Party.createParty(partyplayer);
                    party.setVisible(visible);
                    party.setPartyTitle(titlename);
                    c.getPlayer().setParty(party);
                    c.getSession().writeAndFlush(CWvsContext.PartyPacket.partyCreated(party));
                } else if (partyplayer.equals(party.getLeader()) && party.getMembers().size() == 1) {
                    c.getSession().writeAndFlush(CWvsContext.PartyPacket.partyCreated(party));
                } else {
                    /*  88 */ c.getPlayer().dropMessage(5, "You can't create a party as you are already in one");
                }
                return;

            case 2:
                /*  93 */ if (party != null) {
                    /*  94 */ party.getVisible();
                    /*  95 */ if (partyplayer.equals(party.getLeader())) {
                        /*  96 */ if (GameConstants.isDojo(c.getPlayer().getMapId())) {
                            /*  97 */ Event_DojoAgent.failed(c.getPlayer());
                        }
                        /*  99 */ World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
                        /* 100 */ if (c.getPlayer().getEventInstance() != null) {
                            /* 101 */ c.getPlayer().getEventInstance().disbandParty();
                        }
                    } else {
                        /* 104 */ if (GameConstants.isDojo(c.getPlayer().getMapId())) {
                            /* 105 */ Event_DojoAgent.failed(c.getPlayer());
                        }
                        /* 107 */ World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
                        /* 108 */ if (c.getPlayer().getEventInstance() != null) {
                            /* 109 */ c.getPlayer().getEventInstance().leftParty(c.getPlayer());
                        }
                    }
                    /* 112 */ c.getPlayer().setParty(null);
                    /* 113 */ c.getPlayer().setBlessingAnsanble((byte) 1);
                }
                return;
            case 3:
                /* 117 */ partyid = slea.readInt();
                /* 118 */ if (party == null) {
                    /* 119 */ party = World.Party.getParty(partyid);
                    /* 120 */ if (party != null) {
                        /* 121 */ if (party.getMembers().size() < 6 && c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122901)) == null) {
                            /* 122 */ c.getPlayer().setParty(party);
                            /* 123 */ World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);
                            /* 124 */ c.getPlayer().receivePartyMemberHP();
                            /* 125 */ c.getPlayer().updatePartyMemberHP();
                        } else {
                            /* 127 */ c.getPlayer().dropMessage(5, "해당 파티는 이미 파티원이 꽉 찼습니다.");
                        }
                    } else {
                        /* 130 */ c.getPlayer().dropMessage(5, "가입하려는 파티는 존재하지 않습니다.");
                    }
                } else {
                    /* 133 */ c.getPlayer().dropMessage(5, "이미 파티에 가입되어 있어 파티에 가입할 수 없습니다.");
                }
                return;
            case 4:
                /* 137 */ if (party == null) {
                    /* 138 */ party = World.Party.createParty(partyplayer);
                    /* 139 */ party.setPartyTitle(c.getPlayer().getName() + "님의 파티");
                    /* 140 */ c.getPlayer().setParty(party);
                    /* 141 */ party.setPartyTitle(party.getPatryTitle());
                    /* 142 */ c.getSession().writeAndFlush(CWvsContext.PartyPacket.partyCreated(party));

                    /* 144 */ String theName = slea.readMapleAsciiString();
                    /* 145 */ int theCh = World.Find.findChannel(theName);
                    /* 146 */ if (theCh > 0) {
                        /* 147 */ MapleCharacter invited = ChannelServer.getInstance(theCh).getPlayerStorage().getCharacterByName(theName);
                        /* 148 */ if (invited != null && invited.getParty() == null && invited.getQuestNoAdd(MapleQuest.getInstance(122901)) == null) {
                            /* 149 */ if (party.getMembers().size() < 6) {
                                /* 150 */ c.getPlayer().dropMessage(1, invited.getName() + "님을 파티에 초대했습니다.");
                                /* 151 */ invited.getClient().getSession().writeAndFlush(CWvsContext.PartyPacket.partyInvite(c.getPlayer()));
                            } else {
                                /* 153 */ c.getPlayer().dropMessage(5, "이미 파티원이 최대로 가득 찬 상태입니다.");
                            }
                        } else {
                            /* 156 */ c.getPlayer().dropMessage(5, "이미 파티에 가입되어 있는 대상입니다.");
                        }
                    } else {
                        /* 159 */ c.getPlayer().dropMessage(5, "대상을 찾지 못했습니다.");
                    }
                } else {
                    /* 162 */ String theName = slea.readMapleAsciiString();
                    /* 163 */ int theCh = World.Find.findChannel(theName);
                    /* 164 */ if (theCh > 0) {
                        /* 165 */ MapleCharacter invited = ChannelServer.getInstance(theCh).getPlayerStorage().getCharacterByName(theName);
                        /* 166 */ if (invited != null && invited.getParty() == null && invited.getQuestNoAdd(MapleQuest.getInstance(122901)) == null) {
                            /* 167 */ if (party.getMembers().size() < 6) {
                                /* 168 */ c.getPlayer().dropMessage(1, invited.getName() + "님을 파티에 초대했습니다.");
                                /* 169 */ invited.getClient().getSession().writeAndFlush(CWvsContext.PartyPacket.partyInvite(c.getPlayer()));
                            } else {
                                /* 171 */ c.getPlayer().dropMessage(5, "이미 파티원이 최대로 가득 찬 상태입니다.");
                            }
                        } else {
                            /* 174 */ c.getPlayer().dropMessage(5, "이미 파티에 가입되어 있는 대상입니다.");
                        }
                    } else {
                        /* 177 */ c.getPlayer().dropMessage(5, "대상을 찾지 못했습니다.");
                    }
                }
                return;
            case 6:
                /* 182 */ if (party != null && partyplayer != null && partyplayer.equals(party.getLeader())) {
                    /* 183 */ MaplePartyCharacter expelled = party.getMemberById(slea.readInt());
                    /* 184 */ if (expelled != null) {
                        /* 185 */ if (GameConstants.isDojo(c.getPlayer().getMapId()) && expelled.isOnline()) {
                            /* 186 */ Event_DojoAgent.failed(c.getPlayer());
                        }
                        /* 188 */ World.Party.updateParty(party.getId(), PartyOperation.EXPEL, expelled);
                        /* 189 */ c.getPlayer().setBlessingAnsanble((byte) (c.getPlayer().getBlessingAnsanble() - 1));
                        /* 190 */ if (c.getPlayer().getEventInstance() != null) {

                            /* 194 */ if (expelled.isOnline()) {
                                /* 195 */ c.getPlayer().getEventInstance().disbandParty();
                            }
                        }
                    }
                }
                return;
            case 7:
                /* 202 */ if (party != null) {
                    /* 203 */ MaplePartyCharacter newleader = party.getMemberById(slea.readInt());
                    /* 204 */ if (newleader != null && partyplayer.equals(party.getLeader())) {
                        /* 205 */ World.Party.updateParty(party.getId(), PartyOperation.CHANGE_LEADER, newleader);
                    }
                }
                return;
            case 8:
                /* 210 */ if (slea.readByte() > 0) {
                    /* 211 */ c.getPlayer().getQuest_Map().remove(MapleQuest.getInstance(122900));
                } else {
                    /* 213 */ c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122900));
                }
                return;
            case 13:
                /* 217 */ visible__ = slea.readByte();
                /* 218 */ newTitle = slea.readMapleAsciiString();
                /* 219 */ if (newTitle.length() < 0) {
                    /* 220 */ c.getPlayer().dropMessage(1, "한 글자 이상의 변경할 파티명을 입력해주십시오.");
                    return;
                }
                /* 223 */ party.setVisible(visible__);
                /* 224 */ party.setPartyTitle(newTitle);
                /* 225 */ c.getSession().writeAndFlush(CWvsContext.PartyPacket.updateParty(c.getChannel(), party, PartyOperation.CHANGE_PARTY_TITLE, partyplayer));
                return;
        }
        /* 228 */ System.out.println("Unhandled Party function." + operation);
    }
  
  public static final void AllowPartyInvite(LittleEndianAccessor slea, MapleClient c) {
    if (slea.readByte() > 0) {
      c.getPlayer().getQuest_Map().remove(MapleQuest.getInstance(122901));
    } else {
      c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122901));
    } 
  }
  
  public static final void MemberSearch(LittleEndianAccessor slea, MapleClient c) {
    if (FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())) {
      c.getPlayer().dropMessage(5, "You may not do party search here.");
      return;
    } 
    c.getSession().writeAndFlush(CWvsContext.PartyPacket.showMemberSearch(c.getPlayer().getMap().getCharactersThreadsafe()));
  }
}
