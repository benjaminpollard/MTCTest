package test.dd.com.androidmcs.Repository;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import test.dd.com.androidmcs.Helpers.DatabaseUpdate;
import test.dd.com.androidmcs.Repository.Interfaces.IDatabaseRepository;
//use genrics for reuse fo class and database down the line

public class DatabaseRepository implements IDatabaseRepository {

    private Realm realm;

    public DatabaseRepository()
    {
        realm = Realm.getDefaultInstance();
    }


    public <T extends RealmObject> void UpdateOrInsertItem(T  item)
    {
        realm.beginTransaction();
        realm.insertOrUpdate(item);
        realm.commitTransaction();
    }
    public void Update(DatabaseUpdate update)
    {
        realm.beginTransaction();
        update.Update();
        realm.commitTransaction();
    }

    public <T extends RealmObject> T GetItem(Class<T> type)
    {
        RealmQuery<T> query = realm.where(type);
        return query.findFirst();
    }

    public <T extends RealmObject> RealmResults<T> GetItems(Class<T> type)
    {
        RealmQuery<T> query = realm.where(type);
        return  query.findAll();
    }

    public <T extends RealmObject> void SaveItem(T item)
    {
        realm.beginTransaction();
        realm.copyToRealm(item);
        realm.commitTransaction();
    }

}
