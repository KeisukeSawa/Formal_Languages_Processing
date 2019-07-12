import java.util.Scanner;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

class Calculator{

    // Loolahead()で利用する変数
    // tokenを保存する変数
    static String token;
    // トークンリストから次のトークンを取り出すためのindex
    static int index = 2;
    // トークンを格納するリスト
    static List<String> element = new ArrayList<String>();


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

        // 



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
         
         /*
        // 要素の個数だけ繰り返す
        for(int i = 0; i < element.size(); i++){
            System.out.println(element.get(i));
        }
        */

        // 1番目のtokenをtokenに代入
        Loolahead();


        



    }

    /* 
     関数名：Loolahead
     役割：staticフィールド変数であるtokenにトークンリストelementから次のトークンを代入する
     */
    static void Loolahead(){

        // 次のトークンを代入
        token = element.get(index);

        // indexを1増やす
        index++;
    }

    static void Expression(){

    }

    static void Term(){

    }

    static void Factor(){

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