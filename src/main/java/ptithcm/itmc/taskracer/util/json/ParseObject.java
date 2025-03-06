package ptithcm.itmc.taskracer.util.json;

public class ParseObject {
    public static <T> T parse(Object object, Class<T> clazz) {
        return clazz.cast(object);
    }
}
