/**
 *
 */
package com.github.idnbso.snippetodo.model.data.user;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User class represents the SnippeToDo registered users which consists of the user's id and user's
 * name from the user input. An User is stored in the snippetodo database's users table.
 */
@Entity
@Table(name = "users")
public class User
{
    /**
     * The user's id number in the database with identity option to prevent duplicates.
     */
    @Id
    private int id;

    /**
     * The email address of the user
     */
    private String email;

    /**
     * The first name of the user
     */
    private String firstName;

    /**
     * The last name of the user
     */
    private String lastName;

    /**
     * The password of the user
     */
    private String password;

    /**
     * User class default constructor.
     */
    public User()
    {
        super();
    }

    /**
     * User class main constructor to create a complete User object to be stored in the database.
     *
     * @param id The id of the user in database items table
     */
    public User(int id, String email, String firstName, String lastName, String password)
            throws UserException
    {
        this();
        setId(id);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
    }

    /**
     * Get the user id.
     *
     * @return the id of the user
     */
    public int getId()
    {
        return id;
    }

    /**
     * Get the email address of the user.
     *
     * @return the current email address of the user
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Get the first name of the user
     *
     * @return the current first name of the user
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Get the last name of the user
     *
     * @return the current last name of the user
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Get the password of the user
     *
     * @return the current password of the user
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Set the email address of the user.
     *
     * @return the new email address of the user
     */
    public void setEmail(String email) throws UserException
    {
        if (isEmailValid(email))
        {
            this.email = email;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setEmail: the email value is not a valid string email address.");
            throw new UserException("There was a problem with the email address value to be set.",
                                    t);
        }
    }

    /**
     * Set the first name of the user
     *
     * @return the new first name of the user
     */
    public void setFirstName(String firstName) throws UserException
    {
        if (firstName != null)
        {
            this.firstName = firstName;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setFirstName: the first name value is not a valid string.");
            throw new UserException(
                    "There was a problem with the first name string value to be set.", t);
        }
    }

    /**
     * Set the last name of the user
     *
     * @return the new last name of the user
     */
    public void setLastName(String lastName) throws UserException
    {
        if (lastName != null)
        {
            this.lastName = lastName;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setLastName: the last name value is not a valid string.");
            throw new UserException(
                    "There was a problem with the last name string value to be set.", t);
        }
    }

    /**
     * Set the password of the user
     *
     * @return the new password of the user
     */
    public void setPassword(String password) throws UserException
    {
        if (isPasswordValid(password))
        {
            this.password = password;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setPassword: the last name value is not a valid string.");
            throw new UserException(
                    "There was a problem with the last name string value to be set.", t);
        }
    }

    /**
     * Set the user id.
     *
     * @param id the id of the user to set
     */
    public void setId(int id) throws UserException
    {
        if (id > 0)
        {
            this.id = id;
        }
    }

    /**
     * The equals method for user object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        User user = (User) o;

        if (getId() != user.getId())
        {
            return false;
        }
        if (!getEmail().equals(user.getEmail()))
        {
            return false;
        }
        if (!getFirstName().equals(user.getFirstName()))
        {
            return false;
        }
        if (!getLastName().equals(user.getLastName()))
        {
            return false;
        }
        return getPassword().equals(user.getPassword());
    }

    /**
     * hashCode method for User object.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = getId();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getPassword().hashCode();
        return result;
    }

    /**
     * toString method for the User object which consists of all of its properties: id and name.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Validates an email address with regular expression
     *
     * @param email email address for validation
     * @return true valid email address, false invalid email address
     */
    private boolean isEmailValid(String email)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return isValid(email, EMAIL_PATTERN);
    }

    /**
     * Validates a password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    private boolean isPasswordValid(String password)
    {
        final String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        return isValid(password, PASSWORD_PATTERN);
    }

    /**
     * Validate a string with regular expression
     *
     * @param stringToValid string for validation
     * @return true valid string value, false invalid string value
     */
    private boolean isValid(String stringToValid, final String PATTERN)
    {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(stringToValid);
        return matcher.matches();
    }
}