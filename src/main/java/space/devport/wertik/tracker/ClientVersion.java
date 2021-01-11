package space.devport.wertik.tracker;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum ClientVersion {

    UNKNOWN(Integer.MAX_VALUE, "Unknown"),
    v1_7(3, 5, "1.7-1.7.10"),
    v1_8(44, 47, "1.8-1.8.9"),
    v1_9(107),
    v1_9_1(108),
    v1_9_2(109),
    v1_9_3(110),
    v1_9_4(110),
    v1_10(204, 210, "1.10-1.10.2"),
    v1_11(314, 315),
    v1_11_2(316, "1.11.1-1.11.2"),
    v1_12(328, 335),
    v1_12_1(337, 338),
    v1_12_2(339, 340),
    v1_13(383, 393),
    v1_13_1(399, 401),
    v1_13_2(402, 404),
    v1_14(472, 477),
    v1_14_1(478, 480),
    v1_14_2(481, 485),
    v1_14_3(486, 490),
    v1_14_4(491, 498),
    v1_15(565, 573),
    v1_15_1(574, 575),
    v1_15_2(576, 578),
    v1_16(721, 735),
    v1_16_1(736),
    v1_16_2(737, 751),
    v1_16_3(752, 753),
    v1_16_4(754),
    v1_17(755, Integer.MAX_VALUE);

    private final int min;
    private final int max;

    @Getter
    private final String name;

    ClientVersion(int min, int max, String name) {
        this.min = min;
        this.max = max;
        this.name = name;
    }

    ClientVersion(int min, int max) {
        this.min = min;
        this.max = max;
        this.name = craftName();
    }

    ClientVersion(int min) {
        this.min = this.max = min;
        this.name = craftName();
    }

    ClientVersion(int min, String name) {
        this.min = this.max = min;
        this.name = name;
    }

    private String craftName() {
        return this.name().replace("_", ".").replace("v", "");
    }

    @NotNull
    public static ClientVersion fromProtocol(int protocolVersion) {
        for (ClientVersion version : values()) {
            if (protocolVersion >= version.min && protocolVersion <= version.max)
                return version;
        }
        return ClientVersion.UNKNOWN;
    }
}
