package completion;

import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CompletionsPopup extends BasicComboPopup {
    private MouseAdapter mouseAdp;
    private KeyAdapter keyAdp;

    private final Document document;
    private final int position;

    public CompletionsPopup(JComboBox completions, Document document, int position) {
        super(completions);
        this.document = document;
        this.position = position;
    }

    /**
     * 初期設定
     * ポップアップの挙動を設定
     */
    @Override
    protected void installListListeners() {
        super.installListListeners();
        installMouseListener();
        installKeyListener();
    }

    /**
     * マウスに対する挙動を設定
     */
    private void installMouseListener() {
        mouseAdp = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                insertCompletion();
            }
        };
        list.addMouseListener(mouseAdp);
    }

    /**
     * キーボードに対する挙動を設定
     */
    private void installKeyListener() {
        keyAdp = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                int index = comboBox.getSelectedIndex();
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (index == 0) {
                            comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
                        } else {
                            comboBox.setSelectedIndex(index - 1);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (index == comboBox.getItemCount() - 1) {
                            comboBox.setSelectedIndex(0);
                        } else {
                            comboBox.setSelectedIndex(index + 1);
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                        insertCompletion();
                        break;
                }
            }
        };
        addKeyListener(keyAdp);
    }

    /**
     * 選択された補完候補を挿入
     */
    private void insertCompletion() {
        hide();
        CompletionItem item = (CompletionItem) comboBox.getSelectedItem();
        String completion = item.getCompletion();
        if (item.getReturnClassName().equals("void")) {
            completion += ";";
        }
        try {
            document.insertString(position, completion, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 後片付け
     * 各種リスナーの解放
     */
    @Override
    public void uninstallingUI() {
        list.removeMouseListener(mouseAdp);
        mouseAdp = null;
        list.removeKeyListener(keyAdp);
        keyAdp = null;
        super.uninstallingUI();
    }

    /**
     * これを書かないとキーボードで操作できない
     *
     * @return フォーカスを当てる
     */
    @Override
    public boolean isFocusable() {
        return true;
    }
}