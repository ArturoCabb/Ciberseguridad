package com.example.myapplication;

import java.lang.reflect.GenericArrayType;

public class DiffieHelman {
    public DiffieHelman() {

    }

    public long DiffieHelmanClient(long G, long a, long P) {
        // Generar llave del cliente @param x
        return calculatePower(G, a, P);
    }
    // Create calculatePower() method
    private long calculatePower(long x, long y, long P) {
        return (y == 1)? x : ((long)Math.pow(x, y)) % P;
    }
}
