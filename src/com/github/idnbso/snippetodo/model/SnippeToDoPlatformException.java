/**
 * 
 */
package com.github.idnbso.snippetodo.model;

/**
 * SnippeToDoPlatformException provides exception handling for any class in the
 * SnippeToDo project platform.
 * 
 * @author Idan Busso
 * @author Shani Kahila
 * @see Exception
 */
@SuppressWarnings("serial")
public class SnippeToDoPlatformException extends Exception
{
    /**
     * Constructs a new exception with the specified detail message and cause.
     * Note that the detail message associated with {@code cause} is not
     * automatically incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by
     *            the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     */
    public SnippeToDoPlatformException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
