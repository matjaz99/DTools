package si.matjazcerkvenik.dtools.web.webhook;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CollectionDeserializer implements JsonDeserializer<Collection<?>> {

    @Override
    public Collection<?> deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        Type realType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];

        return parseAsArrayList(json, realType);
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> parseAsArrayList(JsonElement json, T type) {
        ArrayList<T> newArray = new ArrayList<T>();
        Gson gson = new Gson();

        JsonArray array= json.getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();

        while(iterator.hasNext()){
            JsonElement json2 = (JsonElement)iterator.next();
            T object = (T) gson.fromJson(json2, (Class<?>)type);
            newArray.add(object);
        }

        return newArray;
    }

}