package com.koscom.marketdata.bot.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;

public class CapitalCommand extends DefaultCommand {
	private static Log logger = LogFactory.getLog(CapitalCommand.class);

	public CapitalCommand(String command) {
		super(command);
	}

	@Override
	public JsonObject handle(JsonObject command) {
		if(!canHandle(command))
			throw new IllegalArgumentException("illegal command");
		
		// TODO [실습 4-01] 입력 종목 기업의 자본금 정보를 가져오는 CapitalCommand를 작성하여 추가한다.

		/*
		  hint
		  
		   - bot_config.xml에 기업정보를 위한 endpoint정보와 apikey를 설정한다.
		   
		   - 해당 endpoint로 Wavelet의 자본금 정보 api를 호출하여 데이터를 처리한다.
		   
		   - 종목코드를 찾는 방법은 PriceCommand 클래스를 참조
		   
		 */
		
		return null;
	}
}
