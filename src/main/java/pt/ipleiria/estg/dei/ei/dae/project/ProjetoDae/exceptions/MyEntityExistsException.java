package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions;

public class MyEntityExistsException extends Exception{
    public MyEntityExistsException () {

    }

    public MyEntityExistsException (String msg){
        super(msg);
    }
}


