package com.hotelmanagement.hotel.model.enums;

public enum RoomType {
    SINGLE(1),
    DOUBLE(2),
    DELUXE(4),
    SUITE(2);

    private final int defaultCapacity;

    RoomType(int defaultCapacity) {
        this.defaultCapacity = defaultCapacity;
    }

    public int getDefaultCapacity() {
        return defaultCapacity;
    }
}
