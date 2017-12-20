package db;
import java.util.ArrayList;
public class Column {

	public Column() {
	    int i = 0;
    }

    public ArrayList<ArrayList<Object>> convertTableToColumns(Table table) {
        ArrayList<ArrayList<Object>> tbl = new ArrayList<ArrayList<Object>>();
	    for (int i = 0; i < table.getNumColumns(); i++) {
	        ArrayList<Object> col = new ArrayList<Object>();
	        for (int j = 0; j < table.rows.size(); j++) {
	            col.add(table.rows.get(j).getObj(i));
            }
            tbl.add(col);
        }
        return tbl;
    }

    public Table convertColumnsToTable(ArrayList<ArrayList<Object>> cols, String[] colHeaders) {
        Table newTable = new Table("temp", colHeaders);
        for (int i = 1; i < cols.get(0).size(); i++) {
            Object[] addRowArr = new Object[cols.size()];
            for (int j = 0; j < cols.size(); j++) {
                addRowArr[j] = cols.get(j).get(i);
            }
            newTable.addRow(addRowArr);
        }
        return newTable;
    }
}
