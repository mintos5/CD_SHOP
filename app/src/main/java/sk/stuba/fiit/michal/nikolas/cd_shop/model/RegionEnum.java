package sk.stuba.fiit.michal.nikolas.cd_shop.model;

/**
 * Created by Nikolas on 13.4.2016.
 */
public enum RegionEnum {
    argentina(0,"Argentina"),
    australia(1,"Australia"),
    czech(2,"Czech Republic"),
    france(3,"France"),
    greece(4,"Greece"),
    ireland(5,"Ireland"),
    poland(6,"Poland"),
    slovakia(7,"Slovakia"),
    spain(8,"Spain"),
    unitedkingdom(9,"United Kingdom"),
    unitedstates(10,"United States");

    private final int value;
    private final String stringName;

    private RegionEnum(int value, String string) {
        this.value = value;
        this.stringName = string;
    }

    public static RegionEnum getByValue(int test) {
        try {
            return RegionEnum.values()[test];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Unknown enum value :"+ test);
        }
    }

    public String getStringName() {
        return stringName;
    }

    public int getValue() {
        return value;
    }

    public static String[] getAllNames() {
        RegionEnum[] array = RegionEnum.values();
        String[] out = new String[array.length];
        for (int i=0;i<array.length;i++){
            out[i] = array[i].stringName;
        }
        return out;
    }
}
