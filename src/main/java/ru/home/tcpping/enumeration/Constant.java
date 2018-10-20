package ru.home.tcpping.enumeration;

/**
 * Command line arguments related constants.
 */
public enum Constant {
    MPS_DEFAULT(1),
    SIZE_MIN(50),
    SIZE_MAX(3000),
    SIZE_DEFAULT(300);
    int value;
    private Constant(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }
}
