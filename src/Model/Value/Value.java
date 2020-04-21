package Model.Value;

import Model.Type.Type;

public interface Value {
    public Type getType();
    public String toString();
    public Value dup();
    public boolean equals(Object o);
}
