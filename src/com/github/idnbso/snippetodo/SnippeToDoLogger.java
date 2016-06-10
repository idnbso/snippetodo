package com.github.idnbso.snippetodo;

import com.github.idnbso.snippetodo.controller.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SnippeToDoLogger contains the SLF4J with log4j logger.
 */
public class SnippeToDoLogger
{
    /**
     * The logger for the SnippeToDo app, using SLF4J with log4j.
     */
    public final static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
}
