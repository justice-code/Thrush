package org.eddy.solve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Justice-love on 2017/7/24.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThrushResult {

    private String key;
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThrushResult that = (ThrushResult) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
