package endorphin.json;
import java.util.List;
public class DataResult <T>{
    public DataResult() {}
    public DataResult(int total, List<T> rows) {

        this.total = total;
        this.rows = rows;
    }
    private int total;
    private List<T> rows;
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public List<T> getRows() {
        return rows;
    }
    public void setRows(List<T> rows) {
        this.rows = rows;
    }


}
