package sample;

public class RectangleCreator {
    public static double rWidth(double a, double b) {
        if (a < b) {
            return b - a;
        } else {
            return a - b;
        }
    }

    public static double rHeight(double a, double b) {
        if (a < b) {
            return b - a;
        } else {
            return a - b;
        }
    }

    public static double rX(double a, double b, double center, double width) {
        if (a > b) {
            return a - center / 2 - width / 2;
        } else {
            return (a - center / 2 + width / 2);
        }
    }

    public static double rY(double a, double b, double center, double height) {
        if (a > b) {
            return a - center / 2 - height / 2;
        } else {
            return (a - center / 2  + height / 2);
        }
    }
}
