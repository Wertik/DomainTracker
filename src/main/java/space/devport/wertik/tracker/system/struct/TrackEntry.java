package space.devport.wertik.tracker.system.struct;

import lombok.Getter;

abstract class TrackEntry {

    @Getter
    private int unique;
    @Getter
    private transient int online;

    public void incrementOnline() {
        this.online += 1;
    }

    public void decrementOnline() {
        this.online = Math.max(0, online - 1);
    }

    public void incrementUnique() {
        this.unique += 1;
    }
}
