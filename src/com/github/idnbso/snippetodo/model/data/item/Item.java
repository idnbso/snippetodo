package com.github.idnbso.snippetodo.model.data.item;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Item class represents the SnippeToDo items which consists of the item id, the user's id creating
 * the item, and the message description itself from the user input. An Item is stored in the
 * snippetodo database's items table.
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
     * @param id     The id of the item in database items table
     * @param userId The user id of the user which created the item
     * @param listId The text describing the item
     */
    public Item(int id, int userId, int listId, String title, String body, int positionIndex)
            throws ItemException
    {
        this();
        setId(id);
        setUserId(userId);
        setListId(listId);
        setTitle(title);
        setBody(body);
        setPositionIndex(positionIndex);
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
     * @return the list id of the item
     */
    public int getListId()
    {
        return listId;
    }

    /**
     * Get the title of the item.
     *
     * @return the current title of the item
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Get the body text of the item.
     *
     * @return the current body text of the item
     */
    public String getBody()
    {
        return body;
    }

    /**
     * Get the position index in the current list of the item.
     *
     * @return the position index of the item in the current list
     */
    public int getPositionIndex()
    {
        return positionIndex;
    }

    /**
     * Set the list id of the list containing the item.
     *
     * @param listId the integer list id of the item to be set
     */
    public void setListId(int listId) throws ItemException
    {
        if (listId > 0)
        {
            this.listId = listId;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setListId: the listId value is not a valid positive integer.");
            throw new ItemException("There was a problem with the listId value to be set.", t);
        }
    }

    /**
     * Set the position index in the current list of the item.
     *
     * @param index the new position index of the item in a list
     */
    public void setPositionIndex(int index) throws ItemException
    {
        if (index > 0)
        {
            this.positionIndex = index;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setPositionIndex: the index value is not a valid positive integer.");
            throw new ItemException("There was a problem with the index value to be set.", t);
        }
    }

    /**
     * Set the title of the item.
     *
     * @param title the new title of the item
     */
    public void setTitle(String title) throws ItemException
    {
        if (title != null)
        {
            this.title = title;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setTitle: the title value is not a valid string.");
            throw new ItemException("There was a problem with the title value to be set.", t);
        }
    }

    /**
     * Set the body text of the item.
     *
     * @param body the new body text of the item
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    /**
     * Set the user id of the user which created the item.
     *
     * @param userId The userId to set
     */
    public void setUserId(int userId) throws ItemException
    {
        if (userId > 0)
        {
            this.userId = userId;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setUserId: the userId value is not a valid positive integer.");
            throw new ItemException("There was a problem with the userId value to be set.", t);
        }
    }

    /**
     * Set the item id.
     *
     * @param id The id to set
     */
    public void setId(int id) throws ItemException
    {
        if (id > 0)
        {
            this.id = id;
        }
        else
        {
            Throwable t = new Throwable(
                    "ERROR setId: the item's id value is not a valid positive integer.");
            throw new ItemException("There was a problem with the item's id value to be set.", t);
        }
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
