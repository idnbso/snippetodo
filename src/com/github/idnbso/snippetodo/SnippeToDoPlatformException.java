package com.github.idnbso.snippetodo;

/**
 * SnippeToDoPlatformException provides exception handling for any class in the SnippeToDo project
 * platform.
 *
 * @author Idan Busso
 * @author Shani Kahila
 * @see Exception
 */
public class SnippeToDoPlatformException extends Exception
{
    private static final long serialVersionUID = -4595807240297323542L;

    /**
     * Constructs a new exception with the specified detail message and cause. Note that the detail
     * message associated with {@code cause} is not automatically incorporated in this exception's
     * detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link
     *                #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
     *                method). (A null value is permitted, and indicates that the cause is
     *                nonexistent or unknown.)
     */
    public SnippeToDoPlatformException(String message, Throwable cause)
    {
        super(message, cause);
    }
}