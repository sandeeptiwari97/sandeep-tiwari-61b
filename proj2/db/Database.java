package db;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;


public class Database {
    private Map<String, Table> db;
    private Map<String, Object[]> nameToValues;
    public Database() {
        //Map name to Table Object
        db = new HashMap<String, Table>();
        nameToValues = new HashMap<String, Object[]>();

    }

    public String transact(String query) {
        return eval(query);
    }

    // Various common constructs, simplifies parsing.
    private final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*"
            + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    + "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+"
                    + "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+"
                    + SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    + "\\s*(?:,\\s*.+?\\s*)*)");

    private String eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            return "ERROR: Malformed query " + query;
        }
    }

    private String createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            return createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            return "ERROR: Malformed create: " + expr;
        }
    }

    private String createNewTable(String name, String[] cols) {
        StringJoiner joiner = new StringJoiner(", ");
        System.out.println(cols);
        for (int i = 0; i < cols.length - 1; i++) {
            joiner.add(cols[i]);

        }

        for (int j = 0; j < cols.length; j++) {
            cols[j] = cols[j].replaceAll("(\\s)\\s*", "$1");
            System.out.print(cols[j]);
        }

        String colSentence = joiner.toString() + " and " + cols[cols.length - 1];
        System.out.printf("You are trying to create a table named %s with the columns "
                + "%s\n", name, colSentence);
        String checkCols = checkValidHeader(cols);
        if (!checkCols.equals("")) {
            return checkCols;
        }
        Table newTable = new Table(name, cols);
        if (!checkValidTypes(newTable)) {
            return "ERROR: This table needs types and names";
        }
        if (!db.containsKey(name)) {
            db.put(name, newTable);
        }
        return "";
    }
    private String createCond(String name, String exprs, String tables, String conds) {
        String returnThis = "";
        Table applyTable;
        Table newTable;
        if (exprs.equals("*") && !tables.contains(",")) {
            Map<String, Table> map = createSelectAllBasic(tables);
            for (String k : map.keySet()) {
                if (!k.equals("")) {
                    return k;
                }
                applyTable = map.get(k);
                Map<String, Table> map1 = createApplyConditional(applyTable, conds);
                for (String key : map1.keySet()) {
                    returnThis = key;
                    newTable = map1.get(key);
                    db.put(name, newTable);
                    newTable.name = name;
                }
            }
        } else if (exprs.equals("*")) {
            Map<String, Table> map = createJoinBasic(exprs, tables, conds);
            for (String k : map.keySet()) {
                if (!k.equals("")) {
                    return k;
                }
                applyTable = map.get(k);
                Map<String, Table> map1 = createApplyConditional(applyTable, conds);
                for (String key : map1.keySet()) {
                    returnThis = key;
                    newTable = map1.get(key);
                    db.put(name, newTable);
                    newTable.name = name;
                }
            }
        } else if (checkOperator(exprs)) {
            Table curTable = db.get(tables);
            Map<String, Table> map = createCheckNumeric(exprs, curTable);
            for (String k : map.keySet()) {
                if (!k.equals("")) {
                    return k;
                }
                applyTable = map.get(k);
                Map<String, Table> map1 = createApplyConditional(applyTable, conds);
                for (String key : map1.keySet()) {
                    returnThis = key;
                    newTable = map1.get(key);
                    db.put(name, newTable);
                    newTable.name = name;
                }
            }
        } else if (!tables.contains(",")) {
            Map<String, Table> map = createSelectBasic(exprs, tables);
            for (String k : map.keySet()) {
                if (!k.equals("")) {
                    return k;
                }
                applyTable = map.get(k);
                Map<String, Table> map1 = createApplyConditional(applyTable, conds);
                for (String key : map1.keySet()) {
                    returnThis = key;
                    newTable = map1.get(key);
                    db.put(name, newTable);
                    newTable.name = name;
                }
            }
        } else {
            return "ERROR: not valid selectOp" + exprs + tables;
        }
        return "";
    }
    private String createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these "
                + "expressions: '%s' from the join of these tables: '%s', filtered by these "
                + "conditions: '%s'\n", name, exprs, tables, conds);
        Table newTable;
        String returnThis = "";
        if (conds != null) {
            return createCond(name, exprs, tables, conds);
        } else if (exprs.equals("*") && !tables.contains(",")) {
            Map<String, Table> map = createSelectAllBasic(tables);
            for (String key : map.keySet()) {
                returnThis = key;
                newTable = map.get(key);
                db.put(name, newTable);
                newTable.name = name;
            }
        } else if (exprs.equals("*")) {
            Map<String, Table> map = createJoinBasic(exprs, tables, conds);
            for (String key : map.keySet()) {
                returnThis = key;
                newTable = map.get(key);
                db.put(name, newTable);
                newTable.name = name;
            }
        } else if (checkOperator(exprs)) {
            Table curTable = db.get(tables);
            Map<String, Table> map = createCheckNumeric(exprs, curTable);
            for (String key : map.keySet()) {
                returnThis = key;
                newTable = map.get(key);
                db.put(name, newTable);
                newTable.name = name;
            }
        } else if (!tables.contains(",")) {
            Map<String, Table> map = createSelectBasic(exprs, tables);
            for (String key : map.keySet()) {
                returnThis = key;
                newTable = map.get(key);
                db.put(name, newTable);
                newTable.name = name;
            }
        }
        return returnThis;
    }

    private Map<String, Table> createApplyConditional(Table table, String conds) {
        Map<String, Table> map = new HashMap<String, Table>();
        String error = "";
        String[] colHeaders = table.getColHeaders();
        Table newTable = new Table("temp", colHeaders);
        Set<Integer> skip = new HashSet<Integer>();
        String[] splitConds = conds.split(" and ");
        for (String cond : splitConds) {
            if (!checkComparator(cond)) {
                error = "ERROR: Malformed conditional, requires comparator";
                map.put(error, newTable);
                return map;
            }
            String comparator = getComparator(cond);
            //System.out.println("comparator" + comparator);
            String[] condSplit = cond.split("\\" + comparator);
            if (condSplit.length != 2) {
                error = "ERROR: Malformed conditional, requires comparator";
                map.put(error, newTable);
                return map;
            }
            if (condSplit[0] == null || condSplit[1] == null) {
                error = "ERROR: Malformed condition, you need two things to compare";
                map.put(error, newTable);
                return map;
            }
            String firstColName = condSplit[0];
            String second = condSplit[1];
            String type2 = checkLiteralType(second);
            if (type2.contains("ERROR:")) {
                error = type2;
                map.put(error, newTable);
                return map;
            }
            if (type2.equals("column")) {
                //BINARY
                String secondColName = second;
                //System.out.println("firstCol" + firstColName);
                int index1 = table.getIndex(firstColName);
                String type1 = table.getType(index1);
                //System.out.println("secondCol" + secondColName);
                int index2 = table.getIndex(secondColName);
                type2 = table.getType(index2);
                for (int i = 0; i < table.rows.size(); i++) {
                    Object firstObject = table.rows.get(i).getObj(index1);
                    Object secondObject = table.rows.get(i).getObj(index2);
                    boolean unary = false;
                    if (!applyCompare(firstObject, secondObject, type1, type2, comparator, unary)) {
                        skip.add(i);
                    }
                }
                //UNARY
            } else {
                int index1 = table.getIndex(firstColName);
                String type1 = table.getType(index1);
                for (int i = 0; i < table.rows.size(); i++) {
                    Object firstObject = table.rows.get(i).getObj(index1);
                    boolean unary = true;
                    if (!applyCompare(firstObject, second, type1, type2, comparator, unary)) {
                        skip.add(i);
                    }
                }
            }
        }
        //Adds rows to newTable from old table that does follow conditional
        for (int i = 0; i < table.rows.size(); i++) {
            if (!skip.contains(i)) {
                newTable.addRow(table.rows.get(i).inputs);
            }
        }
        map.put(error, newTable);
        return map;
    }

    private Map<String, Table> createSelectAllBasic(String tableName) {
        Table curTable = db.get(tableName);
        Map<String, Table> map = new HashMap<String, Table>();
        map.put("", curTable);
        return map;
    }

    private Map<String, Table> createCheckNumeric(String exprs, Table table) {
        Map<String, Table> errAndTable = new HashMap<String, Table>();
        String selectExpr = "";
        String[] separateExprs = exprs.split(",");
        if (!checkOperator(separateExprs[0])) {
            selectExpr = separateExprs[0];
        } else {
            String computed = computeExpression(separateExprs[0], table);
            if (computed.contains("ERROR:")) {
                errAndTable.put(computed, table);
                return errAndTable;
            }
            selectExpr = computed;
        }
        for (int i = 1; i < separateExprs.length; i++) {
            if (!checkOperator(separateExprs[i])) {
                selectExpr = selectExpr + ", " + separateExprs[i];
            } else {
                String computed = computeExpression(separateExprs[i], table);
                if (computed.contains("ERROR:")) {
                    errAndTable.put(computed, table);
                    return errAndTable;
                }
                selectExpr = selectExpr + ", " + computed;
            }
        }
        return createSelectBasic(selectExpr, table.getName());
    }


    private Map<String, Table> createSelectBasic(String expr, String tableName) {
        Map<String, Table> map = new HashMap<String, Table>();
        Table curTable = db.get(tableName);
        String[] splitExpr;
        if (!expr.contains(",")) {
            splitExpr = new String[1];
            splitExpr[0] = expr;
        } else {
            expr = expr.replaceAll(" ", "");
            splitExpr = expr.split(",");
        }
        String[] colHeader = new String[splitExpr.length];
        for (int i = 0; i < colHeader.length; i++) {
            //System.out.println("splitExpr " + splitExpr[i]);
            String colName = splitExpr[i].trim();
            int index = curTable.getIndex(colName);
            //System.out.println("index " + index);
            String type = curTable.getType(index);
            //System.out.println("type "  + type);
            colHeader[i] = colName + " " + type;
            //System.out.println("colHeader " + colHeader[i]);
        }
        Table newTable = new Table("temp", colHeader);
        for (int i = 0; i < curTable.rows.size(); i++) {
            Object[] temp = new Object[colHeader.length];
            for (int j = 0; j < colHeader.length; j++) {
                temp[j] = curTable.rows.get(i).getObj(curTable.getIndex(splitExpr[j]));
            }
            newTable.addRow(temp);
        }
        map.put("", newTable);
        return map;
    }

    private Map<String, Table> cart2Tbls(Table one, Table two, String[] cols, String[] cols2) {
        Map<String, Table> map = new HashMap<String, Table>();
        String[] colHeaders = new String[cols.length + cols2.length];
        int count = 0;
        for (int i = 0; i < cols.length; i++) {
            colHeaders[count] = cols[i] + " " + one.types[i];
            count += 1;
        }
        for (int i = 0; i < cols2.length; i++) {
            colHeaders[count] = cols2[i] + " " + two.types[i];
            count += 1;
        }
        Table newTable = new Table("temp", colHeaders);
        int numOfRows = one.rows.size() * two.rows.size();
        for (int i = 0; i < one.rows.size(); i++) {
            Object[] firstRow = one.rows.get(i).inputs;
            for (int j = 0; j < two.rows.size(); j++) {
                Object[] row = new Object[colHeaders.length];
                Object[] secondRow = two.rows.get(j).inputs;
                System.arraycopy(firstRow, 0, row, 0, firstRow.length);
                System.arraycopy(secondRow, 0, row, firstRow.length, secondRow.length);
                newTable.addRow(row);
            }
        }
        map.put("", newTable);
        return map;
    }

    private Map<String, Table> createJoinBasic(String exprs, String tables, String conds) {
        System.out.printf("You are trying to select these expressions:"
                + " '%s' from the join of these tables: '%s', filtered by "
                + "these conditions: '%s'\n", exprs, tables, conds);
        Map<String, Table> map = new HashMap<String, Table>();
        Set<String> colNames = new HashSet<String>();
        String[] tableNames = tables.split(",");
        Table curTable = db.get(tableNames[0]);
        Table secondTable = db.get(tableNames[1]);
        String[] cNames1 = curTable.columnNames;
        String[] cNames2 = secondTable.columnNames;
        Set<String> c2 = new HashSet<String>();
        for (String cName : cNames1) {
            colNames.add(cName);
        }
        for (String cName2 : cNames2) {
            c2.add(cName2);
        }
        colNames.retainAll(c2);
        if (colNames.isEmpty()) {
            return cart2Tbls(curTable, secondTable, cNames1, cNames2);
        }
        String commonCol = "";
        for (String comName : colNames) {
            commonCol = comName;
        }
        int firstIndex = curTable.getIndex(commonCol);
        int secondIndex = secondTable.getIndex(commonCol);
        String[] allColNames = new String[cNames1.length + cNames2.length - 1];
        allColNames[0] = commonCol + " " + curTable.getType(commonCol);
        int count = 1;
        for (int i = 0; i < cNames1.length; i++) {
            if (i != firstIndex) {
                allColNames[count] = cNames1[i] + " " + curTable.getType(cNames1[i]);
                count += 1;
            }
        }
        for (int j = 0; j < cNames2.length; j++) {
            if (j != secondIndex) {
                allColNames[count] = cNames2[j] + " " + secondTable.getType(cNames2[j]);
                count += 1;
            }
        }

        Table newTable = new Table("temp", allColNames);
        List<Integer> index1 = new ArrayList<Integer>();
        List<Integer> index2 = new ArrayList<Integer>();
        for (int i = 0; i < curTable.rows.size(); i++) {
            Object firstObj = curTable.rows.get(i).getObj(firstIndex);
            for (int j = 0; j < secondTable.rows.size(); j++) {
                Object secondObj = secondTable.rows.get(j).getObj(secondIndex);
                if (firstObj.equals(secondObj)) {
                    index1.add(i);
                    index2.add(j);
                }
            }
        }
        for (int i = 0; i < index1.size(); i++) {
            Row firstRow = curTable.rows.get(index1.get(i));
            Row secondRow = secondTable.rows.get(index2.get(i));
            Object[] newRow = new Object[allColNames.length];
            newRow[0] = firstRow.inputs[firstIndex];
            int count2 = 1;
            for (int k = 0; k < firstRow.inputs.length; k++) {
                if (k != firstIndex) {
                    newRow[count2] = firstRow.inputs[k];
                    count2 += 1;
                }
            }
            for (int l = 0; l < secondRow.inputs.length; l++) {
                if (l != secondIndex) {
                    newRow[count2] = secondRow.inputs[l];
                    count2 += 1;
                }
            }
            newTable.addRow(newRow);
        }
        map.put("", newTable);
        return map;
    }

    private String convertStringToObjectArray(String line, String name, Table table) {
        String[] splitRow = line.split(",");
        Table curTable = table;
        Object[] values = new Object[curTable.getNumColumns()];
        if (splitRow.length !=  values.length) {
            return "ERROR: Row needs to be the same length as the table"
                    + splitRow.length + " " + values.length;
        }
        for (int i = 0; i < values.length; i++) {
            String type = curTable.getType(i);
            if (splitRow[i].equals("NOVALUE")) {
                values[i] = "NOVALUE";
            } else if (splitRow[i].matches(".*\\d+.*")
                    && !splitRow[i].matches(".*[a-zA-Z]+.*")
                    && !splitRow[i].contains("'")) {
                if (splitRow[i].contains(".")) {
                    if (!type.equals("float")) {
                        return "ERROR: Input " + splitRow[i] + " doesn't match float. " + type;
                    }
                    values[i] = Float.parseFloat(splitRow[i]);
                } else if (!type.equals("int")) {
                    return "ERROR: Input " + splitRow[i] + " is not an int, doesn't match " + type;
                } else {
                    values[i] = Integer.parseInt(splitRow[i]);
                }
            } else {
                if (!type.equals("string")) {
                    return "ERROR: this input is not a string" + splitRow[i]
                            + "is NOT string " + type;
                }
                values[i] = splitRow[i];
            }
        }
        nameToValues.put(name, values);
        return "";
    }

    public boolean checkValidTypes(Table table) {
        for (int i = 0; i < table.getNumColumns(); i++) {
            if (table.getType(i) == null) {
                return false;
            }
        }
        return true;
    }

    public String checkValidHeader(String[] colHeaders) {
        for (int i = 0; i < colHeaders.length; i++) {
            String[] splitHeaders = colHeaders[i].split(" ");
            if (splitHeaders.length == 1) {
                return "ERROR: Need matching pairs of type and name";
            }
        }
        for (int i = 0; i < colHeaders.length; i++) {
            if (!colHeaders[i].contains(" string")) {
                if (!colHeaders[i].contains(" int")) {
                    if (!colHeaders[i].contains(" float")) {
                        return "ERROR: Does not contain valid type.";
                    }
                }
            }
        }
        return "";
    }
    private String loadTable(String name) {
        System.out.printf("You are trying to load the table named %s\n", name);
        String inputFileName = name + ".tbl";
        String line = null;
        try {
            FileReader file = new FileReader(inputFileName);
            BufferedReader buffRead = new BufferedReader(file);
            String colNames = buffRead.readLine();
            if (colNames == null) {
                return "ERROR: you need new column names.";
            }
            String[] colNamesHeaders  = colNames.split(",");
            String checkHeaders = checkValidHeader(colNamesHeaders);
            if (!checkHeaders.equals("")) {
                return checkHeaders;
            }
            Table loadedTable = new Table(name, colNamesHeaders);
            line = buffRead.readLine();

            while (line != null) {
                String errors = convertStringToObjectArray(line, name, loadedTable);
                if (!errors.equals("")) {
                    return errors;
                }
                loadedTable.addRow(nameToValues.get(name));
                line = buffRead.readLine();
            }
            db.put(name, loadedTable);
            buffRead.close();
            /* Implementation for BufferedReader for file names attained from
             * https://www.caveofprogramming.com/java/java-file-reading-and-
             * writing-files-in-java.html */
        } catch (FileNotFoundException ex) {
            return "ERROR: Unable to find file named" + inputFileName + ".";
        } catch (IOException ex) {
            return "ERROR: IOException reading file" + inputFileName + ".";
        }
        return "";
    }

    private String storeTable(String name) {
        System.out.printf("You are trying to store the table named %s\n", name);
        if (!db.containsKey((name))) {
            return ("ERROR: Table " + name + " does not exist in Database.");
        }
        Table curTable = db.get(name);
        return curTable.storeTable(name);

    }

    private String dropTable(String name) {
        System.out.printf("You are trying to drop the table named %s\n", name);
        if (!db.containsKey((name))) {
            return ("ERROR: Table " + name + " does not exist in Database.");
        }
        db.remove(name);
        return "";
    }

    private String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr;
        }
        String name = m.group(1);
        String row = m.group(2);
        if (!db.containsKey(name)) {
            return ("ERROR: Table does not exist in database.");
        }
        String[] splitRow = row.split(",");
        Table curTable = db.get(name);
        String returnThis =  convertStringToObjectArray(row, name, curTable);
        if (!returnThis.equals("")) {
            return returnThis;
        }
        Object[] thisValues = nameToValues.get(name);
        curTable.addRow(thisValues);
        return returnThis;

    }

    private String printTable(String name) {
        //System.out.println(db.get(name));
        System.out.printf("You are trying to print the table named %s\n", name);
        if (!db.containsKey(name)) {
            return "ERROR: This table " + name + " does not exist in the database.";
        }
        Table table = db.get(name);
        return table.print();
    }

    private String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed select: " + expr;
        }
        return select(m.group(1), m.group(2), m.group(3));
    }

    private String joinWithNoConditions(String exprs, String tables, String conds) {
        Set<String> colNames = new HashSet<String>();
        String[] tableNames = tables.split(",");
        Map<Table, String[]> tableToColumnNames = new HashMap<Table, String[]>();
        for (int i = 0; i < tableNames.length; i++) {
            Table curTable = db.get(tableNames[i]);
            String[] cNames = curTable.columnNames;
            tableToColumnNames.put(curTable, cNames);
        }
        Set<String> allTheSets = new HashSet<String>();
        String[] firstTableCols = tableToColumnNames.get(db.get(tableNames[0]));
        for (String col: firstTableCols) {
            allTheSets.add(col);
        }
        for (Table k : tableToColumnNames.keySet()) {
            Set<String> colSet = new HashSet<String>();
            String[] cNames = tableToColumnNames.get(k);
            for (String cName: cNames) {
                colSet.add(cName);
            }
            allTheSets.retainAll(colSet);
        }
        return "";
    }

    private String cartesianTwoTables(Table one, Table two, String[] cNames1, String[] cNames2) {
        String[] colHeaders = new String[cNames1.length + cNames2.length];
        int count = 0;
        for (int i = 0; i < cNames1.length; i++) {
            colHeaders[count] = cNames1[i] + " " + one.types[i];
            count += 1;
        }
        for (int i = 0; i < cNames2.length; i++) {
            colHeaders[count] = cNames2[i] + " " + two.types[i];
            count += 1;
        }

        Table newTable = new Table("temp", colHeaders);
        int numOfRows = one.rows.size() * two.rows.size();
        for (int i = 0; i < one.rows.size(); i++) {
            Object[] firstRow = one.rows.get(i).inputs;
            for (int j = 0; j < two.rows.size(); j++) {
                Object[] row = new Object[colHeaders.length];
                Object[] secondRow = two.rows.get(j).inputs;
                System.arraycopy(firstRow, 0, row, 0, firstRow.length);
                System.arraycopy(secondRow, 0, row, firstRow.length, secondRow.length);
                newTable.addRow(row);
            }
        }
        return newTable.print();
    }

    private String joinBasic(String exprs, String tables, String conds) {
        System.out.printf("You are trying to select these expressions:"
                + " '%s' from the join of these tables: '%s', filtered by "
                + "these conditions: '%s'\n", exprs, tables, conds);
        Set<String> colNames = new HashSet<String>();
        String[] tableNames = tables.split(",");
        Table curTable = db.get(tableNames[0]);
        Table secondTable = db.get(tableNames[1]);
        String[] cNames1 = curTable.columnNames;
        String[] cNames2 = secondTable.columnNames;
        Set<String> c2 = new HashSet<String>();
        for (String cName : cNames1) {
            colNames.add(cName);
        }
        for (String cName2 : cNames2) {
            c2.add(cName2);
        }
        colNames.retainAll(c2);
        if (colNames.isEmpty()) {
            return cartesianTwoTables(curTable, secondTable, cNames1, cNames2);
        }
        String commonCol = "";
        for (String comName: colNames) {
            commonCol = comName;
        }
        int firstIndex = curTable.getIndex(commonCol);
        int secondIndex = secondTable.getIndex(commonCol);
        String[] allColNames = new String[cNames1.length + cNames2.length - 1];
        allColNames[0] = commonCol + " " + curTable.getType(commonCol);
        int count = 1;
        for (int i = 0; i < cNames1.length; i++) {
            if (i != firstIndex) {
                allColNames[count] = cNames1[i] + " " + curTable.getType(cNames1[i]);
                count += 1;
            }
        }
        for (int j = 0; j < cNames2.length; j++) {
            if (j != secondIndex) {
                allColNames[count] = cNames2[j] + " " + secondTable.getType(cNames2[j]);
                count += 1;
            }
        }

        Table newTable = new Table("temp", allColNames);
        List<Integer> index1 = new ArrayList<Integer>();
        List<Integer> index2 = new ArrayList<Integer>();

        for (int i = 0; i < curTable.rows.size(); i++) {
            Object firstObj = curTable.rows.get(i).getObj(firstIndex);
            for (int j = 0; j < secondTable.rows.size(); j++) {
                Object secondObj = secondTable.rows.get(j).getObj(secondIndex);
                if (firstObj.equals(secondObj)) {
                    index1.add(i);
                    index2.add(j);
                }
            }
        }
        for (int i = 0; i < index1.size(); i++) {
            Row firstRow = curTable.rows.get(index1.get(i));
            Row secondRow = secondTable.rows.get(index2.get(i));
            Object[] newRow = new Object[allColNames.length];
            newRow[0] = firstRow.inputs[firstIndex];
            int count2 = 1;
            for (int k = 0; k < firstRow.inputs.length; k++) {
                if (k != firstIndex) {
                    newRow[count2] = firstRow.inputs[k];
                    count2 += 1;
                }
            }
            for (int l = 0; l < secondRow.inputs.length; l++) {
                if (l != secondIndex) {
                    newRow[count2] = secondRow.inputs[l];
                    count2 += 1;
                }
            }
            newTable.addRow(newRow);
        }
        return newTable.print();
    }

    private String selectBasic(String expr, String tableName) {
        Table curTable = db.get(tableName);
        String[] splitExpr;
        if (!expr.contains(",")) {
            splitExpr = new String[1];
            splitExpr[0] = expr;
        } else {
            expr = expr.replaceAll(" ", "");
            splitExpr = expr.split(",");

        }
        String[] colHeader = new String[splitExpr.length];
        for (int i = 0; i < colHeader.length; i++) {
            //System.out.println("splitExpr " + splitExpr[i]);
            int index = curTable.getIndex(splitExpr[i]);
            //System.out.println("index " + index);
            String type = curTable.getType(index);
            //System.out.println("type "  + type);
            colHeader[i] = splitExpr[i] + " " + type;
            //System.out.println("colHeader " + colHeader[i]);
        }

        Table newTable = new Table("temp", colHeader);
        for (int i = 0; i < curTable.rows.size(); i++) {
            Object[] temp = new Object[colHeader.length];
            for (int j = 0; j < colHeader.length; j++) {
                temp[j] = curTable.rows.get(i).getObj(curTable.getIndex(splitExpr[j]));
            }
            newTable.addRow(temp);
        }
        return newTable.print();
    }


    private boolean checkComparator(String exprs) {
        if (exprs.contains(">")) {
            return true;
        } else if (exprs.contains("<")) {
            return true;
        } else if (exprs.contains(">=")) {
            return true;
        } else if (exprs.contains("<=")) {
            return true;
        } else if (exprs.contains("!=")) {
            return true;
        } else if (exprs.contains("==")) {
            return true;
        }
        return false;
    }

    private String getComparator(String exprs) {
        //System.out.println("exprs " + exprs);
        if (exprs.contains(">=")) {
            //System.out.println("HERE1");
            return ">=";
        } else if (exprs.contains("<=")) {
            return "<=";
        } else if (exprs.contains(">")) {
            //System.out.println("HERE2");
            return ">";
        } else if (exprs.contains("<")) {
            return "<";
        } else if (exprs.contains("==")) {
            return "==";
        } else {
            return "!=";
        }
    }

    private boolean checkComparatorTypes(String type1, String type2, String comparator) {
        if ((type1.equals("string")) && (type2.equals("string"))) {
            return true;
        } else {
            return !(type1.equals("string") || type2.equals("string"));
        }
    }

    private boolean applyComparatorString(Object first, Object second, String comparator) {
        String firstString = (String) first;
        firstString = firstString.trim();
        String secondString = (String) second;
        secondString = secondString.trim();
        //check Nan string
        if (firstString.equals("NaN") && secondString.equals("NaN")) {
            return comparator.equals("==");
        } else if (firstString.equals("NaN")) {
            return comparator.equals(">") || comparator.equals(">=") || comparator.equals("!=");
        } else if (secondString.equals("NaN")) {
            return comparator.equals("<") || comparator.equals("<=") || comparator.equals("!=");
        }

        int compared = firstString.compareTo(secondString);
        //System.out.println("compared "+ compared + " " + firstString + "" + second);
        if (comparator.equals(">") && compared > 0) {
            //System.out.println("comparing strings > " + compared);
            return true;
        } else if (comparator.equals("<") && compared < 0) {
            return true;
        } else if (comparator.equals(">=") && compared >= 0) {
            return true;
        } else if (comparator.equals("<=") && compared <= 0) {
            return true;
        } else if (comparator.equals("==") && compared == 0) {
            return true;
        } else {
            return comparator.equals("!=") && compared != 0;
        }
    }

    private boolean applyComparatorInts(Object first, Object second, String comparator) {
        if (first.equals("NaN") && second.equals("NaN")) {
            return comparator.equals("==");
        } else if (first.equals("NaN")) {
            return comparator.equals(">") || comparator.equals(">=") || comparator.equals("!=");
        } else if (second.equals("NaN")) {
            return comparator.equals("<") || comparator.equals("<=") || comparator.equals("!=");
        } else if (comparator.equals(">") && ((int) first > (int) second)) {
            return true;
        } else if (comparator.equals("<") && ((int) first < (int) second)) {
            return true;
        } else if (comparator.equals(">=") && ((int) first >= (int) second)) {
            return true;
        } else if (comparator.equals("<=") && ((int) first <= (int) second)) {
            return true;
        } else if (comparator.equals("==") && ((int) first == (int) second)) {
            return true;
        } else {
            return comparator.equals("!=") && ((int) first != (int) second);
        }
    }

    private boolean applyComparatorDiffNums(Object first, Object second, String comparator) {
        if (first.equals("NaN") && second.equals("NaN")) {
            return comparator.equals("==");
        } else if (first.equals("NaN")) {
            return comparator.equals(">") || comparator.equals(">=") || comparator.equals("!=");
        } else if (second.equals("NaN")) {
            return comparator.equals("<") || comparator.equals("<=") || comparator.equals("!=");
        } else if (comparator.equals(">") && ((int) first > (float) second)) {
            return true;
        } else if (comparator.equals("<") && ((int) first < (float) second)) {
            return true;
        } else if (comparator.equals(">=") && ((int) first >= (float) second)) {
            return true;
        } else if (comparator.equals("<=") && ((int) first <= (float) second)) {
            return true;
        } else if (comparator.equals("==") && ((int) first == (float) second)) {
            return true;
        } else {
            return comparator.equals("!=") && ((int) first != (float) second);
        }
    }
    private boolean applyComparatorFloats(Object first, Object second, String comparator) {
        if (first.equals("NaN") && second.equals("NaN")) {
            return comparator.equals("==");
        } else if (first.equals("NaN")) {
            return comparator.equals(">") || comparator.equals(">=") || comparator.equals("!=");
        } else if (second.equals("NaN")) {
            return comparator.equals("<") || comparator.equals("<=") || comparator.equals("!=");
        } else if (comparator.equals(">") && ((float) first > (float) second)) {
            return true;
        } else if (comparator.equals("<") && ((float) first < (float) second)) {
            return true;
        } else if (comparator.equals(">=") && ((float) first >= (float) second)) {
            return true;
        } else if (comparator.equals("<=") && ((float) first <= (float) second)) {
            return true;
        } else if (comparator.equals("==") && ((float) first == (float) second)) {
            return true;
        } else {
            return comparator.equals("!=") && ((float) first != (float) second);
        }
    }


    private String switchSign(String comparator) {
        if (comparator.equals(">")) {
            return "<";
        } else if (comparator.equals("<")) {
            return ">";
        } else if (comparator.equals(">=")) {
            return "<=";
        } else if (comparator.equals("<=")) {
            return ">=";
        } else if (comparator.equals("==")) {
            return "!=";
        } else {
            return "==";
        }
    }

    private boolean applyCompare(Object f, Object s, String t1, String t2, String comp, boolean u) {
        if (u) {
            String secondString = (String) s;
            secondString = secondString.trim();
            if (t2.equals("int")) {
                s = Integer.parseInt(secondString);
            } else if (t2.equals("float")) {
                s = Float.parseFloat(secondString);
            } else {
                s = s;
            }
        }
        //System.out.println ("type1 " + type1 + " type2 " + type2);
        if ((t1.equals("string")) && (t2.equals("string"))) {
            return applyComparatorString(f, s, comp);
        } else if ((t1.equals("int")) && (t2.equals("int"))) {
            return applyComparatorInts(f, s, comp);
        } else if ((t1.equals("int")) && (t2.equals("float"))) {
            return applyComparatorDiffNums(f, s, comp);
        } else if ((t1.equals("float")) && (t2.equals("int"))) {
            return applyComparatorDiffNums(s, f, switchSign(comp));
        } else {
            return applyComparatorFloats(f, s, comp);
        }
    }

    private String checkLiteralType(String second) {
        if (second.equals("NOVALUE")) {
            return "";
        } else if (second.matches(".*\\d+.*")
                && !second.matches(".*[a-zA-Z]+.*")
                && !second.contains("'")) {
            if (second.contains(".")) {
                return "float";
            } else {
                return "int";
            }
        } else if (second.contains("'")) {
            return "string";
        } else {
            return "column";
        }
    }

    private String applyConditional(Table table, String conds) {
        String[] colHeaders = table.getColHeaders();
        Table newTable = new Table("temp", colHeaders);
        Set<Integer> skip = new HashSet<Integer>();
        String[] splitConds = conds.split(" and ");
        for (String cond: splitConds) {
            if (!checkComparator(cond)) {
                return "ERROR: Malformed conditional, requires comparator";
            }
            String comparator = getComparator(cond);
            //System.out.println("comparator" + comparator);
            String[] condSplit = cond.split("\\" + comparator);
            if (condSplit.length != 2) {
                return "ERROR: Malformed conditional, requires comparator";
            }
            if (condSplit[0] == null || condSplit[1] == null) {
                return "ERROR: Malformed condition, you need two things to compare";
            }
            String firstColName = condSplit[0];
            String second = condSplit[1];
            String type2 = checkLiteralType(second);
            if (type2.contains("ERROR:")) {
                return type2;
            }
            if (type2.equals("column")) {
                //BINARY
                String secondColName = second;
                //System.out.println("firstCol" + firstColName);
                int index1 = table.getIndex(firstColName);
                String type1 = table.getType(index1);
                //System.out.println("secondCol" + secondColName);
                int index2 = table.getIndex(secondColName);
                type2 = table.getType(index2);
                for (int i = 0; i < table.rows.size(); i++) {
                    Object firstObject = table.rows.get(i).getObj(index1);
                    Object secondObject = table.rows.get(i).getObj(index2);
                    boolean unary = false;
                    if (!applyCompare(firstObject, secondObject, type1, type2, comparator, unary)) {
                        skip.add(i);
                    }
                }
                //UNARY
            } else {
                int index1 = table.getIndex(firstColName);
                String type1 = table.getType(index1);
                for (int i = 0; i < table.rows.size(); i++) {
                    Object firstObject = table.rows.get(i).getObj(index1);
                    boolean unary = true;
                    if (!applyCompare(firstObject, second, type1, type2, comparator, unary)) {
                        skip.add(i);
                    }
                }
            }
        }
        //Adds rows to newTable from old table that does follow conditional
        for (int i = 0; i < table.rows.size(); i++) {
            if (!skip.contains(i)) {
                newTable.addRow(table.rows.get(i).inputs);
            }
        }
        return newTable.print();
    }
    ///** EXPRESSIONS ///

    private boolean checkOperator(String exprs) {
        if (exprs.contains("+")) {
            return true;
        } else if (exprs.contains("*")) {
            return true;
        } else if (exprs.contains("/")) {
            return true;
        } else if (exprs.contains("-")) {
            return true;
        }
        return false;
    }

    private String getOperator(String exprs) {
        if (exprs.contains("+")) {
            return "+";
        } else if (exprs.contains("*")) {
            return "*";
        } else if (exprs.contains("/")) {
            return "/";
        } else if (exprs.contains("-")) {
            return "-";
        } else {
            return "";
        }
    }

    private boolean checkTypes(String type1, String type2, String check) {
        if (type1.equals("string") && type2.equals("string") && check.equals("+")) {
            //System.out.println("strings and plus" + type1 + type2 + check);
            return true;
        } else {
            return !(type1.equals("string") || type2.equals("string"));
        }
    }

    private int applyOperatorInt(Object first, Object second, String operator) {
        int returnThis;
        if (operator.equals("+")) {
            returnThis = (int) first + (int) second;
        } else if (operator.equals("*")) {
            returnThis = (int) first * (int) second;
        } else if (operator.equals("/")) {
            returnThis = (int) first / (int) second;
        } else {
            returnThis = (int) first - (int) second;
        }
        return returnThis;
    }

    private float applyOperatorFloat(Object first, Object second, String op) {
        float returnThis;
        if (op.equals("+")) {
            returnThis = (float) first + (float) second;
        } else if (op.equals("*")) {
            returnThis = (float) first * (float) second;
        } else if (op.equals("/")) {
            returnThis =  (float) first /  (float) second;
        } else {
            returnThis =  (float) first -  (float) second;
        }
        return returnThis;
    }

    private float applyOperatorIntFloat(Object first, Object second, String op, boolean switched) {
        float returnThis;
        if (op.equals("+")) {
            returnThis = (int) first + (float) second;
        } else if (op.equals("*")) {
            returnThis = (int) first * (float) second;
        } else if (op.equals("/")) {
            if (switched) {
                returnThis = (float) second / (int) first;
            } else {
                returnThis = (int) first / (float) second;
            }
        } else {
            returnThis = (int) first -  (float) second;
        }
        return returnThis;
    }

    private Object checkNan(Object first, Object sec, String t1, String t2, String op, Table tbl) {
        if (first.equals("NaN") || sec.equals("NaN")) {
            if (t1.equals(t2)) {
                tbl.types[tbl.getNumColumns() - 1] = t1;
                tbl.indexToString.put(tbl.getNumColumns() - 1, t1);
                return "NaN";
            } else if (t1.equals("int") || t2.equals("int")) {
                tbl.types[tbl.getNumColumns() - 1] = "int";
                tbl.indexToString.put(tbl.getNumColumns() - 1, "int");
                return "NaN";
            }
        }
        if (op.equals("/")) {
            if (t2.equals("int") && t1.equals("int")) {
                if (((int) sec) == 0) {
                    tbl.types[tbl.getNumColumns() - 1] = "int";
                    tbl.indexToString.put(tbl.getNumColumns() - 1, "int");
                    return "NaN";
                }
            } else if (t1.equals("float") && t2.equals("int")) {
                if ((int) sec == 0) {
                    tbl.types[tbl.getNumColumns() - 1] = "float";
                    tbl.indexToString.put(tbl.getNumColumns() - 1, "float");
                    return "NaN";
                }
            } else {
                if ((float) sec == 0.0) {
                    tbl.types[tbl.getNumColumns() - 1] = "float";
                    tbl.indexToString.put(tbl.getNumColumns() - 1, "float");
                    return "NaN";
                }
            }
        }
        return applyOp(first, sec, t1, t2, op, tbl);
    }

    private Object checkNoValue(Object obj, String type) {
        if (obj.equals("NOVALUE")) {
            if (type.equals("int")) {
                return 0;
            } else if (type.equals("float")) {
                return 0.0;
            } else {
                return "";
            }
        } else {
            return obj;
        }
    }
    private String applyOperatorString(Object first, Object second, String operator) {
        String returnThis = ((String) first +  (String) second);
        returnThis = returnThis.replaceAll("''", "");
        return returnThis;
    }

    private Object applyOp(Object one, Object two, String type, String type2, String op, Table tb) {
        if (type.equals("int") && type2.equals("int")) {
            tb.types[tb.getNumColumns() - 1 ] = "int";
            tb.indexToString.put(tb.getNumColumns() - 1, "int");
            return applyOperatorInt(one, two, op);
        } else if (type.equals("string") && type2.equals("string") && op.equals("+")) {
            tb.types[tb.getNumColumns() - 1] = "string";
            tb.indexToString.put(tb.getNumColumns() - 1, "string");
            return applyOperatorString(one, two, op);
        } else if (type.equals("int") && type2.equals("float")) {
            tb.types[tb.getNumColumns() - 1] = "float";
            tb.indexToString.put(tb.getNumColumns() - 1, "float");
            return applyOperatorIntFloat(one, two, op, false);
        } else if (type.equals("float") && type2.equals("int")) {
            tb.types[tb.getNumColumns() - 1] = "float";
            tb.indexToString.put(tb.getNumColumns() - 1, "float");
            return applyOperatorIntFloat(two, one, op, true);
        } else {
            tb.types[tb.getNumColumns() - 1] = "float";
            tb.indexToString.put(tb.getNumColumns() - 1, "float");
            return applyOperatorFloat(one, two, op);
        }
    }

    private String computeExpression(String exprs, Table table) {
        String check = getOperator(exprs);
        if (check.equals("")) {
            return "ERROR: requires operator to evaluate";
        }
        String[] colsNewCol = exprs.split(" as ");
        if (colsNewCol.length != 2) {
            return "ERROR: need AS statement";
        }
        String newColName = colsNewCol[1];
        exprs = colsNewCol[0];
        exprs = exprs.replaceAll(" ", "");
        //System.out.println("exprs" + exprs);
        String[] splitExprs = exprs.split("\\" + check);
        if (splitExprs.length != 2) {
            return "ERROR: Need two ColNames";
        }
        if (splitExprs[0] == null || splitExprs[1] == null) {
            return "ERROR: Need two colNames to apply operator";
        }
        //System.out.println("check " + check);
        //System.out.println("splitExprs[0]" + splitExprs[0]);
        String type1 = table.getType(splitExprs[0]);
        //System.out.println("type1 " + type1);
        String type2 = table.getType(splitExprs[1]);
        //System.out.println("type2 " + type2);
        //System.out.println("checkTypes " + checkTypes(type1, type2, check));
        if (!checkTypes(type1, type2, check)) {
            //System.out.println("GOT HERE");
            return "ERROR: Types are not valid for arithmetic" + type1 + type2;
        }
        int index1 = table.getIndex(splitExprs[0]);
        int index2 = table.getIndex(splitExprs[1]);
        table.addColumnHeader(newColName);
        for (int i = 0; i < table.rows.size(); i++) {
            Row currentRow = table.rows.get(i);
            Object first = currentRow.inputs[index1];
            Object second = currentRow.inputs[index2];
            first = checkNoValue(first, type1);
            second = checkNoValue(second, type2);
            Object addThis = checkNan(first, second, type1, type2, check, table);
            currentRow.addColumn(addThis);
        }
        return newColName;
    }

    private String checkNumeric(String exprs, Table table) {
        String selectExpr = "";
        String[] separateExprs = exprs.split(",");
        if (!checkOperator(separateExprs[0])) {
            selectExpr = separateExprs[0];
        } else {
            String computed = computeExpression(separateExprs[0], table);
            if (computed.contains("ERROR:")) {
                return computed;
            }
            selectExpr = computed;
        }
        for (int i = 1; i < separateExprs.length; i++) {
            if (!checkOperator(separateExprs[i])) {
                selectExpr = selectExpr + ", " + separateExprs[i];
            } else {
                String computed = computeExpression(separateExprs[i], table);
                if (computed.contains("ERROR:")) {
                    return computed;
                }
                selectExpr = selectExpr + ", " + computed;
            }
        }
        return selectBasic(selectExpr, table.getName());
    }


    private String selectAllBasic(String tableName) {
        Table curTable = db.get(tableName);
        return curTable.print();
    }

    private String select(String exprs, String tables, String conds) {
        System.out.printf("You are trying to select these expressions:"
                + " '%s' from the join of these tables: '%s', filtered by "
                + "these conditions: '%s'\n", exprs, tables, conds);
        if (conds != null) {
            Table applyTable;
            if (exprs.equals("*") && !tables.contains(",")) {
                Map<String, Table> map = createSelectAllBasic(tables);
                for (String k: map.keySet()) {
                    if (!k.equals("")) {
                        return k;
                    }
                    applyTable = map.get(k);
                    return applyConditional(applyTable, conds);
                }
            } else if (exprs.equals("*")) {
                Map<String, Table> map = createJoinBasic(exprs, tables, conds);
                for (String k: map.keySet()) {
                    if (!k.equals("")) {
                        return k;
                    }
                    applyTable = map.get(k);
                    return applyConditional(applyTable, conds);
                }
            } else if (checkOperator(exprs)) {
                Table curTable = db.get(tables);
                Map<String, Table> map = createCheckNumeric(exprs, curTable);
                for (String k: map.keySet()) {
                    if (!k.equals("")) {
                        return k;
                    }
                    applyTable = map.get(k);
                    return applyConditional(applyTable, conds);
                }
            } else if (!tables.contains(",")) {
                Map<String, Table> map = createSelectBasic(exprs, tables);
                for (String k: map.keySet()) {
                    if (!k.equals("")) {
                        return k;
                    }
                    applyTable = map.get(k);
                    return applyConditional(applyTable, conds);
                }
            } else {
                return "ERROR: not valid selectOp" + exprs + tables;
            }
        } else if (exprs.equals("*") && !tables.contains(",")) {
            return selectAllBasic(tables);
        } else if (exprs.equals("*")) {
            return joinBasic(exprs, tables, conds);
        } else if (checkOperator(exprs)) {
            Table curTable = db.get(tables);
            return checkNumeric(exprs, curTable);
        } else if (!tables.contains(",")) {
            return selectBasic(exprs, tables);
        }
        return "";

    }
}
