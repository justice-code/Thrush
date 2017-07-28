package org.eddy.solve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eddy.Assemble;

/**
 * Created by Justice-love on 2017/7/28.
 */
@Getter @Setter @ToString
public class Passenger {

    @Assemble(expression = "param.getString('passenger_name')")
    private String name;

    @Assemble(expression = "param.getString('mobile_no')")
    private String mobile;

    @Assemble(expression = "param.getString('passenger_id_no')")
    private String idNo;

    @Assemble(expression = "param.getString('passenger_id_type_code')")
    private String idType;

    @Assemble(expression = "param.getString('passenger_type')")
    private String passengerType;

}
