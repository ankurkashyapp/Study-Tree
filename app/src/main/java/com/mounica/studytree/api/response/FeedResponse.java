package com.mounica.studytree.api.response;

/**
 * Created by ankur on 10/5/16.
 */
public class FeedResponse {

    private String id;

    private String category;

    private String title;

    private String subject;

    private String description;

    private String image;

    private UserResponse user;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getSubject ()
    {
        return subject;
    }

    public void setSubject (String subject)
    {
        this.subject = subject;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public UserResponse getUser ()
    {
        return user;
    }

    public void setUser (UserResponse user)
    {
        this.user = user;
    }
}
