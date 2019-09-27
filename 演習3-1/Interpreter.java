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

    // 文字の読み込みのために、Scannerクラスのインスタンス生成
    public static Scanner scan = new Scanner(System.in);

    // 名前を格納するリスト
    static List<String> name_list = new ArrayList<String>();

    // 名前に紐づけられている値を格納するリスト
    static List<String> name_value_list = new ArrayList<String>();

    // 式のトークンを格納するリスト
    static List<String> term_token = new ArrayList<String>();

    // 逆ポーランドの出力を格納するリスト
    static List<String> reversePolandResult = new ArrayList<String>();

    // tokenを保存する変数
    static String token;

    // トークンリストから次のトークンを取り出すためのindex
    static int index = 2;

    // <式>の計算結果を保持する変数
    static int formula_result;

    public static void main(String[] args) {

        while (true) {
            System.out.printf("%s ", ">");
            // 入力したプログラムをString型のinputProgramに読み込む
            String inputProgram = scan.nextLine();
            // 字句解析＋構文解析
            TokenAnalysis(inputProgram);
            // process_typeが3か5なら、While文を抜け、プログラム終了
            if (process_type == 3 || process_type == 5) {
                break;
            }
        }

        // Scannerクラスのインスタンスをクローズ
        scan.close();
    }

    public static void TokenAnalysis(String text) {

        // 字句解析
        // 入力した文字列textを要素ごとに分割
        List<String> token = Row_split(text);

        // プログラムの最初の要素を格納する変数element
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
        // その後、input処理を行うために、Input()を実行
        if (process_type == 1) {
            element = token.get(1);
            if (!CheakName(element)) {
                process_type = 5;
                return;
            }
            Input(element);
        }

        // process_type = 4のとき、次のtokenが"="であることを確認
        // その後、代入文の処理を行うためにAssignment()を実行
        if (process_type == 4) {
            element = token.get(1);
            if (!element.equals("=")) {
                System.out.println("Error : input文の記述法が違います。＜名前＞=＜式＞の形で記述してください。");
                process_type = 5;
                return;
            }
            Assignment(token);
        }

        // process_type = 2のとき、print文の処理を行うためにPrint()を実行
        if (process_type == 2) {
            Print(token);
        }
    }

    /*
     * 関数名：Input 引数：String name 戻り値：なし
     * 役割：input処理を行うメソッド、キーボード入力を待ち、入力された数を名前nameに割り付ける
     */
    static void Input(String name) {

        // 入力したプログラムをString型のinputProgramに読み込む
        String value = scan.nextLine();

        // 入力された文字が数字であるかどうかを判定
        if (!Pattern.matches("^[0-9]+$", value)) {
            System.out.println("Error : 数字を入力してください。");
            process_type = 5;
            return;
        } else {
            int index = CheakName_list(name);
            // nameが定義されていなかった場合
            if (index == -1) {
                // それぞれ「名前リスト」と「値リスト」に値を代入
                name_list.add(name);
                name_value_list.add(value);
            } else {
                // 「値リスト」に値を代入
                name_value_list.set(index, value);
            }
        }
    }

    /*
     * 関数名：Assignment 引数：List<String> 戻り値：なし
     * 役割：代入文処理を行うメソッド、右辺の＜式＞を評価して値を求め、求めた値を左辺の＜名前＞に割り付ける
     */
    static void Assignment(List<String> program) {

        index = 2;

        // <名前> = <数字>の場合
        if (program.size() == 3) {
            int index = CheakName_list(program.get(0));
            // nameが定義されていなかった場合
            if (index == -1) {
                // それぞれ「名前リスト」と「値リスト」に値を代入
                name_list.add(program.get(0));
                name_value_list.add(program.get(2));
            } else {
                // 「値リスト」に値を代入
                name_value_list.set(index, program.get(2));
            }
        } else {

            // formulaを逆ポーランド記法に変換し、ファイル出力
            ReversePoland(program);
            // 式の答えを求める
            Solve();
            int index = CheakName_list(program.get(0));
            // nameが定義されていなかった場合
            if (index == -1) {
                // それぞれ「名前リスト」と「値リスト」に値を代入
                name_list.add(program.get(0));
                name_value_list.add(String.valueOf(formula_result));
            } else {
                // 「値リスト」に値を代入
                name_value_list.set(index, String.valueOf(formula_result));
            }
        }
    }

    /*
     * 関数名：Print 引数：List<String> 戻り値 なし 
     * 役割：print文の処理を行うメソッド ＜式＞を計算して結果をコンソールに出力する
     */
    static void Print(List<String> program) {

        index = 1;

        // formulaを逆ポーランド記法に変換し、ファイル出力
        ReversePoland(program);
        // 式の答えを求める
        Solve();
        // 結果を出力
        System.out.println(formula_result);

    }

    /*
     * 関数名：CheakName_list 引数:String name 戻り値 int
     * 役割：Name_listの中にすでにnameで与えた＜名前＞が定義されているか調べる。定義されていたらそのindex、定義されていなければ-1を返す
     */
    static int CheakName_list(String name) {

        // nameがすでにname_listに存在しているか調べ、そのindexを返す。
        for (int i = 0; i < name_list.size(); i++) {
            if (name.equals(name_list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    /*
     * 関数名：CheakName 引数：String text 戻り値：Boolean
     * 役割：textが＜名前＞の形式（文頭が英字かつ英数字のみで構成されている）であるならばtrue、そうでなければfalseを戻り値として返す
     */
    static Boolean CheakName(String text) {

        if (Pattern.matches("^[a-zA-Z]+$", text.substring(0, 1))) {
            if (!Pattern.matches("^[0-9a-zA-Z]+$", text)) {
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
     * 関数名：NameToValue() 引数：String<list> 戻り値 なし
     * 役割：＜式＞の中に＜名前＞があった場合に＜名前＞を実際の数値に変換するメソッド
     */
    static void NameToValue(List<String> program) {

        // 式の中に＜名前＞があるかを判定
        for (int i = index; i < program.size(); i++) {
            if (!Pattern.matches("^[0-9]+$", program.get(i))) {
                if (!(program.get(i).equals("+") || program.get(i).equals("-") || program.get(i).equals("/")
                        || program.get(i).equals("*") || program.get(i).equals("(") || program.get(i).equals(")"))) {
                    int name_index = CheakName_list(program.get(i));
                    if (name_index == -1) {
                        System.out.println("Error : ＜式＞には既に定義している＜名前＞を入力してください。");
                        process_type = 5;
                        return;
                    } else {
                        term_token.set(i, String.valueOf(name_value_list.get(name_index)));
                    }
                }
            }
        }
    }

    /*
     * 関数名：Solve 引数：List<String> reversePolandResult
     * 役割：逆ポーランド形式に変換した式を実際に計算し、答えを求め出力する
     */
    static void Solve() {

        // スタックを作成する（Dequeを利用）
        Deque<Integer> stack = new ArrayDeque<Integer>();

        int a, b;

        for (int i = 0; i < reversePolandResult.size(); i++) {

            switch (reversePolandResult.get(i)) {
            case "+":
                a = stack.pop();
                b = stack.pop();
                stack.push(b + a);
                break;
            case "-":
                a = stack.pop();
                b = stack.pop();
                stack.push(b - a);
                break;
            case "*":
                a = stack.pop();
                b = stack.pop();
                stack.push(b * a);
                break;
            case "/":
                a = stack.pop();
                b = stack.pop();
                stack.push(b / a);
                break;
            default:
                stack.push(Integer.parseInt(reversePolandResult.get(i)));
            }
        }

        formula_result = stack.pop();

    }

    /*
     * 関数名：ReversePoland 引数：String formula
     * 役割：与えられた式formulaを逆ポーランド記法に変換し、reversePoland.txtにその結果を出力する
     */
    static void ReversePoland(List<String> formula) {

        term_token = formula;
        // term_tokenに入った＜名前＞を数値に変換する
        NameToValue(term_token);

        // 初めのtokenをtokenに代入
        Loolahead();

        // 構文解析開始
        Expression();

        // reversePoland.txtに結果を出力
        try {
            File file = new File("reversePoland.txt");
            FileWriter filewriter = new FileWriter(file);

            for (int i = 0; i < reversePolandResult.size(); i++) {

                filewriter.write(reversePolandResult.get(i));
                filewriter.write(" ");

            }

            filewriter.close();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /*
     * 関数名：Loolahead 
     * 役割：staticフィールド変数であるtokenにトークンリストterm_tokenから次のトークンを代入する
     */
    static void Loolahead() {

        if (index == term_token.size()) {
            token = "end";
        } else {
            // 次のトークンを代入
            token = term_token.get(index);

            // indexを1増やす
            index++;
        }
    }

    /*
     * 関数名：Expression 
     * 役割：文法Expressionを実現
     *  Expression -> term (patern 1)
     *  Expression -> term '+' term (patern 2) 
     *  Expression -> term '-' term (patern 3)
     */
    static void Expression() {

        // termを実行
        Term();

        while (true) {

            // 出力対象のトークンをoutput_expressionに代入
            String output_expression = token;

            switch (token) {
            case "+":
                // 次のトークンをtokenに代入
                Loolahead();
                // Termを実行
                Term();
                reversePolandResult.add(output_expression);
                break;
            case "-":
                // 次のトークンをtokenに代入
                Loolahead();
                // Termを実行
                Term();
                reversePolandResult.add(output_expression);
                break;
            default:
                return;
            }
        }

    }

    /*
     * 関数名：Term
     * 役割：文法termを実現
     * term -> factor (patern 1) 
     * term -> factor '*' factor(patern 2)
     * term -> factor '/' factor (patern 3)
     */
    static void Term() {

        // Factorを実行
        Factor();

        while (true) {

            // 出力対象のトークンをoutput_termに代入
            String output_term = token;

            switch (token) {
            case "*":
                // 次のトークンをtokenに代入
                Loolahead();
                // Termを実行
                Factor();
                reversePolandResult.add(output_term);
                break;
            case "/":
                // 次のトークンをtokenに代入
                Loolahead();
                // Termを実行
                Factor();
                reversePolandResult.add(output_term);
                break;
            default:
                return;
            }
        }
    }

    /*
     * 関数名：Factor
     * 役割：文法factorを実現 
     * factor -> const(定数) (patern 1) 
     * factor ->'('expression ')' (patern 2)
     */
    static void Factor() {

        // 出力対象のトークンをoutput_termに代入
        String output_factor = token;

        switch (token) {
        case "(":
            // 次のトークンをtokenに代入
            Loolahead();
            // Expressionを実行
            Expression();
            if (!token.equals(")")) {
                System.out.println("Error：')' missing");
            }
            // 次のトークンをtokenに代入
            Loolahead();
            break;
        default:
            // CONSTであると判断
            reversePolandResult.add(output_factor);
            // 次のトークンをtokenに代入
            Loolahead();
        }
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