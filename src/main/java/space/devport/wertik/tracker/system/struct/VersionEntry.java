package space.devport.wertik.tracker.system.struct;

import lombok.Getter;
import space.devport.wertik.tracker.ClientVersion;

public class VersionEntry extends TrackEntry {

    @Getter
    private final ClientVersion version;

    public VersionEntry(ClientVersion version) {
        this.version = version;
    }
}
