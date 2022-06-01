package handling.channel.handler;

import client.BuddyList;
import client.BuddylistEntry;
import client.CharacterNameAndId;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;

public class BuddyListHandler {

    private static final class CharacterIdNameBuddyCapacity
            extends CharacterNameAndId {

        private int buddyCapacity;

        public CharacterIdNameBuddyCapacity(int id, int accId, String name, String repName, int level, int job, int buddyCapacity, String groupname, String memo) {
            /*  49 */ super(id, accId, name, repName, level, job, groupname, memo);
            /*  50 */ this.buddyCapacity = buddyCapacity;
        }

        public int getBuddyCapacity() {
            /*  54 */ return this.buddyCapacity;
        }
    }

    private static final void nextPendingRequest(MapleClient c) {
        /*  59 */ CharacterNameAndId pendingBuddyRequest = c.getPlayer().getBuddylist().pollPendingRequest();
        /*  60 */ if (pendingBuddyRequest != null) {
            /*  61 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.requestBuddylistAdd(pendingBuddyRequest.getId(), pendingBuddyRequest.getAccId(), pendingBuddyRequest.getName(), pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob(), c, pendingBuddyRequest.getGroupName(), pendingBuddyRequest.getMemo()));
        }
    }

    private static final CharacterIdNameBuddyCapacity getCharacterIdAndNameFromDatabase(String name, String groupname, String memo) throws SQLException {
        /*  66 */ Connection con = null;
        /*  67 */ PreparedStatement ps = null;
        /*  68 */ ResultSet rs = null;
        /*  69 */ CharacterIdNameBuddyCapacity ret = null;

        /*  71 */ try {
            con = DatabaseConnection.getConnection();
            /*  72 */ ps = con.prepareStatement("SELECT * FROM characters WHERE name LIKE ?");
            /*  73 */ ps.setString(1, name);
            /*  74 */ rs = ps.executeQuery();
            /*  75 */ if (rs.next()) {
                /*  76 */ ret = new CharacterIdNameBuddyCapacity(rs.getInt("id"), rs.getInt("accountid"), rs.getString("name"), rs.getString("name"), rs.getInt("level"), rs.getInt("job"), rs.getInt("buddyCapacity"), groupname, memo);
            }
            /*  78 */ rs.close();

            /*  80 */ ps.close();
            /*  81 */ con.close();
        } /*  82 */ catch (Exception exception) {

            /*  86 */ try {
                if (con != null) {
                    /*  87 */ con.close();
                }
                /*  89 */ if (ps != null) {
                    /*  90 */ ps.close();
                }
                /*  92 */ if (rs != null) {
                    /*  93 */ rs.close();
                }
            } /*  95 */ catch (SQLException se) /*  96 */ {
                se.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        /*  99 */ return ret;
    }

    public static final void BuddyOperation(LittleEndianAccessor slea, MapleClient c) {
        /* 103 */ int mode = slea.readByte();
        /* 104 */ BuddyList buddylist = c.getPlayer().getBuddylist();
        /* 105 */ if (mode == 1) {
            /* 106 */ String addName = slea.readMapleAsciiString();
            /* 107 */ int accid = MapleCharacterUtil.getAccByName(addName);
            /* 108 */ String groupName = slea.readMapleAsciiString();
            /* 109 */ String memo = slea.readMapleAsciiString();
            /* 110 */ byte accountBuddyCheck = slea.readByte();
            /* 111 */ String nickName = "";
            /* 112 */ if (accountBuddyCheck == 1) {
                /* 113 */ nickName = slea.readMapleAsciiString();
            }
            /* 115 */ BuddylistEntry ble = buddylist.get(accid);
            /* 116 */ if (addName.length() > 13 || groupName.length() > 16 || nickName.length() > 13 || memo.length() > 260) {
                return;
            }
            /* 119 */ if (ble != null && !ble.isVisible()) {
                /* 120 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "이미 친구로 등록되어 있습니다."));
                return;
            }
            /* 122 */ if (buddylist.isFull()) {
                /* 123 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "친구리스트가 꽉 찼습니다."));
                return;
            }
            /* 125 */ if (accid == c.getAccID()) {
                /* 126 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "자기계정에 있는 캐릭터는 친구추가 하실 수 없습니다."));
                return;
            }
            /* 128 */ if (accountBuddyCheck == 0) {
                /* 129 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "현재 이 기능은 사용하실 수 없습니다.\r\n아래 계정 통합 체크를 해주세요."));
                return;
            }
            try {
                int channel;
                /* 133 */ CharacterIdNameBuddyCapacity charWithId = null;

                /* 135 */ MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterByName(addName);
                /* 136 */ if (otherChar != null) {
                    /* 137 */ channel = c.getChannel();
                    /* 138 */ if (!otherChar.isGM() || c.getPlayer().isGM()) {
                        /* 139 */ charWithId = new CharacterIdNameBuddyCapacity(otherChar.getId(), otherChar.getAccountID(), otherChar.getName(), otherChar.getName(), otherChar.getLevel(), otherChar.getJob(), otherChar.getBuddylist().getCapacity(), groupName, memo);
                    }
                } else {
                    /* 142 */ channel = World.Find.findChannel(addName);
                    /* 143 */ charWithId = getCharacterIdAndNameFromDatabase(addName, groupName, memo);
                }

                /* 146 */ if (charWithId != null) {
                    /* 147 */ BuddyList.BuddyAddResult buddyAddResult = null;
                    /* 148 */ if (channel != -1) {
                        /* 149 */ buddyAddResult = World.Buddy.requestBuddyAdd(addName, c.getAccID(), c.getChannel(), c.getPlayer().getId(), c.getPlayer().getName(), c.getPlayer().getLevel(), c.getPlayer().getJob(), groupName, memo);
                    } else {
                        /* 151 */ Connection con = null;
                        /* 152 */ PreparedStatement ps = null;
                        /* 153 */ ResultSet rs = null;

                        /* 155 */ try {
                            con = DatabaseConnection.getConnection();
                            /* 156 */ ps = con.prepareStatement("SELECT COUNT(*) as buddyCount FROM buddies WHERE accid = ? AND pending = 0");
                            /* 157 */ ps.setInt(1, charWithId.getAccId());
                            /* 158 */ rs = ps.executeQuery();

                            /* 160 */ if (!rs.next()) {
                                /* 161 */ ps.close();
                                /* 162 */ rs.close();
                                /* 163 */ throw new RuntimeException("Result set expected");
                            }
                            /* 165 */ int count = rs.getInt("buddyCount");
                            /* 166 */ if (count >= charWithId.getBuddyCapacity()) {
                                /* 167 */ buddyAddResult = BuddyList.BuddyAddResult.BUDDYLIST_FULL;
                            }

                            /* 170 */ rs.close();
                            /* 171 */ ps.close();

                            /* 173 */ ps = con.prepareStatement("SELECT pending FROM buddies WHERE accid = ? AND buddyaccid = ?");
                            /* 174 */ ps.setInt(1, charWithId.getAccId());
                            /* 175 */ ps.setInt(2, c.getAccID());
                            /* 176 */ rs = ps.executeQuery();
                            /* 177 */ if (rs.next()) {
                                /* 178 */ buddyAddResult = BuddyList.BuddyAddResult.ALREADY_ON_LIST;
                            }
                            /* 180 */ rs.close();
                            /* 181 */ ps.close();
                            /* 182 */ con.close();
                        } /* 183 */ catch (Exception exception) {

                            /* 187 */ try {
                                if (con != null) {
                                    /* 188 */ con.close();
                                }
                                /* 190 */ if (ps != null) {
                                    /* 191 */ ps.close();
                                }
                                /* 193 */ if (rs != null) {
                                    /* 194 */ rs.close();
                                }
                            } /* 196 */ catch (SQLException se) /* 197 */ {
                                se.printStackTrace();
                            }
                        } finally {
                            try {
                                if (con != null) {
                                    con.close();
                                }
                                if (ps != null) {
                                    ps.close();
                                }
                                if (rs != null) {
                                    rs.close();
                                }
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }

                    }
                    /* 201 */ if (buddyAddResult == BuddyList.BuddyAddResult.BUDDYLIST_FULL) {
                        /* 202 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "상대 친구목록이 꽉 찼습니다."));
                        return;
                    }
                    /* 205 */ int displayChannel = -1;
                    /* 206 */ int otherCid = charWithId.getId();

                    /* 208 */ if (buddyAddResult == BuddyList.BuddyAddResult.ALREADY_ON_LIST) {
                        /* 209 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "이미 대상의 친구목록에 캐릭터가 있습니다."));
                        return;
                    }
                    /* 212 */ if (buddyAddResult != BuddyList.BuddyAddResult.ALREADY_ON_LIST && channel == -1) {
                        /* 213 */ Connection con = null;
                        /* 214 */ PreparedStatement ps = null;
                        /* 215 */ ResultSet rs = null;

                        /* 217 */ try {
                            con = DatabaseConnection.getConnection();
                            /* 218 */ ps = con.prepareStatement("INSERT INTO buddies (`accid`, `buddyaccid`, `groupname`, `pending`, `memo`) VALUES (?, ?, ?, 1, ?)");
                            /* 219 */ ps.setInt(1, charWithId.getAccId());
                            /* 220 */ ps.setInt(2, c.getAccID());
                            /* 221 */ ps.setString(3, groupName);
                            /* 222 */ ps.setString(4, (memo == null) ? "" : memo);
                            /* 223 */ ps.executeUpdate();
                            /* 224 */ ps.close();
                            /* 225 */ con.close();
                        } /* 226 */ catch (Exception exception) {

                            /* 230 */ try {
                                if (con != null) {
                                    /* 231 */ con.close();
                                }
                                /* 233 */ if (ps != null) {
                                    /* 234 */ ps.close();
                                }
                                /* 236 */ if (rs != null) {
                                    /* 237 */ rs.close();
                                }
                            } /* 239 */ catch (SQLException se) /* 240 */ {
                                se.printStackTrace();
                            }
                        } finally {
                            try {
                                if (con != null) {
                                    con.close();
                                }
                                if (ps != null) {
                                    ps.close();
                                }
                                if (rs != null) {
                                    rs.close();
                                }
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }

                    }
                    /* 244 */ buddylist.put(new BuddylistEntry(charWithId.getName(), charWithId.getName(), accid, otherCid, groupName, displayChannel, true, charWithId.getLevel(), charWithId.getJob(), memo));
                    /* 245 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.buddyAddMessage(addName));
                    /* 246 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.updateBuddylist(buddylist.getBuddies(), ble, (byte) 20));
                } else {

                    /* 249 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "대상을 발견하지 못했습니다."));
                }
                /* 251 */            } catch (SQLException e) {
              //  /* 252 */ System.err.println("SQLError" + e);
                /* 253 */ e.printStackTrace();
            }

            /* 256 */        } else if (mode == 2 || mode == 3) {

            /* 258 */ int otherAccId = slea.readInt();
            /* 259 */ if (!buddylist.isFull()) {
                /* 260 */ String otherName = null;
                /* 261 */ String groupName = "그룹 미지정", otherMemo = "";
                /* 262 */ int otherLevel = 0, otherJob = 0;
                /* 263 */ int ch = World.Find.findAccChannel(otherAccId);
                /* 264 */ if (ch > 0) {
                    /* 265 */ MapleCharacter otherChar = ChannelServer.getInstance(ch).getPlayerStorage().getClientById(otherAccId).getPlayer();
                    /* 266 */ if (otherChar == null) {
                        /* 267 */ for (CharacterNameAndId ca : c.getPlayer().getBuddylist().getPendingRequests()) {
                            /* 268 */ if (ca.getAccId() == otherAccId) {
                                /* 269 */ otherName = ca.getName();
                                /* 270 */ otherLevel = ca.getLevel();
                                /* 271 */ otherJob = ca.getJob();
                                /* 272 */ otherMemo = ca.getMemo();
                                break;
                            }
                        }
                    } else {
                        /* 277 */ otherName = otherChar.getName();
                        /* 278 */ otherLevel = otherChar.getLevel();
                        /* 279 */ otherJob = otherChar.getJob();
                    }
                    /* 281 */ if (otherName != null) {
                        /* 282 */ BuddylistEntry ble = new BuddylistEntry(otherName, otherName, otherAccId, otherChar.getId(), groupName, otherChar.getClient().getChannel(), true, otherLevel, otherJob, otherMemo);
                        /* 283 */ buddylist.put(ble);
                        /* 284 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.updateBuddylist(buddylist.getBuddies(), ble, (byte) 20));
                        /* 285 */ notifyRemoteChannel(c, otherChar.getClient().getChannel(), otherChar.getId(), BuddyList.BuddyOperation.ADDED, otherMemo);
                    }
                }
            }
            /* 289 */ nextPendingRequest(c);
        } /* 291 */ else if (mode == 4) {
            /* 292 */ int accId = slea.readInt();

            /* 294 */ if (buddylist.contains(accId)) {
                /* 295 */ buddylist.remove(accId);
                /* 296 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.deleteBuddy(accId));
            }

            /* 305 */ nextPendingRequest(c);
            /* 306 */        } else if (mode == 5) {
            /* 307 */ int accId = slea.readInt();

            /* 309 */ if (buddylist.contains(accId)) {
                /* 310 */ buddylist.remove(accId);
                /* 311 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.deleteBuddy(accId));
            }

            /* 320 */ nextPendingRequest(c);
            /* 321 */        } else if (mode == 6 || mode == 7) {
            /* 322 */ int accId = slea.readInt();
            /* 323 */ String otherMemo = "";
            /* 324 */ int ch = World.Find.findAccChannel(accId);
            /* 325 */ if (ch > 0) {
                /* 326 */ MapleCharacter otherChar = ChannelServer.getInstance(ch).getPlayerStorage().getClientById(accId).getPlayer();
                /* 327 */ if (buddylist.containsVisible(accId) && otherChar != null) {
                    /* 328 */ notifyRemoteChannel(c, otherChar.getClient().getChannel(), otherChar.getId(), BuddyList.BuddyOperation.DELETED, otherMemo);
                }
                /* 330 */ if (otherChar != null) {
                    /* 331 */ otherChar.getClient().getSession().writeAndFlush(CWvsContext.BuddylistPacket.buddyDeclineMessage(c.getPlayer().getName()));
                }
                /* 333 */ buddylist.remove(accId);
                /* 334 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.deleteBuddy(accId));
                /* 335 */ nextPendingRequest(c);
            }
            /* 337 */        } else if (mode == 10) {
            /* 338 */ if (c.getPlayer().getMeso() >= 50000L && c.getPlayer().getBuddyCapacity() < 100) {
                /* 339 */ c.getPlayer().setBuddyCapacity((byte) (c.getPlayer().getBuddyCapacity() + 5));
                /* 340 */ c.getPlayer().gainMeso(-50000L, false);
            } else {
                /* 342 */ c.getPlayer().dropMessage(1, "메소가 부족하거나 이미 친구 목록이 최대입니다.");
            }
            /* 344 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.updateBuddyCapacity(c.getPlayer().getBuddyCapacity()));
            /* 345 */        } else if (mode == 11) {
            /* 346 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "현재 이 기능은 사용하실 수 없습니다."));
         } else if (mode == 12) { //별명, 메모 편집
            /* 346 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "현재 이 기능은 사용하실 수 없습니다."));
        } else if (mode == 13) { //그룹 편집
            /* 346 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "현재 이 기능은 사용하실 수 없습니다."));
        } else if (mode == 14) { //그룹 편집
            /* 346 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "현재 이 기능은 사용하실 수 없습니다."));
            /* 370 */ c.getPlayer().dropMessage(5, "오프라인 상태로 변경되었습니다.");
        }
    }

    private static final void notifyRemoteChannel(MapleClient c, int remoteChannel, int otherCid, BuddyList.BuddyOperation operation, String memo) {
        /* 375 */ MapleCharacter player = c.getPlayer();
        /* 376 */ if (remoteChannel > 0) /* 377 */ {
            World.Buddy.buddyChanged(otherCid, player.getId(), player.getName(), c.getChannel(), operation, player.getLevel(), player.getJob(), c.getAccID(), memo);
        }
    }
}


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\handling\channel\handler\BuddyListHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
