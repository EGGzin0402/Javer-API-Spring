package com.eggzin.javer_service.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreUtils {
	
	public static Float calcScore(Float saldo) {
		return (float) (saldo*0.1);
	}

}
