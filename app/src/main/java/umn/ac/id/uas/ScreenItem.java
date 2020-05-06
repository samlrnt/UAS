package umn.ac.id.uas;

public class ScreenItem {

    String Title,Description;
    int ImgSlide;

    public ScreenItem(String title, String description, int imgSlide) {
        Title = title;
        Description = description;
        ImgSlide = imgSlide;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenImg(int imgSlide) {
        ImgSlide = imgSlide;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenImg() {
        return ImgSlide;
    }
}
