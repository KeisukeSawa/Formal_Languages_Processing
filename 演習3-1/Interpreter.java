import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

public class Interpreter {

    // 事前に決められている演算子などの語を配列にまとめる
    // 予約語配列
    String[] reserved_word = { "input", "print", "end", "print" };
    // 演算子配列
    String[] operator = { "+", "-", "*", "/", "=" };

    /*
     * 入力されたプログラムの種類を格納する変数 1: input文 2: print文 3: end文 4: 代入文 5:エラー
     */
    static int process_type = 0;

    public static void main(String[] args) {

        // 文字の読み込みのために、Scannerクラスのインスタンス生成
        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.printf("%s ", ">");

            // 入力したプログラムをString型のinputProgramに読み込む
            String inputProgram = scan.nextLine();

            // 字句解析＋構文解析
            TokenAnalysis(inputProgram);

            System.out.println(process_type);

            // process_typeが3か5なら、While文を抜け、プログラム終了
            if (process_type == 3 || process_type == 5) {
                break;
            }

            // System.out.println(inputProgram);

        }

        // Scannerクラスのインスタンスをクローズ
        scan.close();

    }

    public static void TokenAnalysis(String text) {

        // 字句解析

        // 入力した文字列textを要素ごとに分割
        List<String> token = Row_split(text);

        String element = token.get(0);

        // プログラムの最初の要素のprocess_typeの設定
        switch (element) {
        case "input":
            process_type = 1; // input文
            break;
        case "print":
            process_type = 2; // print文
            break;
        case "end":
            process_type = 3; // end文
            break;
        default:
            // 代入文
            // 文頭が＜名前＞であるかどうか確認
            if (CheakName(element)) {
                process_type = 4;
            } else {
                process_type = 5;
                return;
            }

        }

        // 構文解析

        // process_type = 1のとき、次のtokenが＜名前＞であることを確認
        if (process_type == 1) {
            element = token.get(1);
            if(!CheakName(element)){
                process_type = 5;
                return;
            }
        }

        // process_type = 4のとき、次のtokenが"="であることを確認
        if (process_type == 4) {
            element = token.get(1);
            if(!element.equals("=")){
                System.out.println("Error : input文の記述法が違います。＜名前＞=＜式＞の形で記述してください。");
                process_type = 5;
                return;
            }
        }

    }

    /*
     * 関数名：CheakName 引数：String text 戻り値：Boolean
     * 役割：textが＜名前＞の形式（文頭が英字かつ英数字のみで構成されている）であるならばtrue、そうでなければfalseを戻り値として返す
     */
    static Boolean CheakName(String text) {

        if (Pattern.matches("^[a-zA-Z]+$", text.substring(0, 1))) {
            if(!Pattern.matches("^[0-9a-zA-Z]+$", text)){
                System.out.println("Error : ＜名前＞の形式が違います。英数字のみを使用してください。");
                return false;
            }
        } else {
            System.out.println("Error : ＜名前＞の文頭には英字を使用してください。");
            return false;
        }

        return true;

    }

    /*
     * 関数名：Row_split 引数：String row 戻り値：String[]
     * 役割：rowを要素ごとに分割し、要素ごとにString型の配列に格納し、返り値として返す。
     */
    static List<String> Row_split(String row) {

        // rowの中身を要素ごとに格納する配列
        List<String> element = new ArrayList<String>();

        // rowの空白、制御文字の削除
        row = row.trim();
        // 文字の間の半角スペースを削除
        // row = row.replaceAll(" ", "");

        // rowから要素を取り出す際のrowの左側、右側の添字
        int left = 0, right = 0;

        // rowの長さだけ繰り返す
        for (int i = 0; i <= row.length(); i++) {
            // rowの最後の文字に来たら実行
            if (right == row.length()) {
                // cutにrowのleft番目の文字からright番目の文字を切り出して代入
                String cut = row.substring(left, right);
                // cutの長さが0でない（cutが空文字でなければ）実行
                if (cut.length() != 0) {
                    // elementにcutを代入
                    element.add(cut);
                }
            }
            // rowのi番目の文字が ' ' であれば実行する（これらの文字は無視）
            else if (row.charAt(i) == ' ') {
                String cut = row.substring(left, right);
                if (cut.length() != 0) {
                    element.add(cut);
                }
                // left,rightをi+1に設定
                left = i + 1;
                right = i + 1;
            }
            // rowのi番目の文字が ')' '+' '-' '*' '/'であれば実行
            else if ((row.charAt(i) == ' ') || (row.charAt(i) == ')') || (row.charAt(i) == '+')
                    || (row.charAt(i) == '-') || (row.charAt(i) == '*') || (row.charAt(i) == '/')
                    || (row.charAt(i) == '=')) {
                String cut = row.substring(left, right);
                if (cut.length() != 0) {
                    element.add(cut);
                }
                element.add(String.valueOf(row.charAt(i)));
                // left,rightをi+1に設定
                left = i + 1;
                right = i + 1;
            }
            // rowのi番目の文字が '(' であれば実行
            else if (row.charAt(i) == '(') {

                element.add(String.valueOf(row.charAt(i)));

                left = i + 1;
                right = i + 1;
            }
            // その他（数字、文字）であれば実行
            else {
                // rightを1増やす
                right++;
            }
        }
        // elementを返す
        return element;
    }

}