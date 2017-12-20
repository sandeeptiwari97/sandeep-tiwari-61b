package db;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Row {
    Object[] inputs;
    public Row(Object[] inputs) {
//		List<Object> row = new ArrayList<Object>();
        this.inputs = inputs;
    }
    public Object getObj(int index) {
        return inputs[index];
    }

    public void addColumn(Object addThis) {
        Object[] newInputs = new Object[inputs.length + 1];
        System.arraycopy(inputs,0, newInputs, 0, inputs.length);
        newInputs[inputs.length] = addThis;
        inputs = newInputs;
    }

}
