package Model.Value;

import Model.Type.RefType;
import Model.Type.Type;

public class RefValue implements Value {
    private int address;
    private Type locationType;

    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress(){
        return this.address;
    }

    /*
    public boolean equals(Object another){
        if (another instanceof RefType) {
            //the 2 are of the same type
            //check the actual value
            RefValue anotherValue = (RefValue) another;
            if(anotherValue.getAddress() == address) //????????????????? enough ????
                return true;
            else
                return false;
        }
        else
            return false;
    }

     */

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    public Type getLocationType() {return locationType;}

    @Override
    public Value dup() {
        return new RefValue(address,locationType);
    }

    @Override
    public String toString() {
        return "(" + Integer.toString(address) + "," + locationType.toString() + ")";
    }
}
