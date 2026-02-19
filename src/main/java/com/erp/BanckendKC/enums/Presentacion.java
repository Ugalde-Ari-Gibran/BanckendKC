package com.erp.BanckendKC.enums;

public enum Presentacion {
    KG,
    MEDIO_KG,
    CUARTO_KG;

    public double toKilos() {
        return switch (this) {
            case KG -> 1.0;
            case MEDIO_KG -> 0.5;
            case CUARTO_KG -> 0.25;
        };
    }
}
