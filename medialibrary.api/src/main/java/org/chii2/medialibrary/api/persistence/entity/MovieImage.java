package org.chii2.medialibrary.api.persistence.entity;

/**
 * Represent Movie Poster
 */
public interface MovieImage {

    // Movie Image Content Type - Poster
    public final static String POSTER_CONTENT_TYPE = "poster";
    // Movie Image Content Type - Backdrop
    public final static String BACKDROP_CONTENT_TYPE = "backdrop";
    // Movie Image Type - JPEG
    public final static String JPEG_IMAGE_TYPE = "jpeg";
    // Movie Image Type - PNG
    public final static String PNG_IMAGE_TYPE = "png";

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
     * Get Content Type (Poster Backdrop)
     *
     * @return Type
     */
    public String getContentType();

    /**
     * Set Content Type
     *
     * @param type Content Type
     */
    public void setContentType(String type);

    /**
     * Get Image Type (JPEG PNG)
     *
     * @return Image Type
     */
    public String getImageType();

    /**
     * Set Image Type
     *
     * @param imageType Image Type
     */
    public void setImageType(String imageType);

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
