package sk.stuba.fiit.michal.nikolas.cd_shop.model;

/**
 * Created by micha on 26.03.2016.
 */
public enum GenresEnum {
    rock(0,"Rock"),
    pop(1,"PoP"),
    jazz(2,"Jazz"),
    funk(3,"Funk"),
    indie(4,"Indie"),
    hiphop(5,"Hip Hop"),
    rnb(6,"RnB"),
    country(7,"Country"),
    folk(8,"Folk"),
    metal(9,"Metal"),
    soul(10,"Soul"),
    blues(11,"Blues"),
    reggae(12,"Reggae"),
    latino(13,"Latino"),
    punk(14,"Punk"),
    classical(15,"Classical");

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
