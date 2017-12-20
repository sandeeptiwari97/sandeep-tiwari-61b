package db;
import jdk.nashorn.internal.codegen.CompilationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class Table {
    public List<Row> rows;
    public String name;
    public String[] columnNames, types;
    public Map<String, Integer> nameToIndex = new HashMap<String, Integer>();
    public Map<Integer, String> indexToString = new HashMap<Integer, String>();
    //map columnName to index
    //map index to type
//	private List<Column> columns;
    public Table(String name, String[] colNames) {
        this.name = name;
        checkUniqueColNames(colNames);
        this.columnNames = colNames;
        this.rows = new ArrayList<Row>();
        splitColNames();
        makeMap(this.columnNames);
        makeTypeMap(this.types);
    }

    //accessor methods

    public int getNumColumns() {
        return this.columnNames.length;
    }

    public String getName() {
        return this.name;
    }

    public String[] getColHeaders() {
        String[] colHeaders = new String[this.getNumColumns()];
        for(int i = 0; i < this.getNumColumns(); i++) {
            colHeaders[i] = columnNames[i] + " " + types[i];
        }
        return colHeaders;
    }

    public void checkUniqueColNames(String[] colNames) {
        for (int i = 0; i < colNames.length; i++) {
            for (int j = i + 1; j < colNames.length; j++) {
                if (colNames[i].equals(colNames[j])) {
                    throw new Error("Column names must be unique. " + colNames[i] + " is not.");
                }
            }
        }
    }

    public void makeMap(String[] names) {
        for (int i = 0; i < names.length; i++) {
            nameToIndex.put(names[i], i);
        }
    }

    public int getIndex (String name) {
        String trimmed = name.trim();
        return nameToIndex.get(trimmed);
    }

    public boolean containsColName(String colName) {
        for (String cName : columnNames) {
            if (cName.equals(colName)) {
                return true;
            }
        }
        return false;
    }

    public String getColName (int index) {
        for(String name : nameToIndex.keySet()) {
            if (nameToIndex.get(name) == index) {
                return name;
            }
        }
        return "ERROR: IndexOutOfBounds";
    }

    public void makeTypeMap(String[] types) {
        for (int i = 0; i < types.length; i++) {
            indexToString.put(i, types[i]);
        }
    }

    public String getType(int index) {
        return indexToString.get(index);
    }

    public String getType(String name) {
        return indexToString.get(nameToIndex.get(name));
    }

    public void splitColNames() {
        types = new String[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            String[] splitted = columnNames[i].split(" ");
            columnNames[i] = splitted[0];
            types[i] = splitted[1];
        }
    }

    public void addColumnHeader(String name) {
        String[] newColHead = new String[this.getNumColumns() + 1];
        System.arraycopy(this.columnNames, 0, newColHead, 0, this.getNumColumns());
        newColHead[this.getNumColumns()] = name;
        this.columnNames = newColHead;
        String[] newTypes = new String[this.types.length + 1];
        System.arraycopy(this.types, 0, newTypes, 0, this.types.length);
        this.types = newTypes;
        nameToIndex.put(name, this.getNumColumns() - 1);
        indexToString.put(this.getNumColumns() - 1, name);
    }

    public void addRow(Object[] values) {
        Row newRow = new Row(values);
        if (rows.contains(newRow)) {
            throw new Error("Duplicate row.");
        }
        rows.add(newRow);
    }

    public String print() {
        // print nicely
        String returnThis = "";
        for (int i = 0; i < getNumColumns(); i++) {
            if (i == 0) {
                returnThis += (columnNames[i] + " " + types[i]);
            } else {
                returnThis = returnThis + "," + columnNames[i] + " " + types[i];
            }
        }
        for (Row row : rows) {
            returnThis += "\n";
            for (int i = 0; i < columnNames.length; i++) {
                Object addThis = row.getObj(i);
                if (types[i].equals("float") && addThis != "NOVALUE" && addThis != "NaN") {
                    addThis = String.format("%.3f", addThis);
                }
                if(i == 0) {
                    returnThis += addThis;
                } else {
                    returnThis = returnThis + "," + addThis;
                }
            }
        }
        return returnThis;
    }

    public String storeTable(String name){
        System.out.printf("You are trying to store the table named %s\n", name);
        try {
            File file = new File(name + ".tbl");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < getNumColumns(); i++) {
                if (i == 0) {
                    writer.write(columnNames[i] + " " + types[i]);
                } else {
                    writer.write("," + columnNames[i] + " " + types[i]);
                }
            }
            writer.write("\n");
            for (Row row : rows) {
                for (int i = 0; i < columnNames.length; i++) {
                    Object addThis = row.getObj(i);
                    if (types[i].equals("float") && addThis != "NOVALUE") {
                        addThis = String.format("%.3f", addThis);
                    }
                    if (i == 0) {
                        writer.write((String) addThis);
                    } else {
                        writer.write("," + addThis);
                    }
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
            return "";
        }
        catch(IOException ex){
            return "ERROR: making file '" + name + "'";
        }
    }
}
