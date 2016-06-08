package completion;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToolTip;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * ポップアップメニューに表示される補完候補の1アイテムのデザイン管理
 */
class CompletionCellRenderer extends JLabel implements ListCellRenderer {

    CompletionCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        CompletionItem data = (CompletionItem) value;
        setText(data.getCompletion());
        setIcon(data.getTypeIcon());
        setToolTipText(data.getDescription("en")); //ToDo 説明文の日本語対応できたらエディターの設定から言語を取得する

        if (isSelected) {
            setBackground(new Color(179, 216, 253));
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }

    @Override
    public JToolTip createToolTip() {
        return new DescriptionToolTip();
    }

    private class DescriptionToolTip extends JToolTip {
        private DescriptionToolTip() {
            setFocusable(true);
        }
    }
}