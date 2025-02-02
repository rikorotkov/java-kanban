package api.helper;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.TaskStatus;

import java.io.IOException;

public class TaskStatusAdapter extends TypeAdapter<TaskStatus> {
    @Override
    public void write(JsonWriter out, TaskStatus value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public TaskStatus read(JsonReader in) throws IOException {
        return TaskStatus.valueOf(in.nextString());
    }
}
