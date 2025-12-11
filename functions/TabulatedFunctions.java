package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {}
    
    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        double eps = Math.ulp(1.0);
        if (leftX + eps < f.getLeftDomainBorder() || rightX - eps > f.getRightDomainBorder()) {
            throw new IllegalArgumentException();
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }

    public static void writeTabulatedFunction(TabulatedFunction f, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.println(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            pw.println(f.getPointX(i) + " " + f.getPointY(i));
        }
        pw.flush();
    }

    public static void outputTabulatedFunction(TabulatedFunction f, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            dos.writeDouble(f.getPointX(i));
            dos.writeDouble(f.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int n = dis.readInt();
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            pts[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(pts);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.resetSyntax();
        st.wordChars('0', '9');
        st.wordChars('-', '-');
        st.wordChars('+', '+');
        st.wordChars('.', '.');
        st.whitespaceChars(0, ' ');
        int t = st.nextToken();
        if (t != StreamTokenizer.TT_WORD) {
            throw new IOException();
        }
        int n = Integer.parseInt(st.sval);
        if (n < 2) {
            throw new IOException();
        }
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            if (st.nextToken() != StreamTokenizer.TT_WORD) throw new IOException();
            double x = Double.parseDouble(st.sval);
            if (st.nextToken() != StreamTokenizer.TT_WORD) throw new IOException();
            double y = Double.parseDouble(st.sval);
            pts[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(pts);
    }
}