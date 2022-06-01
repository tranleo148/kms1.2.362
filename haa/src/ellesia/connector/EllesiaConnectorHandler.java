/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellesia.connector;


import ellesia.connector.netty.EllesiaPacketCreator;
import handling.login.handler.AutoRegister;
import scripting.NPCConversationManager;
import tools.data.LittleEndianAccessor;
import javax.xml.bind.DatatypeConverter;

public class EllesiaConnectorHandler {
    //서버 핑 -> 접속기 퐁
    public static final void HandlePacket(final EllesiaPacketOpcode header, final LittleEndianAccessor slea, final EllesiaClient c) throws InterruptedException {
        switch (header) {
            case HEARTBEAT: {
                c.Ping();
                break;
            }
            case LOGIN: {
                String id = slea.readMapleAsciiString();
                String pw = slea.readMapleAsciiString();
                boolean success = EllesiaWalker.Login(id, pw);
                if (success) {
                    c.setId((id));
                    EllesiaWalker.setAlive(c.getId(), true);
                    EllesiaWalker.setIP(c.getId(), c.getIP());
                    c.clearCRCFailed();
                }
                c.sendPacket(EllesiaPacketCreator.Login(success));
                break;
            }
            case CRC: {
                byte[] crc = slea.read(20);
                String crcs = DatatypeConverter.printHexBinary(crc);
                if (!crcs.equalsIgnoreCase("FFAB7BC8ABD94150D45581D4A9019074F534CADB")) {
                    if (!crcs.equalsIgnoreCase(EllesiaSettings.CRC)) {
                        if (!c.getId().equalsIgnoreCase("")) {
                            c.CRCFailed();
                            if (c.getCRCFailed() >= 2) {
                                System.out.println(c.getId() + " failed to validate crc : " + crcs);
                            }
                            if (c.getCRCFailed() > 4) {
                                String data = "아이디 : " + c.getId() + " 아이피 : " + c.getIP() + " 변조 : " + crcs + "\r\n";
                                NPCConversationManager.writeLog("Log/Ellesia/CRCCheck.log", data, true);
                                EllesiaWalker.setBanned(c.getId(), true, "CRC Failed Check Plz");
                                c.closeSession();
                            }
                        }
                    }
                }
                break;
            }
            case CHECKSUM: {
                String wz = slea.readMapleAsciiString();
                String wzhash = slea.readMapleAsciiString();
                boolean equal = false;
                if (EllesiaSettings.Files.containsKey(wz)) {
                    equal = EllesiaSettings.Files.get(wz).equalsIgnoreCase(wzhash);
                }
                c.sendPacket(
                        EllesiaPacketCreator.WzResult(wz, equal)
                );
                if (!equal && !wzhash.equalsIgnoreCase("")) {
                    EllesiaWalker.setAlive(c.getId(), false);
                    System.out.println(c.getId() + " failed to validate wz : " + wz);
                    String data = "아이디 : " + c.getId() + " 아이피 : " + c.getIP() + " 변조된 위젯 : " + wz + ", Hash : " + wzhash + "\r\n";
                    NPCConversationManager.writeLog("Log/Ellesia/WzHashCheck.log", data, true);
                }
                break;
            }
            case REGISTER: {
                String id = slea.readMapleAsciiString();
                String pw = slea.readMapleAsciiString();

                c.sendPacket(
                        EllesiaPacketCreator.RegisterResult(AutoRegister.createAccount(id, pw, c.getSession().remoteAddress().toString()))
                );
                break;
            }
            case VERSION: {
                int version = slea.readInt();
                if (EllesiaSettings.ConnectorVersion != version) {
                    c.getSession().close();
                }
                break;
            }
            case FILE_LIST: {
                c.sendPacket(
                        EllesiaPacketCreator.FileList(EllesiaSettings.Files.keySet().toArray(new String[EllesiaSettings.Files.keySet().size()]))
                );
                break;
            }
            default: {
                System.out.println("[UNHANDLED] Recv [" + header + "] found");
                break;
            }
        }
    }
}
