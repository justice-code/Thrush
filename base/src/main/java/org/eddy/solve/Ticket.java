package org.eddy.solve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eddy.Assemble;

/**
 * Created by Justice-love on 2017/7/27.
 */
@Getter @Setter @ToString(exclude = {"token"})
public class Ticket {

    // 0
    @Assemble(expression = "param[0]")
    private String token;

    // 3
    @Assemble(expression = "param[3]")
    private String trainNo;

    //11
    @Assemble(expression = "param[11]")
    private String canBuy;

    //商务座 32
    @Assemble(expression = "param[32]")
    private String swz;

    //一等座 31
    @Assemble(expression = "param[31]")
    private String ydz;

    //二等座 30
    @Assemble(expression = "param[30]")
    private String edz;

    //高级软卧
    private String gjrw;

    //软卧 23
    @Assemble(expression = "param[23]")
    private String rw;

    //动卧

    private String dw;

    //硬卧 28
    @Assemble(expression = "param[28]")
    private String yw;

    //软座
    private String rz;

    //硬座 29
    @Assemble(expression = "param[29]")
    private String yz;

    //无座 26
    @Assemble(expression = "param[26]")
    private String wz;

    //其他
    private String qt;
}
