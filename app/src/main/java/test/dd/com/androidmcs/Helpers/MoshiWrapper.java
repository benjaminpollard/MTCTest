package test.dd.com.androidmcs.Helpers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import test.dd.com.androidmcs.Repository.Interfaces.IJsonConverter;

//generic for re-useabiliy
public class MoshiWrapper implements IJsonConverter {

    public <T extends Object> String ToJson(T object , Class type)
    {

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> jsonAdapter = moshi.adapter(type);

       return jsonAdapter.toJson(object);
    }

    public <T extends Object> T FromJson(String json , Class<T> type) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> jsonAdapter = moshi.adapter(type);

        return jsonAdapter.fromJson(json);
    }
    //type changes if  no object , returns string instead {}
    public String inconsistentTypeWorkAround(String jsonResult) throws JSONException {

        JSONObject jsonObj = new JSONObject(jsonResult);
        JSONObject r = jsonObj.getJSONObject("result");
        Object b = r.get("players");
        boolean badTypeFound = false;
        if( r.has("players") && b.equals(""))
        {
            r.remove("players");
            badTypeFound = true;
        }
        if(r.has("teams") && r.get("teams").equals(""))
        {
            r.remove("teams");
            badTypeFound = true;

        }
        if(badTypeFound)
        {
            jsonObj.remove("result");
            jsonObj.put("result", r);
        }
        return jsonObj.toString();
    }


}
