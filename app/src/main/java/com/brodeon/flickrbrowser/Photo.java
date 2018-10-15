package com.brodeon.flickrbrowser;

import java.io.Serializable;

class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Pole zawierające tytuł zdjęcia
     */
    private String mTitle;

    /**
     * Pole zawierające nazwę autora
     */
    private String mAuthor;

    /**
     * Pole zawierające id autora
     */
    private String mAuthorId;

    /**
     * Pole zawierające link do zdjęcia w pełnej rozdzielczości. Link te używany jest w PhotoDetailActivity
     */
    private String mLink;

    /**
     * Pole zawierające tagi zdjęcia
     */
    private String mTags;

    /**
     * Pole zawierające link do zdjęcia w mniejszej rozdzielczości. Link ten używany jest w RecycleViewAdapter
     */
    private String mImage;

    /**
     * Konstruktor obiektu klasy Photo
     * @param title
     * @param author
     * @param authorId
     * @param link
     * @param tags
     * @param image
     */
    public Photo(String title, String author, String authorId, String link, String tags, String image) {
        mTitle = title;
        mAuthor = author;
        mAuthorId = authorId;
        mLink = link;
        mTags = tags;
        mImage = image;
    }

    String getTitle() {
        return mTitle;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getAuthorId() {
        return mAuthorId;
    }

    String getLink() {
        return mLink;
    }

    String getTags() {
        return mTags;
    }

    String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
