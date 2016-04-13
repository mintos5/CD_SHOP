package sk.stuba.fiit.michal.nikolas.cd_shop.model;

/**
 * Created by Nikolas on 13.4.2016.
 */
public enum DecadeEnum {
    fiftieth(0,"All Out 50's"),
    sixtieth(1,"All Out 60's"),
    seventieth(2,"All Out 70's"),
    eightieth(3,"All Out 80's"),
    ninetieth(4,"All Out 90's"),
    secondthousand(5,"All Out 00's"),
    secondthousandfirst(6,"All Out 2001-2009"),
    secondthousandtenth(7,"All Out 2010+");

    private final int value;
    private final String stringName;

    private DecadeEnum(int value, String string) {
        this.value = value;
        this.stringName = string;
    }

    public static DecadeEnum getByValue(int test) {
        try {
            return DecadeEnum.values()[test];
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
        DecadeEnum[] array = DecadeEnum.values();
        String[] out = new String[array.length];
        for (int i=0;i<array.length;i++){
            out[i] = array[i].stringName;
        }
        return out;
    }
}
