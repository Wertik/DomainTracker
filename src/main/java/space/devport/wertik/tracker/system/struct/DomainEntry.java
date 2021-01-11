package space.devport.wertik.tracker.system.struct;

import lombok.Getter;

public class DomainEntry extends TrackEntry {

    @Getter
    private final String hostname;

    public DomainEntry(String hostname) {
        this.hostname = hostname;
    }
}
