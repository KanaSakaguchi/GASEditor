package completion;

import org.json.JSONObject;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Created by sakaguchikana on 2016/05/27.
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

    public JMenuItem getItem(Document document, int location) {
        JMenuItem item = new JMenuItem(getCompletion());
        item.addActionListener(event -> {
            String menu = ((JMenuItem) event.getSource()).getText();
            try {
                if (getReturnClassName().equals("void")) {
                    document.insertString(location, menu + ";", null);
                } else {
                    document.insertString(location, menu, null);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
        return item;
    }

    /**
     * @return 補完候補として表示させる文字列
     */
    @Override
    public String toString() {
        return getCompletion();
    }
}
