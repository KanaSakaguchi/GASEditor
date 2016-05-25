package data;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 補完候補管理
 */
public class CompletionData {
    private static CompletionData instance = new CompletionData();

    private List<Data> completionData;

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
        String path = "methodList.csv";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.map(String::toString).forEach(line -> {
                String[] data = line.split("#");
                completionData.add(new Data(data[0], data[1], data[2]));
            });
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, path + "が読み込めませんでした。\nプログラムを終了します。", "GASEditor", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * 入力補完候補を取得
     *
     * @param className クラス名
     * @return 入力補完候補
     */
    public Data[] getCompletions(String className) {
        ArrayList<Data> completions = new ArrayList<>();
        for (Data data : completionData) {
            if (className.equals(data.className)) {
                completions.add(data);
            }
        }
        return completions.toArray(new Data[0]);
    }
}
