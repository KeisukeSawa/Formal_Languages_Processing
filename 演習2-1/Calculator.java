import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

class Calculator{

    // Loolahead()で利用する変数
    // tokenを保存する変数
    static String token;
    // トークンリストから次のトークンを取り出すためのindex
    static int index = 2;
    // トークンを格納するリスト
    static List<String> element = new ArrayList<String>();

    // 逆ポーランドの出力を格納するリスト
    static List<String> reversePolandResult = new ArrayList<String>();

    public static void main(String[] args){

        // 文字の読み込みのために、Scannerクラスのインスタンス生成
        Scanner scan = new Scanner(System.in);

        // メッセージの出力
        System.out.println("式を入力してください");
        System.out.println("入力形式：'変数名' = '式' 　この形式でなければ受理しません。" );
        System.out.println("入力例：result = (987-(654/327)*123)+456");

        // 入力した式をString型のformulaに読み込む
        String formula = scan.nextLine();

        // Scannerクラスのインスタンスをクローズ
        scan.close();

        // formulaを逆ポーランド記法に変換し、ファイル出力
        ReversePoland(formula);

        // 式の答えを求める
        Solve();

    }

    /*
        関数名：Solve
        引数：List<String> reversePolandResult
        役割：逆ポーランド形式に変換した式を実際に計算し、答えを求め出力する
     */
    static void Solve(){

        // スタックを作成する（Dequeを利用）
        Deque<Integer> stack = new ArrayDeque<Integer>();

        int a,b;

        for(int i=0; i<reversePolandResult.size(); i++){

            switch (reversePolandResult.get(i)) {
                case "+":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b+a);
                    break;
                case "-":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b-a);
                    break;
                case "*":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b*a);
                    break;
                case "/":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b/a);
                    break;
                default:
                    stack.push(Integer.parseInt(reversePolandResult.get(i)));
            }
        }

        System.out.printf("%s %s %d\n",element.get(0),element.get(1),stack.pop());

    }

    /* 
       関数名：ReversePoland
     　引数：String formula
       役割：与えられた式formulaを逆ポーランド記法に変換し、reversePoland.txtにその結果を出力する
     */
    static void ReversePoland(String formula){

        element = Row_split(formula);
        
        /* elementの最初の2要素は、'文字列（変数名）','='であると仮定
           elementの最初の2要素については、逆ポーランドの対象としない。
         */ 

        // 初めのtokenをtokenに代入
        Loolahead();

        // 構文解析開始
        Expression();

        // reversePoland.txtに結果を出力
        try{
            File file = new File("reversePoland.txt");
            FileWriter filewriter = new FileWriter(file);

            for(int i=0; i<reversePolandResult.size();i++){
                
              filewriter.write(reversePolandResult.get(i));
              filewriter.write(" ");

            }
            
            filewriter.close();

        }catch(IOException e){
            System.out.println(e);
        }
        
    }

    /* 
     関数名：Loolahead
     役割：staticフィールド変数であるtokenにトークンリストelementから次のトークンを代入する
     */
    static void Loolahead(){

        if(index == element.size()){
            token = "end";
        }
        else{
            // 次のトークンを代入
            token = element.get(index);

            // indexを1増やす
            index++;
        }
    }

    /*
      関数名：Expression
    　役割：文法Expressionを実現
            Expression -> term (patern 1)
            Expression -> term '+' term (patern 2)
            Expression -> term '-' term (patern 3)
     */
    static void Expression(){

        // termを実行
        Term();

        while(true){

            // 出力対象のトークンをoutput_expressionに代入
            String output_expression = token;

            // tokenが'+'および'-'なら実行
            if(token.equals("+") || token.equals("-")){

                // 次のトークンをtokenに代入
                Loolahead();
                // Termを実行
                Term();

                reversePolandResult.add(output_expression);
                System.out.println(output_expression);

            }
            else{
                return;
            }

        }

    }

    /*
      関数名：Term
    　役割：文法termを実現
            term -> factor (patern 1)
            term -> factor '*' factor (patern 2)
            term -> factor '/' factor (patern 3)
     */
    static void Term(){

        // Factorを実行
        Factor();

        while(true){

            // 出力対象のトークンをoutput_termに代入
            String output_term = token;


            // tokenが'*'および'/'なら実行
            if(token.equals("*") || token.equals("/")){

                // 次のトークンをtokenに代入
                Loolahead();
                // Termを実行
                Factor();

                reversePolandResult.add(output_term);
                System.out.println(output_term);

            }
            else{
                return;
            }

        }
        
        
    }

    /*
      関数名：Factor
    　役割：文法factorを実現
            factor -> const(定数) (patern 1)
            factor -> '(' expression ')' (patern 2)
     */
    static void Factor(){

        // 出力対象のトークンをoutput_termに代入
        String output_factor = token;

        // tokenが'*'および'/'なら実行
        if(token.equals("(")){

            // 次のトークンをtokenに代入
            Loolahead();

            // Expressionを実行
            Expression();

            if(!token.equals(")")){
                System.out.println(" ')' missing");
            }

            // 次のトークンをtokenに代入
            Loolahead();

        }
        else{

            // CONSTであると判断
            reversePolandResult.add(output_factor);
            System.out.println(output_factor);

            // 次のトークンをtokenに代入
            Loolahead();

        }
    }

    /* 
    関数名：Row_split
    引数：String row
    戻り値：String[]
    役割：rowを要素ごとに分割し、要素ごとにString型の配列に格納し、返り値として返す。
         その際、区切り文字のコンマ, セミコロン; ブロックを表す curly blacket　( {　と } )および 括弧 ( ) は読み飛ばす。　
    */
    static List<String> Row_split(String row){

        // rowの中身を要素ごとに格納する配列
        List<String> element = new ArrayList<String>();

        // rowの空白、制御文字の削除
        row = row.trim();
        // 文字の間の半角スペースを削除
        row = row.replaceAll(" ", "");

        // rowから要素を取り出す際のrowの左側、右側の添字
        int left = 0, right = 0;

        // rowの長さだけ繰り返す
        for(int i=0; i<=row.length(); i++){
            // rowの最後の文字に来たら実行
            if(right == row.length() ){
                // cutにrowのleft番目の文字からright番目の文字を切り出して代入
                String cut = row.substring(left, right);
                // cutの長さが0でない（cutが空文字でなければ）実行
                if(cut.length() != 0){
                    // elementにcutを代入
                    element.add(cut);
                }
            }
            // rowのi番目の文字が ')' '+' '-' '*' '/'であれば実行
            else if( (row.charAt(i) == ')') || (row.charAt(i) == '+') || (row.charAt(i) == '-') || (row.charAt(i) == '*') || (row.charAt(i) == '/') || (row.charAt(i) == '=') ){
                String cut = row.substring(left, right);
                if(cut.length() != 0){
                    element.add(cut);
                }
                element.add(String.valueOf(row.charAt(i)));
                // left,rightをi+1に設定
                left = i+1;
                right = i+1;
            }
            // rowのi番目の文字が '(' であれば実行
            else if ( row.charAt(i) == '(' ){

                element.add(String.valueOf(row.charAt(i)));

                left = i+1;
                right = i+1;
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