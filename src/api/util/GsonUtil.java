package api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;

public class GsonUtil {
    private static final Gson gson = new GsonBuilder()
//            .registerTypeAdapter(TaskStatus.class, new TaskStatusAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
//            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .excludeFieldsWithoutExposeAnnotation()  // Это исключит поля без @Expose
            .create();

    public static Gson getGson() {
        return gson;
    }
}