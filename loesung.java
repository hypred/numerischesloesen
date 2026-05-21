import javax.swing.JOptionPane;

public class loesung {
    public static void main(String[] args) {
        double a = Double.parseDouble(JOptionPane.showInputDialog("gebe a von f(x) = a*x + b  ein"));
        double b = Double.parseDouble(JOptionPane.showInputDialog("gebe b von f(x) = a*x + b  ein"));
        double c = Double.parseDouble(JOptionPane.showInputDialog("gebe a von g(x) = a*b^x  ein"));
        double d = Double.parseDouble(JOptionPane.showInputDialog("gebe b von g(x) = a*b^x  ein"));

        double[] interval = hVonX(a, b, c, d);
        if (interval != null) {
            String msg = "Intervall: [" + interval[0] + ", " + interval[1] + "]";
            JOptionPane.showMessageDialog(null, msg);
            System.out.println(msg);

            // refine with bisection
            Double root = bisectRoot(a, b, c, d, interval[0], interval[1], 1e-12, 1000);
            if (root != null) {
                String rmsg = "Gefundene Nullstelle (annähernd): " + root;
                JOptionPane.showMessageDialog(null, rmsg);
                System.out.println(rmsg);
            } else {
                System.out.println("Bisektion konnte nicht durchgeführt werden (kein gültiges Intervall)");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Kein Vorzeichenwechsel gefunden (Limit erreicht)");
            System.out.println("Kein Vorzeichenwechsel gefunden (Limit erreicht)");
        }
    }

    public static double[] hVonX(double a, double b, double c, double d) {
        double x = 0.0;
        double vorher = fVonX(a, b, x) - gVonX(c, d, x);

        int maxIter = 1000000;
        for (int i = 0; i < maxIter; i++) {
            double naechstX = nextX(x); // advance x (here +1)
            double aktuell = fVonX(a, b, naechstX) - gVonX(c, d, naechstX);

            if (vorher * aktuell < 0) {
                return new double[] { x, naechstX };
            }

            x = naechstX;
            vorher = aktuell;
        }

        return null; // not found within maxIter
    }

    public static double fVonX(double a, double b, double x) {
        return a * x + b;
    }

    public static double gVonX(double c, double d, double x) {
        return c * Math.pow(d, x);
    }

    // midpoint between left and right
    public static double mvalue(double left, double right) {
        return (left + right) / 2.0;
    }

    // advance x by one (used for initial interval search)
    public static double nextX(double x) {
        return x + 1.0;
    }

    // Bisect to refine root inside [left,right]. Returns null if invalid interval.
    public static Double bisectRoot(double a, double b, double c, double d, double left, double right, double tol, int maxIter) {
        double fl = fVonX(a, b, left) - gVonX(c, d, left);
        double fr = fVonX(a, b, right) - gVonX(c, d, right);
        if (Math.abs(fl) <= tol) return left;
        if (Math.abs(fr) <= tol) return right;
        if (fl * fr > 0) return null; // no sign change

        double l = left;
        double r = right;
        double fm = Double.NaN;
        for (int i = 0; i < maxIter; i++) {
            double m = mvalue(l, r);
            fm = fVonX(a, b, m) - gVonX(c, d, m);
            if (Math.abs(fm) <= tol) return m;
            double flocal = fVonX(a, b, l) - gVonX(c, d, l);
            if (flocal * fm < 0) {
                r = m;
            } else {
                l = m;
            }
        }
        return mvalue(l, r);
    }
}