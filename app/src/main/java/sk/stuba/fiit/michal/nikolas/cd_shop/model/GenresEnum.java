package sk.stuba.fiit.michal.nikolas.cd_shop.model;

/**
 * Created by micha on 26.03.2016.
 */
public enum GenresEnum {
    rock(0,"Rock"),
    pop(1,"PoP"),
    jazz(2,"Jazz"),
    funky(3,"Funky"),
    rock2(0,"Rock"),
    pop2(1,"PoP"),
    jazz2(2,"Jazz"),
    funky2(3,"Funky"),
    rock3(0,"Rock"),
    pop3(1,"PoP"),
    jazz3(2,"Jazz"),
    funky3(3,"Funky"),
    rock4(0,"Rock"),
    pop4(1,"PoP"),
    jazz4(2,"Jazz"),
    funky4(3,"Funky");

    private final int value;
    private final String stringName;

    private GenresEnum(int value, String string) {
        this.value = value;
        this.stringName = string;
    }

    public static GenresEnum getByValue(int testik) {
        try {
            return GenresEnum.values()[testik];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Unknown enum value :"+ testik);
        }
    }

    public String getStringName() {
        return stringName;
    }

    public int getValue() {
        return value;
    }

    public static String[] getAllNames() {
        GenresEnum[] array = GenresEnum.values();
        String[] out = new String[array.length];
        for (int i=0;i<array.length;i++){
            out[i] = array[i].stringName;
        }
        return out;
    }
}
