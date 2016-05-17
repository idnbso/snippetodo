/**
 * 
 */
package com.github.idnbso.snippetodo.model.data.user;

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
     * The email address of the user
     */
    @Column(nullable = false)
    private String email;

    /**
     * The first name of the user
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * The last name of the user
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * The password of the user
     */
    @Column(nullable = false)
    private String password;

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
     * TODO
     */
    public User(int id, String email, String firstName, String lastName, String password)
    {
        setId(id);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
     * Set the user's id.
     * 
     * @param id the id of the user to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

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
//    /**
//     * Set the user name.
//     *
//     * @param name the name to set for the user
//     */
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//
//    /**
//     * hashCode method for User object.
//     *
//     * @see java.lang.Object#hashCode()
//     */
//    @Override
//    public int hashCode()
//    {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + id;
//        result = prime * result + ((name == null) ? 0 : name.hashCode());
//        return result;
//    }
//
//    /**
//     * The equals method for user object.
//     *
//     * @see java.lang.Object#equals(java.lang.Object)
//     */
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (this == obj)
//        {
//            return true;
//        }
//        if (obj == null)
//        {
//            return false;
//        }
//        if (!(obj instanceof User))
//        {
//            return false;
//        }
//        User other = (User) obj;
//        if (id != other.id)
//        {
//            return false;
//        }
//        if (name == null)
//        {
//            if (other.name != null)
//            {
//                return false;
//            }
//        }
//        else if (!name.equals(other.name))
//        {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * toString method for the User object which consists of all of its
//     * properties: id and name.
//     *
//     * @see java.lang.Object#toString()
//     */
//    @Override
//    public String toString()
//    {
//        StringBuilder builder = new StringBuilder();
//        builder.append("User [id=").append(id).append(", name=").append(name).append("]");
//        return builder.toString();
//    }

}
