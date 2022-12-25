package models;

public class Cell {
    // - blank,  O pilka,  A gracz, _ środek,
    public String type;
    public int goals;  // only if cell type is a side goal.

    public Cell(String type){
        this.type=type;
    }
}
