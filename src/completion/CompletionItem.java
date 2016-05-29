package completion;

import org.json.JSONObject;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 補完候補の1データを管理
 */
public class CompletionItem {
    private final JSONObject data;

    CompletionItem(JSONObject data) {
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
     * @return タイプのアイコン
     */
    public Icon getTypeIcon() {
        return new ImageIcon("img/" + getType() + ".png");
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

    /**
     * @param lang 説明の言語 en または ja ToDo 説明文の日本語対応したい
     * @return 説明文
     */
    public String getDescription(String lang) {
        return data.getJSONObject("description").getString(lang);
    }
}
