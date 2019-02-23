package handler;

import ui.ContentManager;

public abstract class AbstractEventHandler<T> {
    
    protected ContentManager manager;
    
    protected AbstractEventHandler(ContentManager manager) {
        this.manager = manager;
    }

    public abstract void pause();
    public abstract void resume();
    
}
