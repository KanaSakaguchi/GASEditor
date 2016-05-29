package completion;

import javax.swing.JLabel;
import javax.swing.JList;
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

        if (isSelected) {
            setBackground(new Color(179, 216, 253));
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }
}