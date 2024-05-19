package es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem;

public enum EBookShelfItem {
    TO_READ("Para Leer"),
    READ("Leídos");

    private String display_name;
    EBookShelfItem(String display_name) {
        this.display_name = display_name;
    }

    public String getDisplayName() {
        return display_name;
    }
}
