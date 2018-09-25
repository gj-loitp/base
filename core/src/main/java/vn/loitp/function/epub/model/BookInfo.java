package vn.loitp.function.epub.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by loitp on 08.09.2016.
 */
public class BookInfo implements Serializable {
    private String title;
    private byte[] coverImage;
    private String filePath;

    private boolean isCoverImageNotExists;
    private static transient Bitmap coverImageBitmap;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public Bitmap getCoverImageBitmap() {
        return coverImageBitmap;
    }

    public void setCoverImageBitmap(Bitmap coverImageBitmap) {
        this.coverImageBitmap = coverImageBitmap;
    }

    public boolean isCoverImageNotExists() {
        return isCoverImageNotExists;
    }

    public void setCoverImageNotExists(boolean coverImageNotExists) {
        isCoverImageNotExists = coverImageNotExists;
    }
}
