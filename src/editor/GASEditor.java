package editor;

import item.CompletionData;
import item.CompletionItem;
import item.CompletionsPopup;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import java.awt.Rectangle;
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
                    try {
                        String keyword = getKeyword(getText().length() - 1);
                        CompletionItem[] item = CompletionData.getInstance().getCompletions(keyword);
                        JComboBox<CompletionItem> completions = new JComboBox<>(item);
                        CompletionsPopup popup = new CompletionsPopup(completions, getDocument(), getCaretPosition());
                        Rectangle rect = modelToView(getCaretPosition());
                        popup.show(GASEditor.this, rect.x, rect.y + rect.height);
                        requestFocusInWindow();
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

            //startからdotPosの範囲がキーワード
            return getText(start, dotPos);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        //取得に失敗したら空文字
        return "";
    }
}
