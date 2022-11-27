package org.example;


import Jama.Matrix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static final double[] x = {0.847, 1.546, 1.834,	2.647,	2.91};
    private static final double[] y = {-1.104, 1.042, 0.029, -0.344, -0.449};
    private static double[] polynomials = new double[5];
    private static double[] linearSplineA = new double[4];
    private static double[] linearSplineB = new double[4];

    public static void main(String[] args) {

        //Чтение
        double xToTry = 0d;
        double xResultP = 0d;
        double xResultLS = 0d;
        double xResultQS = 0d;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            xToTry = Double.parseDouble(bufferedReader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Полиномы
        int ctr;
        for (int i = 0; i < 5; i++) {
            ctr = 0;
            for (int j = 0; j < 5; j++) {
                if (i != j) {
                    if (ctr == 0 || (ctr == 1 && i == 0)) {
                        polynomials[i] = x[i] - x[j];
                    } else {
                        polynomials[i] *= x[i] - x[j];
                    }
                }
                ctr++;
            }
        }

        for (int i = 0; i < 5; i++) {
            polynomials[i] = y[i] / polynomials[i];
        }

        System.out.println("polynomials:\n" + Arrays.toString(polynomials));

        double temp = 0;
        for (int i = 0; i < 4; i++) {
            temp = polynomials[i];
            for (int j = 0; j < 5; j++) {
                if (i != j) temp *= xToTry - x[j];
            }
            xResultP += temp;
        }

        System.out.println("PolynomialX=\n" + xResultP);


        //Линейный сплайн
        for (int i = 0; i < 4; i++) {
            linearSplineA[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
            linearSplineB[i] = y[i] - linearSplineA[i] * x[i];
        }

        double result = 0d;
        if (xToTry >= 0.847 && xToTry <= 1.546) {
            result = linearSplineA[0] * xToTry + linearSplineB[0];
        } else if (xToTry >= 1.546 && xToTry <= 1.834) {
            result = linearSplineA[1] * xToTry + linearSplineB[1];
        } else if (xToTry >= 1.834 && xToTry <= 2.647) {
            result = linearSplineA[2] * xToTry + linearSplineB[2];
        } else if (xToTry >= 2.647 && xToTry <= 2.91) {
            result = linearSplineA[3] * xToTry + linearSplineB[3];
        }

        System.out.println("Linear Spline result = " + result);


        //Ньютон
        double[] subDivs1 = new double[4];
        double[] subDivs2 = new double[3];
        double[] subDivs3 = new double[2];
        double subDivs4 = 0d;
        for (int i = 0; i < 4; i++) {
            subDivs1[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
        }
        for (int i = 0; i < 3; i++) {
            subDivs2[i] = (subDivs1[i + 1] - subDivs1[i]) / (x[i + 2] - x[i]);
        }
        for (int i = 0; i < 2; i++) {
            subDivs3[i] = (subDivs2[i + 1] - subDivs2[i]) / (x[i + 3] - x[i]);
        }
        subDivs4 = (subDivs3[1] - subDivs3[0]) / (x[4] - x[0]);

        System.out.println("SubDivs1 = \n" + Arrays.toString(subDivs1));
        System.out.println("SubDivs2 = \n" + Arrays.toString(subDivs2));
        System.out.println("SubDivs3 = \n" + Arrays.toString(subDivs3));
        System.out.println("SubDivs4 = \n[" + subDivs4 + "]");

        result = y[0];
        result += subDivs1[0] * (xToTry - x[0]);
        result += subDivs2[0] * (xToTry - x[0]) * (xToTry - x[1]);
        result += subDivs3[0] * (xToTry - x[0]) * (xToTry - x[1]) * (xToTry - x[2]);
        result += subDivs4 * (xToTry - x[0]) * (xToTry - x[1]) * (xToTry - x[2]) * (xToTry - x[3]);

        System.out.println("Newton's = \n" + result);
    }
}