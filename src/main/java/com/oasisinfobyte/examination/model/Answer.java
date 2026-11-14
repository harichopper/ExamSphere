package com.oasisinfobyte.examination.model;

/**
 * Multiple choice option enumeration: A, B, C, D.
 */
public enum Answer {
    A(0),
    B(1),
    C(2),
    D(3);

    private final int index;

    Answer(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Answer fromIndex(int index) {
        for (Answer ans : values()) {
            if (ans.index == index) {
                return ans;
            }
        }
        return null;
    }
}
