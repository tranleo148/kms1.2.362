/*
 * Decompiled with CFR 0.150.
 */
package handling;

import handling.WritableIntValueHolder;
import java.util.Properties;

public class ExternalCodeTableGetter {
    final Properties props;

    public ExternalCodeTableGetter(Properties properties) {
        this.props = properties;
    }

    private static final <T extends Enum<? extends WritableIntValueHolder>> T valueOf(String name, T[] values) {
        for (T val : values) {
            if (!((Enum)val).name().equals(name)) continue;
            return val;
        }
        return null;
    }

    private final <T extends Enum<? extends WritableIntValueHolder>> short getValue(String name, T[] values, short def) {
        String prop = this.props.getProperty(name);
        if (prop != null && prop.length() > 0) {
            String offset;
            String trimmed = prop.trim();
            String[] args = trimmed.split(" ");
            short base = 0;
            if (args.length == 2) {
                base = ((WritableIntValueHolder)((Object)ExternalCodeTableGetter.valueOf((String)args[0], values))).getValue();
                if (base == def) {
                    base = this.getValue(args[0], (Enum[])values, def);
                }
                offset = args[1];
            } else {
                offset = args[0];
            }
            if (offset.length() > 2 && offset.substring(0, 2).equals("0x")) {
                return (short)(Short.parseShort(offset.substring(2), 16) + base);
            }
            return (short)(Short.parseShort(offset) + base);
        }
        return def;
    }

    public static final <T extends Enum<? extends WritableIntValueHolder>> void populateValues(Properties properties, T[] values) {
        ExternalCodeTableGetter exc = new ExternalCodeTableGetter(properties);
        for (T code : values) {
            ((WritableIntValueHolder)code).setValue(exc.getValue(((Enum)code).name(), (Enum[])values, (short)-2));
        }
    }
}

