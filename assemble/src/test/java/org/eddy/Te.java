package org.eddy;

/**
 * Created by Justice-love on 2017/7/27.
 */
public class Te {

    @Assemble(expression = "param[0]")
    private String a;

    @Assemble(expression = "param[1]")
    private String b;

    @Assemble(expression = "param[2]")
    private String d;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Te{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", d='" + d + '\'' +
                '}';
    }
}
