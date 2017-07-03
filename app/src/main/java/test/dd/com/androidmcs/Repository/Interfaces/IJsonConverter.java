package test.dd.com.androidmcs.Repository.Interfaces;

import org.json.JSONException;

import java.io.IOException;
//generic for re-useabiliy
public interface IJsonConverter {
    <T extends Object> String ToJson(T object , Class type);
    <T> T FromJson(String json , Class<T> type) throws IOException;
    String inconsistentTypeWorkAround(String jsonResult) throws JSONException;
}
