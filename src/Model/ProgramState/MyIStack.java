package Model.ProgramState;

import java.util.Stack;

public interface MyIStack<T> {
    public T pop();
    public T top();
    public void push(T el);
    boolean isEmpty();
    public Stack<T> getAll();
}
