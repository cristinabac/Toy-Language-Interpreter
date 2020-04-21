package Model.Type;


import Model.Value.BoolValue;
import Model.Value.Value;

public class BoolType implements Type {
    public boolean equals(Object another){
        if (another instanceof BoolType)
            return true;
        else
            return false;
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }

    public String toString() { return "bool";}
}
