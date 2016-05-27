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

    /**
     * @return クラス名
     */
    public String getClassName() {
        return data.getString("class");
    }

    /**
     * @return MethodかProperty
     */
    public String getType() {
        return data.getString("type");
    }

    /**
     * @return 補完一覧に表示したりする文字列
     */
    public String getCompletion() {
        return data.getString("completion");
    }

    /**
     * @return パラメータの数 タイプがMethod以外なら一律0
     */
    public int getParamCount() {
        if (getType().equals("Method")) {
            return data.getInt("paramCount");
        } else {
            return 0;
        }
    }

    /**
     * @return 戻り値のクラス名
     */
    public String getReturnClassName() {
        return data.getString("return");
    }
}
