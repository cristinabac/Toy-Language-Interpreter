package Model;

public class MyException extends RuntimeException {
    private String msg;
    public MyException(String message){
        this.msg = message;
    }
    public String getMessage(){
        return msg;
    }
}
