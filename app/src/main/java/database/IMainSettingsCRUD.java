package database;

/**
 * Created by Carolin on 21.05.2017.
 */
public interface IMainSettingsCRUD {

    long createMainSettings();
    int readSortMode();
    boolean updateSortMode(int mode);


}
