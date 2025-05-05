package com.mytask.MyTask.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Implementação do padrão Singleton (sistema de log centralizado).
public class LoggerManager {

	private static LoggerManager instance;
	private final Logger logger;

	// Construtor privado para evitar instanciação externa.
	private LoggerManager() {
		this.logger = LoggerFactory.getLogger(LoggerManager.class);
	}

	// Método para obter a instância única (thread-safe).
	public static synchronized LoggerManager getInstance() {
		if (instance == null) {
			instance = new LoggerManager();
		}
		return instance;
	}

	// Registra uma mensagem.
	public void info(String message) {
		logger.info(message);
	}

	// Registra uma mensagem de erro com a exceção associada.
	public void error(String message, Throwable throwable) {
		logger.error(message, throwable);
	}

	// Registra uma mensagem de aviso.
	public void warn(String message) {
		logger.warn(message);
	}

	// Registra uma mensagem com diagnóstico detalhado.
	public void debug(String message) {
		logger.debug(message);
	}

}