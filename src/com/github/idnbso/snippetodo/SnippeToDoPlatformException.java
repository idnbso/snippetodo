package com.github.idnbso.snippetodo;

import java.security.PrivilegedActionException;

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

    /**
     * Constructs a new exception with the specified detail message.  The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *                {@link #getMessage()} method.
     */
    public SnippeToDoPlatformException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of <tt>(cause==null
     * ? null : cause.toString())</tt> (which typically contains the class and detail message of
     * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers
     * for other throwables (for example, {@link PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()}
     *              method).  (A <tt>null</tt> value is permitted, and indicates that the cause is
     *              nonexistent or unknown.)
     * @since 1.4
     */
    public SnippeToDoPlatformException(Throwable cause)
    {
        super(cause);
    }
}