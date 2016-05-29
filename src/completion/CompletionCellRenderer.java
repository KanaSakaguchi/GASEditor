package completion;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;

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