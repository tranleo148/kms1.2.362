/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author koong
 */
public enum MapleDonationSkill { // 가
    HOLY_SYMBOL(0x01, 2311003),
    WIND_BOOSTER(0x02, 5121009),
    SHARP_EYES(0x04, 3121002),
    CROSS_OVER_CHAIN(0x08, 1311015),
    MAGIC_GUARD(0x10, 2001002),
    BUCKSHOT(0x20, 5321054),
    TRIPLING_WIM(0x40, 13100022),
    FINAL_CUT(0x80, 4341002);
    //  QUIVER_KARTRIGE(0x80, 3101009);

    private final int i, j;

    private MapleDonationSkill(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getValue() {
        return i;
    }

    public int getSkillId() {
        return j;
    }

    public static final MapleDonationSkill getByValue(final int value) {
        for (final MapleDonationSkill stat : MapleDonationSkill.values()) {
            if (stat.i == value) {
                return stat;
            }
        }
        return null;
    }

    public static final MapleDonationSkill getBySkillId(final int value) {
        for (final MapleDonationSkill stat : MapleDonationSkill.values()) {
            if (stat.j == value) {
                return stat;
            }
        }
        return null;
    }
}
