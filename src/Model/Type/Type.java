package Model.Type;

import Model.Value.Value;

public interface Type {
    public String toString();
    public boolean equals(Object o);
    public Value defaultValue();
}
