package test.dd.com.androidmcs.Repository.Interfaces;

import io.realm.RealmObject;
import io.realm.RealmResults;
import test.dd.com.androidmcs.Helpers.DatabaseUpdate;
//use genrics for reuse fo class and database down the line
public interface IDatabaseRepository {
    <T extends RealmObject> void UpdateOrInsertItem(T  item);
    <T extends RealmObject> T GetItem(Class<T> type);
    <T extends RealmObject> RealmResults<T> GetItems(Class<T> type);
    <T extends RealmObject> void SaveItem(T item);
    <T extends RealmObject> void Update(DatabaseUpdate update);

}
