// 
// Decompiled by Procyon v0.5.36
// 

package handling.channel.handler;

import client.SkillFactory;
import constants.GameConstants;
import server.SecondaryStatEffect;
import client.Skill;
import client.MapleCharacter;
import java.util.ArrayList;
import tools.Pair;
import java.awt.Rectangle;
import java.awt.Point;
import tools.AttackPair;
import java.util.List;

public class AttackInfo
{
    public int skill;
    public int charge;
    public int lastAttackTickCount;
    public List<AttackPair> allDamage;
    public Point position;
    public Point chain;
    public Point plusPosition;
    public Point plusPosition2;
    public Point plusPosition3;
    public Point attackPosition;
    public Point attackPosition2;
    public Point attackPosition3;
    public Rectangle acrossPosition;
    public int display;
    public int facingleft;
    public int count;
    public int subAttackType;
    public int subAttackUnk;
    public byte hits;
    public byte targets;
    public byte tbyte;
    public byte speed;
    public byte animation;
    public byte plusPos;
    public short AOE;
    public short slot;
    public short csstar;
    public boolean real;
    public boolean across;
    public boolean Aiming;
    public byte attacktype;
    public boolean isLink;
    public byte isBuckShot;
    public byte isShadowPartner;
    public byte nMoveAction;
    public byte rlType;
    public byte bShowFixedDamage;
    public int item;
    public int skilllevel;
    public int asist;
    public int summonattack;
    public List<Point> mistPoints;
    public List<Pair<Integer, Integer>> attackObjects;
    
    public AttackInfo() {
        this.charge = 0;
        this.plusPosition = new Point();
        this.display = 0;
        this.facingleft = 0;
        this.count = 0;
        this.tbyte = 0;
        this.speed = 0;
        this.real = true;
        this.across = false;
        this.Aiming = false;
        this.attacktype = 0;
        this.isLink = false;
        this.isBuckShot = 0;
        this.isShadowPartner = 0;
        this.nMoveAction = -1;
        this.bShowFixedDamage = 0;
        this.skilllevel = 0;
        this.mistPoints = new ArrayList<Point>();
        this.attackObjects = new ArrayList<Pair<Integer, Integer>>();
    }
    
    public final SecondaryStatEffect getAttackEffect(final MapleCharacter chr, final int skillLevel, final Skill skill_) {
        if (GameConstants.isLinkedSkill(this.skill)) {
            final Skill skillLink = SkillFactory.getSkill(GameConstants.getLinkedSkill(this.skill));
            return skillLink.getEffect(skillLevel);
        }
        return skill_.getEffect(skillLevel);
    }
}
