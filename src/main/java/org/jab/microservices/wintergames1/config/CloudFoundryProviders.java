package org.jab.microservices.wintergames1.config;

public enum CloudFoundryProviders {
    PFC (0),
    BLUEMIX (1);

    private int index;

    CloudFoundryProviders(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
