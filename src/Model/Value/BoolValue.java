package Model.Value;

import Model.Type.BoolType;
import Model.Type.Type;

public class BoolValue implements Value {
    private boolean val;
    public BoolValue(boolean v){val=v;}

    public boolean getVal() {return val;}
    public String toString() { return Boolean.toString(val);}

    public boolean equals(Object another){
        if (another instanceof BoolType) {
            //the 2 are of the same type
            //check the actual value
            BoolValue anotherValue = (BoolValue)another;
            boolean anotherVal = anotherValue.getVal();
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
        return new BoolValue(val);
    }

    public Type getType() { return new BoolType();}
}
