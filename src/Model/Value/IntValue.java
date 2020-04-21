package Model.Value;

import Model.Type.IntType;
import Model.Type.Type;

public class IntValue implements Value {
    private int val;
    public IntValue(int v){val=v;}

    public int getVal() {return val;}
    public String toString() { return Integer.toString(val);}

    public boolean equals(Object another){
        if (another instanceof IntType) {
            //the 2 are of the same type
            //check the actual value
            IntValue anotherValue = (IntValue)another; //downcast
            int anotherVal = anotherValue.getVal();
            if(anotherVal == val)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    @Override
    public Value dup() {
        return new IntValue(val);
    }

    public Type getType() { return new IntType();}
}
