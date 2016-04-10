package com.github.idnbso.snippetodo.model.data.item;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Item class represents the SnippeToDo items which consists of the item id, the
 * user's id creating the item, and the message description itself from the user
 * input. An Item is stored in the snippetodo database's items table.
 * 
 * @author Idan Busso
 * @author Shani Kahila
 */
@Entity
@Table(name = "items")
public class Item
{
    /**
     * The Item's id number in the database with auto increment option to
     * prevent duplicates.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The Item's user id number which is equilavent to the user created the
     * item.
     */
    @Column(nullable = false)
    private int userId;

    /**
     * The description text of the item.
     */
    private String description;

    /**
     * Item class default constructor.
     */
    public Item()
    {
        super();
    }

    /**
     * Item class main constructor to create a complete Item object to be stored
     * in the database.
     * 
     * @param id The id of the item in database items table
     * @param userId The user id of the user which created the item
     * @param description The text describing the item
     */
    public Item(int id, int userId, String description)
    {
        super();
        this.setId(id);
        this.setDescription(description);
        this.setUserId(userId);
    }

    /**
     * Get the item id.
     * 
     * @return the id of the item
     */
    public int getId()
    {
        return id;
    }

    /**
     * Get the text description of the item.
     * 
     * @return the text description of the item
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Get the user id of the user which created the item.
     * 
     * @return the userId of the user which created the item
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * Set the user id of the user which created the item.
     * 
     * @param userId The userId to set
     */
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * Set the item's id.
     * 
     * @param id The id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Set the text description of the item.
     * 
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * hashCode method for Item object.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + id;
        result = prime * result + userId;
        return result;
    }

    /**
     * The equals method for Item object.
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
        if (!(obj instanceof Item))
        {
            return false;
        }
        Item other = (Item) obj;
        if (description == null)
        {
            if (other.description != null)
            {
                return false;
            }
        }
        else if (!description.equals(other.description))
        {
            return false;
        }
        if (id != other.id)
        {
            return false;
        }
        if (userId != other.userId)
        {
            return false;
        }
        return true;
    }

    /**
     * toString method for the Item object which consists of all of its
     * properties: id, user id and description.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Item [id=").append(id).append(", userId=").append(userId)
                .append(", description=").append(description).append("]");
        return builder.toString();
    }

}
