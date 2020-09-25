package dto;

public class PersonStyleDTO {
    
    private String name;
    private int year;
    private String styleName;

    public PersonStyleDTO(String name, int year, String styleName) {
        this.name = name;
        this.year = year;
        this.styleName = styleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
    
}
