package at.flori4n.ctf;

public interface State {
    public void preaction();
    public void action();
    public void postAction();
}
