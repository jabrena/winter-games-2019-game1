package org.jab.microservices.wintergames1.config;

public enum CloudFoundryProvider {
    PFC (0),
    BLUEMIX (1);

    private int index;

    CloudFoundryProvider(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
