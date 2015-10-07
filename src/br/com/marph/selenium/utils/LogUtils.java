package br.com.marph.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.maph.selenium.enums.EnumMensagens;
import br.com.maph.selenium.enums.TipoMensagem;

public class LogUtils {
	public static void log(EnumMensagens mensagem, Class<?> clazz) {		
		Logger log = LogManager.getLogger(clazz.getSimpleName());
		
		//if(EnumMensagens.INICIO.equals(mensagem)){
		//	log.info( "INICIO DO TESTE: " + clazz.getSimpleName());
		//}else 
			if(TipoMensagem.INFO.equals(mensagem.getTipoMensagem())){
			log.info(mensagem.getMensagem());
		}else if(TipoMensagem.DEBUG.equals(mensagem.getTipoMensagem())){
			log.debug(mensagem.getMensagem());
		}else{
			log.error(mensagem.getMensagem());
		}
	}
}
