package cgellner.mytodo.database;

import cgellner.mytodo.model.User;

/**
 * Created by Carolin on 16.06.2017.
 */
public interface IRemoteInit {

    public boolean authorizeUser(User user);
    public boolean isConnected();

}
