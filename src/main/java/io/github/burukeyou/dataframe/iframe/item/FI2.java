package io.github.burukeyou.dataframe.iframe.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DataFrame （2 x 2 矩阵）
 *
 *
 * @param <T1>
 * @param <T2>
 *
 * @author caizhihao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FI2<T1, T2> implements FItem {

    private T1 c1;
    private T2 c2;

}
