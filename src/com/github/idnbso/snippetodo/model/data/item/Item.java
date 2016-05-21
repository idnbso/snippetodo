package com.github.idnbso.snippetodo.model.data.item;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Item class represents the SnippeToDo items which consists of the item id, the user's id creating
 * the item, and the message description itself from the user input. An Item is stored in the
 * snippetodo database's items table.
 *
 * @author Idan Busso
 * @author Shani Kahila
 */
@Entity
@Table(name = "items")
public class Item implements Serializable
{
    /**
     * The Item's id number in the database with auto increment option to prevent duplicates.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The Item's user id number which is equivalent to the user created the item.
     */
    private int userId;

    /**
     * The Item's list id number which the item resides in the client.
     */
    private int listId;

    /**
     * The position index number of the specific location in the list where the item resides in the
     * client.
     */
    private int positionIndex;

    /**
     * The title text of the item.
     */
    private String title;

    /**
     * The body text of the item.
     */
    private String body;

    /**
     * Item class default constructor.
     */
    public Item()
    {
        super();
    }

    /**
     * Item class main constructor to create a complete Item object to be stored in the database.
     *
     * @param id          The id of the item in database items table
     * @param userId      The user id of the user which created the item
     * @param listId      The text describing the item
     */
    public Item(int id, int userId, int listId, String title, String body, int positionIndex)
    {
        super();
        this.setId(id);
        this.setUserId(userId);
        this.setListId(listId);
        this.setTitle(title);
        this.setBody(body);
        this.setPositionIndex(positionIndex);
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
     * Get the user id of the user which created the item.
     *
     * @return the userId of the user which created the item
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * Get the list id of the list containing the item.
     *
     * @return the userId of the user which created the item
     */
    public int getListId()
    {
        return listId;
    }

    public void setListId(int listId)
    {
        this.listId = listId;
    }

    public int getPositionIndex()
    {
        return positionIndex;
    }

    public void setPositionIndex(int index)
    {
        this.positionIndex = index;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
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
     * hashCode method for Item object.
     *
     * @see java.lang.Object#hashCode()
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

        Item item = (Item) o;

        if (getId() != item.getId())
        {
            return false;
        }
        if (getUserId() != item.getUserId())
        {
            return false;
        }
        if (getListId() != item.getListId())
        {
            return false;
        }
        if (getPositionIndex() != item.getPositionIndex())
        {
            return false;
        }
        if (!getTitle().equals(item.getTitle()))
        {
            return false;
        }
        return getBody().equals(item.getBody());

    }

    /**
     * The equals method for Item object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public int hashCode()
    {
        int result = getId();
        result = 31 * result + getUserId();
        result = 31 * result + getListId();
        result = 31 * result + getPositionIndex();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getBody().hashCode();
        return result;
    }

    /**
     * toString method for the Item object which consists of all of its properties: id, user id and
     * description.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", listId=").append(listId);
        sb.append(", positionIndex=").append(positionIndex);
        sb.append(", title='").append(title).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
