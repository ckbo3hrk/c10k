package io.home.assignment.fibo;

import com.google.common.base.Preconditions;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public final class Fibonacci {
    private Fibonacci() {
    }

    /**
     *  Calculate the Nth fibonacci number
     */
    public static BigInteger getNth(int num) {
        Preconditions.checkArgument(num >= 0, "The input parameter cannot be negative");

        if (num <= 1) {
            return BigInteger.valueOf(num);
        }

        BigInteger n = BigInteger.valueOf(num);

        final BigInteger[][] result = {{ONE, ZERO}, {ZERO, ONE}}; // identity matrix.
        final BigInteger[][] fiboM = {{ONE, ONE}, {ONE, ZERO}};

        while (n.compareTo(BigInteger.ZERO) > 0) {
            if (n.testBit(0)) {
                mulMatrix(result, fiboM);
            }
            n = n.shiftRight(1);
            mulMatrix(fiboM, fiboM);
        }

        return result[1][0];
    }

    /**
     * Calculate sum of n even numbers in fibonacci sequence
     */
    public static BigInteger getNthEvenSum(int n) {
        return getNth(n * 3 + 2).subtract(BigInteger.ONE).shiftRight(1);
    }

    /**
     * Perform matrix multiplication
     */
    private static void mulMatrix(BigInteger[][] m, BigInteger [][] n) {
        BigInteger a = m[0][0].multiply(n[0][0]).add(m[0][1].multiply(n[1][0]));
        BigInteger b = m[0][0].multiply(n[0][1]).add(m[0][1].multiply(n[1][1]));
        BigInteger c = m[1][0].multiply(n[0][0]).add(m[1][1].multiply(n[0][1]));
        BigInteger d = m[1][0].multiply(n[0][1]).add(m[1][1].multiply(n[1][1]));
        m[0][0] = a;
        m[0][1] = b;
        m[1][0] = c;
        m[1][1] = d;
    }
}
