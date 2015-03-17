package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;


public abstract class BubbleDocsService {

    @Atomic
    public final void execute() throws BubbleDocsException {
    	accessControl();
        dispatch();
    }
    
    /* Verifies if the client has rights to do the operation. */
    protected abstract void accessControl() throws BubbleDocsException;
    
    protected abstract void dispatch() throws BubbleDocsException;
}