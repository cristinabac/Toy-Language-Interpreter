package Model.ProgramState;

import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyStack() {
        stack = new Stack<T>();
    }

    @Override
    public T pop() {
        return stack.pop();
    }

    @Override
    public T top() {
        return stack.peek();
    }

    @Override
    public void push(T el) {
        stack.push(el);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Stack<T> getAll() {
        return stack;
    }

    @Override
    public String toString() {
        //return stack.toString();
        StringBuilder rez = new StringBuilder();
        Stack<T> newStk = new Stack<T>();
        while(!stack.isEmpty()){
            T obj = stack.pop();
            newStk.push(obj);
            rez.append(obj.toString()).append("\n");
        }
        while(!newStk.isEmpty()){
            T obj = newStk.pop();
            stack.push(obj);
        }
        return rez.toString();
    }
}
