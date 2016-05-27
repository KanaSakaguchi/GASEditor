package editor;

import data.CompletionData;
import data.Data;

import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * GAS編集用エディタ
 */
class GASEditor extends JEditorPane {
    GASEditor() {
        addDotListener();
    }

    /**
     * .が入力されたらコード補完用ポップアップを表示
     */
    private void addDotListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                if (event.getKeyChar() == '.') {
                    String keyword = getKeyword(getText().length() - 1);
                    showCompletionPopup(keyword);
                }
            }
        });
    }

    /**
     * コード補完用ポップアップメニューを作成
     *
     * @param keyword メニュー項目構築に用いるキーワード
     */
    private void showCompletionPopup(String keyword) {
        Data[] completions = CompletionData.getInstance().getCompletions(keyword);
        if (completions.length == 0) {
            return;
        }
        JPopupMenu completionPopup = new JPopupMenu();
        for (Data completion : completions) {
            JMenuItem item = new JMenuItem(completion.getCompletion());
            item.addActionListener(event -> {
                String menu = ((JMenuItem) event.getSource()).getText();
                try {
                    if (completion.getReturnClassName().equals("void")) {
                        getDocument().insertString(getText().length(), menu + ";", null);
                    } else {
                        getDocument().insertString(getText().length(), menu, null);
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
            completionPopup.add(item);
        }
        Point caretPosition = getCaret().getMagicCaretPosition();
        completionPopup.show(this, caretPosition.x, caretPosition.y + 20);
    }

    /**
     * 指定した位置までの文字列を取り出す
     *
     * @param end 終了位置
     * @return 指定した位置までの文字列
     * @throws BadLocationException 指定位置が存在しない場合
     */
    private String getText(int end) throws BadLocationException {
        return getText(0, end);
    }

    /**
     * 指定した範囲の文字列を取り出す
     *
     * @param start 開始位置
     * @param end   終了位置
     * @return 指定した範囲の文字列
     * @throws BadLocationException 指定位置が存在しない場合
     */
    @Override
    public String getText(int start, int end) throws BadLocationException {
        int len = end - start;
        return super.getText(start, len);
    }

    /**
     * .の前のキーワードを取り出す
     *
     * @param dotPos .の位置
     * @return キーワード 取得に失敗したら空文字
     */
    private String getKeyword(int dotPos) {
        try {
            //入力された.より前の文字を取得
            String strBeforePos = getText(dotPos);

            //先頭・改行コード・スペース・タブ・.の中で一番dotPosに近い位置が開始位置
            int start = 0;
            if (strBeforePos.lastIndexOf("\n") + 1 > start) {
                start = strBeforePos.lastIndexOf("\n") + 1;
            }
            if (strBeforePos.lastIndexOf(" ") + 1 > start) {
                start = strBeforePos.lastIndexOf(" ") + 1;
            }
            if (strBeforePos.lastIndexOf("\t") + 1 > start) {
                start = strBeforePos.lastIndexOf("\t") + 1;
            }
            if (strBeforePos.lastIndexOf(".") + 1 > start) {
                start = strBeforePos.lastIndexOf(".") + 1;
            }

            //dotPos前と(一番dotPosから遠い位置が終了位置
            int end = dotPos;
            if (strBeforePos.substring(start).contains("(")) {
                end = start + strBeforePos.substring(start).indexOf("(");
            }

            //その範囲がキーワード
            return getText(start, end);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        //取得に失敗したら空文字
        return "";
    }
}
