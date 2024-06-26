package io.github.burukeyou.dataframe.iframe.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaxMin<T> {

    private T max;
    private T min;
}
