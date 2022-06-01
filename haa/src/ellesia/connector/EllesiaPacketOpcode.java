/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellesia.connector;

public enum EllesiaPacketOpcode {
    LOGIN(0x00), //Login
    HEARTBEAT(0x01), //Ping Pong with Online Users
    CRC(0x02), //CRC Hash
    FILE_LIST(0x03), //Checksum File List
    CHECKSUM(0x04), //Checksum Result
    REGISTER(0x05), //Register account
    VERSION(0x06); //Connector Version check

    private int code = -2;

    public final int getValue() {
        return code;
    }

    private EllesiaPacketOpcode(int code) {
        this.code = code;
    }
}
