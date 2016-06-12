package editor;

import completion.CompletionData;
import completion.CompletionItem;
import completion.CompletionsPopup;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * GAS編集用エディタ
 */
class GASEditor extends JEditorPane {
    GASEditor() {
        addKeyListener(new KeyAdp());
    }

    private class KeyAdp extends KeyAdapter {
        UndoManager undoManager = new UndoManager();

        KeyAdp() {
            getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
        }

        @Override
        public void keyTyped(KeyEvent event) {
            if (event.getKeyChar() == '.') { //.が入力されたらコード補完用ポップアップを表示
                try {
                    String keyword = getKeyword(getCaretPosition());
                    JComboBox<CompletionItem> completions = CompletionData.getInstance().getCompletions(keyword);
                    if (completions.getItemCount() > 0) {
                        CompletionsPopup popup = new CompletionsPopup(completions, getDocument(), getCaretPosition());
                        Rectangle rect = modelToView(getCaretPosition());
                        popup.show(GASEditor.this, rect.x, rect.y + rect.height);
                        requestFocusInWindow();
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.isControlDown()) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_Z:
                        if (undoManager.canUndo()) {
                            undoManager.undo();
                            event.consume();
                        }
                        break;
                    case KeyEvent.VK_Y:
                        if (undoManager.canRedo()) {
                            undoManager.redo();
                            event.consume();
                        }
                        break;
                }
            } else if (event.isMetaDown() && event.getKeyCode() == KeyEvent.VK_Z) {
                if (event.isShiftDown()) {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                        event.consume();
                    }
                } else if (undoManager.canUndo()) {
                    undoManager.undo();
                    event.consume();
                }
            }
        }
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
