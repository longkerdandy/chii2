package org.chii2.medialibrary.api.persistence.entity;

/**
 * Represent Movie Poster
 */
public interface MovieImage {

    /**
     * Get ID
     *
     * @return ID
     */
    public String getId();

    /**
     * Set ID
     *
     * @param id ID
     */
    public void setId(String id);

    /**
     * Get Image Type (Poster Backdrop)
     *
     * @return Type
     */
    public String getType();

    /**
     * Set Image Type
     *
     * @param type Type
     */
    public void setType(String type);

    /**
     * Get Image Size
     *
     * @return Size
     */
    public String getSize();

    /**
     * Set Image Size
     *
     * @param size
     */
    public void setSize(String size);

    /**
     * Get Image Height
     *
     * @return Height
     */
    public int getHeight();

    /**
     * Set Image Height
     *
     * @param height Height
     */
    public void setHeight(int height);

    /**
     * Get Image Width
     *
     * @return Width
     */
    public int getWidth();

    /**
     * Set Image Width
     *
     * @param width Width
     */
    public void setWidth(int width);

    /**
     * Get Image URL
     *
     * @return URL
     */
    public String getUrl();

    /**
     * Set Image URL
     *
     * @param url URL
     */
    public void setUrl(String url);

    /**
     * Get Image Provider ID
     *
     * @return Provider ID
     */
    public String getProviderId();

    /**
     * Set Image Provider ID
     *
     * @param providerId
     */
    public void setProviderId(String providerId);

    /**
     * Get the real Image
     *
     * @return Image
     */
    public byte[] getImage();

    /**
     * Set the real Image
     *
     * @param image Image
     */
    public void setImage(byte[] image);
}
