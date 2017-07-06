package cgellner.mytodo.database.interfaces;

import cgellner.mytodo.model.User;

/**
 * Created by Carolin on 16.06.2017.
 */
public interface IRemoteInitAsync {

    public static interface CallbackFunction<T>{

        public void process (T result);
    }

    public void authorizeUser(User user, CallbackFunction<Boolean> callback);
    public void isConnected(CallbackFunction<Boolean> callback);
}
