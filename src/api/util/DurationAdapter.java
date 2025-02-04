package api.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration duration) throws IOException {
        if (duration == null) {
            out.nullValue();
            return;
        }
        out.value(duration.toString()); // ISO-8601 формат, например "PT1H30M"
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        String durationString = in.nextString();
        return Duration.parse(durationString);
    }
}
