package com.frederiksen.formidable.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utils {
    public static boolean LOG_ENABLE = true;
    public static int LOG_LEVEL = 1;

    public static String readFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            String lines = "";
            while (scanner.hasNextLine()) {
                lines += scanner.nextLine() + "\n";
            }
            return lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static void log(String msg) {
        if (LOG_ENABLE) {
            System.out.println(msg);
        }
    }

    public static void log(String msg, int level) {
        if (level >= LOG_LEVEL) {
            System.out.println(msg);
        }
    }

    public static float rangeIntersect(float aMin, float aMax, float bMin, float bMax) {
        if (aMin < bMin) {
            if (aMax > bMax) {
                // b is completely within a
                return bMax - bMin;
            } else {
                // a is to the left of b
                return aMax - bMin;
            }
        } else {
            if (bMax > aMax) {
                // a is completely within b
                return aMax - aMin;
            } else {
                // a is to the right of b
                return bMax - aMin;
            }
        }
    }
}
