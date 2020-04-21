package View;

import Controller.Controller;
import Model.Expression.ArithExp;
import Model.Expression.HeapExpressions.ReadHeapExp;
import Model.Expression.RelExp;
import Model.Expression.ValueExp;
import Model.Expression.VarExp;
import Model.ProgramState.*;
import Model.Statements.*;
import Model.Statements.HeapStatements.NewStmt;
import Model.Statements.HeapStatements.WriteHeapStmt;
import Model.Statements.SemaphoreStatements.AcquireStmt;
import Model.Statements.SemaphoreStatements.CreateSemaphoreStmt;
import Model.Statements.SemaphoreStatements.ReleaseStmt;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.RefType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Repository.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Interpreter {
    private static IStmt concatStatements(IStmt... statements) {
        if (statements.length == 1)
            return statements[0];

        return new CompStmt(statements[0], concatStatements(Arrays.copyOfRange(statements, 1, statements.length)));
    }

    public static ArrayList<IStmt> getAllExamples(){
        IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new
                        VarExp("v"))));

        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new
                                ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new
                                        ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new
                                        IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));

        IStmt ex4 = new CompStmt(new VarDeclStmt("varf",new StringType()),
                new CompStmt(new AssignStmt("varf",new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new VarDeclStmt("varc",new IntType()),
                                new CompStmt(new openRFileStmt(new VarExp("varf")),
                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                new CompStmt( new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),new closeRFileStmt(new VarExp("varf"))))))))));

        IStmt ex5 = new CompStmt(new VarDeclStmt("s",new StringType()),
                new CompStmt(new AssignStmt("s",new ValueExp(new StringValue("ana"))),new PrintStmt(new VarExp("s"))));


        IStmt ex6 =  new CompStmt(new VarDeclStmt("a",new IntType()),new CompStmt(new NopStmt(),new CompStmt(
                new VarDeclStmt("b",new BoolType()), new CompStmt( new AssignStmt("b",new ValueExp(new BoolValue(true))),
                new CompStmt(new IfStmt(new VarExp("b"),new CompStmt(new AssignStmt("a",new ValueExp(new IntValue(10))),new AssignStmt("b",new ValueExp(new BoolValue(false)))),
                        new CompStmt(new AssignStmt("a",new ValueExp(new IntValue(-10))),new AssignStmt("b",new ValueExp(new BoolValue(false))))),
                        new CompStmt(new PrintStmt(new VarExp("a")),new PrintStmt(new VarExp("b"))))))));


        IStmt ex7 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new RelExp(new VarExp("v"),new ValueExp(new IntValue(1)),">"),new AssignStmt("v",new ValueExp(new
                                        IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));

        IStmt ex8 = new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelExp(new VarExp("v"),new ValueExp(new IntValue(0)),">"),
                                new CompStmt(new PrintStmt(new VarExp("v")),new AssignStmt("v",new ArithExp('-',new VarExp("v"),new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        IStmt ex9 = new CompStmt(new VarDeclStmt("v",new RefType(new IntType())),
                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a",new RefType(new RefType(new IntType()))),
                                new CompStmt(new NopStmt(),new CompStmt(
                                        new NewStmt("v",new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a"))))
                                )))));

        IStmt ex10 = new CompStmt(new VarDeclStmt("v",new RefType(new IntType())),
                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(40))),new PrintStmt(new ReadHeapExp(new VarExp("v")))))));

        IStmt ex11 =  new CompStmt(new VarDeclStmt("v",new RefType(new IntType())),
                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a",new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a",new VarExp("v")),new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                        new PrintStmt(new ArithExp('+',new ReadHeapExp(new ReadHeapExp(new VarExp("a"))),new ValueExp(new IntValue(5)))))))));

        IStmt ex12 = new CompStmt(new VarDeclStmt("v",new RefType(new IntType())),
                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new WriteHeapStmt("v",new ValueExp(new IntValue(30))),
                                new PrintStmt(new ArithExp('+',new ReadHeapExp(new VarExp("v")),new ValueExp(new IntValue(5)))))));

        IStmt ex13 = new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new ArithExp('+',new VarExp("v"),new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),new AssignStmt("v",new ArithExp('-',new VarExp("v"),new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        IStmt ex14 = new CompStmt(new VarDeclStmt("v",new IntType()),new CompStmt(new VarDeclStmt("a",new RefType(new IntType())),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(10))),new CompStmt(new NewStmt("a",new ValueExp(new IntValue(22))),
                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("a",new ValueExp(new IntValue(30))),
                                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(32))), new CompStmt(new PrintStmt(new VarExp("v")),new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                new CompStmt(new PrintStmt(new VarExp("v")),new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));

        IStmt ex15 = new CompStmt(new VarDeclStmt("a",new IntType()),new ForkStmt(new PrintStmt(new VarExp("s"))));

        IStmt ex16 = new CompStmt(new VarDeclStmt("a",new IntType()), new CompStmt(new VarDeclStmt("b",new IntType()),
                new CompStmt(new ForkStmt(new ForkStmt(new PrintStmt(new VarExp("a")))), new ForkStmt(new PrintStmt(new VarExp("b"))))));

        //int a
        //int b
        //int c
        //fork(fork(print a))
        //fork(print b)
        //print c
        IStmt ex17  = new CompStmt(new VarDeclStmt("a", new IntType()), new CompStmt( new VarDeclStmt("b",new IntType()),
                new CompStmt(new VarDeclStmt("c",new IntType()), new CompStmt(new ForkStmt(new ForkStmt(new PrintStmt(new VarExp("a")))),
                        new CompStmt(new ForkStmt(new PrintStmt(new VarExp("b"))), new PrintStmt(new VarExp("c")))))));

        //string s; s=ana; print(a);   => typecheck err
        IStmt ex18 = new CompStmt(new VarDeclStmt("s",new StringType()),
                new CompStmt(new AssignStmt("s",new ValueExp(new StringValue("ana"))),new PrintStmt(new VarExp("a"))));

        //typecheck err ->openRFileStmt(new VarExp("varc")
        IStmt ex19 = new CompStmt(new VarDeclStmt("varf",new StringType()),
                new CompStmt(new AssignStmt("varf",new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new VarDeclStmt("varc",new IntType()),
                                new CompStmt(new openRFileStmt(new VarExp("varc")),
                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                new CompStmt( new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),new closeRFileStmt(new VarExp("varf"))))))))));

        //typecheck err -> closeRFileStmt(new VarExp("varc")
        IStmt ex20 = new CompStmt(new VarDeclStmt("varf",new StringType()),
                new CompStmt(new AssignStmt("varf",new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new VarDeclStmt("varc",new IntType()),
                                new CompStmt(new openRFileStmt(new VarExp("varf")),
                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                new CompStmt( new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),new closeRFileStmt(new VarExp("varc"))))))))));

        //string s; s=1; print(s);   => typecheck err
        IStmt ex21 = new CompStmt(new VarDeclStmt("s",new StringType()),
                new CompStmt(new AssignStmt("s",new ValueExp(new IntValue(1))),new PrintStmt(new VarExp("s"))));

        /*
        int a;
        nop;
        bool b;
        b=true;
        if(a) THEN (a=10;b=false) ELSE (a=-10;b=true;)
        print(a);
        print(b);
         */
        //typecheck err  -> if(a)
        IStmt ex22 =  new CompStmt(new VarDeclStmt("a",new IntType()),new CompStmt(new NopStmt(),new CompStmt(
                new VarDeclStmt("b",new BoolType()), new CompStmt( new AssignStmt("b",new ValueExp(new BoolValue(true))),
                new CompStmt(new IfStmt(new VarExp("a"),new CompStmt(new AssignStmt("a",new ValueExp(new IntValue(10))),new AssignStmt("b",new ValueExp(new BoolValue(false)))),
                        new CompStmt(new AssignStmt("a",new ValueExp(new IntValue(-10))),new AssignStmt("b",new ValueExp(new BoolValue(false))))),
                        new CompStmt(new PrintStmt(new VarExp("a")),new PrintStmt(new VarExp("b"))))))));

        //tyepcheck err -> readFileStmt(new VarExp("varf"),"varf")
        IStmt ex23 = new CompStmt(new VarDeclStmt("varf",new StringType()),
                new CompStmt(new AssignStmt("varf",new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new VarDeclStmt("varc",new IntType()),
                                new CompStmt(new openRFileStmt(new VarExp("varf")),
                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varf"),
                                                new CompStmt( new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),new closeRFileStmt(new VarExp("varf"))))))))));


        //tyepcheck err -> readFileStmt(new VarExp("varc"),"varc")
        IStmt ex24 = new CompStmt(new VarDeclStmt("varf",new StringType()),
                new CompStmt(new AssignStmt("varf",new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new VarDeclStmt("varc",new IntType()),
                                new CompStmt(new openRFileStmt(new VarExp("varf")),
                                        new CompStmt(new readFileStmt(new VarExp("varc"),"varc"),
                                                new CompStmt( new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFileStmt(new VarExp("varf"),"varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),new closeRFileStmt(new VarExp("varf"))))))))));


        //typecheck err
        //int v; v=4; (while (v>0) print(a);v=v-1);print(v)
        IStmt ex25 = new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelExp(new VarExp("v"),new ValueExp(new IntValue(0)),">"),
                                new CompStmt(new PrintStmt(new VarExp("a")),new AssignStmt("v",new ArithExp('-',new VarExp("v"),new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        //typecheck err
        //int a
        //int b
        //int c
        //fork(fork(print s))
        //fork(print b)
        //print c
        IStmt ex26  = new CompStmt(new VarDeclStmt("a", new IntType()), new CompStmt( new VarDeclStmt("b",new IntType()),
                new CompStmt(new VarDeclStmt("c",new IntType()), new CompStmt(new ForkStmt(new ForkStmt(new PrintStmt(new VarExp("s")))),
                        new CompStmt(new ForkStmt(new PrintStmt(new VarExp("b"))), new PrintStmt(new VarExp("c")))))));


        IStmt ex27 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new ForkStmt(new CompStmt( new NewStmt("a", new ValueExp(new IntValue(420))), new PrintStmt(new ReadHeapExp(new VarExp("a"))))),
                        new ForkStmt(new CompStmt( new NewStmt("a", new ValueExp(new IntValue(7))), new PrintStmt(new ReadHeapExp(new VarExp("a")))))
                )
        );

        IStmt ex28=  new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new CompStmt(new NewStmt("a", new ValueExp(new IntValue(11))), new WriteHeapStmt("a", new ValueExp(new IntValue(6)))),
                        new CompStmt(
                                new ForkStmt(new CompStmt( new CompStmt(new NewStmt("a", new ValueExp(new IntValue(420))),new WriteHeapStmt("a", new ValueExp(new IntValue(400)))), new PrintStmt(new ReadHeapExp(new VarExp("a"))))),
                                new ForkStmt(new CompStmt( new CompStmt(new NewStmt("a", new ValueExp(new IntValue(420))),new WriteHeapStmt("a", new ValueExp(new IntValue(20)))), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))
                ));


        IStmt ex29 = concatStatements(
                new VarDeclStmt("v1", new RefType(new IntType())),
                new VarDeclStmt("cnt", new IntType()),
                new NewStmt("v1", new ValueExp(new IntValue(1))),
                new CreateSemaphoreStmt("cnt", new ReadHeapExp(new VarExp("v1"))),
                new ForkStmt(new CompStmt(new AcquireStmt("cnt"), new CompStmt(
                        new WriteHeapStmt("v1", new ArithExp('*', new ReadHeapExp(new VarExp("v1")), new ValueExp(new IntValue(10)))),
                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v1"))), new ReleaseStmt("cnt"))
                ))),
                new ForkStmt(new CompStmt(new AcquireStmt("cnt"), new CompStmt(
                        new WriteHeapStmt("v1", new ArithExp('*', new ReadHeapExp(new VarExp("v1")), new ValueExp(new IntValue(10)))),
                        new CompStmt(new WriteHeapStmt("v1", new ArithExp('*', new ReadHeapExp(new VarExp("v1")), new ValueExp(new IntValue(2)))),
                                new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v1"))), new ReleaseStmt("cnt"))
                        )))),
                new AcquireStmt("cnt"),
                new PrintStmt(new ArithExp('-', new ReadHeapExp(new VarExp("v1")),new ValueExp(new IntValue(1)))),
                new ReleaseStmt("cnt")

        );


        ArrayList<IStmt> arr = new ArrayList<>();
        arr.add(ex1);
        arr.add(ex2);
        arr.add(ex3);
        arr.add(ex4);
        arr.add(ex5);
        arr.add(ex6);
        arr.add(ex7);
        arr.add(ex8);
        arr.add(ex9);
        arr.add(ex10);
        arr.add(ex11);
        arr.add(ex12);
        arr.add(ex13);
        arr.add(ex14);
        arr.add(ex15);
        arr.add(ex16);
        arr.add(ex17);
        arr.add(ex18);
        arr.add(ex19);
        arr.add(ex20);
        arr.add(ex21);
        arr.add(ex22);
        arr.add(ex23);
        arr.add(ex24);
        arr.add(ex25);
        arr.add(ex26);
        arr.add(ex27);
        arr.add(ex28);
        arr.add(ex29);
        return arr;
    }

    public static void main(String[] args) {

    }
}
