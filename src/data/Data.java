package data;

/**
 * メソッドリストの1行分のデータを保持
 */
public class Data {
    public final String className;
    public final String methodName;
    public final String returnClassName;

    Data(String className, String methodName, String returnClassName) {
        this.className = className;
        this.methodName = methodName;
        this.returnClassName = returnClassName;
    }
}
