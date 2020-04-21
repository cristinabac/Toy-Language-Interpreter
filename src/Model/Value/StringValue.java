package Model.Value;

import Model.Type.StringType;
import Model.Type.Type;

public class StringValue implements Value {
    private String str;

    public StringValue(String string){
        this.str = string;
    }

    public String getVal(){
        return str;
    }

    public boolean equals(Object another){
        if (another instanceof StringType) {
            //the 2 are of the same type
            //check the actual value
            StringValue anotherValue = (StringValue) another;
            String anotherVal = anotherValue.getVal();
            if(anotherVal == str)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public Value dup() {
        return new StringValue(str);
    }

    @Override
    public String toString() {
        return str;
    }
}
