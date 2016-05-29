package item;

import org.json.JSONArray;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 補完候補管理
 */
public class CompletionData {
    private static CompletionData instance = new CompletionData();

    private List<CompletionItem> completionData;

    public static CompletionData getInstance() {
        return instance;
    }

    private CompletionData() {
        completionData = new ArrayList<>();
        importCsvFile();
    }

    /**
     * カレントディレクトリにあるmethodList.csvを読み込んでリストに追加
     */
    private void importCsvFile() {
        String path = "methodList.json";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            final String[] json = {""};
            stream.map(String::toString).forEach(line -> json[0] += line);
            JSONArray jsonArray = new JSONArray(json[0]);
            for (int n = 0; n < jsonArray.length(); n++) {
                completionData.add(new CompletionItem(jsonArray.getJSONObject(n)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, path + "が読み込めませんでした。\nプログラムを終了します。", "GASEditor", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * 入力補完候補を取得
     *
     * @param keyword 補完候補検索に使うキーワード
     * @return 入力補完候補
     */
    public JComboBox<CompletionItem> getCompletions(String keyword) {
        JComboBox<CompletionItem> completions;
        if (keyword.contains("(")) {
            completions = new JComboBox<>(getCompretionsByMethod(keyword));
        } else {
            completions = new JComboBox<>(getCompletionsByClassOrProperty(keyword));
        }
        completions.setRenderer(new CompletionCellRenderer());
        return completions;
    }

    /**
     * クラス名から補完候補を取得
     *
     * @param className クラス名
     * @return 補完候補
     */
    private CompletionItem[] getCompletionsByClassOrProperty(String className) {
        List<CompletionItem> completions = completionData.stream().filter(item -> className.equals(item.getClassName())).collect(Collectors.toList());
        return completions.toArray(new CompletionItem[0]);
    }

    /**
     * メソッドから補完候補を取得
     *
     * @param methodName メソッド
     * @return 補完候補
     */
    private CompletionItem[] getCompretionsByMethod(String methodName) {
        for (CompletionItem item : completionData) {
            //FixMe このメソッド判定はいけてない
            int paramCount = 0;
            if (methodName.indexOf(")") - methodName.indexOf("(") > 1) {
                paramCount = methodName.split(",").length;
            }
            if (item.getCompletion().startsWith(methodName.substring(0, methodName.indexOf("(") + 1))
                    && item.getParamCount() == paramCount) {
                return getCompletionsByClassOrProperty(item.getReturnClassName());
            }
        }
        return new CompletionItem[0];
    }
}
