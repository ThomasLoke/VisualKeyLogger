package handler;

import ui.ContentManager;

public abstract class AbstractEventHandler<M extends ContentManager, T> {
    
    protected M manager;
    
    protected AbstractEventHandler(M manager) {
        this.manager = manager;
    }

    public abstract void pause();
    public abstract void resume();
    
}
