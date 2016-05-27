package data;

import org.json.JSONObject;

/**
 * メソッドリストの1行分のデータを保持
 */
public class Data {
    private final JSONObject data;

    Data(JSONObject data) {
        this.data = data;
    }

    public String getClassName() {
        return data.getString("class");
    }

    public String getType() {
        return data.getString("type");
    }

    public String getCompletion() {
        return data.getString("completion");
    }

    public int getParamCount() {
        return data.getInt("paramCount");
    }

    public String getReturnClassName() {
        return data.getString("return");
    }
}
