package constants.programs;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleCoolDownValueHolder;
import client.SkillFactory;
import constants.KoreaCalendar;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import server.Setting;
import server.ShutdownServer;
import server.Timer;
import tools.HexTool;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;

public class AdminTool extends JFrame {
  private List<String> names;
  
  public AdminTool() {
    this.names = new ArrayList<>();
    this.itemid = new ArrayList<>();
    initComponents();
  }
  
  private void initComponents() {
    this.jButton18 = new JButton();
    this.jScrollPane1 = new JScrollPane();
    this.jList1 = new JList<>();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.jTabbedPane2 = new JTabbedPane();
    this.jPanel3 = new JPanel();
    this.jScrollPane3 = new JScrollPane();
    this.jList3 = new JList<>();
    this.jLabel3 = new JLabel();
    this.jTextField1 = new JTextField();
    this.jTextField2 = new JTextField();
    this.jLabel4 = new JLabel();
    this.jLabel5 = new JLabel();
    this.jButton4 = new JButton();
    this.jButton5 = new JButton();
    this.jButton6 = new JButton();
    this.jButton7 = new JButton();
    this.jButton8 = new JButton();
    this.후원포인트체크여부2 = new JCheckBox();
    this.후원포인트체크여부1 = new JCheckBox();
    this.리붓시간 = new JTextField();
    this.리붓 = new JButton();
    this.쪽지닉네임입력 = new JTextField();
    this.쪽지내용 = new JTextField();
    this.쪽지보내기 = new JButton();
    this.닉네임입력 = new JTextField();
    this.포인트입력 = new JTextField();
    this.포인트지급 = new JButton();
    this.홍보포인트체크여부 = new JCheckBox();
    this.후원포인트체크여부 = new JCheckBox();
    this.GM설정 = new JButton();
    this.현접해제1 = new JTextField();
    this.현접해제 = new JButton();
    this.jPanel4 = new JPanel();
    this.jButton10 = new JButton();
    this.jButton11 = new JButton();
    jCheckBox1 = new JCheckBox();
    jTabbedPane1 = new JTabbedPane();
    this.jPanel5 = new JPanel();
    this.jScrollPane2 = new JScrollPane();
    jList2 = new JList<>();
    this.jPanel6 = new JPanel();
    this.jScrollPane5 = new JScrollPane();
    jList4 = new JList<>();
    this.jPanel7 = new JPanel();
    this.jScrollPane7 = new JScrollPane();
    jList5 = new JList<>();
    this.jPanel8 = new JPanel();
    this.jScrollPane8 = new JScrollPane();
    jList6 = new JList<>();
    this.jPanel9 = new JPanel();
    this.jScrollPane9 = new JScrollPane();
    jList7 = new JList<>();
    this.jPanel10 = new JPanel();
    this.jScrollPane10 = new JScrollPane();
    jList8 = new JList<>();
    this.jPanel11 = new JPanel();
    this.jScrollPane11 = new JScrollPane();
    jList9 = new JList<>();
    this.jPanel12 = new JPanel();
    this.jScrollPane12 = new JScrollPane();
    jList10 = new JList<>();
    this.jPanel13 = new JPanel();
    this.jScrollPane13 = new JScrollPane();
    jList11 = new JList<>();
    this.jPanel1 = new JPanel();
    this.jScrollPane4 = new JScrollPane();
    this.jTextPane1 = new JTextPane();
    this.jLabel6 = new JLabel();
    this.jButton9 = new JButton();
    this.jButton12 = new JButton();
    this.jLabel7 = new JLabel();
    this.jLabel8 = new JLabel();
    this.jButton13 = new JButton();
    this.jButton14 = new JButton();
    this.jButton16 = new JButton();
    this.jButton17 = new JButton();
    this.jPanel2 = new JPanel();
    this.jButton15 = new JButton();
    this.jScrollPane6 = new JScrollPane();
    this.jTextArea1 = new JTextArea();
    this.jButton1 = new JButton();
    this.jButton2 = new JButton();
    this.jButton3 = new JButton();
    this.jButton19 = new JButton();
    setDefaultCloseOperation(2);
    setTitle("관리기");
    setResizable(false);
    this.jButton18.setText("골드애플 초기화");
    this.jButton18.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton18ActionPerformed(evt);
          }
        });
    this.jScrollPane1.setViewportView(this.jList1);
    this.jLabel1.setText("현재 접속자");
    this.jLabel2.setText("동접 : 0");
    this.jScrollPane3.setViewportView(this.jList3);
    this.jLabel3.setText("아이템 (코드, 갯수)");
    this.jTextField1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jTextField1ActionPerformed(evt);
          }
        });
    this.jLabel4.setText("아이템 코드");
    this.jLabel5.setText("갯수");
    this.jButton4.setText("아이템 추가");
    this.jButton4.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton4ActionPerformed(evt);
          }
        });
    this.jButton5.setText("아이템 제거");
    this.jButton5.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton5ActionPerformed(evt);
          }
        });
    this.jButton6.setText("아이템 지급");
    this.jButton6.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton6ActionPerformed(evt);
          }
        });
    this.jButton7.setText("핫타임");
    this.jButton7.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton7ActionPerformed(evt);
          }
        });
    this.jButton8.setText("럭셔리타임");
    this.jButton8.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton8ActionPerformed(evt);
          }
        });
    this.후원포인트체크여부2.setText("패치");
    this.후원포인트체크여부1.setText("점검");
    this.리붓시간.setText("리붓 시간(분)");
    this.리붓시간.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.리붓시간ActionPerformed(evt);
          }
        });
    this.리붓.setText("리붓");
    this.리붓.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.리붓ActionPerformed(evt);
          }
        });
    this.쪽지닉네임입력.setText("닉네임 입력");
    this.쪽지닉네임입력.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.쪽지닉네임입력ActionPerformed(evt);
          }
        });
    this.쪽지내용.setText("쪽지 내용 입력");
    this.쪽지내용.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.쪽지내용ActionPerformed(evt);
          }
        });
    this.쪽지보내기.setText("쪽지 보내기");
    this.쪽지보내기.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.쪽지보내기ActionPerformed(evt);
          }
        });
    this.닉네임입력.setText("닉네임 입력");
    this.닉네임입력.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.닉네임입력ActionPerformed(evt);
          }
        });
    this.포인트입력.setText("지급 할 포인트 입력");
    this.포인트입력.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.포인트입력ActionPerformed(evt);
          }
        });
    this.포인트지급.setText("포인트 지급");
    this.포인트지급.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.포인트지급ActionPerformed(evt);
          }
        });
    this.홍보포인트체크여부.setText("홍보 포인트");
    this.후원포인트체크여부.setText("후원 포인트");
    this.후원포인트체크여부.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.후원포인트체크여부ActionPerformed(evt);
          }
        });
    this.GM설정.setText("GM설정");
    this.GM설정.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.GM설정ActionPerformed(evt);
          }
        });
    this.현접해제1.setText("현접 해제 할 닉네임을 입력 해주세요.");
    this.현접해제1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.현접해제1ActionPerformed(evt);
          }
        });
    this.현접해제.setText("현접 해제");
    this.현접해제.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.현접해제ActionPerformed(evt);
          }
        });
    GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
    this.jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.jLabel3, GroupLayout.Alignment.LEADING, -1, -1, 32767).addComponent(this.jScrollPane3, GroupLayout.Alignment.LEADING, -2, 0, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButton4, -1, -1, 32767).addComponent(this.jButton6, -1, -1, 32767).addComponent(this.jButton5, -1, -1, 32767).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addComponent(this.jTextField1, -2, 89, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jTextField2, -2, 38, -2)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jLabel4).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel5))).addGap(0, 100, 32767))).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButton7, -2, 106, -2).addComponent(this.jButton8, -2, 106, -2).addComponent(this.GM설정, GroupLayout.Alignment.TRAILING, -2, 106, -2))).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.후원포인트체크여부).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.홍보포인트체크여부).addGap(0, 0, 32767)))).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup().addComponent(this.닉네임입력, -2, 108, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.포인트입력)).addGroup(GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup().addComponent(this.쪽지닉네임입력, -2, 108, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.쪽지내용, -2, 139, -2)).addComponent(this.현접해제1)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.포인트지급, -2, 108, -2).addComponent(this.쪽지보내기, -2, 108, -2).addComponent(this.현접해제, -2, 108, -2))).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.리붓시간, -2, 108, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.리붓)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.후원포인트체크여부1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.후원포인트체크여부2))).addGap(0, 0, 32767))).addContainerGap()));
    jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.jLabel4).addComponent(this.jLabel5)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(this.jScrollPane3, -2, 229, -2).addGap(18, 19, 32767)).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jTextField1, -2, -1, -2).addComponent(this.jTextField2, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton4).addComponent(this.jButton7)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton5).addComponent(this.jButton8)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton6).addComponent(this.GM설정)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.홍보포인트체크여부).addComponent(this.후원포인트체크여부)).addGap(2, 2, 2))).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.포인트지급).addComponent(this.포인트입력, -2, -1, -2).addComponent(this.닉네임입력, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 7, 32767).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.쪽지닉네임입력, -2, -1, -2).addComponent(this.쪽지내용, -2, -1, -2).addComponent(this.쪽지보내기)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 12, 32767).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.현접해제).addComponent(this.현접해제1, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.리붓시간, -2, -1, -2).addComponent(this.리붓)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.후원포인트체크여부1).addComponent(this.후원포인트체크여부2)).addGap(17, 17, 17)));
    this.jTabbedPane2.addTab("Item", this.jPanel3);
    this.jPanel4.setPreferredSize(new Dimension(370, 400));
    this.jButton10.setText("채팅 청소");
    this.jButton10.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton10ActionPerformed(evt);
          }
        });
    this.jButton11.setText("얼리기");
    this.jButton11.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton11ActionPerformed(evt);
          }
        });
    jCheckBox1.setText("Scroll Lock");
    jCheckBox1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jCheckBox1ActionPerformed(evt);
          }
        });
    this.jScrollPane2.setViewportView(jList2);
    GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
    this.jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane2, -1, 463, 32767).addContainerGap()));
    jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane2, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("일반", this.jPanel5);
    this.jScrollPane5.setViewportView(jList4);
    GroupLayout jPanel6Layout = new GroupLayout(this.jPanel6);
    this.jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane5, -1, 463, 32767).addContainerGap()));
    jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane5, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("전체", this.jPanel6);
    this.jScrollPane7.setViewportView(jList5);
    GroupLayout jPanel7Layout = new GroupLayout(this.jPanel7);
    this.jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane7, -1, 463, 32767).addContainerGap()));
    jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane7, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("친구", this.jPanel7);
    this.jScrollPane8.setViewportView(jList6);
    GroupLayout jPanel8Layout = new GroupLayout(this.jPanel8);
    this.jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane8, -1, 463, 32767).addContainerGap()));
    jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane8, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("길드", this.jPanel8);
    this.jScrollPane9.setViewportView(jList7);
    GroupLayout jPanel9Layout = new GroupLayout(this.jPanel9);
    this.jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -1, 463, 32767).addContainerGap()));
    jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane9, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("파티", this.jPanel9);
    this.jScrollPane10.setViewportView(jList8);
    GroupLayout jPanel10Layout = new GroupLayout(this.jPanel10);
    this.jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel10Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane10, -1, 463, 32767).addContainerGap()));
    jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel10Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane10, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("메신저", this.jPanel10);
    this.jScrollPane11.setViewportView(jList9);
    GroupLayout jPanel11Layout = new GroupLayout(this.jPanel11);
    this.jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel11Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane11, -1, 463, 32767).addContainerGap()));
    jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel11Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane11, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("귓속말", this.jPanel11);
    this.jScrollPane12.setViewportView(jList10);
    GroupLayout jPanel12Layout = new GroupLayout(this.jPanel12);
    this.jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane12, -1, 463, 32767).addContainerGap()));
    jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel12Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane12, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("교환", this.jPanel12);
    this.jScrollPane13.setViewportView(jList11);
    GroupLayout jPanel13Layout = new GroupLayout(this.jPanel13);
    this.jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel13Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane13, -1, 463, 32767).addContainerGap()));
    jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel13Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane13, -1, 404, 32767).addContainerGap()));
    jTabbedPane1.addTab("상점&미니게임", this.jPanel13);
    GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
    this.jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jButton10, -2, 97, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton11, -2, 97, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox1).addGap(0, 0, 32767))).addContainerGap()));
    jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton10, -2, 19, -2).addComponent(this.jButton11, -2, 19, -2).addComponent(jCheckBox1)).addContainerGap()));
    this.jTabbedPane2.addTab("Chat", this.jPanel4);
    this.jScrollPane4.setViewportView(this.jTextPane1);
    this.jLabel6.setText("패킷");
    this.jButton9.setText("패킷 보내기");
    this.jButton9.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton9ActionPerformed(evt);
          }
        });
    this.jButton12.setText("스킬캐싱");
    this.jButton12.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton12ActionPerformed(evt);
          }
        });
    this.jLabel7.setText("스킬");
    this.jLabel8.setText("패킷출력");
    this.jButton13.setText("리시브 패킷");
    this.jButton13.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton13ActionPerformed(evt);
          }
        });
    this.jButton14.setText("샌드 패킷");
    this.jButton14.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton14ActionPerformed(evt);
          }
        });
    this.jButton16.setText("해쉬");
    this.jButton16.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton16ActionPerformed(evt);
          }
        });
    this.jButton17.setText("옵코드리로드");
    this.jButton17.setActionCommand("스택 제거");
    this.jButton17.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton17ActionPerformed(evt);
          }
        });
    GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
    this.jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane4).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel6).addComponent(this.jButton9, -1, -1, 32767).addComponent(this.jButton12, -1, -1, 32767).addComponent(this.jLabel7).addComponent(this.jLabel8).addComponent(this.jButton13, -1, -1, 32767).addComponent(this.jButton14, -1, -1, 32767).addComponent(this.jButton16, -1, -1, 32767).addComponent(this.jButton17, -1, -1, 32767)).addGap(0, 369, 32767))).addContainerGap()));
    jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel6).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane4, -2, 118, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jButton9).addGap(16, 16, 16).addComponent(this.jLabel7).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton12).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel8).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jButton13).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton14).addGap(26, 26, 26).addComponent(this.jButton16).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton17).addContainerGap(-1, 32767)));
    this.jTabbedPane2.addTab("Develop", this.jPanel1);
    this.jButton15.setText("공지");
    this.jButton15.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton15ActionPerformed(evt);
          }
        });
    this.jTextArea1.setColumns(20);
    this.jTextArea1.setRows(5);
    this.jTextArea1.setLineWrap(true);
    this.jScrollPane6.setViewportView(this.jTextArea1);
    GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
    this.jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap(29, 32767).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane6, -2, 434, -2).addComponent(this.jButton15, -2, 67, -2)).addGap(26, 26, 26)));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane6, -2, 138, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton15, -2, 23, -2).addContainerGap(313, 32767)));
    this.jTabbedPane2.addTab("Notice", this.jPanel2);
    this.jButton1.setText("새로고침");
    this.jButton1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton1ActionPerformed(evt);
          }
        });
    this.jButton2.setText("접속종료");
    this.jButton2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton2ActionPerformed(evt);
          }
        });
    this.jButton3.setText("쿨타임초기화");
    this.jButton3.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton3ActionPerformed(evt);
          }
        });
    this.jButton19.setText("네오코어 초기화");
    this.jButton19.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            AdminTool.this.jButton19ActionPerformed(evt);
          }
        });
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel1).addComponent(this.jScrollPane1, -2, 0, 32767).addComponent(this.jLabel2).addComponent(this.jButton1, -2, 90, -2).addComponent(this.jButton2, -2, 90, -2).addComponent(this.jButton18, -1, -1, 32767).addComponent(this.jButton3, -1, -1, 32767).addComponent(this.jButton19, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jTabbedPane2, -2, 491, -2).addContainerGap(-1, 32767)));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTabbedPane2, -2, 0, 32767).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -2, 264, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton2, -2, 27, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton1, -2, 27, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton3, -2, 27, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton18).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton19).addGap(0, 23, 32767))).addContainerGap()));
    pack();
  }
  
  private static DefaultListModel ChattingList0 = new DefaultListModel();
  
  private static DefaultListModel ChattingList1 = new DefaultListModel();
  
  private static DefaultListModel ChattingList2 = new DefaultListModel();
  
  private static DefaultListModel ChattingList3 = new DefaultListModel();
  
  private static DefaultListModel ChattingList4 = new DefaultListModel();
  
  private static DefaultListModel ChattingList5 = new DefaultListModel();
  
  private static DefaultListModel ChattingList6 = new DefaultListModel();
  
  private static DefaultListModel ChattingList7 = new DefaultListModel();
  
  private static DefaultListModel ChattingList8 = new DefaultListModel();
  
  private final List<String> itemid;
  
  private JButton GM설정;
  
  private JButton jButton1;
  
  private JButton jButton10;
  
  private JButton jButton11;
  
  private JButton jButton12;
  
  private JButton jButton13;
  
  private JButton jButton14;
  
  private JButton jButton15;
  
  private JButton jButton16;
  
  private JButton jButton17;
  
  private JButton jButton18;
  
  private JButton jButton19;
  
  private JButton jButton2;
  
  private JButton jButton3;
  
  private JButton jButton4;
  
  private JButton jButton5;
  
  private JButton jButton6;
  
  private JButton jButton7;
  
  private JButton jButton8;
  
  private JButton jButton9;
  
  private static JCheckBox jCheckBox1;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JLabel jLabel3;
  
  private JLabel jLabel4;
  
  private JLabel jLabel5;
  
  private JLabel jLabel6;
  
  private JLabel jLabel7;
  
  private JLabel jLabel8;
  
  private JList<String> jList1;
  
  private static JList<String> jList10;
  
  private static JList<String> jList11;
  
  private static JList<String> jList2;
  
  private JList<String> jList3;
  
  private static JList<String> jList4;
  
  private static JList<String> jList5;
  
  private static JList<String> jList6;
  
  private static JList<String> jList7;
  
  private static JList<String> jList8;
  
  private static JList<String> jList9;
  
  private JPanel jPanel1;
  
  private JPanel jPanel10;
  
  private JPanel jPanel11;
  
  private JPanel jPanel12;
  
  private JPanel jPanel13;
  
  private JPanel jPanel2;
  
  private JPanel jPanel3;
  
  private JPanel jPanel4;
  
  private JPanel jPanel5;
  
  private JPanel jPanel6;
  
  private JPanel jPanel7;
  
  private JPanel jPanel8;
  
  private JPanel jPanel9;
  
  private JScrollPane jScrollPane1;
  
  private JScrollPane jScrollPane10;
  
  private JScrollPane jScrollPane11;
  
  private JScrollPane jScrollPane12;
  
  private JScrollPane jScrollPane13;
  
  private JScrollPane jScrollPane2;
  
  private JScrollPane jScrollPane3;
  
  private JScrollPane jScrollPane4;
  
  private JScrollPane jScrollPane5;
  
  private JScrollPane jScrollPane6;
  
  private JScrollPane jScrollPane7;
  
  private JScrollPane jScrollPane8;
  
  private JScrollPane jScrollPane9;
  
  private static JTabbedPane jTabbedPane1;
  
  private JTabbedPane jTabbedPane2;
  
  private JTextArea jTextArea1;
  
  private JTextField jTextField1;
  
  private JTextField jTextField2;
  
  private JTextPane jTextPane1;
  
  private JTextField 닉네임입력;
  
  private JButton 리붓;
  
  private JTextField 리붓시간;
  
  private JTextField 쪽지내용;
  
  private JTextField 쪽지닉네임입력;
  
  private JButton 쪽지보내기;
  
  private JTextField 포인트입력;
  
  private JButton 포인트지급;
  
  private JButton 현접해제;
  
  private JTextField 현접해제1;
  
  private JCheckBox 홍보포인트체크여부;
  
  private JCheckBox 후원포인트체크여부;
  
  private JCheckBox 후원포인트체크여부1;
  
  private JCheckBox 후원포인트체크여부2;
  
  public List<String> getSelectedName() {
    return this.jList1.getSelectedValuesList();
  }
  
  public String StringFilter(BufferedReader check, BufferedReader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    while (check.readLine() != null) {
      String line = reader.readLine();
      if (line.indexOf("//") != -1)
        line = line.replace(line.substring(line.indexOf("//"), line.length()), ""); 
      builder.append(line);
    } 
    return builder.toString();
  }
  
  private static DefaultListModel getChattingList(int index) {
    switch (index) {
      case 0:
        return ChattingList0;
      case 1:
        return ChattingList1;
      case 2:
        return ChattingList2;
      case 3:
        return ChattingList3;
      case 4:
        return ChattingList4;
      case 5:
        return ChattingList5;
      case 6:
        return ChattingList6;
      case 7:
        return ChattingList7;
      case 8:
        return ChattingList8;
    } 
    return null;
  }
  
  private static JList<String> getChatting(int index) {
    switch (index) {
      case 0:
        return jList2;
      case 1:
        return jList4;
      case 2:
        return jList5;
      case 3:
        return jList6;
      case 4:
        return jList7;
      case 5:
        return jList8;
      case 6:
        return jList9;
      case 7:
        return jList10;
      case 8:
        return jList11;
    } 
    return null;
  }
  
  public static void addMessage(int index, String msg) {
    try {
      if (getChattingList(index).size() >= 300)
        getChattingList(index).removeElementAt(0); 
      getChattingList(index).addElement(msg);
      getChatting(index).setModel(getChattingList(index));
      if (!jCheckBox1.isSelected())
        getChatting(index).ensureIndexIsVisible(getChattingList(index).size() - 2); 
    } catch (Throwable throwable) {}
  }
  
  private void jButton18ActionPerformed(ActionEvent evt) {
    Setting.settingGoldApple();
  }
  
  private void jButton4ActionPerformed(ActionEvent evt) {
    String itemid = this.jTextField1.getText() + ", " + this.jTextField2.getText();
    this.itemid.add(itemid);
    this.jList3.setListData(this.itemid.toArray(new String[0]));
  }
  
  private void jButton5ActionPerformed(ActionEvent evt) {
    for (String item : this.jList3.getSelectedValuesList())
      this.itemid.remove(item); 
    this.jList3.setListData(this.itemid.toArray(new String[0]));
  }
  
  private void jButton6ActionPerformed(ActionEvent evt) {
    for (String name : this.jList1.getSelectedValuesList()) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
          if (name != null && player.getName() != null && name.equals(player.getName()))
            for (String item : this.itemid) {
              int realitemid = Integer.parseInt(item.split(",")[0]);
              int count = Integer.parseInt(item.split(",")[1].replace(" ", ""));
              player.gainCabinetItem(realitemid, count);
            }  
        } 
      } 
      for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
        if (name != null && csplayer.getName() != null && name.equals(csplayer.getName()))
          for (String item : this.itemid) {
            int realitemid = Integer.parseInt(item.split(",")[0]);
            int count = Integer.parseInt(item.split(",")[1].replace(" ", ""));
            csplayer.gainCabinetItem(realitemid, count);
          }  
      } 
      for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
        if (name != null && csplayer.getName() != null && name.equals(csplayer.getName()))
          for (String item : this.itemid) {
            int realitemid = Integer.parseInt(item.split(",")[0]);
            int count = Integer.parseInt(item.split(",")[1].replace(" ", ""));
            csplayer.gainCabinetItem(realitemid, count);
          }  
      } 
      for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
        if (name != null && csplayer.getName() != null && name.equals(csplayer.getName()))
          for (String item : this.itemid) {
            int realitemid = Integer.parseInt(item.split(",")[0]);
            int count = Integer.parseInt(item.split(",")[1].replace(" ", ""));
            csplayer.gainCabinetItem(realitemid, count);
          }  
      } 
    } 
  }
  
  private void jButton7ActionPerformed(ActionEvent evt) {
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter hp : cserv.getPlayerStorage().getAllCharacters().values()) {
        hp.dropMessage(1, "[HOT] Happy !  핫타임 - 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
        hp.dropMessage(6, "[HOT] Happy !  핫타임 - 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
        hp.gainCabinetItem(2630442, 1);
      } 
    } 
    for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values())
      csplayer.gainCabinetItem(2431156, 1);
    for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values())
      csplayer.gainCabinetItem(2431156, 1); 
    for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values())
      csplayer.gainCabinetItem(2431156, 1); 
  }
  
  private void jButton8ActionPerformed(ActionEvent evt) {
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter hp : cserv.getPlayerStorage().getAllCharacters().values()) {
        hp.dropMessage(1, "[HOT] 프레스티지 핫타임 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
        hp.dropMessage(6, "[HOT] 프레스티지 핫타임 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
        hp.gainItem(2435885, 1);
      } 
    } 
    for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values())
      csplayer.gainCabinetItem(2435885, 1); 
    for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values())
      csplayer.gainCabinetItem(2435885, 1); 
    for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values())
      csplayer.gainCabinetItem(2435885, 1); 
  }
  
  private void jButton11ActionPerformed(ActionEvent evt) {
    String msg = "";
    for (ChannelServer ch : ChannelServer.getAllInstances()) {
      if (ch.얼리기()) {
        ch.얼리기(false);
        if (msg == "")
          msg = "얼리기"; 
        continue;
      } 
      ch.얼리기(true);
      if (msg == "")
        msg = "녹이기"; 
    } 
    this.jButton11.setLabel(msg);
  }
  
  private void jCheckBox1ActionPerformed(ActionEvent evt) {}
  
  private void jButton9ActionPerformed(ActionEvent evt) {
    StringReader sr = new StringReader(this.jTextPane1.getText());
    StringReader sr2 = new StringReader(this.jTextPane1.getText());
    BufferedReader check = new BufferedReader(sr);
    BufferedReader reader = new BufferedReader(sr2);
    String text = null;
    try {
      text = StringFilter(check, reader);
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write(HexTool.getByteArrayFromHexString(text));
    byte[] packet = mplew.getPacket();
    for (String charname : getSelectedName()) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
          if (charname.equals(player.getName()))
            player.getClient().send(packet); 
        } 
      } 
    } 
    for (String charname : getSelectedName()) {
      for (MapleCharacter player : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
        if (charname.equals(player.getName()))
          player.getClient().send(packet); 
      } 
    } 
    for (String charname : getSelectedName()) {
      for (MapleCharacter player : FarmServer.getPlayerStorage().getAllCharacters().values()) {
        if (charname.equals(player.getName()) && 
          player != null)
          player.getClient().send(packet); 
      } 
    } 
  }
  
  private void jButton12ActionPerformed(ActionEvent evt) {
    SkillFactory.reload();
  }
  
  private void jButton13ActionPerformed(ActionEvent evt) {
    ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
  }
  
  private void jButton14ActionPerformed(ActionEvent evt) {
    ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
  }
  
  private void jButton16ActionPerformed(ActionEvent evt) {}
  
  private void jButton17ActionPerformed(ActionEvent evt) {
    SendPacketOpcode.reloadValues();
    RecvPacketOpcode.reloadValues();
    System.out.println("옵코드 리셋 완료");
  }
  
  private void jButton15ActionPerformed(ActionEvent evt) {
    if (this.jTextArea1.getText() != null)
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values())
          player.getClient().send(CField.ImageTalkNpc(9001048, 5000, this.jTextArea1.getText())); 
      }  
  }
  
  private void jButton1ActionPerformed(ActionEvent evt) {
    this.names.clear();
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values())
        this.names.add(player.getName()); 
    } 
    for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values())
      this.names.add(csplayer.getName()); 
    for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values())
      this.names.add(csplayer.getName()); 
    for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values())
      this.names.add(csplayer.getName()); 
    this.jList1.setListData(this.names.toArray(new String[0]));
    this.jLabel2.setText("동접 : " + this.names.size());
  }
  
  private void jButton2ActionPerformed(ActionEvent evt) {
    for (String name : getSelectedName()) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
          if (player != null && player.getName().contains(name)) {
            player.getWorldGMMsg(player, "이 접속이 끊김");
            player.getClient().getChannelServer().getPlayerStorage().deregisterPendingPlayer(player.getId());
            player.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(player);
            player.getClient().disconnect(player, true, false, true);
            player.getClient().getSession().close();
          } 
        } 
      } 
      for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
        if (csplayer != null && csplayer.getName() != null && 
          csplayer.getName().equals(name)) {
          csplayer.getWorldGMMsg(csplayer, "캐시샵에서 접속이 끊김");
          csplayer.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(csplayer);
          csplayer.getClient().disconnect(csplayer, true, true, false);
          csplayer.getClient().getSession().close();
        } 
      } 
      for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
        if (csplayer != null && csplayer.getName() != null && 
          csplayer.getName().equals(name)) {
          csplayer.getWorldGMMsg(csplayer, "경매장에서 접속이 끊김");
          csplayer.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(csplayer);
          csplayer.getClient().disconnect(csplayer, true, true, false);
          csplayer.getClient().getSession().close();
        } 
      } 
      for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
        if (csplayer != null && csplayer.getName() != null && 
          csplayer.getName().equals(name)) {
          csplayer.getWorldGMMsg(csplayer, "농장에서 접속이 끊김");
          csplayer.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(csplayer);
          csplayer.getClient().disconnect(csplayer, true, true, false);
          csplayer.getClient().getSession().close();
        } 
      } 
    } 
  }
  
  private void jButton3ActionPerformed(ActionEvent evt) {
    for (String charname : getSelectedName()) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
          if (charname.equals(player.getName()))
            for (MapleCoolDownValueHolder m : player.getCooldowns()) {
              int skil = m.skillId;
              player.removeCooldown(skil);
              player.getClient().send(CField.skillCooldown(skil, 0));
            }  
        } 
      } 
    } 
  }
  
  private void jButton10ActionPerformed(ActionEvent evt) {
    int index = jTabbedPane1.getSelectedIndex();
    getChattingList(index).removeAllElements();
    getChatting(index).setModel(getChattingList(index));
  }
  
  private void jTextField1ActionPerformed(ActionEvent evt) {}
  
  private void jButton19ActionPerformed(ActionEvent evt) {
    Setting.settingNeoPos();
  }
  
  private void 리붓시간ActionPerformed(ActionEvent evt) {}
  
  private void 리붓ActionPerformed(ActionEvent evt) {
    if (this.리붓시간 == null) {
      JOptionPane.showMessageDialog(null, "좌측에서 쪽지 내용을 작성한 후 눌러주세요.");
      return;
    } 
    if (!this.후원포인트체크여부1.isSelected() && !this.후원포인트체크여부2.isSelected()) {
      JOptionPane.showMessageDialog(null, "점검 또는 패치를 선택하여 주세요.");
      return;
    } 
    if (this.후원포인트체크여부1.isSelected() && this.후원포인트체크여부2.isSelected()) {
      JOptionPane.showMessageDialog(null, "한 가지만 선택하여 주세요.");
      return;
    } 
    try {
      ServerConstants.reboottime = Integer.parseInt(this.리붓시간.getText());
      KoreaCalendar kc = new KoreaCalendar();
      int hour = kc.getHour();
      int min = kc.getMin() + ServerConstants.reboottime;
      if (min >= 60) {
        hour++;
        min -= 60;
        if (hour >= 24)
          hour = 0; 
      } 
      String am = (hour >= 12) ? "오후" : "오전";
      String type = this.후원포인트체크여부2.isSelected() ? "패치가" : "점검이";
      if (ServerConstants.ts == null && (ServerConstants.t == null || !ServerConstants.t.isAlive())) {
        for (ChannelServer cserv : ChannelServer.getAllInstances())
          cserv.setServerMessage("안녕하세요, Happy !  입니다. 잠시 후 " + am + " " + hour + "시 " + min + "분 부터 서버" + type + " 진행됩니다. 원활한 진행을 위해 지금 바로 접속을 종료해주시기 바랍니다. 이용에 불편을 끼쳐 드려 죄송합니다."); 
        AuctionServer.saveItems();
        ServerConstants.t = new Thread(ShutdownServer.getInstance());
        ServerConstants.ts = Timer.EventTimer.getInstance().register(new Runnable() {
              public void run() {
                if (ServerConstants.reboottime == 0) {
                  ShutdownServer.getInstance().shutdown();
                  ServerConstants.t.start();
                  ServerConstants.ts.cancel(false);
                  return;
                } 
                ServerConstants.reboottime--;
              }
            },  60000L);
      } else {
        JOptionPane.showMessageDialog(null, "이미 리붓 시간이 지정되어 있습니다.\r\n남은 리붓 시간 : " + ServerConstants.reboottime + "분");
      } 
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "리붓 시간을 숫자로 입력해주세요.");
    } 
  }
  
  private void 쪽지닉네임입력ActionPerformed(ActionEvent evt) {}
  
  private void 쪽지내용ActionPerformed(ActionEvent evt) {}
  
  private void 쪽지보내기ActionPerformed(ActionEvent evt) {
    if (this.쪽지닉네임입력 == null) {
      JOptionPane.showMessageDialog(null, "좌측에서 유저 이름을 작성한 후 눌러주세요.");
      return;
    } 
    if (this.쪽지내용 == null) {
      JOptionPane.showMessageDialog(null, "좌측에서 쪽지 내용을 작성한 후 눌러주세요.");
      return;
    } 
    MapleCharacterUtil.sendNote(this.쪽지닉네임입력.getText(), "메이플GM", "안녕하세요. Happy !  입니다. " + this.쪽지내용.getText() + "", 0, 6, 0);
    JOptionPane.showMessageDialog(null, this.쪽지닉네임입력.getText() + "님에게 쪽지를 보냈습니다.\r\n      [쪽지내용]\r\n\r\n안녕하세요. Happy !  입니다, " + this.쪽지내용.getText() + "");
  }
  
  private void 닉네임입력ActionPerformed(ActionEvent evt) {}
  
  private void 포인트입력ActionPerformed(ActionEvent evt) {}
  
  private void 포인트지급ActionPerformed(ActionEvent evt) {
    StringBuilder sb = new StringBuilder();
    boolean gived = false;
    if (this.후원포인트체크여부.isSelected() && this.홍보포인트체크여부.isSelected()) {
      JOptionPane.showMessageDialog(null, "포인트는 한종류만 지급이 가능합니다.");
      return;
    } 
    if (!this.후원포인트체크여부.isSelected() && !this.홍보포인트체크여부.isSelected()) {
      JOptionPane.showMessageDialog(null, "지급 할 포인트 종류를 체크하여 주세요.");
      return;
    } 
    String pointa = this.홍보포인트체크여부.isSelected() ? "홍보" : "후원";
    String pointas = this.홍보포인트체크여부.isSelected() ? "홍보" : "후원";
    boolean ingame = false;
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
        if (player != null && player.getName().contains(this.닉네임입력.getText())) {
          ingame = true;
          player.dropMessage(1, "포인트 지급을 위해 접속을 종료하여 주세요.\r\n캐릭터 선택창에서 3분 대기 후 접속하시면 됩니다.");
        } 
      } 
    } 
    for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
      if (csplayer != null && csplayer.getName() != null && 
        csplayer.getName().equals(this.닉네임입력.getText())) {
        ingame = true;
        csplayer.dropMessage(1, "포인트 지급을 위해 접속을 종료하여 주세요.\r\n캐릭터 선택창에서 3분 대기 후 접속하시면 됩니다.");
        break;
      } 
    } 
    for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
      if (csplayer != null && csplayer.getName() != null && 
        csplayer.getName().equals(this.닉네임입력.getText())) {
        ingame = true;
        csplayer.dropMessage(1, "포인트 지급을 위해 접속을 종료하여 주세요.\r\n캐릭터 선택창에서 3분 대기 후 접속하시면 됩니다.");
        break;
      } 
    } 
    for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
      if (csplayer != null && csplayer.getName() != null && 
        csplayer.getName().equals(this.닉네임입력.getText())) {
        ingame = true;
        csplayer.dropMessage(1, "포인트 지급을 위해 접속을 종료하여 주세요.\r\n캐릭터 선택창에서 3분 대기 후 접속하시면 됩니다.");
        break;
      } 
    } 
    if (ingame) {
      JOptionPane.showMessageDialog(null, this.닉네임입력.getText() + "님이 접속중 입니다.");
      return;
    } 
    Connection con = null;
    PreparedStatement ps = null, ps1 = null;
    ResultSet rs = null, rs1 = null;
    try {
      con = DatabaseConnection.getConnection();
      int accountid = 0, point = 0;
      ps = con.prepareStatement("SELECT * FROM characters WHERE name = ?");
      ps.setString(1, this.닉네임입력.getText());
      rs = ps.executeQuery();
      if (rs.next()) {
        accountid = rs.getInt("accountid");
      } else {
        JOptionPane.showMessageDialog(null, "해당 유저를 찾을 수 없습니다.");
        return;
      } 
      ps1 = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
      ps1.setInt(1, accountid);
      rs1 = ps1.executeQuery();
      String pointa2 = this.후원포인트체크여부.isSelected() ? "nxcredit" : "mPoints";
      if (rs1.next()) {
        point = rs1.getInt(pointa2);
        gived = true;
      } else {
        JOptionPane.showMessageDialog(null, "알 수 없는 오류가 발생 하였습니다.");
        return;
      } 
      if (gived) {
        ps = con.prepareStatement("UPDATE accounts SET " + pointa2 + " = ? WHERE id = ?");
        ps.setInt(1, point + Integer.parseInt(this.포인트입력.getText()));
        ps.setInt(2, accountid);
        ps.executeUpdate();
      } 
      ps.close();
      rs.close();
      ps1.close();
      rs1.close();
      con.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (ps1 != null)
          ps1.close(); 
        if (rs != null)
          rs.close(); 
        if (rs1 != null)
          rs1.close(); 
      } catch (Exception exception) {}
    } 
    MapleCharacterUtil.sendNote(this.닉네임입력.getText(), "메이플GM", "" + this.닉네임입력.getText() + "용사님! 용사님의 소중한 " + pointas + " 감사드립니다, 용사님의 기대치에 부응하도록 많은 노력을 하겠습니다. 용사님에게 " + pointa + " 포인트 " + this.포인트입력.getText() + "포인트를 지급 해드렸습니다. 포인트 확인을 해주시길 바랍니다.", 0, 6, 0);
    if (!gived) {
      JOptionPane.showMessageDialog(null, "좌측에서 유저 이름을 작성한 후 지급하여 주세요.");
    } else {
      JOptionPane.showMessageDialog(null, this.닉네임입력.getText() + "님에게 " + pointa + " 포인트 " + Integer.parseInt(this.포인트입력.getText()) + "만큼 지급을 완료 하였습니다. (오프라인 지급)");
    } 
  }
  
  private void 후원포인트체크여부ActionPerformed(ActionEvent evt) {}
  
  private void GM설정ActionPerformed(ActionEvent evt) {
    for (String name : this.jList1.getSelectedValuesList()) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
          if (name != null && player.getName() != null && name.equals(player.getName())) {
            player.setGMLevel((byte)(player.isGM() ? 0 : 6));
            player.dropMessage(-1, player.isGM() ? "[알림] GM설정 되었습니다." : "[알림] GM해제 되었습니다.");
          } 
        } 
      } 
      for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
        if (name != null && csplayer.getName() != null && name.equals(csplayer.getName()))
          csplayer.setGMLevel((byte)(csplayer.isGM() ? 0 : 6)); 
      } 
      for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
        if (name != null && csplayer.getName() != null && name.equals(csplayer.getName()))
          csplayer.setGMLevel((byte)(csplayer.isGM() ? 0 : 6)); 
      } 
      for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
        if (name != null && csplayer.getName() != null && name.equals(csplayer.getName()))
          csplayer.setGMLevel((byte)(csplayer.isGM() ? 0 : 6)); 
      } 
    } 
  }
  
  private void 현접해제1ActionPerformed(ActionEvent evt) {}
  
  private void 현접해제ActionPerformed(ActionEvent evt) {
    String name = this.현접해제1.getText();
    boolean disconnect = false;
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
        if (player != null && player.getName().contains(name)) {
          disconnect = true;
          player.getClient().getChannelServer().getPlayerStorage().deregisterPendingPlayer(player.getId());
          player.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(player);
          player.getClient().disconnect(player, true, false, true);
          player.getClient().getSession().close();
        } 
      } 
    } 
    for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
      if (csplayer != null && csplayer.getName() != null && 
        csplayer.getName().equals(name)) {
        disconnect = true;
        CashShopServer.getPlayerStorage().deregisterPlayer(csplayer);
        csplayer.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(csplayer);
        csplayer.getClient().disconnect(csplayer, true, true, false);
        csplayer.getClient().getSession().close();
      } 
    } 
    for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
      if (csplayer != null && csplayer.getName() != null && 
        csplayer.getName().equals(name)) {
        disconnect = true;
        AuctionServer.getPlayerStorage().deregisterPlayer(csplayer);
        csplayer.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(csplayer);
        csplayer.getClient().disconnect(csplayer, true, true, false);
        csplayer.getClient().getSession().close();
      } 
    } 
    for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
      if (csplayer != null && csplayer.getName() != null && 
        csplayer.getName().equals(name)) {
        disconnect = true;
        FarmServer.getPlayerStorage().deregisterPlayer(csplayer);
        csplayer.getClient().getChannelServer().getPlayerStorage().deregisterPlayer(csplayer);
        csplayer.getClient().disconnect(csplayer, true, true, false);
        csplayer.getClient().getSession().close();
      } 
    } 
    if (!disconnect) {
      Connection con = null;
      PreparedStatement ps = null, ps1 = null;
      ResultSet rs = null, rs1 = null;
      try {
        con = DatabaseConnection.getConnection();
        int accountid = 0;
        ps = con.prepareStatement("SELECT * FROM characters WHERE name = ?");
        ps.setString(1, this.현접해제1.getText());
        rs = ps.executeQuery();
        if (rs.next()) {
          accountid = rs.getInt("accountid");
          disconnect = true;
        } else {
          JOptionPane.showMessageDialog(null, "해당 유저를 찾을 수 없습니다.");
          return;
        } 
        ps = con.prepareStatement("UPDATE accounts SET loggedin = ? WHERE id = ?");
        ps.setInt(1, 0);
        ps.setInt(2, accountid);
        ps.executeUpdate();
        ps.close();
        rs.close();
        con.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
      } finally {
        try {
          if (con != null)
            con.close(); 
          if (ps != null)
            ps.close(); 
          if (ps1 != null)
            ps1.close(); 
          if (rs != null)
            rs.close(); 
          if (rs1 != null)
            rs1.close(); 
        } catch (Exception exception) {}
      } 
    } 
    if (disconnect) {
      JOptionPane.showMessageDialog(null, name + "님 현접해제 완료.");
    } else {
      JOptionPane.showMessageDialog(null, name + "유저를 찾을수 없습니다.");
    } 
  }
  
  public static void main(String[] args) {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        } 
      } 
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(AdminTool.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(AdminTool.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(AdminTool.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      Logger.getLogger(AdminTool.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    EventQueue.invokeLater(new Runnable() {
          public void run() {
            (new AdminTool()).setVisible(true);
          }
        });
  }
}
