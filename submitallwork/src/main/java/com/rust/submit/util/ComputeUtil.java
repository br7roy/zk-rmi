package com.rust.submit.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rust
 */
public class ComputeUtil {

	public static BigDecimal minus(float a, float b) {

		BigDecimal bg = new BigDecimal(a);
		BigDecimal subtract = bg.subtract(new BigDecimal(b));
		BigDecimal bigDecimal = subtract.setScale(3, RoundingMode.HALF_UP);
		return bigDecimal;
	}


}
