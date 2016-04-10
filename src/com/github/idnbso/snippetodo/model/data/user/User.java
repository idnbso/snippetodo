/**
 * 
 */
package com.github.idnbso.snippetodo.model.data.user;

import java.io.Serializable;

import javax.persistence.*;

/**
 * User class represents the SnippeToDo registered users which consists of the
 * user's id and user's name from the user input. An User is stored in the
 * snippetodo database's users table.
 * 
 * @author Idan Busso
 * @author Shani Kahila
 */
@Entity
@Table(name = "users")
public class User
{
    /**
     * The user's id number in the database with identity option to prevent
     * duplicates.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the user
     */
    @Column(nullable = false)
    private String name;

    /**
     * User class default constructor.
     */
    public User()
    {
        super();
    }

    /**
     * User class main constructor to create a complete User object to be stored
     * in the database.
     * 
     * @param id The id of the user in database items table
     * @param name The name of the user
     */
    public User(int id, String name)
    {
        super();
        this.setId(id);
        this.setName(name);
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
     * Get the user name.
     * 
     * @return the name of the user
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the user's id.
     * 
     * @param id the id of the user to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Set the user name.
     * 
     * @param name the name to set for the user
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * hashCode method for User object.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * The equals method for user object.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof User))
        {
            return false;
        }
        User other = (User) obj;
        if (id != other.id)
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        return true;
    }

    /**
     * toString method for the User object which consists of all of its
     * properties: id and name.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("User [id=").append(id).append(", name=").append(name).append("]");
        return builder.toString();
    }

}
