package org.eddy.solve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created by Justice-love on 2017/7/24.
 */
@Getter @Setter
@AllArgsConstructor
public class ResultKey {

    @NonNull
    private String key;

    @NonNull
    private String ognl;
}
